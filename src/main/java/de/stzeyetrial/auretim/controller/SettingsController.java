package de.stzeyetrial.auretim.controller;

import de.stzeyetrial.auretim.audio.SpeechSynthesizer;
import de.stzeyetrial.auretim.audio.Tone;
import de.stzeyetrial.auretim.audio.ToneUtils;
import de.stzeyetrial.auretim.config.Config;
import de.stzeyetrial.auretim.config.ConfigMeta;
import de.stzeyetrial.auretim.controller.nBack.*;
import de.stzeyetrial.auretim.input.Input;
import de.stzeyetrial.auretim.input.InputFactory;
import de.stzeyetrial.auretim.screens.Screens;
import de.stzeyetrial.auretim.util.EnterSubmitHandler;
import de.stzeyetrial.auretim.util.NullSafeNumberStringConverter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.ByteBuffer;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import de.stzeyetrial.auretim.util.Stimulus;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.SwipeEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javafx.util.StringConverter;
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
	private AnchorPane _anchorPane;

	@FXML
	private CheckBox _useNoGoCheckbox;

	@FXML
	private ComboBox<Integer> _frequencyChoiceBox;

	@FXML
	private Circle _indicator;

	@FXML
	private TextField _directoryTextField;

	@FXML
	private TextField _volumeTextField;

	@FXML
	private CheckBox _useAutoCompletionCheckbox;

	@FXML
	private TextField _pulseDurationTextField;

	@FXML
	private TextField _timeoutTextField;

	@FXML
	private TextField _minimumDelayTextField;

	@FXML
	private TextField _maximumDelayTextField;

	@FXML
	private TextField _minimumResponseTimeTextField;

	@FXML
	private TextField _repetitionsTextField;

	@FXML
	private ComboBox<Input> _inputComboBox;

	@FXML
	private Accordion _accordion;

	@FXML
	private Accordion _parentAccordion;

	@FXML
	private Button _inputTestButton;

	@FXML
	private RadioButton _radioButtonMale;

	@FXML
	private RadioButton _radioButtonFemale;

	@FXML
	private RadioButton _radioButtonGerman;

	@FXML
	private RadioButton _radioButtonEnglish;

	@FXML
	private ComboBox<String> _stimulusTypeComboBox;

	@FXML
	private TextField _sequenceLengthTextField;

	@FXML
	private TextField _sequenceNRepeatTextField;

	@FXML
	private TextField _sequenceNMatchTextField;

	@FXML
	private TextField _sequenceNLuresTextField;

	@FXML
	private TextField _sequenceNBackLevelTextField;

	@FXML
	private CheckBox _sequenceReUseElementsCheckbox;

	@FXML
	private CheckBox _useVoiceRecognitionCheckbox;

	@FXML
	private TextField _mackworthLengthTextField;

	@FXML
	private TextField _mackworthTargetsTextField;

	@FXML
	private TextField _mackworthIntervalTextField;

	@FXML
	private TextField _mackworthNCirclesTextField;

	@FXML
	private AnchorPane _dynamicContentAnchorPane;

	private Paint _indicatorDefaultColor;

	private boolean _positive = true;



	public SettingsController() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> _executor.shutdown()));
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		_rb = resources;
		_indicatorDefaultColor = _indicator.getFill();

		final Config config = Config.getInstance();
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


		ComboBox<String> _testSelectionComboBox = new ComboBox<>();

		VBox contentAnchor = (VBox) _dynamicContentAnchorPane.getChildren().get(0);

		_testSelectionComboBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String previousValue, String selectedValue) {

				for (int i = contentAnchor.getChildren().size()-1; i > 0; i--) {
					contentAnchor.getChildren().remove(i);
				}
				
				switch(selectedValue){
					case "nBack_Visual_Identity":
						addVisualIdentityNBackSettings();
						break;
					case "nBack_Visual_Location":
						addVisualLocationNBackSettings();
						break;
					case "nBack_Auditory":
						addAuditoryNBackSettings();
						break;
				}
				
				
			}
		});

		_testSelectionComboBox.itemsProperty().get().add("nBack_Visual_Identity");
		_testSelectionComboBox.itemsProperty().get().add("nBack_Visual_Location");
		_testSelectionComboBox.itemsProperty().get().add("nBack_Auditory");
		_testSelectionComboBox.itemsProperty().get().add("nBack_Dual");
		_testSelectionComboBox.itemsProperty().get().add("Mackworth Clock");
		_testSelectionComboBox.itemsProperty().get().add("PVT");
		_testSelectionComboBox.itemsProperty().get().add("Visual Identity & Auditive Dual-nBack");
		_testSelectionComboBox.itemsProperty().get().add("Visual Location & Auditive Dual-nBack");
		_testSelectionComboBox.itemsProperty().get().add("Visual Location & Identity Dual-nBack");
		_testSelectionComboBox.itemsProperty().get().add("Spatial Working Memory Update Test");


		contentAnchor.getChildren().add(_testSelectionComboBox);

		_testSelectionComboBox.getSelectionModel().select("nBack_Visual_Identity");



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
				((VisualIdentityNBackTestController) getScreenManager().getController(Screens.N_BACK_TEST_VISUAL_IDENTITY)).setConfig();
				((VisualLocationNBackTestController) getScreenManager().getController(Screens.N_BACK_TEST_VISUAL_LOCATION)).setConfig();
				((AuditiveNBackTestController) getScreenManager().getController(Screens.N_BACK_TEST_AUDITIVE)).setConfig();
				((MackworthClockTestController) getScreenManager().getController(Screens.MACKWORTH_CLOCK_TEST)).setConfig();
				((VisualIdentityDualNBackTestController) getScreenManager().getController(Screens.N_BACK_TEST_VISUAL_IDENTITY_AUDITIVE_DUAL)).setConfig();
				((VisualLocationIdentityDualNBackTestController) getScreenManager().getController(Screens.N_BACK_TEST_VISUAL_LOCATION_IDENTITY_DUAL)).setConfig();
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
	private void next(final SwipeEvent e) {
		final int index = _accordion.getPanes().indexOf(_accordion.getExpandedPane());
		if (index + 1 < _accordion.getPanes().size()) {
			_accordion.setExpandedPane(_accordion.getPanes().get(index + 1));
		}
		e.consume();
	}

	@FXML
	private void previous(final SwipeEvent e) {
		final int index = _accordion.getPanes().indexOf(_accordion.getExpandedPane());
		if (index - 1 >= 0) {
			_accordion.setExpandedPane(_accordion.getPanes().get(index - 1));
		}
		e.consume();
	}

	@FXML
	private void lowerVolume(final ActionEvent e) {
		final int volume = Config.getInstance().volumeProperty().get() - Config.VOLUME_DELTA;
		if (volume >= Config.MIN_VOLUME) {
			Config.getInstance().volumeProperty().set(volume);
		}
	}

	@FXML
	private void higherVolume(final ActionEvent e) {
		final int volume = Config.getInstance().volumeProperty().get() + Config.VOLUME_DELTA;
		if (volume <= Config.MAX_VOLUME) {
			Config.getInstance().volumeProperty().set(volume);
		}
	}

	@FXML
	private void chooser(final ActionEvent e) {
		getScreenManager().setScreen(Screens.DIRECTORY_CHOOSER);
		e.consume();
	}

	@FXML
	private void testTone(final ActionEvent e) {
		final int pulseDuration = Config.getInstance().pulseDurationProperty().get();
		int frequency = Config.getInstance().frequencyProperty().get() * ((_positive) ? 1 : 2);
		if (Config.getInstance().useNoGoProperty().get()) {
			frequency *= ((_positive) ? 1 : 2);
			_positive = !_positive;
		} else {
			_positive = true;
		}
		final int volume = Config.getInstance().volumeProperty().get();
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
	}

	public void load(ActionEvent actionEvent) {
		System.out.println("Load called");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Config File");

		File file = fileChooser.showOpenDialog(_anchorPane.getScene().getWindow());
		if (file != null){
			Config config = Config.getInstance();
			config.inputProperty().unbind();
			config.frequencyProperty().unbind();
			ConfigMeta configMeta = ConfigMeta.getInstance();
			configMeta.activeConfigProperty().setValue(file.getName());
			try {
				configMeta.save();
			} catch (IOException e) {
				e.printStackTrace();
			}

			config.frequencyProperty().bind(_frequencyChoiceBox.getSelectionModel().selectedItemProperty());
			config.inputProperty().bind(_inputComboBox.getSelectionModel().selectedItemProperty());

		}
	}

	public void createNew(ActionEvent actionEvent) {
		FileChooser fileChooser = new FileChooser();
		//Show save file dialog
		File file = fileChooser.showSaveDialog(_anchorPane.getScene().getWindow());


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
		VBox.setMargin(label, new Insets(10, 0, 0, 0));
		return label;

	}

	private TextField createTextField(IntegerProperty valueProperty, int vkType){

		TextField textField = new TextField();
		textField.textProperty().bindBidirectional(valueProperty, new NullSafeNumberStringConverter(NumberFormat.getIntegerInstance()));
		textField.getProperties().put("vkType", vkType);
		textField.setOnKeyPressed(new EnterSubmitHandler());
		VBox.setMargin(textField, new Insets(0, 0, 10, 0));
		return textField;
	}

	private void addSequenceSettings(Stimulus.Type[] availableStimulusTypes, StringProperty stimulusTypeProperty, IntegerProperty sequenceLengthProperty, IntegerProperty sequenceRepeatProperty, IntegerProperty sequenceMatchesProperty , IntegerProperty sequenceLuresProperty, IntegerProperty sequenceNBackLevelProperty){
		VBox contentAnchor = (VBox) _dynamicContentAnchorPane.getChildren().get(0);

		if (availableStimulusTypes.length > 0){
			ComboBox<String> stimulusTypeComboBox = new ComboBox();

			for (int i = 0; i < availableStimulusTypes.length; i++) {
				stimulusTypeComboBox.getItems().add(String.valueOf(availableStimulusTypes[i]));
			}
			stimulusTypeComboBox.valueProperty().bindBidirectional(stimulusTypeProperty);
			VBox.setMargin(stimulusTypeComboBox, new Insets(0, 0, 10, 0));
			Label stimulusTypeLabel = createLabel(stimulusTypeComboBox, "Stimulus Type");
			contentAnchor.getChildren().add(stimulusTypeLabel);
			contentAnchor.getChildren().add(stimulusTypeComboBox);

		}





		TextField sequenceLengthTextField = createTextField(sequenceLengthProperty, 1);
		TextField sequenceRepeatingTextField = createTextField(sequenceRepeatProperty, 1);
		TextField sequenceMatchesTextField = createTextField(sequenceMatchesProperty, 1);
		TextField sequenceLuresTextField = createTextField(sequenceLuresProperty, 1);
		TextField sequenceNBackLevelTextField = createTextField(sequenceNBackLevelProperty, 1);

		Label sequenceLengthLabel = createLabel(sequenceLengthTextField, "Length");
		Label sequenceRepeatingLabel = createLabel(sequenceLengthTextField, "Repeating Elements");
		Label sequenceMatchesLabel = createLabel(sequenceLengthTextField, "Matches");
		Label sequenceLuresLabel = createLabel(sequenceLengthTextField, "Lures");
		Label sequenceNBackLevelLabel = createLabel(sequenceLengthTextField, "n-Back Level");

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

	private void addVisualIdentityNBackSettings(){

		VBox contentAnchor = (VBox) _dynamicContentAnchorPane.getChildren().get(0);

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

		VBox contentAnchor = (VBox) _dynamicContentAnchorPane.getChildren().get(0);


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

	}




	private void addAuditoryNBackSettings(){

		VBox contentAnchor = (VBox) _dynamicContentAnchorPane.getChildren().get(0);

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


}