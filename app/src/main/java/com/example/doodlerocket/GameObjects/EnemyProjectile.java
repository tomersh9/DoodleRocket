package com.example.doodlerocket.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.doodlerocket.R;

import static com.example.doodlerocket.GameView.screenRatioX;
import static com.example.doodlerocket.GameView.screenRatioY;

public class EnemyProjectile implements IGameObjects {

    private int x,y,speed;
    private  int width, height;
    private Bitmap projectileBitmap;

    public EnemyProjectile(Resources resources, Enemy enemy) {

        //projectile from bottom center of enemy
        x = enemy.getObjectX() + enemy.getEnemyWidth()/2;
        y = enemy.getObjectY() + enemy.getEnemyHeight();
        speed = 30;

        projectileBitmap = BitmapFactory.decodeResource(resources, R.drawable.slice_beams_40);
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
