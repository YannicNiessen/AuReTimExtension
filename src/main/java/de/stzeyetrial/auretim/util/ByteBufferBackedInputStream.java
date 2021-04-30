package de.stzeyetrial.auretim.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * @author strasser
 */
public class ByteBufferBackedInputStream extends InputStream {
    private final ByteBuffer _buffer;

    public ByteBufferBackedInputStream(final ByteBuffer buf) {
        this._buffer = buf;
    }

	@Override
    public int read() throws IOException {
        if (!_buffer.hasRemaining()) {
            return -1;
        }
        return _buffer.get() & 0xFF;
    }

	@Override
    public int read(final byte[] bytes, final int off, final int len) throws IOException {
        if (!_buffer.hasRemaining()) {
            return -1;
        }

        final int length = Math.min(len, _buffer.remaining());
        _buffer.get(bytes, off, length);

        return length;
    }
}