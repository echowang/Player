package com.danny.player.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.danny.media.library.provider.MediaProviderFactory;
import com.danny.media.library.provider.MusicProvider;
import com.danny.media.library.model.Song;
import com.danny.media.library.utils.LogUtil;
import com.danny.player.R;
import com.danny.player.glide.GlideApp;
import com.danny.player.glide.GlideRoundTransform;
import com.danny.player.glide.PlayerGlide;

import java.io.File;
import java.util.List;

/**
 * Created by dannywang on 2017/12/7.
 */

public class MusicListAdpter extends RecyclerView.Adapter {
    private final static String TAG = MusicListAdpter.class.getSimpleName();
    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<Song> songList;

    private int selectedPosition = 0;
    private boolean isPlaying = true;

    private OnMusicItemClick onMusicItemClick;

    public MusicListAdpter(Context context){
        this.mContext = context;
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

    public void setSongList(List<Song> songList,int selectedPosition,boolean isPlaying){
        if (songList == null){
            return;
        }
        this.songList = songList;
        this.selectedPosition = selectedPosition;
        this.isPlaying = isPlaying;
        notifyDataSetChanged();
    }

    public void updateSelectedItem(int position){
        if (songList == null || position < 0 || position >= songList.size() || position == selectedPosition){
            return;
        }

        isPlaying = true;
        selectedPosition = position;
        notifyDataSetChanged();
    }

    public void updatePlayAnimationStatue(boolean playing){
        isPlaying = playing;
        notifyDataSetChanged();
    }

    public int updateSelectedItem(Song song){
        if (song != null && songList != null){
            int position = songList.indexOf(song);
            updateSelectedItem(position);

            return position;
        }

        return -1;
    }

    public void setOnMusicItemClick(OnMusicItemClick onMusicItemClick) {
        this.onMusicItemClick = onMusicItemClick;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        private View container;
        private ImageView itemAlbumIcon;
        private TextView itemNameText;
        private TextView itemDescText;
        private ImageView itemAnimation;

        private MusicProvider musicProvider;


        public RecyclerViewHolder(View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.recycler_item_container);
            itemAlbumIcon = itemView.findViewById(R.id.item_music_album_icon);
            itemNameText = itemView.findViewById(R.id.item_music_name);
            itemDescText = itemView.findViewById(R.id.item_music_describe);
            itemAnimation = itemView.findViewById(R.id.item_music_play_animation);

            musicProvider = MediaProviderFactory.getInstance().getMusciProvideo(mContext);
        }

        public void itemBindSong(final int position,final Song song){
            if (song == null){
                return;
            }

            if (position == selectedPosition){
                container.setBackgroundResource(R.drawable.recyclerview_item_selected_selector);

                itemAnimation.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AnimationDrawable animator = (AnimationDrawable) itemAnimation.getDrawable();
                        if (isPlaying){
                            animator.start();
                        }else{
                            animator.stop();
                        }
                    }
                },300);

                itemAnimation.setVisibility(View.VISIBLE);
            }else{
                container.setBackgroundResource(R.drawable.recyclerview_item_selector);
                itemAnimation.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AnimationDrawable animator = (AnimationDrawable) itemAnimation.getDrawable();
                        animator.stop();
                    }
                },300);

                itemAnimation.setVisibility(View.GONE);
            }

            Uri albumUri = musicProvider.getAlbumPath(mContext,song);
            if (albumUri == null){
                PlayerGlide.loadImage(mContext,R.mipmap.default_artist,itemAlbumIcon);
            }else{
                LogUtil.i(TAG,song.getTitle() + " : " + albumUri.getPath());
                PlayerGlide.loadLocalRoundImage(mContext,R.mipmap.default_artist,R.mipmap.default_artist,albumUri.getPath(),6,itemAlbumIcon);
            }
            itemNameText.setText(song.getTitle());
            itemDescText.setText(song.getArtist() + "-" + song.getAlbum());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LogUtil.d(TAG,"onClick");
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
