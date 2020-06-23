package com.example.doodlerocket.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.doodlerocket.GameObjects.SoundManager;
import com.example.doodlerocket.GameObjects.User;
import com.example.doodlerocket.R;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class GameOverActivity extends AppCompatActivity {

    SharedPreferences sp;

    //vars to check if player is in Top 10
    private List<User> users = new ArrayList<>();
    private int currScore;
    private boolean isTop10;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over_layout);

        //fixed portrait mode
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sp = getSharedPreferences("storage",MODE_PRIVATE);

        //load user list
        loadUserList();

        //check if player entered the top 10
        currScore = getIntent().getIntExtra("score",0);
        isTop10 = getIsTop10(currScore);

        TextView gameOverTv = findViewById(R.id.gameover_tv);
        YoYo.with(Techniques.Flash).duration(2000).playOn(gameOverTv);

        //total game currency
        TextView totalGemsTv = findViewById(R.id.total_gems_tv);
        int totalGems = sp.getInt("money",0);
        String totalGemsString = getString(R.string.total_gems);
        totalGemsTv.setText(totalGemsString + " " + totalGems );

        //gems earned in match
        TextView gemsTv = findViewById(R.id.gems_earned_tv);
        int gems = sp.getInt("gems",0);
        String gemsString = getString(R.string.gems);
        gemsTv.setText(gemsString + " " + gems);

        //highScore display
        TextView highScoreTv = findViewById(R.id.highscore_tv);
        final int highScore = sp.getInt("highscore",0);
        String highscoreString = getString(R.string.high_score);
        highScoreTv.setText(highscoreString + " " + highScore );

        //setting score
        TextView scoreTv = findViewById(R.id.score_tv);
        String scoreString = getString(R.string.score);
        scoreTv.setText(scoreString + " " + currScore);

        //ref to default layout
        final LinearLayout btnLayout = findViewById(R.id.game_over_btn_layout);

        //ref to submit name layout
        final LinearLayout submitLayout = findViewById(R.id.submit_layout);
        final EditText nameEt = findViewById(R.id.name_et);
        final Button submitBtn = findViewById(R.id.submit_btn);

        if(!isTop10) {
            submitLayout.setVisibility(View.GONE);
            btnLayout.setVisibility(View.VISIBLE);
        }

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nameEt.getText().toString().equals("")) {
                    Toast.makeText(GameOverActivity.this, R.string.valid_name, Toast.LENGTH_SHORT).show();
                    return;
                }

                //save user in List
                User user = new User(nameEt.getText().toString(), currScore);
                users.add(user);

                //disable edit text and enable access to buttons layout
                submitLayout.setVisibility(View.GONE);
                btnLayout.setVisibility(View.VISIBLE);

                Toast.makeText(GameOverActivity.this, nameEt.getText().toString() + getString(R.string.is_in_top_10), Toast.LENGTH_SHORT).show();

            }
        });

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

    private void loadUserList() {

        try {
            FileInputStream fis = openFileInput("users");
            ObjectInputStream ois = new ObjectInputStream(fis);
            users = (List<User>) ois.readObject(); //needs "casting"
            ois.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (users == null) {
                users = new ArrayList<>();
            }
        }
    }

    //check if player entered the top 10
    private boolean getIsTop10(int score) {

        if (users.isEmpty()) { //first place
            return true;
        } else if (users.size() < 10) { //have place in list
            return true;
        }

        for (int i = 0; i < 10; i++) { //only top 10 can make it to list
            if (score >= users.get(i).getScore()) {
                return true;
            }
        }
        return false;
    }

    private void saveUserList() {
        try {
            FileOutputStream fos = openFileOutput("users", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos); //can handle Objects!!
            //always write the Root Object
            oos.writeObject(users); //writing object directly (needs to Serialize Person)
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveUserList(); //save updated top 10 list
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GameOverActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
