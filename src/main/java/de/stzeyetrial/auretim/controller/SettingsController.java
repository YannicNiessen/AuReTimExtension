package de.stzeyetrial.auretim.controller;

import com.sun.glass.ui.PlatformFactory;
import de.stzeyetrial.auretim.audio.SpeechSynthesizer;
import de.stzeyetrial.auretim.audio.Tone;
import de.stzeyetrial.auretim.audio.ToneUtils;
import de.stzeyetrial.auretim.config.Config;
import de.stzeyetrial.auretim.config.ConfigMeta;
import de.stzeyetrial.auretim.controller.nBack.*;
import de.stzeyetrial.auretim.input.Input;
import de.stzeyetrial.auretim.input.InputFactory;
import de.stzeyetrial.auretim.input.SpeechDecoder;
import de.stzeyetrial.auretim.screens.Screens;
import de.stzeyetrial.auretim.util.*;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.ByteBuffer;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import javax.sound.sampled.LineUnavailableException;

import javafx.util.Duration;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

/**
 * FXML Controller class
 *
 * @author strasser
 */
public class SettingsController extends AbstractBackSupportController {
	private static final int WAIT_TIME = 10;

	private final ValidationSupport _validation = new ValidationSupport();
	private final ExecutorService _executor = Executors.newSingleThreadExecutor( r -> {
		final Thread t = Executors.defaultThreadFactory().newThread(r);
		t.setDaemon(true);
		return t;
	});

	private ResourceBundle _rb;



	@FXML
	private BorderPane _dynamicContentAnchorPane;

	private double _lastYposition = 0;

	private boolean _positive = true;

	private ScrollPane _scrollPane;

	ComboBox<String> _testSelectionComboBox;

	String _currentPage;



