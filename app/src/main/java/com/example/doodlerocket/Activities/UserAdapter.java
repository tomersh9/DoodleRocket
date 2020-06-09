package com.example.doodlerocket.Activities;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doodlerocket.GameObjects.User;
import com.example.doodlerocket.R;
import com.example.doodlerocket.ShopItem;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{

    private List<User> users;

    //constructor
    public UserAdapter(List<User> users)
    {
        this.users = users;
    }


    //this class Holds our inflated views
    public class UserViewHolder extends RecyclerView.ViewHolder {

        TextView placeTv;
        TextView scoreTv;
        TextView nameTv;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            placeTv = itemView.findViewById(R.id.rank_cell_tv);
            scoreTv = itemView.findViewById(R.id.score_cell_tv);
            nameTv = itemView.findViewById(R.id.name_cell_tv);
        }
    }

    @NonNull
    @Override //creates view holder by inflating view and sending the ViewHolder that view
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Inflating view from parent context
        View view = LayoutInflater.from(parent.getContext())
                .inflate((R.layout.highscore_cell),parent,false);

        //class that now holds our inflated view
        UserViewHolder userViewHolder = new UserViewHolder(view);

        //return holder to OS
        return userViewHolder;
    }

    //the new content will enter here
    //called each time we see different cell on screen (a lot)
    //his only job is setting views of the ViewHolder with data from current object
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        //current item in list
        User user = users.get(position);
        user.setPlace(position+1);

        //setting inflated views from our holder with data
        if(user.getPlace() == 1) {
            holder.placeTv.setTextColor(Color.GREEN);
            holder.scoreTv.setTextColor(Color.GREEN);
            holder.nameTv.setTextColor(Color.GREEN);
        }
        else if(user.getPlace() == 2) {
            holder.placeTv.setTextColor(Color.YELLOW);
            holder.scoreTv.setTextColor(Color.YELLOW);
            holder.nameTv.setTextColor(Color.YELLOW);
        }
        else if(user.getPlace() == 3) {
            holder.placeTv.setTextColor(Color.MAGENTA);
            holder.scoreTv.setTextColor(Color.MAGENTA);
            holder.nameTv.setTextColor(Color.MAGENTA);
        }

        holder.placeTv.setText(user.getPlace()+"");
        holder.scoreTv.setText(user.getScore()+"");
        holder.nameTv.setText(user.getName());

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

}
