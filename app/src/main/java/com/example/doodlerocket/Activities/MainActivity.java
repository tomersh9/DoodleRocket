package com.example.doodlerocket.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
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

    private GameView gameView;
    private Handler handler = new Handler();
    private Timer timer; //resume or stop GameView thread
    private final static long refreshRate = 10; //refresh rate

    //current level
    private int currLvl;
    private int skinID;
    private int backgroundID;

    //screen size
    int width, height;

    private SharedPreferences sp;

    private SoundManager soundManager; //SFX
    private MediaPlayer mediaPlayer; //BG MUSIC

    //only need 1 instance of AlertDialog and then inflate it with other layouts
    private AlertDialog gameAlertDialog;

    private List<User> users = new ArrayList<>();

    //backgrounds ID (fix later to work only with lvl)
    private int[] bgID = {R.drawable.moon_bg_800,R.drawable.city_bg,R.drawable.desert_backgorund
                        ,R.drawable.forest_bg_400,R.drawable.ocean_bg_1,R.drawable.ice_bg_800
                        ,R.drawable.lava_bg_1};

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        soundManager = new SoundManager(this);

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
        backgroundID = sp.getInt("lvl_bg", R.drawable.stars_pxl_png);
        currLvl = sp.getInt("curr_lvl", 1);

        //initialize actual game
        setGameView();
        setContentView(gameView);

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

    //calling this method to load game when needed
    private void setGameView() {
        //game is running on thread behind the scenes
        gameView = new GameView(this, width, height, skinID, backgroundID, currLvl, soundManager);

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
                }, 0, refreshRate); //delay = 0, each 10mis refresh screen
            }

            @Override
            public void endGame(int score, boolean isWon) {

                //stop invalidate
                timer.cancel();
                //release sounds
                soundManager.stopSfx();
                //release music
                stopMusic();

                //load user list
                loadUserList();

                //values to move on
                final int currScore = score;
                boolean isTop10 = getIsTop10(score);

                if (isTop10) {

                    //popping alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    View usernameView = getLayoutInflater().inflate(R.layout.username_layout, null);

                    //alert animation
                    YoYo.with(Techniques.SlideInDown).duration(1000).playOn(usernameView);

                    //money
                    TextView coinsTv = usernameView.findViewById(R.id.record_victory_coins_tv);
                    int totalCoins = sp.getInt("money", 0);
                    String totalCoinsString = getString(R.string.total_coins);
                    coinsTv.setText(totalCoinsString + " " + totalCoins);

                    //highScore display
                    TextView highScoreTv = usernameView.findViewById(R.id.record_victory_highscore_tv);
                    final int highScore = sp.getInt("highscore", 0);
                    String highscoreString = getString(R.string.high_score);
                    highScoreTv.setText(highscoreString + " " + highScore);

                    TextView gemsTv = usernameView.findViewById(R.id.record_victory_gems_tv);
                    int gems = sp.getInt("gems", 0);
                    String gemsString = getString(R.string.gems_earned);
                    gemsTv.setText(gemsString + " " + gems);

                    //setting score
                    TextView scoreTv = usernameView.findViewById(R.id.record_victory_score_tv);
                    String scoreString = getString(R.string.score);
                    scoreTv.setText(scoreString + " " + score);

                    //input username
                    final EditText nameEt = usernameView.findViewById(R.id.name_et);

                    //submit to leadBoard
                    final Button boardBtn = usernameView.findViewById(R.id.record_board_btn);
                    final Button submitBtn = usernameView.findViewById(R.id.submit_btn);
                    submitBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(nameEt.getText().toString().equals("")) {
                                Toast.makeText(MainActivity.this, "Enter valid name", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            //save user in List
                            User user = new User(nameEt.getText().toString(), currScore);
                            users.add(user);

                            //disable edit text and enable access to score board
                            nameEt.setVisibility(View.GONE);
                            submitBtn.setVisibility(View.GONE);
                            boardBtn.setVisibility(View.VISIBLE);

                            Toast.makeText(MainActivity.this, nameEt.getText().toString()+" is in Top 10!", Toast.LENGTH_SHORT).show();

                        }
                    });

                    boardBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this,ScoreBoardActivity.class);
                            startActivity(intent);
                            finish();
                            gameAlertDialog.dismiss();
                        }
                    });

                    Button menuBtn = usernameView.findViewById(R.id.record_menu_btn);
                    menuBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                            gameAlertDialog.dismiss();
                        }
                    });

                    Button nextLvlBtn = usernameView.findViewById(R.id.record_next_btn);
                    nextLvlBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //start next level now
                            SharedPreferences.Editor editor = sp.edit();
                            currLvl++; //next lvl
                            editor.putInt("curr_lvl",currLvl); //commit next lvl
                            editor.putInt("lvl_bg",bgID[currLvl-1]); //next background
                            editor.commit();

                            //restart gameView
                            gameAlertDialog.dismiss();
                            setGameView();
                            setContentView(gameView);
                            resumeGame();


                        }
                    });

                    //building the alert dialog each time with different builder
                    gameAlertDialog = builder.setView(usernameView).show();
                    gameAlertDialog.setCanceledOnTouchOutside(false);
                    gameAlertDialog.setCancelable(false);
                }
                //won the level
                else if (isWon && !isTop10) {
                    //victory alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    View victoryView = getLayoutInflater().inflate(R.layout.victory_alert_dialog, null);

                    //alert animation
                    YoYo.with(Techniques.FadeIn).duration(1000).playOn(victoryView);

                    //money
                    TextView coinsTv = victoryView.findViewById(R.id.victory_coins_tv);
                    int totalCoins = sp.getInt("money", 0);
                    String totalCoinsString = getString(R.string.total_coins);
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
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                            gameAlertDialog.dismiss();
                        }
                    });

                    Button nextLvlBtn = victoryView.findViewById(R.id.victory_dialog_next_btn);
                    nextLvlBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, LevelBlockOne.class);
                            startActivity(intent);
                            finish();
                            gameAlertDialog.dismiss();
                        }
                    });

                    //building the alert dialog each time with different builder
                    gameAlertDialog = builder.setView(victoryView).show();
                    gameAlertDialog.setCanceledOnTouchOutside(false);
                    gameAlertDialog.setCancelable(false);

                } else {
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

    private void loadUserList() {

        try {
            FileInputStream fis = openFileInput("users");
            ObjectInputStream ois = new ObjectInputStream(fis);
            users = (List<User>) ois.readObject(); //needs "casting"
            ois.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (users == null) {
                users = new ArrayList<>();
            }
        }
    }

    private boolean getIsTop10(int score) {

        if (users.isEmpty()) { //first place
            return true;
        }
        else if(users.size() < 10) { //have place in list
            return true;
        }

        for(int i = 0 ; i < 10; i++) { //only top 10 can make it to list
            if(score >= users.get(i).getScore()) {
                return true;
            }
        }
        return false;
    }

    @Override //alert dialog when back pressed
    public void onBackPressed() {

       // timer.cancel();

        soundManager.pauseSfx();

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
        try {
            FileOutputStream fos = openFileOutput("users",MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos); //can handle Objects!!
            //always write the Root Object
            oos.writeObject(users); //writing object directly (needs to Serialize Person)
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
