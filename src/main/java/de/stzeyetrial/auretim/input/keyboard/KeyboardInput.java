package de.stzeyetrial.auretim.input.keyboard;

import de.stzeyetrial.auretim.input.AbstractInputSource;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * @author strasser
 */
public class KeyboardInput extends AbstractInputSource {
	private static Scene SCENE;
	private static KeyboardInput INSTANCE;

	private KeyboardInput(final Scene scene) {
		scene.addEventFilter(KeyEvent.KEY_PRESSED, (final KeyEvent event) -> {
			if (KeyCode.SPACE == event.getCode()) {
				event.consume();
				fire();
			}
		});
	}

	public static void init(final Scene scene) {
		SCENE = scene;
	}

	public static synchronized KeyboardInput getInstance() throws IllegalStateException {
		if (SCENE == null) {
			throw new IllegalStateException("scene not initialized. Call init(scene) first.");
		} else {
			if (INSTANCE == null) {
				INSTANCE = new KeyboardInput(SCENE);
			}

			return INSTANCE;
		}
	}	
}