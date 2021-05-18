package de.stzeyetrial.auretim.controller.nBack;

import de.stzeyetrial.auretim.config.Config;
import de.stzeyetrial.auretim.screens.Screens;
import de.stzeyetrial.auretim.session.Session;
import de.stzeyetrial.auretim.tasks.MonoNBackTask;
import de.stzeyetrial.auretim.util.Result;
import de.stzeyetrial.auretim.util.Stimulus;
import de.stzeyetrial.auretim.util.StimulusSet;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class IdentityNBackTestController extends AbstractNBackTestController {


    private Node _stimulusNode;


    @FXML
    private HBox _stimulusContainerBox;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        System.out.println("Initialize called");

        setConfig();

    }

    @Override
    protected void start() {

        unbind();

        final int length				= Config.getInstance().visualIdentitySequenceLengthProperty().get();
        final int nRepeat				= Config.getInstance().visualIdentitySequenceNRepeatProperty().get();
        final int nMatch				= Config.getInstance().visualIdentitySequenceNMatchProperty().get();
        final int nLures				= Config.getInstance().visualIdentitySequenceNLuresProperty().get();
        final int nBackLevel			= Config.getInstance().visualIdentitySequenceNBackLevelProperty().get();
        final int timeout				= Config.getInstance().visualIdentityIntervalProperty().get();
        final boolean reUseElements		= Config.getInstance().visualIdentitySequenceReUseElementProperty().get();

        final int nOptions = _stimulusSet.get_elements().size();

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
    protected void outputStimulus(Stimulus stimulus) {
        int value;
        if (stimulus.get_values().length != 0){
            value = stimulus.get_values()[0];
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
                System.out.println(imagePath);
                Image image = new Image("file:" + imagePath);
                ((ImageView) _stimulusNode).setImage(image);
        }
    }

    public void setConfig(){
        _stimulusSet = StimulusSet.getSet(Config.getInstance().visualIdentityStimulusTypeProperty().getValue());

        _stimulusType = _stimulusSet.get_type();

        setLayout();
    }


    protected void setLayout(){
        _stimulusContainerBox.getChildren().clear();
        double availableSpace= Math.min(_stimulusContainerBox.getPrefWidth(), _stimulusContainerBox.getPrefHeight());

        _stimulusNode = Stimulus.getContainerNode(_stimulusType, availableSpace, availableSpace);

        _stimulusContainerBox.getChildren().add(_stimulusNode);
    }

}
