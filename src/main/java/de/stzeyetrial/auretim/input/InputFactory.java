package de.stzeyetrial.auretim.input;

import de.stzeyetrial.auretim.config.Config;
import de.stzeyetrial.auretim.input.gpio.GPIOButtonInput;
import de.stzeyetrial.auretim.input.keyboard.KeyboardInput;
import de.stzeyetrial.auretim.input.mouse.MouseButtonInput;
import javafx.scene.Scene;

/**
 * @author strasser
 */
public class InputFactory {
	public static boolean _initialized = false;

	public static synchronized void init(final Scene scene) {
		if (!_initialized) {
			MouseButtonInput.init(scene);
			KeyboardInput.init(scene);
			_initialized = true;
		}
	}

	public static synchronized IInputSource getInstance() throws IllegalStateException {
		switch (Config.getInstance().inputProperty().get()) {
			case MOUSE:
				return MouseButtonInput.getInstance();
			case KEYBOARD:
				return KeyboardInput.getInstance();
			case BUTTON:
				return GPIOButtonInput.getInstance();
			default:
				throw new IllegalStateException("unknown input");
		}
	}
}