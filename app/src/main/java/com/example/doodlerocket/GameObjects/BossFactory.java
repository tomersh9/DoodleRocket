package com.example.doodlerocket.GameObjects;

import android.content.res.Resources;

import com.example.doodlerocket.R;

public class BossFactory {

    private int speed, health;
    private int canvasW;
    private Resources resources;

    //boss skins
    private int[] bossSkins = {R.drawable.moon_boss_200,R.drawable.kraken_boss_200,R.drawable.spider_boss_200
                              ,R.drawable.forest_boss_200, R.drawable.ocean_boss_150
                              ,R.drawable.lava_boss_200};

    //setting base parameters of enemy and increase with levels
    public BossFactory(Resources resources, int speed, int health , int canvasW) {

        this.speed = speed;
        this.health = health;
        this.canvasW = canvasW;
        this.resources = resources;
    }

    public Boss generateBoss(int lvl) {

        //spawn enemies with different properties according to level
        //Boss(int x, int y, int speed, int health, int canvasW, int enemySkinID, Resources resources)

        switch (lvl) {

            case 1:
                return new Boss(canvasW/2,0,speed,health+10,canvasW,bossSkins[0],resources);

            case 2:
                return new Boss(canvasW/2,0,speed,health+20,canvasW,bossSkins[1],resources);

            case 3:
                return new Boss(canvasW/2,0,speed,health+30,canvasW,bossSkins[2],resources);

            case 4:
                return new Boss(canvasW/2,0,speed,health+40,canvasW,bossSkins[3],resources);

            case 5:
                return new Boss(canvasW/2,0,speed,health+50,canvasW,bossSkins[4],resources);

            case 6:
                return new Boss(canvasW/2,0,speed,health+70,canvasW,bossSkins[5],resources);

        }
        return null;
    }
}
