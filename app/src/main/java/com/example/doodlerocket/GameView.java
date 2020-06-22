package com.example.doodlerocket;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.example.doodlerocket.GameObjects.Boss;
import com.example.doodlerocket.GameObjects.BossFactory;
import com.example.doodlerocket.GameObjects.BossProjectile;
import com.example.doodlerocket.GameObjects.Bullet;
import com.example.doodlerocket.GameObjects.Enemy;
import com.example.doodlerocket.GameObjects.EnemyFactory;
import com.example.doodlerocket.GameObjects.EnemyProjectile;
import com.example.doodlerocket.GameObjects.FireBoost;
import com.example.doodlerocket.GameObjects.GoldCoin;
import com.example.doodlerocket.GameObjects.GreenCoin;
import com.example.doodlerocket.GameObjects.LifeBoost;
import com.example.doodlerocket.GameObjects.Meteor;
import com.example.doodlerocket.GameObjects.MeteorFactory;
import com.example.doodlerocket.GameObjects.Player;
import com.example.doodlerocket.GameObjects.ShieldBoost;
import com.example.doodlerocket.GameObjects.SilverCoin;
import com.example.doodlerocket.GameObjects.SoundManager;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class GameView extends View {

    private static int MOBS = 6;

    private Context context;

    //high score data
    SharedPreferences sp;
    private int money; //green currency
    private int gameCurrency;
    private int score; //regular coins score
    private int currLvl;
    private boolean isWon;

    //SFX manager
    private SoundManager soundManager;

    //player
    Player player;
    private int health;
    private boolean isMoving = false;

    //player bullets
    private int extraCounter;
    private boolean extraBullet = false;
    private boolean isBullet = true;
    private List<Bullet> bullets = new ArrayList<>();
    private List<Bullet> removeBulletsList = new ArrayList<>();

    //enemy projectiles
    private boolean isProjectile = true;
    private List<EnemyProjectile> projectiles = new ArrayList<>();
    private List<EnemyProjectile> removeProjectilesList = new ArrayList<>();

    //enemy counter to know when to spawn boss
    //boss unique projectiles
    private int enemyCounter;
    private boolean isBoss = false;
    private boolean isBossProjectile = true;

    private int bossEvent = 0;
    private int bossHealth;
    private int bossSpeed;
    private Boss boss;
    private BossFactory bossFactory;
    private List<BossProjectile> bossProjectiles = new ArrayList<>();
    private List<BossProjectile> removeBossProjectilesList = new ArrayList<>();

    //enemy factory
    private EnemyFactory enemyFactory;
    private boolean isTimeToEnemy = true;
    private List<Enemy> enemies = new ArrayList<>();
    private List<Enemy> removeEnemiesList = new ArrayList<>();

    //meteor factory DP
    private MeteorFactory meteorFactory;
    private boolean isTimeToSpawnMeteor = true;
    private List<Meteor> meteors = new ArrayList<>();
    private List<Meteor> removeMeteorsList = new ArrayList<>();

    //items
    private boolean isTimeToGoldCoin = true;
    private boolean isTimeToSilverCoin = true;
    private boolean isTimeToGreenCoin = true;
    private List<GoldCoin> goldCoins = new ArrayList<>();
    private List<GoldCoin> removeGoldCoins = new ArrayList<>();
    private List<SilverCoin> silverCoins = new ArrayList<>();
    private List<SilverCoin> removeSilverCoins = new ArrayList<>();
    private List<GreenCoin> greenCoins = new ArrayList<>();
    private List<GreenCoin> removeGreenCoins = new ArrayList<>();

    //extra bullets boost
    private boolean isTimeToFireBoost = false;
    private boolean stopSpawnFireBoost = false;
    private List<FireBoost> fireBoostList = new ArrayList<>();
    private List<FireBoost> removeFireBoosts = new ArrayList<>();

    //extra health boost
    private boolean isTimeToLifeBoost = false;
    private boolean stopSpawnLifeBoost = false;
    private List<LifeBoost> lifeBoostList = new ArrayList<>();
    private List<LifeBoost> removeLifeBoosts = new ArrayList<>();

    //shield boost
    private Paint shieldPaint = new Paint();
    private boolean isTimeToShieldBoost = false;
    private boolean stopSpawnShieldBoost = false;
    private boolean shieldTimer = false;
    private List<ShieldBoost> shieldBoostList = new ArrayList<>();
    private List<ShieldBoost> removeShieldBoosts = new ArrayList<>();

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
    private Paint bossHealthPaint = new Paint();
    private Paint greenPaint = new Paint();
    private Paint bossHealthBarPaint = new Paint();

    //interface to communicate with the MainActivity "father" outside
    public interface GameViewListener {
        void pauseGame();

        void resumeGame();

        void playBossMusic();

        void endGame(int score, boolean isWon);
    }

    //listener object
    public GameViewListener gameViewListener;

    //will be called outside this class to use this interface methods
    public void setGameViewListener(GameViewListener gameViewListener) {
        this.gameViewListener = gameViewListener;
    }

    public GameView(Context context, int screenW, int screenH, int skinID, int backgroundID, int currLvl, SoundManager soundManager) { //context when calling it
        super(context);

        this.context = context;

        //reading total player money
        sp = getContext().getSharedPreferences("storage", Context.MODE_PRIVATE);
        money = sp.getInt("money", 0);
        isWon = false;

        //adjust screen to all sizes
        canvasW = screenW;
        canvasH = screenH;

        //initialize sound manager
        this.soundManager = soundManager;

        //set player
        player = new Player(canvasW, canvasH, getResources(), skinID);

        //enemy counter
        enemyCounter = 0;

        //extra bullets
        extraCounter = 0;

        //current lvl (to delay objects)
        this.currLvl = currLvl;

        //initialize background fit to screen
        this.backgroundID = backgroundID;
        setGameBackground();

        //factories
        meteorFactory = new MeteorFactory(player.getMinX(), player.getMaxX(), 20, getResources());
        enemyFactory = new EnemyFactory(getResources(), player.getMinX(), player.getMaxX(), 10, 3, canvasW);
        bossFactory = new BossFactory(getResources(), 10, 30, canvasW);

        //create boss
        boss = bossFactory.generateBoss(currLvl);
        bossHealth = boss.getHealth(); //to trigger mid fight events
        bossSpeed = boss.getSpeed(); //same

        //hearts, score and life
        setUI();
    }


    private void setGameBackground() {
        //setting up background to fit screen
        background = BitmapFactory.decodeResource(getResources(), backgroundID);

        if (backgroundID == R.drawable.moon_bg_800) {
            isScale = false; //stay original size
        } else {
            isScale = true;
            Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            dWidth = size.x;
            dHeight = size.y;
            rect = new Rect(0, 0, dWidth, dHeight);
        }
    }

    private void setUI() {
        //setting score paint properties
        //custom font
        Typeface fontTypeface = ResourcesCompat.getFont(getContext(), R.font.retro);
        scorePaint.setColor(Color.YELLOW);
        scorePaint.setTextSize(80);
        scorePaint.setTypeface(fontTypeface);
        scorePaint.setAntiAlias(true);

        //game currency paint
        greenPaint.setColor(Color.GREEN);
        greenPaint.setTextSize(70);
        greenPaint.setTypeface(fontTypeface);
        greenPaint.setAntiAlias(true);

        //boss health indication
        bossHealthPaint.setColor(Color.RED);
        bossHealthPaint.setTextSize(120);
        bossHealthPaint.setAntiAlias(true);
        bossHealthPaint.setTypeface(fontTypeface);

        //shield paint around player
        shieldPaint.setAntiAlias(true);
        shieldPaint.setARGB(100,0,0,255);
        shieldPaint.setStyle(Paint.Style.STROKE);
        shieldPaint.setStrokeWidth(30);

        //boss healthbar
        bossHealthBarPaint.setColor(Color.RED);

        //life display of fish (have or not)
        life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.heart_red_48);
        life[1] = BitmapFactory.decodeResource(getResources(), R.drawable.heart_gray_48);

        //pause button
        pauseBtn = BitmapFactory.decodeResource(getResources(), R.drawable.pause_icon_50);

        //initialize score & health
        score = 0;
        gameCurrency = 0;
        health = player.getHealth(); // 3 life points
    }

    @Override
    protected void onDraw(Canvas canvas) { //order matters!!
        super.onDraw(canvas);

        //getting canvas measures
        final int canvasW = canvas.getWidth(); //1440
        final int canvasH = canvas.getHeight(); //2352

        //local vars of player constantly updating
        int playerX = player.getObjectX();
        int playerY = player.getObjectY();
        Bitmap playerBitmap = player.getPlayerBitmap();

        //UI drawing
        if (isScale) { //fit screen
            canvas.drawBitmap(background, null, rect, null);
        } else { //don't fit screen
            canvas.drawBitmap(background, 0, 0, null);
        }

        //score text
        canvas.drawText("Score:" + score, 60, 150, scorePaint);

        //currency text
        canvas.drawText("Gems:" + gameCurrency, 60, canvasH - 150, greenPaint);

        //draw pause button
        canvas.drawBitmap(pauseBtn, canvasW - 250, 100, null);

        if(shieldTimer) {
            canvas.drawCircle((float)(playerX+playerBitmap.getWidth()/2),(float)(playerY+playerBitmap.getHeight()/2),180,shieldPaint);
        }

        //draw boss health
        if (isBoss) {
            canvas.drawRect(50, 175, boss.getHealth() * 30, 210, bossHealthBarPaint);
        }

        //update hearts bitmaps
        for (int i = 0; i < 3; i++) {

            //20x - first heart position
            int x = (int) (20 + life[0].getWidth() * i);
            int y = 200; //margin top

            if (i < health) { //full heart
                canvas.drawBitmap(life[0], x, y, null);
            } else { //grey heart
                canvas.drawBitmap(life[1], x, y, null);
            }
        }

        //draw player
        player.drawObject(canvas);
        player.updateLocation();

        //handles player's life events and boosts spawning
        playerHealthEvents();

        //fire boosts
        fireBoostEvents();
        
        //shield boost
        shieldBoostEvent();

        //keep spawning objects until end level then spawn boss
        if (enemyCounter < MOBS + currLvl) { //min mobs to kill each lvl

            //spawn meteors
            spawnMeteors();

            //spawn enemies with projectiles
            spawnEnemies();

            //enemy shoots
            spawnProjectiles();

            //spawn coins
            spawnGoldCoins();
            spawnSilverCoins();

            //spawn only 3 per lvl
            if(extraCounter < 2) {
                spawnFireBoosts();
            }
            
            spawnLifeBoosts();
            
            spawnShieldBoost();

        } else { //boss event

            isBoss = true;

            //ugly way of calling it once
            bossEvent++;
            if (bossEvent >= 2) {
                bossEvent = 2;
            }
            //play only once
            if (bossEvent == 1) {
                gameViewListener.playBossMusic();
            }

            //draw boss
            boss.drawObject(canvas);
            boss.updateLocation();
            spawnBossProjectiles();

            //rage mode
            if (boss.getHealth() < bossHealth / 2) {
                boss.setSpeed(bossSpeed*2);
            }
            else if(boss.getHealth() < bossHealth / 3) {
                boss.setSpeed(bossSpeed*3);
            }

            //win the level
            if (boss.getHealth() <= 0) {
                isWon = true;
                boss.setDead(true);
                soundManager.startEnemyDeathSfx();
                gameOver();
            }
        }

        /*Happens all the time*/
        //shoot bullets
        shoot();

        //spawn currency
        spawnGreenCoins();

        //max 3 bullets
        if (extraCounter > 2) {
            extraCounter = 2;
        }

        //*******HANDLES EVENTS OF OBJECTS COLLISIONS OR EXITING SCREEN*************//

        //actual game currency
        for (GreenCoin greenCoin : greenCoins) {

            greenCoin.updateLocation();

            if (greenCoin.collisionDetection(playerX, playerY, playerBitmap)) {
                soundManager.startFireBoostSfx();
                gameCurrency += 25;
                removeGreenCoins.add(greenCoin);
            } else if (greenCoin.getObjectY() > canvasH) {
                removeGreenCoins.add(greenCoin);
            } else {
                greenCoin.drawObject(canvas);
            }
        }
        greenCoins.removeAll(removeGreenCoins);

        //score
        for (GoldCoin goldCoin : goldCoins) {

            goldCoin.updateLocation();

            if (goldCoin.collisionDetection(playerX, playerY, playerBitmap)) {
                soundManager.startGoldCoinSfx();
                score += 50;
                removeGoldCoins.add(goldCoin);
            } else if (goldCoin.getObjectY() > canvasH) {
                removeGoldCoins.add(goldCoin);
            } else {
                goldCoin.drawObject(canvas);
            }
        }
        goldCoins.removeAll(removeGoldCoins);

        for (SilverCoin silverCoin : silverCoins) {

            silverCoin.updateLocation();

            if (silverCoin.collisionDetection(playerX, playerY, playerBitmap)) {
                soundManager.startSilverCoinSfx();
                score += 10;
                removeSilverCoins.add(silverCoin);
            } else if (silverCoin.getObjectY() > canvasH) {
                removeSilverCoins.add(silverCoin);
            } else {
                silverCoin.drawObject(canvas);
            }
        }
        silverCoins.removeAll(removeSilverCoins);

        //boosts
        for (FireBoost fireBoost : fireBoostList) {
            fireBoost.updateLocation();
            if (fireBoost.collisionDetection(playerX, playerY, playerBitmap)) {
                soundManager.startFireBoostSfx();
                extraCounter++;
                extraBullet = true;
                isBullet = false;
                removeFireBoosts.add(fireBoost);
            } else if (fireBoost.getObjectY() > canvasH) {
                removeFireBoosts.add(fireBoost);
            } else {
                fireBoost.drawObject(canvas);
            }
        }
        fireBoostList.removeAll(removeFireBoosts);

        for(LifeBoost lifeBoost : lifeBoostList) {
            lifeBoost.updateLocation();
            if(lifeBoost.collisionDetection(playerX,playerY,playerBitmap)) {
                soundManager.startFireBoostSfx();
                health++;
                removeLifeBoosts.add(lifeBoost);
            }
            else if(lifeBoost.getObjectY() > canvasH) {
                removeLifeBoosts.add(lifeBoost);
            }
            else {
                lifeBoost.drawObject(canvas);
            }
        }
        lifeBoostList.removeAll(removeLifeBoosts);

        for(ShieldBoost shieldBoost : shieldBoostList) {
            shieldBoost.updateLocation();
            if(shieldBoost.collisionDetection(playerX,playerY,playerBitmap)) {
                soundManager.startFireBoostSfx();
                shieldTimer = true;
                removeShieldBoosts.add(shieldBoost);
            }
            else if(shieldBoost.getObjectY() > canvasH) {
                removeShieldBoosts.add(shieldBoost);
            }
            else {
                shieldBoost.drawObject(canvas);
            }
        }
        shieldBoostList.removeAll(removeShieldBoosts);

        for (final Meteor meteor : meteors) {

            //moving on screen
            meteor.updateLocation();

            //meteor hits player
            if ( !shieldTimer && !meteor.isBoom() && meteor.collisionDetection(playerX, playerY, playerBitmap)) {
                soundManager.startMeteorDeathSfx();
                soundManager.startPlayerHitSfx();
                health--;
                meteor.setBoom(true);
                //hit animation and then remove it
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        removeMeteorsList.add(meteor);
                    }
                }, 400);
            }
            //meteor goes off screen
            else if (meteor.getObjectY() > canvasH) {
                removeMeteorsList.add(meteor);
            } else {
                meteor.drawObject(canvas);
            }
        }
        meteors.removeAll(removeMeteorsList);

        for (Enemy enemy : enemies) {

            enemy.updateLocation();

            if ( !shieldTimer && enemy.collisionDetection(playerX, playerY, playerBitmap)) {

                soundManager.startPlayerHitSfx();
                soundManager.startEnemyDeathSfx();
                enemyCounter++; //after number of enemies, release the boss
                health--;
                removeEnemiesList.add(enemy);
            } else if (enemy.getObjectY() > canvasH) {
                removeEnemiesList.add(enemy);
            } else {
                enemy.drawObject(canvas);
            }
        }
        enemies.removeAll(removeEnemiesList);

        //handle bullets and collisions
        for (final Bullet bullet : bullets) {

            bullet.updateLocation();

            if (bullet.getObjectY() < 0) { //cross the screen
                removeBulletsList.add(bullet);

            } else if (isBoss && Rect.intersects(bullet.getCollisionShape(), boss.getCollisionShape())) { //hits boss
                soundManager.startEnemyHitSfx();
                //bullet.setBoom(true);
                boss.takeDamage(1);
                removeBulletsList.add(bullet);

            } else { //moves on screen
                bullet.drawObject(canvas);
            }

            //check for collision bullet-meteor
            for (final Meteor meteor : meteors) {

                if (!meteor.isBoom() && Rect.intersects(bullet.getCollisionShape(), meteor.getCollisionShape())) {

                    score += 10;
                    soundManager.startMeteorDeathSfx();
                    meteor.setBoom(true);
                    //hit animation and then remove it
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            removeMeteorsList.add(meteor);
                        }
                    }, 400);
                    removeBulletsList.add(bullet);
                }
            }

            //collision bullet - enemy
            for (final Enemy enemy : enemies) {

                if (!enemy.isDead() && Rect.intersects(bullet.getCollisionShape(), enemy.getCollisionShape())) {
                    //flash enemy
                    soundManager.startEnemyHitSfx();
                    enemy.takeDamage(1); //dmg taken

                    //enemy death
                    if (enemy.getHealth() == 0) {

                        score += 75;
                        //death animation
                        enemyCounter++; //after number of enemies, release the boss
                        soundManager.startEnemyDeathSfx();
                        enemy.setDead(true);
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                removeEnemiesList.add(enemy);
                            }
                        }, 300);
                    }
                    removeBulletsList.add(bullet);
                }
            }
            enemies.removeAll(removeEnemiesList);
        }
        //remove all previous bullets from list
        bullets.removeAll(removeBulletsList);

        for (EnemyProjectile projectile : projectiles) {

            projectile.updateLocation();

            if (projectile.getObjectY() > canvasH) {
                removeProjectilesList.add(projectile);
            } else if ( !shieldTimer && projectile.collisionDetection(playerX, playerY, playerBitmap)) {
                soundManager.startPlayerHitSfx();
                health--;
                removeProjectilesList.add(projectile);
            } else {
                projectile.drawObject(canvas);
            }
        }
        projectiles.removeAll(removeProjectilesList);

        //handles boss events
        if (isBoss) {

            //handles boss projectile when hitting player
            for (BossProjectile bossProjectile : bossProjectiles) {

                bossProjectile.updateLocation();

                if (bossProjectile.getObjectY() > canvasH) {
                    removeBossProjectilesList.add(bossProjectile);
                } else if ( !shieldTimer && bossProjectile.collisionDetection(playerX, playerY, playerBitmap)) {
                    soundManager.startPlayerHitSfx();
                    health--;
                    removeBossProjectilesList.add(bossProjectile);
                } else {
                    bossProjectile.drawObject(canvas);
                }
            }
            bossProjectiles.removeAll(removeBossProjectilesList);
        }

    }

    //handles player's health
    private void playerHealthEvents() {
        if(health < 3 && health > 0) {
            lifeBoostEvents();
        }
        else if (health == 0) {

            //failed the stage
            isWon = false;

            //death sfx
            soundManager.startPlayerDeathSfx();

            //move intent
            gameOver();
        }
    }

    private void shieldBoostEvent() {
        if(enemyCounter == 2) {
            if(stopSpawnShieldBoost) {
                //do nothing
            }
            else {
                isTimeToShieldBoost = true;
                stopSpawnShieldBoost = true;
            }
        }
        else if(enemyCounter == 5) {
            if(stopSpawnShieldBoost) {
                //do nothing
            }
            else {
                isTimeToShieldBoost = true;
                stopSpawnShieldBoost = true;
            }
        }
        else {
            stopSpawnShieldBoost = false;
        }
    }

    private void fireBoostEvents() {
        if(enemyCounter == 3) {
            if(stopSpawnFireBoost) {
                //do nothing
            }
            else {
                isTimeToFireBoost = true;
                stopSpawnFireBoost = true;
            }
        }
        else if (enemyCounter == 6) {
            if(stopSpawnFireBoost) {
                //do nothing
            }
            else {
                isTimeToFireBoost = true;
                stopSpawnFireBoost = true;
            }

        }
        else if(enemyCounter == 9) {
            if(stopSpawnFireBoost) {
                //do nothing
            }
            else {
                isTimeToFireBoost = true;
                stopSpawnFireBoost = true;
            }
        }
        else {
            stopSpawnFireBoost = false;
        }
    }

    private void lifeBoostEvents() {
        //spawn life boosts on certain events
        if(enemyCounter == 4) {
            if(stopSpawnLifeBoost) {
                //do nothing
            }
            else {
                isTimeToLifeBoost = true;
                stopSpawnLifeBoost = true;
            }
        }
        else if (enemyCounter == 7) {
            if(stopSpawnLifeBoost) {
                //do nothing
            }
            else {
                isTimeToLifeBoost = true;
                stopSpawnLifeBoost = true;
            }

        }
        else if(enemyCounter == 10) {
            if(stopSpawnLifeBoost) {
                //do nothing
            }
            else {
                isTimeToLifeBoost = true;
                stopSpawnLifeBoost = true;
            }
        }
        else {
            stopSpawnLifeBoost = false;
        }
    }

    //*********SPAWNING OBJECTS TO THE GAME (WITH DELAY ACCORDING TO THE LEVEL)**************//

    private void spawnFireBoosts() {
        if (isTimeToFireBoost) {
            fireBoostList.add(new FireBoost(player.getMinX(), player.getMaxX(), getResources()));
            isTimeToFireBoost = false;
        }
    }

    private void spawnLifeBoosts() {
        if (isTimeToLifeBoost) {
            lifeBoostList.add(new LifeBoost(player.getMinX(), player.getMaxX(), getResources()));
            isTimeToLifeBoost = false;
        }
    }

    private void spawnShieldBoost() {
        if(isTimeToShieldBoost) {
            shieldBoostList.add(new ShieldBoost(player.getMinX(), player.getMaxX(), getResources()));
            isTimeToShieldBoost = false;
            tickShieldTime(); //time shield lasts on player
        }
    }

    private void tickShieldTime() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                shieldTimer = false;
            }
        },4000); //lasts 3 seconds
    }

    private void spawnGreenCoins() {
        if (isTimeToGreenCoin) {
            greenCoins.add(new GreenCoin(player.getMinX(), player.getMaxX(), getResources()));
            delayGreenCoin();
        }
    }

    private void spawnBossProjectiles() {
        if (isBossProjectile) {
            bossProjectiles.add(new BossProjectile(getResources(), boss));
            delayBossProjectiles();
        }
    }

    private void delayBossProjectiles() {
        isBossProjectile = false;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isBossProjectile = true;
            }
        }, 600 - (currLvl * 50));
    }


    private void spawnSilverCoins() {

        if (isTimeToSilverCoin) {
            silverCoins.add(new SilverCoin(player.getMinX(), player.getMaxX(), getResources()));
            delaySilverCoin();
        }

    }

    private void spawnEnemies() {

        if (isTimeToEnemy) {
            enemies.add(enemyFactory.generateEnemy(currLvl));
            delayEnemy();
        }
    }

    private void spawnProjectiles() {
        //for each enemy spawns a projectile to shoot
        //don't shoot when dead
        if (isProjectile) {
            for (Enemy enemy : enemies) {
                if (enemy != null && !enemy.isDead()) {
                    soundManager.startEnemyLaserSfx();
                    projectiles.add(new EnemyProjectile(getResources(), enemy));
                    delayEnemyShots();
                }
            }
        }
    }

    private void spawnMeteors() {
        if (isTimeToSpawnMeteor) {
            meteors.add(meteorFactory.generateMeteor(currLvl));
            delayMeteor();
        }
    }

    private void spawnGoldCoins() {
        if (isTimeToGoldCoin) {
            goldCoins.add(new GoldCoin(player.getMinX(), player.getMaxX(), getResources()));
            delayGoldCoin();
        }
    }

    private void shoot() {

        if (isBullet && !extraBullet && extraCounter == 0) {
            soundManager.startPlayerLaserSfx();
            bullets.add(new Bullet(getResources(), player.getObjectX(), player.getObjectY()));
            delayBullets(); //isFire = false cause delay between creating new bullets and drawing them
        } else if (extraBullet && extraCounter > 0) {

            isBullet = false; //stop regular

            if (extraCounter == 1) {
                soundManager.startPlayerLaserSfx();
                bullets.add(new Bullet(getResources(), player.getObjectX() - 75, player.getObjectY()));
                bullets.add(new Bullet(getResources(), player.getObjectX() + 75, player.getObjectY()));
                delayExtraBullets(); //isFire = false cause delay between creating new bullets and drawing them
            } else if (extraCounter == 2) {
                soundManager.startPlayerLaserSfx();
                bullets.add(new Bullet(getResources(), player.getObjectX() - 75, player.getObjectY()));
                bullets.add(new Bullet(getResources(), player.getObjectX(), player.getObjectY()));
                bullets.add(new Bullet(getResources(), player.getObjectX() + 75, player.getObjectY()));
                delayExtraBullets(); //isFire = false cause delay between creating new bullets and drawing them
            }
        }
    }

    //delay between shots
    private void delayBullets() {
        isBullet = false;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isBullet = true;
            }
        }, 300 - (currLvl * 20));
    }

    //timer for multiple bullets
    private void delayExtraBullets() {
        //default delay
        int delay = 500;
        if(extraCounter == 2) { //not to OP
            delay = 650;
        }
        extraBullet = false;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                extraBullet = true;
            }
        }, delay - (currLvl * 20)); //delay = 300
    }

    private void delayGreenCoin() {
        isTimeToGreenCoin = false;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isTimeToGreenCoin = true;
            }
        }, 5000 + (currLvl * 50));
    }

    private void delayEnemyShots() {
        isProjectile = false;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isProjectile = true;
            }
        }, 2000 - (currLvl * 100));
    }


    private void delayEnemy() {
        isTimeToEnemy = false;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isTimeToEnemy = true;
            }
        }, 3500 - (currLvl * 300));
    }

    //delay between meteor spawns
    private void delayMeteor() {
        isTimeToSpawnMeteor = false;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isTimeToSpawnMeteor = true;
            }
        }, 1600 - (currLvl * 200));
    }

    //delay between coins spawns
    private void delayGoldCoin() {
        isTimeToGoldCoin = false;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isTimeToGoldCoin = true;
            }
        }, 5000 - (currLvl * 50));
    }

    private void delaySilverCoin() {
        isTimeToSilverCoin = false;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isTimeToSilverCoin = true;
            }
        }, 3000 - (currLvl * 50));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            //Drag&Drop
            case MotionEvent.ACTION_MOVE:

                if (isMoving) {
                    player.setX((int) event.getRawX() - 100);
                    player.setY((int) event.getRawY() - 100);
                } else if (event.getX() > player.getObjectX()
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

                if (!isMoving) {
                    if (event.getX() >= canvasW - 250 && event.getX() <= canvasW - 250 + pauseBtn.getWidth()
                            && event.getY() >= 100 && event.getY() <= 100 + pauseBtn.getHeight()) {
                        gameViewListener.pauseGame();
                    }
                }
                break;
        }
        return true;
    }

    public void gameOver() {
        //move to game over intent or victory dialog
        SharedPreferences.Editor editor = sp.edit();

        editor.putInt("gems", gameCurrency);
        int total = money + gameCurrency; //actual money to buy things
        editor.putInt("money", total);
        editor.commit();

        soundManager.pauseSfx();
        gameViewListener.endGame(score, isWon);
    }
}
