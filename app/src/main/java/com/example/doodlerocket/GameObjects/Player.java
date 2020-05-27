package com.example.doodlerocket.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.doodlerocket.R;

public class Player implements IGameObjects {

    private int x,y;
    private int minX, maxX;
    private int health;
    private Bitmap playerBitmap;

    public Player(int canvasW,Resources resources) {

        //initial position of player
        this.x = 450;
        this.y = 2100;
        this.health = 3;

        //scaled bitmap
        playerBitmap = BitmapFactory.decodeResource(resources, R.drawable.spaceship_tin_png);
        int width = playerBitmap.getWidth()/10;
        int height = playerBitmap.getHeight()/10;
        playerBitmap = Bitmap.createScaledBitmap(playerBitmap,width,height,false);

        //bounds of player in screen
        this.minX = 0;
        this.maxX = canvasW - width;

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
