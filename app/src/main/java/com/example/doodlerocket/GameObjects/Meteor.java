package com.example.doodlerocket.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.doodlerocket.R;

public class Meteor implements IGameObjects {

    private int meteorX, meteorY, meteorSpeed;
    private Bitmap meteorBitmap;
    private int bitmapResArr[] = {R.drawable.meteor1,R.drawable.meteor2,R.drawable.meteor3,R.drawable.meteor4,R.drawable.meteor5,
                                    R.drawable.meteor6, R.drawable.meteor7, R.drawable.meteor8, R.drawable.meteor9,R.drawable.meteor10};


    public Meteor(int playerMinX, int playerMaxX,int speed, Resources resources) {

        meteorX = (int) Math.floor(Math.random() * ((playerMaxX - playerMinX) + playerMinX)); //random X position at spawn
        meteorY = 0; //top of screen
        meteorSpeed = speed;
        meteorBitmap = BitmapFactory.decodeResource(resources,bitmapResArr[(int) (Math.random()* (9))]);
    }

    @Override
    public void drawObject(Canvas canvas) {

        canvas.drawBitmap(meteorBitmap,meteorX,meteorY,null);
    }

    @Override
    public void updateLocation() {
        meteorY += meteorSpeed;
    }

    public void setBitmap(Resources res, int i) {
        meteorBitmap = BitmapFactory.decodeResource(res,bitmapResArr[i]);
    }

    @Override
    public int getObjectX() {
        return meteorX;
    }

    @Override
    public int getObjectY() {
        return meteorY;
    }

    public void setMeteorX(int meteorX) {
        this.meteorX = meteorX;
    }

    public void setMeteorY(int meteorY) {
        this.meteorY = meteorY;
    }

    public Bitmap getMeteorBitmap() {
        return meteorBitmap;
    }


    @Override
    public boolean collisionDetection(int playerX, int playerY, Bitmap playerBitmap) {

        if     (playerX < getObjectX() + (meteorBitmap.getWidth())
                && getObjectX() < (playerX + playerBitmap.getWidth()) //rocket between obstacle width
                && playerY < getObjectY() + (meteorBitmap.getHeight())
                && getObjectY() < (playerY + playerBitmap.getHeight())) { //rocket between obstacle height
            return true;
        }
        return false;
    }

}
