package com.example.doodlerocket.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.doodlerocket.R;

public abstract class Consumables implements IGameObjects {

    private int consumableX, consumableY, consumableSpeed;
    private Bitmap consumableBitmap;

    public Consumables(int playerMinX, int playerMaxX, int speed, Resources resources) {

        consumableX = (int) Math.floor(Math.random() * ((playerMaxX - playerMinX) + playerMinX));
        consumableY = 0;
        consumableSpeed = speed;
        consumableBitmap = BitmapFactory.decodeResource(resources, R.drawable.goldcoin_1);
    }

    @Override
    public void drawObject(Canvas canvas) {
        canvas.drawBitmap(consumableBitmap,getObjectX(),getObjectY(),null);
    }

    @Override
    public void updateLocation() {
        consumableY += consumableSpeed;
    }

    @Override
    public int getObjectX() {
        return consumableX;
    }

    @Override
    public int getObjectY() {
        return consumableY;
    }

    @Override
    public boolean collisionDetection(int playerX, int playerY, Bitmap playerBitmap) {
        if(playerX < consumableX && consumableX < (playerX + playerBitmap.getWidth())
                && playerY < consumableY && consumableY < (playerY + playerBitmap.getHeight())) {
            return true;
        }
        return false;
    }
}
