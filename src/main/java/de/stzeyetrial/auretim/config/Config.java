package de.stzeyetrial.auretim.config;

import de.stzeyetrial.auretim.input.Input;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.stzeyetrial.auretim.util.Stimulus;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author strasser
 */
public class Config {
	public static final int MIN_VOLUME									= -80;
	public static final int MAX_VOLUME									= 0;
	public static final int VOLUME_DELTA								= 10;

	private static String _configFilename;

	private static final String CONFIG_COMMENT							= "AuReTim configuration";

	private static final String PROPERTY_DIRECTORY						= "directory";
	private static final String PROPERTY_INPUT							= "input";
	private static final String PROPERTY_FREQUENCY						= "frequency";
	private static final String PROPERTY_VOLUME							= "volume";
	private static final String PROPERTY_USE_AUTO_COMPLETION			= "userAutoCompletion";
	private static final String PROPERTY_USE_NO_GO						= "useNoGo";
	private static final String PROPERTY_FREQUENCIES					= "frequencies";
	private static final String PROPERTY_PULSEDURATION					= "pulseDuration";
	private static final String PROPERTY_TIMEOUT						= "timeout";
	private static final String PROPERTY_MINIMUM_DELAY					= "minimumDelay";
	private static final String PROPERTY_MAXIMUM_DELAY					= "maximumDelay";
	private static final String PROPERTY_MINIMUM_RESPONSE_TIME			= "minimumResponseTime";
	private static final String PROPERTY_REPETITIONS					= "repetitions";
	private static final String PROPERTY_VOICE_FEMALE					= "useFemaleVoice";
	private static final String PROPERTY_VOICE_MALE						= "useMaleVoice";
	private static final String PROPERTY_VOICE_GERMAN					= "useGermanVoice";
	private static final String PROPERTY_VOICE_ENGLISH					= "useEnglishVoice";

	private static final String PROPERTY_VISUAL_IDENTITY_STIMULUS_TYPE					= "visualIdentityStimulusType";
	private static final String PROPERTY_AUDITORY_STIMULUS_TYPE							= "auditoryStimulusType";

	private static final String PROPERTY_VISUAL_IDENTITY_SEQUENCE_LENGTH				= "visualIdentitySequenceLength";
	private static final String PROPERTY_VISUAL_IDENTITY_SEQUENCE_N_REPEAT				= "visualIdentitySequenceRepeat";
	private static final String PROPERTY_VISUAL_IDENTITY_SEQUENCE_N_MATCH				= "visualIdentitySequenceNMatch";
	private static final String PROPERTY_VISUAL_IDENTITY_SEQUENCE_N_LURES				= "visualIdentitySequenceNLures";
	private static final String PROPERTY_VISUAL_IDENTITY_SEQUENCE_N_BACK_LEVEL			= "visualIdentitySequenceNBackLevel";
	private static final String PROPERTY_VISUAL_IDENTITY_SEQUENCE_RE_USE_ELEMENT		= "visualIdentitySequenceReUseElement";
	private static final String PROPERTY_VISUAL_IDENTITY_INTERVAL						= "visualIdentityInterval";


	private static final String PROPERTY_VISUAL_LOCATION_SEQUENCE_LENGTH				= "visualLocationSequenceLength";
	private static final String PROPERTY_VISUAL_LOCATION_SEQUENCE_N_REPEAT				= "visualLocationSequenceRepeat";
	private static final String PROPERTY_VISUAL_LOCATION_SEQUENCE_N_MATCH				= "visualLocationSequenceNMatch";
	private static final String PROPERTY_VISUAL_LOCATION_SEQUENCE_N_LURES				= "visualLocationSequenceNLures";
	private static final String PROPERTY_VISUAL_LOCATION_SEQUENCE_N_BACK_LEVEL			= "visualLocationSequenceNBackLevel";
	private static final String PROPERTY_VISUAL_LOCATION_SEQUENCE_RE_USE_ELEMENT		= "visualLocationSequenceReUseElement";
	private static final String PROPERTY_VISUAL_LOCATION_INTERVAL						= "visualLocationInterval";
	private static final String PROPERTY_VISUAL_LOCATION_ROW_COUNT						= "visualLocationRowCount";


