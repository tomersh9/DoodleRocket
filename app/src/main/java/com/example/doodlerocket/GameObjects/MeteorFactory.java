package com.example.doodlerocket.GameObjects;

import android.content.res.Resources;

import com.example.doodlerocket.R;

import java.util.Random;

public class MeteorFactory {

    private int playerMinX, playerMaxX, speed;
    private Resources resources;

    private int earthSkins[] = {R.drawable.meteor1,R.drawable.meteor2};
    private int moonSkins[] = {R.drawable.meteor3,R.drawable.meteor4};
    private int desertSkins[] = {R.drawable.meteor9, R.drawable.meteor10};
    private int forestSkins[] = {R.drawable.meteor7, R.drawable.meteor8};
    private int waterSkins[] = {R.drawable.meteor5,R.drawable.meteor6};
    private int iceSkins[] = {R.drawable.meteor5,R.drawable.meteor1};
    private int hellSkins[] = {R.drawable.meteor10,R.drawable.meteor7,R.drawable.meteor1,R.drawable.meteor5};

    public MeteorFactory(int playerMinX, int playerMaxX, int speed, Resources resources) {
        this.playerMinX = playerMinX;
        this.playerMaxX = playerMaxX;
        this.speed = speed;
        this.resources = resources;
    }

    //generate meteors according to the level background
    public Meteor generateMeteor(int currLvl) {

        //random between 2 meteors each level
        Random random = new Random();
        int randRes = random.nextInt(2);
        int randHell = random.nextInt(3);

        switch (currLvl) {

            //lvl 1
            case 1:
                return new Meteor(playerMinX,playerMaxX,speed,moonSkins[randRes],resources);

            //lvl 2
            case 2:
                return new Meteor(playerMinX,playerMaxX,speed+4,earthSkins[randRes],resources);

            //lvl 3
            case 3:
                return new Meteor(playerMinX,playerMaxX,speed+6,desertSkins[randRes],resources);

            //lvl 4
            case 4:
                return new Meteor(playerMinX,playerMaxX,speed+9,forestSkins[randRes],resources);

            //lvl 5
            case 5:
                return new Meteor(playerMinX,playerMaxX,speed+10,waterSkins[randRes],resources);

            //lvl 6
            case 6:
                return new Meteor(playerMinX,playerMaxX,speed+9,iceSkins[randRes],resources);

            //final lvl
            case 7:
                return new Meteor(playerMinX,playerMaxX,speed+6,hellSkins[randHell],resources);
        }
        return null;
    }
}
