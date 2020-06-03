package com.example.doodlerocket.GameObjects;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaPlayer;

import com.example.doodlerocket.R;

import java.util.ArrayList;
import java.util.List;

public class Bullet implements IGameObjects {

    private int x,y,speed;
    private Bitmap bulletBitmap;
    private boolean isBoom = false;

    private int i = 0;
    private List<Bitmap> hitEffectList = new ArrayList<>();

    public Bullet(Context context, Resources resources, int playerX, int playerY) {

        x = playerX + 75;
        y = playerY;
        speed = 20;

        //hit effect
        hitEffectList.add(BitmapFactory.decodeResource(resources,R.drawable.boom1));
        hitEffectList.add(BitmapFactory.decodeResource(resources,R.drawable.boom2));
        hitEffectList.add(BitmapFactory.decodeResource(resources,R.drawable.boom3));
        hitEffectList.add(BitmapFactory.decodeResource(resources,R.drawable.boom4));
        hitEffectList.add(BitmapFactory.decodeResource(resources,R.drawable.boom5));


        bulletBitmap = BitmapFactory.decodeResource(resources, R.drawable.slice_beams_43);
        int width = bulletBitmap.getWidth()/2;
        int height = bulletBitmap.getHeight()/2;
        bulletBitmap = Bitmap.createScaledBitmap(bulletBitmap,width,height,false);

    }

    @Override
    public void drawObject(Canvas canvas) {

        if(!isBoom)
            canvas.drawBitmap(bulletBitmap,getObjectX(),getObjectY(),null);

        else  { //explode on hit
            canvas.drawBitmap(hitEffectList.get(i/5),getObjectX(),getObjectY(),null);
            i++;
            i=i%25;
        }
    }

    @Override
    public void updateLocation() {
        if(!isBoom) {
            y -= speed;
        }
        else {
            y += speed/3;
        }

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

    public boolean isBoom() {
        return isBoom;
    }

    public void setBoom(boolean boom) {
        isBoom = boom;
    }

    @Override
    public boolean collisionDetection(int meteorX, int meteorY, Bitmap meteorBitmap) {

        if(meteorX < x && x < (meteorX + meteorBitmap.getWidth())
                && meteorY < y && y < (meteorY + meteorBitmap.getHeight())) {
            return true;
        }
        return false;
    }

    public Rect getCollisionShape() {
        return new Rect(x,y,x+bulletBitmap.getWidth(),y+bulletBitmap.getHeight());
    }
}
