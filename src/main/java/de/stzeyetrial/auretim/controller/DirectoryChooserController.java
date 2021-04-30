package de.stzeyetrial.auretim.controller;

import de.stzeyetrial.auretim.config.Config;
import de.stzeyetrial.auretim.screens.Screens;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author strasser
 */
public class DirectoryChooserController extends AbstractBackSupportController {
	@FXML
	private TextField _selectedDirectory;

	@FXML
	private VBox _content;

	@Override
	public void enter() {
		final String currentDirectory = Config.getInstance().directoryProperty().get();
		final Path currentPath = FileSystems.getDefault().getPath(currentDirectory).toAbsolutePath().getParent();

		init(currentPath);
	}

	@Override
	protected void back() {
		getScreenManager().setScreen(Screens.SETTINGS);
	}

	@FXML
	private void buttonSelect(final ActionEvent e) {
		Config.getInstance().directoryProperty().set(_selectedDirectory.textProperty().get());
		back();
	}

	private void init(final Path currentPath) {
		_content.getChildren().removeAll(_content.getChildren());

		_selectedDirectory.textProperty().set(currentPath.toAbsolutePath().toString());

		try {
			if (currentPath.getParent() != null) {
				final Button tf = createButton("..");
				tf.setOnAction(e -> init(currentPath.getParent()));
				_content.getChildren().add(tf);
			}
			final List<Path> list = new ArrayList<>();
			Files.newDirectoryStream(currentPath, p -> Files.isDirectory(p)).forEach(p -> {
				list.add(p);
			});
			
			list.stream()
				.sorted((Path o1, Path o2) -> o1.getFileName().compareTo(o2.getFileName()))
				.forEach(p -> {
					final Button tf = createButton(p.getFileName().toString());
					tf.setOnAction(e -> init(p));
					_content.getChildren().add(tf);
				});
		} catch (IOException ex) {
			getScreenManager().showException(ex);
		}
	}

	private Button createButton(final String text) {
		final Button tf = new Button(text);
		tf.setPrefSize(205, 20);
		tf.setStyle("-fx-background-color: rgba(0, 0, 0, 0); -fx-text-fill: -dark; -fx-alignment: center-left;");
		tf.setAlignment(Pos.BASELINE_LEFT);
		
		tf.setFocusTraversable(false);
		return tf;
	}
}