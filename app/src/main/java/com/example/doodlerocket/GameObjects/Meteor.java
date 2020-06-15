package com.example.doodlerocket.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.doodlerocket.R;

import java.util.ArrayList;
import java.util.List;

public class Meteor implements IGameObjects {

    private int meteorX, meteorY, meteorSpeed;
    private Bitmap meteorBitmap;
    private boolean isBoom = false;

    private int bitmapResArr[] = {R.drawable.meteor1,R.drawable.meteor2,R.drawable.meteor3,R.drawable.meteor4,R.drawable.meteor5,
                                    R.drawable.meteor6, R.drawable.meteor7, R.drawable.meteor8, R.drawable.meteor9,R.drawable.meteor10};

    private int i = 0;
    private List<Bitmap> hitEffectList = new ArrayList<>();


    public Meteor(int playerMinX, int playerMaxX,int speed, int meteorSkinID,Resources resources) {

        meteorX = (int) Math.floor(Math.random() * ((playerMaxX - playerMinX) + playerMinX)); //random X position at spawn
        meteorY = 0; //top of screen
        meteorSpeed = speed;

        //smaller bitmaps
        meteorBitmap = BitmapFactory.decodeResource(resources,meteorSkinID);
        float width = meteorBitmap.getWidth()*0.6f;
        float height = meteorBitmap.getHeight()*0.6f;
        meteorBitmap = Bitmap.createScaledBitmap(meteorBitmap,(int)width,(int)height,false);

        //hit effect
        hitEffectList.add(BitmapFactory.decodeResource(resources,R.drawable.boom1));
        hitEffectList.add(BitmapFactory.decodeResource(resources,R.drawable.boom2));
        hitEffectList.add(BitmapFactory.decodeResource(resources,R.drawable.boom3));
        hitEffectList.add(BitmapFactory.decodeResource(resources,R.drawable.boom4));
        hitEffectList.add(BitmapFactory.decodeResource(resources,R.drawable.boom5));
    }

    @Override
    public void drawObject(Canvas canvas) {
        if(!isBoom) {
            canvas.drawBitmap(meteorBitmap,meteorX,meteorY,null);
        }
        else  { //explode on hit
            canvas.drawBitmap(hitEffectList.get(i/5),getObjectX(),getObjectY(),null);
            i++;
            i=i%25;
        }
    }

    @Override
    public void updateLocation() {
        if(!isBoom) {
            meteorY += meteorSpeed;
        }
    }

    public void setBitmap(Resources res, int i) {

        meteorBitmap = BitmapFactory.decodeResource(res,bitmapResArr[i]);
    }

    @Override
    public int getObjectX() {
        return meteorX;
    }

    @Override
    public int getObjectY() {
        return meteorY;
    }

    public boolean isBoom() {
        return isBoom;
    }

    public void setBoom(boolean boom) {
        isBoom = boom;
    }

    public void setMeteorX(int meteorX) {
        this.meteorX = meteorX;
    }

    public void setMeteorY(int meteorY) {
        this.meteorY = meteorY;
    }

    public Bitmap getMeteorBitmap() {
        return meteorBitmap;
    }


    @Override //tweak here
    public boolean collisionDetection(int playerX, int playerY, Bitmap playerBitmap) {

        if     (playerX < getObjectX() + (meteorBitmap.getWidth())
                && getObjectX() < (playerX + playerBitmap.getWidth()) //rocket between obstacle width
                && playerY < getObjectY() + (meteorBitmap.getHeight())
                && getObjectY() < (playerY + playerBitmap.getHeight())) { //rocket between obstacle height
            return true;
        }
        return false;
    }

    public Rect getCollisionShape() {
        return new Rect(meteorX,meteorY,meteorX+meteorBitmap.getWidth(),meteorY+meteorBitmap.getHeight());
    }

}
