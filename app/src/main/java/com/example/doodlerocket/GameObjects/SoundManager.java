package com.example.doodlerocket.GameObjects;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.example.doodlerocket.R;

public class SoundManager {

    private static SoundPool soundPool;

    //SFX
    private static int playerHitSound;
    private static int enemyHitSound;
    private static int playerLaserSound;
    private static int enemyLaserSound;
    private static int meteorDeathSound;
    private static int playerDeathSound;
    private static int enemyDeathSound;
    private static int goldCoinSound;
    private static int silverCoinSound;
    private static int shieldBoostSound;
    private static int gemSound;
    private static int lifeBoostSound;
    private static int fireBoostSound;

    //fields
    public int playerLaser;

    public SoundManager (Context context) {

        //new versions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(13)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else { //old versions
            soundPool = new SoundPool(13, AudioManager.STREAM_MUSIC, 0);
        }

        playerHitSound = soundPool.load(context, R.raw.hurt1, 1);
        enemyHitSound = soundPool.load(context, R.raw.enemy_hit, 1);
        playerLaserSound = soundPool.load(context, R.raw.laser_shoot_1, 1);
        enemyLaserSound = soundPool.load(context, R.raw.laser_enemy, 1);
        meteorDeathSound = soundPool.load(context, R.raw.meteor_hit, 1);
        enemyDeathSound = soundPool.load(context, R.raw.explosion_enemy, 1);
        playerDeathSound = soundPool.load(context, R.raw.boom1, 1);
        goldCoinSound = soundPool.load(context, R.raw.pick_gold_coin, 1);
        silverCoinSound = soundPool.load(context, R.raw.pick_silver_coin, 1);
        shieldBoostSound = soundPool.load(context, R.raw.shield_boost_sfx, 1);
        gemSound = soundPool.load(context,R.raw.green_coin_sfx,1);
        lifeBoostSound = soundPool.load(context,R.raw.lifeboost_sfx,1);
        fireBoostSound = soundPool.load(context,R.raw.fireboost_sfx,1);
    }

    public void startGemSfx() {soundPool.play(gemSound,0.8f,0.8f,1,0,1);}

    public void startLifeBoostSfx() {soundPool.play(lifeBoostSound,0.9f,0.9f,1,0,1);}

    public void startFireBoostSfx() {soundPool.play(fireBoostSound,0.9f,0.9f,1,0,1);}

    public void startShieldBoostSfx() {
        soundPool.play(shieldBoostSound,0.9f,0.9f,1,0,1);
    }

    public void startPlayerHitSfx() {
        soundPool.play(playerHitSound,1f,1f,1,0,1);
    }

    public void startPlayerDeathSfx() {
        soundPool.play(playerDeathSound,0.7f,0.7f,1,0,1);
    }

    public void startEnemyHitSfx() {
        soundPool.play(enemyHitSound,0.5f,0.5f,1,0,1);
    }

    public void startEnemyDeathSfx() {
        soundPool.play(enemyDeathSound,0.7f,0.7f,1,0,1);
    }

    public void startMeteorDeathSfx() {
        soundPool.play(meteorDeathSound,0.9f,0.9f,1,0,1);
    }

    public void startPlayerLaserSfx() {soundPool.play(playerLaserSound,0.25f,0.25f,1,0,1);
    }

    public void startEnemyLaserSfx() {
        soundPool.play(enemyLaserSound,0.4f,0.4f,1,0,1);
    }

    public void startGoldCoinSfx() {
        soundPool.play(goldCoinSound,0.5f,0.5f,1,0,1);
    }

    public void startSilverCoinSfx() {
        soundPool.play(silverCoinSound,0.5f,0.5f,1,0,1);
    }

    //release memory
    public void stopSfx() {
        soundPool.release();
        //soundPool = null;
    }

    public void resumeSfx() {
        soundPool.autoResume();
    }

    public void pauseSfx() {
        soundPool.autoPause();
    }
}
