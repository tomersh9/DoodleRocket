package com.example.doodlerocket.Activities;

import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.Timer;
import java.util.TimerTask;

public class LevelBlockTwo extends AppCompatActivity {

    SharedPreferences sp;
    private int backgroundID;
    private int currLvl;
    private int globalLvl;

    private TextView lvl3Tv;
    private TextView lvl4Tv;
    private ImageView lvl3Btn;
    private ImageView lvl4Btn;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_block_two_layout);

        //fixed portrait mode
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sp = getSharedPreferences("storage",MODE_PRIVATE);
        backgroundID = sp.getInt("lvl_bg",R.drawable.stars_pxl_png);
        globalLvl = sp.getInt("global_lvl",1);

        lvl3Tv = findViewById(R.id.lvl_3_tv);
        lvl4Tv = findViewById(R.id.lvl_4_tv);
        lvl3Btn = findViewById(R.id.lvl_3_btn);
        lvl4Btn = findViewById(R.id.lvl_4_btn);

        //change layout according to levels unlocked
        enableLevels();

        lvl3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //need to unlock level
                if(3 > globalLvl) {
                    Toast.makeText(LevelBlockTwo.this, R.string.unlock_first, Toast.LENGTH_SHORT).show();
                    return;
                }

                //lvl background
                backgroundID = R.drawable.desert_backgorund;
                currLvl = 3;

                lvl3Btn.animate().scaleX(0.5f).scaleY(0.5f).alpha(0.3f).setDuration(500).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        //time entry to lvl
                        lvl3Btn.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(500).setStartDelay(250);

                        Intent intent = new Intent(LevelBlockTwo.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).start();

                //time entry to lvl
                /*Intent intent = new Intent(LevelBlockTwo.this,MainActivity.class);
                startActivity(intent);*/

            }
        });

        lvl4Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //need to unlock level
                if(4 > globalLvl) {
                    Toast.makeText(LevelBlockTwo.this, R.string.unlock_first, Toast.LENGTH_SHORT).show();
                    return;
                }

                //put level background
                backgroundID = R.drawable.forest_bg_400;
                currLvl = 4;

                //time entry to lvl
                lvl4Btn.animate().scaleX(0.5f).scaleY(0.5f).alpha(0.3f).setDuration(500).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        //time entry to lvl
                        lvl4Btn.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(500).setStartDelay(250);

                        Intent intent = new Intent(LevelBlockTwo.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).start();
            }
        });

        //animation planets - YoYo
        YoYo.with(Techniques.ZoomInLeft).duration(1000).playOn(lvl3Btn);
        YoYo.with(Techniques.ZoomInLeft).duration(1000).playOn(lvl4Btn);

        //bounce infinite text
        ObjectAnimator bounceTextAnimator1 = new ObjectAnimator().ofFloat(lvl3Tv,"translationY",-70).setDuration(1400);
        ObjectAnimator bounceTextAnimator2 = new ObjectAnimator().ofFloat(lvl4Tv,"translationY",-70).setDuration(1400);

        bounceTextAnimator1.setRepeatMode(ValueAnimator.REVERSE);
        bounceTextAnimator1.setRepeatCount(ValueAnimator.INFINITE);
        bounceTextAnimator2.setRepeatMode(ValueAnimator.REVERSE);
        bounceTextAnimator2.setRepeatCount(ValueAnimator.INFINITE);

        //animation planets
        ObjectAnimator animator1 = new ObjectAnimator().ofFloat(lvl3Btn,"translationY",-70).setDuration(1400);
        animator1.setRepeatMode(ValueAnimator.REVERSE);
        animator1.setRepeatCount(ValueAnimator.INFINITE);

        ObjectAnimator animator2 = new ObjectAnimator().ofFloat(lvl4Btn,"translationY",-70).setDuration(1400);
        animator2.setRepeatMode(ValueAnimator.REVERSE);
        animator2.setRepeatCount(ValueAnimator.INFINITE);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator1,animator2);
        set.playTogether(bounceTextAnimator1,bounceTextAnimator2);
        set.start();


        //next page
        ImageButton nextBlockBtn = findViewById(R.id.next_btn_block_2);
        nextBlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelBlockTwo.this,LevelBlockThree.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        //previous page
        ImageButton prevBlockBtn = findViewById(R.id.prev_btn_block_2);
        prevBlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelBlockTwo.this,LevelBlockOne.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
    }

    private void enableLevels() {
        if(globalLvl >= 3) {
            lvl3Tv.setTextColor(Color.WHITE);
            lvl3Tv.setText(R.string.desert);
            lvl3Btn.setImageResource(R.drawable.desert_300);
        }
        if(globalLvl >= 4) {
            lvl4Tv.setTextColor(Color.WHITE);
            lvl4Tv.setText(R.string.forest);
            lvl4Btn.setImageResource(R.drawable.forest_300);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //put level background
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
}
