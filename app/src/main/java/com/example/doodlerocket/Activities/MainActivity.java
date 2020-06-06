package com.example.doodlerocket.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.doodlerocket.GameObjects.SoundManager;
import com.example.doodlerocket.GameView;
import com.example.doodlerocket.R;

import java.sql.BatchUpdateException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;
    private Handler handler = new Handler();
    private Timer timer; //resume or stop GameView thread
    private final static long refreshRate = 10; //refresh rate

    //screen size
    int width, height;

    private SharedPreferences sp;

    private SoundManager soundManager; //SFX
    private MediaPlayer mediaPlayer; //BG MUSIC

    //only need 1 instance of AlertDialog and then inflate it with other layouts
    private AlertDialog gameAlertDialog;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        soundManager = new SoundManager(this);

        //fixed portrait mode
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //screen pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        //getting info to send GameView
        int skinID;
        int backgroundID;
        final int currLvl;
        sp = getSharedPreferences("storage",MODE_PRIVATE);
        skinID = sp.getInt("skin_id", R.drawable.default_ship_100);
        backgroundID = sp.getInt("lvl_bg",R.drawable.stars_pxl_png);
        currLvl = sp.getInt("curr_lvl",1);


        //game is running on thread behind the scenes
        gameView = new GameView(this,width,height,skinID,backgroundID,currLvl,soundManager);
        setContentView(gameView); //content display

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
        },0, refreshRate); //delay = 0, each 10mis refresh screen

        //listens to events in GameView
        gameView.setGameViewListener(new GameView.GameViewListener() {

            @Override
            public void playBossMusic() {
                startMusic();
                System.out.println("starting boss music!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
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
                        stopMusic();
                        finish();
                        gameAlertDialog.dismiss();
                    }
                });
                Button noBtn = pauseView.findViewById(R.id.alert_no_btn);
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resumeGame();
                        gameAlertDialog.dismiss();
                    }
                });

                //building the alert dialog each time with different builder
                gameAlertDialog = builder.setView(pauseView).show();
                gameAlertDialog.setCanceledOnTouchOutside(false);
                gameAlertDialog.setCancelable(false);

            }

            @Override
            public void resumeGame() {

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
                },0, refreshRate); //delay = 0, each 10mis refresh screen
            }

            @Override
            public void endGame(int score,boolean isWon) {

                //stop invalidate
                timer.cancel();
                //release sounds
                soundManager.stopSfx();
                //release music
                stopMusic();

                //won the level
                if(isWon) {
                    //victory alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    View victoryView = getLayoutInflater().inflate(R.layout.victory_alert_dialog,null);

                    //alert animation
                    YoYo.with(Techniques.FadeIn).duration(1000).playOn(victoryView);

                    //money
                    TextView coinsTv = victoryView.findViewById(R.id.victory_coins_tv);
                    int totalCoins = sp.getInt("money",0);
                    String totalCoinsString = getString(R.string.total_coins);
                    coinsTv.setText(totalCoinsString + " " + totalCoins );

                    //highScore display
                    TextView highScoreTv = victoryView.findViewById(R.id.victory_highscore_tv);
                    final int highScore = sp.getInt("highscore",0);
                    String highscoreString = getString(R.string.high_score);
                    highScoreTv.setText(highscoreString + " " + highScore );

                    TextView gemsTv = victoryView.findViewById(R.id.victory_gems_tv);
                    int gems = sp.getInt("gems",0);
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
                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                            startActivity(intent);
                            finish();
                            gameAlertDialog.dismiss();
                        }
                    });

                    Button nextLvlBtn = victoryView.findViewById(R.id.victory_dialog_next_btn);
                    nextLvlBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this,LevelBlockOne.class);
                            startActivity(intent);
                            finish();
                            gameAlertDialog.dismiss();
                        }
                    });

                    //building the alert dialog each time with different builder
                    gameAlertDialog = builder.setView(victoryView).show();
                    gameAlertDialog.setCanceledOnTouchOutside(false);
                    gameAlertDialog.setCancelable(false);
                }
                else {
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
                gameAlertDialog.dismiss();
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

    public void startMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.theme_compressed);
            mediaPlayer.setVolume(1,1);
            mediaPlayer.setLooping(false);
        }
        mediaPlayer.start();
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

}
