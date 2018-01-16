package com.danny.player.ui.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.danny.media.library.model.Song;
import com.danny.player.R;

/**
 * Created by tingw on 2018/1/16.
 */

public class MusicControllerBar extends LinearLayout implements View.OnClickListener {
    private ImageView musicIcon;
    private TextView musicName;
    private TextView musicArtist;
    private ImageButton playBtn;
    private ImageButton netxtBtn;
    private SeekBar musicSeekBar;

    private MusicControllerBarListener controllerBarListener;

    public MusicControllerBar(Context context) {
        this(context,null);
    }

    public MusicControllerBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MusicControllerBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MusicControllerBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.music_controller_bar,this,true);

        musicIcon = findViewById(R.id.music_icon);
        musicName = findViewById(R.id.music_name);
        musicArtist = findViewById(R.id.music_artist);
        playBtn = findViewById(R.id.music_bar_play);
        netxtBtn = findViewById(R.id.music_bar_next);
        musicSeekBar = findViewById(R.id.music_seekbar);

        playBtn.setOnClickListener(this);
        netxtBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.music_bar_play:{
                if (controllerBarListener != null){
                    controllerBarListener.onPlayOrPauseClick();
                }
                break;
            }
            case R.id.music_bar_next:{
                if (controllerBarListener != null){
                    controllerBarListener.onNextClick();
                }
                break;
            }
        }
    }

    public void setControllerBarListener(MusicControllerBarListener controllerBarListener) {
        this.controllerBarListener = controllerBarListener;
    }

    public void setPlayStatue(boolean play){
        if (play){
            playBtn.setImageResource(R.mipmap.ic_play_bar_btn_pause);
        }else{
            playBtn.setImageResource(R.mipmap.ic_play_bar_btn_play);
        }
    }
    public void setMusicInfo(Song song){
        if (song != null){
            musicName.setText(song.getTitle());
            musicArtist.setText(song.getArtist());
            musicSeekBar.setMax(song.getDuration());
        }
    }

    public void updateMusicProgress(int progress){
        musicSeekBar.setProgress(progress);
    }

    public interface MusicControllerBarListener{
        void onNextClick();
        void onPlayOrPauseClick();
    }
}
