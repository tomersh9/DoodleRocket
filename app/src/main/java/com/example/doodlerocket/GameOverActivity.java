package com.example.doodlerocket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over_layout);

        //setting score
        TextView scoreTv = findViewById(R.id.score_tv);
        scoreTv.setText("Score " + getIntent().getIntExtra("score",0));

        //return to game
        Button resetBtn = findViewById(R.id.reset_btn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToGameIntent = new Intent(GameOverActivity.this,MainActivity.class);
                startActivity(backToGameIntent);
            }
        });

    }
}
