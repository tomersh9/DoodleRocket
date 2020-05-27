package com.example.doodlerocket.GameObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

//all objects in game implements this so they can interact with each other
//and can be manipulated by us
public interface IGameObjects {
    void drawObject(Canvas canvas);
    void updateLocation();
    int getObjectX();
    int getObjectY();
    boolean collisionDetection(int playerX, int playerY, Bitmap playerBitmap);
}
