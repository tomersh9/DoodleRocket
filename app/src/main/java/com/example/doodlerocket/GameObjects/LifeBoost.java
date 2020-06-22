package com.example.doodlerocket.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.doodlerocket.R;

public class LifeBoost implements IGameObjects {

    private int boostX, boostY, boostSpeed;
    private Bitmap boostBitmap;

    public LifeBoost(int playerMinX, int playerMaxX, Resources resources) {

        boostX = (int) Math.floor(Math.random() * ((playerMaxX - playerMinX) + playerMinX));
        boostY = 0;
        boostSpeed = 10;
        boostBitmap = BitmapFactory.decodeResource(resources, R.drawable.life_boost_50);
    }

    @Override
    public void drawObject(Canvas canvas) {
        canvas.drawBitmap(boostBitmap,getObjectX(),getObjectY(),null);
    }

    @Override
    public void updateLocation() {
        boostY += boostSpeed;
    }

    @Override
    public int getObjectX() {
        return boostX;
    }

    @Override
    public int getObjectY() {
        return boostY;
    }

    public void setBoostX(int boostX) {
        this.boostX = boostX;
    }

    public void setBoostY(int boostY) {
        this.boostY = boostY;
    }

    @Override
    public boolean collisionDetection(int playerX, int playerY, Bitmap playerBitmap) {

        if(playerX - 50 < boostX && boostX < (playerX + playerBitmap.getWidth())
                && playerY < boostY && boostY < (playerY + playerBitmap.getHeight())) {
            return true;
        }
        return false;
    }
}
