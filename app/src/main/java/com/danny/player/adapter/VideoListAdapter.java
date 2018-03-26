package com.danny.player.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.danny.media.library.video.model.Video;
import com.danny.media.library.utils.StringUtil;
import com.danny.player.R;
import com.danny.player.glide.PlayerGlide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tingw on 2018/1/24.
 */

public class VideoListAdapter extends RecyclerView.Adapter {
    private Context context;
    private LayoutInflater inflater;

    private List<Video> videoList = new ArrayList<>();

    private OnItemClickListener<Video> onItemClickListener;

    public VideoListAdapter(Context context){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.video_list_item,parent,false);
        VideoListViewHolder viewHolder = new VideoListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VideoListViewHolder){
            Video video = getItem(position);
            VideoListViewHolder viewHolder = (VideoListViewHolder) holder;
            viewHolder.bindVideo(video,position);
        }
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public Video getItem(int position){
        if (position >= videoList.size()){
            return null;
        }
        return videoList.get(position);
    }

    public void updateVideoList(List<Video> videoList){
        if (videoList != null){
            this.videoList.clear();
            this.videoList.addAll(videoList);
            notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(OnItemClickListener<Video> onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public class VideoListViewHolder extends RecyclerView.ViewHolder{
        private ImageView thumbnailImage;
        private TextView nameText;
        private TextView durationText;

        public VideoListViewHolder(View itemView) {
            super(itemView);
            thumbnailImage = itemView.findViewById(R.id.video_item_thumbnail);
            nameText = itemView.findViewById(R.id.video_item_name);
            durationText = itemView.findViewById(R.id.video_item_duration);
        }

        public void bindVideo(final Video video, final int position){
            if (video == null){
                return;
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null){
                        onItemClickListener.onItenClick(position,video);
                    }
                }
            });

            if (!TextUtils.isEmpty(video.getTitle())){
                nameText.setText(video.getTitle());
            }

            String duration = StringUtil.durationToTimeString(video.getDuration());
            if (!TextUtils.isEmpty(duration)){
                durationText.setText(duration);
            }

            PlayerGlide.loadLocalRoundImage(context,R.mipmap.default_artist,R.mipmap.default_artist,video.getThumbnail() ,6,thumbnailImage);
        }
    }
}
