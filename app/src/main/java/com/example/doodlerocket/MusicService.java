package com.example.doodlerocket;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MusicService extends Service {

    MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startMusic();
        Toast.makeText(this, "Create music", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startMusic();
        return START_STICKY;
    }

    public void startMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.theme_comp);
            mediaPlayer.setVolume(0.75f,0.75f);
            mediaPlayer.setLooping(true);
        }
        mediaPlayer.start(); //start or resume
    }

    public void resumeMusic() {
        if(mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public void pauseMusic() {
        if(mediaPlayer!=null) {
            mediaPlayer.pause();
        }
    }

    public void stopMusic() {
        if(mediaPlayer!=null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopMusic();
    }
}
