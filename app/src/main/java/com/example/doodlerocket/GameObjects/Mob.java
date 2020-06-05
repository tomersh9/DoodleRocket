package com.example.doodlerocket.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

public abstract class Mob implements IGameObjects {

    private int x,y,speed;
    private int health;
    private boolean isDead = false;


    public Mob(int x, int y, int speed, int health) {

        this.x = x;
        this.y = y;
        this.health = health;
        this.speed = speed;
    }

    @Override
    public abstract void drawObject(Canvas canvas);

    @Override
    public abstract void updateLocation();

    @Override
    public abstract boolean collisionDetection(int playerX, int playerY, Bitmap playerBitmap);

    @Override
    public int getObjectX() {
        return x;
    }

    @Override
    public int getObjectY() {
        return y;
    }

    public int getSpeed() {
        return this.speed;
    }

    public void moveHorizontal(int x) {
        this.x = x;
    }

    public void moveVertical(int speed) {
        this.y += speed;
    }

    public void takeDamage(int dmg) {
        this.health -= dmg;
    }

    public int getHealth() {
        return this.health;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }
}
