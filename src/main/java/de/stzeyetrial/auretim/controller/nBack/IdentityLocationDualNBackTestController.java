package de.stzeyetrial.auretim.controller.nBack;

import de.stzeyetrial.auretim.config.Config;
import de.stzeyetrial.auretim.screens.Screens;
import de.stzeyetrial.auretim.session.Session;
import de.stzeyetrial.auretim.tasks.DualNBackTask;
import de.stzeyetrial.auretim.tasks.MonoNBackTask;
import de.stzeyetrial.auretim.util.Result;
import de.stzeyetrial.auretim.util.Stimulus;
import de.stzeyetrial.auretim.util.StimulusSet;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class IdentityLocationDualNBackTestController extends AbstractNBackTestController {


    private TextField _stimulusTextField;
    private GridPane _stimulusGridPane;


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

        final int nBackLevel			= Config.getInstance().dualIdentityLocationSequenceNBackLevelProperty().get();

        final int length				= Config.getInstance().dualIdentityLocationSequenceLengthProperty().get();
        final int nRepeatIdentitySequence				= Config.getInstance().dualIdentityLocationFirstSequenceNRepeatProperty().get();
        final int nMatchIdentitySequence				= Config.getInstance().dualIdentityLocationFirstSequenceNMatchProperty().get();
        final int nLuresIdentitySequence				= Config.getInstance().dualIdentityLocationFirstSequenceNLuresProperty().get();
        final boolean reUseElementsIdentitySequence		= Config.getInstance().dualIdentityLocationFirstSequenceReUseElementProperty().get();


        final int nRepeatLocationSequence				= Config.getInstance().dualIdentityLocationSecondSequenceNRepeatProperty().get();
        final int nMatchLocationSequence				= Config.getInstance().dualIdentityLocationSecondSequenceNMatchProperty().get();
        final int nLuresLocationSequence				= Config.getInstance().dualIdentityLocationSecondSequenceNLuresProperty().get();
        final boolean reUseElementsLocationSequence		= Config.getInstance().dualIdentityLocationSecondSequenceReUseElementProperty().get();

        final int timeout				= Config.getInstance().dualIdentityLocationIntervalProperty().get();

        final int nOptionsIdentitySequence = _stimulusSet.get_elements().size();

        int rowNumber = Config.getInstance().dualIdentityLocationRowCountProperty().get();

        final int nOptionsLocationSequence = rowNumber * rowNumber;

        final List<Result> results = Session.getCurrentSession().getResults();
        results.clear();

        final DualNBackTask task;
        try {
            task = new DualNBackTask(results, length, nOptionsIdentitySequence, nRepeatIdentitySequence, nMatchIdentitySequence, nLuresIdentitySequence, reUseElementsIdentitySequence, nOptionsLocationSequence, nRepeatLocationSequence, nMatchLocationSequence, nLuresLocationSequence, reUseElementsLocationSequence, nBackLevel, timeout);
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


        int rowNumber = Config.getInstance().dualIdentityLocationRowCountProperty().getValue();
        _stimulusGridPane = new GridPane();

        double blockSize = (availableSpace * 0.8) / rowNumber;
        double gapSize = (availableSpace * 0.2) / (rowNumber - 1);

        for(int column = 0; column < rowNumber; column++){
            for (int row = 0; row < rowNumber; row++){
                Node stimulusNode = Stimulus.getContainerNode(_stimulusType, blockSize, blockSize);

                _stimulusGridPane.add(stimulusNode, row, column);
            }
        }

        _stimulusGridPane.setVgap(gapSize);
        _stimulusGridPane.setHgap(gapSize);
        _stimulusContainerBox.getChildren().add(_stimulusGridPane);
    }


    private void clearBoxes(){
        for (int i = 0; i < _stimulusGridPane.getChildren().toArray().length; i++){
            Node currentNode = _stimulusGridPane.getChildren().get(i);
            switch (_stimulusType){

                case DIGIT:
                case LETTER:
                    ((TextField) currentNode).setText("");
                    break;
                case COLOR:
                    ((Rectangle) currentNode).setFill(Paint.valueOf("white"));
                    break;
                case IMAGE:
                    ((ImageView) currentNode).setImage(null);
            }

        }
    }

    @Override
    protected void outputStimulus(Stimulus stimulus) throws Exception {

            int locationValue;
            int identityValue;
            if (stimulus.get_values().length == 2){
                identityValue = stimulus.get_values()[0];
                locationValue = stimulus.get_values()[1];
            }else{
                return;
            }

            clearBoxes();

            Node stimulusNode = _stimulusGridPane.getChildren().get(locationValue);

            switch (_stimulusType){

                case DIGIT:
                case LETTER:
                    ((TextField) stimulusNode).setText(_stimulusSet.get_elements().get(identityValue));
                    break;
                case COLOR:
                    String hexColor = _stimulusSet.get_elements().get(identityValue);
                    ((Rectangle) stimulusNode).setFill(Paint.valueOf(hexColor));
                    break;
                case IMAGE:
                    String imagePath = _stimulusSet.get_elements().get(identityValue);
                    Image image = new Image("file:" + imagePath);
                    ((ImageView) stimulusNode).setImage(image);
            }

    }

    public void setConfig(){
        _stimulusSet = StimulusSet.getSet(Config.getInstance().dualIdentityLocationStimulusTypeProperty().getValue());
        _stimulusType = _stimulusSet.get_type();

        setLayout();
    }

}
