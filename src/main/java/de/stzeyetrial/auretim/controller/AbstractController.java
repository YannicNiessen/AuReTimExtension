package de.stzeyetrial.auretim.controller;

import de.stzeyetrial.auretim.screens.IScreenControlSupport;
import de.stzeyetrial.auretim.screens.ScreenManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * @author strasser
 */
public abstract class AbstractController implements IScreenControlSupport, Initializable {
	private ScreenManager _controller;

	@Override
	public final void setScreenManager(final ScreenManager controller) {
		_controller = controller;
	}

	protected ScreenManager getScreenManager() {
		return _controller;
	}

	@Override
	public void enter() {
	}

	@Override
	public void leave() {
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
	}
}