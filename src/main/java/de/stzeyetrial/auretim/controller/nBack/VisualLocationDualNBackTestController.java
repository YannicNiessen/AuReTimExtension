package de.stzeyetrial.auretim.controller.nBack;

import de.stzeyetrial.auretim.audio.SpeechSynthesizer;
import de.stzeyetrial.auretim.util.Stimulus;
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
import java.util.ResourceBundle;

public class VisualLocationDualNBackTestController extends AbstractNBackTestController {


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

    }

    @Override
    protected void setLayout() {

        _stimulusContainerBox.getChildren().removeAll();

        double availableSpace= Math.min(_stimulusContainerBox.getPrefWidth(), _stimulusContainerBox.getPrefHeight());


        int rowNumber = 4;
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
                locationValue = stimulus.get_values()[0];
                audioValue = stimulus.get_values()[1];
            }else{
                return;
            }
            for (int i = 0; i < _stimulusGridPane.getChildren().toArray().length; i++){
                ((Rectangle) _stimulusGridPane.getChildren().get(i)).setFill(Paint.valueOf("white"));
            }
            FillTransition fillBlue = new FillTransition(Duration.millis(350),((Rectangle) _stimulusGridPane.getChildren().get(locationValue)) ,Color.WHITE, Color.BLUE);

            fillBlue.play();

            String output;

            switch (_stimulusType){

                case DIGIT:
                    output = Stimulus.getComputedDigit(audioValue);
                    break;
                case LETTER:
                    output = Stimulus.getComputedLetter(audioValue);
                    break;
                default:
                    throw new Exception("Stimulus type is not audio compatible");
            }

            SpeechSynthesizer.speak(output);

    }

}
