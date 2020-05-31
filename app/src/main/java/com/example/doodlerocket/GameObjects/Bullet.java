package com.example.doodlerocket.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.doodlerocket.R;

public class Bullet implements IGameObjects {

    private int x,y,speed;
    private Bitmap bulletBitmap;

    public Bullet(Resources resources, int playerX, int playerY) {

        x = playerX + 50;
        y = playerY;
        speed = 20;

        bulletBitmap = BitmapFactory.decodeResource(resources, R.drawable.laser_1);
        int width = bulletBitmap.getWidth()*3;
        int height = bulletBitmap.getHeight()*3;
        bulletBitmap = Bitmap.createScaledBitmap(bulletBitmap,width,height,false);

    }

    @Override
    public void drawObject(Canvas canvas) {
        canvas.drawBitmap(bulletBitmap,getObjectX(),getObjectY(),null);
    }

    @Override
    public void updateLocation() {
        y -= speed;
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
    public boolean collisionDetection(int meteorX, int meteorY, Bitmap meteorBitmap) {

        if(meteorX - 50 < x && x < (meteorX + meteorBitmap.getWidth())
                && meteorY < y && y < (meteorY + meteorBitmap.getHeight())) {
            return true;
        }
        return false;
    }

    public Rect getCollisionShape() {
        return new Rect(x,y,x+bulletBitmap.getWidth(),y+bulletBitmap.getHeight());
    }
}
