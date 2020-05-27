package com.example.doodlerocket.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doodlerocket.R;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        SharedPreferences sp = getSharedPreferences("username",MODE_PRIVATE);

        //TextView helloTv = findViewById(R.id.name_output);

        //helloTv.setText("Hello " + sp.getString("username",""));

        Button playBtn = findViewById(R.id.play_btn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startGameIntent = new Intent(HomeActivity.this,MainActivity.class);
                startActivity(startGameIntent);
            }
        });

        Button shopBtn = findViewById(R.id.store_btn);
        shopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,ShopActivity.class);
                startActivity(intent);
            }
        });
    }
}
