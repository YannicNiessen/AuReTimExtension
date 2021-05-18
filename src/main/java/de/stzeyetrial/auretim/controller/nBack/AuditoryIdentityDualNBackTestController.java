package de.stzeyetrial.auretim.controller.nBack;

import de.stzeyetrial.auretim.audio.SpeechSynthesizer;
import de.stzeyetrial.auretim.config.Config;
import de.stzeyetrial.auretim.screens.Screens;
import de.stzeyetrial.auretim.session.Session;
import de.stzeyetrial.auretim.tasks.DualNBackTask;
import de.stzeyetrial.auretim.util.Result;
import de.stzeyetrial.auretim.util.Stimulus;
import de.stzeyetrial.auretim.util.StimulusSet;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AuditoryIdentityDualNBackTestController extends AbstractNBackTestController {


    private Node _stimulusNode;

    private StimulusSet _auditoryStimulusSet;

    private Stimulus.Type _auditoryStimulusType;


    @FXML
    private HBox _stimulusContainerBox;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        System.out.println("Initialize called");

        setConfig();
        SpeechSynthesizer.setup();

    }

    @Override
    protected void start() {

        unbind();

        final int nBackLevel			= Config.getInstance().dualAuditoryIdentitySequenceNBackLevelProperty().get();

        final int length				= Config.getInstance().dualAuditoryIdentitySequenceLengthProperty().get();
        final int nRepeatAuditorySequence				= Config.getInstance().dualAuditoryIdentityFirstSequenceNRepeatProperty().get();
        final int nMatchAuditorySequence				= Config.getInstance().dualAuditoryIdentityFirstSequenceNMatchProperty().get();
        final int nLuresAuditorySequence				= Config.getInstance().dualAuditoryIdentityFirstSequenceNLuresProperty().get();
        final boolean reUseElementsAuditorySequence		= Config.getInstance().dualAuditoryIdentityFirstSequenceReUseElementProperty().get();


        final int nRepeatIdentitySequence				= Config.getInstance().dualAuditoryIdentitySecondSequenceNRepeatProperty().get();
        final int nMatchIdentitySequence				= Config.getInstance().dualAuditoryIdentitySecondSequenceNMatchProperty().get();
        final int nLuresIdentitySequence				= Config.getInstance().dualAuditoryIdentitySecondSequenceNLuresProperty().get();
        final boolean reUseElementsIdentitySequence		= Config.getInstance().dualAuditoryIdentitySecondSequenceReUseElementProperty().get();

        final int timeout				= Config.getInstance().dualAuditoryIdentityIntervalProperty().get();

        final int nOptionsAuditorySequence = _auditoryStimulusSet.get_elements().size();
        final int nOptionsIdentitySequence = _stimulusSet.get_elements().size();

        final List<Result> results = Session.getCurrentSession().getResults();
        results.clear();

        final DualNBackTask task;
        try {
            task = new DualNBackTask(results, length, nOptionsAuditorySequence, nRepeatAuditorySequence, nMatchAuditorySequence, nLuresAuditorySequence, nOptionsIdentitySequence, nRepeatIdentitySequence, nMatchIdentitySequence, nLuresIdentitySequence, nBackLevel, timeout);
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
        int audioValue;

        if (stimulus.get_values().length == 2){
            audioValue = stimulus.get_values()[0];
            value = stimulus.get_values()[1];
        }else{
            return;
        }

        switch (_stimulusType){

            case DIGIT:
            case LETTER:
                ((TextField) _stimulusNode).setText(_stimulusSet.get_elements().get(value));
                break;
            case COLOR:
                String hexColor = _stimulusSet.get_elements().get(value);
                ((Rectangle) _stimulusNode).setFill(Paint.valueOf(hexColor));
                break;
            case IMAGE:
                String imagePath = _stimulusSet.get_elements().get(value);
                Image image = new Image("file:" + imagePath);
                ((ImageView) _stimulusNode).setImage(image);
        }

        String output;

        switch (_auditoryStimulusType){

            case DIGIT:
            case LETTER:
                output = _auditoryStimulusSet.get_elements().get(audioValue);
                break;
            default:
                throw new Exception("Stimulus type is not audio compatible");
        }

        SpeechSynthesizer.speak(output);




    }


    protected void setLayout(){
        _stimulusContainerBox.getChildren().clear();
        double availableSpace= Math.min(_stimulusContainerBox.getPrefWidth(), _stimulusContainerBox.getPrefHeight());

        _stimulusNode = Stimulus.getContainerNode(_stimulusType, availableSpace, availableSpace);

        _stimulusContainerBox.getChildren().add(_stimulusNode);
    }

    public void setConfig(){
        _auditoryStimulusSet = StimulusSet.getSet(Config.getInstance().dualAuditoryIdentityFirstStimulusTypeProperty().getValue());
        _stimulusSet = StimulusSet.getSet(Config.getInstance().dualAuditoryIdentitySecondStimulusTypeProperty().getValue());
        _auditoryStimulusType = _auditoryStimulusSet.get_type();
        _stimulusType = _stimulusSet.get_type();

        setLayout();
    }

}
