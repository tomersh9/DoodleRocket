package com.example.doodlerocket.Activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.doodlerocket.GameObjects.Bullet;
import com.example.doodlerocket.GameObjects.SoundManager;
import com.example.doodlerocket.R;

public class HomeActivity extends AppCompatActivity {

    AlertDialog gameAlertDialog;

    SharedPreferences sp;
    private int skinID;

    private ImageView shipIcon;
    private ImageButton playBtn;

    MediaPlayer mediaPlayer;
    private boolean isMute = false;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //fixed portrait mode
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sp = getSharedPreferences("storage",MODE_PRIVATE);
        skinID = sp.getInt("skin_id",R.drawable.default_ship_100);

        //start background music
        //startMusic();

        //title animation
        TextView titleTv = findViewById(R.id.home_title_tv);
        YoYo.with(Techniques.FadeInDown).duration(1500).playOn(titleTv);

        //showing current ship equipped
        shipIcon = findViewById(R.id.home_ship_icon);
        shipIcon.setImageResource(skinID);

        playBtn = findViewById(R.id.play_btn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startGameIntent = new Intent(HomeActivity.this,LevelBlockOne.class);
                startActivity(startGameIntent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        ImageButton shopBtn = findViewById(R.id.store_btn);
        shopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,ShopActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        ImageButton leadboardBtn = findViewById(R.id.leadboard_btn);
        leadboardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,ScoreBoardActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        //set buttons animations
        setAnimations();

       /* //mute music btn
        final ImageButton volumeBtn = findViewById(R.id.vol_btn);
        volumeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isMute) {
                    volumeBtn.setImageResource(R.drawable.mute_yellow_50);
                    pauseMusic();
                    isMute = true;
                }
                else {
                    volumeBtn.setImageResource(R.drawable.vol_yellow_50);
                    startMusic();
                    isMute = false;
                }
            }
        });*/

    }

    private void setAnimations() {

        //layout btn drops down
        LinearLayout linearLayout = findViewById(R.id.home_btn_layout);
        ObjectAnimator layoutAnimator = new ObjectAnimator().ofFloat(linearLayout,"translationY",-2000,0).setDuration(1500);

        //play btn drops down
        ObjectAnimator playAnimator = new ObjectAnimator().ofFloat(playBtn,"translationY",-2000,0).setDuration(1500);

        //set
        AnimatorSet btnsSet = new AnimatorSet();
        btnsSet.playTogether(layoutAnimator,playAnimator);
        btnsSet.start();

        //animate ship
        ObjectAnimator shipFlyAnimator = new ObjectAnimator().ofFloat(shipIcon,"translationY",3500,0).setDuration(1600);
        ObjectAnimator shipBounceAnimator = new ObjectAnimator().ofFloat(shipIcon,"translationY",-70).setDuration(1400);

        AnimatorSet shipSet = new AnimatorSet();
        shipSet.play(shipFlyAnimator).before(shipBounceAnimator);
        shipSet.start();

        //shipBounceAnimator.start();
        shipBounceAnimator.setRepeatMode(ValueAnimator.REVERSE);
        shipBounceAnimator.setRepeatCount(ValueAnimator.INFINITE);
    }

    /*public void startMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.theme_compressed);
            mediaPlayer.setVolume(0.75f,0.75f);
            mediaPlayer.setLooping(true);
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

    @Override
    protected void onResume() {
        super.onResume();
        isMute = false;
        startMusic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusic();
    }*/

    //put skin on image when back
    @Override
    protected void onResume() {
        super.onResume();
        skinID = sp.getInt("skin_id",R.drawable.default_ship_100);
        shipIcon.setImageResource(skinID);
    }

    @Override //alert dialog when back pressed
    public void onBackPressed() {


        //for each new dialog we create a builder to show this specific dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

        //first you inflate the layout and then create the builder to show it
        View pauseView = getLayoutInflater().inflate(R.layout.alert_dialog_view, null);

        //alert animation
        YoYo.with(Techniques.Landing).duration(1000).playOn(pauseView);

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
}
