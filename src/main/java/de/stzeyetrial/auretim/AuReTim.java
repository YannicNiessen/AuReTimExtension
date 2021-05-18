package de.stzeyetrial.auretim;

import de.stzeyetrial.auretim.config.Config;
import de.stzeyetrial.auretim.screens.Screens;
import de.stzeyetrial.auretim.screens.ScreenManager;
import static javafx.application.Application.launch;

import com.guigarage.flatterfx.FlatterFX;
import de.stzeyetrial.auretim.input.InputFactory;
import de.stzeyetrial.auretim.tests.InputLatencyTest;
import de.stzeyetrial.auretim.util.Stimulus;
import de.stzeyetrial.auretim.util.StimulusSet;
import javafx.application.Application;
import javafx.application.Preloader.StateChangeNotification;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.LinkedList;

public class AuReTim extends Application {
    @Override
    public void start(final Stage stage) throws Exception {
		notifyPreloader(new StateChangeNotification(StateChangeNotification.Type.BEFORE_START));

		final Config config = Config.getInstance();
		config.load();

		StimulusSet.loadAllSetsFromDisk();


		final ScreenManager manager = new ScreenManager();
		manager.setScreen(Screens.MAIN);

        final Scene scene = new Scene(manager);
		scene.getStylesheets().add(FlatterFX.class.getResource("flatterfx.css").toExternalForm());
		stage.initStyle(StageStyle.UNDECORATED);

		if ("arm".equals(System.getProperty("os.arch"))) {
			scene.setCursor(Cursor.NONE);
			stage.setFullScreen(true);
			stage.setFullScreenExitHint("");
		}

        stage.setScene(scene);

		InputFactory.init(scene);

        stage.show();









    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
		launch(args);
    }
}