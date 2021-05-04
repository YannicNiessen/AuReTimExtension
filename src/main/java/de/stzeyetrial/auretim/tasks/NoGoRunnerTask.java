package de.stzeyetrial.auretim.tasks;

import de.stzeyetrial.auretim.audio.Tone;
import de.stzeyetrial.auretim.audio.ToneUtils;
import de.stzeyetrial.auretim.util.Result;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;
import javafx.beans.property.IntegerProperty;

/**
 * @author strasser
 */
public class NoGoRunnerTask extends AbstractRunnerTask {
	private final Tone[] _tones = new Tone[2];

	private boolean _positive;

	public NoGoRunnerTask(final List<Result> results, final int frequency, final IntegerProperty volumeProperty, final int pulseDuration, final int minimumResponseTime, final int delay, final int timeout, final int repetitions, boolean visual) {
		super(results, frequency, volumeProperty, pulseDuration, minimumResponseTime, delay, timeout, repetitions, visual);
	}

	@Override
	protected void callImpl() throws Exception {
		final ByteBuffer buffer1 = ToneUtils.createToneBuffer(_pulseDuration, _frequency);
		final ByteBuffer buffer2 = ToneUtils.createToneBuffer(_pulseDuration, _frequency * 2);

		try (final Tone tone1 = Tone.createTone(buffer1); final Tone tone2 = Tone.createTone(buffer2)) {
			_tones[0] = tone1;
			_tones[1] = tone2;

			callImpl2();
		}
	}

	@Override
	protected int getDelay() {
		return _delay;
	}

	@Override
	protected Tone getTone() {
		final int index = ThreadLocalRandom.current().nextInt(2);
		_positive = (index == 0);
		return _tones[index];
	}

	@Override
	protected AbstractInputTask getInputTask(final CyclicBarrier gate, final long testStart, final int maximumTime, final int minimumResponseTime) {
		return new NoGoInputTask(gate, testStart, maximumTime, minimumResponseTime, _positive);
	}
}