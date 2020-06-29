package com.example.doodlerocket.Activities;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.doodlerocket.GameObjects.User;
import com.example.doodlerocket.SingleShopList;
import com.example.doodlerocket.R;
import com.example.doodlerocket.ShopItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ShopActivity extends AppCompatActivity {

    private SharedPreferences sp;

    private int coins;
    private List<ShopItem> shopItems;
    private List<ShopItem> stateList;

    private boolean isRTL;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_activity);

        //fixed portrait mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //change list language
        isRTL = getResources().getBoolean(R.bool.is_rtl);

        //getting static shop list (singleton)
        this.shopItems = SingleShopList.getInstance(this);
        this.stateList = shopItems;

        sp = getSharedPreferences("storage", MODE_PRIVATE);
        coins = sp.getInt("money", 0);

        //load history of shop
        loadData();

        //load list of states regardless of language
        loadItemsState();

        final TextView coinsTv = findViewById(R.id.shop_coins_amount);
        coinsTv.setText(coins + "");

        //ref our RecycleView in activity_main
        RecyclerView recyclerView = findViewById(R.id.shop_list);
        recyclerView.setHasFixedSize(true); // const size

        //defines the layout (Vertical, Horizontal, Grid..)
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); //number of col in a row

        //create instance of adapter that has ViewHolder inside (to inflate our cells)
        final ShopItemAdapter shopItemAdapter = new ShopItemAdapter(shopItems, this);

        //setListener is our own public method
        shopItemAdapter.setMyShopItemListener(new ShopItemAdapter.MyShopItemListener() {
            @Override
            public void onItemClicked(int i, View v) {

                //instance of item in the shop
                ShopItem item = shopItems.get(i);
                int price = item.getPrice();

                //can't buy used skins
                if (coins >= price && !item.isBought() && item.getPrice() != 0) {
                    //buy skin and reduce coins
                    coins -= price;
                    coinsTv.setText(coins + "");

                    //change item to bought and equip
                    item.setBought(true);
                    //item.setRarity(getString(R.string.owned));

                    //real-time change
                    shopItemAdapter.notifyDataSetChanged();

                    //reduce total money
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("money", coins);
                    editor.commit();

                } else if (item.getPrice() == 0) { //default skin

                    item.setEquipped(true);
                    //item.setRarity(getString(R.string.equipped));
                    shopItemAdapter.notifyDataSetChanged();

                    //save equipped state
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("skin_id", item.getSkinId());
                    editor.commit();

                    //only current item is equipped
                    setItemsState(item);

                } else if (item.isBought()) {

                    //notify change
                    item.setEquipped(true);
                    //item.setRarity(getString(R.string.equipped));
                    shopItemAdapter.notifyDataSetChanged();

                    //save equipped state
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("skin_id", item.getSkinId());
                    editor.commit();

                    //only current item is equipped
                    setItemsState(item);

                } else { //can't afford buying
                    YoYo.with(Techniques.Shake).duration(700).playOn(v);
                }
            }
        });

        //set the adapter
        recyclerView.setAdapter(shopItemAdapter);

        Button returnBtn = findViewById(R.id.return_btn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); //kills this activity
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void loadItemsState() {
        for(int i = 0 ; i <shopItems.size()-1 ; i++) {
            this.stateList.get(i).setBought(this.shopItems.get(i).isBought());
            this.stateList.get(i).setEquipped(this.shopItems.get(i).isEquipped());
        }
    }

    private void setItemsState(ShopItem item) {
        //update current item equipped
        for (ShopItem shopItem : shopItems) {
            if (!shopItem.equals(item)) {
                if (shopItem.isBought() || shopItem.getPrice() == 0) {
                    shopItem.setEquipped(false);
                    //shopItem.setRarity(getString(R.string.owned));
                }
            }
        }
    }

    private void saveData() {
        if (isRTL) {
            SharedPreferences.Editor editor = sp.edit();
            Gson gson = new Gson();
            String json = gson.toJson(shopItems);
            editor.putString("items_heb", json);
            editor.commit();
        }
        else {
            SharedPreferences.Editor editor = sp.edit();
            Gson gson = new Gson();
            String json = gson.toJson(shopItems);
            editor.putString("items_eng", json);
            editor.commit();
        }

        //save state list
        SharedPreferences.Editor editor = sp.edit();
        Gson gson2 = new Gson();
        String json2 = gson2.toJson(stateList);
        editor.putString("items_state", json2);
        editor.commit();
    }

    private void loadData() {
        if(isRTL) {
            Gson gson = new Gson();
            String json = sp.getString("items_heb", null);
            Type type = new TypeToken<List<ShopItem>>() {
            }.getType();
            if (gson.fromJson(json, type) != null) {
                shopItems = gson.fromJson(json, type);
            }
        }
        else {
            Gson gson = new Gson();
            String json = sp.getString("items_eng", null);
            Type type = new TypeToken<List<ShopItem>>() {
            }.getType();
            if (gson.fromJson(json, type) != null) {
                shopItems = gson.fromJson(json, type);
            }
        }

        //load list state
        Gson gson2 = new Gson();
        String json2 = sp.getString("items_state", null);
        Type type = new TypeToken<List<ShopItem>>() {
        }.getType();
        if (gson2.fromJson(json2, type) != null) {
            stateList = gson2.fromJson(json2, type);
        }
        else {
            stateList = new ArrayList<>();
            stateList = this.shopItems;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.stateList = shopItems; //save list of states
        saveData();
    }
}
