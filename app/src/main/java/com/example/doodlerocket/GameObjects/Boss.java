package com.example.doodlerocket.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.doodlerocket.R;

import java.util.ArrayList;
import java.util.List;

public class Boss extends Mob {

    //properties
    private int x,y,speed;
    private int width, height;
    private int canvasW;
    private boolean movingLeft = true;
    private Bitmap bossBitmap;

    //creating death effect
    private List<Bitmap> deathEffectList = new ArrayList<>();
    private boolean enteringScreen = true;
    private int i = 0;


    public Boss(int x, int y, int speed, int health, int canvasW, int enemySkinID, Resources resources) {
        super(x, y, speed,health);

        //from father
        this.x = getObjectX();
        this.y = getObjectY();
        this.speed = getSpeed();
        //this.health = getHealth();

        //unique
        this.canvasW = canvasW;

        //assign enemy bitmap picture
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

        if(!this.isDead()) {
            canvas.drawBitmap(bossBitmap,getObjectX(),getObjectY(),null);
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
            moveVertical(speed/5);
            if(getObjectY()> 150) {
                enteringScreen = false;
            }
        }

        //after entering start moving
        if(!this.isDead()) {
            //moving horizontally
            if(movingLeft) {
                moveHorizontal(x -= speed);
                if(getObjectX() + bossBitmap.getWidth() < canvasW/3) {
                    movingLeft = false;
                }
            }
            else {
                moveHorizontal(x += speed);
                if(getObjectX() + bossBitmap.getWidth() > canvasW - 100) {
                    movingLeft = true;
                }
            }
        }

    }

    public int getBossWidth() {
        return width;
    }

    public int getBossHeight() {
        return height;
    }

    @Override
    public int getObjectX() {
        return super.getObjectX();
    }

    @Override
    public int getObjectY() {
        return super.getObjectY();
    }

    @Override
    public void takeDamage(int dmg) {
        super.takeDamage(dmg);
    }

    @Override
    public int getHealth() {
        return super.getHealth();
    }

    public Bitmap getBossBitmap() {
        return bossBitmap;
    }

    @Override
    public boolean isDead() {
        return super.isDead();
    }

    @Override
    public void setDead(boolean dead) {
        super.setDead(dead);
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
