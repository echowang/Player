package com.danny.player.ui.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.danny.media.library.model.Song;
import com.danny.media.library.utils.StringUtil;
import com.danny.player.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tingw on 2018/1/18.
 */
public class MusicPlayControllerBar extends LinearLayout implements View.OnClickListener {
    @BindView(R.id.tv_current_time)
    TextView playTime;
    @BindView(R.id.tv_total_time)
    TextView totalTime;
    @BindView(R.id.sb_progress)
    SeekBar seekBar;
    @BindView(R.id.iv_mode)
    ImageView playModel;
    @BindView(R.id.iv_prev)
    ImageView prevBtn;
    @BindView(R.id.iv_play)
    ImageView playBtn;
    @BindView(R.id.iv_next)
    ImageView nextBtn;

    private MusicPlayControllerBarListener playControllerBarListener;

    public MusicPlayControllerBar(Context context) {
        this(context,null);
    }

    public MusicPlayControllerBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MusicPlayControllerBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MusicPlayControllerBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView(){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.music_play_controller_bar,this,true);
        ButterKnife.bind(this,view);

        initViewEvent();
    }

    private void initViewEvent(){
        playBtn.setOnClickListener(this);
        prevBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
    }

    /**
     * 更新当前正在播放的歌曲
     * @param song
     * @param progress   播放的进度
     */
    public void updatePlaySource(Song song,int progress){
        if (song == null){
            return;
        }

        seekBar.setMax(song.getDuration());
        seekBar.setProgress(progress);

        playTime.setText(StringUtil.durationToTimeString(progress));
        totalTime.setText(StringUtil.durationToTimeString(song.getDuration()));
    }

    public void updatePlayStatue(boolean play){
        playBtn.setSelected(play);
    }

    public void updatePlayProgress(int progress){
        seekBar.setProgress(progress);
        playTime.setText(StringUtil.durationToTimeString(progress));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_play:{
                if (playControllerBarListener != null){
                    v.setSelected(!v.isSelected());
                    playControllerBarListener.onPlayOrPauseClick(v.isSelected());
                }
                break;
            }
            case R.id.iv_prev:{
                if (playControllerBarListener != null){
                    playControllerBarListener.onPrevClick();
                }
                break;
            }
            case R.id.iv_next:{
                if (playControllerBarListener != null){
                    playControllerBarListener.onNextClick();
                }
                break;
            }
        }
    }

    public void setPlayControllerBarListener(MusicPlayControllerBarListener playControllerBarListener){
        this.playControllerBarListener = playControllerBarListener;
    }

    public interface MusicPlayControllerBarListener{
        void onPlayOrPauseClick(boolean isPlay);
        void onNextClick();
        void onPrevClick();
    }
}