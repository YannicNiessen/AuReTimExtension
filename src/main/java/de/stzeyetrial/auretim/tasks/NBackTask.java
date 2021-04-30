package de.stzeyetrial.auretim.tasks;

import de.stzeyetrial.auretim.audio.Tone;
import de.stzeyetrial.auretim.audio.ToneUtils;
import de.stzeyetrial.auretim.util.Result;
import javafx.beans.property.IntegerProperty;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author strasser
 */
public class NBackTask extends AbstractNBackTask {
	private final int _timeout;
	private final int _minimumResponseTime;

	private Tone _tone;

	public NBackTask(final List<Result> results, final int length, final int nOptions, final int nRepeat, final int nMatch, final int nLures, final int nBackLevel, final boolean reUseElements, boolean dual, final int minimumResponseTime, final int timeout) {
		super(results, length, nOptions, nRepeat, nMatch, nLures, nBackLevel, timeout, reUseElements, dual);

		_minimumResponseTime = minimumResponseTime;
		_timeout = timeout;

	}

	@Override
	protected void callImpl() {

		try {
			callImpl2();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected int getDelay() {
		//return ThreadLocalRandom.current().nextInt(_delay, _maximumDelay + 1);
		return 0;
	}

	@Override
	protected Tone getTone() {
		return _tone;
	}

	@Override
	protected AbstractInputTask getInputTask(CyclicBarrier gate, long testStart, boolean result) {
		return new NBackInputTask(gate, testStart, _timeout, _minimumResponseTime, result);
	}


}