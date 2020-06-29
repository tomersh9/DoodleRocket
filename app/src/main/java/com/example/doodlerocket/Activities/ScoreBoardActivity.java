package com.example.doodlerocket.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.doodlerocket.GameObjects.User;
import com.example.doodlerocket.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScoreBoardActivity extends AppCompatActivity {

    private List<User> users = null;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoreboard_layout);

        //fixed portrait mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TextView titleTv = findViewById(R.id.highscore_title);
        TextView nameTv = findViewById(R.id.name_layout_tv);
        TextView placeTv = findViewById(R.id.rank_layout_tv);
        TextView scoreTv = findViewById(R.id.score_layout_tv);

        //view animations
        //YoYo.with(Techniques.SlideInLeft).duration(2000).delay(300).playOn(titleTv);
        YoYo.with(Techniques.StandUp).duration(1200).playOn(placeTv);
        YoYo.with(Techniques.StandUp).duration(1200).playOn(scoreTv);
        YoYo.with(Techniques.StandUp).duration(1200).playOn(nameTv);

        Button backHomebtn = findViewById(R.id.back_to_home_board_btn);
        backHomebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreBoardActivity.this,HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });

        //load user list
        loadUserList();

        //sort list by score with Comparator
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                return u2.getScore() - u1.getScore();
            }
        });

        //only top 10 list
        while(users.size() > 10) {
            int i = users.size() - 1;
            users.remove(i);
        }

        RecyclerView recyclerView = findViewById(R.id.score_list);
        recyclerView.setHasFixedSize(true); // const size

        //defines the layout (Vertical, Horizontal, Grid..)
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1)); //number of col in a row

        //create instance of adapter that has ViewHolder inside (to inflate our cells)
        final UserAdapter userAdapter = new UserAdapter(users);
        //setting adapter
        recyclerView.setAdapter(userAdapter);

    }

    private void saveUserList() {

        try {
            FileOutputStream fos = openFileOutput("users",MODE_PRIVATE);
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

    private void loadUserList() {

        try {
            FileInputStream fis = openFileInput("users");
            ObjectInputStream ois = new ObjectInputStream(fis);
            users = (List<User>) ois.readObject();
            ois.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            if(users == null) {
                users = new ArrayList<>();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //push list to memory
        saveUserList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
