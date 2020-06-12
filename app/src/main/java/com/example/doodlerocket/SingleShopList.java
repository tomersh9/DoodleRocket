package com.example.doodlerocket;

import java.util.ArrayList;
import java.util.List;

public class SingleShopList {

    private static List<ShopItem> shopItems = null;

    private SingleShopList(){}

    public static List<ShopItem> getInstance(){
        if(shopItems == null){
            shopItems = new ArrayList<ShopItem>();
            createShopList(shopItems); //create List only 1 time
        }
        return shopItems;
    }

    private static void createShopList(List<ShopItem> shopItems) {
        shopItems.add(new ShopItem(R.drawable.ship_green_grey_100,5000,"Legendary"));
        shopItems.add(new ShopItem(R.drawable.ship_grey_orange_100,500,"Rare"));
        shopItems.add(new ShopItem(R.drawable.ship_grey_red_100,150,"Common"));
        shopItems.add(new ShopItem(R.drawable.ship_polls_100,500,"Rare"));
        shopItems.add(new ShopItem(R.drawable.ship_purple_black_100,5000,"Legendary"));
        shopItems.add(new ShopItem(R.drawable.ship_red_long_100,20000,"Premium"));
        shopItems.add(new ShopItem(R.drawable.ship_red_reg_100,5000,"Legendary"));
        shopItems.add(new ShopItem(R.drawable.ship_white_polls_100,500,"Rare"));
        shopItems.add(new ShopItem(R.drawable.blast_ship_100,150,"Common"));
        shopItems.add(new ShopItem(R.drawable.blue_red_rare_ship_100,500,"Rare"));
        shopItems.add(new ShopItem(R.drawable.green_gold_ship_100,5000,"Legendary"));
        shopItems.add(new ShopItem(R.drawable.green_red_ship_100,20000,"Premium"));
        shopItems.add(new ShopItem(R.drawable.guitar_pick_ship_100,500,"Rare"));
        shopItems.add(new ShopItem(R.drawable.red_polls_ship_100,5000,"Legendary"));
        shopItems.add(new ShopItem(R.drawable.blackship_prem_100,250,"Premium"));
        shopItems.add(new ShopItem(R.drawable.blueship_detailed_prem_100,500,"Rare"));
        shopItems.add(new ShopItem(R.drawable.blueship_prem_100,5000,"Legendary"));
        shopItems.add(new ShopItem(R.drawable.blueship_reg_100,20000,"Premium"));
        shopItems.add(new ShopItem(R.drawable.blueship_tiny_100,20000,"Premium"));
    }
}
