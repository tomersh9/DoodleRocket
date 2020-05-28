package com.example.doodlerocket.Activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doodlerocket.R;

public class LevelBlockTwo extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_block_two_layout);

        //lvl 3
        ImageView lvl3Btn = findViewById(R.id.lvl_3_btn);
        lvl3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelBlockTwo.this,MainActivity.class);
                startActivity(intent);
            }
        });

        //lvl 4
        ImageView lvl4Btn = findViewById(R.id.lvl_4_btn);
        lvl4Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelBlockTwo.this,MainActivity.class);
                startActivity(intent);
            }
        });

        //animation planets
        ObjectAnimator animator1 = new ObjectAnimator().ofFloat(lvl3Btn,"translationY",-70).setDuration(1400);
        animator1.setRepeatMode(ValueAnimator.REVERSE);
        animator1.setRepeatCount(ValueAnimator.INFINITE);

        ObjectAnimator animator2 = new ObjectAnimator().ofFloat(lvl4Btn,"translationY",-70).setDuration(1400);
        animator2.setRepeatMode(ValueAnimator.REVERSE);
        animator2.setRepeatCount(ValueAnimator.INFINITE);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator1,animator2);
        set.start();


        //next page
        ImageButton nextBlockBtn = findViewById(R.id.next_btn_block_2);
        nextBlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelBlockTwo.this,MainActivity.class);
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

    //on back pressed
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
