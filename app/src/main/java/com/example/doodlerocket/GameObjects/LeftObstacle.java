package com.example.doodlerocket.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.doodlerocket.R;

import static android.graphics.Bitmap.createScaledBitmap;

public class LeftObstacle {

    private int obstacleX, obstacleY, obstacleWidth,obstacleHeight,speed;
    private Bitmap obstaclePaint , scaledRocket;

    public LeftObstacle(int minRocketX, int maxRocketX, Resources res) {

        this.obstacleX = (int) (Math.floor(Math.random() * (maxRocketX - minRocketX) + minRocketX));
        this.obstacleY = 0; //spawns at top of screen
        this.speed = 10; //platforms speed going down
        this.obstacleWidth = 500;
        this.obstacleHeight = 100;
        this.obstaclePaint = BitmapFactory.decodeResource(res, R.drawable.meteor5);
        scaledRocket = createScaledBitmap(obstaclePaint,obstacleWidth,obstacleHeight,false);

        //obstaclePaint.setColor(Color.BLACK);
        //obstaclePaint.setAntiAlias(false);
    }

    public void drawObstacle(Canvas canvas) {
        canvas.drawBitmap(scaledRocket,getObstacleX(),getObstacleY(),null);
       // canvas.drawRect(getObstacleX(),getObstacleY(), getObstacleX() + obstacleWidth,getObstacleY() + obstacleHeight,obstaclePaint);
    }

    //moving vertically in certain speed
    public void updateLocation() {
        this.obstacleY += speed;
    }

    //collision detection
    public boolean obstacleHitChecker(int rocketX, int rocketY, Bitmap rocketBitmap) {

        if     (rocketX < getObstacleX() + obstacleWidth
                && obstacleX < (rocketX + rocketBitmap.getWidth()) //rocket between obstacle width
                && rocketY < getObstacleY() + obstacleHeight
                && getObstacleY() < (rocketY + rocketBitmap.getHeight())) { //rocket between obstacle height
            return true;
        }
        return false;
    }

    public int getObstacleX() {
        return obstacleX;
    }

    public void setObstacleX(int obstacleX) {
        this.obstacleX = obstacleX;
    }

    public int getObstacleY() {
        return obstacleY;
    }

    public void setObstacleY(int obstacleY) {
        this.obstacleY = obstacleY;
    }
}
