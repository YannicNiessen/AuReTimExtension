
package de.stzeyetrial.auretim.input;

import de.stzeyetrial.auretim.tests.SpeechDecoderTest;
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
    private static boolean recording = false;
    public static SpeechDecoder instance;

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

    public void initialize(SpeechDecoder.Language language, SpeechDecoder.MaterialType materialType) throws InterruptedException, IOException, LineUnavailableException {

        Config c = Decoder.defaultConfig();

        String languageString = (language == SpeechDecoder.Language.GERMAN) ? "german" : "english";
        String typeString =  materialType.toString().toLowerCase();


        String acousticModel = "speechRecognition/acousticModels/" + languageString + "/";
        String languageModels = "speechRecognition/languageModels/" + typeString + "/" + languageString + ".arpa";
        String dictionary = "speechRecognition/dictionaries/" + typeString + "/" + languageString + ".dic";



        c.setString("-hmm", acousticModel);
        c.setString("-lm", languageModels);
        c.setString("-dict", dictionary);


        c.setInt("-vad_postspeech", 15);
        c.setFloat("-vad_threshold", 2.0);
       // c.setString("-logfn", "/dev/null");
        c.setFloat("-silprob", 1.0);
        c.setFloat("-wip", 1e-25);

        speechDecoder = new Decoder(c);
        recognizedWordsList = new ArrayList<String>();

        instance = this;

    }

    public synchronized void clearWords(){
        currentWords.clear();
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
        while ((nbytes = audioStream.read(b)) > 0) {

            ByteBuffer bb = ByteBuffer.wrap(b, 0, nbytes);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            short[] s = new short[nbytes/2];
            bb.asShortBuffer().get(s);
            speechDecoder.processRaw(s, nbytes/2, false, false);
            if (!speechDecoder.getInSpeech()){
                String currentWord = getCurrentWord();
                if (currentWord != null){
                    currentWords.add(currentWord);
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
        return currentWords;
    }

    public boolean isRecording() {
        return recording;
    }
}