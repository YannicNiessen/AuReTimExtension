package de.stzeyetrial.auretim.input;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author strasser
 */
public abstract class AbstractInputSource implements IInputSource {
	private final AtomicReference<IInputCallback> _callbackRef = new AtomicReference<>();

	@Override
	public void setInputCallback(final IInputCallback callback) {
		_callbackRef.set(callback);
	}

	protected void fire() {
		final IInputCallback callback = _callbackRef.getAndSet(null);
		if (callback != null) {
			callback.onInput();
		}
	}
}