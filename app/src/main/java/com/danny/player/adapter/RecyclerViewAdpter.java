package com.danny.player.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.danny.player.R;

/**
 * Created by dannywang on 2017/12/7.
 */

public class RecyclerViewAdpter extends RecyclerView.Adapter {
//    private Context context;
    private LayoutInflater layoutInflater;

    public RecyclerViewAdpter(Context context){
//        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_item,parent,false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
        recyclerViewHolder.setItemNameText("item postion " + position);
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        private TextView itemNameText;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            itemNameText = (TextView) itemView.findViewById(R.id.recycler_item_name);
        }

        public void setItemNameText(String text){
            itemNameText.setText(text);
        }
    }
}
