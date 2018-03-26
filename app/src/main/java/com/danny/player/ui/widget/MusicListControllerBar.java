package com.danny.player.ui.widget;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.danny.media.library.provider.MediaProviderFactory;
import com.danny.media.library.music.provider.MusicProvider;
import com.danny.media.library.music.model.Song;
import com.danny.media.library.utils.LogUtil;
import com.danny.player.R;
import com.danny.player.glide.PlayerGlide;

/**
 * Created by tingw on 2018/1/16.
 */

public class MusicListControllerBar extends LinearLayout implements View.OnClickListener {
    private final static String TAG = MusicListControllerBar.class.getSimpleName();

    private View container;
    private ImageView musicIcon;
    private TextView musicName;
    private TextView musicArtist;
    private ImageButton playBtn;
    private ImageButton netxtBtn;
    private ProgressBar musicSeekBar;

    private MusicControllerBarListener controllerBarListener;

    public MusicListControllerBar(Context context) {
        this(context,null);
    }

    public MusicListControllerBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MusicListControllerBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MusicListControllerBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.music_list_controller_bar,this,true);

        container = findViewById(R.id.music_controller_bar);
        musicIcon = findViewById(R.id.music_album_icon);
        musicName = findViewById(R.id.music_name);
        musicArtist = findViewById(R.id.music_artist);
        playBtn = findViewById(R.id.music_bar_play);
        netxtBtn = findViewById(R.id.music_bar_next);
        musicSeekBar = findViewById(R.id.music_seekbar);

        container.setOnClickListener(this);
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
            case R.id.music_controller_bar:{
                if (controllerBarListener != null){
                    controllerBarListener.OnControllerBarClick();
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

            MusicProvider musicProvider = MediaProviderFactory.getInstance().getMusciProvideo(getContext());
            Uri albumUri = musicProvider.getAlbumPath(getContext(),song);
            if (albumUri == null){
                PlayerGlide.loadImage(getContext(),R.mipmap.default_artist,musicIcon);
            }else {
                PlayerGlide.loadLocalRoundImage(getContext(),R.mipmap.default_artist,R.mipmap.default_artist,albumUri.getPath(),6,musicIcon);
            }
        }
    }

    public void updateMusicProgress(int progress){
        LogUtil.i(TAG,"progress : " + progress);
        musicSeekBar.setProgress(progress);
    }

    public interface MusicControllerBarListener{
        void onNextClick();
        void onPlayOrPauseClick();
        void OnControllerBarClick();
    }
}
