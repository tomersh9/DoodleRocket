package com.example.doodlerocket.Activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doodlerocket.R;

public class LevelBlockOne extends AppCompatActivity {

    ObjectAnimator animator1;
    ObjectAnimator animator2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_block_one_layout);

        //lvl 1
        final ImageView lvl1Btn = findViewById(R.id.lvl_1_btn);
        lvl1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelBlockOne.this,MainActivity.class);
                startActivity(intent);
            }
        });

        //lvl 2
        ImageView lvl2Btn = findViewById(R.id.lvl_2_btn);
        lvl2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelBlockOne.this,MainActivity.class);
                startActivity(intent);
            }
        });

        //animation planets
        animator1 = new ObjectAnimator().ofFloat(lvl1Btn,"translationY",-70).setDuration(1400);
        animator1.setRepeatMode(ValueAnimator.REVERSE);
        animator1.setRepeatCount(ValueAnimator.INFINITE);

        animator2 = new ObjectAnimator().ofFloat(lvl2Btn,"translationY",-70).setDuration(1400);
        animator2.setRepeatMode(ValueAnimator.REVERSE);
        animator2.setRepeatCount(ValueAnimator.INFINITE);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator1,animator2);
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

    }

    //on back pressed
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
