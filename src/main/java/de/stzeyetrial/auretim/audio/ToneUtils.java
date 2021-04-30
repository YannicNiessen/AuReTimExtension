package de.stzeyetrial.auretim.audio;

import java.nio.ByteBuffer;

/**
 * @author strasser
 */
public final class ToneUtils {
	private ToneUtils() {
	}

	public static ByteBuffer createToneBuffer(final int pulseDuration, final int frequency) {
		final int length = (int) Math.round(Math.round(pulseDuration * frequency / 1000d) * 1000d / frequency * Tone.SAMPLING_RATE / 1000d);

		final double term = 2d * Math.PI * frequency / Tone.SAMPLING_RATE;
		final ByteBuffer buffer = ByteBuffer.allocate(length * Short.SIZE / Byte.SIZE); // 16 bit, short!
		for (int i = 0; i < length; i++) {
			buffer.putShort((short) (Short.MAX_VALUE * Math.sin(term * i)));
		}

		return (ByteBuffer) buffer.rewind();
	}
}