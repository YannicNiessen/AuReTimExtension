
package de.stzeyetrial.auretim.input;

import de.stzeyetrial.auretim.tests.SpeechDecoderTest;
import de.stzeyetrial.auretim.util.Stimulus;
import edu.cmu.pocketsphinx.Config;
import edu.cmu.pocketsphinx.Decoder;
import edu.cmu.pocketsphinx.*;
import javafx.beans.property.ReadOnlyListWrapper;

import javax.sound.sampled.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpeechDecoder {
    static {
        System.loadLibrary("pocketsphinx_jni");
    }

    private static TargetDataLine line;
    private static Decoder speechDecoder;
    private static List<String> recognizedWordsList = new ArrayList<>();
    public volatile List<String> currentWords;
    private static volatile boolean recording = false;
    public static SpeechDecoder instance;
    private static Language _currentLanguage;
    private static Stimulus.Type _currentType;

    public enum Language {
        GERMAN,
        ENGLISH
    }

    public enum MaterialType{
        LETTERS,
        DIGITS,
        COLORS
    }


    public static SpeechDecoder getInstance()
    {

        if (instance == null){
            instance = new SpeechDecoder();
        }

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

    public void initialize(SpeechDecoder.Language language, Stimulus.Type materialType) throws InterruptedException, IOException, LineUnavailableException {

        if (speechDecoder != null){
            recording = false;
        }

        Config c = Decoder.defaultConfig();

        String languageString = (language == SpeechDecoder.Language.GERMAN) ? "german" : "english";
        String typeString =  materialType.toString().toLowerCase() + "s";

        _currentLanguage= language;
        _currentType = materialType;

        System.out.println(typeString);

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
        //c.setFloat("-samprate", 48000);
        speechDecoder = new Decoder(c);
        recognizedWordsList = new ArrayList<String>();

        instance = this;

    }

    public synchronized void clearWords(){
        if (currentWords != null){
            currentWords.clear();
        }
    }


    public void startRecording() throws LineUnavailableException, IOException, InterruptedException {
        recording = true;
        AudioFormat format = getAudioFormat();

        line = (TargetDataLine) AudioSystem.getTargetDataLine(format);
        line.open(getAudioFormat());
        line.start();
        speechDecoder.startUtt();
        currentWords = new ArrayList<>();

        byte[] b = new byte[line.getBufferSize() / 5];

        AudioInputStream audioStream = new AudioInputStream(line);

        System.out.println("Recording started");
        int nbytes;
        while ((nbytes = audioStream.read(b)) > 0 && recording) {

            ByteBuffer bb = ByteBuffer.wrap(b, 0, nbytes);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            short[] s = new short[nbytes/2];
            bb.asShortBuffer().get(s);
            speechDecoder.processRaw(s, nbytes/2, false, false);
            if (!speechDecoder.getInSpeech()){
                String currentWord = getCurrentWord();
                if (currentWord != null){
                if (_currentType == Stimulus.Type.DIGIT){
                    if (_currentLanguage == Language.GERMAN){
                        currentWord = String.valueOf(germanWordToInt(currentWord));
                    }else{
                        currentWord = String.valueOf(englishWordToInt(currentWord));
                    }
                }
                    currentWords.add(currentWord);
                }

            }
        }
        System.out.println("line ended");
        speechDecoder.endUtt();
        speechDecoder.delete();
        line.close();

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
            recording = false;
    }

    public synchronized List<String> getRecognizedWordsList(){
        return currentWords;
    }

    public boolean isRecording() {
        return recording;
    }

    private int germanWordToInt(String word) {
        switch (word) {
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
    private int englishWordToInt(String word) {
        switch (word) {
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


}