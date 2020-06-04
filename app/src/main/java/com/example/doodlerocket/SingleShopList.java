package com.example.doodlerocket;

import java.util.ArrayList;
import java.util.List;

public class SingleShopList {

    private static SingleShopList instance;
    private List<ShopItem> shopItems = null;

    private SingleShopList(){}

    public static SingleShopList getInstance(){
        if(instance == null){
            instance = new SingleShopList();
            instance.createShopList(); //create List only 1 time
        }
        return instance;
    }

    public void createShopList() {

        shopItems = new ArrayList<>();
        shopItems.add(new ShopItem(R.drawable.ship_green_grey_100,100,"Legendary"));
        shopItems.add(new ShopItem(R.drawable.ship_grey_orange_100,50,"Rare"));
        shopItems.add(new ShopItem(R.drawable.ship_grey_red_100,1999999990,"Common"));
        shopItems.add(new ShopItem(R.drawable.ship_polls_100,50,"Rare"));
        shopItems.add(new ShopItem(R.drawable.ship_purple_black_100,100,"Legendary"));
        shopItems.add(new ShopItem(R.drawable.ship_red_long_100,25000,"Premium"));
        shopItems.add(new ShopItem(R.drawable.ship_red_reg_100,100,"Legendary"));
        shopItems.add(new ShopItem(R.drawable.ship_white_polls_100,50,"Rare"));
        shopItems.add(new ShopItem(R.drawable.blast_ship_100,10,"Common"));
        shopItems.add(new ShopItem(R.drawable.blue_red_rare_ship_100,50,"Rare"));
        shopItems.add(new ShopItem(R.drawable.green_gold_ship_100,100000,"Legendary"));
        shopItems.add(new ShopItem(R.drawable.green_red_ship_100,250,"Premium"));
        shopItems.add(new ShopItem(R.drawable.guitar_pick_ship_100,50,"Rare"));
        shopItems.add(new ShopItem(R.drawable.red_polls_ship_100,1045656560,"Legendary"));
        shopItems.add(new ShopItem(R.drawable.blackship_prem_100,250,"Premium"));
        shopItems.add(new ShopItem(R.drawable.blueship_detailed_prem_100,50,"Rare"));
        shopItems.add(new ShopItem(R.drawable.blueship_prem_100,1044440,"Legendary"));
        shopItems.add(new ShopItem(R.drawable.blueship_reg_100,250,"Premium"));
        shopItems.add(new ShopItem(R.drawable.blueship_tiny_100,250,"Premium"));
    }

    public List<ShopItem> getShopList() {
        return this.shopItems;
    }
}