	private static final String PROPERTY_AUDITORY_SEQUENCE_LENGTH				= "auditorySequenceLength";
	private static final String PROPERTY_AUDITORY_SEQUENCE_N_REPEAT				= "auditorySequenceRepeat";
	private static final String PROPERTY_AUDITORY_SEQUENCE_N_MATCH				= "auditorySequenceNMatch";
	private static final String PROPERTY_AUDITORY_SEQUENCE_N_LURES				= "auditorySequenceNLures";
	private static final String PROPERTY_AUDITORY_SEQUENCE_N_BACK_LEVEL			= "auditorySequenceNBackLevel";
	private static final String PROPERTY_AUDITORY_SEQUENCE_RE_USE_ELEMENT		= "auditorySequenceReUseElement";
	private static final String PROPERTY_AUDITORY_INTERVAL						= "auditoryInterval";


	private static final String PROPERTY_USE_VOICE_RECOGNITION			= "useVoiceRecogntion";
	private static final String PROPERTY_MACKWORTH_LENGTH				= "mackworthLength";
	private static final String PROPERTY_MACKWORTH_TARGETS				= "mackworthTargets";
	private static final String PROPERTY_MACKWORTH_INTERVAL				= "mackworthInterval";
	private static final String PROPERTY_MACKWORTH_N_CIRCLES			= "mackworthNCircles";





	private static final String PROPERTY_DIRECTORY_DEFAULT				= ".";
	private static final String PROPERTY_INPUT_DEFAULT					= Input.BUTTON.toString();
	private static final String PROPERTY_FREQUENCY_DEFAULT				= Integer.toString(440);
	private static final String PROPERTY_VOLUME_DEFAULT					= Double.toString(0.5);
	private static final String PROPERTY_USE_AUTO_COMPLETION_DEFAULT	= Boolean.toString(false);
	private static final String PROPERTY_USE_NO_GO_DEFAULT				= Boolean.toString(false);
	private static final String PROPERTY_FREQUENCIES_DEFAULT			= "220,440,880,1760,3520";
	private static final String PROPERTY_PULSEDURATION_DEFAULT			= Integer.toString(500);
	private static final String PROPERTY_TIMEOUT_DEFAULT				= Integer.toString(2);
	private static final String PROPERTY_MINIMUM_DELAY_DEFAULT			= Integer.toString(2);
	private static final String PROPERTY_MAXIMUM_DELAY_DEFAULT			= Integer.toString(8);
	private static final String PROPERTY_REPETITIONS_DEFAULT			= Integer.toString(10);
	private static final String PROPERTY_MINIMUM_RESPONSE_TIME_DEFAULT	= Integer.toString(100);
	private static final String PROPERTY_VOICE_FEMALE_DEFAULT			= Boolean.toString(true);
	private static final String PROPERTY_VOICE_MALE_DEFAULT				= Boolean.toString(false);
	private static final String PROPERTY_VOICE_GERMAN_DEFAULT			= Boolean.toString(true);
	private static final String PROPERTY_VOICE_ENGLISH_DEFAULT			= Boolean.toString(false);
	private static final String PROPERTY_STIMULUS_TYPE_DEFAULT 			= Stimulus.Type.DIGIT.toString();
	private static final String PROPERTY_SEQUENCE_LENGTH_DEFAULT		= Integer.toString(20);
	private static final String PROPERTY_SEQUENCE_N_REPEAT_DEFAULT		= Integer.toString(5);
	private static final String PROPERTY_SEQUENCE_N_MATCH_DEFAULT		= Integer.toString(5);
	private static final String PROPERTY_SEQUENCE_N_LURES_DEFAULT		= Integer.toString(3);
	private static final String PROPERTY_SEQUENCE_N_BACK_LEVEL_DEFAULT	= Integer.toString(2);
	private static final String PROPERTY_SEQUENCE_RE_USE_ELEMENT_DEFAULT= Boolean.toString(true);
	private static final String PROPERTY_USE_VOICE_RECOGNITION_DEFAULT	= Boolean.toString(false);
	private static final String PROPERTY_MACKWORTH_LENGTH_DEFAULT		= Integer.toString(60);
	private static final String PROPERTY_MACKWORTH_TARGETS_DEFAULT		= Integer.toString(5);
	private static final String PROPERTY_MACKWORTH_INTERVAL_DEFAULT		= Integer.toString(1000);
	private static final String PROPERTY_MACKWORTH_N_CIRCLES_DEFAULT 	= Integer.toString(24);
	private static final String PROPERTY_N_BACK_INTERVAL_DEFAULT 		= Integer.toString(1500);
	private static final String PROPERTY_N_BACK_ROW_COUNT_DEFAULT 		= Integer.toString(3);






