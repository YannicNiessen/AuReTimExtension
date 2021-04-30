package de.stzeyetrial.auretim.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;

/**
 * @author strasser
 */
public class DialogController implements Initializable {
	public static final String DIALOG_FXML = "/fxml/Dialog.fxml";

	@FXML
	private Node _pane;

	@FXML
	private Label _messageLabel;

	@FXML
	private Label _titleLabel;

	private String _messageTitle;
	private String _errorTitle;

	@FXML
	private void close(final ActionEvent event) {
		_pane.visibleProperty().set(false);
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		_pane.setVisible(false);
		_messageTitle = resources.getString("dialog.message.title");
		_errorTitle = resources.getString("dialog.error.title");
	}

	public void showError(final Throwable t) {
		_titleLabel.setStyle("-fx-background-color: rgba(200, 0, 0, 255); -fx-text-fill: rgba(255, 255, 255, 255);");
		_titleLabel.setText(_errorTitle);
		_messageLabel.setText(t.getMessage());
		_pane.setVisible(true);
	}

	public void showMessage(final String message) {
		_titleLabel.setStyle("-fx-background-color: rgba(163, 163, 163, 255); -fx-text-fill: rgba(255, 255, 255, 255);");
		_titleLabel.setText(_messageTitle);
		_messageLabel.setText(message);
		_pane.setVisible(true);
	}
}