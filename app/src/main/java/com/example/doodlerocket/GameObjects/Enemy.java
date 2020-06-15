package com.example.doodlerocket.GameObjects;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.print.PrinterId;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.doodlerocket.R;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Enemy implements IGameObjects {

    private int x,y,speed;
    private int health;
    private int width, height;
    private int canvasW;
    private boolean movingLeft;
    private Bitmap enemyBitmap;

    //creating death effect
    private List<Bitmap> deathEffectList = new ArrayList<>();
    private boolean isDead = false;
    private int i = 0;

    public Enemy(int x, int y, int speed, int health, int enemySkinID, int canvasW,boolean movingLeft ,Resources resources) {

        this.x = x;
        this.y = y;
        this.speed = speed;
        this.health = health;
        this.canvasW = canvasW;
        this.movingLeft = movingLeft;

        enemyBitmap = BitmapFactory.decodeResource(resources,enemySkinID);
        this.width = enemyBitmap.getWidth();
        this.height = enemyBitmap.getHeight();
        enemyBitmap = Bitmap.createScaledBitmap(enemyBitmap,width,height,false);

        //death effect - unique to enemy
        deathEffectList.add(BitmapFactory.decodeResource(resources,R.drawable.boom1));
        deathEffectList.add(BitmapFactory.decodeResource(resources,R.drawable.boom2));
        deathEffectList.add(BitmapFactory.decodeResource(resources,R.drawable.boom3));
        deathEffectList.add(BitmapFactory.decodeResource(resources,R.drawable.boom4));
        deathEffectList.add(BitmapFactory.decodeResource(resources,R.drawable.boom5));

    }

    @Override
    public void drawObject(Canvas canvas) {

        if(!isDead) {
            canvas.drawBitmap(enemyBitmap,getObjectX(),getObjectY(),null);
        }
        else {
            canvas.drawBitmap(deathEffectList.get(i/5),(x+enemyBitmap.getWidth()/2),(y+enemyBitmap.getHeight()/2),null);
            i++;
            i=i%25;
        }

    }

    @Override
    public void updateLocation() {

        if(!isDead) {
            y += speed/2;

            if(movingLeft) {
                x -= speed;
                if(x + enemyBitmap.getWidth() < canvasW/4) {
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
        else {
            y += speed/4;
        }

    }

    @Override
    public int getObjectX() {
        return x;
    }

    public int getHealth() { return health;}

    public void takeDamage(int dmg) {
        this.health -= dmg;
        this.y-=20; // bounce on hit
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

    //to resize in GameView
    public int getEnemyHeight() {return height;}
    public int getEnemyWidth() {return width;}

    public Bitmap getEnemyBitmap() {
        return enemyBitmap;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
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

    public Rect getCollisionShape() {
        return new Rect(x,y,x+enemyBitmap.getWidth(),y+enemyBitmap.getHeight());
    }
}