	private static Config _instance;

	private final StringProperty _directory				= new SimpleStringProperty();
	private final ObjectProperty<Input> _input			= new SimpleObjectProperty<>();
	private final IntegerProperty _frequency			= new SimpleIntegerProperty();
	private final IntegerProperty _volume				= new SimpleIntegerProperty();
	private final BooleanProperty _useAutoCompletion	= new SimpleBooleanProperty();
	private final BooleanProperty _useNoGo				= new SimpleBooleanProperty();
	private final IntegerProperty _pulseDuration		= new SimpleIntegerProperty();
	private final IntegerProperty _timeout				= new SimpleIntegerProperty();
	private final IntegerProperty _minimumDelay			= new SimpleIntegerProperty();
	private final IntegerProperty _maximumDelay			= new SimpleIntegerProperty();
	private final IntegerProperty _minimumResponseTime	= new SimpleIntegerProperty();
	private final IntegerProperty _repetitions			= new SimpleIntegerProperty();

	private final BooleanProperty _voiceFemale 			= new SimpleBooleanProperty();
	private final BooleanProperty _voiceMale 			= new SimpleBooleanProperty();
	private final BooleanProperty _voiceGerman 			= new SimpleBooleanProperty();
	private final BooleanProperty _voiceEnglish 		= new SimpleBooleanProperty();

	private final StringProperty _visualIdentityStimulusType			= new SimpleStringProperty();
	private final StringProperty _auditoryStimulusType			= new SimpleStringProperty();

	private final IntegerProperty _visualIdentitySequenceLength 		= new SimpleIntegerProperty();
	private final IntegerProperty _visualIdentitySequenceNRepeat 		= new SimpleIntegerProperty();
	private final IntegerProperty _visualIdentitySequenceNMatch		= new SimpleIntegerProperty();
	private final IntegerProperty _visualIdentitySequenceNLures 		= new SimpleIntegerProperty();
	private final IntegerProperty _visualIdentitySequenceNBackLevel 	= new SimpleIntegerProperty();
	private final BooleanProperty _visualIdentitySequenceReUseElement = new SimpleBooleanProperty();
	private final IntegerProperty _visualIdentityInterval = new SimpleIntegerProperty();

	private final IntegerProperty _visualLocationSequenceLength 		= new SimpleIntegerProperty();
	private final IntegerProperty _visualLocationSequenceNRepeat 		= new SimpleIntegerProperty();
	private final IntegerProperty _visualLocationSequenceNMatch		= new SimpleIntegerProperty();
	private final IntegerProperty _visualLocationSequenceNLures 		= new SimpleIntegerProperty();
	private final IntegerProperty _visualLocationSequenceNBackLevel 	= new SimpleIntegerProperty();
	private final BooleanProperty _visualLocationSequenceReUseElement = new SimpleBooleanProperty();
	private final IntegerProperty _visualLocationInterval = new SimpleIntegerProperty();
	private final IntegerProperty _visualLocationRowCount = new SimpleIntegerProperty();


	private final IntegerProperty _auditorySequenceLength 		= new SimpleIntegerProperty();
	private final IntegerProperty _auditorySequenceNRepeat 		= new SimpleIntegerProperty();
	private final IntegerProperty _auditorySequenceNMatch		= new SimpleIntegerProperty();
	private final IntegerProperty _auditorySequenceNLures 		= new SimpleIntegerProperty();
	private final IntegerProperty _auditorySequenceNBackLevel 	= new SimpleIntegerProperty();
	private final BooleanProperty _auditorySequenceReUseElement = new SimpleBooleanProperty();
	private final IntegerProperty _auditoryInterval 			= new SimpleIntegerProperty();


