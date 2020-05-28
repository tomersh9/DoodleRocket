package com.example.doodlerocket.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.doodlerocket.R;

public class Enemy implements IGameObjects {

    private int x,y,speed;
    private int width, height;
    private int canvasW;
    private boolean movingLeft = true;
    private Bitmap enemyBitmap;
    private int[] enemySkins = {R.drawable.alien_spaceship_png,R.drawable.alien_elf_blue_png,R.drawable.alien_boss_rgb_png
                                ,R.drawable.alien_green_png,R.drawable.alien_ship_reg_png,R.drawable.alien_spaceship_purple_png
                                ,R.drawable.alienship_reg_purple_png,R.drawable.alienship_reg_red_png,R.drawable.alien_grey_eye_png};

    public Enemy(Resources resources, int x, int y, int canvasW) {
        this.x = x;
        this.y = y;
        this.speed = 10;
        this.canvasW = canvasW;

        enemyBitmap = BitmapFactory.decodeResource(resources,R.drawable.alien_spaceship_png);
        width = enemyBitmap.getWidth()/5;
        height = enemyBitmap.getHeight()/5;
        enemyBitmap = Bitmap.createScaledBitmap(enemyBitmap,width,height,false);
    }

    @Override
    public void drawObject(Canvas canvas) {
        canvas.drawBitmap(enemyBitmap,getObjectX(),getObjectY(),null);
    }

    @Override
    public void updateLocation() {

        if(movingLeft) {

            x -= speed;
            if(x + enemyBitmap.getWidth() < canvasW/6) {
                movingLeft = false;
            }
        }
        else {

            x += speed;
            if(x + enemyBitmap.getWidth() > canvasW - 100) {
                movingLeft = true;
            }
        }
    }

    public void setEnemyBitmap(Resources res, int i) {
        enemyBitmap = BitmapFactory.decodeResource(res,enemySkins[i]);
        width = enemyBitmap.getWidth()/5;
        height = enemyBitmap.getHeight()/5;
        enemyBitmap = Bitmap.createScaledBitmap(enemyBitmap,width,height,false);
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

    public Bitmap getEnemyBitmap() {
        return enemyBitmap;
    }

    @Override
    public boolean collisionDetection(int playerX, int playerY, Bitmap playerBitmap) {
        if     (playerX < getObjectX() + (enemyBitmap.getWidth())
                && getObjectX() < (playerX + playerBitmap.getWidth()) //rocket between obstacle width
                && playerY < getObjectY() + (enemyBitmap.getHeight())
                && getObjectY() < (playerY + playerBitmap.getHeight())) { //rocket between obstacle height
            return true;
        }
        return false;
    }
}
