package com.example.doodlerocket.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.doodlerocket.R;

public class GoldCoin implements IGameObjects {

    private int coinX, coinY, coinSpeed;
    private Bitmap coinBitmap;

    public GoldCoin(int playerMinX, int playerMaxX, Resources resources) {

        coinX = (int) Math.floor(Math.random() * ((playerMaxX - playerMinX) + playerMinX));
        coinY = 0;
        coinSpeed = 23;
        coinBitmap = BitmapFactory.decodeResource(resources, R.drawable.goldcoin_1);
    }

    @Override
    public void drawObject(Canvas canvas) {
        canvas.drawBitmap(coinBitmap,getObjectX(),getObjectY(),null);
    }

    @Override
    public void updateLocation() {
        coinY += coinSpeed;
    }

    @Override
    public int getObjectX() {
        return coinX;
    }

    @Override
    public int getObjectY() {
        return coinY;
    }

    public void setCoinX(int coinX) {
        this.coinX = coinX;
    }

    public void setCoinY(int coinY) {
        this.coinY = coinY;
    }

    @Override
    public boolean collisionDetection(int playerX, int playerY, Bitmap playerBitmap) {

        if(playerX < coinX && coinX < (playerX + playerBitmap.getWidth())
                && playerY < coinY && coinY < (playerY + playerBitmap.getHeight())) {
            return true;
        }
        return false;
    }
}
