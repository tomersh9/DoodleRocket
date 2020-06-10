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
    public Meteor generateMeteor(int backgroundID) {

        //random between 2 meteors each level
        Random random = new Random();
        int randRes = random.nextInt(2);
        int randHell = random.nextInt(3);

        switch (backgroundID) {

            //lvl 1
            case R.drawable.moon_bg_800:
                return new Meteor(playerMinX,playerMaxX,speed,moonSkins[randRes],resources);

            //lvl 2
            case R.drawable.city_bg:
                return new Meteor(playerMinX,playerMaxX,speed+3,earthSkins[randRes],resources);

            //lvl 3
            case R.drawable.desert_backgorund:
                return new Meteor(playerMinX,playerMaxX,speed+5,desertSkins[randRes],resources);

            //lvl 4
            case R.drawable.forest_bg_400:
                return new Meteor(playerMinX,playerMaxX,speed+7,forestSkins[randRes],resources);

            //lvl 5
            case R.drawable.ocean_bg_1:
                return new Meteor(playerMinX,playerMaxX,speed+8,waterSkins[randRes],resources);

            //lvl 6
            case R.drawable.ice_bg_800:
                return new Meteor(playerMinX,playerMaxX,speed+10,iceSkins[randRes],resources);

            case R.drawable.lava_bg_1:
                return new Meteor(playerMinX,playerMaxX,speed+9,hellSkins[randHell],resources);
        }
        return null;
    }
}
