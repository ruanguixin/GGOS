package com.ggos.util;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;

public class MusicUtil {
    private static AudioClip start;
    private static AudioClip bomb;
    private static AudioClip win;

    static {

        try {
            start = Applet.newAudioClip(new File("music/start.wav").toURL());
            bomb = Applet.newAudioClip(new File("music/bomb.wav").toURL());
            win = Applet.newAudioClip(new File("music/win.wav").toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public static void playStart() {
        start.play();
    }

    public static void playBomb() {
        bomb.play();
    }

    public static void playWin() {
        win.play();
    }

}
