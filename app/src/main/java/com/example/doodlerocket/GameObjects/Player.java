package com.example.doodlerocket.GameObjects;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.doodlerocket.R;

public class Player implements IGameObjects {

    private int x,y;
    private int width, height;
    private int minX, maxX;
    private int health;
    private Bitmap playerBitmap;

    public Player(int canvasW,float screenRatioX,float screenRatioY,Resources resources, int skinID) {

        //initial position of player
        this.x = (int)(450 * screenRatioX);
        this.y = (int)(2100 * screenRatioY);
        this.health = 3;

        //scaled bitmap
        playerBitmap = BitmapFactory.decodeResource(resources, skinID);
        width = playerBitmap.getWidth()/2;
        height = playerBitmap.getHeight()/2;

        //fit all screen sizes
        width *= (int) screenRatioX;
        height *= (int) screenRatioY;

        playerBitmap = Bitmap.createScaledBitmap(playerBitmap,width,height,false);

        //bounds of player in screen
        this.minX = 0;
        this.maxX = canvasW - playerBitmap.getWidth();

    }

    @Override
    public void drawObject(Canvas canvas) {
        canvas.drawBitmap(playerBitmap,getObjectX(),getObjectY(),null);
    }

    @Override
    public void updateLocation() {
        if(x < minX) x = minX;
        if(x > maxX) x = maxX;
    }

    public Bitmap getPlayerBitmap() {
        return playerBitmap;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    @Override
    public int getObjectX() {
        return x;
    }

    @Override
    public int getObjectY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean collisionDetection(int otherX, int otherY, Bitmap otherBitmap) {
        return false;
    }
}
