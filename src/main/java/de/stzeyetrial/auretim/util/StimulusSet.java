package de.stzeyetrial.auretim.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class StimulusSet implements Serializable {


    private static final long serialVersionUID = -3944434863811190883L;
    private static final List<StimulusSet> loadedSets = new LinkedList<>();

    private Stimulus.Type _type;
    private final List<String> _elements;
    private final String _name;

    private static String[] digitSet = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private static String[] letterSet = {"b", "f", "h", "i", "j", "l", "m", "o", "r", "s"};
    private static String[] colorSet = {"#000000", "#FF0000", "#00FF00", "#0000FF", "#FFFFFF", "#00FFFF", "#FFFF00", "#FF00FF", "#964B00", "#FFC0CB"};


    public StimulusSet(Stimulus.Type type, List<String> elements, String name){
        _name = name;
        _type = type;
        _elements = elements;
    }

    public static void loadAllSetsFromDisk(){




        StimulusSet defaultDIGIT = new StimulusSet(Stimulus.Type.DIGIT, Arrays.asList(digitSet), "DIGIT");
        StimulusSet defaultLETTER = new StimulusSet(Stimulus.Type.LETTER, Arrays.asList(letterSet), "LETTER");
        StimulusSet defaultCOLOR = new StimulusSet(Stimulus.Type.COLOR, Arrays.asList(colorSet), "COLOR");

        loadedSets.add(defaultDIGIT);
        loadedSets.add(defaultLETTER);
        loadedSets.add(defaultCOLOR);

        try (Stream<Path> paths = Files.walk(Paths.get("stimulusSets"))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(StimulusSet::loadFromDisk);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveAllSetsToDisk(){
       for(StimulusSet s : loadedSets){
           s.saveToDisk();
       }
    }

    public static void loadFromDisk(Path path){
        try {
            FileInputStream fi = new FileInputStream(path.toString());
            ObjectInputStream oi = new ObjectInputStream(fi);
            StimulusSet s = (StimulusSet) oi.readObject();
            oi.close();
            fi.close();
            if (s.get_name().equals("DIGIT") || s.get_name().equals("LETTER") || s.get_name().equals("COLOR")){
                return;
            }
            loadedSets.add(s);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void saveToDisk(){
        try {
            File directory = new File("stimulusSets");
            if (! directory.exists()){
                directory.mkdir();
            }

            FileOutputStream fileOut = new FileOutputStream("stimulusSets/" + _name + ".stimulus.set");
            ObjectOutputStream objectOut;
            objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this);
            objectOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static StimulusSet getSet(String name){
        for(StimulusSet s : loadedSets){
            if (s.get_name().equals(name)){
                return s;
            }
        }
        return null;
    }

    public void deleteSetFromDisk(){


        if (this.get_name().equals("DIGIT") || this.get_name().equals("LETTER") || this.get_name().equals("COLOR")){
            return;
        }
        File setFile = new File("stimulusSets/" + this._name + ".stimulus.set");
        setFile.delete();
        loadedSets.remove(this);

    }


    public static List<StimulusSet> getLoadedSets() {
        return loadedSets;
    }

    public String get_name() {
        return _name;
    }

    public List<String> get_elements() {
        return _elements;
    }

    public Stimulus.Type get_type() {
        return _type;
    }

    public void set_type(Stimulus.Type type){
        _type = type;
    }

}
