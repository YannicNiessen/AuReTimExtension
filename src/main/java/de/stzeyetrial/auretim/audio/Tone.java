package de.stzeyetrial.auretim.audio;

import de.stzeyetrial.auretim.util.ByteBufferBackedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;

/**
 * @author strasser
 */
public final class Tone implements AutoCloseable {
	public static final int SAMPLING_RATE = 8000;
	private static final AudioFormat AUDIO_FORMAT = new AudioFormat(SAMPLING_RATE, Short.SIZE, 1, true, true);

	private final LineListener _listener = new MyLineListener();
	private final Clip _clip;

	private CountDownLatch _latch;

	private Tone(final ByteBuffer buffer) throws LineUnavailableException, IOException {
		_clip = AudioSystem.getClip();

		_clip.open(new AudioInputStream(new ByteBufferBackedInputStream(buffer), AUDIO_FORMAT, buffer.limit()));

		addLineListener(_listener);
	}

	public static Tone createTone(final ByteBuffer buffer) throws LineUnavailableException, IOException {
		return new Tone(buffer);
	}

	public final void play(final int volume) {
		_latch = new CountDownLatch(1);

		final FloatControl gain = (FloatControl) _clip.getControl(FloatControl.Type.MASTER_GAIN);
		gain.setValue((float) volume);

		_clip.setFramePosition(0);
		_clip.start();
		_clip.drain();

		try {
			_latch.await(); // Wait to finish play
		} catch (InterruptedException ex) {
		}
	}

	public final void addLineListener(LineListener listener) {
		_clip.addLineListener(listener);
	}

	public final void removeLineListener(LineListener listener) {
		_clip.removeLineListener(listener);
	}

	@Override
	public final void close() {
		removeLineListener(_listener);
		_clip.close();
	}

	private final class MyLineListener implements LineListener {
		@Override
		public void update(final LineEvent event) {
			if (LineEvent.Type.STOP == event.getType()) {
				_latch.countDown();				
			}
		}
	}
}