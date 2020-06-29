package com.example.doodlerocket.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.doodlerocket.GameObjects.Boss;
import com.example.doodlerocket.GameObjects.SoundManager;
import com.example.doodlerocket.GameObjects.User;
import com.example.doodlerocket.GameView;
import com.example.doodlerocket.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    //handling actual game display
    private GameView gameView;
    private Handler handler = new Handler();
    private Timer timer; //resume or stop GameView thread
    private final static long refreshRate = 10; //refresh rate
    private boolean isFirstLoad = true;

    //current level
    private int globalLvl;
    private int currLvl;
    private int skinID;
    private int backgroundID;
    private int currScore;

    //screen size
    int width, height;

    private SharedPreferences sp;

    private SoundManager soundManager; //SFX
    private MediaPlayer mediaPlayer; //BG MUSIC

    //only need 1 instance of AlertDialog and then inflate it with other layouts
    private AlertDialog gameAlertDialog;

    //hide navigation bar in game
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //fixed portrait mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //screen pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        //getting info to send GameView
        sp = getSharedPreferences("storage", MODE_PRIVATE);
        skinID = sp.getInt("skin_id", R.drawable.default_ship_100);
        currLvl = sp.getInt("curr_lvl", 1);
        globalLvl = sp.getInt("global_lvl", 1);
        currScore = 0;

        //set GameView background according to level
        setBackground(currLvl);

        //initialize actual game
        setGameView();
        setContentView(gameView); //show canvas

        //refresh canvas
        timer = new Timer();
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
        }, 0, refreshRate); //delay = 0, each 10mis refresh screen
    }

    private void setBackground(int currLvl) {

        switch (currLvl) {
            case 1:
                backgroundID = R.drawable.moon_bg_800;
                break;
            case 2:
                backgroundID = R.drawable.city_bg;
                break;
            case 3:
                backgroundID = R.drawable.desert_backgorund;
                break;
            case 4:
                backgroundID = R.drawable.forest_bg_400;
                break;
            case 5:
                backgroundID = R.drawable.beach_gif_bg;
                break;
            case 6:
                backgroundID = R.drawable.ocean_bg_1;
                break;
            case 7:
                backgroundID = R.drawable.lava_bg_1;
                break;
        }
    }

    //calling this method to load game when needed
    private void setGameView() {

        //create SFX
        soundManager = new SoundManager(MainActivity.this);

        //reset load count
        isFirstLoad = true;

        //setting current lvl
        currLvl = sp.getInt("curr_lvl", 1);

        //game is running on a back thread
        gameView = new GameView(this, width, height, skinID, backgroundID, currLvl, currScore,soundManager);

        //listens to events in GameView
        gameView.setGameViewListener(new GameView.GameViewListener() {

            @Override
            public void playBossMusic() {
                startMusic();
            }

            @Override
            public void pauseGame() {

                //stop GameView thread
                timer.cancel();

                //pause sfx
                soundManager.pauseSfx();

                //pause music
                pauseMusic();

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
                        //kill intent without saving score
                        gameAlertDialog.dismiss();
                        stopMusic();
                        finish();
                    }
                });
                Button noBtn = pauseView.findViewById(R.id.alert_no_btn);
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gameAlertDialog.dismiss();
                        resumeGame();
                    }
                });

                //building the alert dialog each time with different builder
                gameAlertDialog = builder.setView(pauseView).show();
                gameAlertDialog.setCanceledOnTouchOutside(false);
                gameAlertDialog.setCancelable(false);
            }

            @Override
            public void resumeGame() {

                soundManager.resumeSfx();
                //need to create new Timer after it has been stopped
                timer = new Timer();
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
                }, 0, refreshRate); //delay = 0, each 10mis refresh screen
            }

            @Override
            public void endGame(final int score, boolean isWon) {

                //only call this event once!
                if (!isFirstLoad) {
                    return;
                } else {
                    isFirstLoad = false;
                }

                //stop invalidate
                timer.cancel();

                //release sounds
                soundManager.stopSfx();

                //release music
                stopMusic();

                //won the level
                if (isWon) {

                    //unlock next level
                    unlockNextLvl();

                    //victory alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    View victoryView = getLayoutInflater().inflate(R.layout.victory_alert_dialog, null);

                    //alert animation
                    YoYo.with(Techniques.FadeIn).duration(1000).playOn(victoryView);

                    //money
                    TextView coinsTv = victoryView.findViewById(R.id.victory_coins_tv);
                    int totalCoins = sp.getInt("money", 0);
                    String totalCoinsString = getString(R.string.total_gems);
                    coinsTv.setText(totalCoinsString + " " + totalCoins);

                    //highScore display
                    TextView highScoreTv = victoryView.findViewById(R.id.victory_highscore_tv);
                    final int highScore = sp.getInt("highscore", 0);
                    String highscoreString = getString(R.string.high_score);
                    highScoreTv.setText(highscoreString + " " + highScore);

                    TextView gemsTv = victoryView.findViewById(R.id.victory_gems_tv);
                    int gems = sp.getInt("gems", 0);
                    String gemsString = getString(R.string.gems_earned);
                    gemsTv.setText(gemsString + " " + gems);

                    //setting score
                    TextView scoreTv = victoryView.findViewById(R.id.victory_score_tv);
                    String scoreString = getString(R.string.score);
                    scoreTv.setText(scoreString + " " + score);

                    Button menuBtn = victoryView.findViewById(R.id.victory_dialog_menu_btn);
                    menuBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            gameAlertDialog.dismiss();
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                    Button nextLvlBtn = victoryView.findViewById(R.id.victory_dialog_next_btn);
                    nextLvlBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //restart gameView
                            currScore = score;
                            gameAlertDialog.dismiss();
                            setBackground(currLvl);
                            setGameView();
                            setContentView(gameView);
                            resumeGame();
                        }
                    });

                    //building the alert dialog each time with different builder
                    gameAlertDialog = builder.setView(victoryView).show();
                    gameAlertDialog.setCanceledOnTouchOutside(false);
                    gameAlertDialog.setCancelable(false);

                } else {

                    currScore = 0;

                    stopMusic();

                    //move to game over page
                    Intent gameOverIntent = new Intent(MainActivity.this, GameOverActivity.class);
                    gameOverIntent.putExtra("score", score);
                    startActivity(gameOverIntent);

                    //kill intent
                    finish();
                }
            }
        });
    }

    @Override //alert dialog when back pressed
    public void onBackPressed() {
        gameView.gameViewListener.pauseGame();
    }

    public void startMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.boss_music_cut);
            mediaPlayer.setVolume(1, 1);
            mediaPlayer.setLooping(false);
        }
        mediaPlayer.start();
    }

    public void pauseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    //calls only if player won the level
    private void unlockNextLvl() {

        //don't unlock new levels if playing previous levels
        if (globalLvl > currLvl) {
            currLvl++;
        } else {
            //max levels
            globalLvl++;
            if (globalLvl > 7) {
                globalLvl = 7;
            }
            currLvl = globalLvl;
        }

        //saving new global level (unlock if needed)
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("global_lvl", globalLvl);
        editor.putInt("curr_lvl", currLvl);
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //gameAlertDialog.dismiss();
        //same reason
    }
}
