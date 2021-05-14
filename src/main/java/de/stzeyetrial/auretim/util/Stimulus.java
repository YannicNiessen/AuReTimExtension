package de.stzeyetrial.auretim.util;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.Arrays;

public class Stimulus {

    private static String[] digitSet = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private static String[] letterSet = {"b", "f", "h", "i", "j", "l", "m", "o", "r", "s"};
    private static String[] hexColorSet = {"#000000", "#FF0000", "#00FF00", "#0000FF", "#FFFFFF", "#000000", "#FF0000", "#00FF00", "#0000FF", "#FFFFFF"};
    private static String[] imagePathSet = {"/home/pi/BachelorArbeit/AuReTim/images/image01.jpeg", "/home/pi/BachelorArbeit/AuReTim/images/image02.jpeg", "/home/pi/BachelorArbeit/AuReTim/images/image03.jpeg","/home/pi/BachelorArbeit/AuReTim/images/image04.jpeg", "/home/pi/BachelorArbeit/AuReTim/images/image05.jpeg", "/home/pi/BachelorArbeit/AuReTim/images/image01.jpeg", "/home/pi/BachelorArbeit/AuReTim/images/image02.jpeg", "/home/pi/BachelorArbeit/AuReTim/images/image03.jpeg","/home/pi/BachelorArbeit/AuReTim/images/image04.jpeg", "/home/pi/BachelorArbeit/AuReTim/images/image05.jpeg"};

    private int _dimensions;

    private Integer[] _values;


    public Stimulus(int dimensions, Integer[] values){
        this._dimensions = dimensions;
        this._values = values;
    }


    public Integer[] get_values() {
        return _values;
    }

    public int get_dimensions() {
        return _dimensions;
    }

    public boolean isUnreal(){
        return Arrays.asList(_values).contains(-1);
    }

    public boolean isGo(){return Arrays.asList(_values).contains(1);}

    public boolean isNoGo(){return Arrays.asList(_values).contains(2);}

    public static Node getContainerNode(Type type, double prefWidth, double prefHeight){

        switch(type){
            case COLOR :
                return new Rectangle(prefWidth, prefHeight);
            case IMAGE:
                return new ImageView();
            default:
                TextField textField= new TextField();
                textField.setPrefHeight(prefHeight);
                textField.setPrefWidth(prefWidth);
                textField.setEditable(false);
                textField.setFont(new Font((int) (prefHeight / 3)));
                textField.setAlignment(Pos.CENTER);
                return textField;
        }
    }

    public static String getComputedDigit(int value){
        if (value >= digitSet.length){
            throw new ArrayIndexOutOfBoundsException();
        }
        return digitSet[value];
    }

    public static String getComputedLetter(int value){
        if (value >= letterSet.length){
            throw new ArrayIndexOutOfBoundsException();
        }
        return letterSet[value];
    }

    public static String getComputedHexColor(int value){
        if (value >= hexColorSet.length){
            throw new ArrayIndexOutOfBoundsException();
        }
        return hexColorSet[value];
    }

    public static String getComputedImagePath(int value){
        if (value >= imagePathSet.length){
            throw new ArrayIndexOutOfBoundsException();
        }
        return imagePathSet[value];
    }

    public enum Type{
        DIGIT,
        LETTER,
        COLOR,
        IMAGE
    }

    public static Stimulus unrealStimulus(){
        return new Stimulus(1, new Integer[] {-1});
    }
    public static Stimulus goStimulus(){return new Stimulus(1, new Integer[] {1});}
    public static Stimulus noGoStimulus(){return new Stimulus(1, new Integer[] {2}); }



    @Override
    public String toString() {
        return "Stimulus{" +
                "_dimensions=" + _dimensions +
                ", _values=" + Arrays.toString(_values) +
                '}';
    }
}
