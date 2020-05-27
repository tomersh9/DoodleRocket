package com.example.doodlerocket.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import static android.graphics.Bitmap.createScaledBitmap;

public class GapObstacle  {

    //one style for all obstacles
    private Paint obstaclePaint;
    private Paint paint;

    //left platform, right platform, scaled size platform
    private Bitmap obstacleLeftBitmap, obstacleRightBitmap, scaledObstacleLeftBitmap , scaledObstacleRightBitmap;

    //all the obstacles properties (size, location, speed etc..)
    private float playerGap;
    private float obstacle1StartX, obstacle2StartX, obstacle1EndX, obstacle2EndX, obstacleY;
    private float obstacleWidth, obstacleHeight;
    private float speed;

    public GapObstacle(float playerGap , int canvasWidth) {

        this.playerGap = playerGap;
        this.speed = 13;
        this.obstacle1StartX = 0;
        this.obstacle1EndX = 500;
        this.obstacle2StartX = canvasWidth - playerGap;
        this.obstacle2EndX = obstacle2StartX + 500;
        this.obstacleY = 0; //starts at top screen
        this.obstacleHeight = 100;

        this.obstaclePaint = new Paint();
        obstaclePaint.setColor(Color.GREEN);
        obstaclePaint.setAntiAlias(false);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(false);

       // this.obstacleLeftBitmap = BitmapFactory.decodeResource(res,R.drawable.platform328);
        //this.obstacleRightBitmap = BitmapFactory.decodeResource(res,R.drawable.platform328);
       // scaledObstacleLeftBitmap = createScaledBitmap(obstacleLeftBitmap,(int)obstacleWidth,(int) obstacleHeight,false);
        //scaledObstacleRightBitmap = createScaledBitmap(obstacleRightBitmap,(int)obstacleWidth,(int) obstacleHeight,false);
    }

    //draw 2 platforms each time (left & right)
    public void drawObstacle(Canvas canvas) {

        canvas.drawRect(getObstacle1StartX(),getObstacleY(), getObstacle1EndX(),getObstacleY() + obstacleHeight ,obstaclePaint);
        canvas.drawRect(getObstacle1EndX() + (playerGap*2),getObstacleY(), getObstacle1EndX() + (playerGap*2) + 500,getObstacleY() + obstacleHeight,paint);
        //canvas.drawBitmap(scaledObstacleLeftBitmap,getObstacleLeftX(),getObstacleY(),null);
        //canvas.drawBitmap(scaledObstacleRightBitmap,getObstacleRightX(),getObstacleY(),null);
    }

    //move platforms vertically
    public void updateLocation() {
        this.obstacleY +=speed;
    }

    public boolean playerCollisionDetection(Bitmap playerBitmap, int playerX, int playerY) {

        if     (playerX < getObstacle1StartX() + getObstacle1EndX()
                && getObstacle1StartX() < (playerX + playerBitmap.getWidth()) //rocket between obstacle width
                && playerY < getObstacleY() + obstacleHeight
                && getObstacleY() < (playerY + playerBitmap.getHeight())
                || playerX < getObstacle2StartX() + getObstacle2EndX()
                && getObstacle2StartX() < (playerX + playerBitmap.getWidth()) //rocket between obstacle width
                && playerY < getObstacleY() + obstacleHeight
                && getObstacleY() < (playerY + playerBitmap.getHeight())) { //rocket between obstacle height
            return true;
        }
        return false;
    }

    public float getPlayerGap() {
        return playerGap;
    }

    public void setPlayerGap(float playerGap) {
        this.playerGap = playerGap;
    }

    public float getObstacle1EndX() {
        return obstacle1EndX;
    }

    public void setObstacle1EndX(float obstacle1EndX) {
        this.obstacle1EndX = obstacle1EndX;
    }

    public float getObstacle2EndX() {
        return obstacle2EndX;
    }

    public void setObstacle2EndX(float obstacle2EndX) {
        this.obstacle2EndX = obstacle2EndX;
    }

    public float getObstacle1StartX() {
        return obstacle1StartX;
    }

    public void setObstacle1StartX(float obstacle1StartX) {
        this.obstacle1StartX = obstacle1StartX;
    }

    public float getObstacle2StartX() {
        return obstacle2StartX;
    }

    public void setObstacle2StartX(float obstacle2StartX) {
        this.obstacle2StartX = obstacle2StartX;
    }

    public float getObstacleY() {
        return obstacleY;
    }

    public void setObstacleY(float obstacleY) {
        this.obstacleY = obstacleY;
    }
}
