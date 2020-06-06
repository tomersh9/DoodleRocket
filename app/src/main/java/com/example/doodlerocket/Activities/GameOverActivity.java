package com.example.doodlerocket.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.doodlerocket.GameObjects.SoundManager;
import com.example.doodlerocket.R;

import org.w3c.dom.Text;

public class GameOverActivity extends AppCompatActivity {

    SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over_layout);

        //fixed portrait mode
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sp = getSharedPreferences("storage",MODE_PRIVATE);

        TextView gameOverTv = findViewById(R.id.gameover_tv);
        YoYo.with(Techniques.Flash).duration(2000).playOn(gameOverTv);

        //money
        TextView coinsTv = findViewById(R.id.coins_tv);
        int totalCoins = sp.getInt("money",0);
        String totalCoinsString = getString(R.string.total_coins);
        coinsTv.setText(totalCoinsString + " " + totalCoins );

        //highScore display
        TextView highScoreTv = findViewById(R.id.highscore_tv);
        final int highScore = sp.getInt("highscore",0);
        String highscoreString = getString(R.string.high_score);
        highScoreTv.setText(highscoreString + " " + highScore );

        //setting score
        TextView scoreTv = findViewById(R.id.score_tv);
        int score = getIntent().getIntExtra("score",0);
        String scoreString = getString(R.string.score);
        scoreTv.setText(scoreString + " " + score );

        //return to game
        Button resetBtn = findViewById(R.id.reset_btn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToGameIntent = new Intent(GameOverActivity.this,MainActivity.class);
                startActivity(backToGameIntent);
                finish();
            }
        });

        Button menuBtn = findViewById(R.id.menu_btn);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menuIntent = new Intent(GameOverActivity.this,HomeActivity.class);
                startActivity(menuIntent);
                finish();
            }
        });

        Button scoreBtn = findViewById(R.id.score_board_btn);
        scoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scoreIntent = new Intent(GameOverActivity.this,ScoreBoardActivity.class);
                scoreIntent.putExtra("high_score",highScore);
                startActivity(scoreIntent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GameOverActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
