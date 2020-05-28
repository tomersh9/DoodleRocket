package com.example.doodlerocket.Activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doodlerocket.R;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        //screen pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        final ImageView splashSpaceship = findViewById(R.id.splash_spaceship);
        final TextView titleTv = findViewById(R.id.splash_title);

        //2 animators combined and operated by animations set
        ObjectAnimator shipAnimator = new ObjectAnimator().ofFloat(splashSpaceship,"translationY",-height).setDuration(2500);
        ObjectAnimator textAnimator = new ObjectAnimator().ofFloat(titleTv,"alpha",0f,1f).setDuration(1500);
        textAnimator.setStartDelay(1400);
        AnimatorSet set = new AnimatorSet();

        //activate animations
        set.playTogether(shipAnimator,textAnimator);
        set.start();

        //move to main menu
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreen.this,HomeActivity.class);
                startActivity(mainIntent);
                finish();
            }
        },SPLASH_TIME_OUT);

    }
}
