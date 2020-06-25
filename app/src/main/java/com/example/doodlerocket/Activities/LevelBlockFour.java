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

public class LevelBlockFour extends AppCompatActivity {

    SharedPreferences sp;
    private int backgroundID;
    private int currLvl;
    private int globalLvl;

    private TextView lvl7Tv;
    private ImageView lvl7Btn;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_level_block);

        //fixed portrait mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sp = getSharedPreferences("storage", MODE_PRIVATE);
        backgroundID = sp.getInt("lvl_bg", R.drawable.stars_pxl_png);
        globalLvl = sp.getInt("global_lvl", 1);


        lvl7Tv = findViewById(R.id.lvl_7_tv);
        lvl7Btn = findViewById(R.id.lvl_7_btn);

        //change layout according to levels unlocked
        enableLevels();

        lvl7Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //need to unlock level
                if (7 > globalLvl) {
                    Toast.makeText(LevelBlockFour.this, R.string.unlock_first, Toast.LENGTH_SHORT).show();
                    return;
                }

                //lvl background
                backgroundID = R.drawable.lava_bg_1;
                currLvl = 7;

                lvl7Btn.animate().scaleX(0.5f).scaleY(0.5f).alpha(0.3f).setDuration(500).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        //time entry to lvl
                        lvl7Btn.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(500).setStartDelay(250);

                        Intent intent = new Intent(LevelBlockFour.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).start();

            }
        });


        //animation planets - YoYo
        YoYo.with(Techniques.ZoomInLeft).duration(1000).playOn(lvl7Btn);

        //bounce infinite text
        ObjectAnimator bounceTextAnimator1 = new ObjectAnimator().ofFloat(lvl7Tv, "translationY", -70).setDuration(1400);

        bounceTextAnimator1.setRepeatMode(ValueAnimator.REVERSE);
        bounceTextAnimator1.setRepeatCount(ValueAnimator.INFINITE);

        //animation planets
        ObjectAnimator animator1 = new ObjectAnimator().ofFloat(lvl7Btn, "translationY", -70).setDuration(1400);
        animator1.setRepeatMode(ValueAnimator.REVERSE);
        animator1.setRepeatCount(ValueAnimator.INFINITE);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator1);
        set.playTogether(bounceTextAnimator1);
        set.start();

        //previous page
        ImageButton prevBlockBtn = findViewById(R.id.prev_btn_block_4);
        prevBlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelBlockFour.this, LevelBlockThree.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void enableLevels() {
        if (globalLvl == 7) {
            lvl7Tv.setText(R.string.hell);
            lvl7Btn.setImageResource(R.drawable.lava_300);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //put level background
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("lvl_bg", backgroundID);
        editor.putInt("curr_lvl", currLvl);
        editor.commit();
    }

    //on back pressed
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
