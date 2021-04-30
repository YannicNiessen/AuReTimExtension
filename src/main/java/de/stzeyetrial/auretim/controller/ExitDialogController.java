package de.stzeyetrial.auretim.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;

/**
 * @author strasser
 */
public class ExitDialogController implements Initializable {
	public static final String DIALOG_FXML = "/fxml/ExitDialog.fxml";

	@FXML
	private Node _pane;

	@FXML
	private CheckBox _shutdownOnExitCheckbox;

	@FXML
	private void ok(final ActionEvent event) {
		_pane.visibleProperty().set(false);
		Platform.exit();
		System.exit(_shutdownOnExitCheckbox.isSelected() ? 1 : 0);
	}

	@FXML
	private void cancel(final ActionEvent event) {
		_pane.visibleProperty().set(false);
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		_pane.setVisible(false);
	}

	public void confirmExit() {
		_pane.setVisible(true);
	}
}