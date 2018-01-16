package com.danny.player.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.danny.media.library.model.Song;
import com.danny.player.R;

import java.util.List;

/**
 * Created by dannywang on 2017/12/7.
 */

public class MusicListAdpter extends RecyclerView.Adapter {
    private final static String TAG = MusicListAdpter.class.getSimpleName();
    private LayoutInflater layoutInflater;
    private List<Song> songList;

    private int selectedPosition = 0;

    private OnMusicItemClick onMusicItemClick;

    public MusicListAdpter(Context context){
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

        Song song = getItem(position);
        recyclerViewHolder.itemBindSong(position,song);
    }

    @Override
    public int getItemCount() {
        return songList == null ? 0 : songList.size();
    }

    private Song getItem(int position){
        if (songList != null && position >= 0 && position < songList.size()){
            return songList.get(position);
        }
        return null;
    }

    public void setSongList(List<Song> songList){
        if (songList == null){
            return;
        }
        this.songList = songList;
        notifyDataSetChanged();
    }

    public void updateSelectedItem(int position){
        if (songList == null || position < 0 || position >= songList.size() || position == selectedPosition){
            return;
        }

        selectedPosition = position;
        notifyDataSetChanged();
    }

    public void updateSelectedItem(Song song){
        if (song != null && songList != null){
            int position = songList.indexOf(song);
            updateSelectedItem(position);
        }
    }

    public void setOnMusicItemClick(OnMusicItemClick onMusicItemClick) {
        this.onMusicItemClick = onMusicItemClick;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        private View container;
        private TextView itemNameText;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.recycler_item_container);
            itemNameText = itemView.findViewById(R.id.recycler_item_name);
        }

        public void itemBindSong(final int position,final Song song){
            if (song == null){
                return;
            }

            if (position == selectedPosition){
                container.setBackgroundResource(R.drawable.recyclerview_item_selected_selector);
            }else{
                container.setBackgroundResource(R.drawable.recyclerview_item_selector);
            }
            itemNameText.setText(song.getTitle());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG,"onClick");
                    if (onMusicItemClick != null){
                        onMusicItemClick.onMusicItenClick(position,song);
                    }
                }
            });
        }
    }

    public interface OnMusicItemClick{
        void onMusicItenClick(int position,Song song);
    }
}
