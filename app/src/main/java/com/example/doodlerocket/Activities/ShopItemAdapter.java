package com.example.doodlerocket.Activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doodlerocket.R;
import com.example.doodlerocket.ShopItem;

import java.util.ArrayList;
import java.util.List;

public class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.ShopItemViewHolder>{

    private List<ShopItem> shopItems;
    private MyShopItemListener myShopItemListener;

    interface MyShopItemListener {
        void onItemClicked(int i , View v);
    }

    //decide who listens
    public void setMyShopItemListener(MyShopItemListener myShopItemListener) {this.myShopItemListener = myShopItemListener;}

    //constructor
    public ShopItemAdapter(List<ShopItem> shopItems)
    {
        this.shopItems = shopItems;
    }


    //this class Holds our inflated views
    public class ShopItemViewHolder extends RecyclerView.ViewHolder {

        ImageView itemIv;
        TextView priceTv;
        TextView rarityTv;

        public ShopItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemIv = itemView.findViewById(R.id.ship);
            priceTv = itemView.findViewById(R.id.price_tv);
            rarityTv = itemView.findViewById(R.id.rarity_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(myShopItemListener != null)
                        myShopItemListener.onItemClicked(getAdapterPosition(),v);
                }
            });
        }
    }

    @NonNull
    @Override //creates view holder by inflating view and sending the ViewHolder that view
    public ShopItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Inflating view from parent context
        View view = LayoutInflater.from(parent.getContext())
                .inflate((R.layout.shop_item_cell),parent,false);

        //class that now holds our inflated view
        ShopItemViewHolder shopItemViewHolder = new ShopItemViewHolder(view);

        //return holder to OS
        return shopItemViewHolder;
    }

    //the new content will enter here
    //called each time we see different cell on screen (a lot)
    //his only job is setting views of the ViewHolder with data from current object
    @Override
    public void onBindViewHolder(@NonNull ShopItemViewHolder holder, int position) {

        //current item in list
        ShopItem item = shopItems.get(position);

        //setting inflated views from our holder with data
        holder.itemIv.setImageResource(item.getSkinId());
        holder.priceTv.setText(item.getPrice()+"");
        holder.rarityTv.setText(item.getRarity());

        if(item.getPrice() > 1000000) {
            holder.priceTv.setTextSize(10);
        }
        if(item.getRarity().matches("Common")) {
            holder.rarityTv.setTextColor(Color.WHITE);
        }
        if(item.getRarity().matches("Rare")) {
            holder.rarityTv.setTextColor(Color.BLUE);
        }
        if(item.getRarity().matches("Legendary")) {
            holder.rarityTv.setTextColor(Color.YELLOW);
        }
        if(item.getRarity().matches("Premium")) {
            holder.rarityTv.setTextColor(Color.MAGENTA);
        }
        if(item.isBought()) {
            holder.rarityTv.setTextColor(Color.WHITE);
        }
        if(item.isEquipped()) {
            holder.rarityTv.setTextColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return shopItems.size();
    }

}
