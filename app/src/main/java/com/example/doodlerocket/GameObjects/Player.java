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
    private int minY, maxY;
    private int health;
    private Bitmap playerBitmap;

    public Player(int canvasW,int canvasH,Resources resources, int skinID) {

        //initial position of player
        this.x = canvasW/2;
        this.y = 1500;
        this.health = 3;

        //scaled bitmap
        playerBitmap = BitmapFactory.decodeResource(resources, skinID);
        width = (int)(playerBitmap.getWidth()/1.5f);
        height = (int)(playerBitmap.getHeight()/1.5f);
        playerBitmap = Bitmap.createScaledBitmap(playerBitmap,width,height,false);

        //bounds of player in screen
        this.minX = 0;
        this.maxX = canvasW - playerBitmap.getWidth();
        this.minY = 0;
        this.maxY = canvasH - playerBitmap.getHeight();

    }

    @Override
    public void drawObject(Canvas canvas) {
        canvas.drawBitmap(playerBitmap,getObjectX(),getObjectY(),null);
    }

    @Override
    public void updateLocation() {
        //retain player in screen
        if(x < minX) x = minX;
        if(x > maxX) x = maxX;
        if(y < minY) y = minY;
        if(y > maxY) y = maxY;
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
