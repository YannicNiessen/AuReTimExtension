package de.stzeyetrial.auretim.screens;

import de.stzeyetrial.auretim.controller.DialogController;
import de.stzeyetrial.auretim.controller.ExitDialogController;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

/**
 * @author strasser
 */
public class ScreenManager extends StackPane {
	private static final String BUNDLES_FILE = "bundles.strings";

	private final Map<Screens, Node> _screens = new HashMap<>();
	private final Map<Node, IScreenControlSupport> _controllers = new HashMap<>();

	private final DialogController _dialogController;
	private Node _dialogPane;

	private final ExitDialogController _exitDialogController;
	private Node _exitDialogPane;

	public ScreenManager() {
		for (final Screens screen : Screens.values()) {
			loadScreen(screen);
		}

		final FXMLLoader loader = new FXMLLoader(getClass().getResource(DialogController.DIALOG_FXML));
		loader.setResources(ResourceBundle.getBundle(BUNDLES_FILE));
		try {
			_dialogPane = loader.load();
			getChildren().add(_dialogPane);
		} catch (IOException ex) {
			Logger.getLogger(ScreenManager.class.getName()).log(Level.SEVERE, "Could not create dialog.", ex);
		}
		_dialogController = loader.getController();

		final FXMLLoader loader2 = new FXMLLoader(getClass().getResource(ExitDialogController.DIALOG_FXML));
		loader2.setResources(ResourceBundle.getBundle(BUNDLES_FILE));
		try {
			_exitDialogPane = loader2.load();
			getChildren().add(_exitDialogPane);
		} catch (IOException ex) {
			Logger.getLogger(ScreenManager.class.getName()).log(Level.SEVERE, "Could not create exit dialog.", ex);
		}
		_exitDialogController = loader2.getController();
	}

	private boolean loadScreen(final Screens screen) {
		try {
			final FXMLLoader loader = new FXMLLoader(getClass().getResource(screen.getFile()));
			loader.setResources(ResourceBundle.getBundle(BUNDLES_FILE));

			final Parent screenNode = (Parent) loader.load();
			if (loader.getController() instanceof IScreenControlSupport) {
				final IScreenControlSupport controller = ((IScreenControlSupport) loader.getController());
				controller.setScreenManager(this);
				_controllers.put(screenNode, controller);
			}

			_screens.put(screen, screenNode);

			return true;
		} catch (Exception e) {
			//showException(e);
			e.printStackTrace(System.err);
			return false;
		}
	}

	public void setScreen(final Screens screen) throws IllegalArgumentException {
		final Node target = _screens.get(screen);
		if (target != null) {
			if (getChildren().size() > 2) {
				final Node n = getChildren().remove(0);
				_controllers.get(n).leave();
			}
			getChildren().add(0, target);
			_controllers.get(target).enter();
		} else {
			throw new IllegalArgumentException(String.format("screen %s hasn't been loaded!", screen.toString()));
		}
	}

	public void unloadScreen(final Screens screen) {
		if (_screens.remove(screen) == null) {
			throw new IllegalArgumentException(String.format("screen %s does not exist!", screen.toString()));
		}
	}

	public void showException(final Throwable t) {
		_dialogController.showError(t);
	}

	public void showMessage(final String message) {
		_dialogController.showMessage(message);
	}

	public void showExitConfirmationDialog() {
		_exitDialogController.confirmExit();
	}

	public IScreenControlSupport getController(Screens screen){
		Parent screenNode = (Parent) _screens.get(screen);
		IScreenControlSupport controller = _controllers.get(screenNode);
		return controller;
	}
}