	public SettingsController() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> _executor.shutdown()));
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		_rb = resources;

		final Config config = Config.getInstance();

		_testSelectionComboBox = new ComboBox<>();

		VBox contentAnchor = new VBox();

		_scrollPane = new ScrollPane();

		_scrollPane.setContent(contentAnchor);

		_scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		_scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

		_scrollPane.setPrefHeight(755);
		_scrollPane.setPrefWidth(480);

		VBox.setMargin(_scrollPane, new Insets(30));

		_scrollPane.setStyle("-fx-background: rgb(47.0, 52.0, 57.0);-fx-background-insets: 0; -fx-padding: 0;");

		_scrollPane.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				_lastYposition = event.getSceneY();
			}
		});


		_scrollPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				double newYposition = event.getSceneY();
				double diff = newYposition - _lastYposition;
				_lastYposition = newYposition;
				CustomScrollEvent cse = new CustomScrollEvent();
				cse.fireVerticalScroll((int)diff, SettingsController.this, (EventTarget) event.getSource());
			}

		});

		_dynamicContentAnchorPane.setCenter(_scrollPane);

		_testSelectionComboBox.valueProperty().addListener((observableValue, previousValue, selectedValue) -> {

			_currentPage = selectedValue;

			for (int i = contentAnchor.getChildren().size()-1; i > 0; i--) {
				contentAnchor.getChildren().remove(i);
			}

			switch(selectedValue){
				case "General":
					addGeneralSettings();
					break;
				case "Stimulus Sets":
					addStimulusSetSettings();
					break;
				case "PVT_AUDITORY":
					addAuditoryPVTSettings();
					break;
				case "PVT_VISUAL":
					addVisualPVTSettings();
					break;
				case "N_BACK_VISUAL_STIMULUS_IDENTITY":
					addVisualIdentityNBackSettings();
					break;
				case "N_BACK_VISUAL_LOCATION_IDENTITY":
					addVisualLocationNBackSettings();
					break;
				case "N_BACK_AUDITORY_STIMULUS_IDENTITY":
					addAuditoryNBackSettings();
					break;
				case "N_BACK_DUAL_AUDITORY_VISUAL_LOCATION_IDENTITY":
					addDualAuditoryLocationNBackSettings();
					break;
				case "N_BACK_DUAL_AUDITORY_VISUAL_STIMULUS_IDENTITY":
					addDualAuditoryIdentityNBackSettings();
					break;
				case "N_BACK_DUAL_VISUAL_VISUAL_STIMULUS_IDENTITY_LOCATION_IDENTITY":
					addDualIdentityLocationNBackSettings();
					break;
				case "MACKWORTH_CLOCK":
					addMackworthClockSettings();
					break;
				case "SPATIAL_WORKING_MEMORY":
					addSpatialWorkingMemoryUpdatingSettings();
					break;
			}


		});

		_testSelectionComboBox.getItems().add("General");
		_testSelectionComboBox.getItems().add("Stimulus Sets");

		for (int i = 0; i < TestType.values().length; i++) {
			_testSelectionComboBox.getItems().add(TestType.values()[i].name());
		}


		_testSelectionComboBox.setVisibleRowCount(_testSelectionComboBox.getItems().size());

		contentAnchor.getChildren().add(_testSelectionComboBox);

		_testSelectionComboBox.getSelectionModel().select("General");
		_testSelectionComboBox.setPrefWidth(480);


	}

	@Override
	protected void back() {
		getScreenManager().setScreen(Screens.MAIN);
	}

	@FXML
	private void save(final ActionEvent e) {
		if (!_validation.isInvalid()) {
				saveImplicit();

				SpeechSynthesizer.setup();
				getScreenManager().showMessage(_rb.getString("settingsSaved.text"));
		} else {
			_validation.initInitialDecoration();
		}
	}

	private void saveImplicit(){
		try {
			Config.getInstance().save();
			ConfigMeta.getInstance().save();
			StimulusSet.saveAllSetsToDisk();

			((IdentityNBackTestController) getScreenManager().getController(Screens.N_BACK_VISUAL_STIMULUS_IDENTITY)).setConfig();
			((LocationNBackTestController) getScreenManager().getController(Screens.N_BACK_VISUAL_LOCATION_IDENTITY)).setConfig();
			((AuditoryNBackTestController) getScreenManager().getController(Screens.N_BACK_AUDITORY_STIMULUS_IDENTITY)).setConfig();
			((MackworthClockTestController) getScreenManager().getController(Screens.MACKWORTH_CLOCK)).setConfig();
			((SpatialWorkingMemoryUpdatingTestController) getScreenManager().getController(Screens.SPATIAL_WORKING_MEMORY)).setConfig();
			((AuditoryIdentityDualNBackTestController) getScreenManager().getController(Screens.N_BACK_DUAL_AUDITORY_VISUAL_STIMULUS_IDENTITY)).setConfig();
			((AuditoryLocationDualNBackTestController) getScreenManager().getController(Screens.N_BACK_DUAL_AUDITORY_VISUAL_LOCATION_IDENTITY)).setConfig();
			((IdentityLocationDualNBackTestController) getScreenManager().getController(Screens.N_BACK_DUAL_VISUAL_VISUAL_STIMULUS_IDENTITY_LOCATION_IDENTITY)).setConfig();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}




	@FXML
	private void lowerVolume(final ActionEvent e) {
		final int volume = Config.getInstance().auditoryPVTvolumeProperty().get() - Config.VOLUME_DELTA;
		if (volume >= Config.MIN_VOLUME) {
			Config.getInstance().auditoryPVTvolumeProperty().set(volume);
		}
	}

	@FXML
	private void higherVolume(final ActionEvent e) {
		final int volume = Config.getInstance().auditoryPVTvolumeProperty().get() + Config.VOLUME_DELTA;
		if (volume <= Config.MAX_VOLUME) {
			Config.getInstance().auditoryPVTvolumeProperty().set(volume);
		}
	}

	@FXML
	private void chooser(final ActionEvent e) {
		getScreenManager().setScreen(Screens.DIRECTORY_CHOOSER);
		e.consume();
	}

	@FXML
	private void testTone(final ActionEvent e) {
		final int pulseDuration = Config.getInstance().auditoryPVTpulseDurationProperty().get();
		int frequency = Config.getInstance().auditoryPVTfrequencyProperty().get() * ((_positive) ? 1 : 2);
		if (Config.getInstance().auditoryPVTuseNoGoProperty().get()) {
			frequency *= ((_positive) ? 1 : 2);
			_positive = !_positive;
		} else {
			_positive = true;
		}
		final int volume = Config.getInstance().auditoryPVTvolumeProperty().get();
		final ByteBuffer buffer = ToneUtils.createToneBuffer(pulseDuration, frequency);
		try (final Tone tone = Tone.createTone(buffer)) {
			tone.play(volume);
		} catch (LineUnavailableException | IOException ex) {
			getScreenManager().showException(ex);
		}
	}

	@FXML
	private void inputTest(final ActionEvent e, Button inputTestButton, Circle indicator, Input inputType) {
		e.consume();

		final AtomicInteger time = new AtomicInteger(WAIT_TIME);

		inputTestButton.setDisable(true);
		inputTestButton.setText(String.format(_rb.getString("inputTestButtonWait.text"), time.getAndDecrement()));
		indicator.setFill(Color.web("#9f9f9f"));

		final Timeline timeline = new Timeline(
			new KeyFrame(
				Duration.seconds(1),
				ae -> inputTestButton.setText(String.format(_rb.getString("inputTestButtonWait.text"), time.getAndDecrement()))
			)
		);

		timeline.setCycleCount(WAIT_TIME);
		timeline.play();
		if (inputType == Input.SPEECH){
			Runnable r = () -> {
				try {
					SpeechDecoder.getInstance().initialize(SpeechDecoder.Language.GERMAN, Stimulus.Type.DIGIT);
					SpeechDecoder.getInstance().startRecording();
				} catch (InterruptedException | IOException | LineUnavailableException interruptedException) {
					interruptedException.printStackTrace();
				}
			};
			Thread t = new Thread(r);
			t.start();

			Runnable r2 = () -> {
				for (int i = 0; i < WAIT_TIME; i++) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException interruptedException) {
						interruptedException.printStackTrace();
					}
				}
				SpeechDecoder.getInstance().stopRecording();

				List<String> recognizedWords = SpeechDecoder.getInstance().currentWords;

				if(!recognizedWords.isEmpty()){
					String recognizedWordsString = "";
					for (int i = 0; i < recognizedWords.size(); i++) {
						recognizedWordsString += recognizedWords.get(i) + " ";
					}
					String finalRecognizedWordsString = recognizedWordsString;
					Platform.runLater(() ->{
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						alert.setTitle("Recognized Words");
						alert.setHeaderText("");
						alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
						alert.getDialogPane().setStyle("-fx-font-size: 0.5em;");
						alert.setContentText("The following digits were recognized: " + finalRecognizedWordsString);
						alert.show();
					});

				}

				timeline.stop();
				Platform.runLater(() ->{
					inputTestButton.setDisable(false);
					inputTestButton.setText(_rb.getString("inputTestButton.text"));
				});

			};
			new Thread(r2).start();




		}else{


		
		_executor.submit(() -> {
			final CountDownLatch latch = new CountDownLatch(1);
			InputFactory.getInstance().setInputCallback(latch::countDown);

			try {
				if (latch.await(WAIT_TIME, TimeUnit.SECONDS)) {
					Platform.runLater(() -> indicator.fillProperty().setValue(Color.GREEN));
				} else {
					Platform.runLater(() -> indicator.fillProperty().setValue(Color.RED));
				}				
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}


			timeline.stop();




			Platform.runLater(() -> {
				inputTestButton.setDisable(false);
				inputTestButton.setText(_rb.getString("inputTestButton.text"));
			});
		});
		}



	}




	public void load(ActionEvent actionEvent) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Config File");
		fileChooser.setInitialDirectory(new File(ConfigMeta.getInstance().configDirectoryProperty().getValue()));
		File file = fileChooser.showOpenDialog(_dynamicContentAnchorPane.getScene().getWindow());

		if (!file.getName().equals("meta.config.properties")){
			loadImplicit(file);
		}

	}

	public void loadImplicit(File file){
		if (file != null){
			Config config = Config.getInstance();
			config.inputProperty().unbind();
			config.auditoryPVTfrequencyProperty().unbind();
			ConfigMeta configMeta = ConfigMeta.getInstance();
			configMeta.activeConfigProperty().setValue(file.getName());
			String temp = _currentPage;

			try {
				configMeta.save();
				config.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
			_testSelectionComboBox.getSelectionModel().select("");
			_testSelectionComboBox.getSelectionModel().select(temp);



		}
	}


	public void createNew(ActionEvent actionEvent) {

		TextInputDialog td = new TextInputDialog("Create Config");

		// setHeaderText
		td.setHeaderText("Enter config name");
		td.getDialogPane().setStyle("-fx-font-size: 0.5em;");

		Optional<String> response = td.showAndWait();

		if (response.isPresent()){


		File directory = new File("config");
		if (! directory.exists()){
			directory.mkdir();
			System.out.println("directory exists");
		}

		File file = new File(ConfigMeta.getInstance().configDirectoryProperty().getValue() + response.get() + ".config.properties");
		saveTextToFile("", file);
		loadImplicit(file);
		saveImplicit();



		}else{
			System.out.println("no response");
		}

		/*
		FileChooser fileChooser = new FileChooser();
		//Show save file dialog
		File file = fileChooser.showSaveDialog(_dynamicContentAnchorPane.getScene().getWindow());
		if (file != null) {
			saveTextToFile("", file);
		}
		*/
	}

	private void saveTextToFile(String content, File file) {
		try {
			PrintWriter writer;
			writer = new PrintWriter(file);
			writer.println(content);
			writer.close();
		} catch (IOException ex) {
		}
	}


	private Label createLabel(Node node, String text){

		Label label = new Label();
		label.setLabelFor(node);
		label.setText(text);
		VBox.setMargin(label, new Insets(10, 50, 0, 50));
		return label;

	}

	private TextField createTextField(Property valueProperty, int vkType){

		TextField textField = new TextField();
		if (valueProperty instanceof IntegerProperty){
			textField.textProperty().bindBidirectional(valueProperty, new NullSafeNumberStringConverter(NumberFormat.getIntegerInstance()));
		}else{
			textField.textProperty().bindBidirectional(valueProperty);
		}
		textField.getProperties().put("vkType", vkType);
		textField.setOnKeyPressed(new EnterSubmitHandler());
		textField.setPrefWidth(380);
		VBox.setMargin(textField, new Insets(0, 50, 10, 50));
		return textField;
	}

	private void createAndAddStimulusComboBox(VBox parentElement, Stimulus.Type[] availableStimulusTypes, StringProperty stimulusTypeProperty, String label){
		if (availableStimulusTypes.length == 0) return;

		ComboBox<String> stimulusTypeComboBox = new ComboBox<>();


		for(StimulusSet s : StimulusSet.getLoadedSets()){
			if (Arrays.asList(availableStimulusTypes).contains(s.get_type())){
				stimulusTypeComboBox.getItems().add(s.get_name());
			}
		}

		stimulusTypeComboBox.valueProperty().bindBidirectional(stimulusTypeProperty);
		stimulusTypeComboBox.setPrefWidth(380);
		VBox.setMargin(stimulusTypeComboBox, new Insets(0, 50, 10, 50));
		Label stimulusTypeLabel = createLabel(stimulusTypeComboBox, label);
		parentElement.getChildren().add(stimulusTypeLabel);
		parentElement.getChildren().add(stimulusTypeComboBox);

	}

	private ColorPicker createColorPicker(StringProperty colorProperty){

		ColorPicker colorPicker = new ColorPicker(Color.web(colorProperty.getValue()));


		colorPicker.valueProperty().addListener(new ChangeListener<Color>() {
			@Override
			public void changed(ObservableValue<? extends Color> observableValue, Color color, Color t1) {
				String hex = String.format( "#%02X%02X%02X",
						(int)( t1.getRed() * 255 ),
						(int)( t1.getGreen() * 255 ),
						(int)( t1.getBlue() * 255 ) );
				colorProperty.setValue(hex);

			}
		});
		VBox.setMargin(colorPicker, new Insets(0, 50, 10, 50));

		return colorPicker;
	}




	private void addSequenceSettings(Stimulus.Type[] availableStimulusTypes, StringProperty stimulusTypeProperty, IntegerProperty sequenceLengthProperty, IntegerProperty sequenceRepeatProperty, IntegerProperty sequenceMatchesProperty , IntegerProperty sequenceLuresProperty, IntegerProperty sequenceNBackLevelProperty){
		VBox contentAnchor = (VBox) _scrollPane.getContent();

		createAndAddStimulusComboBox(contentAnchor, availableStimulusTypes, stimulusTypeProperty, "Stimulus Type");

		TextField sequenceLengthTextField = createTextField(sequenceLengthProperty, 1);
		TextField sequenceRepeatingTextField = createTextField(sequenceRepeatProperty, 1);
		TextField sequenceMatchesTextField = createTextField(sequenceMatchesProperty, 1);
		TextField sequenceLuresTextField = createTextField(sequenceLuresProperty, 1);
		TextField sequenceNBackLevelTextField = createTextField(sequenceNBackLevelProperty, 1);

		Label sequenceLengthLabel = createLabel(sequenceLengthTextField, "Length");
		Label sequenceRepeatingLabel = createLabel(sequenceRepeatingTextField, "Repeating Elements");
		Label sequenceMatchesLabel = createLabel(sequenceMatchesTextField, "Matches");
		Label sequenceLuresLabel = createLabel(sequenceLuresTextField, "Lures");
		Label sequenceNBackLevelLabel = createLabel(sequenceNBackLevelTextField, "n-Back Level");

		contentAnchor.getChildren().add(sequenceLengthLabel);
		contentAnchor.getChildren().add(sequenceLengthTextField);
		contentAnchor.getChildren().add(sequenceRepeatingLabel);
		contentAnchor.getChildren().add(sequenceRepeatingTextField);
		contentAnchor.getChildren().add(sequenceMatchesLabel);
		contentAnchor.getChildren().add(sequenceMatchesTextField);
		contentAnchor.getChildren().add(sequenceLuresLabel);
		contentAnchor.getChildren().add(sequenceLuresTextField);
		contentAnchor.getChildren().add(sequenceNBackLevelLabel);
		contentAnchor.getChildren().add(sequenceNBackLevelTextField);
	}

	private void addDualSequenceSettings(String firstSequenceName, String secondSequenceName, Stimulus.Type[] availableStimulusTypesFirstSequence, Stimulus.Type[] availableStimulusTypesSecondSequence, StringProperty stimulusTypePropertyFirstSequence, StringProperty stimulusTypePropertySecondSequence, IntegerProperty sequenceLengthProperty, IntegerProperty firstSequenceRepeatProperty, IntegerProperty secondSequenceRepeatProperty, IntegerProperty firstSequenceMatchesProperty , IntegerProperty secondSequenceMatchesProperty ,IntegerProperty firstSequenceLuresProperty, IntegerProperty secondSequenceLuresProperty, IntegerProperty sequenceNBackLevelProperty){
		VBox contentAnchor = (VBox) _scrollPane.getContent();

		TextField sequenceNBackLevelTextField = createTextField(sequenceNBackLevelProperty, 1);
		Label sequenceNBackLevelLabel = createLabel(sequenceNBackLevelTextField, "n-Back Level");

		TextField sequenceLengthTextField = createTextField(sequenceLengthProperty, 1);
		Label sequenceLengthLabel = createLabel(sequenceLengthTextField,  " Sequence Length");

		createAndAddStimulusComboBox(contentAnchor, availableStimulusTypesFirstSequence, stimulusTypePropertyFirstSequence, firstSequenceName + " Stimulus Type");

		TextField firstSequenceRepeatingTextField = createTextField(firstSequenceRepeatProperty, 1);
		TextField firstSequenceMatchesTextField = createTextField(firstSequenceMatchesProperty, 1);
		TextField firstSequenceLuresTextField = createTextField(firstSequenceLuresProperty, 1);

		Label firstSequenceRepeatingLabel = createLabel(firstSequenceRepeatingTextField, firstSequenceName + " Sequence Repeating Elements");
		Label firstSequenceMatchesLabel = createLabel(firstSequenceMatchesTextField, firstSequenceName + " Sequence Matches");
		Label firstSequenceLuresLabel = createLabel(firstSequenceLuresTextField, firstSequenceName + " Sequence Lures");

		createAndAddStimulusComboBox(contentAnchor, availableStimulusTypesSecondSequence, stimulusTypePropertySecondSequence, secondSequenceName + " Stimulus Type");

		TextField secondSequenceRepeatingTextField = createTextField(secondSequenceRepeatProperty, 1);
		TextField secondSequenceMatchesTextField = createTextField(secondSequenceMatchesProperty, 1);
		TextField secondSequenceLuresTextField = createTextField(secondSequenceLuresProperty, 1);

		Label secondSequenceRepeatingLabel = createLabel(secondSequenceRepeatingTextField, secondSequenceName + " Sequence Repeating Elements");
		Label secondSequenceMatchesLabel = createLabel(secondSequenceMatchesTextField, secondSequenceName + " Sequence Matches");
		Label secondSequenceLuresLabel = createLabel(secondSequenceLuresTextField, secondSequenceName + " Sequence Lures");

		contentAnchor.getChildren().add(sequenceLengthLabel);
		contentAnchor.getChildren().add(sequenceLengthTextField);

		contentAnchor.getChildren().add(sequenceNBackLevelLabel);
		contentAnchor.getChildren().add(sequenceNBackLevelTextField);

		contentAnchor.getChildren().add(firstSequenceRepeatingLabel);
		contentAnchor.getChildren().add(firstSequenceRepeatingTextField);
		contentAnchor.getChildren().add(firstSequenceMatchesLabel);
		contentAnchor.getChildren().add(firstSequenceMatchesTextField);
		contentAnchor.getChildren().add(firstSequenceLuresLabel);
		contentAnchor.getChildren().add(firstSequenceLuresTextField);


		contentAnchor.getChildren().add(secondSequenceRepeatingLabel);
		contentAnchor.getChildren().add(secondSequenceRepeatingTextField);
		contentAnchor.getChildren().add(secondSequenceMatchesLabel);
		contentAnchor.getChildren().add(secondSequenceMatchesTextField);
		contentAnchor.getChildren().add(secondSequenceLuresLabel);
		contentAnchor.getChildren().add(secondSequenceLuresTextField);

	}

	private void addVisualIdentityNBackSettings(){

		VBox contentAnchor = (VBox) _scrollPane.getContent();

		Config config = Config.getInstance();

		IntegerProperty sequenceLengthProperty = config.visualIdentitySequenceLengthProperty();
		IntegerProperty sequenceRepeatProperty = config.visualIdentitySequenceNRepeatProperty();
		IntegerProperty sequenceMatchProperty = config.visualIdentitySequenceNMatchProperty();
		IntegerProperty sequenceLuresProperty = config.visualIdentitySequenceNLuresProperty();
		IntegerProperty sequenceNBackLevelProperty = config.visualIdentitySequenceNBackLevelProperty();
		IntegerProperty intervalProperty  = config.visualIdentityIntervalProperty();


		StringProperty selectedStimulusProperty = config.visualIdentityStimulusTypeProperty();

		Stimulus.Type[] availableTypes = {Stimulus.Type.DIGIT, Stimulus.Type.LETTER, Stimulus.Type.COLOR, Stimulus.Type.IMAGE};

		addSequenceSettings(availableTypes, selectedStimulusProperty, sequenceLengthProperty, sequenceRepeatProperty, sequenceMatchProperty, sequenceLuresProperty, sequenceNBackLevelProperty);

		TextField intervalTextField = createTextField(intervalProperty, 1);
		Label intervalLabel = createLabel(intervalTextField, "Interval (ms)");
		contentAnchor.getChildren().add(intervalLabel);
		contentAnchor.getChildren().add(intervalTextField);

	}
	private void addVisualLocationNBackSettings(){

		VBox contentAnchor = (VBox) _scrollPane.getContent();


		Config config = Config.getInstance();

		IntegerProperty sequenceLengthProperty = config.visualLocationSequenceLengthProperty();
		IntegerProperty sequenceRepeatProperty = config.visualLocationSequenceNRepeatProperty();
		IntegerProperty sequenceMatchProperty = config.visualLocationSequenceNMatchProperty();
		IntegerProperty sequenceLuresProperty = config.visualLocationSequenceNLuresProperty();
		IntegerProperty sequenceNBackLevelProperty = config.visualLocationSequenceNBackLevelProperty();
		IntegerProperty intervalProperty   = config.visualLocationIntervalProperty();
		IntegerProperty rowCountProperty   = config.visualLocationRowCountProperty();

		StringProperty selectedStimulusProperty = config.visualIdentityStimulusTypeProperty();

		Stimulus.Type[] availableTypes = {};

		addSequenceSettings(availableTypes, selectedStimulusProperty, sequenceLengthProperty, sequenceRepeatProperty, sequenceMatchProperty, sequenceLuresProperty, sequenceNBackLevelProperty);

		TextField intervalTextField = createTextField(intervalProperty, 1);
		Label intervalLabel = createLabel(intervalTextField, "Interval (ms)");
		contentAnchor.getChildren().add(intervalLabel);
		contentAnchor.getChildren().add(intervalTextField);

		TextField rowCountTextField = createTextField(rowCountProperty, 1);
		Label rowCountLabel = createLabel(rowCountTextField, "Number of rows & columns");
		contentAnchor.getChildren().add(rowCountLabel);
		contentAnchor.getChildren().add(rowCountTextField);

		ColorPicker colorPicker = createColorPicker(config.visualLocationColorProperty());
		Label colorPickerLabel = createLabel(colorPicker, "Color");

		colorPicker.setAccessibleText("hello");

		contentAnchor.getChildren().add(colorPickerLabel);
		contentAnchor.getChildren().add(colorPicker);



	}




	private void addAuditoryNBackSettings(){

		VBox contentAnchor = (VBox) _scrollPane.getContent();

		Config config = Config.getInstance();

		IntegerProperty sequenceLengthProperty = config.auditorySequenceLengthProperty();
		IntegerProperty sequenceRepeatProperty = config.auditorySequenceNRepeatProperty();
		IntegerProperty sequenceMatchProperty = config.auditorySequenceNMatchProperty();
		IntegerProperty sequenceLuresProperty = config.auditorySequenceNLuresProperty();
		IntegerProperty sequenceNBackLevelProperty = config.auditorySequenceNBackLevelProperty();
		IntegerProperty intervalProperty  = config.auditoryIntervalProperty();


		StringProperty selectedStimulusProperty = config.auditoryStimulusTypeProperty();

		Stimulus.Type[] availableTypes = {Stimulus.Type.DIGIT, Stimulus.Type.LETTER};

		addSequenceSettings(availableTypes, selectedStimulusProperty, sequenceLengthProperty, sequenceRepeatProperty, sequenceMatchProperty, sequenceLuresProperty, sequenceNBackLevelProperty);

		TextField intervalTextField = createTextField(intervalProperty, 1);
		Label intervalLabel = createLabel(intervalTextField, "Interval (ms)");
		contentAnchor.getChildren().add(intervalLabel);
		contentAnchor.getChildren().add(intervalTextField);


	}

	private void addDualAuditoryLocationNBackSettings(){

		VBox contentAnchor = (VBox) _scrollPane.getContent();

		Config config = Config.getInstance();

		IntegerProperty sequenceLengthProperty = config.dualAuditoryLocationSequenceLengthProperty();
		IntegerProperty sequenceNBackLevelProperty = config.dualAuditoryLocationSequenceNBackLevelProperty();
		IntegerProperty intervalProperty  = config.dualAuditoryLocationIntervalProperty();

		IntegerProperty firstSequenceRepeatProperty = config.dualAuditoryLocationFirstSequenceNRepeatProperty();
		IntegerProperty firstSequenceMatchProperty = config.dualAuditoryLocationFirstSequenceNMatchProperty();
		IntegerProperty firstSequenceLuresProperty = config.dualAuditoryLocationFirstSequenceNLuresProperty();

		IntegerProperty secondSequenceRepeatProperty = config.dualAuditoryLocationSecondSequenceNRepeatProperty();
		IntegerProperty secondSequenceMatchProperty = config.dualAuditoryLocationSecondSequenceNMatchProperty();
		IntegerProperty secondSequenceLuresProperty = config.dualAuditoryLocationSecondSequenceNLuresProperty();

		StringProperty auditorySequenceSelectedStimulusProperty = config.dualAuditoryLocationStimulusTypeProperty();

		Stimulus.Type[] availableTypes = {Stimulus.Type.DIGIT, Stimulus.Type.LETTER};

		TextField intervalTextField = createTextField(intervalProperty, 1);
		Label intervalLabel = createLabel(intervalTextField, "Interval (ms)");

		contentAnchor.getChildren().add(intervalLabel);
		contentAnchor.getChildren().add(intervalTextField);



		IntegerProperty rowCountProperty = Config.getInstance().dualAuditoryLocationRowCountProperty();
		TextField rowCountTextField = createTextField(rowCountProperty, 1);
		Label rowCountLabel = createLabel(rowCountTextField, "Location Number of Rows & Columns");

		contentAnchor.getChildren().add(rowCountLabel);
		contentAnchor.getChildren().add(rowCountTextField);



		addDualSequenceSettings("Auditory", "Location", availableTypes, new Stimulus.Type[]{}, auditorySequenceSelectedStimulusProperty, null, sequenceLengthProperty, firstSequenceRepeatProperty,secondSequenceRepeatProperty, firstSequenceMatchProperty, secondSequenceMatchProperty,firstSequenceLuresProperty, secondSequenceLuresProperty, sequenceNBackLevelProperty);

	}

	private void addDualAuditoryIdentityNBackSettings(){
		VBox contentAnchor = (VBox) _scrollPane.getContent();

		Config config = Config.getInstance();

		IntegerProperty sequenceLengthProperty = config.dualAuditoryIdentitySequenceLengthProperty();
		IntegerProperty sequenceNBackLevelProperty = config.dualAuditoryIdentitySequenceNBackLevelProperty();
		IntegerProperty intervalProperty  = config.dualAuditoryIdentityIntervalProperty();

		IntegerProperty firstSequenceRepeatProperty = config.dualAuditoryIdentityFirstSequenceNRepeatProperty();
		IntegerProperty firstSequenceMatchProperty = config.dualAuditoryIdentityFirstSequenceNMatchProperty();
		IntegerProperty firstSequenceLuresProperty = config.dualAuditoryIdentityFirstSequenceNLuresProperty();

		IntegerProperty secondSequenceRepeatProperty = config.dualAuditoryIdentitySecondSequenceNRepeatProperty();
		IntegerProperty secondSequenceMatchProperty = config.dualAuditoryIdentitySecondSequenceNMatchProperty();
		IntegerProperty secondSequenceLuresProperty = config.dualAuditoryIdentitySecondSequenceNLuresProperty();

		StringProperty auditorySequenceSelectedStimulusProperty = config.dualAuditoryIdentityFirstStimulusTypeProperty();
		StringProperty identitySequenceSelectedStimulusProperty = config.dualAuditoryIdentitySecondStimulusTypeProperty();

		Stimulus.Type[] availableTypesAuditory = {Stimulus.Type.DIGIT, Stimulus.Type.LETTER};
		Stimulus.Type[] availableTypesIdentity = {Stimulus.Type.DIGIT, Stimulus.Type.LETTER, Stimulus.Type.COLOR, Stimulus.Type.IMAGE};

		TextField intervalTextField = createTextField(intervalProperty, 1);
		Label intervalLabel = createLabel(intervalTextField, "Interval (ms)");
		contentAnchor.getChildren().add(intervalLabel);
		contentAnchor.getChildren().add(intervalTextField);

		addDualSequenceSettings("Auditory", "Identity", availableTypesAuditory, availableTypesIdentity, auditorySequenceSelectedStimulusProperty, identitySequenceSelectedStimulusProperty, sequenceLengthProperty, firstSequenceRepeatProperty,secondSequenceRepeatProperty, firstSequenceMatchProperty, secondSequenceMatchProperty,firstSequenceLuresProperty, secondSequenceLuresProperty, sequenceNBackLevelProperty);
	}

	private void addDualIdentityLocationNBackSettings(){
		VBox contentAnchor = (VBox) _scrollPane.getContent();

		Config config = Config.getInstance();

		IntegerProperty sequenceLengthProperty = config.dualIdentityLocationSequenceLengthProperty();
		IntegerProperty sequenceNBackLevelProperty = config.dualIdentityLocationSequenceNBackLevelProperty();
		IntegerProperty intervalProperty  = config.dualAuditoryIdentityIntervalProperty();

		IntegerProperty firstSequenceRepeatProperty = config.dualIdentityLocationFirstSequenceNRepeatProperty();
		IntegerProperty firstSequenceMatchProperty = config.dualIdentityLocationFirstSequenceNMatchProperty();
		IntegerProperty firstSequenceLuresProperty = config.dualIdentityLocationFirstSequenceNLuresProperty();

		IntegerProperty secondSequenceRepeatProperty = config.dualIdentityLocationSecondSequenceNRepeatProperty();
		IntegerProperty secondSequenceMatchProperty = config.dualIdentityLocationSecondSequenceNMatchProperty();
		IntegerProperty secondSequenceLuresProperty = config.dualIdentityLocationSecondSequenceNLuresProperty();

		StringProperty identitySequenceSelectedStimulusProperty = config.dualIdentityLocationStimulusTypeProperty();

		Stimulus.Type[] availableTypes = {Stimulus.Type.DIGIT, Stimulus.Type.LETTER, Stimulus.Type.COLOR, Stimulus.Type.IMAGE};

		TextField intervalTextField = createTextField(intervalProperty, 1);
		Label intervalLabel = createLabel(intervalTextField, "Interval (ms)");
		contentAnchor.getChildren().add(intervalLabel);
		contentAnchor.getChildren().add(intervalTextField);

		IntegerProperty rowCountProperty = Config.getInstance().dualIdentityLocationRowCountProperty();
		TextField rowCountTextField = createTextField(rowCountProperty, 1);
		Label rowCountLabel = createLabel(rowCountTextField, "Location Number of Rows & Columns");

		contentAnchor.getChildren().add(rowCountLabel);
		contentAnchor.getChildren().add(rowCountTextField);

		addDualSequenceSettings("Identity", "Location", availableTypes, new Stimulus.Type[]{}, identitySequenceSelectedStimulusProperty, null, sequenceLengthProperty, firstSequenceRepeatProperty,secondSequenceRepeatProperty, firstSequenceMatchProperty, secondSequenceMatchProperty,firstSequenceLuresProperty, secondSequenceLuresProperty, sequenceNBackLevelProperty);
	}


	private void addAuditoryPVTSettings(){
		VBox contentAnchor = (VBox) _scrollPane.getContent();

		Config config = Config.getInstance();

		CheckBox useNoGoCheckbox = new CheckBox();

		useNoGoCheckbox.selectedProperty().bindBidirectional(config.auditoryPVTuseNoGoProperty());
		VBox.setMargin(useNoGoCheckbox, new Insets(0, 50 , 10, 50));


		TextField volumeTextField = createTextField(config.auditoryPVTvolumeProperty(), 1);
		TextField pulseDurationTextField = createTextField(config.auditoryPVTpulseDurationProperty(), 1);
		TextField timeoutTextField = createTextField(config.auditoryPVTtimeoutProperty(), 1);
		TextField minimumDelayTextField = createTextField(config.auditoryPVTminimumDelayProperty(), 1);
		TextField maximumDelayTextField = createTextField(config.auditoryPVTmaximumDelayProperty(), 1);
		TextField repetitionsTextField = createTextField(config.auditoryPVTrepetitionsProperty(), 1);
		TextField minimumResponseTimeTextField = createTextField(config.auditoryPVTminimumResponseTimeProperty(), 1);

		Label volumeTextFieldLabel = createLabel(volumeTextField, "Volume");
		Label pulseDurationTextFieldLabel = createLabel(pulseDurationTextField, "Pulse Duration (ms) ");
		Label timeoutTextFieldLabel = createLabel(timeoutTextField, "Timeout (ms)");
		Label minimumDelayTextFieldLabel = createLabel(minimumDelayTextField, "Minimum Delay (s)");
		Label maximumDelayTextFieldLabel = createLabel(maximumDelayTextField, "Maximum Delay (s)");
		Label repetitionsTextFieldLabel = createLabel(repetitionsTextField, "Repetitions");
		Label minimumResponseTimeTextFieldLabel = createLabel(minimumResponseTimeTextField, "Minimum Response Time (ms)");
		Label useNoGoCheckBoxLabel = createLabel(useNoGoCheckbox, "Use No-Go Paradigm");

		_validation.registerValidator(volumeTextField, false, Validator.createEmptyValidator(""));
		_validation.registerValidator(pulseDurationTextField, false, Validator.createEmptyValidator(""));
		_validation.registerValidator(timeoutTextField, false, Validator.createEmptyValidator(""));
		_validation.registerValidator(minimumDelayTextField, false, Validator.createEmptyValidator(""));
		_validation.registerValidator(maximumDelayTextField, false, Validator.createEmptyValidator(""));
		_validation.registerValidator(repetitionsTextField, false, Validator.createEmptyValidator(""));
		_validation.registerValidator(minimumResponseTimeTextField, false, Validator.createEmptyValidator(""));

		config.auditoryPVTuseNoGoProperty().addListener((final ObservableValue<? extends Boolean> observable, final Boolean oldValue, Boolean newValue) -> maximumDelayTextField.setDisable(newValue));
		maximumDelayTextField.setDisable(config.auditoryPVTuseNoGoProperty().get());

		contentAnchor.getChildren().add(volumeTextFieldLabel);
		contentAnchor.getChildren().add(volumeTextField);
		contentAnchor.getChildren().add(pulseDurationTextFieldLabel);
		contentAnchor.getChildren().add(pulseDurationTextField);
		contentAnchor.getChildren().add(minimumDelayTextFieldLabel);
		contentAnchor.getChildren().add(minimumDelayTextField);
		contentAnchor.getChildren().add(maximumDelayTextFieldLabel);
		contentAnchor.getChildren().add(maximumDelayTextField);
		contentAnchor.getChildren().add(repetitionsTextFieldLabel);
		contentAnchor.getChildren().add(repetitionsTextField);
		contentAnchor.getChildren().add(minimumResponseTimeTextFieldLabel);
		contentAnchor.getChildren().add(minimumResponseTimeTextField);
		contentAnchor.getChildren().add(timeoutTextFieldLabel);
		contentAnchor.getChildren().add(timeoutTextField);
		contentAnchor.getChildren().add(useNoGoCheckBoxLabel);
		contentAnchor.getChildren().add(useNoGoCheckbox);



	}

	private void addVisualPVTSettings() {
		VBox contentAnchor = (VBox) _scrollPane.getContent();

		Config config = Config.getInstance();

		CheckBox useNoGoCheckbox = new CheckBox();

		useNoGoCheckbox.selectedProperty().bindBidirectional(config.visualPVTuseNoGoProperty());
		VBox.setMargin(useNoGoCheckbox, new Insets(0, 50, 10, 50));


		TextField pulseDurationTextField = createTextField(config.visualPVTpulseDurationProperty(), 1);
		TextField timeoutTextField = createTextField(config.visualPVTtimeoutProperty(), 1);
		TextField minimumDelayTextField = createTextField(config.visualPVTminimumDelayProperty(), 1);
		TextField maximumDelayTextField = createTextField(config.visualPVTmaximumDelayProperty(), 1);
		TextField repetitionsTextField = createTextField(config.visualPVTrepetitionsProperty(), 1);
		TextField minimumResponseTimeTextField = createTextField(config.visualPVTminimumResponseTimeProperty(), 1);

		Label pulseDurationTextFieldLabel = createLabel(pulseDurationTextField, "Pulse Duration (ms)");
		Label timeoutTextFieldLabel = createLabel(timeoutTextField, "Timeout (ms)");
		Label minimumDelayTextFieldLabel = createLabel(minimumDelayTextField, "Minimum Delay (s)");
		Label maximumDelayTextFieldLabel = createLabel(maximumDelayTextField, "Maximum Delay (s)");
		Label repetitionsTextFieldLabel = createLabel(repetitionsTextField, "Repetitions");
		Label minimumResponseTimeTextFieldLabel = createLabel(minimumResponseTimeTextField, "Minimum Response Time (ms)");
		Label useNoGoCheckBoxLabel = createLabel(useNoGoCheckbox, "Use No-Go Paradigm");

		_validation.registerValidator(pulseDurationTextField, false, Validator.createEmptyValidator(""));
		_validation.registerValidator(timeoutTextField, false, Validator.createEmptyValidator(""));
		_validation.registerValidator(minimumDelayTextField, false, Validator.createEmptyValidator(""));
		_validation.registerValidator(maximumDelayTextField, false, Validator.createEmptyValidator(""));
		_validation.registerValidator(repetitionsTextField, false, Validator.createEmptyValidator(""));
		_validation.registerValidator(minimumResponseTimeTextField, false, Validator.createEmptyValidator(""));

		config.visualPVTuseNoGoProperty().addListener((final ObservableValue<? extends Boolean> observable, final Boolean oldValue, Boolean newValue) -> maximumDelayTextField.setDisable(newValue));
		maximumDelayTextField.setDisable(config.visualPVTuseNoGoProperty().get());


		ColorPicker goColorPicker = createColorPicker(Config.getInstance().visualPVTgoColorProperty());
		ColorPicker noGoColorPicker = createColorPicker(Config.getInstance().visualPVTnoGoColorProperty());
		Label goColorPickerLabel = createLabel(goColorPicker, "Go Color");
		Label noGoColorPickerLabel = createLabel(noGoColorPicker, "No-Go Color");

		contentAnchor.getChildren().add(pulseDurationTextFieldLabel);
		contentAnchor.getChildren().add(pulseDurationTextField);
		contentAnchor.getChildren().add(minimumDelayTextFieldLabel);
		contentAnchor.getChildren().add(minimumDelayTextField);
		contentAnchor.getChildren().add(maximumDelayTextFieldLabel);
		contentAnchor.getChildren().add(maximumDelayTextField);
		contentAnchor.getChildren().add(repetitionsTextFieldLabel);
		contentAnchor.getChildren().add(repetitionsTextField);
		contentAnchor.getChildren().add(minimumResponseTimeTextFieldLabel);
		contentAnchor.getChildren().add(minimumResponseTimeTextField);
		contentAnchor.getChildren().add(timeoutTextFieldLabel);
		contentAnchor.getChildren().add(timeoutTextField);
		contentAnchor.getChildren().add(useNoGoCheckBoxLabel);
		contentAnchor.getChildren().add(useNoGoCheckbox);
		contentAnchor.getChildren().add(goColorPickerLabel);
		contentAnchor.getChildren().add(goColorPicker);
		contentAnchor.getChildren().add(noGoColorPickerLabel);
		contentAnchor.getChildren().add(noGoColorPicker);
	}

	private void addMackworthClockSettings(){

		VBox contentAnchor = (VBox) _scrollPane.getContent();

		Config config = Config.getInstance();

		TextField testLengthTextField = createTextField(config.mackworthLengthProperty(), 1);
		TextField targetsTextField = createTextField(config.mackworthTargetsProperty(), 1);
		TextField intervalTextField = createTextField(config.mackworthIntervalProperty(), 1);
		TextField circleNumberTextField = createTextField(config.mackworthNCirclesProperty(), 1);

		Label testLengthTextFieldLabel = createLabel(testLengthTextField, "Repetitions");
		Label targetsTextFieldLabel = createLabel(targetsTextField, "Targets\n (repetition number slash separated, zero indexed)");
		Label intervalTextFieldLabel = createLabel(intervalTextField, "Interval (ms)");
		Label circleNumberTextFieldLabel = createLabel(circleNumberTextField, "Number of circles");

		ColorPicker colorPicker = createColorPicker(config.mackworthColorProperty());
		Label colorPickerLabel = createLabel(colorPicker, "Color");

		targetsTextField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
			if (t1)
				return;
			try {


				String targets = targetsTextField.getText();
				String[] targetsSeparated = targets.split("/");

				for (String s : targetsSeparated) {
					Integer.parseInt(s.trim());
				}


				targetsTextField.setText(targets.replaceAll("\\s+",
						""));


			}catch (Exception e){
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("");
				alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				alert.getDialogPane().setStyle("-fx-font-size: 0.5em;");
				alert.setContentText("The target indices you entered are not valid. A valid example is 3/15/30/45/60/63/70");
				alert.show();
			}
		});



		contentAnchor.getChildren().add(testLengthTextFieldLabel);
		contentAnchor.getChildren().add(testLengthTextField);
		contentAnchor.getChildren().add(targetsTextFieldLabel);
		contentAnchor.getChildren().add(targetsTextField);
		contentAnchor.getChildren().add(intervalTextFieldLabel);
		contentAnchor.getChildren().add(intervalTextField);
		contentAnchor.getChildren().add(circleNumberTextFieldLabel);
		contentAnchor.getChildren().add(circleNumberTextField);
		contentAnchor.getChildren().add(colorPickerLabel);
		contentAnchor.getChildren().add(colorPicker);


	}


	private void addSpatialWorkingMemoryUpdatingSettings(){

		VBox contentAnchor = (VBox) _scrollPane.getContent();

		Config config = Config.getInstance();

		TextField frameCountTextField = createTextField(config.spatialWorkingMemoryFramesProperty(), 1);
		TextField repetitionsTextField = createTextField(config.spatialWorkingMemoryRepetitionsProperty(), 1);
		TextField intervalTextField = createTextField(config.spatialWorkingMemoryIntervalProperty(), 1);
		TextField initialDelayTextField = createTextField(config.spatialWorkingMemoryInitialDelayProperty(), 1);

		Label frameCountTextFieldLabel = createLabel(frameCountTextField, "Number of Frames");
		Label repetitionsTextFieldLabel = createLabel(repetitionsTextField, "Repetitions including start values");
		Label intervalTextFieldLabel = createLabel(intervalTextField, "Interval (ms)");
		Label initialDelayTextFieldLabel = createLabel(initialDelayTextField, "Initial Delay (ms)");

		ColorPicker colorPicker = createColorPicker(config.spatialWorkingMemoryColorProperty());
		Label colorPickerLabel = createLabel(colorPicker, "Color");

		contentAnchor.getChildren().add(frameCountTextFieldLabel);
		contentAnchor.getChildren().add(frameCountTextField);
		contentAnchor.getChildren().add(repetitionsTextFieldLabel);
		contentAnchor.getChildren().add(repetitionsTextField);
		contentAnchor.getChildren().add(intervalTextFieldLabel);
		contentAnchor.getChildren().add(intervalTextField);
		contentAnchor.getChildren().add(initialDelayTextFieldLabel);
		contentAnchor.getChildren().add(initialDelayTextField);
		contentAnchor.getChildren().add(colorPickerLabel);
		contentAnchor.getChildren().add(colorPicker);


	}

	private void addGeneralSettings(){

		VBox contentAnchor = (VBox) _scrollPane.getContent();

		Config config = Config.getInstance();


		CheckBox useAutoCompletionCheckBox = new CheckBox();
		useAutoCompletionCheckBox.selectedProperty().bindBidirectional(config.useAutoCompletionProperty());
		VBox.setMargin(useAutoCompletionCheckBox, new Insets(0, 50, 10, 50));

		Label useAutoCompletionCheckBoxLabel = createLabel(useAutoCompletionCheckBox, "Use Autocompletion");

		ComboBox<Input> inputComboBox = new ComboBox<>();
		inputComboBox.itemsProperty().get().addAll(Input.values());

		inputComboBox.getSelectionModel().select(config.inputProperty().getValue());
		config.inputProperty().bind(inputComboBox.getSelectionModel().selectedItemProperty());

		VBox.setMargin(inputComboBox, new Insets(0, 50, 10, 50));
		Label inputComboBoxLabel = createLabel(inputComboBox, "Input Device");

		Circle indicatorCircle = new Circle();
		indicatorCircle.setFill(Paint.valueOf("white"));
		VBox.setMargin(indicatorCircle, new Insets(0, 50, 10, 50));
		indicatorCircle.setRadius(30);

		Button testInputButton = new Button();
		testInputButton.setText("Test Input");
		testInputButton.setOnAction(e -> inputTest(e, testInputButton, indicatorCircle, inputComboBox.getValue()));
		VBox.setMargin(testInputButton, new Insets(0, 50, 10, 50));

		DirectoryChooser configDirectoryChooser = new DirectoryChooser();
		configDirectoryChooser.setTitle("Choose Config Directory");

		Button configDirectoryButton = new Button(ConfigMeta.getInstance().configDirectoryProperty().getValue());
		configDirectoryButton.setOnAction(actionEvent -> {
			File configDirectory = configDirectoryChooser.showDialog(_dynamicContentAnchorPane.getScene().getWindow());
			if (configDirectory != null){
				ConfigMeta.getInstance().configDirectoryProperty().setValue(configDirectory.getAbsolutePath() + "/");
				configDirectoryButton.setText(configDirectory.getAbsolutePath() + "/");
			}
		});
		Label configDirectoryLabel = createLabel(configDirectoryButton, "Config Directory");
		VBox.setMargin(configDirectoryButton, new Insets(0, 50, 10, 50));

		DirectoryChooser resultDirectoryChooser = new DirectoryChooser();
		resultDirectoryChooser.setTitle("Choose Result Directory");

		Button resultDirectoryButton = new Button(ConfigMeta.getInstance().resultDirectoryProperty().getValue());
		resultDirectoryButton.setOnAction(actionEvent -> {
			File resultDirectory = resultDirectoryChooser.showDialog(_dynamicContentAnchorPane.getScene().getWindow());
			if (resultDirectory != null){
				ConfigMeta.getInstance().resultDirectoryProperty().setValue(resultDirectory.getAbsolutePath() + "/");
				resultDirectoryButton.setText(resultDirectory.getAbsolutePath() + "/");
			}
		});
		Label resultDirectoryLabel = createLabel(resultDirectoryButton, "Result Directory");
		VBox.setMargin(resultDirectoryButton, new Insets(0, 50, 10, 50));

		contentAnchor.getChildren().add(useAutoCompletionCheckBoxLabel);
		contentAnchor.getChildren().add(useAutoCompletionCheckBox);
		contentAnchor.getChildren().add(inputComboBoxLabel);
		contentAnchor.getChildren().add(inputComboBox);
		contentAnchor.getChildren().add(testInputButton);
		contentAnchor.getChildren().add(indicatorCircle);

		contentAnchor.getChildren().add(configDirectoryLabel);
		contentAnchor.getChildren().add(configDirectoryButton);

		contentAnchor.getChildren().add(resultDirectoryLabel);
		contentAnchor.getChildren().add(resultDirectoryButton);

	}

	private void addStimulusSetSettings(){
		VBox contentAnchor = (VBox) _scrollPane.getContent();

		Config config = Config.getInstance();


		ComboBox<String> stimulusSetComboBox = new ComboBox<>();
		VBox.setMargin(stimulusSetComboBox, new Insets(0, 50, 10, 50));

		stimulusSetComboBox.getItems().addAll((StimulusSet.getLoadedSets()).stream().map(StimulusSet::get_name).collect(Collectors.toList()));

		ComboBox<Stimulus.Type> stimulusSetTypeComboBox = new ComboBox<>();

		stimulusSetTypeComboBox.getItems().addAll(Stimulus.Type.values());
		stimulusSetTypeComboBox.valueProperty().addListener((observableValue, s, t1) -> {
			Objects.requireNonNull(StimulusSet.getSet(stimulusSetComboBox.getValue())).set_type(t1);
			String currentSetName = stimulusSetComboBox.getValue();
			stimulusSetComboBox.getSelectionModel().select("-1");
		 	stimulusSetComboBox.getSelectionModel().select(currentSetName);
		});


		VBox.setMargin(stimulusSetTypeComboBox, new Insets(0, 50 , 10 , 50));

		VBox elementVBox = new VBox();
		VBox.setMargin(elementVBox, new Insets(0 , 50, 10,50));
		elementVBox.setPrefWidth(contentAnchor.getPrefWidth() - 100);

		Button deleteButton = new Button();
		VBox.setMargin(deleteButton, new Insets(0, 50 , 10 , 50));
		deleteButton.setText("Delete current Set");
		deleteButton.setStyle("-fx-background-color: red;");
		deleteButton.setOnAction(actionEvent -> {

			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Confirmation");
			alert.setHeaderText("");
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.getDialogPane().setStyle("-fx-font-size: 0.5em;");
			alert.setContentText("Are you sure that you want to delete the set: " + stimulusSetComboBox.getValue() + " ?");
			Optional<ButtonType> response = alert.showAndWait();

			if (response.isEmpty()) return;
			if (response.get().getButtonData().equals(ButtonBar.ButtonData.CANCEL_CLOSE)) return;

			Objects.requireNonNull(StimulusSet.getSet(stimulusSetComboBox.getValue())).deleteSetFromDisk();
			_testSelectionComboBox.getSelectionModel().select("");
			_testSelectionComboBox.getSelectionModel().select("Stimulus Sets");

		});

		Button addButton = new Button();
		VBox.setMargin(addButton, new Insets(30, 50 , 10 , 50));
		addButton.setText("Add new Set");
		addButton.setOnAction(actionEvent -> {

			TextInputDialog alert = new TextInputDialog();
			alert.setTitle("Prompt");
			alert.setHeaderText("");
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.getDialogPane().setStyle("-fx-font-size: 0.5em;");
			alert.setContentText("Please enter the name of the new set");
			Optional<String> response = alert.showAndWait();

			if (response.isEmpty()) return;
		 	if (response.get().equals("")) return;
			StimulusSet.getLoadedSets().add(new StimulusSet(Stimulus.Type.DIGIT, new ArrayList<>(), response.get()));
			stimulusSetComboBox.getItems().add(response.get());
			stimulusSetComboBox.getSelectionModel().select(response.get());
		});

		stimulusSetComboBox.valueProperty().addListener((observableValue, s, t1) -> {
			StimulusSet currentSet = StimulusSet.getSet(t1);
			elementVBox.getChildren().clear();
			if (currentSet == null) return;

			HBox addItemHBox = new HBox();

			TextField addItemTextField =  new TextField();
			addItemTextField.setPromptText("Enter new Item here");


			ColorPicker addItemColorPicker = new ColorPicker();
			addItemColorPicker.setPromptText("Enter new Color here");

			FileChooser addItemFileChooser = new FileChooser();


			Button addItemChooseFileButton = new Button();
			addItemChooseFileButton.setText("Choose Image");
			addItemChooseFileButton.setOnAction(actionEvent -> {

				File file = addItemFileChooser.showOpenDialog(_dynamicContentAnchorPane.getScene().getWindow());
				addItemTextField.setText(file.getAbsolutePath());
			});

			Button addItemButton = new Button();
			addItemButton.setText("➕");
			addItemButton.setStyle("-fx-background-color: green;");
			addItemButton.setMaxHeight(50);
			addItemButton.setOnAction(actionEvent -> {

				String textValue = null;
				if(currentSet.get_type() == Stimulus.Type.LETTER || currentSet.get_type() == Stimulus.Type.DIGIT){
					if (addItemTextField.getText().equals(""))
						return;
					textValue = addItemTextField.getText();
				}else if(currentSet.get_type() == Stimulus.Type.COLOR){
					Color c = addItemColorPicker.getValue();

					textValue = String.format( "#%02X%02X%02X",
							(int)( c.getRed() * 255 ),
							(int)( c.getGreen() * 255 ),
							(int)( c.getBlue() * 255 ) );
				}else if(currentSet.get_type() == Stimulus.Type.IMAGE){
					if (addItemTextField.getText().equals(""))
						return;
					textValue = addItemTextField.getText();
				}
				if(textValue == null) return;

				currentSet.get_elements().add(textValue);

				getElementHBox(currentSet, elementVBox, textValue);
				addItemTextField.setText("");
				addItemColorPicker.setValue(Color.WHITE);
			});

			addItemTextField.setOnKeyPressed(keyEvent -> {
				if (keyEvent.getCode().equals(KeyCode.ENTER)){
					addItemButton.fire();
				}
			});

			if (currentSet.get_type() == Stimulus.Type.COLOR){
				addItemHBox.getChildren().add(addItemColorPicker);
			}else if(currentSet.get_type() == Stimulus.Type.IMAGE){
				addItemHBox.getChildren().add(addItemChooseFileButton);
			}else{
				addItemHBox.getChildren().add(addItemTextField);
			}

			addItemHBox.getChildren().add(addItemButton);

			addItemHBox.setPrefWidth(elementVBox.getPrefWidth());
			HBox.setHgrow(addItemTextField, Priority.ALWAYS);
			HBox.setHgrow(addItemColorPicker, Priority.ALWAYS);
			VBox.setMargin(addItemHBox, new Insets(25, 0, 10 , 0));


			elementVBox.getChildren().add(addItemHBox);

			Platform.runLater(() -> {
				stimulusSetTypeComboBox.valueProperty().setValue(currentSet.get_type());
			});
			for(String text : currentSet.get_elements()){
				getElementHBox(currentSet, elementVBox, text);
			}

			if (currentSet.get_name().equals("DIGIT") || currentSet.get_name().equals("LETTER") || currentSet.get_name().equals("COLOR")){
				addItemButton.setDisable(true);
				Text noDefaultEditText = new Text("Editing default sets: DIGIT, LETTER, COLOR is not allowed");
				noDefaultEditText.setFill(Color.RED);
				elementVBox.getChildren().add(noDefaultEditText);
			}




		});


		stimulusSetComboBox.getSelectionModel().select(0);

		Label stimulusSetComboBoxLabel = createLabel(stimulusSetComboBox, "Stimulus Set");
		Label stimulusSetTypeComboBoxLabel = createLabel(stimulusSetTypeComboBox, "Set Type");
		Label elementVBoxLabel = createLabel(elementVBox, "Items in Set");

		contentAnchor.getChildren().add(stimulusSetComboBoxLabel);
		contentAnchor.getChildren().add(stimulusSetComboBox);
		contentAnchor.getChildren().add(stimulusSetTypeComboBoxLabel);
		contentAnchor.getChildren().add(stimulusSetTypeComboBox);


		contentAnchor.getChildren().add(elementVBoxLabel);
		contentAnchor.getChildren().add(elementVBox);
		contentAnchor.getChildren().add(deleteButton);
		contentAnchor.getChildren().add(addButton);







	}

	private void getElementHBox(StimulusSet currentSet, VBox anchorNode, String text){
		HBox elementHBox = new HBox();
		elementHBox.setPrefWidth(anchorNode.getPrefWidth());
		Text t = new Text(text);
		if(currentSet.get_type() != Stimulus.Type.IMAGE){
			t.setFont(new Font(30));
		}else{
			t.setFont(new Font(10));
		}

		Button b = new Button();
		b.setOnAction(actionEvent -> {
			currentSet.get_elements().remove(text);
			anchorNode.getChildren().remove(elementHBox);
		});
		b.setText("❌");
		b.setStyle("-fx-background-color: red;");
		b.setAlignment(Pos.CENTER_RIGHT);
		b.setMaxHeight(50);

		if (currentSet.get_name().equals("DIGIT") || currentSet.get_name().equals("LETTER") || currentSet.get_name().equals("COLOR")) {
			b.setDisable(true);
		}

		elementHBox.setFillHeight(true);
		Region region1 = new Region();

		HBox.setHgrow(region1, Priority.ALWAYS);

		elementHBox.getChildren().add(t);
		elementHBox.getChildren().add(region1);
		if (currentSet.get_type() == Stimulus.Type.COLOR) {

			Circle colorCircle = new Circle();
			colorCircle.setRadius(15);
			try{
				colorCircle.setFill(Color.web(text));
			}catch (Exception e){
				e.printStackTrace();
			}
			HBox.setMargin(colorCircle, new Insets(2.5, 10, 0, 0));
			elementHBox.getChildren().add(colorCircle);

		}

		elementHBox.getChildren().add(b);
		elementHBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));


		VBox.setMargin(elementHBox, new Insets(10, 0 ,0, 0));


		anchorNode.getChildren().add(anchorNode.getChildren().size() - 1, elementHBox);
	}

}