package de.stzeyetrial.auretim.input.mouse;

import de.stzeyetrial.auretim.input.AbstractInputSource;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * @author strasser
 */
public class MouseButtonInput extends AbstractInputSource {
	private static Scene SCENE;
	private static MouseButtonInput INSTANCE;

	private MouseButtonInput(final Scene scene) {
		scene.addEventFilter(MouseEvent.MOUSE_CLICKED, (final MouseEvent event) -> {
			if (MouseButton.PRIMARY == event.getButton() && !event.isSynthesized()) {
				event.consume();
				fire();
			}
		});
	}

	public static void init(final Scene scene) {
		SCENE = scene;
	}

	public static synchronized MouseButtonInput getInstance() throws IllegalStateException {
		if (SCENE == null) {
			throw new IllegalStateException("scene not initialized. Call init(scene) first.");
		} else {
			if (INSTANCE == null) {
				INSTANCE = new MouseButtonInput(SCENE);
			}

			return INSTANCE;
		}
	}
}