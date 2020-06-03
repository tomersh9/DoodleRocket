package com.example.doodlerocket.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.doodlerocket.SingleShopList;
import com.example.doodlerocket.R;
import com.example.doodlerocket.ShopItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ShopActivity extends AppCompatActivity {

    private SharedPreferences sp;

    private int coins;
    private List<ShopItem> shopItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_activity);

        sp = getSharedPreferences("storage",MODE_PRIVATE);
        coins = sp.getInt("money",0);

        //Singleton of shop list (static)
        SingleShopList singleShopList = SingleShopList.getInstance();
        shopItems = singleShopList.createShopList();

        //load history of shop
        loadData();

        final TextView coinsTv = findViewById(R.id.shop_coins_amount);
        coinsTv.setText(coins+"");

        //ref our RecycleView in activity_main
        RecyclerView recyclerView = findViewById(R.id.shop_list);
        recyclerView.setHasFixedSize(true); // const size

        //defines the layout (Vertical, Horizontal, Grid..)
        recyclerView.setLayoutManager(new GridLayoutManager(this,2)); //number of col in a row

        //create instance of adapter that has ViewHolder inside (to inflate our cells)
        final ShopItemAdapter shopItemAdapter = new ShopItemAdapter(shopItems);

        //setListener is our own public method
        shopItemAdapter.setMyShopItemListener(new ShopItemAdapter.MyShopItemListener() {
            @Override
            public void onItemClicked(int i, View v) {

                //instance of item in the shop
                ShopItem item = shopItems.get(i);
                int price = item.getPrice();

                //can't buy used skins
                if(coins >= price && !item.isBought()) {
                    //buy skin and reduce coins
                    coins -= price;
                    coinsTv.setText(coins+"");

                    //change item to bought and equip
                    item.setBought(true);
                    item.setRarity("Owned");

                    //real-time change
                    shopItemAdapter.notifyDataSetChanged();

                    //reduce total money
                    //send bitmap to player
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("money",coins);
                    editor.commit();

                }
                else if(item.isBought()) {

                        //notify change
                        item.setEquipped(true);
                        item.setRarity("Equipped");
                        shopItemAdapter.notifyDataSetChanged();

                        //save equipped state
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("skin_id",item.getSkinId());
                        editor.commit();

                    }
                else { //can't afford buying
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
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
    }

    private void saveData() {
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(shopItems);
        editor.putString("items",json);
        editor.commit();

    }

    private void loadData() {

        Gson gson = new Gson();
        String json = sp.getString("items",null);
        Type type = new TypeToken<List<ShopItem>>() {}.getType();
        shopItems = gson.fromJson(json,type);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //save changes in memory
        saveData();
    }
}
