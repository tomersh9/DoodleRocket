package com.example.doodlerocket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import com.example.doodlerocket.Activities.GameOverActivity;
import com.example.doodlerocket.GameObjects.Bullet;
import com.example.doodlerocket.GameObjects.Enemy;
import com.example.doodlerocket.GameObjects.GoldCoin;
import com.example.doodlerocket.GameObjects.Meteor;
import com.example.doodlerocket.GameObjects.Player;
import com.example.doodlerocket.GameObjects.SilverCoin;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import static android.content.Context.SENSOR_SERVICE;

public class GameView extends View implements SensorEventListener {

    //high score data
    SharedPreferences sp;

    //accelerometer sensor
    SensorManager manager;
    Sensor sensor;
    private float velocity;

    //player
    Player player;
    private int health;
    private int rocketSpeed;
    private int score;
    private boolean isMoving = false;
    private boolean isFire = false;
    private Bitmap life[] = new Bitmap[2]; //hearts

    //projectiles
    private Bullet bullet;

    //enemies
    Enemy enemy;

    //meteors
    private List<Meteor> meteors = new ArrayList<>();

    //items
    private GoldCoin goldCoin;
    private SilverCoin silverCoin;

    //canvas properties
    private int canvasW, canvasH;

    //background full screen
    private Bitmap background;
    private Rect rect;
    int dWidth, dHeight;

    //paint
    private Paint scorePaint = new Paint();

    public GameView(Context context) { //context when calling it
        super(context);

        //high-score
        sp = getContext().getSharedPreferences("highscore",Context.MODE_PRIVATE);

        //setting up accelerometer sensor
        manager = (SensorManager)context.getSystemService(SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(sensor!=null){
            this.manager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_GAME);
        }

        player = new Player(1440,getResources());

        //projectiles
        bullet = new Bullet(getResources(),player.getObjectX(),player.getObjectY());

        //setting up background to fit screen
        background = BitmapFactory.decodeResource(getResources(), R.drawable.stars_pxl_png);
        Display display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        rect = new Rect(0,0,dWidth,dHeight);

        //enemies
        enemy = new Enemy(getResources(),player.getObjectX(),300,1440);

        //meteors
        Meteor meteor1 = new Meteor(0,1160,18,getResources());
        Meteor meteor2 = new Meteor(0,1160,9,getResources());
        Meteor meteor3 = new Meteor(0,1160,21,getResources());
        Meteor meteor4 = new Meteor(0,1160,15,getResources());

        meteors.add(meteor1);
        meteors.add(meteor2);
        meteors.add(meteor3);
        meteors.add(meteor4);

        //items
        goldCoin = new GoldCoin(0,1160,getResources());
        silverCoin = new SilverCoin(0,1160,getResources());

        //setting score paint properties
        //custom font
        Typeface customTypeface = ResourcesCompat.getFont(context, R.font.retro);
        scorePaint.setColor(Color.YELLOW);
        scorePaint.setTextSize(80);
        scorePaint.setTypeface(customTypeface);
        scorePaint.setAntiAlias(true);

        //life display of fish (have or not)
        life[0] = BitmapFactory.decodeResource(getResources(),R.drawable.heart_red_50);
        life[1] = BitmapFactory.decodeResource(getResources(),R.drawable.heart_gray_50);

        //initialize score & health
        score = 0;
        health = player.getHealth(); // 3 life points
    }

