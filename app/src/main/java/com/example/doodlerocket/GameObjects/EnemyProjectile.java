package com.example.doodlerocket.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.doodlerocket.R;

public class EnemyProjectile implements IGameObjects {

    private int x,y,speed;
    private Bitmap projectileBitmap;

    public EnemyProjectile(Resources resources, Enemy enemy) {

        //projectile from bottom center of enemy
        x = enemy.getObjectX() + enemy.getEnemyWidth()/2;
        y = enemy.getObjectY() + enemy.getEnemyHeight();
        speed = 30;

        projectileBitmap = BitmapFactory.decodeResource(resources, R.drawable.laser_2);
        int width = projectileBitmap.getWidth()*3;
        int height = projectileBitmap.getHeight()*3;
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

        if(playerX + 50 < x && x < (playerX + playerBitmap.getWidth() - 50)
                && playerY < y && y < (playerY + playerBitmap.getHeight())) {
            return true;
        }
        return false;
    }

    public Rect getCollisionShape() {
        return new Rect(x,y,x+ projectileBitmap.getWidth(),y+ projectileBitmap.getHeight());
    }
}
