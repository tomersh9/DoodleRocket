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
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import com.example.doodlerocket.Activities.GameOverActivity;
import com.example.doodlerocket.GameObjects.Bullet;
import com.example.doodlerocket.GameObjects.Enemy;
import com.example.doodlerocket.GameObjects.EnemyProjectile;
import com.example.doodlerocket.GameObjects.GoldCoin;
import com.example.doodlerocket.GameObjects.Meteor;
import com.example.doodlerocket.GameObjects.Player;
import com.example.doodlerocket.GameObjects.SilverCoin;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameView extends View {

    //high score data
    SharedPreferences sp;
    int money;

    //player
    Player player;
    private int health;
    private int score;
    private boolean isMoving = false;

    //player bullets
    private boolean isBullet = true;
    private List<Bullet> bullets = new ArrayList<>();
    private List<Bullet> removeBulletsList = new ArrayList<>();

    //enemy projectiles
    private boolean isProjectile = true;
    private List<EnemyProjectile> projectiles = new ArrayList<>();
    private List<EnemyProjectile> removeProjectilesList = new ArrayList<>();

    //enemies
    private boolean isTimeToEnemy = true;
    private List<Enemy> enemies = new ArrayList<>();
    private List<Enemy> removeEnemiesList = new ArrayList<>();

    //meteors
    private boolean isTimeToSpawnMeteor = true;
    private List<Meteor> meteors = new ArrayList<>();
    private List<Meteor> removeMeteorsList = new ArrayList<>();

    //items
    private boolean isTimeToCoin = true;

    private List<GoldCoin> goldCoins = new ArrayList<>();
    private List<GoldCoin> removeGoldCoins = new ArrayList<>();

    private List<SilverCoin> silverCoins = new ArrayList<>();
    private List<SilverCoin> removeSilverCoins = new ArrayList<>();

    //canvas properties
    private int canvasW, canvasH;

    //background full screen
    private Bitmap background;
    private Rect rect;
    private boolean isScale = true;
    private int dWidth, dHeight;

    //UI
    private Paint scorePaint = new Paint();
    private Bitmap life[] = new Bitmap[2]; //hearts



    public GameView(Context context, int skinID, int backgroundID) { //context when calling it
        super(context);

        //reading total player money
        sp = getContext().getSharedPreferences("storage",Context.MODE_PRIVATE);
        money = sp.getInt("money",0);

        //player
        player = new Player(1440,getResources(),skinID);

        //setting up background to fit screen
        background = BitmapFactory.decodeResource(getResources(), backgroundID);

        if(backgroundID == R.drawable.moon_bg_800) {
            isScale = false; //stay original size
        }
        else {
            isScale = true;
            Display display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            dWidth = size.x;
            dHeight = size.y;
            rect = new Rect(0,0,dWidth,dHeight);
        }

        //setting score paint properties
        //custom font
        Typeface customTypeface = ResourcesCompat.getFont(context, R.font.retro);
        scorePaint.setColor(Color.YELLOW);
        scorePaint.setTextSize(80);
        scorePaint.setTypeface(customTypeface);
        scorePaint.setAntiAlias(true);

        //life display of fish (have or not)
        life[0] = BitmapFactory.decodeResource(getResources(),R.drawable.heart_red_48);
        life[1] = BitmapFactory.decodeResource(getResources(),R.drawable.heart_gray_48);

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

        //local vars of player constantly updating
        int playerX = player.getObjectX();
        int playerY = player.getObjectY();
        Bitmap playerBitmap = player.getPlayerBitmap();

        //UI drawing
        if(isScale) { //fit screen
            canvas.drawBitmap(background,null,rect,null);
        }
        else { //don't fit screen
            canvas.drawBitmap(background,0,0,null);
        }

        canvas.drawText("Score:" + score, 60,150,scorePaint);

        //move outside
        if(health == 0) {

            //deliver high score data
            SharedPreferences.Editor editor = sp.edit();
            int total = money + score;

            if(score>sp.getInt("highscore",0)) {
                editor.putInt("highscore",score);
            }
            else {
                editor.putInt("money",total);
            }
            editor.commit();

            gameOver();
        }

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

        //shoot bullets
        shoot();

        //spawn meteors
        spawnMeteors();

        //spawn enemies with projectiles
        spawnEnemies();

        //enemy shoots
        spawnProjectiles();

        //spawn coins
        spawnCoins();

        //consumables
        for(GoldCoin goldCoin : goldCoins) {
            goldCoin.drawObject(canvas);
            goldCoin.updateLocation();

            if(goldCoin.collisionDetection(playerX,playerY,playerBitmap)) {
                score += 50;
                removeGoldCoins.add(goldCoin);
            }
            else if(goldCoin.getObjectY() > canvasH) {
                removeGoldCoins.add(goldCoin);
            }
            else {
                goldCoin.drawObject(canvas);
            }
        }
        goldCoins.removeAll(removeGoldCoins);

        for(SilverCoin silverCoin : silverCoins) {
            silverCoin.drawObject(canvas);
            silverCoin.updateLocation();

            if(silverCoin.collisionDetection(playerX,playerY,playerBitmap)) {
                score += 10;
                removeSilverCoins.add(silverCoin);
            }
            else if(silverCoin.getObjectY() > canvasH) {
                removeSilverCoins.add(silverCoin);
            }
            else {
                silverCoin.drawObject(canvas);
            }
        }
        silverCoins.removeAll(removeSilverCoins);

        for(Meteor meteor : meteors) {

            meteor.updateLocation();

            //meteor hits player
            if (meteor.collisionDetection(playerX, playerY, playerBitmap)) {
                health--;
                removeMeteorsList.add(meteor);
            }
            //meteor goes off screen
            else if (meteor.getObjectY() > canvasH) {
                removeMeteorsList.add(meteor);
            }
            else {
                meteor.drawObject(canvas);
            }
        }
        meteors.removeAll(removeMeteorsList);

        for(Enemy enemy : enemies) {

            enemy.updateLocation();

            if(enemy.collisionDetection(playerX,playerY,playerBitmap)) {
                health--;
                removeEnemiesList.add(enemy);
            }
            else if(enemy.getObjectY() > canvasH) {
                removeEnemiesList.add(enemy);
            }
            else {
                enemy.drawObject(canvas);
            }
        }
        enemies.removeAll(removeEnemiesList);

        //handle bullets and collisions
        for (Bullet bullet : bullets) {

            bullet.updateLocation();

            if (bullet.getObjectY() < 0) {

                removeBulletsList.add(bullet);
            }
            else {
                bullet.drawObject(canvas);
            }

            //check for collision bullet-meteor
            for (Meteor meteor : meteors) {
                if(Rect.intersects(bullet.getCollisionShape(),meteor.getCollisionShape())) {
                    removeBulletsList.add(bullet);
                    removeMeteorsList.add(meteor);
                }
            }

            //collision bullet - enemy
            for(Enemy enemy : enemies) {

                if(Rect.intersects(bullet.getCollisionShape(),enemy.getCollisionShape())) {

                    enemy.takeDamage(1); //dmg taken
                    if(enemy.getHealth() == 0 ) {
                        //enemy.die(); //animate death effect here
                        removeEnemiesList.add(enemy);
                    }
                    removeBulletsList.add(bullet);
                }
            }
            enemies.removeAll(removeEnemiesList);
        }
        //remove all previous bullets from list
        bullets.removeAll(removeBulletsList);

        for(EnemyProjectile projectile : projectiles) {
            projectile.updateLocation();

            if(projectile.getObjectY() > canvasH) {
                removeProjectilesList.add(projectile);
            }
            else if(projectile.collisionDetection(playerX,playerY,playerBitmap)) {
                health--;
                removeProjectilesList.add(projectile);
            }
            else {
                projectile.drawObject(canvas);
            }
        }
        projectiles.removeAll(removeProjectilesList);

    }

    private void spawnEnemies() {

        if(isTimeToEnemy) {

            Enemy enemy = new Enemy(getResources(),(int) Math.floor(Math.random() * (player.getMaxX())),50,canvasW);
            System.out.println(enemy.getHealth());
            enemies.add(enemy);
            delayEnemy();
        }
    }

    private void spawnProjectiles() {

        if(isProjectile) {
            for(Enemy enemy : enemies) {
                projectiles.add(new EnemyProjectile(getResources(), enemy));
                delayEnemyShots();
            }
        }
    }

    private void spawnMeteors() {
        if(isTimeToSpawnMeteor) {
            meteors.add(new Meteor(player.getMinX(),player.getMaxX(),20,getResources()));
            delayMeteor(); //wait 2 secs between spawns
        }
    }

    private void spawnCoins() {
        if(isTimeToCoin) {
            goldCoins.add(new GoldCoin(player.getMinX(),player.getMaxX(),getResources()));
            delayCoin();
        }
    }

    //if can fire, add new bullet to list: draw and update its location in the for loop
    private void shoot() {
        if(isBullet) {
            bullets.add(new Bullet(getResources(), player.getObjectX(), player.getObjectY()));
            delayBullets(); //isFire = false cause delay between creating new bullets and drawing them
        }
    }

    private void delayEnemyShots() {
        isProjectile = false;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isProjectile = true;
            }
        },650);
    }


    private void delayEnemy() {
        isTimeToEnemy = false;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isTimeToEnemy = true;
            }
        },2000);
    }

    //delay between shots
    private void delayBullets() {
        isBullet = false;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isBullet = true;
            }
        },200);
    }

    //delay between meteor spawns
    private void delayMeteor() {
        isTimeToSpawnMeteor = false;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isTimeToSpawnMeteor = true;
            }
        },250);
    }

    //delay between meteor spawns
    private void delayCoin() {
        isTimeToCoin = false;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isTimeToCoin = true;
            }
        },2500);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_MOVE:

                //Drag&Drop

                if(isMoving) {
                    player.setX((int)event.getX());
                    player.setY((int)event.getY());
                }
                else if(   event.getX() > player.getObjectX()
                        && event.getX() < player.getObjectX() + player.getPlayerBitmap().getWidth()
                        && event.getY() > player.getObjectY()
                        && event.getY() < player.getObjectY() + player.getPlayerBitmap().getHeight()) {
                    isMoving = true;

                }
                break;

            case MotionEvent.ACTION_UP:
                isMoving = false;
                break;

        }
        return true;
    }

    public void gameOver() {

        //PROBLEM HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //move to game over intent
        Intent gameOverIntent = new Intent(getContext(), GameOverActivity.class);
        gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        gameOverIntent.putExtra("score", score);
        getContext().startActivity(gameOverIntent);
    }

}
