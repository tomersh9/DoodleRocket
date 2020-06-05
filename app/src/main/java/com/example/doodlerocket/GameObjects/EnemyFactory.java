package com.example.doodlerocket.GameObjects;

import android.content.res.Resources;

import com.example.doodlerocket.R;

import java.util.Random;

public class EnemyFactory {

    private int playerMinX,playerMaxX,speed, health;
    private int canvasW;
    private Resources resources;

    //regular enemies skins
    private int[] enemySkins =
            {R.drawable.alien_eye_100,R.drawable.alien_ship_boss_purple_100
            ,R.drawable.alien_ship_boss_red_100,R.drawable.alien_ship_grey_100
            ,R.drawable.alien_ship_pink_100,R.drawable.alien_ship_purple_100};

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

        //spawn enemies with different properties according to level
        switch (lvl) {

            case 1:
                return new Enemy((int) Math.floor(Math.random() * (playerMaxX)),0,speed,health,canvasW,enemySkins[0],resources);

            case 2:
                return new Enemy((int) Math.floor(Math.random() * (playerMaxX)),0,speed+3,health+1,canvasW,enemySkins[1],resources);

            case 3:
                return new Enemy((int) Math.floor(Math.random() * (playerMaxX)),0,speed+5,health+2,canvasW,enemySkins[2],resources);

            case 4:
                return new Enemy((int) Math.floor(Math.random() * (playerMaxX)),0,speed+7,health+3,canvasW,enemySkins[3],resources);

            case 5:
                return new Enemy((int) Math.floor(Math.random() * (playerMaxX)),0,speed+10,health+4,canvasW,enemySkins[4],resources);

            case 6:
                return new Enemy((int) Math.floor(Math.random() * (playerMaxX)),0,speed+12,health+5,canvasW,enemySkins[5],resources);

        }
        return null;
    }
}
