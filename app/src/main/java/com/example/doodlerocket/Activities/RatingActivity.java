package com.example.doodlerocket.Activities;

import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.doodlerocket.GameObjects.Star;
import com.example.doodlerocket.R;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class RatingActivity extends AppCompatActivity {

    private List<Star> stars = new ArrayList<>();
    private List<ImageButton> starsImgList = new ArrayList<>();

    private int ratingNum;

    private SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_layout);

        //save rating state
        sp = getSharedPreferences("storage",MODE_PRIVATE);
        ratingNum = sp.getInt("rating",0);

        //animations
        TextView rateTv = findViewById(R.id.rate_tv);
        ObjectAnimator titleAnimator = new ObjectAnimator().ofFloat(rateTv,"translationY",-1500,0).setDuration(1000);
        titleAnimator.start();

        final ImageButton star1 = findViewById(R.id.star_1);
        final ImageButton star2 = findViewById(R.id.star_2);
        final ImageButton star3 = findViewById(R.id.star_3);
        final ImageButton star4 = findViewById(R.id.star_4);
        final ImageButton star5 = findViewById(R.id.star_5);

        //save star list
        starsImgList.add(star1);
        starsImgList.add(star2);
        starsImgList.add(star3);
        starsImgList.add(star4);
        starsImgList.add(star5);

        for(int i = 0 ; i< ratingNum ; i++) {
            starsImgList.get(i).setImageResource(R.drawable.gold_star_96);
        }


        //**********Rating Click Listeners*************//
        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.gold_star_96);
                star2.setImageResource(R.drawable.black_star_96);
                star3.setImageResource(R.drawable.black_star_96);
                star4.setImageResource(R.drawable.black_star_96);
                star5.setImageResource(R.drawable.black_star_96);

                ratingNum = 1;
            }
        });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.gold_star_96);
                star2.setImageResource(R.drawable.gold_star_96);
                star3.setImageResource(R.drawable.black_star_96);
                star4.setImageResource(R.drawable.black_star_96);
                star5.setImageResource(R.drawable.black_star_96);

                ratingNum = 2;
            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.gold_star_96);
                star2.setImageResource(R.drawable.gold_star_96);
                star3.setImageResource(R.drawable.gold_star_96);
                star4.setImageResource(R.drawable.black_star_96);
                star5.setImageResource(R.drawable.black_star_96);

                ratingNum = 3;
            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.gold_star_96);
                star2.setImageResource(R.drawable.gold_star_96);
                star3.setImageResource(R.drawable.gold_star_96);
                star4.setImageResource(R.drawable.gold_star_96);
                star5.setImageResource(R.drawable.black_star_96);

                ratingNum = 4;
            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.gold_star_96);
                star2.setImageResource(R.drawable.gold_star_96);
                star3.setImageResource(R.drawable.gold_star_96);
                star4.setImageResource(R.drawable.gold_star_96);
                star5.setImageResource(R.drawable.gold_star_96);

                ratingNum = 5;
            }
        });

        Button backBtn = findViewById(R.id.back_rate_btn);
        ObjectAnimator btnAnimator = new ObjectAnimator().ofFloat(backBtn,"translationY",3500,0).setDuration(1000);
        btnAnimator.start();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("rating",ratingNum);
        editor.commit();
    }
}
