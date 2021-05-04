package de.stzeyetrial.auretim.controller.nBack;

import de.stzeyetrial.auretim.config.Config;
import de.stzeyetrial.auretim.screens.Screens;
import de.stzeyetrial.auretim.session.Session;
import de.stzeyetrial.auretim.tasks.MonoNBackTask;
import de.stzeyetrial.auretim.util.Result;
import de.stzeyetrial.auretim.util.Stimulus;
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
        final int minimumResponseTime	= Config.getInstance().minimumResponseTimeProperty().get();
        
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
    protected void outputStimulus(Stimulus stimulus) {
        int value;
        if (stimulus.get_values().length != 0){
            value = stimulus.get_values()[0];
        }else{
            return;
        }

        switch (_stimulusType){

            case DIGIT:
                ((TextField) _stimulusNode).setText(Stimulus.getComputedDigit(value));
                break;
            case LETTER:
                ((TextField) _stimulusNode).setText(Stimulus.getComputedLetter(value));
                break;
            case COLOR:
                String hexColor = Stimulus.getComputedHexColor(value);
                ((Rectangle) _stimulusNode).setFill(Paint.valueOf(hexColor));
                break;
            case IMAGE:
                String imagePath = Stimulus.getComputedImagePath(value);
                Image image = new Image("file:" + imagePath);
                ((ImageView) _stimulusNode).setImage(image);
        }
    }


    protected void setLayout(){
        _stimulusContainerBox.getChildren().clear();
        double availableSpace= Math.min(_stimulusContainerBox.getPrefWidth(), _stimulusContainerBox.getPrefHeight());

        _stimulusNode = Stimulus.getContainerNode(_stimulusType, availableSpace, availableSpace);

        _stimulusContainerBox.getChildren().add(_stimulusNode);
    }

}
