package com.example.doodlerocket.GameObjects;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
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

    public SoundManager (Context context) {

        //new versions
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                            .setMaxStreams(9)
                            .setAudioAttributes(audioAttributes)
                            .build();
        }
        else { //old versions
            soundPool = new SoundPool(9, AudioManager.STREAM_MUSIC,0);
        }


        playerHitSound = soundPool.load(context, R.raw.hurt1,1);
        enemyHitSound = soundPool.load(context,R.raw.enemy_hit,1);
        playerLaserSound = soundPool.load(context, R.raw.laser_shoot_1,1);
        enemyLaserSound = soundPool.load(context,R.raw.laser_enemy,1);
        meteorDeathSound = soundPool.load(context,R.raw.meteor_hit,1);
        enemyDeathSound = soundPool.load(context,R.raw.explosion_enemy,1);
        playerDeathSound = soundPool.load(context, R.raw.boom1,1);
        goldCoinSound = soundPool.load(context, R.raw.pick_gold_coin,1);
        silverCoinSound = soundPool.load(context,R.raw.pick_silver_coin,1);
    }

    public void startPlayerHitSfx() {
        soundPool.play(playerHitSound,0.7f,0.7f,1,0,1);
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
        soundPool.play(meteorDeathSound,0.7f,0.7f,1,0,1);
    }

    public void startPlayerLaserSfx() {
        soundPool.play(playerLaserSound,0.15f,0.15f,1,0,1);
    }

    public void startEnemyLaserSfx() {
        soundPool.play(enemyLaserSound,0.25f,0.25f,1,0,1);
    }

    public void startGoldCoinSfx() {
        soundPool.play(goldCoinSound,0.3f,0.3f,1,0,1);
    }

    public void startSilverCoinSfx() {
        soundPool.play(silverCoinSound,0.3f,0.3f,1,0,1);
    }

    //release memory
    public void stopSfx() {
        soundPool.release();
        soundPool = null;
    }
}
