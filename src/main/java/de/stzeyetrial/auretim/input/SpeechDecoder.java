
package de.stzeyetrial.auretim.input;

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
    public static List<String> synList = Collections.synchronizedList(recognizedWordsList);
    private static boolean recording = false;
    public static SpeechDecoder instance;


    public static SpeechDecoder getInstance() {
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

    public void initialize() throws InterruptedException, IOException, LineUnavailableException {

        Config c = Decoder.defaultConfig();
        c.setString("-hmm", "/home/pi/Downloads/cmusphinx-ptm-voxforge-de-r20171217/model_parameters/voxforge.cd_ptm_5000/");
        //c.setString("-lm", "/home/pi/Downloads/srilm-voxforge-de-r20171217.arpa");
        c.setString("-lm", "/home/pi/Documents/myLM.arpa");
        c.setString("-dict", "/home/pi/Downloads/myDic.dic");
        c.setInt("-vad_postspeech", 15);
        c.setFloat("-vad_threshold", 2.0);
        c.setString("-logfn", "/dev/null");
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
}