package com.example.doodlerocket.Activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.doodlerocket.R;
import com.r0adkll.slidr.Slidr;

import java.util.Timer;
import java.util.TimerTask;

public class LevelBlockOne extends AppCompatActivity {

    SharedPreferences sp;
    private int backgroundID;
    private int currLvl;
    private int globalLvl;

    ObjectAnimator animator1;
    ObjectAnimator animator2;

    /*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LOW_PROFILE);
        }
    }*/

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_block_one_layout);

        //fixed portrait mode
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sp = getSharedPreferences("storage",MODE_PRIVATE);
        backgroundID = sp.getInt("lvl_bg",R.drawable.stars_pxl_png); //default bg
        globalLvl = sp.getInt("global_lvl",1);

        TextView lvl1Tv = findViewById(R.id.lvl_1_tv);
        TextView lvl2Tv = findViewById(R.id.lvl_2_tv);

        //lvl 1
        final ImageView lvl1Btn = findViewById(R.id.lvl_1_btn);
        lvl1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //set level background
                backgroundID = R.drawable.moon_bg_800;
                currLvl = 1;

                lvl1Btn.animate().scaleX(0.5f).scaleY(0.5f).alpha(0.3f).setDuration(500).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        //time entry to lvl
                        lvl1Btn.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(500).setStartDelay(250);

                        Intent intent = new Intent(LevelBlockOne.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).start();


            }
        });

        //lvl 2
        final ImageView lvl2Btn = findViewById(R.id.lvl_2_btn);
        lvl2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //need to unlock level
                if(2 > globalLvl) {
                    Toast.makeText(LevelBlockOne.this, "You need to unlock it first", Toast.LENGTH_SHORT).show();
                    return;
                }

                //set level background
                backgroundID = R.drawable.city_bg;
                currLvl = 2;

                //time entry to lvl
                Intent intent = new Intent(LevelBlockOne.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //FIX ON HEBREW!
        //animation planets - YoYo
        YoYo.with(Techniques.ZoomInLeft).duration(1000).playOn(lvl1Btn);
        YoYo.with(Techniques.ZoomInRight).duration(1000).playOn(lvl2Btn);

        //bounce infinite text
        ObjectAnimator bounceTextAnimator1 = new ObjectAnimator().ofFloat(lvl1Tv,"translationY",-70).setDuration(1400);
        ObjectAnimator bounceTextAnimator2 = new ObjectAnimator().ofFloat(lvl2Tv,"translationY",-70).setDuration(1400);

        bounceTextAnimator1.setRepeatMode(ValueAnimator.REVERSE);
        bounceTextAnimator1.setRepeatCount(ValueAnimator.INFINITE);
        bounceTextAnimator2.setRepeatMode(ValueAnimator.REVERSE);
        bounceTextAnimator2.setRepeatCount(ValueAnimator.INFINITE);

        //bounce infinite img
        animator1 = new ObjectAnimator().ofFloat(lvl1Btn,"translationY",-70).setDuration(1400);
        animator1.setRepeatMode(ValueAnimator.REVERSE);
        animator1.setRepeatCount(ValueAnimator.INFINITE);

        animator2 = new ObjectAnimator().ofFloat(lvl2Btn,"translationY",-70).setDuration(1400);
        animator2.setRepeatMode(ValueAnimator.REVERSE);
        animator2.setRepeatCount(ValueAnimator.INFINITE);

        //play animations together
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator1,animator2);
        set.playTogether(bounceTextAnimator1,bounceTextAnimator2);
        set.start();

        //next page
        ImageButton nextBlockBtn = findViewById(R.id.next_btn_block_1);
        nextBlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelBlockOne.this,LevelBlockTwo.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        ImageButton prevBlockBtn = findViewById(R.id.prev_btn_block_1);
        prevBlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelBlockOne.this,HomeActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        //change layout according to levels unlocked
        enableLevels();
    }

    private void enableLevels() {
        //make locked levels grey or something
    }


    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("lvl_bg",backgroundID);
        editor.putInt("curr_lvl",currLvl);
        editor.commit();
    }

    //on back pressed
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LevelBlockOne.this,HomeActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
