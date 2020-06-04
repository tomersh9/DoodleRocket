package com.example.doodlerocket;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
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
import com.example.doodlerocket.GameObjects.EnemyFactory;
import com.example.doodlerocket.GameObjects.EnemyProjectile;
import com.example.doodlerocket.GameObjects.GoldCoin;
import com.example.doodlerocket.GameObjects.Meteor;
import com.example.doodlerocket.GameObjects.MeteorFactory;
import com.example.doodlerocket.GameObjects.Player;
import com.example.doodlerocket.GameObjects.SilverCoin;
import com.example.doodlerocket.GameObjects.SoundManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameView extends View {

    //high score data
    SharedPreferences sp;
    private int money;
    private int currLvl;

    //animations
    ValueAnimator valueAnimator;

    //SFX manager
    private SoundManager soundManager;

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

    //enemy factory
    private EnemyFactory enemyFactory;

    //meteors
    private boolean isTimeToSpawnMeteor = true;
    private List<Meteor> meteors = new ArrayList<>();
    private List<Meteor> removeMeteorsList = new ArrayList<>();

    //meteor factory DP
    private MeteorFactory meteorFactory;

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
    private int backgroundID;

    //UI
    private Paint scorePaint = new Paint();
    private Bitmap life[] = new Bitmap[2]; //hearts
    private Bitmap pauseBtn;

    //interface to communicate with the MainActivity "father" outside
    public interface GameViewListener {
        void pauseGame();
        void resumeGame();
        void endGame(int score);
    }

    //listener object
    public GameViewListener gameViewListener;

    //will be called outside this class to use this interface methods
    public void setGameViewListener(GameViewListener gameViewListener) {this.gameViewListener = gameViewListener;}

    public GameView(Context context, int skinID, int backgroundID, int currLvl, SoundManager soundManager) { //context when calling it
        super(context);


        //reading total player money
        sp = getContext().getSharedPreferences("storage",Context.MODE_PRIVATE);
        money = sp.getInt("money",0);

        //initialize sound manager
        this.soundManager = soundManager;

        //set player
        player = new Player(1440,2352,getResources(),skinID);

        //current lvl (to delay objects)
        this.currLvl = currLvl;

        //initialize background fit to screen
        this.backgroundID = backgroundID;
        setGameBackground();

        //factories
        meteorFactory = new MeteorFactory(player.getMinX(),player.getMaxX(),20,getResources());
        enemyFactory = new EnemyFactory(getResources(),player.getMinX(),player.getMaxX(),5,3,1440);

        //hearts, score and life
        setUI();
    }


    private void setGameBackground() {
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
    }

    private void setUI() {
        //setting score paint properties
        //custom font
        Typeface customTypeface = ResourcesCompat.getFont(getContext(), R.font.retro);
        scorePaint.setColor(Color.YELLOW);
        scorePaint.setTextSize(80);
        scorePaint.setTypeface(customTypeface);
        scorePaint.setAntiAlias(true);

        //life display of fish (have or not)
        life[0] = BitmapFactory.decodeResource(getResources(),R.drawable.heart_red_48);
        life[1] = BitmapFactory.decodeResource(getResources(),R.drawable.heart_gray_48);

        //pause button
        pauseBtn = BitmapFactory.decodeResource(getResources(),R.drawable.pause_icon_50);

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

        //score text
        canvas.drawText("Score:" + score, 60,150,scorePaint);

        //draw pause button
        canvas.drawBitmap(pauseBtn,canvasW - 250,100,null);

        //saving data before death
        if(health == 0) {

            soundManager.startPlayerDeathSfx();
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

            goldCoin.updateLocation();

            if(goldCoin.collisionDetection(playerX,playerY,playerBitmap)) {
                soundManager.startGoldCoinSfx();
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

            silverCoin.updateLocation();

            if(silverCoin.collisionDetection(playerX,playerY,playerBitmap)) {
                soundManager.startSilverCoinSfx();
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

            //moving on screen
            meteor.updateLocation();

            //meteor hits player
            if (meteor.collisionDetection(playerX, playerY, playerBitmap)) {
                soundManager.startMeteorDeathSfx();
                soundManager.startPlayerHitSfx();
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

                soundManager.startPlayerHitSfx();
                soundManager.startEnemyDeathSfx();
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
        for (final Bullet bullet : bullets) {

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

                    soundManager.startMeteorDeathSfx();
                    bullet.setBoom(true);

                    //hit animation and then remove it
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            removeBulletsList.add(bullet);
                        }
                    },500);
                    removeMeteorsList.add(meteor);
                }
            }

            //collision bullet - enemy
            for(final Enemy enemy : enemies) {

                if(Rect.intersects(bullet.getCollisionShape(),enemy.getCollisionShape())) {
                    //flash enemy
                    soundManager.startEnemyHitSfx();
                    enemy.takeDamage(1); //dmg taken

                    //enemy death
                    if(enemy.getHealth() == 0 ) {

                        //death animation
                        soundManager.startEnemyDeathSfx();
                        enemy.setDead(true);
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                removeEnemiesList.add(enemy);
                            }
                        },500);
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
                soundManager.startPlayerHitSfx();
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
            enemies.add(enemyFactory.generateEnemy(currLvl));
            delayEnemy();
        }
    }

    private void spawnProjectiles() {
        //for each enemy spawns a projectile to shoot
        //don't shoot when dead
        if(isProjectile) {
            for(Enemy enemy : enemies) {
                if(!enemy.isDead()) {
                    soundManager.startEnemyLaserSfx();
                    projectiles.add(new EnemyProjectile(getResources(), enemy));
                    delayEnemyShots();
                }
            }
        }
    }

    private void spawnMeteors() {
        if(isTimeToSpawnMeteor) {
            meteors.add(meteorFactory.generateMeteor(backgroundID));
            delayMeteor();
        }
    }

    private void spawnCoins() {
        if(isTimeToCoin) {
            goldCoins.add(new GoldCoin(player.getMinX(),player.getMaxX(),getResources()));
            silverCoins.add(new SilverCoin(player.getMinX(),player.getMaxX(),getResources()));
            delayCoin();
        }
    }

    //if can fire, add new bullet to list: draw and update its location in the for loop
    private void shoot() {
        if(isBullet) {
            soundManager.startPlayerLaserSfx();
            bullets.add(new Bullet(getContext(),getResources(), player.getObjectX(), player.getObjectY()));
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
        },2000 - (currLvl*100));
    }


    private void delayEnemy() {
        isTimeToEnemy = false;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isTimeToEnemy = true;
            }
        },7000 - (currLvl*600));
    }

    //delay between shots
    private void delayBullets() {
        isBullet = false;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isBullet = true;
            }
        },400 - (currLvl*20));
    }

    //delay between meteor spawns
    private void delayMeteor() {
        isTimeToSpawnMeteor = false;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isTimeToSpawnMeteor = true;
            }
        },2200 - (currLvl*200));
    }

    //delay between meteor spawns
    private void delayCoin() {
        isTimeToCoin = false;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isTimeToCoin = true;
            }
        },5000 - (currLvl*50));
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            //Drag&Drop
            case MotionEvent.ACTION_MOVE:

                if(isMoving) {
                    player.setX((int)event.getRawX() - 100);
                    player.setY((int)event.getRawY() - 100);
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

            case MotionEvent.ACTION_DOWN: //pause button

                if(!isMoving) {
                    if(event.getX() >= canvasW - 250 && event.getX() <= canvasW-250 + pauseBtn.getWidth()
                            && event.getY() >= 100 && event.getY() <= 100 + pauseBtn.getHeight()) {
                        gameViewListener.pauseGame();
                    }
                }

                break;
        }
        return true;
    }

    public void gameOver() {
        //move to game over intent
        gameViewListener.endGame(score);
    }
}
