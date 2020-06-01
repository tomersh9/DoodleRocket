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
import com.example.doodlerocket.R;
import com.example.doodlerocket.ShopItem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private int coins;
    private boolean isBought;
    private boolean isEquipped;

    private List<ShopItem> shopItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_activity);

        sp = getSharedPreferences("storage",MODE_PRIVATE);
        coins = sp.getInt("money",0);
        isBought = sp.getBoolean("is_bought",false);
        isEquipped = sp.getBoolean("is_equipped",false);

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
                    editor.putBoolean("is_bought",item.isBought());
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
                        editor.putBoolean("is_equipped",item.isEquipped());
                        editor.commit();

                    }
                else { //can't afford buying
                    YoYo.with(Techniques.Shake).duration(700).playOn(v);
                }

                //save changes in memory
                saveData();
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

        try {
            FileOutputStream fos = openFileOutput("items",MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos); //can handle Objects
            oos.writeObject(shopItems); //object of List<ShopItem>
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {

        if(shopItems == null) {
            setShopList(); //create static objects
        }

        try {
            FileInputStream fis = openFileInput("items");
            ObjectInputStream ois = new ObjectInputStream(fis);
            shopItems = (List<ShopItem>) ois.readObject(); //create new list to store the list we saved
            ois.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setShopList() {
        shopItems = new ArrayList<>();
        //fill list with data (static)
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
