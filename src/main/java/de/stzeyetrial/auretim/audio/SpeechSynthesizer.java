package de.stzeyetrial.auretim.audio;


import de.stzeyetrial.auretim.config.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class SpeechSynthesizer {

    static String voiceName;

    public static void setup(){

        Config config = Config.getInstance();

        boolean female = config.voiceFemaleProperty().getValue();
        boolean german = config.voiceGermanProperty().getValue();

        if (female && german){
            voiceName = "mb-de1";
        }else if (female && !german){
            voiceName = "mb-us1";
        }else if (!female && german){
            voiceName = "mb-de6";
        }else {
            voiceName = "mb-us2";
        }

    }



    public static void speak(String speakText){
        System.out.println(System.getProperty("user.dir"));
        System.out.println(System.getProperty("PATH"));
        String command = "/usr/bin/espeak-ng -s 150 -v " + voiceName + " \"" + speakText +  "\" --stdout | aplay -D 'sysdefault'\n";
        System.out.println(command);
        try {
            Runtime.getRuntime().exec(new String[] { "/bin/sh"//$NON-NLS-1$
                    , "-c", command });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) throws Exception {

    }
}
