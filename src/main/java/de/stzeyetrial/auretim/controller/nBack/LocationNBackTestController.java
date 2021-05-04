package de.stzeyetrial.auretim.controller.nBack;

import de.stzeyetrial.auretim.config.Config;
import de.stzeyetrial.auretim.screens.Screens;
import de.stzeyetrial.auretim.session.Session;
import de.stzeyetrial.auretim.tasks.MonoNBackTask;
import de.stzeyetrial.auretim.util.Result;
import de.stzeyetrial.auretim.util.Stimulus;
import javafx.animation.FillTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LocationNBackTestController extends AbstractNBackTestController {

    private GridPane _stimulusGridPane;
    private int _rowNumber;


    @Override
    protected void start() {
        unbind();

        final int length				= Config.getInstance().visualLocationSequenceLengthProperty().get();
        final int nRepeat				= Config.getInstance().visualLocationSequenceNRepeatProperty().get();
        final int nMatch				= Config.getInstance().visualLocationSequenceNMatchProperty().get();
        final int nLures				= Config.getInstance().visualLocationSequenceNLuresProperty().get();
        final int nBackLevel			= Config.getInstance().visualLocationSequenceNBackLevelProperty().get();
        final int timeout				= Config.getInstance().visualLocationIntervalProperty().get();
        final boolean reUseElements		= Config.getInstance().visualLocationSequenceReUseElementProperty().get();
        final int minimumResponseTime	= Config.getInstance().minimumResponseTimeProperty().get();

        final boolean useVoiceRecognition = Config.getInstance().useVoiceRecognitionProperty().get();
        int rowNumber = Config.getInstance().visualLocationRowCountProperty().getValue();

        final int nOptions = rowNumber * rowNumber;

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

    @FXML
    private HBox _stimulusContainerBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        setConfig();
    }

    @Override
    protected void setLayout() {

        _stimulusContainerBox.getChildren().clear();

        double availableSpace= Math.min(_stimulusContainerBox.getPrefWidth(), _stimulusContainerBox.getPrefHeight());

        _stimulusGridPane = new GridPane();

        double blockSize = (availableSpace * 0.8) / _rowNumber;
        double gapSize = (availableSpace * 0.2) / (_rowNumber - 1);

        for(int column = 0; column < _rowNumber; column++){
            for (int row = 0; row < _rowNumber; row++){
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
    protected void outputStimulus(Stimulus stimulus) {

            int value;
            if (stimulus.get_values().length != 0){
                value = stimulus.get_values()[0];
            }else{
                return;
            }
            for (int i = 0; i < _stimulusGridPane.getChildren().toArray().length; i++){
                ((Rectangle) _stimulusGridPane.getChildren().get(i)).setFill(Paint.valueOf("white"));
            }
            FillTransition fillBlue = new FillTransition(Duration.millis(100),((Rectangle) _stimulusGridPane.getChildren().get(value)) ,Color.WHITE, Color.BLUE);
            fillBlue.play();

    }

    @Override
    public void setConfig() {
        super.setConfig();

        if (_rowNumber != Config.getInstance().visualLocationRowCountProperty().getValue()){
            _rowNumber = Config.getInstance().visualLocationRowCountProperty().getValue();
            setLayout();
        }
    }
}
