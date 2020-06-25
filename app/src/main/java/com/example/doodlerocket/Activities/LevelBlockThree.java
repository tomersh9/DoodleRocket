package com.example.doodlerocket.Activities;

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

public class LevelBlockThree extends AppCompatActivity {

    SharedPreferences sp;
    private int backgroundID;
    private int currLvl;
    private int globalLvl;

    private TextView lvl5Tv;
    private TextView lvl6Tv;
    private ImageView lvl5Btn;
    private ImageView lvl6Btn;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_block_three_layout);

        //fixed portrait mode
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sp = getSharedPreferences("storage",MODE_PRIVATE);
        backgroundID = sp.getInt("lvl_bg",R.drawable.stars_pxl_png);
        globalLvl = sp.getInt("global_lvl",1);


        lvl5Tv = findViewById(R.id.lvl_5_tv);
        lvl6Tv = findViewById(R.id.lvl_6_tv);
        lvl5Btn = findViewById(R.id.lvl_5_btn);
        lvl6Btn = findViewById(R.id.lvl_6_btn);

        enableLevels();

        lvl5Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //need to unlock level
                if(5 > globalLvl) {
                    Toast.makeText(LevelBlockThree.this, R.string.unlock_first, Toast.LENGTH_SHORT).show();
                    return;
                }

                //lvl background
                backgroundID = R.drawable.ocean_bg_1;
                currLvl = 5;

                lvl5Btn.animate().scaleX(0.5f).scaleY(0.5f).alpha(0.3f).setDuration(500).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        //time entry to lvl
                        lvl5Btn.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(500).setStartDelay(250);

                        Intent intent = new Intent(LevelBlockThree.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).start();

            }
        });

        lvl6Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //need to unlock level
                if(6 > globalLvl) {
                    Toast.makeText(LevelBlockThree.this, R.string.unlock_first, Toast.LENGTH_SHORT).show();
                    return;
                }

                //put level background
                backgroundID = R.drawable.ice_bg_800;
                currLvl = 6;

                lvl6Btn.animate().scaleX(0.5f).scaleY(0.5f).alpha(0.3f).setDuration(500).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        //time entry to lvl
                        lvl6Btn.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(500).setStartDelay(250);

                        Intent intent = new Intent(LevelBlockThree.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).start();
            }
        });

        //animation planets - YoYo
        YoYo.with(Techniques.ZoomInLeft).duration(1000).playOn(lvl5Btn);
        YoYo.with(Techniques.ZoomInLeft).duration(1000).playOn(lvl6Btn);

        //bounce infinite text
        ObjectAnimator bounceTextAnimator1 = new ObjectAnimator().ofFloat(lvl5Tv,"translationY",-70).setDuration(1400);
        ObjectAnimator bounceTextAnimator2 = new ObjectAnimator().ofFloat(lvl6Tv,"translationY",-70).setDuration(1400);

        bounceTextAnimator1.setRepeatMode(ValueAnimator.REVERSE);
        bounceTextAnimator1.setRepeatCount(ValueAnimator.INFINITE);
        bounceTextAnimator2.setRepeatMode(ValueAnimator.REVERSE);
        bounceTextAnimator2.setRepeatCount(ValueAnimator.INFINITE);

        //animation planets
        ObjectAnimator animator1 = new ObjectAnimator().ofFloat(lvl5Btn,"translationY",-70).setDuration(1400);
        animator1.setRepeatMode(ValueAnimator.REVERSE);
        animator1.setRepeatCount(ValueAnimator.INFINITE);

        ObjectAnimator animator2 = new ObjectAnimator().ofFloat(lvl6Btn,"translationY",-70).setDuration(1400);
        animator2.setRepeatMode(ValueAnimator.REVERSE);
        animator2.setRepeatCount(ValueAnimator.INFINITE);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator1,animator2);
        set.playTogether(bounceTextAnimator1,bounceTextAnimator2);
        set.start();


        //next page
        ImageButton nextBlockBtn = findViewById(R.id.next_btn_block_3);
        nextBlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelBlockThree.this,LevelBlockFour.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        //previous page
        ImageButton prevBlockBtn = findViewById(R.id.prev_btn_block_3);
        prevBlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelBlockThree.this,LevelBlockTwo.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
    }

    private void enableLevels() {
        if(globalLvl >= 5) {
            lvl5Tv.setTextColor(Color.WHITE);
            lvl5Tv.setText(R.string.ocean);
            lvl5Btn.setImageResource(R.drawable.ocean_300);
        }
        if(globalLvl >= 6) {
            lvl6Tv.setTextColor(Color.WHITE);
            lvl6Tv.setText(R.string.ice);
            lvl6Btn.setImageResource(R.drawable.ice_300);
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