    @Override
    protected void onDraw(Canvas canvas) { //order matters!!
        super.onDraw(canvas);

        //getting canvas measures
        canvasW = canvas.getWidth(); //1440
        canvasH = canvas.getHeight(); //2352

        //local vars of player
        int playerX = player.getObjectX();
        int playerY = player.getObjectY();
        Bitmap playerBitmap = player.getPlayerBitmap();

        //UI drawing
        canvas.drawBitmap(background,null,rect,null);
        canvas.drawText("Score:" + score, 60,150,scorePaint);

        //update hearts bitmaps
        for(int i=0; i<3; i++) {

            //20x - first heart position
            int x = (int) (20 + life[0].getWidth() * i);
            int y = 200; //margin top

            if(i<health) { //full heart
                canvas.drawBitmap(life[0],x,y,null);
            }
            else { //grey heart
                canvas.drawBitmap(life[1],x,y,null);
            }
        }

        //draw player
        player.drawObject(canvas);
        player.updateLocation();

        //spawn items
        spawnCoins(canvas);

        //on touch event movement
        if(isMoving) {
            isMoving = false;
        }

        enemy.drawObject(canvas);
        enemy.updateLocation();

        //spawn meteors
        for(Meteor meteor : meteors) {

            meteor.drawObject(canvas);
            meteor.updateLocation();

            //meteor hits player
            if(meteor.collisionDetection(playerX,playerY,playerBitmap)) {

                health--;
                meteor.setMeteorY(canvasH + 50);
                meteor.setBitmap(getResources(), (int) (Math.random()* (9)));

                if(health == 0) {
                    gameOver();
                }
            }

            //meteor goes off screen
            if(meteor.getObjectY() > canvasH) {
                meteor.setMeteorY(0);
                meteor.setMeteorX((int) Math.floor(Math.random() * ((player.getMaxX() - player.getMinX()) + player.getMinX())));
                meteor.setBitmap(getResources(), (int) (Math.random()* (9)));
            }

            //bullet hits meteor
            if(isFire) {

                bullet.drawObject(canvas);
                bullet.updateLocation();

                if(bullet.collisionDetection(meteor.getObjectX(),meteor.getObjectY(),meteor.getMeteorBitmap())) {

                    //delete meteor
                    meteor.setMeteorY(0);
                    meteor.setMeteorX((int) Math.floor(Math.random() * ((player.getMaxX() - player.getMinX()) + player.getMinX())));
                    meteor.setBitmap(getResources(), (int) (Math.random()* (9)));

                    //relocate bullet
                    bullet.setX(playerX + 50);
                    bullet.setY(playerY);
                    isFire = false;
                }

                if(bullet.collisionDetection(enemy.getObjectX(),enemy.getObjectY(),enemy.getEnemyBitmap())) {

                    //delete meteor
                    enemy.setY(300);
                    enemy.setX((int) Math.floor(Math.random() * ((player.getMaxX() - player.getMinX()) + player.getMinX())));
                    enemy.setEnemyBitmap(getResources(), (int) (Math.random()* (8)));

                    //relocate bullet
                    bullet.setX(playerX + 50);
                    bullet.setY(playerY);
                    isFire = false;
                }

                if (bullet.getObjectY() < 0) {

                    bullet.setX(playerX + 50);
                    bullet.setY(playerY);
                    isFire = false;
                }
            }
        }

    }

    public void spawnCoins(Canvas canvas) {

        goldCoin.drawObject(canvas);
        goldCoin.updateLocation();

        silverCoin.drawObject(canvas);
        silverCoin.updateLocation();

        if(silverCoin.collisionDetection(player.getObjectX(),player.getObjectY(),player.getPlayerBitmap())) {
            score += 10;
            silverCoin.setCoinY(0);
            silverCoin.setCoinX((int) Math.floor(Math.random() * ((player.getMaxX() - player.getMinX()) + player.getMinX())));
        }

        if(silverCoin.getObjectY() > canvasH) {
            silverCoin.setCoinY(0);
            silverCoin.setCoinX((int) Math.floor(Math.random() * ((player.getMaxX() - player.getMinX()) + player.getMinX())));
        }

        if(goldCoin.collisionDetection(player.getObjectX(),player.getObjectY(),player.getPlayerBitmap())) {
            score += 50;
            goldCoin.setCoinY(0); //top screen
            goldCoin.setCoinX((int) Math.floor(Math.random() * ((player.getMaxX() - player.getMinX()) + player.getMinX())));
        }

        if(goldCoin.getObjectY() > canvasH) {
            goldCoin.setCoinY(0); //top screen
            goldCoin.setCoinX((int) Math.floor(Math.random() * ((player.getMaxX() - player.getMinX()) + player.getMinX())));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_MOVE:
                player.setX((int) event.getX());
                isMoving = true;
                break;

            case MotionEvent.ACTION_DOWN:
                if(event.getY() < canvasH/2) { //top half of screen to fire
                    isFire = true;
                    break;
                }
        }
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        /*velocity=event.values[0];
        System.out.println(velocity);
        rocketSpeed-=velocity/6;
        if(rocketSpeed>20) rocketSpeed=20;
        if(rocketSpeed<-20) rocketSpeed=-20;*/
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //nothing
    }

    public void gameOver() {

        //deliver high score data
        SharedPreferences.Editor editor = sp.edit();
        if(score>sp.getInt("highscore",0)) {
            editor.putInt("highscore",score);
        }
        editor.commit();

        //move to game over intent
        Intent gameOverIntent = new Intent(getContext(), GameOverActivity.class);
        gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        gameOverIntent.putExtra("score", score);
        getContext().startActivity(gameOverIntent);
    }

}
