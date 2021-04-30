package de.stzeyetrial.auretim.controller.nBack;

import de.stzeyetrial.auretim.util.Stimulus;
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
import java.util.ResourceBundle;

public class VisualLocationIdentityDualNBackTestController extends AbstractNBackTestController {


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
                locationValue = stimulus.get_values()[0];
                identityValue = stimulus.get_values()[1];
            }else{
                return;
            }

            clearBoxes();

            Node stimulusNode = _stimulusGridPane.getChildren().get(locationValue);

            switch (_stimulusType){

                case DIGIT:
                    ((TextField) stimulusNode).setText(Stimulus.getComputedDigit(identityValue));
                    break;
                case LETTER:
                    ((TextField) stimulusNode).setText(Stimulus.getComputedLetter(identityValue));
                    break;
                case COLOR:
                    String hexColor = Stimulus.getComputedHexColor(identityValue);
                    ((Rectangle) stimulusNode).setFill(Paint.valueOf(hexColor));
                    break;
                case IMAGE:
                    String imagePath = Stimulus.getComputedImagePath(identityValue);
                    Image image = new Image("file:" + imagePath);
                    ((ImageView) stimulusNode).setImage(image);
            }

    }

}
