package de.stzeyetrial.auretim.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class TestSequence implements Serializable {

    private static final long serialVersionUID = -3944434863811681983L;


    private static final List<TestSequence> loadedSequences = new LinkedList<>();

    private final List<String> _elements;
    private final String _name;


    public TestSequence(List<String> elements, String name){
        _name = name;
        _elements = elements;
    }

    public static void loadAllSequencesFromDisk(){

        try (Stream<Path> paths = Files.walk(Paths.get("testSequences"))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(TestSequence::loadFromDisk);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveAllSetsToDisk(){
       for(TestSequence s : loadedSequences){
           s.saveToDisk();
       }
    }

    public static void loadFromDisk(Path path){
        try {
            FileInputStream fi = new FileInputStream(path.toString());
            ObjectInputStream oi = new ObjectInputStream(fi);
            TestSequence s = (TestSequence) oi.readObject();
            oi.close();
            fi.close();
            loadedSequences.add(s);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void saveToDisk(){
        try {
            File directory = new File("testSequences");
            if (! directory.exists()){
                directory.mkdir();
            }
            FileOutputStream fileOut = new FileOutputStream("testSequences/" + _name + ".testsequence");
            ObjectOutputStream objectOut;
            objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this);
            objectOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TestSequence getSet(String name){
        for(TestSequence s : loadedSequences){
            if (s.get_name().equals(name)){
                return s;
            }
        }
        return null;
    }

    public void deleteSetFromDisk(){

        File testSequenceFile = new File("testSequences/" + this._name + ".testsequence");
        testSequenceFile.delete();
        loadedSequences.remove(this);

    }


    public static List<TestSequence> getLoadedSets() {
        return loadedSequences;
    }

    public String get_name() {
        return _name;
    }

    public List<String> get_elements() {
        return _elements;
    }


}
