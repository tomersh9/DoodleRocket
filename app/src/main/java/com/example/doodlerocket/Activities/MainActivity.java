package com.example.doodlerocket.Activities;

import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.doodlerocket.GameObjects.SoundManager;
import com.example.doodlerocket.GameView;
import com.example.doodlerocket.R;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;
    private Handler handler = new Handler();
    private final static long refreshRate = 10; //refresh rate

    private SharedPreferences sp;

    private SoundManager soundManager;



    //only need 1 instance of AlertDialog and then inflate it with other layouts
    private AlertDialog gameAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        soundManager = new SoundManager(this);

        //getting info to send GameView
        int skinID;
        int backgroundID;
        sp = getSharedPreferences("storage",MODE_PRIVATE);
        skinID = sp.getInt("skin_id", R.drawable.default_ship_100);
        backgroundID = sp.getInt("lvl_bg",R.drawable.stars_pxl_png);

        //game is running on thread behind the scenes
        gameView = new GameView(this,skinID,backgroundID,soundManager);
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
        },0, refreshRate); //delay = 0, each 10mis refresh screen
    }

    @Override //alert dialog when back pressed
    public void onBackPressed() {


        //for each new dialog we create a builder to show this specific dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        //first you inflate the layout and then create the builder to show it
        View pauseView = getLayoutInflater().inflate(R.layout.alert_dialog_view, null);

        //alert animation
        YoYo.with(Techniques.BounceInUp).duration(1000).playOn(pauseView);

        Button yesBtn = pauseView.findViewById(R.id.alert_yes_btn);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button noBtn = pauseView.findViewById(R.id.alert_no_btn);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameAlertDialog.dismiss();
            }
        });

        //building the alert dialog each time with different builder
        gameAlertDialog = builder.setView(pauseView).show();
        gameAlertDialog.setCanceledOnTouchOutside(false);
        gameAlertDialog.setCancelable(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundManager.stopSfx();

    }
}
