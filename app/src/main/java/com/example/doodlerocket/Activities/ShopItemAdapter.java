package com.example.doodlerocket.Activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doodlerocket.R;
import com.example.doodlerocket.ShopItem;

import java.util.List;

public class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.ShopItemViewHolder>{

    private List<ShopItem> shopItems;
    private MyShopItemListener listener;

    interface MyShopItemListener {
        void onItemClicked(int i , View v);
    }

    //decide who listens
    public void setListener(MyShopItemListener listener) {this.listener = listener;}

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
                    if(listener != null)
                        listener.onItemClicked(getAdapterPosition(),v);
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

    }

    @Override
    public int getItemCount() {
        return shopItems.size();
    }

}
