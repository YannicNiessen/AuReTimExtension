package de.stzeyetrial.auretim;

import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Preloader.ProgressNotification;
import javafx.application.Preloader.StateChangeNotification;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Simple Preloader Using the ProgressBar Control
 *
 * @author strasser
 */
public class Preloader extends javafx.application.Preloader {
	private static final String LOGO = "/stz-eyetrial.png";

	private Stage _stage;
	
	private Scene createPreloaderScene() {
		final BorderPane p = new BorderPane();
		p.setStyle("-fx-background-color: rgba(255, 255, 255, 255);");

		try {
			p.setCenter(new ImageView(getClass().getResource(LOGO).toURI().toString()));
		} catch (URISyntaxException ex) {
			Logger.getLogger(Preloader.class.getName()).log(Level.WARNING, "image not found", ex);
		}


		//p.setBottom(new ProgressIndicator(ProgressIndicator.INDETERMINATE_PROGRESS));
		return new Scene(p, 320, 480);		
	}
	
	@Override
	public void start(final Stage stage) throws Exception {
		_stage = stage;
		stage.setScene(createPreloaderScene());		
		stage.show();
	}
	
	@Override
	public void handleStateChangeNotification(final StateChangeNotification scn) {
		if (scn.getType() == StateChangeNotification.Type.BEFORE_START) {
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException ex) {
//			}
			_stage.hide();
		}
	}

	@Override
	public void handleProgressNotification(final ProgressNotification pn) {
		//_progress.setProgress(pn.getProgress());
	}		
}