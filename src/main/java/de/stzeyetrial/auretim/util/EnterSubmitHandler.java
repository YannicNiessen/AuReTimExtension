package de.stzeyetrial.auretim.util;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * @author strasser
 */
public class EnterSubmitHandler implements EventHandler<KeyEvent> {
	@Override
	public void handle(final KeyEvent event) {
		if (KeyCode.ENTER == event.getCode()) {
			Node node = ((Node) event.getSource()).getParent();
			while (node instanceof ComboBox || node instanceof ChoiceBox) {
				node = (Node) node.getParent();
			}
			node.requestFocus();
		}
	}
}