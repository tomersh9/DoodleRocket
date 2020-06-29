package com.example.doodlerocket.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.example.doodlerocket.R;

import java.util.Random;

public class EnemyFactory {

    private int playerMinX,playerMaxX,speed, health;
    private int canvasW;
    private Resources resources;

    //switch enemy movement
    private int i = 0;

    //regular enemies skins
    private int[] enemySkins =
            {R.drawable.alien_eye_100,R.drawable.alien_ship_boss_purple_100
            ,R.drawable.alien_ship_boss_red_100,R.drawable.alien_ship_grey_100
            ,R.drawable.alien_ship_pink_100,R.drawable.alien_ship_purple_100, R.drawable.alien_boss_rgb_100};

    //setting base parameters of enemy and increase with levels
    public EnemyFactory(Resources resources, int playerMinX, int playerMaxX, int speed, int health ,int canvasW) {

        this.playerMinX = playerMinX;
        this.playerMaxX = playerMaxX;
        this.speed = speed;
        this.health = health;
        this.canvasW = canvasW;
        this.resources = resources;
    }

    public Enemy generateEnemy(int lvl) {

        //switch enemy movement
        i++;

        //public Enemy(Resources resources, int x, int y, int speed, int health,int canvasW,int enemySkinID)
        //spawn enemies with different properties according to level
        switch (lvl) {

            case 1:
                return new Enemy((int) Math.floor(Math.random() * (playerMaxX)),0,speed,health+1,enemySkins[0],canvasW,(i%2==0),resources);

            case 2:
                return new Enemy((int) Math.floor(Math.random() * (playerMaxX)),0,speed+3,health+3,enemySkins[1],canvasW,(i%2==0),resources);

            case 3:
                return new Enemy((int) Math.floor(Math.random() * (playerMaxX)),0,speed+4,health+4,enemySkins[2],canvasW,(i%2==0),resources);

            case 4:
                return new Enemy((int) Math.floor(Math.random() * (playerMaxX)),0,speed+6,health+4,enemySkins[3],canvasW,(i%2==0),resources);

            case 5:
                return new Enemy((int) Math.floor(Math.random() * (playerMaxX)),0,speed+7,health+5,enemySkins[4],canvasW,(i%2==0),resources);

            case 6:
                return new Enemy((int) Math.floor(Math.random() * (playerMaxX)),0,speed+5,health+5,enemySkins[5],canvasW,(i%2==0),resources);

            case 7:
                return new Enemy((int) Math.floor(Math.random() * (playerMaxX)),0,speed+4,health+4,R.drawable.hell_mob_85,canvasW,(i%2==0),resources);

        }
        return null;
    }
}
