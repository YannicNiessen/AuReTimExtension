package de.stzeyetrial.auretim.controller;

import de.stzeyetrial.auretim.audio.SpeechSynthesizer;
import de.stzeyetrial.auretim.audio.Tone;
import de.stzeyetrial.auretim.audio.ToneUtils;
import de.stzeyetrial.auretim.config.Config;
import de.stzeyetrial.auretim.config.ConfigMeta;
import de.stzeyetrial.auretim.controller.nBack.*;
import de.stzeyetrial.auretim.screens.Screens;
import de.stzeyetrial.auretim.util.CustomScrollEvent;
import de.stzeyetrial.auretim.util.EnterSubmitHandler;
import de.stzeyetrial.auretim.util.NullSafeNumberStringConverter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.ByteBuffer;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.stzeyetrial.auretim.util.Stimulus;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;

import javax.sound.sampled.LineUnavailableException;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

/**
 * FXML Controller class
 *
 * @author strasser
 */
public class SettingsController extends AbstractBackSupportController {
	private static final int WAIT_TIME = 5;

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


	private Paint _indicatorDefaultColor;

	private boolean _positive = true;

	private ScrollPane _scrollPane;



	public SettingsController() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> _executor.shutdown()));
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		_rb = resources;
		//_indicatorDefaultColor = _indicator.getFill();

		final Config config = Config.getInstance();

		/*
		_directoryTextField.textProperty().bindBidirectional(config.directoryProperty());
		_validation.registerValidator(_directoryTextField, false, Validator.createEmptyValidator(""));
		_directoryTextField.setOnKeyPressed(new EnterSubmitHandler());
		_useAutoCompletionCheckbox.selectedProperty().bindBidirectional(config.useAutoCompletionProperty());
		_useNoGoCheckbox.selectedProperty().bindBidirectional(config.useNoGoProperty());
		_volumeTextField.textProperty().bindBidirectional(config.volumeProperty(), new NullSafeNumberStringConverter(NumberFormat.getIntegerInstance()));
		_validation.registerValidator(_volumeTextField, false, Validator.createEmptyValidator(""));
		
		_pulseDurationTextField.getProperties().put("vkType", "numeric");
		_pulseDurationTextField.textProperty().bindBidirectional(config.pulseDurationProperty(), new NullSafeNumberStringConverter(NumberFormat.getIntegerInstance()));
		_pulseDurationTextField.setOnKeyPressed(new EnterSubmitHandler());
		_validation.registerValidator(_pulseDurationTextField, false, Validator.createEmptyValidator(""));
		_timeoutTextField.textProperty().bindBidirectional(config.timeoutProperty(), new NullSafeNumberStringConverter(NumberFormat.getIntegerInstance()));
		_timeoutTextField.getProperties().put("vkType", "numeric");
		_timeoutTextField.setOnKeyPressed(new EnterSubmitHandler());
		_validation.registerValidator(_timeoutTextField, false, Validator.createEmptyValidator(""));
		_minimumDelayTextField.textProperty().bindBidirectional(config.minimumDelayProperty(), new NullSafeNumberStringConverter(NumberFormat.getIntegerInstance()));
		_minimumDelayTextField.getProperties().put("vkType", "numeric");
		_minimumDelayTextField.setOnKeyPressed(new EnterSubmitHandler());
		_validation.registerValidator(_minimumDelayTextField, false, Validator.createEmptyValidator(""));
		_maximumDelayTextField.textProperty().bindBidirectional(config.maximumDelayProperty(), new NullSafeNumberStringConverter(NumberFormat.getIntegerInstance()));
		_maximumDelayTextField.getProperties().put("vkType", "numeric");
		_maximumDelayTextField.setOnKeyPressed(new EnterSubmitHandler());
		config.useNoGoProperty().addListener((final ObservableValue<? extends Boolean> observable, final Boolean oldValue, Boolean newValue) -> _maximumDelayTextField.setDisable(newValue));
		_maximumDelayTextField.setDisable(config.useNoGoProperty().get());

		_validation.registerValidator(_maximumDelayTextField, false, Validator.createEmptyValidator(""));
		_repetitionsTextField.textProperty().bindBidirectional(config.repetitionsProperty(), new NullSafeNumberStringConverter(NumberFormat.getIntegerInstance()));
		_repetitionsTextField.getProperties().put("vkType", "numeric");
		_repetitionsTextField.setOnKeyPressed(new EnterSubmitHandler());
		_validation.registerValidator(_repetitionsTextField, false, Validator.createEmptyValidator(""));
		_minimumResponseTimeTextField.textProperty().bindBidirectional(config.minimumResponseTimeProperty(), new NullSafeNumberStringConverter(NumberFormat.getIntegerInstance()));
		_minimumResponseTimeTextField.getProperties().put("vkType", "numeric");
		_minimumResponseTimeTextField.setOnKeyPressed(new EnterSubmitHandler());
		_validation.registerValidator(_minimumResponseTimeTextField, false, Validator.createEmptyValidator(""));

		_radioButtonFemale.selectedProperty().bindBidirectional(config.voiceFemaleProperty());
		_radioButtonMale.selectedProperty().bindBidirectional(config.voiceMaleProperty());
		_radioButtonGerman.selectedProperty().bindBidirectional(config.voiceGermanProperty());
		_radioButtonEnglish.selectedProperty().bindBidirectional(config.voiceEnglishProperty());

		_stimulusTypeComboBox.getItems().add(Stimulus.Type.DIGIT.toString());
		_stimulusTypeComboBox.getItems().add(Stimulus.Type.LETTER.toString());
		_stimulusTypeComboBox.getItems().add(Stimulus.Type.COLOR.toString());
		_stimulusTypeComboBox.getItems().add(Stimulus.Type.IMAGE.toString());


		_stimulusTypeComboBox.valueProperty().bindBidirectional(config.visualIdentityStimulusTypeProperty());

		_sequenceLengthTextField.textProperty().bindBidirectional(config.visualIdentitySequenceLengthProperty(), new NullSafeNumberStringConverter(NumberFormat.getIntegerInstance()));
		_sequenceLengthTextField.getProperties().put("vkType", 1);
		_sequenceLengthTextField.setOnKeyPressed(new EnterSubmitHandler());

		_sequenceNRepeatTextField.textProperty().bindBidirectional(config.visualIdentitySequenceNRepeatProperty(), new NullSafeNumberStringConverter(NumberFormat.getIntegerInstance()));
		_sequenceNRepeatTextField.getProperties().put("vkType", 1);
		_sequenceNRepeatTextField.setOnKeyPressed(new EnterSubmitHandler());

		_sequenceNMatchTextField.textProperty().bindBidirectional(config.visualIdentitySequenceNMatchProperty(), new NullSafeNumberStringConverter(NumberFormat.getIntegerInstance()));
		_sequenceNMatchTextField.getProperties().put("vkType", 1);
		_sequenceNMatchTextField.setOnKeyPressed(new EnterSubmitHandler());

		_sequenceNLuresTextField.textProperty().bindBidirectional(config.visualIdentitySequenceNLuresProperty(), new NullSafeNumberStringConverter(NumberFormat.getIntegerInstance()));
		_sequenceNLuresTextField.getProperties().put("vkType", 1);
		_sequenceNLuresTextField.setOnKeyPressed(new EnterSubmitHandler());

		_sequenceNBackLevelTextField.textProperty().bindBidirectional(config.visualIdentitySequenceNBackLevelProperty(), new NullSafeNumberStringConverter(NumberFormat.getIntegerInstance()));
		_sequenceNBackLevelTextField.getProperties().put("vkType", 1);
		_sequenceNBackLevelTextField.setOnKeyPressed(new EnterSubmitHandler());

		_sequenceReUseElementsCheckbox.selectedProperty().bindBidirectional(config.visualIdentitySequenceReUseElementProperty());

		_useVoiceRecognitionCheckbox.selectedProperty().bindBidirectional(config.useVoiceRecognitionProperty());

		System.out.println(config.mackworthLengthProperty().getValue());

		_mackworthLengthTextField.textProperty().bindBidirectional(config.mackworthLengthProperty(), new NullSafeNumberStringConverter(NumberFormat.getIntegerInstance()));
		_mackworthLengthTextField.getProperties().put("vkType", 1);
		_mackworthTargetsTextField.setOnKeyPressed(new EnterSubmitHandler());
		System.out.println(config.mackworthLengthProperty().getValue());

		_mackworthTargetsTextField.textProperty().bindBidirectional(config.mackworthTargetsProperty(), new NullSafeNumberStringConverter(NumberFormat.getIntegerInstance()));
		_mackworthTargetsTextField.getProperties().put("vkType", 1);
		_mackworthIntervalTextField.setOnKeyPressed(new EnterSubmitHandler());

		_mackworthIntervalTextField.textProperty().bindBidirectional(config.mackworthIntervalProperty(), new NullSafeNumberStringConverter(NumberFormat.getIntegerInstance()));
		_mackworthIntervalTextField.getProperties().put("vkType", 1);
		_mackworthIntervalTextField.setOnKeyPressed(new EnterSubmitHandler());

		_mackworthNCirclesTextField.textProperty().bindBidirectional(config.mackworthNCirclesProperty(), new NullSafeNumberStringConverter(NumberFormat.getIntegerInstance()));
		_mackworthNCirclesTextField.getProperties().put("vkType", 1);
		_mackworthNCirclesTextField.setOnKeyPressed(new EnterSubmitHandler());


		_frequencyChoiceBox.setConverter(new StringConverter<Integer>() {
			@Override
			public String toString(final Integer object) {
				return String.format("%d Hz", object);
			}

			@Override
			public Integer fromString(final String string) {
				return Integer.valueOf(string.split(" ")[0]);
			}
		});
		_frequencyChoiceBox.getSelectionModel().select(config.frequencyProperty().getValue());
		config.frequencyProperty().bind(_frequencyChoiceBox.getSelectionModel().selectedItemProperty());
		_frequencyChoiceBox.itemsProperty().bind(config.frequenciesProperty());

		_inputComboBox.getSelectionModel().select(config.inputProperty().getValue());
		config.inputProperty().bind(_inputComboBox.getSelectionModel().selectedItemProperty());
		_inputComboBox.itemsProperty().get().addAll(Input.values());

*/

		ComboBox<String> _testSelectionComboBox = new ComboBox<>();

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

		_testSelectionComboBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String previousValue, String selectedValue) {


				for (int i = contentAnchor.getChildren().size()-1; i > 0; i--) {
					contentAnchor.getChildren().remove(i);
				}
				
				switch(selectedValue){
					case "PVT":
						addAuditoryPVTSettings();
						break;
					case "Visual PVT":
						addVisualPVTSettings();
						break;
					case "nBack_Visual_Identity":
						addVisualIdentityNBackSettings();
						break;
					case "nBack_Visual_Location":
						addVisualLocationNBackSettings();
						break;
					case "nBack_Auditory":
						addAuditoryNBackSettings();
						break;
					case "Visual Identity & Auditory Dual-nBack":
						addDualAuditoryIdentityNBackSettings();
						break;
					case "Visual Location & Auditory Dual-nBack":
						addDualAuditoryLocationNBackSettings();
						break;
					case "Visual Location & Identity Dual-nBack":
						addDualIdentityLocationNBackSettings();
						break;
				}
				
				
			}
		});

		_testSelectionComboBox.itemsProperty().get().add("nBack_Visual_Identity");
		_testSelectionComboBox.itemsProperty().get().add("nBack_Visual_Location");
		_testSelectionComboBox.itemsProperty().get().add("nBack_Auditory");
		_testSelectionComboBox.itemsProperty().get().add("Mackworth Clock");
		_testSelectionComboBox.itemsProperty().get().add("PVT");
		_testSelectionComboBox.itemsProperty().get().add("Visual PVT");
		_testSelectionComboBox.itemsProperty().get().add("Visual Identity & Auditory Dual-nBack");
		_testSelectionComboBox.itemsProperty().get().add("Visual Location & Auditory Dual-nBack");
		_testSelectionComboBox.itemsProperty().get().add("Visual Location & Identity Dual-nBack");
		_testSelectionComboBox.itemsProperty().get().add("Spatial Working Memory Update Test");


		contentAnchor.getChildren().add(_testSelectionComboBox);

		_testSelectionComboBox.getSelectionModel().select("nBack_Visual_Identity");

		_testSelectionComboBox.setPrefWidth(480);


	}

	@Override
	protected void back() {
		getScreenManager().setScreen(Screens.MAIN);
	}

	@FXML
	private void save(final ActionEvent e) {
		if (!_validation.isInvalid()) {
			try {
				Config.getInstance().save();
				((IdentityNBackTestController) getScreenManager().getController(Screens.N_BACK_TEST_VISUAL_IDENTITY)).setConfig();
				((LocationNBackTestController) getScreenManager().getController(Screens.N_BACK_TEST_VISUAL_LOCATION)).setConfig();
				((AuditoryNBackTestController) getScreenManager().getController(Screens.N_BACK_TEST_AUDITIVE)).setConfig();
				((MackworthClockTestController) getScreenManager().getController(Screens.MACKWORTH_CLOCK_TEST)).setConfig();
				((AuditoryIdentityDualNBackTestController) getScreenManager().getController(Screens.N_BACK_TEST_VISUAL_IDENTITY_AUDITIVE_DUAL)).setConfig();
				((AuditoryLocationDualNBackTestController) getScreenManager().getController(Screens.N_BACK_TEST_VISUAL_LOCATION_AUDITIVE_DUAL)).setConfig();
				((IdentityLocationDualNBackTestController) getScreenManager().getController(Screens.N_BACK_TEST_VISUAL_LOCATION_IDENTITY_DUAL)).setConfig();
				SpeechSynthesizer.setup();
				getScreenManager().showMessage(_rb.getString("settingsSaved.text"));
			} catch (IOException ex) {
				getScreenManager().showException(ex);
			}
		} else {
			_validation.initInitialDecoration();
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
	private void inputTest(final ActionEvent e) {
		e.consume();
		/*
		final AtomicInteger time = new AtomicInteger(WAIT_TIME);

		_inputTestButton.setDisable(true);
		_inputTestButton.setText(String.format(_rb.getString("inputTestButtonWait.text"), time.getAndDecrement()));
		_indicator.setFill(Color.web("#9f9f9f"));

		final Timeline timeline = new Timeline(
			new KeyFrame(
				Duration.seconds(1), 
				ae -> _inputTestButton.setText(String.format(_rb.getString("inputTestButtonWait.text"), time.getAndDecrement()))
			)
		);
		timeline.setCycleCount(WAIT_TIME);
		timeline.play();
		
		_executor.submit(() -> {
			final CountDownLatch latch = new CountDownLatch(1);
			InputFactory.getInstance().setInputCallback(() -> latch.countDown());

			try {
				if (latch.await(WAIT_TIME, TimeUnit.SECONDS)) {
					Platform.runLater(() -> _indicator.fillProperty().setValue(Color.GREEN));
				} else {
					Platform.runLater(() -> _indicator.fillProperty().setValue(Color.RED));
				}				
			} catch (InterruptedException ex) {
			}

			timeline.stop();
			Platform.runLater(() -> {
				_inputTestButton.setDisable(false);
				_inputTestButton.setText(_rb.getString("inputTestButton.text"));
			});
		});
				*/


	}




	public void load(ActionEvent actionEvent) {
		System.out.println("Load called");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Config File");

		File file = fileChooser.showOpenDialog(_dynamicContentAnchorPane.getScene().getWindow());
		if (file != null){
			Config config = Config.getInstance();
			config.inputProperty().unbind();
			config.auditoryPVTfrequencyProperty().unbind();
			ConfigMeta configMeta = ConfigMeta.getInstance();
			configMeta.activeConfigProperty().setValue(file.getName());
			try {
				configMeta.save();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}


	public void createNew(ActionEvent actionEvent) {
		FileChooser fileChooser = new FileChooser();
		//Show save file dialog
		File file = fileChooser.showSaveDialog(_dynamicContentAnchorPane.getScene().getWindow());

		if (file != null) {
			saveTextToFile("", file);
		}
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

	private TextField createTextField(IntegerProperty valueProperty, int vkType){

		TextField textField = new TextField();
		textField.textProperty().bindBidirectional(valueProperty, new NullSafeNumberStringConverter(NumberFormat.getIntegerInstance()));
		textField.getProperties().put("vkType", vkType);
		textField.setOnKeyPressed(new EnterSubmitHandler());
		textField.setPrefWidth(380);
		VBox.setMargin(textField, new Insets(0, 50, 10, 50));
		return textField;
	}

	private void createAndAddStimulusComboBox(VBox parentElement, Stimulus.Type[] availableStimulusTypes, StringProperty stimulusTypeProperty, String label){
		if (availableStimulusTypes.length > 0){
			ComboBox<String> stimulusTypeComboBox = new ComboBox();

			for (int i = 0; i < availableStimulusTypes.length; i++) {
				stimulusTypeComboBox.getItems().add(String.valueOf(availableStimulusTypes[i]));
			}
			stimulusTypeComboBox.valueProperty().bindBidirectional(stimulusTypeProperty);
			stimulusTypeComboBox.setPrefWidth(380);
			VBox.setMargin(stimulusTypeComboBox, new Insets(0, 50, 10, 50));
			Label stimulusTypeLabel = createLabel(stimulusTypeComboBox, label);
			parentElement.getChildren().add(stimulusTypeLabel);
			parentElement.getChildren().add(stimulusTypeComboBox);
		}
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
		Label pulseDurationTextFieldLabel = createLabel(pulseDurationTextField, "Pulse Duration");
		Label timeoutTextFieldLabel = createLabel(timeoutTextField, "Timeout (ms)");
		Label minimumDelayTextFieldLabel = createLabel(minimumDelayTextField, "Minimum Delay");
		Label maximumDelayTextFieldLabel = createLabel(maximumDelayTextField, "Maximum Delay");
		Label repetitionsTextFieldLabel = createLabel(repetitionsTextField, "Repetitions");
		Label minimumResponseTimeTextFieldLabel = createLabel(minimumResponseTimeTextField, "Minimum Response Time");
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

		Label pulseDurationTextFieldLabel = createLabel(pulseDurationTextField, "Pulse Duration");
		Label timeoutTextFieldLabel = createLabel(timeoutTextField, "Timeout (ms)");
		Label minimumDelayTextFieldLabel = createLabel(minimumDelayTextField, "Minimum Delay");
		Label maximumDelayTextFieldLabel = createLabel(maximumDelayTextField, "Maximum Delay");
		Label repetitionsTextFieldLabel = createLabel(repetitionsTextField, "Repetitions");
		Label minimumResponseTimeTextFieldLabel = createLabel(minimumResponseTimeTextField, "Minimum Response Time");
		Label useNoGoCheckBoxLabel = createLabel(useNoGoCheckbox, "Use No-Go Paradigm");

		_validation.registerValidator(pulseDurationTextField, false, Validator.createEmptyValidator(""));
		_validation.registerValidator(timeoutTextField, false, Validator.createEmptyValidator(""));
		_validation.registerValidator(minimumDelayTextField, false, Validator.createEmptyValidator(""));
		_validation.registerValidator(maximumDelayTextField, false, Validator.createEmptyValidator(""));
		_validation.registerValidator(repetitionsTextField, false, Validator.createEmptyValidator(""));
		_validation.registerValidator(minimumResponseTimeTextField, false, Validator.createEmptyValidator(""));

		config.visualPVTuseNoGoProperty().addListener((final ObservableValue<? extends Boolean> observable, final Boolean oldValue, Boolean newValue) -> maximumDelayTextField.setDisable(newValue));
		maximumDelayTextField.setDisable(config.visualPVTuseNoGoProperty().get());

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





}