package de.stzeyetrial.auretim.tasks;

import de.stzeyetrial.auretim.audio.Tone;
import de.stzeyetrial.auretim.util.Result;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

/**
 * @author strasser
 */
public class NBackSpeechTask extends MonoNBackSpeechTask {
	private final int _timeout;
	private final int _minimumResponseTime;

	private Tone _tone;

	public NBackSpeechTask(final List<Result> results, final int length, final int nRepeat, final int nMatch, final int nLures, final int nBackLevel, final int timeout, final boolean reUseElements, final int minimumResponseTime, boolean dual) {
		super(results, length,  nRepeat, nMatch, nLures, nBackLevel, timeout, reUseElements, dual);

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

	protected AbstractInputTask getInputTask(CyclicBarrier gate, long testStart, boolean result) {
		return null;
	}

	protected NBackSpeechInputTask getSpeechInputTask(CyclicBarrier gate, long testStart, boolean result, int expectedValue) {
		return new NBackSpeechInputTask(gate, testStart, _timeout, _minimumResponseTime, result, expectedValue);
	}


}