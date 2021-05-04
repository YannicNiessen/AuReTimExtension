package de.stzeyetrial.auretim.tasks;

import de.stzeyetrial.auretim.audio.Tone;
import de.stzeyetrial.auretim.audio.ToneUtils;
import de.stzeyetrial.auretim.util.Result;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;

import de.stzeyetrial.auretim.util.Stimulus;
import javafx.beans.property.IntegerProperty;

/**
 * @author strasser
 */
public class RunnerTask extends AbstractRunnerTask {
	private final int _maximumDelay;

	private Tone _tone;

	public RunnerTask(final List<Result> results, final int frequency, final IntegerProperty volumeProperty, final int pulseDuration, final int minimumResponseTime, final int minimumDelay, final int maximumDelay, final int timeout, final int repetitions, boolean visual) {
		super(results, frequency, volumeProperty, pulseDuration, minimumResponseTime, minimumDelay, timeout, repetitions, visual);

		_maximumDelay = maximumDelay;
	}

	@Override
	protected void callImpl() throws Exception {
		final ByteBuffer buffer = ToneUtils.createToneBuffer(_pulseDuration, _frequency);

		try (final Tone tone = Tone.createTone(buffer)) {
			_tone = tone;

			callImpl2();
		}
	}

	@Override
	protected int getDelay() {
		return ThreadLocalRandom.current().nextInt(_delay, _maximumDelay + 1);
	}

	@Override
	protected Tone getTone() {
		return _tone;
	}

	@Override
	protected Stimulus getStimulus() {
		return Stimulus.goStimulus();
	}

	@Override
	protected InputTask getInputTask(final CyclicBarrier gate, final long testStart, final int maximumTime, final int minimumResponseTime) {
		return new InputTask(gate, testStart, maximumTime, minimumResponseTime);
	}	
}