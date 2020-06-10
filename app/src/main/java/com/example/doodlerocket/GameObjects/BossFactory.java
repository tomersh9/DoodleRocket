package com.example.doodlerocket.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.doodlerocket.R;

import java.util.ArrayList;
import java.util.List;

public class BossFactory {

    private int speed, health;
    private int canvasW;
    private Resources resources;

    private List<Bitmap> idleList = new ArrayList<>();

    //boss skins
    private int[] bossSkins = {R.drawable.moon_boss_200,R.drawable.kraken_boss_200,R.drawable.spider_boss_200
                              ,R.drawable.forest_boss_200, R.drawable.ocean_boss_150,R.drawable.blood_boss_170
                              ,R.drawable.lava_boss_200};

    //setting base parameters of enemy and increase with levels
    public BossFactory(Resources resources, int speed, int health , int canvasW) {

        this.speed = speed;
        this.health = health;
        this.canvasW = canvasW;
        this.resources = resources;
    }

    public Boss generateBoss(int lvl) {

        //spawn enemies with different properties according to level
        //Boss(int x, int y, int speed, int health, int canvasW, int enemySkinID, Resources resources)

        switch (lvl) {

            case 1: //moon - gunner
                idleList.clear();
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.frame_0000));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.frame_0010));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.frame_0014));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.frame_0018));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.frame_0021));
                return new Boss(canvasW/2,-100,speed,health+20,bossSkins[0],canvasW,idleList,resources);

            case 2: //earth
                idleList.clear();
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.kraken_boss_200));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.kraken_boss_200));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.kraken_boss_200));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.kraken_boss_200));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.kraken_boss_200));
                return new Boss(canvasW/2,-100,speed+3,health+30,bossSkins[1],canvasW,idleList,resources);

            case 3: //desert - spider
                idleList.clear();
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.f1_spider_boss));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.f2_spider_boss));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.f3_spider_boss));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.f4_spider_boss));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.f5_spider_boss));
                return new Boss(canvasW/2,0,speed+5,health+40,bossSkins[2],canvasW,idleList,resources);

            case 4: //forest - squid
                idleList.clear();
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.f1_forest_boss));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.f2_forest_boss));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.f3_forest_boss));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.f4_forest_boss));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.f5_forest_boss));
                return new Boss(canvasW/2,0,speed+7,health+50,bossSkins[3],canvasW,idleList,resources);

            case 5: //ocean - blue octupus
                idleList.clear();
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.f1_ocean_boss));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.f2_ocean_boss));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.f3_ocean_boss));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.f4_ocean_boss));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.f5_ocean_boss));
                return new Boss(canvasW/2,0,speed+9,health+60,bossSkins[4],canvasW,idleList,resources);

            case 6: //ice
                idleList.clear();
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.blood_boss_170));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.blood_boss_170));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.blood_boss_170));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.blood_boss_170));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.blood_boss_170));
                return new Boss(canvasW/2,0,speed+10,health+90,bossSkins[5],canvasW,idleList,resources);

            case 7: //hell
                idleList.clear();
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.lava_boss_200));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.lava_boss_200));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.lava_boss_200));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.lava_boss_200));
                idleList.add(BitmapFactory.decodeResource(resources,R.drawable.lava_boss_200));
                return new Boss(canvasW/2,0,speed+15,health+200,bossSkins[6],canvasW,idleList,resources);
        }
        return null;
    }
}
