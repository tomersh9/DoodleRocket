package com.example.doodlerocket.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doodlerocket.GameView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;
    private Handler handler = new Handler();
    private final static long interval = 10; //refresh rate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //game is running on thread behind the scenes
        gameView = new GameView(this);
        setContentView(gameView); //content display

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        gameView.invalidate(); //refresh screen (repaint canvas)
                    }
                });
            }
        },0, interval); //delay = 0, each 10mis refresh screen
    }

}
