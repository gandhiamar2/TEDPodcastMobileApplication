package com.example.gandh.hw7;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;

import java.io.IOException;

/**
 * Created by gandh on 3/10/2017.
 */

public class Player implements Runnable {

    String url;
    MediaPlayer mediaPlayer;
    public Player(String url, MediaPlayer mediaPlayer) {
        this.url = url;
        this.mediaPlayer = mediaPlayer;
    }

    @Override
    public void run() {
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (IllegalStateException e) {
            e.printStackTrace();
        }



    }
}
