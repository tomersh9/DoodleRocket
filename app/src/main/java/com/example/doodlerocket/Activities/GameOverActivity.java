package com.example.doodlerocket.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
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

        sp = getSharedPreferences("storage",MODE_PRIVATE);

        TextView gameOverTv = findViewById(R.id.gameover_tv);
        YoYo.with(Techniques.Flash).duration(2000).playOn(gameOverTv);

        //money
        TextView coinsTv = findViewById(R.id.coins_tv);
        coinsTv.setText("Coins "+sp.getInt("money",0));

        //highScore display
        TextView highScoreTv = findViewById(R.id.highscore_tv);
        final int highScore = sp.getInt("highscore",0);
        highScoreTv.setText("High Score " + highScore );

        //setting score
        TextView scoreTv = findViewById(R.id.score_tv);
        scoreTv.setText("Score " + getIntent().getIntExtra("score",0));

        //return to game
        Button resetBtn = findViewById(R.id.reset_btn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToGameIntent = new Intent(GameOverActivity.this,LevelBlockOne.class);
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
