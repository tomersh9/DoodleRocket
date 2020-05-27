package com.example.doodlerocket;

import android.graphics.Bitmap;

public class ShopItem {

    private int skinId;
    private int price;
    private String rarity;

    public ShopItem(int skinId, int price, String rarity) {
        this.skinId = skinId;
        this.price = price;
        this.rarity = rarity;
    }

    public int getSkinId() {
        return skinId;
    }

    public void setSkinId(int skinId) {
        this.skinId = skinId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }
}
