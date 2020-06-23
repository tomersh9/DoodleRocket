package com.example.doodlerocket.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.doodlerocket.R;

import java.util.ArrayList;
import java.util.List;

public class Boss implements IGameObjects {

    //properties
    private int x,y,speed, health;
    private int width, height;
    private int canvasW;
    private boolean movingLeft = true;
    private Bitmap bossBitmap;
    private boolean enteringScreen = true;
    private boolean isDead = false;

    //animation list
    private List<Bitmap> idleList;
    private int j = 0;

    //creating death effect
    private List<Bitmap> deathEffectList = new ArrayList<>();
    private int i = 0;


    public Boss(int x, int y, int speed, int health, int enemySkinID, int canvasW, List<Bitmap> idleList, Resources resources) {

        //properties
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.health = health;
        this.canvasW = canvasW;
        this.idleList = idleList; //each boss has its own idle animation

        //assign boss bitmap picture
        bossBitmap = BitmapFactory.decodeResource(resources,enemySkinID);
        this.width = bossBitmap.getWidth();
        this.height = bossBitmap.getHeight();

        //death effect - unique to Boss
        deathEffectList.add(BitmapFactory.decodeResource(resources, R.drawable.boom1));
        deathEffectList.add(BitmapFactory.decodeResource(resources,R.drawable.boom2));
        deathEffectList.add(BitmapFactory.decodeResource(resources,R.drawable.boom3));
        deathEffectList.add(BitmapFactory.decodeResource(resources,R.drawable.boom4));
        deathEffectList.add(BitmapFactory.decodeResource(resources,R.drawable.boom5));

    }

    @Override
    public void drawObject(Canvas canvas) {

        if(!isDead) {
            canvas.drawBitmap(idleList.get(j/5),getObjectX(),getObjectY(),null);
            j++;
            j=j%25;
        }
        else {
            canvas.drawBitmap(deathEffectList.get(i/5),(getObjectX()+ bossBitmap.getWidth()/2),(getObjectY()+ bossBitmap.getHeight()/2),null);
            i++;
            i=i%25;
        }
    }

    @Override
    public void updateLocation() {

        //enter screen from top and only then move horizontally
        while(enteringScreen) {
            y += speed/3;
            if(y > 380) {
                enteringScreen = false;
            }
        }

        //after entering start moving
        if(!isDead) {
            //moving horizontally
            if(movingLeft) {
                x -= speed;
                if(x + bossBitmap.getWidth() < canvasW/3) {
                    movingLeft = false;
                }
            }
            else {
                x += speed;
                if(x + bossBitmap.getWidth() > canvasW + 300) {
                    movingLeft = true;
                }
            }
        }

    }

    public boolean isEnteringScreen() {
        return enteringScreen;
    }

    public void setEnteringScreen(boolean enteringScreen) {
        this.enteringScreen = enteringScreen;
    }

    public Bitmap getBossBitmap() {
        return this.bossBitmap;
    }

    public void takeDamage(int dmg) {
        this.health -= dmg;
    }

    public int getBossWidth() {
        return width;
    }

    public int getBossHeight() {
        return height;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    @Override
    public int getObjectX() {
        return this.x;
    }

    @Override
    public int getObjectY() {
        return this.y;
    }

    @Override
    public boolean collisionDetection(int playerX, int playerY, Bitmap playerBitmap) {

        if     (playerX < getObjectX() + (bossBitmap.getWidth())
                && getObjectX() < (playerX + playerBitmap.getWidth()) //rocket between obstacle width
                && playerY < getObjectY() + (bossBitmap.getHeight())
                && getObjectY() < (playerY + playerBitmap.getHeight())) { //rocket between obstacle height
            return true;
        }
        return false;
    }

    public Rect getCollisionShape() {
        return new Rect(getObjectX(),getObjectY(),getObjectX()+bossBitmap.getWidth(),getObjectY()+bossBitmap.getHeight());
    }
}
