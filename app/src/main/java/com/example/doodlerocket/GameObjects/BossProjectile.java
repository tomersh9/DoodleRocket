package com.example.doodlerocket.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.doodlerocket.R;


public class BossProjectile implements IGameObjects {

    private int x,y,speed;
    private  int width, height;
    private Bitmap projectileBitmap;

    public BossProjectile(Resources resources, Boss boss) {

        //projectile from bottom center of enemy
        x = boss.getObjectX() + boss.getBossWidth()/2;
        y = boss.getObjectY() + boss.getBossHeight();
        speed = 40;

        projectileBitmap = BitmapFactory.decodeResource(resources, R.drawable.boss_beam);
        width = projectileBitmap.getWidth()/2;
        height = projectileBitmap.getHeight()/2;
        projectileBitmap = Bitmap.createScaledBitmap(projectileBitmap,width,height,false);

    }

    @Override
    public void drawObject(Canvas canvas) {
        canvas.drawBitmap(projectileBitmap,getObjectX(),getObjectY(),null);
    }

    @Override
    public void updateLocation() {
        y += speed;
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
    public boolean collisionDetection(int playerX, int playerY, Bitmap playerBitmap) {

        //can tweak player hit box
        if(playerX + 10 < x && x < (playerX + playerBitmap.getWidth() - 10)
                && playerY < y && y < (playerY + playerBitmap.getHeight())) {
            return true;
        }
        return false;
    }

    public Rect getCollisionShape() {
        return new Rect(x,y,x+ projectileBitmap.getWidth(),y+ projectileBitmap.getHeight());
    }
}
