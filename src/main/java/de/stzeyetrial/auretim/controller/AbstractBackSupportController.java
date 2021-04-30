package de.stzeyetrial.auretim.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.SwipeEvent;

/**
 * @author strasser
 */
abstract class AbstractBackSupportController extends AbstractController {
	@FXML
	protected void buttonBack(final ActionEvent e) {
		back();
		e.consume();
	}

	@FXML
	protected void swipeBack(final SwipeEvent e) {
		back();
		e.consume();
	}

	protected abstract void back();	
}