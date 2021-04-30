package de.stzeyetrial.auretim.controller.nBack;

import de.stzeyetrial.auretim.audio.SpeechSynthesizer;
import de.stzeyetrial.auretim.util.Stimulus;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class VisualIdentityDualNBackTestController extends AbstractNBackTestController {


    private Node _stimulusNode;


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

    }

    @Override
    protected void outputStimulus(Stimulus stimulus) throws Exception {
        int value;
        int audioValue;

        System.out.println("output");
        System.out.println(stimulus.get_values().length);
        if (stimulus.get_values().length == 2){
            value = stimulus.get_values()[0];
            audioValue = stimulus.get_values()[1];
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


    protected void setLayout(){
        _stimulusContainerBox.getChildren().clear();
        double availableSpace= Math.min(_stimulusContainerBox.getPrefWidth(), _stimulusContainerBox.getPrefHeight());

        _stimulusNode = Stimulus.getContainerNode(_stimulusType, availableSpace, availableSpace);

        _stimulusContainerBox.getChildren().add(_stimulusNode);
    }

}
