package com.example.doodlerocket.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.doodlerocket.R;

import java.util.ArrayList;
import java.util.List;

public class GreenCoin implements IGameObjects {

    private int coinX, coinY, coinSpeed;

    private List<Bitmap> greenCoinList = new ArrayList<>();
    private int pos = 0;

    public GreenCoin(int playerMinX, int playerMaxX, Resources resources) {

        greenCoinList.add(BitmapFactory.decodeResource(resources,R.drawable.green_money_30_01));
        greenCoinList.add(BitmapFactory.decodeResource(resources,R.drawable.green_money_30_02));
        greenCoinList.add(BitmapFactory.decodeResource(resources,R.drawable.green_money_30_03));
        greenCoinList.add(BitmapFactory.decodeResource(resources,R.drawable.green_money_30_04));
        greenCoinList.add(BitmapFactory.decodeResource(resources,R.drawable.green_money_30_04));

        coinX = (int) Math.floor(Math.random() * ((playerMaxX - playerMinX) + playerMinX));
        coinY = 0;
        coinSpeed = 40;
    }

    @Override
    public void drawObject(Canvas canvas) {
        canvas.drawBitmap(greenCoinList.get(pos/5),getObjectX(),getObjectY(),null);
        pos++;
        pos=pos%25;
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
