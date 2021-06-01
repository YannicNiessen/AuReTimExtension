package de.stzeyetrial.auretim.tasks;

import de.stzeyetrial.auretim.util.Result;
import java.util.concurrent.CyclicBarrier;

/**
 * @author strasser
 */
class InputTask extends AbstractInputTask {
	InputTask(final CyclicBarrier gate, final long testStart, final int maximumTime, final int minimumResponseTime) {
		super(gate, testStart, maximumTime, minimumResponseTime);
	}

	@Override
	protected Result evaluate(final long startStep, final long duration, final boolean timeout, final int minimumResponseTime) {
		if (timeout) {
			return new Result(startStep, duration, Result.Type.FALSE_NEGATIVE);
		} else if (duration < minimumResponseTime) {
			return new Result(startStep, duration, Result.Type.FALSE_POSITIVE);
		} else {
			return new Result(startStep, duration, Result.Type.TRUE_POSITIVE);
		}
	}
}