package com.example.doodlerocket.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.doodlerocket.R;

public class LoginPage extends Activity {

    SharedPreferences sp;
    EditText nameEt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        nameEt = findViewById(R.id.name_input);

        //getting info from editor
        sp = getSharedPreferences("username",MODE_PRIVATE);

        if(sp.getBoolean("not_first_run",false))
            nameEt.setText(sp.getString("username",""));

        Button loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nameEt.getText().toString().matches("")) {
                    Toast.makeText(LoginPage.this, "Must enter name", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(LoginPage.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    //best place to store data
    @Override
    protected void onPause() {
        super.onPause();

        //create Editor to save data in the SharedPreferences Object
        SharedPreferences.Editor editor = sp.edit();

        //saving data in sp
        editor.putString("username",nameEt.getText().toString());
        editor.putBoolean("not_first_visit",true);

        //committing (pushing) the data
        editor.commit();
    }
}
