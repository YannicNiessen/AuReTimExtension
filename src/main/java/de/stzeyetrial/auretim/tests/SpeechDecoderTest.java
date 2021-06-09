
package de.stzeyetrial.auretim.tests;

import de.stzeyetrial.auretim.input.SpeechDecoder;
import edu.cmu.pocketsphinx.Config;
import edu.cmu.pocketsphinx.Decoder;
import edu.cmu.pocketsphinx.Hypothesis;

import javax.sound.sampled.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SpeechDecoderTest {
    static {
        System.loadLibrary("pocketsphinx_jni");
    }

    private static TargetDataLine line;
    private static Decoder speechDecoder;
    private static List<String> recognizedWordsList = new ArrayList<>();
    public static List<String> synList = Collections.synchronizedList(recognizedWordsList);
    private static boolean recording = false;
    public static SpeechDecoderTest instance;
    public static String[] letterSet = {"b", "f", "h", "i", "j", "l", "m", "o", "r", "s"};
    public static String[] colorSetGerman = {"rot", "grün", "blau", "weiß", "schwarz", "orange", "gelb", "braun", "rosa", "violett"};
    public static String[] colorSetEnglish = {"red", "green", "blue", "white", "black", "orange", "yellow", "brown", "pink", "purple"};


    public static SpeechDecoderTest getInstance() {





        return instance;
    }

    static AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 16;
        int channels = 1;
        int frameSize = 2;
        boolean bigEndian = false;
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate, sampleSizeInBits,
                channels, frameSize , sampleRate, bigEndian);
        return format;
    }

    public void initialize(Language language, MaterialType materialType) throws InterruptedException, IOException, LineUnavailableException {

        Config c = Decoder.defaultConfig();

        String languageString = (language == Language.GERMAN) ? "german" : "english";
        String typeString =  materialType.toString().toLowerCase();


        String acousticModel = "speechRecognition/acousticModels/" + languageString + "/";
        String languageModels = "speechRecognition/languageModels/" + typeString + "/" + languageString + ".arpa";
        String dictionary = "speechRecognition/dictionaries/" + typeString + "/" + languageString + ".dic";



        c.setString("-hmm", acousticModel);
        c.setString("-lm", languageModels);
        c.setString("-dict", dictionary);


        c.setInt("-vad_postspeech", 15);
        c.setFloat("-vad_threshold", 2.0);
        c.setString("-logfn", "/dev/null");
        c.setFloat("-silprob", 1.0);
        c.setFloat("-wip", 1e-25);

        speechDecoder = new Decoder(c);
        recognizedWordsList = new ArrayList<String>();

        instance = this;

    }

    public synchronized void clearList(){
        synList.clear();
    }


    public void startRecording() throws LineUnavailableException, IOException, InterruptedException {
        recording = true;
        AudioFormat format = getAudioFormat();

        line = (TargetDataLine) AudioSystem.getTargetDataLine(format);
        line.open(getAudioFormat());
        line.start();
        speechDecoder.startUtt();
        recognizedWordsList = new ArrayList<>();
        synList = Collections.synchronizedList(recognizedWordsList);

        byte[] b = new byte[line.getBufferSize() / 5];

        AudioInputStream audioStream = new AudioInputStream(line);

        System.out.println("Recording started");
        int nbytes;
        while ((nbytes = audioStream.read(b)) > 0) {

            ByteBuffer bb = ByteBuffer.wrap(b, 0, nbytes);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            short[] s = new short[nbytes/2];
            bb.asShortBuffer().get(s);
            speechDecoder.processRaw(s, nbytes/2, false, false);
            if (!speechDecoder.getInSpeech()){
                String currentWord = getCurrentWord();
                if (currentWord != null){
                    synchronized (synList){
                        synList.add(currentWord);
                     //  System.out.println(currentWord);
                    }
                }

            }
        }
        System.out.println("line ended");
    }




    public String getCurrentWord() throws InterruptedException {

        try{
            Hypothesis hypothesis= speechDecoder.hyp();
            String currentWord = hypothesis.getHypstr();

            if (currentWord != null){

                speechDecoder.endUtt();
                speechDecoder.startUtt();
                return currentWord;

            }

        }catch(Exception ex){
              // ex.printStackTrace();
        }
        return null;

    }

    public void stopRecording() {
            line.close();
    }

    public synchronized List<String> getRecognizedWordsList(){
        return synList;
    }

    public boolean isRecording() {
        return recording;
    }

    public static void main(String[] args) {

        SpeechDecoderTest s = new SpeechDecoderTest();

        System.out.println("Choose language: type 0 for german or 1 for english");
        String language = System.console().readLine();
        clearConsole();
        System.out.println("Choose Material: type 0 for digits, 1 for letters or 2 for colors");
        String material = System.console().readLine();
        clearConsole();

        System.out.println("Please enter your name:");
        String subjectName = System.console().readLine();

        clearConsole();


        Language l = (language.equals("0")) ? Language.GERMAN : Language.ENGLISH;
        MaterialType m = null;

        switch(material){
            case "0":
                m = MaterialType.DIGITS;
                break;
            case "1":
                m = MaterialType.LETTERS;
                break;
            case "2":
                m = MaterialType.COLORS;
                break;
            default:
                System.out.println("Not a valid option. Exiting...");
                System.exit(0);
        }

        try {
            s.initialize(l, m);

            MaterialType finalM = m;
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                        if (finalM == MaterialType.DIGITS){
                            SpeechDecoderTest.digitTest(l, subjectName);
                        }else if (finalM == MaterialType.LETTERS){
                            SpeechDecoderTest.letterTest(l, subjectName);
                        }else{
                            SpeechDecoderTest.colorTest(l, subjectName);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            Thread t = new Thread(r);
            t.start();
            s.startRecording();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

    }


    public static void digitTest(Language language, String subjectName) throws InterruptedException {

        int _wordsSizePreAdd = 0;

        int REPETITIONS_PER_DIGIT = 20;

        String[] results = new String[10 * REPETITIONS_PER_DIGIT];

        Thread.sleep(2000);

        List<Integer> usedDigits = new ArrayList<>();

        if (language == Language.GERMAN)
            System.out.println("Bitte wiederhole die gezeigte Ziffer");
        else
            System.out.println("Please repeat the shown digit");

        for (int i = 0; i < REPETITIONS_PER_DIGIT; i++){

/*
            for (int j = 0; j <results.length; j++) {
                if (results[j] != null){
                    System.out.println(String.valueOf((j / REPETITIONS_PER_DIGIT )) + " : " + results[j]);
                }
            }
*/
            System.console().readLine();
            clearConsole();
            System.out.println("Proceeding in 1");
            Thread.sleep(1000);
            clearConsole();

            _wordsSizePreAdd = SpeechDecoderTest.synList.size();
            for (int j = 0; j < 10; j++){

                int currentNumber = (int) (Math.random() * 10);

                while(usedDigits.contains(currentNumber)){
                    currentNumber = (int) (Math.random() * 10);
                }
                usedDigits.add(currentNumber);
                if (language == Language.GERMAN)
                    System.out.println(intToGermanWord(currentNumber));
                else
                    System.out.println(intToEnglishWord(currentNumber));
                Thread.sleep(1500);

                List<String> words = new ArrayList<>();
                while(words.isEmpty()) {
                    synchronized (SpeechDecoderTest.synList) {
                        for (int k = _wordsSizePreAdd; k < SpeechDecoderTest.synList.size(); k++) {
                            words.add(SpeechDecoderTest.synList.get(k));
                        }
                    }
                    if(words.isEmpty()){
                        Thread.sleep(50);
                    }else{
                        _wordsSizePreAdd = SpeechDecoderTest.synList.size();
                    }
                }
              // System.out.println("Recognized: " + words.toString());
                results[currentNumber * REPETITIONS_PER_DIGIT + i] = String.join( " ", words);

              //  Thread.sleep(250);
                clearConsole();


            }
            usedDigits.clear();
        }
        String csvString = "Expected, Response \n";
        for (int i = 0; i < results.length; i++) {
            System.out.println((i / REPETITIONS_PER_DIGIT) + " : " + results[i]);
            String rowString = String.valueOf(i / REPETITIONS_PER_DIGIT) + ",";
            String[] words = results[i].split(" ");

            for (int j = 0; j < words.length; j++){
                if (language == Language.GERMAN)
                    rowString += String.valueOf(germanWordToInt(words[j])) + " ";
                else
                    rowString += String.valueOf(englishWordToInt(words[j])) + " ";
            }
            rowString += "\n";
            csvString += rowString;

        }
        PrintWriter writer = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy--HH:mm:ss");
            String dateString  = dateFormat.format(new Date());
            writer = new PrintWriter("digits_" + language + "_" + subjectName + "_" + "_" + dateString +".csv", "UTF-8");
            writer.println(csvString);
            writer.close();
            writer = new PrintWriter("digits_" + language + "_" + subjectName + "_" + "_" + dateString +"_BACKUP.csv", "UTF-8");
            writer.println(csvString);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void letterTest(Language language, String subjectName) throws InterruptedException {

        int _wordsSizePreAdd = 0;

        int REPETITIONS_PER_LETTER = 20;

        String[] results = new String[10 * REPETITIONS_PER_LETTER];

        Thread.sleep(2000);
        List<Integer> usedDigits = new ArrayList<>();



        if (language == Language.GERMAN)
            System.out.println("Bitte wiederhole den gezeigten Buchstaben");
        else
            System.out.println("Please repeat the shown letter");

        for (int i = 0; i < REPETITIONS_PER_LETTER; i++){



            if (language == Language.GERMAN)
                System.out.println("Drücke Enter um mit dem nächsten Block fortzufahren. \n Fortschritt: " + i + "/" + REPETITIONS_PER_LETTER);
            else
                System.out.println("Press enter to proceed to next block. \n Progress: " + i + "/" + REPETITIONS_PER_LETTER);

            System.console().readLine();
            clearConsole();
            System.out.println("Proceeding in 1");
            Thread.sleep(1000);
            clearConsole();

            _wordsSizePreAdd = SpeechDecoderTest.synList.size();
            for (int j = 0; j < 10; j++){

                int currentNumber = (int) (Math.random() * 10);

                while(usedDigits.contains(currentNumber)){
                    currentNumber = (int) (Math.random() * 10);
                }
                usedDigits.add(currentNumber);
                System.out.println(letterSet[currentNumber]);

                Thread.sleep(1500);

                List<String> words = new ArrayList<>();
                while(words.isEmpty()){
                synchronized (SpeechDecoderTest.synList){
                    for (int k = _wordsSizePreAdd; k < SpeechDecoderTest.synList.size(); k++) {
                        words.add(SpeechDecoderTest.synList.get(k));
                    }
                }
                if(words.isEmpty()){
                    Thread.sleep(50);
                }else{
                    _wordsSizePreAdd = SpeechDecoderTest.synList.size();
                }
            }

                results[currentNumber * REPETITIONS_PER_LETTER + i] = String.join( " ", words);
                clearConsole();
            }
            usedDigits.clear();

        }
        String csvString = "Expected, Response \n";
        for (int i = 0; i < results.length; i++) {
            System.out.println(letterSet[(i / REPETITIONS_PER_LETTER)] + " : " + results[i]);
            String rowString = letterSet[i / REPETITIONS_PER_LETTER] + ",";
            String[] words = results[i].split(" ");

            for (int j = 0; j < words.length; j++){
                    rowString += words[j] + " ";
            }
            rowString += "\n";
            csvString += rowString;

        }
        PrintWriter writer = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy--HH:mm:ss");
            String dateString  = dateFormat.format(new Date());
            writer = new PrintWriter("letters_" + language + "_" + subjectName + "_" + "_" + dateString +".csv", "UTF-8");
            writer.println(csvString);
            writer.close();
            writer = new PrintWriter("letters_" + language + "_" + subjectName + "_" + "_" + dateString +"_BACKUP.csv", "UTF-8");
            writer.println(csvString);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void colorTest(Language language, String subjectName) throws InterruptedException {

        int _wordsSizePreAdd = 0;

        int REPETITIONS_PER_COLOR = 20;

        String[] results = new String[10 * REPETITIONS_PER_COLOR];

        Thread.sleep(2000);
        List<Integer> usedDigits = new ArrayList<>();



        if (language == Language.GERMAN)
            System.out.println("Bitte wiederhole die gezeigte Farbe");
        else
            System.out.println("Please repeat the shown color");

        for (int i = 0; i < REPETITIONS_PER_COLOR; i++){
            if (language == Language.GERMAN)
                System.out.println("Drücke Enter um mit dem nächsten Block fortzufahren. \n Fortschritt: " + i + "/" + REPETITIONS_PER_COLOR);
            else
                System.out.println("Press enter to proceed to next block. \n Progress: " + i + "/" + REPETITIONS_PER_COLOR);


            System.console().readLine();
            clearConsole();
            System.out.println("Proceeding in 1");
            Thread.sleep(1000);
            clearConsole();

            _wordsSizePreAdd = SpeechDecoderTest.synList.size();
            for (int j = 0; j < 10; j++){
                int currentNumber = (int) (Math.random() * 10);

                while(usedDigits.contains(currentNumber)){
                    currentNumber = (int) (Math.random() * 10);
                }
                usedDigits.add(currentNumber);
                if (language == Language.GERMAN){
                    System.out.println(colorSetGerman[currentNumber]);
                }else{
                    System.out.println(colorSetEnglish[currentNumber]);
                }

                Thread.sleep(1500);


                List<String> words = new ArrayList<>();
                while(words.isEmpty()){
                    synchronized (SpeechDecoderTest.synList){
                        for (int k = _wordsSizePreAdd; k < SpeechDecoderTest.synList.size(); k++) {
                            words.add(SpeechDecoderTest.synList.get(k));
                        }
                    }
                    if(words.isEmpty()){
                        Thread.sleep(50);
                    }else{
                        _wordsSizePreAdd = SpeechDecoderTest.synList.size();
                    }
                }

                results[currentNumber * REPETITIONS_PER_COLOR + i] = String.join( " ", words);
                clearConsole();
            }
            usedDigits.clear();
        }
        String csvString = "Expected, Response \n";
        for (int i = 0; i < results.length; i++) {

            if (language == Language.GERMAN){
                System.out.println(colorSetGerman[(i / REPETITIONS_PER_COLOR)] + " : " + results[i]);
            }else{
                System.out.println(colorSetEnglish[(i / REPETITIONS_PER_COLOR)] + " : " + results[i]);
            }
            String rowString = "";
            if (language == Language.GERMAN){
                rowString = colorSetGerman[i / REPETITIONS_PER_COLOR] + ",";
            }else{
                rowString = colorSetEnglish[i / REPETITIONS_PER_COLOR] + ",";
            }

            String[] words = results[i].split(" ");

            for (int j = 0; j < words.length; j++){
                rowString += words[j] + " ";
            }
            rowString += "\n";
            csvString += rowString;

        }
        PrintWriter writer = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy--HH:mm:ss");
            String dateString  = dateFormat.format(new Date());
            writer = new PrintWriter("colors_" + language + "_" + subjectName + "_" + "_" + dateString +".csv", "UTF-8");
            writer.println(csvString);
            writer.close();
            writer = new PrintWriter("colors_" + language + "_" + subjectName + "_" + "_" + dateString +"_BACKUP.csv", "UTF-8");
            writer.println(csvString);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    private enum Language {
        GERMAN,
        ENGLISH
    }

    private enum MaterialType{
        LETTERS,
        DIGITS,
        COLORS
    }
    private static void clearConsole(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static int germanWordToInt(String word){
        switch(word) {
            case "null":
                return 0;
            case "eins":
                return 1;
            case "zwei":
                return 2;
            case "drei":
                return 3;
            case "vier":
                return 4;
            case "fünf":
                return 5;
            case "sechs":
                return 6;
            case "sieben":
                return 7;
            case "acht":
                return 8;
            case "neun":
                return 9;
        }
        return -1;
    }
    private static int englishWordToInt(String word){
        switch(word) {
            case "zero":
                return 0;
            case "one":
                return 1;
            case "two":
                return 2;
            case "three":
                return 3;
            case "four":
                return 4;
            case "five":
                return 5;
            case "six":
                return 6;
            case "seven":
                return 7;
            case "eight":
                return 8;
            case "nine":
                return 9;
        }
        return -1;
    }


    private static String intToGermanWord(int number){
        switch(number){
            case 0:
                return "null";
            case 1:
                return "eins";
            case 2:
                return "zwei";
            case 3:
                return "drei";
            case 4:
                return "vier";
            case 5:
                return "fünf";
            case 6:
                return "sechs";
            case 7:
                return "sieben";
            case 8:
                return "acht";
            case 9:
                return "neun";
            default:
                return "-";
        }
    }

    private static String intToEnglishWord(int number){
        switch(number){
            case 0:
                return "zero";
            case 1:
                return "one";
            case 2:
                return "two";
            case 3:
                return "three";
            case 4:
                return "four";
            case 5:
                return "five";
            case 6:
                return "six";
            case 7:
                return "seven";
            case 8:
                return "eight";
            case 9:
                return "nine";
            default:
                return "-";
        }
    }


}
