package com.example.doodlerocket.GameObjects;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.doodlerocket.R;

import java.util.Random;

public class Enemy implements IGameObjects {

    private int x,y,speed;
    private int health;
    private int width, height;
    private int canvasW;
    private Resources resources;
    private boolean movingLeft = true;
    private Bitmap enemyBitmap;
    private int[] enemySkins = {R.drawable.alien_boss_rgb_100,R.drawable.alien_boss_ship_detailed_100,R.drawable.alien_elf_100
                                ,R.drawable.alien_eye_100,R.drawable.alien_green_100,R.drawable.alien_ship_boss_purple_100
                                ,R.drawable.alien_boss_rgb_100,R.drawable.alien_ship_boss_red_100,R.drawable.alien_ship_grey_100
                                ,R.drawable.alien_ship_pink_100,R.drawable.alien_ship_purple_100};

    public Enemy(Context context, Resources resources, int x, int y, int canvasW) {


        //random works this way..
        Random rand = new Random();
        int randRes = rand.nextInt(10);

        this.resources = resources;
        this.x = x;
        this.y = y;
        this.speed = 10;
        this.health = 5;
        this.canvasW = canvasW;

        enemyBitmap = BitmapFactory.decodeResource(resources,enemySkins[randRes]);
        this.width = enemyBitmap.getWidth();
        this.height = enemyBitmap.getHeight();
        enemyBitmap = Bitmap.createScaledBitmap(enemyBitmap,width,height,false);

    }

    @Override
    public void drawObject(Canvas canvas) {
        canvas.drawBitmap(enemyBitmap,getObjectX(),getObjectY(),null);
    }

    @Override
    public void updateLocation() {

        y += speed/2;

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


    @Override
    public int getObjectX() {
        return x;
    }

    public int getHealth() { return health;}

    public void takeDamage(int dmg) {
        this.health -= dmg;
        this.y-=90; // bounce
    }

    public void die() {
        /*enemyBitmap = BitmapFactory.decodeResource(resources,R.drawable.explosion_01);
        enemyBitmap = BitmapFactory.decodeResource(resources,R.drawable.explosion_02);
        enemyBitmap = BitmapFactory.decodeResource(resources,R.drawable.explosion_03);
        enemyBitmap = BitmapFactory.decodeResource(resources,R.drawable.explosion_04);
        enemyBitmap = BitmapFactory.decodeResource(resources,R.drawable.explosion_05);*/
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

    public int getEnemyHeight() {return height;}

    public int getEnemyWidth() {return width;}

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

    public Rect getCollisionShape() {
        return new Rect(x,y,x+enemyBitmap.getWidth(),y+enemyBitmap.getHeight());
    }
}
