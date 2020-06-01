package com.example.doodlerocket;

import android.graphics.Bitmap;

import java.io.Serializable;

public class ShopItem implements Serializable {

    private int skinId;
    private int price;
    private String rarity;
    private boolean isBought;
    private boolean isEquipped;

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

    public boolean isBought() {
        return isBought;
    }

    public void setBought(boolean bought) {
        isBought = bought;
    }

    public boolean isEquipped() {
        return isEquipped;
    }

    public void setEquipped(boolean equipped) {
        isEquipped = equipped;
    }
}
