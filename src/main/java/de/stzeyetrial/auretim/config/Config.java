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

	private static final String PROPERTY_USE_AUTO_COMPLETION			= "userAutoCompletion";

	private static final String PROPERTY_DIRECTORY						= "directory";
	private static final String PROPERTY_INPUT							= "input";

	private static final String PROPERTY_VISUAL_PVT_GO_COLOR = 			"visualPVTgoColor";
	private static final String PROPERTY_VISUAL_PVT_NO_GO_COLOR = 			"visualPVTnoGoColor";
	private static final String PROPERTY_VISUAL_LOCATION_COLOR = 			"visualLocationSequenceColor";
	private static final String PROPERTY_DUAL_AUDITORY_LOCATION_COLOR = 			"dualAuditoryLocationColor";
	private static final String PROPERTY_MACKWORTH_COLOR = 			"mackworthColor";
	private static final String PROPERTY_SPATIAL_WORKING_MEMORY_COLOR = 			"spatialWorkingMemoryColor";

	private static final String PROPERTY_AUDITORY_PVT_FREQUENCY						= "auditoryPVTfrequency";
	private static final String PROPERTY_AUDITORY_PVT_VOLUME							= "auditoryPVTvolume";
	private static final String PROPERTY_AUDITORY_PVT_USE_NO_GO						= "auditoryPVTuseNoGo";
	private static final String PROPERTY_AUDITORY_PVT_FREQUENCIES					= "auditoryPVTfrequencies";
	private static final String PROPERTY_AUDITORY_PVT_PULSEDURATION					= "auditoryPVTpulseDuration";
	private static final String PROPERTY_AUDITORY_PVT_TIMEOUT						= "auditoryPVTtimeout";
	private static final String PROPERTY_AUDITORY_PVT_MINIMUM_DELAY					= "auditoryPVTminimumDelay";
	private static final String PROPERTY_AUDITORY_PVT_MAXIMUM_DELAY					= "auditoryPVTmaximumDelay";
	private static final String PROPERTY_AUDITORY_PVT_MINIMUM_RESPONSE_TIME			= "auditoryPVTminimumResponseTime";
	private static final String PROPERTY_AUDITORY_PVT_REPETITIONS					= "auditoryPVTrepetitions";


	private static final String PROPERTY_VISUAL_PVT_USE_NO_GO						= "visualPVTuseNoGo";
	private static final String PROPERTY_VISUAL_PVT_PULSEDURATION					= "visualPVTpulseDuration";
	private static final String PROPERTY_VISUAL_PVT_TIMEOUT						= "visualPVTtimeout";
	private static final String PROPERTY_VISUAL_PVT_MINIMUM_DELAY					= "visualPVTminimumDelay";
	private static final String PROPERTY_VISUAL_PVT_MAXIMUM_DELAY					= "visualPVTmaximumDelay";
	private static final String PROPERTY_VISUAL_PVT_MINIMUM_RESPONSE_TIME			= "visualPVTminimumResponseTime";
	private static final String PROPERTY_VISUAL_PVT_REPETITIONS					= "visualPVTrepetitions";








	private static final String PROPERTY_VOICE_FEMALE					= "useFemaleVoice";
	private static final String PROPERTY_VOICE_MALE						= "useMaleVoice";
	private static final String PROPERTY_VOICE_GERMAN					= "useGermanVoice";
	private static final String PROPERTY_VOICE_ENGLISH					= "useEnglishVoice";

	private static final String PROPERTY_VISUAL_IDENTITY_STIMULUS_TYPE					= "visualIdentityStimulusType";
	private static final String PROPERTY_AUDITORY_STIMULUS_TYPE							= "auditoryStimulusType";
	private static final String PROPERTY_DUAL_AUDITORY_LOCATION_STIMULUS_TYPE			= "dualAuditoryLocationStimulusType";
	private static final String PROPERTY_DUAL_AUDITORY_IDENTITY_FIRST_STIMULUS_TYPE		= "dualAuditoryIdentityFirstStimulusType";
	private static final String PROPERTY_DUAL_AUDITORY_IDENTITY_SECOND_STIMULUS_TYPE	= "dualAuditoryIdentitySecondStimulusType";
	private static final String PROPERTY_DUAL_IDENTITY_LOCATION_STIMULUS_TYPE			= "dualIdentityLocationStimulusType";

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

	private static final String PROPERTY_DUAL_AUDITORY_LOCATION_SEQUENCE_LENGTH				= "dualAuditoryLocationSequenceLength";
	private static final String PROPERTY_DUAL_AUDITORY_LOCATION_SEQUENCE_N_BACK_LEVEL			= "dualAuditoryLocationSequenceNBackLevel";
	private static final String PROPERTY_DUAL_AUDITORY_LOCATION_INTERVAL						= "dualAuditoryLocationInterval";
	private static final String PROPERTY_DUAL_AUDITORY_LOCATION_ROW_COUNT						= "dualAuditoryLocationRowCount";

	private static final String PROPERTY_DUAL_AUDITORY_LOCATION_FIRST_SEQUENCE_N_REPEAT				= "dualAuditoryLocationFirstSequenceRepeat";
	private static final String PROPERTY_DUAL_AUDITORY_LOCATION_FIRST_SEQUENCE_N_MATCH				= "dualAuditoryLocationFirstSequenceNMatch";
	private static final String PROPERTY_DUAL_AUDITORY_LOCATION_FIRST_SEQUENCE_N_LURES				= "dualAuditoryLocationFirstSequenceNLures";
	private static final String PROPERTY_DUAL_AUDITORY_LOCATION_FIRST_SEQUENCE_RE_USE_ELEMENT		= "dualAuditoryLocationFirstSequenceReUseElement";

	private static final String PROPERTY_DUAL_AUDITORY_LOCATION_SECOND_SEQUENCE_N_REPEAT				= "dualAuditoryLocationFirstSequenceRepeat";
	private static final String PROPERTY_DUAL_AUDITORY_LOCATION_SECOND_SEQUENCE_N_MATCH				= "dualAuditoryLocationFirstSequenceNMatch";
	private static final String PROPERTY_DUAL_AUDITORY_LOCATION_SECOND_SEQUENCE_N_LURES				= "dualAuditoryLocationFirstSequenceNLures";
	private static final String PROPERTY_DUAL_AUDITORY_LOCATION_SECOND_SEQUENCE_RE_USE_ELEMENT		= "dualAuditoryLocationFirstSequenceReUseElement";


	private static final String PROPERTY_DUAL_IDENTITY_LOCATION_SEQUENCE_LENGTH				= "dualIdentityLocationSequenceLength";
	private static final String PROPERTY_DUAL_IDENTITY_LOCATION_SEQUENCE_N_BACK_LEVEL			= "dualIdentityLocationSequenceNBackLevel";
	private static final String PROPERTY_DUAL_IDENTITY_LOCATION_INTERVAL						= "dualIdentityLocationInterval";
	private static final String PROPERTY_DUAL_IDENTITY_LOCATION_ROW_COUNT						= "dualIdentityLocationRowCount";



	private static final String PROPERTY_DUAL_IDENTITY_LOCATION_FIRST_SEQUENCE_N_REPEAT				= "dualIdentityLocationFirstSequenceRepeat";
	private static final String PROPERTY_DUAL_IDENTITY_LOCATION_FIRST_SEQUENCE_N_MATCH				= "dualIdentityLocationFirstSequenceNMatch";
	private static final String PROPERTY_DUAL_IDENTITY_LOCATION_FIRST_SEQUENCE_N_LURES				= "dualIdentityLocationFirstSequenceNLures";
	private static final String PROPERTY_DUAL_IDENTITY_LOCATION_FIRST_SEQUENCE_RE_USE_ELEMENT		= "dualIdentityLocationFirstSequenceReUseElement";

	private static final String PROPERTY_DUAL_IDENTITY_LOCATION_SECOND_SEQUENCE_N_REPEAT				= "dualIdentityLocationFirstSequenceRepeat";
	private static final String PROPERTY_DUAL_IDENTITY_LOCATION_SECOND_SEQUENCE_N_MATCH				= "dualIdentityLocationFirstSequenceNMatch";
	private static final String PROPERTY_DUAL_IDENTITY_LOCATION_SECOND_SEQUENCE_N_LURES				= "dualIdentityLocationFirstSequenceNLures";
	private static final String PROPERTY_DUAL_IDENTITY_LOCATION_SECOND_SEQUENCE_RE_USE_ELEMENT		= "dualIdentityLocationFirstSequenceReUseElement";

	private static final String PROPERTY_DUAL_AUDITORY_IDENTITY_SEQUENCE_LENGTH				= "dualAuditoryIdentitySequenceLength";
	private static final String PROPERTY_DUAL_AUDITORY_IDENTITY_SEQUENCE_N_BACK_LEVEL			= "dualAuditoryIdentitySequenceNBackLevel";
	private static final String PROPERTY_DUAL_AUDITORY_IDENTITY_INTERVAL						= "dualAuditoryIdentityInterval";



	private static final String PROPERTY_DUAL_AUDITORY_IDENTITY_FIRST_SEQUENCE_N_REPEAT				= "dualAuditoryIdentityFirstSequenceRepeat";
	private static final String PROPERTY_DUAL_AUDITORY_IDENTITY_FIRST_SEQUENCE_N_MATCH				= "dualAuditoryIdentityFirstSequenceNMatch";
	private static final String PROPERTY_DUAL_AUDITORY_IDENTITY_FIRST_SEQUENCE_N_LURES				= "dualAuditoryIdentityFirstSequenceNLures";
	private static final String PROPERTY_DUAL_AUDITORY_IDENTITY_FIRST_SEQUENCE_RE_USE_ELEMENT		= "dualAuditoryIdentityFirstSequenceReUseElement";

	private static final String PROPERTY_DUAL_AUDITORY_IDENTITY_SECOND_SEQUENCE_N_REPEAT				= "dualAuditoryIdentityFirstSequenceRepeat";
	private static final String PROPERTY_DUAL_AUDITORY_IDENTITY_SECOND_SEQUENCE_N_MATCH				= "dualAuditoryIdentityFirstSequenceNMatch";
	private static final String PROPERTY_DUAL_AUDITORY_IDENTITY_SECOND_SEQUENCE_N_LURES				= "dualAuditoryIdentityFirstSequenceNLures";
	private static final String PROPERTY_DUAL_AUDITORY_IDENTITY_SECOND_SEQUENCE_RE_USE_ELEMENT		= "dualAuditoryIdentityFirstSequenceReUseElement";




	private static final String PROPERTY_USE_VOICE_RECOGNITION			= "useVoiceRecogntion";
	private static final String PROPERTY_MACKWORTH_LENGTH				= "mackworthLength";
	private static final String PROPERTY_MACKWORTH_TARGETS				= "mackworthTargets";
	private static final String PROPERTY_MACKWORTH_INTERVAL				= "mackworthInterval";
	private static final String PROPERTY_MACKWORTH_N_CIRCLES			= "mackworthNCircles";

	private static final String PROPERTY_SPATIAL_WORKING_MEMORY_FRAMES	= "spatialWorkingMemoryFrames";
	private static final String PROPERTY_SPATIAL_WORKING_MEMORY_REPETITIONS	= "spatialWorkingMemoryRepetitions";
	private static final String PROPERTY_SPATIAL_WORKING_MEMORY_INTERVAL	= "spatialWorkingMemoryInterval";
	private static final String PROPERTY_SPATIAL_WORKING_MEMORY_INITIAL_DELAY	= "spatialWorkingMemoryInitialDelay";





	private static final String PROPERTY_COLOR_DEFAULT = "#00FF00";
	private static final String PROPERTY_NO_GO_COLOR_DEFAULT = "#FF0000";
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

	private static final String PROPERTY_SPATIAL_WORKING_MEMORY_FRAMES_DEFAULT 		= Integer.toString(2);
	private static final String PROPERTY_SPATIAL_WORKING_MEMORY_REPETITIONS_DEFAULT 		= Integer.toString(5);
	private static final String PROPERTY_SPATIAL_WORKING_MEMORY_INTERVAL_DEFAULT 	= Integer.toString(1000);
	private static final String PROPERTY_SPATIAL_WORKING_MEMORY_INITIAL_DELAY_DEFAULT 	= Integer.toString(3000);





	private static Config _instance;

	private final StringProperty _directory				= new SimpleStringProperty();
	private final ObjectProperty<Input> _input			= new SimpleObjectProperty<>();
	private final BooleanProperty _useAutoCompletion	= new SimpleBooleanProperty();


	private final StringProperty _visualPVTgoColor = new SimpleStringProperty();
	private final StringProperty _visualPVTnoGoColor = new SimpleStringProperty();
	private final StringProperty _visualLocationColor = new SimpleStringProperty();
	private final StringProperty _dualAuditoryLocationColor = new SimpleStringProperty();

	private final StringProperty _mackworthColor = new SimpleStringProperty();
	private final StringProperty _spatialWorkingMemoryColor = new SimpleStringProperty();

	private final IntegerProperty _auditoryPVTfrequency			= new SimpleIntegerProperty();
	private final IntegerProperty _auditoryPVTvolume				= new SimpleIntegerProperty();
	private final BooleanProperty _auditoryPVTuseNoGo				= new SimpleBooleanProperty();
	private final IntegerProperty _auditoryPVTpulseDuration		= new SimpleIntegerProperty();
	private final IntegerProperty _auditoryPVTtimeout				= new SimpleIntegerProperty();
	private final IntegerProperty _auditoryPVTminimumDelay			= new SimpleIntegerProperty();
	private final IntegerProperty _auditoryPVTmaximumDelay			= new SimpleIntegerProperty();
	private final IntegerProperty _auditoryPVTminimumResponseTime	= new SimpleIntegerProperty();
	private final IntegerProperty _auditoryPVTrepetitions			= new SimpleIntegerProperty();

	private final IntegerProperty _visualPVTfrequency			= new SimpleIntegerProperty();
	private final IntegerProperty _visualPVTvolume				= new SimpleIntegerProperty();
	private final BooleanProperty _visualPVTuseNoGo				= new SimpleBooleanProperty();
	private final IntegerProperty _visualPVTpulseDuration		= new SimpleIntegerProperty();
	private final IntegerProperty _visualPVTtimeout				= new SimpleIntegerProperty();
	private final IntegerProperty _visualPVTminimumDelay			= new SimpleIntegerProperty();
	private final IntegerProperty _visualPVTmaximumDelay			= new SimpleIntegerProperty();
	private final IntegerProperty _visualPVTminimumResponseTime	= new SimpleIntegerProperty();
	private final IntegerProperty _visualPVTrepetitions			= new SimpleIntegerProperty();

	private final BooleanProperty _voiceFemale 			= new SimpleBooleanProperty();
	private final BooleanProperty _voiceMale 			= new SimpleBooleanProperty();
	private final BooleanProperty _voiceGerman 			= new SimpleBooleanProperty();
	private final BooleanProperty _voiceEnglish 		= new SimpleBooleanProperty();

	private final StringProperty _visualIdentityStimulusType			= new SimpleStringProperty();
	private final StringProperty _auditoryStimulusType			= new SimpleStringProperty();
	private final StringProperty _dualAuditoryLocationStimulusType			= new SimpleStringProperty();
	private final StringProperty _dualAuditoryIdentityFirstStimulusType			= new SimpleStringProperty();
	private final StringProperty _dualAuditoryIdentitySecondStimulusType			= new SimpleStringProperty();
	private final StringProperty _dualIdentityLocationStimulusType			= new SimpleStringProperty();

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

	private final IntegerProperty _dualAuditoryLocationSequenceLength 		= new SimpleIntegerProperty();
	private final IntegerProperty _dualAuditoryLocationInterval 			= new SimpleIntegerProperty();
	private final IntegerProperty _dualAuditoryLocationSequenceNBackLevel 	= new SimpleIntegerProperty();
	private final IntegerProperty _dualAuditoryLocationRowCount 	= new SimpleIntegerProperty();



	private final IntegerProperty _dualAuditoryLocationFirstSequenceNRepeat 		= new SimpleIntegerProperty();
	private final IntegerProperty _dualAuditoryLocationFirstSequenceNMatch		= new SimpleIntegerProperty();
	private final IntegerProperty _dualAuditoryLocationFirstSequenceNLures 		= new SimpleIntegerProperty();
	private final BooleanProperty _dualAuditoryLocationFirstSequenceReUseElement = new SimpleBooleanProperty();

	private final IntegerProperty _dualAuditoryLocationSecondSequenceNRepeat 		= new SimpleIntegerProperty();
	private final IntegerProperty _dualAuditoryLocationSecondSequenceNMatch		= new SimpleIntegerProperty();
	private final IntegerProperty _dualAuditoryLocationSecondSequenceNLures 		= new SimpleIntegerProperty();
	private final BooleanProperty _dualAuditoryLocationSecondSequenceReUseElement = new SimpleBooleanProperty();


	private final IntegerProperty _dualIdentityLocationSequenceLength 		= new SimpleIntegerProperty();
	private final IntegerProperty _dualIdentityLocationInterval 			= new SimpleIntegerProperty();
	private final IntegerProperty _dualIdentityLocationSequenceNBackLevel 	= new SimpleIntegerProperty();
	private final IntegerProperty _dualIdentityLocationRowCount 	= new SimpleIntegerProperty();

	private final IntegerProperty _dualIdentityLocationFirstSequenceNRepeat 		= new SimpleIntegerProperty();
	private final IntegerProperty _dualIdentityLocationFirstSequenceNMatch		= new SimpleIntegerProperty();
	private final IntegerProperty _dualIdentityLocationFirstSequenceNLures 		= new SimpleIntegerProperty();
	private final BooleanProperty _dualIdentityLocationFirstSequenceReUseElement = new SimpleBooleanProperty();

	private final IntegerProperty _dualIdentityLocationSecondSequenceNRepeat 		= new SimpleIntegerProperty();
	private final IntegerProperty _dualIdentityLocationSecondSequenceNMatch		= new SimpleIntegerProperty();
	private final IntegerProperty _dualIdentityLocationSecondSequenceNLures 		= new SimpleIntegerProperty();
	private final BooleanProperty _dualIdentityLocationSecondSequenceReUseElement = new SimpleBooleanProperty();

	private final IntegerProperty _dualAuditoryIdentitySequenceLength 		= new SimpleIntegerProperty();
	private final IntegerProperty _dualAuditoryIdentityInterval 			= new SimpleIntegerProperty();
	private final IntegerProperty _dualAuditoryIdentitySequenceNBackLevel 	= new SimpleIntegerProperty();



	private final IntegerProperty _dualAuditoryIdentityFirstSequenceNRepeat 		= new SimpleIntegerProperty();
	private final IntegerProperty _dualAuditoryIdentityFirstSequenceNMatch		= new SimpleIntegerProperty();
	private final IntegerProperty _dualAuditoryIdentityFirstSequenceNLures 		= new SimpleIntegerProperty();
	private final BooleanProperty _dualAuditoryIdentityFirstSequenceReUseElement = new SimpleBooleanProperty();

	private final IntegerProperty _dualAuditoryIdentitySecondSequenceNRepeat 		= new SimpleIntegerProperty();
	private final IntegerProperty _dualAuditoryIdentitySecondSequenceNMatch		= new SimpleIntegerProperty();
	private final IntegerProperty _dualAuditoryIdentitySecondSequenceNLures 		= new SimpleIntegerProperty();
	private final BooleanProperty _dualAuditoryIdentitySecondSequenceReUseElement = new SimpleBooleanProperty();


	private final BooleanProperty _useVoiceRecognition 	= new SimpleBooleanProperty();
	private final IntegerProperty _mackworthLength 		= new SimpleIntegerProperty();
	private final StringProperty _mackworthTargets		= new SimpleStringProperty();
	private final IntegerProperty _mackworthInterval 		= new SimpleIntegerProperty();

	private final IntegerProperty _mackworthNCircles 	= new SimpleIntegerProperty();

	private final IntegerProperty _spatialWorkingMemoryFrames 	= new SimpleIntegerProperty();
	private final IntegerProperty _spatialWorkingMemoryRepetitions 	= new SimpleIntegerProperty();
	private final IntegerProperty _spatialWorkingMemoryInterval 	= new SimpleIntegerProperty();
	private final IntegerProperty _spatialWorkingMemoryInitialDelay 	= new SimpleIntegerProperty();



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

		System.out.println("Config load called");
		System.out.println(_configFilename);

		final Properties p = new Properties();
		try {
			p.load(new FileInputStream("config/" + _configFilename));
		} catch (final IOException ex) {
			Logger.getLogger(Config.class.getName()).log(Level.WARNING, "Could not load config file.", ex);
		}

		System.out.println(p.getProperty(PROPERTY_INPUT));

		directoryProperty().setValue(p.getProperty(PROPERTY_DIRECTORY, PROPERTY_DIRECTORY_DEFAULT));
		inputProperty().setValue(Input.valueOf(p.getProperty(PROPERTY_INPUT, PROPERTY_INPUT_DEFAULT)));
		useAutoCompletionProperty().setValue(Boolean.valueOf(p.getProperty(PROPERTY_USE_AUTO_COMPLETION, PROPERTY_USE_AUTO_COMPLETION_DEFAULT)));

		visualPVTgoColorProperty().setValue(p.getProperty(PROPERTY_VISUAL_PVT_GO_COLOR, PROPERTY_COLOR_DEFAULT));
		visualPVTnoGoColorProperty().setValue(p.getProperty(PROPERTY_VISUAL_PVT_NO_GO_COLOR, PROPERTY_NO_GO_COLOR_DEFAULT));
		visualLocationColorProperty().setValue(p.getProperty(PROPERTY_VISUAL_LOCATION_COLOR, PROPERTY_COLOR_DEFAULT));
		dualAuditoryLocationColorProperty().setValue(p.getProperty(PROPERTY_DUAL_AUDITORY_LOCATION_COLOR, PROPERTY_COLOR_DEFAULT));
		mackworthColorProperty().setValue(p.getProperty(PROPERTY_MACKWORTH_COLOR, PROPERTY_COLOR_DEFAULT));
		spatialWorkingMemoryColorProperty().setValue(p.getProperty(PROPERTY_SPATIAL_WORKING_MEMORY_COLOR, PROPERTY_COLOR_DEFAULT));

		auditoryPVTfrequencyProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_AUDITORY_PVT_FREQUENCY, PROPERTY_FREQUENCY_DEFAULT)));
		auditoryPVTvolumeProperty().setValue(Double.valueOf(p.getProperty(PROPERTY_AUDITORY_PVT_VOLUME, PROPERTY_VOLUME_DEFAULT)));
		auditoryPVTuseNoGoProperty().setValue(Boolean.valueOf(p.getProperty(PROPERTY_AUDITORY_PVT_USE_NO_GO, PROPERTY_USE_NO_GO_DEFAULT)));
		auditoryPVTpulseDurationProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_AUDITORY_PVT_PULSEDURATION, PROPERTY_PULSEDURATION_DEFAULT)));
		auditoryPVTtimeoutProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_AUDITORY_PVT_TIMEOUT, PROPERTY_TIMEOUT_DEFAULT)));
		auditoryPVTminimumDelayProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_AUDITORY_PVT_MINIMUM_DELAY, PROPERTY_MINIMUM_DELAY_DEFAULT)));
		auditoryPVTmaximumDelayProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_AUDITORY_PVT_MAXIMUM_DELAY, PROPERTY_MAXIMUM_DELAY_DEFAULT)));
		auditoryPVTrepetitionsProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_AUDITORY_PVT_REPETITIONS, PROPERTY_REPETITIONS_DEFAULT)));
		auditoryPVTminimumResponseTimeProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_AUDITORY_PVT_MINIMUM_RESPONSE_TIME, PROPERTY_MINIMUM_RESPONSE_TIME_DEFAULT)));


		visualPVTuseNoGoProperty().setValue(Boolean.valueOf(p.getProperty(PROPERTY_VISUAL_PVT_USE_NO_GO, PROPERTY_USE_NO_GO_DEFAULT)));
		visualPVTpulseDurationProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_VISUAL_PVT_PULSEDURATION, PROPERTY_PULSEDURATION_DEFAULT)));
		visualPVTtimeoutProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_VISUAL_PVT_TIMEOUT, PROPERTY_TIMEOUT_DEFAULT)));
		visualPVTminimumDelayProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_VISUAL_PVT_MINIMUM_DELAY, PROPERTY_MINIMUM_DELAY_DEFAULT)));
		visualPVTmaximumDelayProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_VISUAL_PVT_MAXIMUM_DELAY, PROPERTY_MAXIMUM_DELAY_DEFAULT)));
		visualPVTrepetitionsProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_VISUAL_PVT_REPETITIONS, PROPERTY_REPETITIONS_DEFAULT)));
		visualPVTminimumResponseTimeProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_VISUAL_PVT_MINIMUM_RESPONSE_TIME, PROPERTY_MINIMUM_RESPONSE_TIME_DEFAULT)));


		voiceFemaleProperty().setValue(Boolean.valueOf(p.getProperty(PROPERTY_VOICE_FEMALE, PROPERTY_VOICE_FEMALE_DEFAULT)));
		voiceMaleProperty().setValue(Boolean.valueOf(p.getProperty(PROPERTY_VOICE_MALE, PROPERTY_VOICE_MALE_DEFAULT)));
		voiceGermanProperty().setValue(Boolean.valueOf(p.getProperty(PROPERTY_VOICE_GERMAN, PROPERTY_VOICE_GERMAN_DEFAULT)));
		voiceEnglishProperty().setValue(Boolean.valueOf(p.getProperty(PROPERTY_VOICE_ENGLISH, PROPERTY_VOICE_ENGLISH_DEFAULT)));

		visualIdentityStimulusTypeProperty().setValue(p.getProperty(PROPERTY_VISUAL_IDENTITY_STIMULUS_TYPE, PROPERTY_STIMULUS_TYPE_DEFAULT));
		auditoryStimulusTypeProperty().setValue(p.getProperty(PROPERTY_AUDITORY_STIMULUS_TYPE, PROPERTY_STIMULUS_TYPE_DEFAULT));

		dualAuditoryLocationStimulusTypeProperty().setValue(p.getProperty(PROPERTY_DUAL_AUDITORY_LOCATION_STIMULUS_TYPE, PROPERTY_STIMULUS_TYPE_DEFAULT));
		dualAuditoryIdentityFirstStimulusTypeProperty().setValue(p.getProperty(PROPERTY_DUAL_AUDITORY_IDENTITY_FIRST_STIMULUS_TYPE, PROPERTY_STIMULUS_TYPE_DEFAULT));
		dualAuditoryIdentitySecondStimulusTypeProperty().setValue(p.getProperty(PROPERTY_DUAL_AUDITORY_IDENTITY_SECOND_STIMULUS_TYPE, PROPERTY_STIMULUS_TYPE_DEFAULT));
		dualIdentityLocationStimulusTypeProperty().setValue(p.getProperty(PROPERTY_DUAL_IDENTITY_LOCATION_STIMULUS_TYPE, PROPERTY_STIMULUS_TYPE_DEFAULT));


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

		dualAuditoryLocationSequenceLengthProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_AUDITORY_LOCATION_SEQUENCE_LENGTH, PROPERTY_SEQUENCE_LENGTH_DEFAULT)));
		dualAuditoryLocationIntervalProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_AUDITORY_LOCATION_INTERVAL, PROPERTY_N_BACK_INTERVAL_DEFAULT)));
		dualAuditoryLocationSequenceNBackLevelProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_AUDITORY_LOCATION_SEQUENCE_N_BACK_LEVEL, PROPERTY_SEQUENCE_N_BACK_LEVEL_DEFAULT)));


		dualAuditoryLocationFirstSequenceNRepeatProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_AUDITORY_LOCATION_FIRST_SEQUENCE_N_REPEAT, PROPERTY_SEQUENCE_N_REPEAT_DEFAULT)));
		dualAuditoryLocationFirstSequenceNMatchProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_AUDITORY_LOCATION_FIRST_SEQUENCE_N_MATCH, PROPERTY_SEQUENCE_N_MATCH_DEFAULT)));
		dualAuditoryLocationFirstSequenceNLuresProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_AUDITORY_LOCATION_FIRST_SEQUENCE_N_LURES, PROPERTY_SEQUENCE_N_LURES_DEFAULT)));
		dualAuditoryLocationFirstSequenceReUseElementProperty().setValue(Boolean.valueOf(p.getProperty(PROPERTY_DUAL_AUDITORY_LOCATION_FIRST_SEQUENCE_RE_USE_ELEMENT, PROPERTY_SEQUENCE_RE_USE_ELEMENT_DEFAULT)));

		dualAuditoryLocationSecondSequenceNRepeatProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_AUDITORY_LOCATION_SECOND_SEQUENCE_N_REPEAT, PROPERTY_SEQUENCE_N_REPEAT_DEFAULT)));
		dualAuditoryLocationSecondSequenceNMatchProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_AUDITORY_LOCATION_SECOND_SEQUENCE_N_MATCH, PROPERTY_SEQUENCE_N_MATCH_DEFAULT)));
		dualAuditoryLocationSecondSequenceNLuresProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_AUDITORY_LOCATION_SECOND_SEQUENCE_N_LURES, PROPERTY_SEQUENCE_N_LURES_DEFAULT)));
		dualAuditoryLocationSecondSequenceReUseElementProperty().setValue(Boolean.valueOf(p.getProperty(PROPERTY_DUAL_AUDITORY_LOCATION_SECOND_SEQUENCE_RE_USE_ELEMENT, PROPERTY_SEQUENCE_RE_USE_ELEMENT_DEFAULT)));
		dualAuditoryLocationRowCountProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_AUDITORY_LOCATION_ROW_COUNT, PROPERTY_N_BACK_ROW_COUNT_DEFAULT)));


		dualAuditoryIdentitySequenceLengthProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_AUDITORY_IDENTITY_SEQUENCE_LENGTH, PROPERTY_SEQUENCE_LENGTH_DEFAULT)));
		dualAuditoryIdentityIntervalProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_AUDITORY_IDENTITY_INTERVAL, PROPERTY_N_BACK_INTERVAL_DEFAULT)));
		dualAuditoryIdentitySequenceNBackLevelProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_AUDITORY_IDENTITY_SEQUENCE_N_BACK_LEVEL, PROPERTY_SEQUENCE_N_BACK_LEVEL_DEFAULT)));


		dualAuditoryIdentityFirstSequenceNRepeatProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_AUDITORY_IDENTITY_FIRST_SEQUENCE_N_REPEAT, PROPERTY_SEQUENCE_N_REPEAT_DEFAULT)));
		dualAuditoryIdentityFirstSequenceNMatchProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_AUDITORY_IDENTITY_FIRST_SEQUENCE_N_MATCH, PROPERTY_SEQUENCE_N_MATCH_DEFAULT)));
		dualAuditoryIdentityFirstSequenceNLuresProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_AUDITORY_IDENTITY_FIRST_SEQUENCE_N_LURES, PROPERTY_SEQUENCE_N_LURES_DEFAULT)));
		dualAuditoryIdentityFirstSequenceReUseElementProperty().setValue(Boolean.valueOf(p.getProperty(PROPERTY_DUAL_AUDITORY_IDENTITY_FIRST_SEQUENCE_RE_USE_ELEMENT, PROPERTY_SEQUENCE_RE_USE_ELEMENT_DEFAULT)));

		dualAuditoryIdentitySecondSequenceNRepeatProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_AUDITORY_IDENTITY_SECOND_SEQUENCE_N_REPEAT, PROPERTY_SEQUENCE_N_REPEAT_DEFAULT)));
		dualAuditoryIdentitySecondSequenceNMatchProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_AUDITORY_IDENTITY_SECOND_SEQUENCE_N_MATCH, PROPERTY_SEQUENCE_N_MATCH_DEFAULT)));
		dualAuditoryIdentitySecondSequenceNLuresProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_AUDITORY_IDENTITY_SECOND_SEQUENCE_N_LURES, PROPERTY_SEQUENCE_N_LURES_DEFAULT)));
		dualAuditoryIdentitySecondSequenceReUseElementProperty().setValue(Boolean.valueOf(p.getProperty(PROPERTY_DUAL_AUDITORY_IDENTITY_SECOND_SEQUENCE_RE_USE_ELEMENT, PROPERTY_SEQUENCE_RE_USE_ELEMENT_DEFAULT)));

		dualIdentityLocationSequenceLengthProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_IDENTITY_LOCATION_SEQUENCE_LENGTH, PROPERTY_SEQUENCE_LENGTH_DEFAULT)));
		dualIdentityLocationIntervalProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_IDENTITY_LOCATION_INTERVAL, PROPERTY_N_BACK_INTERVAL_DEFAULT)));
		dualIdentityLocationSequenceNBackLevelProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_IDENTITY_LOCATION_SEQUENCE_N_BACK_LEVEL, PROPERTY_SEQUENCE_N_BACK_LEVEL_DEFAULT)));
		dualIdentityLocationRowCountProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_IDENTITY_LOCATION_ROW_COUNT, PROPERTY_N_BACK_ROW_COUNT_DEFAULT)));


		dualIdentityLocationFirstSequenceNRepeatProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_IDENTITY_LOCATION_FIRST_SEQUENCE_N_REPEAT, PROPERTY_SEQUENCE_N_REPEAT_DEFAULT)));
		dualIdentityLocationFirstSequenceNMatchProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_IDENTITY_LOCATION_FIRST_SEQUENCE_N_MATCH, PROPERTY_SEQUENCE_N_MATCH_DEFAULT)));
		dualIdentityLocationFirstSequenceNLuresProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_IDENTITY_LOCATION_FIRST_SEQUENCE_N_LURES, PROPERTY_SEQUENCE_N_LURES_DEFAULT)));
		dualIdentityLocationFirstSequenceReUseElementProperty().setValue(Boolean.valueOf(p.getProperty(PROPERTY_DUAL_IDENTITY_LOCATION_FIRST_SEQUENCE_RE_USE_ELEMENT, PROPERTY_SEQUENCE_RE_USE_ELEMENT_DEFAULT)));

		dualIdentityLocationSecondSequenceNRepeatProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_IDENTITY_LOCATION_SECOND_SEQUENCE_N_REPEAT, PROPERTY_SEQUENCE_N_REPEAT_DEFAULT)));
		dualIdentityLocationSecondSequenceNMatchProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_IDENTITY_LOCATION_SECOND_SEQUENCE_N_MATCH, PROPERTY_SEQUENCE_N_MATCH_DEFAULT)));
		dualIdentityLocationSecondSequenceNLuresProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_DUAL_IDENTITY_LOCATION_SECOND_SEQUENCE_N_LURES, PROPERTY_SEQUENCE_N_LURES_DEFAULT)));
		dualIdentityLocationSecondSequenceReUseElementProperty().setValue(Boolean.valueOf(p.getProperty(PROPERTY_DUAL_IDENTITY_LOCATION_SECOND_SEQUENCE_RE_USE_ELEMENT, PROPERTY_SEQUENCE_RE_USE_ELEMENT_DEFAULT)));

		useVoiceRecognitionProperty().setValue(Boolean.valueOf(p.getProperty(PROPERTY_USE_VOICE_RECOGNITION, PROPERTY_USE_VOICE_RECOGNITION_DEFAULT)));

		mackworthLengthProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_MACKWORTH_LENGTH, PROPERTY_MACKWORTH_LENGTH_DEFAULT)));
		mackworthTargetsProperty().setValue(p.getProperty(PROPERTY_MACKWORTH_TARGETS, PROPERTY_MACKWORTH_TARGETS_DEFAULT));
		mackworthIntervalProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_MACKWORTH_INTERVAL, PROPERTY_MACKWORTH_INTERVAL_DEFAULT)));
		mackworthNCirclesProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_MACKWORTH_N_CIRCLES, PROPERTY_MACKWORTH_N_CIRCLES_DEFAULT)));

		spatialWorkingMemoryFramesProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_SPATIAL_WORKING_MEMORY_FRAMES, PROPERTY_SPATIAL_WORKING_MEMORY_FRAMES_DEFAULT)));
		spatialWorkingMemoryRepetitionsProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_SPATIAL_WORKING_MEMORY_REPETITIONS, PROPERTY_SPATIAL_WORKING_MEMORY_REPETITIONS_DEFAULT)));
		spatialWorkingMemoryIntervalProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_SPATIAL_WORKING_MEMORY_INTERVAL, PROPERTY_SPATIAL_WORKING_MEMORY_INTERVAL_DEFAULT)));
		spatialWorkingMemoryInitialDelayProperty().setValue(Integer.valueOf(p.getProperty(PROPERTY_SPATIAL_WORKING_MEMORY_INITIAL_DELAY, PROPERTY_SPATIAL_WORKING_MEMORY_INITIAL_DELAY_DEFAULT)));


		Arrays.stream(p.getProperty(PROPERTY_AUDITORY_PVT_FREQUENCY, PROPERTY_FREQUENCIES_DEFAULT)
			.split(","))
			.map(s -> s.trim())
			.mapToInt(s -> Integer.valueOf(s))
			.forEach(f -> frequenciesProperty().get().add(f));
	}

	public void save() throws IOException {
		final Properties p = new Properties();

		p.setProperty(PROPERTY_DIRECTORY,				directoryProperty().getValue());
		p.setProperty(PROPERTY_INPUT,					inputProperty().getValue().toString());
		p.setProperty(PROPERTY_AUDITORY_PVT_FREQUENCIES,				PROPERTY_FREQUENCIES_DEFAULT);
		p.setProperty(PROPERTY_USE_AUTO_COMPLETION,		Boolean.toString(useAutoCompletionProperty().getValue()));

		p.setProperty(PROPERTY_VISUAL_LOCATION_COLOR, visualLocationColorProperty().getValue());
		p.setProperty(PROPERTY_DUAL_AUDITORY_LOCATION_COLOR, dualAuditoryLocationColorProperty().getValue());
		p.setProperty(PROPERTY_VISUAL_PVT_GO_COLOR, visualPVTgoColorProperty().getValue());
		p.setProperty(PROPERTY_VISUAL_PVT_NO_GO_COLOR, visualPVTnoGoColorProperty().getValue());
		p.setProperty(PROPERTY_MACKWORTH_COLOR, mackworthColorProperty().getValue());
		p.setProperty(PROPERTY_SPATIAL_WORKING_MEMORY_COLOR, spatialWorkingMemoryColorProperty().getValue());

		p.setProperty(PROPERTY_AUDITORY_PVT_FREQUENCY,				Integer.toString(auditoryPVTfrequencyProperty().getValue()));
		p.setProperty(PROPERTY_AUDITORY_PVT_VOLUME,					Double.toString(auditoryPVTvolumeProperty().getValue()));
		p.setProperty(PROPERTY_AUDITORY_PVT_USE_NO_GO,				Boolean.toString(auditoryPVTuseNoGoProperty().getValue()));
		p.setProperty(PROPERTY_AUDITORY_PVT_PULSEDURATION,			Integer.toString(auditoryPVTpulseDurationProperty().getValue()));
		p.setProperty(PROPERTY_AUDITORY_PVT_TIMEOUT,					Integer.toString(auditoryPVTtimeoutProperty().getValue()));
		p.setProperty(PROPERTY_AUDITORY_PVT_MINIMUM_DELAY,			Integer.toString(auditoryPVTminimumDelayProperty().getValue()));
		p.setProperty(PROPERTY_AUDITORY_PVT_MAXIMUM_DELAY,			Integer.toString(auditoryPVTmaximumDelayProperty().getValue()));
		p.setProperty(PROPERTY_AUDITORY_PVT_REPETITIONS,				Integer.toString(auditoryPVTrepetitionsProperty().getValue()));
		p.setProperty(PROPERTY_AUDITORY_PVT_MINIMUM_RESPONSE_TIME,	Integer.toString(auditoryPVTminimumResponseTimeProperty().getValue()));

		p.setProperty(PROPERTY_VISUAL_PVT_USE_NO_GO,				Boolean.toString(visualPVTuseNoGoProperty().getValue()));
		p.setProperty(PROPERTY_VISUAL_PVT_PULSEDURATION,			Integer.toString(visualPVTpulseDurationProperty().getValue()));
		p.setProperty(PROPERTY_VISUAL_PVT_TIMEOUT,					Integer.toString(visualPVTtimeoutProperty().getValue()));
		p.setProperty(PROPERTY_VISUAL_PVT_MINIMUM_DELAY,			Integer.toString(visualPVTminimumDelayProperty().getValue()));
		p.setProperty(PROPERTY_VISUAL_PVT_MAXIMUM_DELAY,			Integer.toString(visualPVTmaximumDelayProperty().getValue()));
		p.setProperty(PROPERTY_VISUAL_PVT_REPETITIONS,				Integer.toString(visualPVTrepetitionsProperty().getValue()));


		p.setProperty(PROPERTY_VOICE_FEMALE, 			Boolean.toString(voiceFemaleProperty().getValue()));
		p.setProperty(PROPERTY_VOICE_MALE, 				Boolean.toString(voiceMaleProperty().getValue()));
		p.setProperty(PROPERTY_VOICE_GERMAN, 			Boolean.toString(voiceGermanProperty().getValue()));
		p.setProperty(PROPERTY_VOICE_ENGLISH, 			Boolean.toString(voiceEnglishProperty().getValue()));



		p.setProperty(PROPERTY_VISUAL_IDENTITY_STIMULUS_TYPE, 			visualIdentityStimulusTypeProperty().getValue());
		p.setProperty(PROPERTY_AUDITORY_STIMULUS_TYPE, 			auditoryStimulusTypeProperty().getValue());

		p.setProperty(PROPERTY_DUAL_AUDITORY_LOCATION_STIMULUS_TYPE, 			dualAuditoryLocationStimulusTypeProperty().getValue());
		p.setProperty(PROPERTY_DUAL_AUDITORY_IDENTITY_FIRST_STIMULUS_TYPE, 			dualAuditoryIdentityFirstStimulusTypeProperty().getValue());
		p.setProperty(PROPERTY_DUAL_AUDITORY_IDENTITY_SECOND_STIMULUS_TYPE, 			dualAuditoryIdentitySecondStimulusTypeProperty().getValue());
		p.setProperty(PROPERTY_DUAL_IDENTITY_LOCATION_STIMULUS_TYPE, 			dualIdentityLocationStimulusTypeProperty().getValue());


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


		p.setProperty(PROPERTY_DUAL_AUDITORY_LOCATION_SEQUENCE_LENGTH, 		Integer.toString(dualAuditoryLocationSequenceLengthProperty().getValue()));
		p.setProperty(PROPERTY_DUAL_AUDITORY_LOCATION_SEQUENCE_N_BACK_LEVEL, 	Integer.toString(dualAuditoryLocationSequenceNBackLevelProperty().getValue()));
		p.setProperty(PROPERTY_DUAL_AUDITORY_LOCATION_INTERVAL, 	Integer.toString(dualAuditoryLocationIntervalProperty().getValue()));
		p.setProperty(PROPERTY_DUAL_AUDITORY_LOCATION_ROW_COUNT, 	Integer.toString(dualAuditoryLocationRowCountProperty().getValue()));

		p.setProperty(PROPERTY_DUAL_AUDITORY_LOCATION_FIRST_SEQUENCE_N_REPEAT, 		Integer.toString(dualAuditoryLocationFirstSequenceNRepeatProperty().getValue()));
		p.setProperty(PROPERTY_DUAL_AUDITORY_LOCATION_FIRST_SEQUENCE_N_MATCH, 		Integer.toString(dualAuditoryLocationFirstSequenceNMatchProperty().getValue()));
		p.setProperty(PROPERTY_DUAL_AUDITORY_LOCATION_FIRST_SEQUENCE_N_LURES, 		Integer.toString(dualAuditoryLocationFirstSequenceNLuresProperty().getValue()));
		p.setProperty(PROPERTY_DUAL_AUDITORY_LOCATION_FIRST_SEQUENCE_RE_USE_ELEMENT, Boolean.toString(dualAuditoryLocationFirstSequenceReUseElementProperty().getValue()));

		p.setProperty(PROPERTY_DUAL_AUDITORY_LOCATION_SECOND_SEQUENCE_N_REPEAT, 		Integer.toString(dualAuditoryLocationSecondSequenceNRepeatProperty().getValue()));
		p.setProperty(PROPERTY_DUAL_AUDITORY_LOCATION_SECOND_SEQUENCE_N_MATCH, 		Integer.toString(dualAuditoryLocationSecondSequenceNMatchProperty().getValue()));
		p.setProperty(PROPERTY_DUAL_AUDITORY_LOCATION_SECOND_SEQUENCE_N_LURES, 		Integer.toString(dualAuditoryLocationSecondSequenceNLuresProperty().getValue()));
		p.setProperty(PROPERTY_DUAL_AUDITORY_LOCATION_SECOND_SEQUENCE_RE_USE_ELEMENT, Boolean.toString(dualAuditoryLocationSecondSequenceReUseElementProperty().getValue()));


		p.setProperty(PROPERTY_DUAL_AUDITORY_IDENTITY_SEQUENCE_LENGTH, 		Integer.toString(dualAuditoryIdentitySequenceLengthProperty().getValue()));
		p.setProperty(PROPERTY_DUAL_AUDITORY_IDENTITY_SEQUENCE_N_BACK_LEVEL, 	Integer.toString(dualAuditoryIdentitySequenceNBackLevelProperty().getValue()));
		p.setProperty(PROPERTY_DUAL_AUDITORY_IDENTITY_INTERVAL, 	Integer.toString(dualAuditoryIdentityIntervalProperty().getValue()));

		p.setProperty(PROPERTY_DUAL_AUDITORY_IDENTITY_FIRST_SEQUENCE_N_REPEAT, 		Integer.toString(dualAuditoryIdentityFirstSequenceNRepeatProperty().getValue()));
		p.setProperty(PROPERTY_DUAL_AUDITORY_IDENTITY_FIRST_SEQUENCE_N_MATCH, 		Integer.toString(dualAuditoryIdentityFirstSequenceNMatchProperty().getValue()));
		p.setProperty(PROPERTY_DUAL_AUDITORY_IDENTITY_FIRST_SEQUENCE_N_LURES, 		Integer.toString(dualAuditoryIdentityFirstSequenceNLuresProperty().getValue()));
		p.setProperty(PROPERTY_DUAL_AUDITORY_IDENTITY_FIRST_SEQUENCE_RE_USE_ELEMENT, Boolean.toString(dualAuditoryIdentityFirstSequenceReUseElementProperty().getValue()));

		p.setProperty(PROPERTY_DUAL_AUDITORY_IDENTITY_SECOND_SEQUENCE_N_REPEAT, 		Integer.toString(dualAuditoryIdentitySecondSequenceNRepeatProperty().getValue()));
		p.setProperty(PROPERTY_DUAL_AUDITORY_IDENTITY_SECOND_SEQUENCE_N_MATCH, 		Integer.toString(dualAuditoryIdentitySecondSequenceNMatchProperty().getValue()));
		p.setProperty(PROPERTY_DUAL_AUDITORY_IDENTITY_SECOND_SEQUENCE_N_LURES, 		Integer.toString(dualAuditoryIdentitySecondSequenceNLuresProperty().getValue()));
		p.setProperty(PROPERTY_DUAL_AUDITORY_IDENTITY_SECOND_SEQUENCE_RE_USE_ELEMENT, Boolean.toString(dualAuditoryIdentitySecondSequenceReUseElementProperty().getValue()));


		p.setProperty(PROPERTY_DUAL_IDENTITY_LOCATION_SEQUENCE_LENGTH, 		Integer.toString(dualIdentityLocationSequenceLengthProperty().getValue()));
		p.setProperty(PROPERTY_DUAL_IDENTITY_LOCATION_SEQUENCE_N_BACK_LEVEL, 	Integer.toString(dualIdentityLocationSequenceNBackLevelProperty().getValue()));
		p.setProperty(PROPERTY_DUAL_IDENTITY_LOCATION_INTERVAL, 	Integer.toString(dualIdentityLocationIntervalProperty().getValue()));
		p.setProperty(PROPERTY_DUAL_IDENTITY_LOCATION_ROW_COUNT, 	Integer.toString(dualIdentityLocationRowCountProperty().getValue()));

		p.setProperty(PROPERTY_DUAL_IDENTITY_LOCATION_FIRST_SEQUENCE_N_REPEAT, 		Integer.toString(dualIdentityLocationFirstSequenceNRepeatProperty().getValue()));
		p.setProperty(PROPERTY_DUAL_IDENTITY_LOCATION_FIRST_SEQUENCE_N_MATCH, 		Integer.toString(dualIdentityLocationFirstSequenceNMatchProperty().getValue()));
		p.setProperty(PROPERTY_DUAL_IDENTITY_LOCATION_FIRST_SEQUENCE_N_LURES, 		Integer.toString(dualIdentityLocationFirstSequenceNLuresProperty().getValue()));
		p.setProperty(PROPERTY_DUAL_IDENTITY_LOCATION_FIRST_SEQUENCE_RE_USE_ELEMENT, Boolean.toString(dualIdentityLocationFirstSequenceReUseElementProperty().getValue()));

		p.setProperty(PROPERTY_DUAL_IDENTITY_LOCATION_SECOND_SEQUENCE_N_REPEAT, 		Integer.toString(dualIdentityLocationSecondSequenceNRepeatProperty().getValue()));
		p.setProperty(PROPERTY_DUAL_IDENTITY_LOCATION_SECOND_SEQUENCE_N_MATCH, 		Integer.toString(dualIdentityLocationSecondSequenceNMatchProperty().getValue()));
		p.setProperty(PROPERTY_DUAL_IDENTITY_LOCATION_SECOND_SEQUENCE_N_LURES, 		Integer.toString(dualIdentityLocationSecondSequenceNLuresProperty().getValue()));
		p.setProperty(PROPERTY_DUAL_IDENTITY_LOCATION_SECOND_SEQUENCE_RE_USE_ELEMENT, Boolean.toString(dualIdentityLocationSecondSequenceReUseElementProperty().getValue()));


		p.setProperty(PROPERTY_USE_VOICE_RECOGNITION, 	Boolean.toString(useVoiceRecognitionProperty().getValue()));
		p.setProperty(PROPERTY_MACKWORTH_LENGTH, 		Integer.toString(mackworthLengthProperty().getValue()));
		p.setProperty(PROPERTY_MACKWORTH_TARGETS, 		mackworthTargetsProperty().getValue());
		p.setProperty(PROPERTY_MACKWORTH_INTERVAL, 		Integer.toString(mackworthIntervalProperty().getValue()));
		p.setProperty(PROPERTY_MACKWORTH_N_CIRCLES,		Integer.toString(mackworthNCirclesProperty().getValue()));

		p.setProperty(PROPERTY_SPATIAL_WORKING_MEMORY_FRAMES, Integer.toString(spatialWorkingMemoryFramesProperty().getValue()));
		p.setProperty(PROPERTY_SPATIAL_WORKING_MEMORY_REPETITIONS, Integer.toString(spatialWorkingMemoryRepetitionsProperty().getValue()));
		p.setProperty(PROPERTY_SPATIAL_WORKING_MEMORY_INTERVAL, Integer.toString(spatialWorkingMemoryIntervalProperty().getValue()));
		p.setProperty(PROPERTY_SPATIAL_WORKING_MEMORY_INITIAL_DELAY, Integer.toString(spatialWorkingMemoryInitialDelayProperty().getValue()));


		p.store(new FileOutputStream("config/" + _configFilename), CONFIG_COMMENT);


	}


	public StringProperty visualPVTgoColorProperty(){return _visualPVTgoColor;}
	public StringProperty visualPVTnoGoColorProperty(){return _visualPVTnoGoColor;}
	public StringProperty visualLocationColorProperty(){return _visualLocationColor;}
	public StringProperty dualAuditoryLocationColorProperty(){return _dualAuditoryLocationColor;}
	public StringProperty mackworthColorProperty(){return _mackworthColor;}
	public StringProperty spatialWorkingMemoryColorProperty(){return _spatialWorkingMemoryColor;}


	public IntegerProperty spatialWorkingMemoryFramesProperty(){return _spatialWorkingMemoryFrames;}
	public IntegerProperty spatialWorkingMemoryRepetitionsProperty(){return _spatialWorkingMemoryRepetitions;}
	public IntegerProperty spatialWorkingMemoryIntervalProperty(){return _spatialWorkingMemoryInterval;}
	public IntegerProperty spatialWorkingMemoryInitialDelayProperty(){return _spatialWorkingMemoryInitialDelay;}

	public StringProperty directoryProperty() {
		return _directory;
	}

	public ObjectProperty<Input> inputProperty() {
		return _input;
	}

	public BooleanProperty useAutoCompletionProperty() {
		return _useAutoCompletion;
	}


	public IntegerProperty auditoryPVTfrequencyProperty() {
		return _auditoryPVTfrequency;
	}

	public IntegerProperty auditoryPVTvolumeProperty() {
		return _auditoryPVTvolume;
	}


	public BooleanProperty auditoryPVTuseNoGoProperty() {
		return _auditoryPVTuseNoGo;
	}

	public IntegerProperty auditoryPVTpulseDurationProperty() {
		return _auditoryPVTpulseDuration;
	}

	public IntegerProperty auditoryPVTtimeoutProperty() {
		return _auditoryPVTtimeout;
	}

	public IntegerProperty auditoryPVTminimumDelayProperty() {
		return _auditoryPVTminimumDelay;
	}

	public IntegerProperty auditoryPVTmaximumDelayProperty() {
		return _auditoryPVTmaximumDelay;
	}

	public IntegerProperty auditoryPVTminimumResponseTimeProperty() {
		return _auditoryPVTminimumResponseTime;
	}

	public IntegerProperty auditoryPVTrepetitionsProperty() {
		return _auditoryPVTrepetitions;
	}

	public IntegerProperty visualPVTfrequencyProperty() {
		return _visualPVTfrequency;
	}

	public IntegerProperty visualPVTvolumeProperty() {
		return _visualPVTvolume;
	}


	public BooleanProperty visualPVTuseNoGoProperty() {
		return _visualPVTuseNoGo;
	}

	public IntegerProperty visualPVTpulseDurationProperty() {
		return _visualPVTpulseDuration;
	}

	public IntegerProperty visualPVTtimeoutProperty() {
		return _visualPVTtimeout;
	}

	public IntegerProperty visualPVTminimumDelayProperty() {
		return _visualPVTminimumDelay;
	}

	public IntegerProperty visualPVTmaximumDelayProperty() {
		return _visualPVTmaximumDelay;
	}

	public IntegerProperty visualPVTminimumResponseTimeProperty() {
		return _visualPVTminimumResponseTime;
	}

	public IntegerProperty visualPVTrepetitionsProperty() {
		return _visualPVTrepetitions;
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
	public StringProperty dualAuditoryLocationStimulusTypeProperty(){return _dualAuditoryLocationStimulusType;}
	public StringProperty dualAuditoryIdentityFirstStimulusTypeProperty(){return _dualAuditoryIdentityFirstStimulusType;}
	public StringProperty dualAuditoryIdentitySecondStimulusTypeProperty(){return _dualAuditoryIdentitySecondStimulusType;}
	public StringProperty dualIdentityLocationStimulusTypeProperty(){return _dualIdentityLocationStimulusType;}

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

	//dual auditory location

	public IntegerProperty dualAuditoryLocationSequenceLengthProperty(){return _dualAuditoryLocationSequenceLength;}

	public IntegerProperty dualAuditoryLocationIntervalProperty(){return _dualAuditoryLocationInterval;}

	public IntegerProperty dualAuditoryLocationSequenceNBackLevelProperty(){return _dualAuditoryLocationSequenceNBackLevel;}

	public IntegerProperty dualAuditoryLocationRowCountProperty(){return _dualAuditoryLocationRowCount;}


	public IntegerProperty dualAuditoryLocationFirstSequenceNRepeatProperty(){return _dualAuditoryLocationFirstSequenceNRepeat;}

	public IntegerProperty dualAuditoryLocationFirstSequenceNMatchProperty(){return _dualAuditoryLocationFirstSequenceNMatch;}

	public IntegerProperty dualAuditoryLocationFirstSequenceNLuresProperty(){return _dualAuditoryLocationFirstSequenceNLures;}

	public BooleanProperty dualAuditoryLocationFirstSequenceReUseElementProperty(){return _dualAuditoryLocationFirstSequenceReUseElement;}

	public IntegerProperty dualAuditoryLocationSecondSequenceNRepeatProperty(){return _dualAuditoryLocationSecondSequenceNRepeat;}

	public IntegerProperty dualAuditoryLocationSecondSequenceNMatchProperty(){return _dualAuditoryLocationSecondSequenceNMatch;}

	public IntegerProperty dualAuditoryLocationSecondSequenceNLuresProperty(){return _dualAuditoryLocationSecondSequenceNLures;}


	public BooleanProperty dualAuditoryLocationSecondSequenceReUseElementProperty(){return _dualAuditoryLocationSecondSequenceReUseElement;}

	//dual identity location
	public IntegerProperty dualIdentityLocationSequenceLengthProperty(){return _dualIdentityLocationSequenceLength;}

	public IntegerProperty dualIdentityLocationIntervalProperty(){return _dualIdentityLocationInterval;}

	public IntegerProperty dualIdentityLocationSequenceNBackLevelProperty(){return _dualIdentityLocationSequenceNBackLevel;}

	public IntegerProperty dualIdentityLocationRowCountProperty(){return _dualIdentityLocationRowCount;}


	public IntegerProperty dualIdentityLocationFirstSequenceNRepeatProperty(){return _dualIdentityLocationFirstSequenceNRepeat;}

	public IntegerProperty dualIdentityLocationFirstSequenceNMatchProperty(){return _dualIdentityLocationFirstSequenceNMatch;}

	public IntegerProperty dualIdentityLocationFirstSequenceNLuresProperty(){return _dualIdentityLocationFirstSequenceNLures;}

	public BooleanProperty dualIdentityLocationFirstSequenceReUseElementProperty(){return _dualIdentityLocationFirstSequenceReUseElement;}

	public IntegerProperty dualIdentityLocationSecondSequenceNRepeatProperty(){return _dualIdentityLocationSecondSequenceNRepeat;}

	public IntegerProperty dualIdentityLocationSecondSequenceNMatchProperty(){return _dualIdentityLocationSecondSequenceNMatch;}

	public IntegerProperty dualIdentityLocationSecondSequenceNLuresProperty(){return _dualIdentityLocationSecondSequenceNLures;}


	public BooleanProperty dualIdentityLocationSecondSequenceReUseElementProperty(){return _dualIdentityLocationSecondSequenceReUseElement;}


	//dual auditory identity
	public IntegerProperty dualAuditoryIdentitySequenceLengthProperty(){return _dualAuditoryIdentitySequenceLength;}

	public IntegerProperty dualAuditoryIdentityIntervalProperty(){return _dualAuditoryIdentityInterval;}

	public IntegerProperty dualAuditoryIdentitySequenceNBackLevelProperty(){return _dualAuditoryIdentitySequenceNBackLevel;}


	public IntegerProperty dualAuditoryIdentityFirstSequenceNRepeatProperty(){return _dualAuditoryIdentityFirstSequenceNRepeat;}

	public IntegerProperty dualAuditoryIdentityFirstSequenceNMatchProperty(){return _dualAuditoryIdentityFirstSequenceNMatch;}

	public IntegerProperty dualAuditoryIdentityFirstSequenceNLuresProperty(){return _dualAuditoryIdentityFirstSequenceNLures;}

	public BooleanProperty dualAuditoryIdentityFirstSequenceReUseElementProperty(){return _dualAuditoryIdentityFirstSequenceReUseElement;}

	public IntegerProperty dualAuditoryIdentitySecondSequenceNRepeatProperty(){return _dualAuditoryIdentitySecondSequenceNRepeat;}

	public IntegerProperty dualAuditoryIdentitySecondSequenceNMatchProperty(){return _dualAuditoryIdentitySecondSequenceNMatch;}

	public IntegerProperty dualAuditoryIdentitySecondSequenceNLuresProperty(){return _dualAuditoryIdentitySecondSequenceNLures;}


	public BooleanProperty dualAuditoryIdentitySecondSequenceReUseElementProperty(){return _dualAuditoryIdentitySecondSequenceReUseElement;}





	public BooleanProperty useVoiceRecognitionProperty(){return _useVoiceRecognition;}

	public IntegerProperty mackworthLengthProperty(){return _mackworthLength;}

	public StringProperty mackworthTargetsProperty(){return _mackworthTargets;}

	public IntegerProperty mackworthIntervalProperty(){return _mackworthInterval;}

	public IntegerProperty mackworthNCirclesProperty(){return _mackworthNCircles;}


}