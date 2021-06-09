package de.stzeyetrial.auretim.controller.nBack;

import de.stzeyetrial.auretim.audio.SpeechSynthesizer;
import de.stzeyetrial.auretim.config.Config;
import de.stzeyetrial.auretim.screens.Screens;
import de.stzeyetrial.auretim.session.Session;
import de.stzeyetrial.auretim.tasks.DualNBackTask;
import de.stzeyetrial.auretim.util.Result;
import de.stzeyetrial.auretim.util.Stimulus;
import de.stzeyetrial.auretim.util.StimulusSet;
import javafx.animation.FillTransition;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AuditoryLocationDualNBackTestController extends AbstractNBackTestController {


    private TextField _stimulusTextField;
    private GridPane _stimulusGridPane;
    private String _hexColor;



    @FXML
    private HBox _stimulusContainerBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        setConfig();
    }

    @Override
    protected void start() {

        unbind();

        final int nBackLevel			= Config.getInstance().dualAuditoryLocationSequenceNBackLevelProperty().get();

        final int length				= Config.getInstance().dualAuditoryLocationSequenceLengthProperty().get();
        final int nRepeatAuditorySequence				= Config.getInstance().dualAuditoryLocationFirstSequenceNRepeatProperty().get();
        final int nMatchAuditorySequence				= Config.getInstance().dualAuditoryLocationFirstSequenceNMatchProperty().get();
        final int nLuresAuditorySequence				= Config.getInstance().dualAuditoryLocationFirstSequenceNLuresProperty().get();
        final boolean reUseElementsAuditorySequence		= Config.getInstance().dualAuditoryLocationFirstSequenceReUseElementProperty().get();


        final int nRepeatLocationSequence				= Config.getInstance().dualAuditoryLocationSecondSequenceNRepeatProperty().get();
        final int nMatchLocationSequence				= Config.getInstance().dualAuditoryLocationSecondSequenceNMatchProperty().get();
        final int nLuresLocationSequence				= Config.getInstance().dualAuditoryLocationSecondSequenceNLuresProperty().get();
        final boolean reUseElementsLocationSequence		= Config.getInstance().dualAuditoryLocationSecondSequenceReUseElementProperty().get();

        final int timeout				= Config.getInstance().dualAuditoryLocationIntervalProperty().get();

        final int nOptionsAuditorySequence = _stimulusSet.get_elements().size();

        _hexColor = Config.getInstance().dualAuditoryLocationColorProperty().getValue();

        int rowNumber = Config.getInstance().dualAuditoryLocationRowCountProperty().get();

        final int nOptionsLocationSequence = rowNumber * rowNumber;

        final List<Result> results = Session.getCurrentSession().getResults();
        results.clear();

        final DualNBackTask task;
        try {
            task = new DualNBackTask(results, length, nOptionsAuditorySequence, nRepeatAuditorySequence, nMatchAuditorySequence, nLuresAuditorySequence,reUseElementsAuditorySequence, nOptionsLocationSequence, nRepeatLocationSequence, nMatchLocationSequence, nLuresLocationSequence, reUseElementsLocationSequence, nBackLevel, timeout);
            task.setOnSucceeded(event -> getScreenManager().setScreen(Screens.RESULT));
            //task.setOnFailed(event -> getScreenManager().showException(task.getException()));
            bind(task);

            _future = _executor.submit(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void setLayout() {

        _stimulusContainerBox.getChildren().clear();

        double availableSpace= Math.min(_stimulusContainerBox.getPrefWidth(), _stimulusContainerBox.getPrefHeight());


        int rowNumber =  Config.getInstance().dualAuditoryLocationRowCountProperty().get();
        _stimulusGridPane = new GridPane();

        double blockSize = (availableSpace * 0.8) / rowNumber;
        double gapSize = (availableSpace * 0.2) / (rowNumber - 1);

        for(int column = 0; column < rowNumber; column++){
            for (int row = 0; row < rowNumber; row++){
                Rectangle rect = new Rectangle(blockSize, blockSize);
                rect.setFill(Paint.valueOf("white"));
                _stimulusGridPane.add(rect, row, column);
            }
        }

        _stimulusGridPane.setVgap(gapSize);
        _stimulusGridPane.setHgap(gapSize);
        _stimulusContainerBox.getChildren().add(_stimulusGridPane);
    }

    @Override
    protected void outputStimulus(Stimulus stimulus) throws Exception {

            int locationValue;
            int audioValue;
            if (stimulus.get_values().length == 2){
                audioValue = stimulus.get_values()[0];
                locationValue = stimulus.get_values()[1];
            }else{
                return;
            }
            for (int i = 0; i < _stimulusGridPane.getChildren().toArray().length; i++){
                ((Rectangle) _stimulusGridPane.getChildren().get(i)).setFill(Paint.valueOf("white"));
            }
            FillTransition fillBlue = new FillTransition(Duration.millis(350),((Rectangle) _stimulusGridPane.getChildren().get(locationValue)) ,Color.WHITE, Color.web(_hexColor));

            fillBlue.play();

            String output;

            switch (_stimulusType){

                case DIGIT:
                case LETTER:
                    output = _stimulusSet.get_elements().get(audioValue);
                    break;
                default:
                    throw new Exception("Stimulus type is not audio compatible");
            }

            SpeechSynthesizer.speak(output);

    }

    public void setConfig(){
        _stimulusSet = StimulusSet.getSet(Config.getInstance().dualAuditoryLocationStimulusTypeProperty().getValue());
        _stimulusType = _stimulusSet.get_type();

        setLayout();
    }

}
