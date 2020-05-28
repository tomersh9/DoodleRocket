package com.example.doodlerocket.Activities;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doodlerocket.GameObjects.Player;
import com.example.doodlerocket.R;
import com.example.doodlerocket.ShopItem;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_activity);

        //instance of player
        //maybe we need SP to send bitmap to GameView to initialize player with skin selected

        //ref our RecycleView in activity_main
        RecyclerView recyclerView = findViewById(R.id.shop_list);
        recyclerView.setHasFixedSize(true); // const size

        //defines the layout (Vertical, Horizontal, Grid..)
        recyclerView.setLayoutManager(new GridLayoutManager(this,2)); //number of col in a row

        //fill list of items in the shop
        final List<ShopItem> shopItems = new ArrayList<>();

        shopItems.add(new ShopItem(R.drawable.ship_blast_png,100,"Legendary"));
        shopItems.add(new ShopItem(R.drawable.spaceship_green_png,50,"Rare"));
        shopItems.add(new ShopItem(R.drawable.spaceship_long_red_png,10,"Common"));
        shopItems.add(new ShopItem(R.drawable.spaceship_orange_png,50,"Rare"));
        shopItems.add(new ShopItem(R.drawable.spaceship_purple_long_png,100,"Legendary"));
        shopItems.add(new ShopItem(R.drawable.premium_spaceship_png,250,"Premium"));
        shopItems.add(new ShopItem(R.drawable.gold_green_spaceship_png,100,"Legendary"));
        shopItems.add(new ShopItem(R.drawable.green_red_spaceship_png,50,"Rare"));
        shopItems.add(new ShopItem(R.drawable.purple_green_ship_png,10,"Common"));
        shopItems.add(new ShopItem(R.drawable.red_blue_ship_premium,50,"Rare"));
        shopItems.add(new ShopItem(R.drawable.spaceship_red_png,100,"Legendary"));
        shopItems.add(new ShopItem(R.drawable.ship_blue,250,"Premium"));
        shopItems.add(new ShopItem(R.drawable.reg_ship_premium_png,50,"Rare"));
        shopItems.add(new ShopItem(R.drawable.blue_purple_ship_premium,100,"Legendary"));
        shopItems.add(new ShopItem(R.drawable.spaceship_polls_png,250,"Premium"));


        //create instance of adapter that has ViewHolder inside (to inflate our cells)
        final ShopItemAdapter shopItemAdapter = new ShopItemAdapter(shopItems);

        //setListener is our own public method
        shopItemAdapter.setListener(new ShopItemAdapter.MyShopItemListener() {
            @Override
            public void onItemClicked(int i, View v) {
                Toast.makeText(ShopActivity.this, shopItems.get(i).getPrice()+"", Toast.LENGTH_SHORT).show();
            }
        });

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

    //when pressing back btn
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
