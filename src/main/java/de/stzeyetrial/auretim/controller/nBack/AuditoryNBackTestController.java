package de.stzeyetrial.auretim.controller.nBack;

import de.stzeyetrial.auretim.audio.SpeechSynthesizer;
import de.stzeyetrial.auretim.config.Config;
import de.stzeyetrial.auretim.input.Input;
import de.stzeyetrial.auretim.screens.Screens;
import de.stzeyetrial.auretim.session.Session;
import de.stzeyetrial.auretim.tasks.AbstractNBackTask;
import de.stzeyetrial.auretim.tasks.MonoNBackTask;
import de.stzeyetrial.auretim.util.Result;
import de.stzeyetrial.auretim.util.Stimulus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.ResourceBundle;

public class AuditoryNBackTestController extends AbstractNBackTestController {


    @FXML
    private TextField _volumeTextField;

    @FXML
    private Button _lowerVolumeButton;

    @FXML
    private Button _higherVolumeButton;

    public AuditoryNBackTestController(){
        super();
        SpeechSynthesizer.setup();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        _volumeTextField.textProperty().bindBidirectional(Config.getInstance().auditoryPVTvolumeProperty(), NumberFormat.getIntegerInstance());
        setConfig();

    }

    @Override
    protected void start() {
        unbind();

        final int length				= Config.getInstance().auditorySequenceLengthProperty().get();
        final int nRepeat				= Config.getInstance().auditorySequenceNRepeatProperty().get();
        final int nMatch				= Config.getInstance().auditorySequenceNMatchProperty().get();
        final int nLures				= Config.getInstance().auditorySequenceNLuresProperty().get();
        final int nBackLevel			= Config.getInstance().auditorySequenceNBackLevelProperty().get();
        final int timeout				= Config.getInstance().auditoryIntervalProperty().get();
        final boolean reUseElements		= Config.getInstance().auditorySequenceReUseElementProperty().get();


        final boolean useVoiceRecognition = Config.getInstance().useVoiceRecognitionProperty().get();

        final int nOptions = 10;
        final List<Result> results = Session.getCurrentSession().getResults();
        results.clear();

        final MonoNBackTask task;
        try {
            task = new MonoNBackTask(results, length, nOptions, nRepeat, nMatch, nLures, nBackLevel,  timeout, false);
            task.setOnSucceeded(event -> getScreenManager().setScreen(Screens.RESULT));
            //task.setOnFailed(event -> getScreenManager().showException(task.getException()));
            bind(task);

        _future = _executor.submit(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void outputStimulus(Stimulus stimulus) throws Exception {

        int value;
        if (stimulus.get_values().length != 0){
            value = stimulus.get_values()[0];
        }else{
            return;
        }

        String output;

        switch (_stimulusType){

            case DIGIT:
                output = Stimulus.getComputedDigit(value);
                break;
            case LETTER:
                output = Stimulus.getComputedLetter(value);
                break;
            default:
                throw new Exception("Stimulus type is not audio compatible");
        }

        SpeechSynthesizer.speak(output);

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

    @Override
    protected void unbind() {
        super.unbind();
        if (Config.getInstance().inputProperty().get() == Input.MOUSE) {
            _lowerVolumeButton.disableProperty().unbind();
            _higherVolumeButton.disableProperty().unbind();
        }
    }

    @Override
    protected void bind(AbstractNBackTask task) {
        super.bind(task);
        if (Config.getInstance().inputProperty().get() == Input.MOUSE) {
            _lowerVolumeButton.disableProperty().bind(task.runningProperty());
            _higherVolumeButton.disableProperty().bind(task.runningProperty());
            _volumeTextField.disableProperty().bind(task.runningProperty());
        }
    }

    @Override
    protected void setLayout() {

    }
}
