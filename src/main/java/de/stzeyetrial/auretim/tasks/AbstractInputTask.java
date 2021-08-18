package de.stzeyetrial.auretim.tasks;

import de.stzeyetrial.auretim.input.InputFactory;
import de.stzeyetrial.auretim.output.ITrigger;
import de.stzeyetrial.auretim.output.TriggerFactory;
import de.stzeyetrial.auretim.output.TriggerType;
import de.stzeyetrial.auretim.util.Result;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * @author strasser
 */
abstract class AbstractInputTask implements Callable<Result> {
	private final ITrigger _trigger = TriggerFactory.getInstance().createTrigger();

	private final CountDownLatch _latch;
	private final CyclicBarrier _gate;
	private final long _testStart;
	private final int _minimumResponseTime;
	private final int _maximumTime;

	AbstractInputTask(final CyclicBarrier gate, final long testStart, final int maximumTime, final int minimumResponseTime) {
		_gate = gate;
		_testStart = testStart;
		_maximumTime = maximumTime;
		_minimumResponseTime = minimumResponseTime;

		_latch = new CountDownLatch(1);
		InputFactory.getInstance().setInputCallback(() -> _latch.countDown());
	}

	@Override
	public final Result call() throws Exception {

		_gate.await();


		final long start = System.currentTimeMillis();
		final boolean timeout = !_latch.await(_maximumTime, TimeUnit.MILLISECONDS);
		final long now = System.currentTimeMillis();

		final long startStep = start - _testStart;
		final long duration = now - start;

		if (!timeout) {
			_trigger.trigger(TriggerType.RESPONSE);
		}

		return evaluate(startStep, duration, timeout, _minimumResponseTime);
	}

	protected abstract Result evaluate(final long startStep, final long duration, final boolean timeout, final int minimumResponseTime);
}