	private final BooleanProperty _useVoiceRecognition 	= new SimpleBooleanProperty();
	private final IntegerProperty _mackworthLength 		= new SimpleIntegerProperty();
	private final IntegerProperty _mackworthTargets		= new SimpleIntegerProperty();
	private final IntegerProperty _mackworthInterval 		= new SimpleIntegerProperty();

	private final IntegerProperty _mackworthNCircles 	= new SimpleIntegerProperty();

	private final ObjectProperty<ObservableList<Integer>> _frequencies = new SimpleObjectProperty<>(FXCollections.observableArrayList());

	private Config() {
	}

	public static synchronized Config getInstance() {
		if (_instance == null) {
			_instance = new Config();
		}

		return _instance;
	}

	public void load() {

		ConfigMeta configMeta = ConfigMeta.getInstance();
		configMeta.load();
		_configFilename = configMeta.activeConfigProperty().getValue();

		final Properties p = new Properties();
		try {
			p.load(new FileInputStream(_configFilename));
		} catch (final IOException ex) {
			Logger.getLogger(Config.class.getName()).log(Level.WARNING, "Could not load config file.", ex);
		}

		directoryProperty().setValue(p.getProperty(PROPERTY_DIRECTORY, PROPERTY_DIRECTORY_DEFAULT));
		inputProperty().setValue(Input.valueOf(p.getProperty(PROPERTY_INPUT, PROPERTY_INPUT_DEFAULT)));
		frequencyProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_FREQUENCY, PROPERTY_FREQUENCY_DEFAULT)));
		volumeProperty().setValue(Double.valueOf(p.getProperty(PROPERTY_VOLUME, PROPERTY_VOLUME_DEFAULT)));
		useAutoCompletionProperty().setValue(Boolean.valueOf(p.getProperty(PROPERTY_USE_AUTO_COMPLETION, PROPERTY_USE_AUTO_COMPLETION_DEFAULT)));
		useNoGoProperty().setValue(Boolean.valueOf(p.getProperty(PROPERTY_USE_NO_GO, PROPERTY_USE_NO_GO_DEFAULT)));
		pulseDurationProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_PULSEDURATION, PROPERTY_PULSEDURATION_DEFAULT)));
		timeoutProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_TIMEOUT, PROPERTY_TIMEOUT_DEFAULT)));
		minimumDelayProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_MINIMUM_DELAY, PROPERTY_MINIMUM_DELAY_DEFAULT)));
		maximumDelayProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_MAXIMUM_DELAY, PROPERTY_MAXIMUM_DELAY_DEFAULT)));
		repetitionsProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_REPETITIONS, PROPERTY_REPETITIONS_DEFAULT)));
		minimumResponseTimeProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_MINIMUM_RESPONSE_TIME, PROPERTY_MINIMUM_RESPONSE_TIME_DEFAULT)));


		voiceFemaleProperty().setValue(Boolean.valueOf(p.getProperty(PROPERTY_VOICE_FEMALE, PROPERTY_VOICE_FEMALE_DEFAULT)));
		voiceMaleProperty().setValue(Boolean.valueOf(p.getProperty(PROPERTY_VOICE_MALE, PROPERTY_VOICE_MALE_DEFAULT)));
		voiceGermanProperty().setValue(Boolean.valueOf(p.getProperty(PROPERTY_VOICE_GERMAN, PROPERTY_VOICE_GERMAN_DEFAULT)));
		voiceEnglishProperty().setValue(Boolean.valueOf(p.getProperty(PROPERTY_VOICE_ENGLISH, PROPERTY_VOICE_ENGLISH_DEFAULT)));

		visualIdentityStimulusTypeProperty().setValue(p.getProperty(PROPERTY_VISUAL_IDENTITY_STIMULUS_TYPE, PROPERTY_STIMULUS_TYPE_DEFAULT));
		auditoryStimulusTypeProperty().setValue(p.getProperty(PROPERTY_AUDITORY_STIMULUS_TYPE, PROPERTY_STIMULUS_TYPE_DEFAULT));


		visualIdentitySequenceLengthProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_VISUAL_IDENTITY_SEQUENCE_LENGTH, PROPERTY_SEQUENCE_LENGTH_DEFAULT)));
		visualIdentitySequenceNRepeatProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_VISUAL_IDENTITY_SEQUENCE_N_REPEAT, PROPERTY_SEQUENCE_N_REPEAT_DEFAULT)));
		visualIdentitySequenceNMatchProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_VISUAL_IDENTITY_SEQUENCE_N_MATCH, PROPERTY_SEQUENCE_N_MATCH_DEFAULT)));
		visualIdentitySequenceNLuresProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_VISUAL_IDENTITY_SEQUENCE_N_LURES, PROPERTY_SEQUENCE_N_LURES_DEFAULT)));
		visualIdentitySequenceNBackLevelProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_VISUAL_IDENTITY_SEQUENCE_N_BACK_LEVEL, PROPERTY_SEQUENCE_N_BACK_LEVEL_DEFAULT)));
		visualIdentitySequenceReUseElementProperty().setValue(Boolean.valueOf(p.getProperty(PROPERTY_VISUAL_IDENTITY_SEQUENCE_RE_USE_ELEMENT, PROPERTY_SEQUENCE_RE_USE_ELEMENT_DEFAULT)));
		visualIdentityIntervalProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_VISUAL_IDENTITY_INTERVAL, PROPERTY_N_BACK_INTERVAL_DEFAULT)));


		visualLocationSequenceLengthProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_VISUAL_LOCATION_SEQUENCE_LENGTH, PROPERTY_SEQUENCE_LENGTH_DEFAULT)));
		visualLocationSequenceNRepeatProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_VISUAL_LOCATION_SEQUENCE_N_REPEAT, PROPERTY_SEQUENCE_N_REPEAT_DEFAULT)));
		visualLocationSequenceNMatchProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_VISUAL_LOCATION_SEQUENCE_N_MATCH, PROPERTY_SEQUENCE_N_MATCH_DEFAULT)));
		visualLocationSequenceNLuresProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_VISUAL_LOCATION_SEQUENCE_N_LURES, PROPERTY_SEQUENCE_N_LURES_DEFAULT)));
		visualLocationSequenceNBackLevelProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_VISUAL_LOCATION_SEQUENCE_N_BACK_LEVEL, PROPERTY_SEQUENCE_N_BACK_LEVEL_DEFAULT)));
		visualLocationSequenceReUseElementProperty().setValue(Boolean.valueOf(p.getProperty(PROPERTY_VISUAL_LOCATION_SEQUENCE_RE_USE_ELEMENT, PROPERTY_SEQUENCE_RE_USE_ELEMENT_DEFAULT)));
		visualLocationIntervalProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_VISUAL_LOCATION_INTERVAL, PROPERTY_N_BACK_INTERVAL_DEFAULT)));
		visualLocationRowCountProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_VISUAL_LOCATION_ROW_COUNT, PROPERTY_N_BACK_ROW_COUNT_DEFAULT)));


		auditorySequenceLengthProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_AUDITORY_SEQUENCE_LENGTH, PROPERTY_SEQUENCE_LENGTH_DEFAULT)));
		auditorySequenceNRepeatProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_AUDITORY_SEQUENCE_N_REPEAT, PROPERTY_SEQUENCE_N_REPEAT_DEFAULT)));
		auditorySequenceNMatchProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_AUDITORY_SEQUENCE_N_MATCH, PROPERTY_SEQUENCE_N_MATCH_DEFAULT)));
		auditorySequenceNLuresProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_AUDITORY_SEQUENCE_N_LURES, PROPERTY_SEQUENCE_N_LURES_DEFAULT)));
		auditorySequenceNBackLevelProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_AUDITORY_SEQUENCE_N_BACK_LEVEL, PROPERTY_SEQUENCE_N_BACK_LEVEL_DEFAULT)));
		auditorySequenceReUseElementProperty().setValue(Boolean.valueOf(p.getProperty(PROPERTY_AUDITORY_SEQUENCE_RE_USE_ELEMENT, PROPERTY_SEQUENCE_RE_USE_ELEMENT_DEFAULT)));
		auditoryIntervalProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_AUDITORY_INTERVAL, PROPERTY_N_BACK_INTERVAL_DEFAULT)));


		useVoiceRecognitionProperty().setValue(Boolean.valueOf(p.getProperty(PROPERTY_USE_VOICE_RECOGNITION, PROPERTY_USE_VOICE_RECOGNITION_DEFAULT)));

		mackworthLengthProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_MACKWORTH_LENGTH, PROPERTY_MACKWORTH_LENGTH_DEFAULT)));
		mackworthTargetsProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_MACKWORTH_TARGETS, PROPERTY_MACKWORTH_TARGETS_DEFAULT)));
		mackworthIntervalProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_MACKWORTH_INTERVAL, PROPERTY_MACKWORTH_INTERVAL_DEFAULT)));
		mackworthNCirclesProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_MACKWORTH_N_CIRCLES, PROPERTY_MACKWORTH_N_CIRCLES_DEFAULT)));


		Arrays.stream(p.getProperty(PROPERTY_FREQUENCIES, PROPERTY_FREQUENCIES_DEFAULT)
			.split(","))
			.map(s -> s.trim())
			.mapToInt(s -> Integer.valueOf(s))
			.forEach(f -> frequenciesProperty().get().add(f));
	}

	public void save() throws IOException {
		final Properties p = new Properties();

		p.setProperty(PROPERTY_DIRECTORY,				directoryProperty().getValue());
		p.setProperty(PROPERTY_INPUT,					inputProperty().getValue().toString());
		p.setProperty(PROPERTY_FREQUENCY,				Integer.toString(frequencyProperty().getValue()));
		p.setProperty(PROPERTY_VOLUME,					Double.toString(volumeProperty().getValue()));
		p.setProperty(PROPERTY_USE_AUTO_COMPLETION,		Boolean.toString(useAutoCompletionProperty().getValue()));
		p.setProperty(PROPERTY_USE_NO_GO,				Boolean.toString(useNoGoProperty().getValue()));
		p.setProperty(PROPERTY_FREQUENCIES,				PROPERTY_FREQUENCIES_DEFAULT);
		p.setProperty(PROPERTY_PULSEDURATION,			Integer.toString(pulseDurationProperty().getValue()));
		p.setProperty(PROPERTY_TIMEOUT,					Integer.toString(timeoutProperty().getValue()));
		p.setProperty(PROPERTY_MINIMUM_DELAY,			Integer.toString(minimumDelayProperty().getValue()));
		p.setProperty(PROPERTY_MAXIMUM_DELAY,			Integer.toString(maximumDelayProperty().getValue()));
		p.setProperty(PROPERTY_REPETITIONS,				Integer.toString(repetitionsProperty().getValue()));
		p.setProperty(PROPERTY_MINIMUM_RESPONSE_TIME,	Integer.toString(minimumResponseTimeProperty().getValue()));
		p.setProperty(PROPERTY_VOICE_FEMALE, 			Boolean.toString(voiceFemaleProperty().getValue()));
		p.setProperty(PROPERTY_VOICE_MALE, 				Boolean.toString(voiceMaleProperty().getValue()));
		p.setProperty(PROPERTY_VOICE_GERMAN, 			Boolean.toString(voiceGermanProperty().getValue()));
		p.setProperty(PROPERTY_VOICE_ENGLISH, 			Boolean.toString(voiceEnglishProperty().getValue()));
		p.setProperty(PROPERTY_VISUAL_IDENTITY_STIMULUS_TYPE, 			visualIdentityStimulusTypeProperty().getValue());
		p.setProperty(PROPERTY_AUDITORY_STIMULUS_TYPE, 			auditoryStimulusTypeProperty().getValue());


		p.setProperty(PROPERTY_VISUAL_IDENTITY_SEQUENCE_LENGTH, 		Integer.toString(visualIdentitySequenceLengthProperty().getValue()));
		p.setProperty(PROPERTY_VISUAL_IDENTITY_SEQUENCE_N_REPEAT, 		Integer.toString(visualIdentitySequenceNRepeatProperty().getValue()));
		p.setProperty(PROPERTY_VISUAL_IDENTITY_SEQUENCE_N_MATCH, 		Integer.toString(visualIdentitySequenceNMatchProperty().getValue()));
		p.setProperty(PROPERTY_VISUAL_IDENTITY_SEQUENCE_N_LURES, 		Integer.toString(visualIdentitySequenceNLuresProperty().getValue()));
		p.setProperty(PROPERTY_VISUAL_IDENTITY_SEQUENCE_N_BACK_LEVEL, 	Integer.toString(visualIdentitySequenceNBackLevelProperty().getValue()));
		p.setProperty(PROPERTY_VISUAL_IDENTITY_SEQUENCE_RE_USE_ELEMENT, Boolean.toString(visualIdentitySequenceReUseElementProperty().getValue()));
		p.setProperty(PROPERTY_VISUAL_IDENTITY_INTERVAL, 	Integer.toString(visualIdentityIntervalProperty().getValue()));


		p.setProperty(PROPERTY_VISUAL_LOCATION_SEQUENCE_LENGTH, 		Integer.toString(visualLocationSequenceLengthProperty().getValue()));
		p.setProperty(PROPERTY_VISUAL_LOCATION_SEQUENCE_N_REPEAT, 		Integer.toString(visualLocationSequenceNRepeatProperty().getValue()));
		p.setProperty(PROPERTY_VISUAL_LOCATION_SEQUENCE_N_MATCH, 		Integer.toString(visualLocationSequenceNMatchProperty().getValue()));
		p.setProperty(PROPERTY_VISUAL_LOCATION_SEQUENCE_N_LURES, 		Integer.toString(visualLocationSequenceNLuresProperty().getValue()));
		p.setProperty(PROPERTY_VISUAL_LOCATION_SEQUENCE_N_BACK_LEVEL, 	Integer.toString(visualLocationSequenceNBackLevelProperty().getValue()));
		p.setProperty(PROPERTY_VISUAL_LOCATION_SEQUENCE_RE_USE_ELEMENT, Boolean.toString(visualLocationSequenceReUseElementProperty().getValue()));
		p.setProperty(PROPERTY_VISUAL_LOCATION_INTERVAL, 	Integer.toString(visualLocationIntervalProperty().getValue()));
		p.setProperty(PROPERTY_VISUAL_LOCATION_ROW_COUNT, 	Integer.toString(visualLocationRowCountProperty().getValue()));


		p.setProperty(PROPERTY_AUDITORY_SEQUENCE_LENGTH, 		Integer.toString(auditorySequenceLengthProperty().getValue()));
		p.setProperty(PROPERTY_AUDITORY_SEQUENCE_N_REPEAT, 		Integer.toString(auditorySequenceNRepeatProperty().getValue()));
		p.setProperty(PROPERTY_AUDITORY_SEQUENCE_N_MATCH, 		Integer.toString(auditorySequenceNMatchProperty().getValue()));
		p.setProperty(PROPERTY_AUDITORY_SEQUENCE_N_LURES, 		Integer.toString(auditorySequenceNLuresProperty().getValue()));
		p.setProperty(PROPERTY_AUDITORY_SEQUENCE_N_BACK_LEVEL, 	Integer.toString(auditorySequenceNBackLevelProperty().getValue()));
		p.setProperty(PROPERTY_AUDITORY_SEQUENCE_RE_USE_ELEMENT, Boolean.toString(auditorySequenceReUseElementProperty().getValue()));
		p.setProperty(PROPERTY_AUDITORY_INTERVAL, 	Integer.toString(auditoryIntervalProperty().getValue()));


		p.setProperty(PROPERTY_USE_VOICE_RECOGNITION, 	Boolean.toString(useVoiceRecognitionProperty().getValue()));
		p.setProperty(PROPERTY_MACKWORTH_LENGTH, 		Integer.toString(mackworthLengthProperty().getValue()));
		p.setProperty(PROPERTY_MACKWORTH_TARGETS, 		Integer.toString(mackworthTargetsProperty().getValue()));
		p.setProperty(PROPERTY_MACKWORTH_INTERVAL, 		Integer.toString(mackworthIntervalProperty().getValue()));
		p.setProperty(PROPERTY_MACKWORTH_N_CIRCLES,		Integer.toString(mackworthNCirclesProperty().getValue()));

		p.store(new FileOutputStream(_configFilename), CONFIG_COMMENT);


	}

	public StringProperty directoryProperty() {
		return _directory;
	}

	public ObjectProperty<Input> inputProperty() {
		return _input;
	}

	public IntegerProperty frequencyProperty() {
		return _frequency;
	}

	public IntegerProperty volumeProperty() {
		return _volume;
	}

	public BooleanProperty useAutoCompletionProperty() {
		return _useAutoCompletion;
	}

	public BooleanProperty useNoGoProperty() {
		return _useNoGo;
	}

	public IntegerProperty pulseDurationProperty() {
		return _pulseDuration;
	}

	public IntegerProperty timeoutProperty() {
		return _timeout;
	}

	public IntegerProperty minimumDelayProperty() {
		return _minimumDelay;
	}

	public IntegerProperty maximumDelayProperty() {
		return _maximumDelay;
	}

	public IntegerProperty minimumResponseTimeProperty() {
		return _minimumResponseTime;
	}

	public IntegerProperty repetitionsProperty() {
		return _repetitions;
	}

	public ObjectProperty<ObservableList<Integer>> frequenciesProperty() {
		return _frequencies;
	}

	public BooleanProperty voiceFemaleProperty(){
		return _voiceFemale;
	}

	public BooleanProperty voiceMaleProperty(){
		return _voiceMale;
	}

	public BooleanProperty voiceGermanProperty(){
		return _voiceGerman;
	}

	public BooleanProperty voiceEnglishProperty(){
		return _voiceEnglish;
	}

	public StringProperty visualIdentityStimulusTypeProperty(){return _visualIdentityStimulusType;}
	public StringProperty auditoryStimulusTypeProperty(){return _auditoryStimulusType;}

	//Visual Identity

	public IntegerProperty visualIdentitySequenceLengthProperty(){return _visualIdentitySequenceLength;}

	public IntegerProperty visualIdentitySequenceNRepeatProperty(){return _visualIdentitySequenceNRepeat;}

	public IntegerProperty visualIdentitySequenceNMatchProperty(){return _visualIdentitySequenceNMatch;}

	public IntegerProperty visualIdentitySequenceNLuresProperty(){return _visualIdentitySequenceNLures;}

	public IntegerProperty visualIdentitySequenceNBackLevelProperty(){return _visualIdentitySequenceNBackLevel;}

	public BooleanProperty visualIdentitySequenceReUseElementProperty(){return _visualIdentitySequenceReUseElement;}

	public IntegerProperty visualIdentityIntervalProperty(){return _visualIdentityInterval;}

	// Visual Location

	public IntegerProperty visualLocationSequenceLengthProperty(){return _visualLocationSequenceLength;}

	public IntegerProperty visualLocationSequenceNRepeatProperty(){return _visualLocationSequenceNRepeat;}

	public IntegerProperty visualLocationSequenceNMatchProperty(){return _visualLocationSequenceNMatch;}

	public IntegerProperty visualLocationSequenceNLuresProperty(){return _visualLocationSequenceNLures;}

	public IntegerProperty visualLocationSequenceNBackLevelProperty(){return _visualLocationSequenceNBackLevel;}

	public BooleanProperty visualLocationSequenceReUseElementProperty(){return _visualLocationSequenceReUseElement;}

	public IntegerProperty visualLocationIntervalProperty(){return _visualLocationInterval;}

	public IntegerProperty visualLocationRowCountProperty(){return _visualLocationRowCount;}


	//auditory

	public IntegerProperty auditorySequenceLengthProperty(){return _auditorySequenceLength;}

	public IntegerProperty auditorySequenceNRepeatProperty(){return _auditorySequenceNRepeat;}

	public IntegerProperty auditorySequenceNMatchProperty(){return _auditorySequenceNMatch;}

	public IntegerProperty auditorySequenceNLuresProperty(){return _auditorySequenceNLures;}

	public IntegerProperty auditorySequenceNBackLevelProperty(){return _auditorySequenceNBackLevel;}

	public BooleanProperty auditorySequenceReUseElementProperty(){return _auditorySequenceReUseElement;}

	public IntegerProperty auditoryIntervalProperty(){return _auditoryInterval;}



	public BooleanProperty useVoiceRecognitionProperty(){return _useVoiceRecognition;}

	public IntegerProperty mackworthLengthProperty(){return _mackworthLength;}

	public IntegerProperty mackworthTargetsProperty(){return _mackworthTargets;}

	public IntegerProperty mackworthIntervalProperty(){return _mackworthInterval;}

	public IntegerProperty mackworthNCirclesProperty(){return _mackworthNCircles;}


}