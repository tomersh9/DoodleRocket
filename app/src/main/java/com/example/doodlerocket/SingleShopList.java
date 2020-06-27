package com.example.doodlerocket;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SingleShopList {

    private static List<ShopItem> shopItems = null;

    private SingleShopList() {}

    public static List<ShopItem> getInstance(Context context) {
        //create list only once
        if (shopItems == null) {
            shopItems = new ArrayList<>();
            createShopList(shopItems,context);
        }
        return shopItems;
    }

    private static void createShopList(List<ShopItem> shopItems, Context context) {
        shopItems.add(new ShopItem(R.drawable.default_ship_100, 0, context.getString(R.string.def)));
        shopItems.add(new ShopItem(R.drawable.ship_green_grey_100, 2500, context.getString(R.string.legendary)));
        shopItems.add(new ShopItem(R.drawable.ship_grey_orange_100, 500, context.getString(R.string.rare)));
        shopItems.add(new ShopItem(R.drawable.ship_grey_red_100, 250, context.getString(R.string.common)));
        shopItems.add(new ShopItem(R.drawable.ship_polls_100, 500, context.getString(R.string.rare)));
        shopItems.add(new ShopItem(R.drawable.ship_purple_black_100, 2500, context.getString(R.string.legendary)));
        shopItems.add(new ShopItem(R.drawable.ship_red_long_100, 10000, context.getString(R.string.premium)));
        shopItems.add(new ShopItem(R.drawable.ship_red_reg_100, 2500, context.getString(R.string.legendary)));
        shopItems.add(new ShopItem(R.drawable.ship_white_polls_100, 500, context.getString(R.string.rare)));
        shopItems.add(new ShopItem(R.drawable.blast_ship_100, 250, context.getString(R.string.common)));
        shopItems.add(new ShopItem(R.drawable.blue_red_rare_ship_100, 500, context.getString(R.string.rare)));
        shopItems.add(new ShopItem(R.drawable.green_gold_ship_100, 2500, context.getString(R.string.legendary)));
        shopItems.add(new ShopItem(R.drawable.green_red_ship_100, 10000, context.getString(R.string.premium)));
        shopItems.add(new ShopItem(R.drawable.guitar_pick_ship_100, 500, context.getString(R.string.rare)));
        shopItems.add(new ShopItem(R.drawable.red_polls_ship_100, 2500, context.getString(R.string.legendary)));
        shopItems.add(new ShopItem(R.drawable.blackship_prem_100, 250, context.getString(R.string.common)));
        shopItems.add(new ShopItem(R.drawable.blueship_detailed_prem_100, 500, context.getString(R.string.rare)));
        shopItems.add(new ShopItem(R.drawable.blueship_prem_100, 2500, context.getString(R.string.legendary)));
        shopItems.add(new ShopItem(R.drawable.blueship_reg_100, 10000, context.getString(R.string.premium)));
        shopItems.add(new ShopItem(R.drawable.blueship_tiny_100, 10000, context.getString(R.string.premium)));

        //sort shop list by price with Comparator
        Collections.sort(shopItems, new Comparator<ShopItem>() {
            @Override
            public int compare(ShopItem s1, ShopItem s2) {
                return s1.getPrice() - s2.getPrice();
            }
        });
    }
}
