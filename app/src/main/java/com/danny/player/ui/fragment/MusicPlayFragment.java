package com.danny.player.ui.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.danny.media.library.broadcast.AudioVolumeBroadcast;
import com.danny.media.library.provider.MediaProviderFactory;
import com.danny.media.library.provider.MusicProvider;
import com.danny.media.library.model.Lrc;
import com.danny.media.library.model.Song;
import com.danny.media.library.service.PlayerModel;
import com.danny.media.library.service.PlayerService;
import com.danny.media.library.utils.LogUtil;
import com.danny.player.R;
import com.danny.player.adapter.PlayPagerAdapter;
import com.danny.player.application.PlayerApplication;
import com.danny.player.glide.PlayerGlide;
import com.danny.player.ui.widget.MusicAlbumCoverView;
import com.danny.player.ui.widget.MusicPlayControllerBar;
import com.tmall.ultraviewpager.UltraViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.wcy.lrcview.LrcView;

/**
 * Created by tingw on 2018/1/17.
 */
public class MusicPlayFragment extends BaseFragment implements PlayerService.IServiceUIRefreshListener<Song>, MusicPlayControllerBar.MusicPlayControllerBarListener,AudioVolumeBroadcast.OnAudioVolumeChangeListener {
    private final static String TAG = MusicPlayFragment.class.getSimpleName();

    @BindView(R.id.music_play_container_bg)
    ImageView containerBgView;
    @BindView(R.id.music_play_controller_bar)
    MusicPlayControllerBar musicPlayControllerBar;
    @BindView(R.id.music_play_viewpager)
    UltraViewPager musicPlayViewPager;

    private List<View> mViewPagerContent;
    private MusicAlbumCoverView albumCoverView;
    private LrcView mLrcViewSingle;

    private SeekBar volumeSeekBar;
    private LrcView mLrcViewFull;

    private AudioManager audioManager;
    private AudioVolumeBroadcast audioVolumeBroadcast;

    private PlayerService<Song> playerService;

    @Override
    protected int getLayout() {
        return R.layout.fragment_music_play;
    }

    @Override
    protected void initView() {
        super.initView();
        setToolBarBackStatue(true);

        initViewPager();
        initViewEvent();
        initVolumeBar();

        playerService = PlayerApplication.getApplication().getMusicPlayerService();
        playerService.setUIRefreshListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        audioVolumeBroadcast = AudioVolumeBroadcast.registerAudioVolumeBroadcast(getContext(),this);
    }

    @Override
    public void onResume() {
        super.onResume();

        Song song = playerService.getPlaySource();
        updateMusicInfo(song);

        musicPlayControllerBar.updateModelStatue(playerService.getPlayerModel());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AudioVolumeBroadcast.unRegisterAudioVolumeBroadcast(getContext(),audioVolumeBroadcast);
    }

    private void initViewPager(){
        PlayerGlide.loadLocalBlurImage(getContext(),R.mipmap.play_default_bg,4,3,containerBgView);
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View coverView = layoutInflater.inflate(R.layout.fragment_play_page_cover, null);
        View lrcView = layoutInflater.inflate(R.layout.fragment_play_page_lrc,null);
        albumCoverView = coverView.findViewById(R.id.album_cover_view);
        mLrcViewSingle = coverView.findViewById(R.id.lrc_view_single);
        volumeSeekBar = lrcView.findViewById(R.id.sb_volume);
        mLrcViewFull = lrcView.findViewById(R.id.lrc_view_full);

        mViewPagerContent = new ArrayList<>(2);
        mViewPagerContent.add(coverView);
        mViewPagerContent.add(lrcView);

        musicPlayViewPager.initIndicator();
        musicPlayViewPager.getIndicator()
                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                .setFocusColor(Color.GREEN)
                .setNormalColor(Color.WHITE)
                .setRadius((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()))
                .setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        musicPlayViewPager.getIndicator()
                .build();
        musicPlayViewPager.setAdapter(new PlayPagerAdapter(mViewPagerContent));
    }

    private void initViewEvent(){
        musicPlayControllerBar.setPlayControllerBarListener(this);
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                LogUtil.i(TAG,"volume : " + seekBar.getProgress());
                if (audioManager != null){
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,seekBar.getProgress(),AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                }
            }
        });
    }

    private void initVolumeBar(){
        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
//        LogUtil.i(TAG,"volume max : " + audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
//        LogUtil.i(TAG,"volume : " + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        volumeSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
    }

    private void updateMusicInfo(Song song){
        if (song == null){
            return;
        }
        setToolBarTitle(song.getTitle());

        MusicProvider musicProvider = MediaProviderFactory.getInstance().getMusciProvideo(getContext());
        Lrc lrc = musicProvider.findLrcBySong(song);
        if (lrc != null){
            LogUtil.i(TAG,"lrc type : " + lrc.getType());
            if (Lrc.LrcType.LOCAL == lrc.getType()){
                File lrcFile = lrc.getLrcFile();
                if (lrcFile.exists()){
                    LogUtil.i(TAG,"lrc path : " + lrcFile.getPath());
                    mLrcViewSingle.loadLrc(lrcFile);
                    mLrcViewFull.loadLrc(lrcFile);
                }
            }
        }
        Bitmap albumBitmap = musicProvider.getAlbumImage(getContext(),song);
        if (albumBitmap == null){
            albumBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.default_artist);
        }
        albumCoverView.setCoverBitmap(albumBitmap);
        if (playerService.isPlaying()){
            albumCoverView.startAnimation();
        }else{
            albumCoverView.stopAnimation();
        }

        musicPlayControllerBar.updatePlaySource(song,playerService.getPlayProgress());
        musicPlayControllerBar.updatePlayStatue(playerService.isPlaying());
    }

    //MusicPlayControllerBarListener
    @Override
    public void onPlayOrPauseClick(boolean isPlay) {
        if (isPlay){
            playerService.resume();
            albumCoverView.startAnimation();
        }else{
            playerService.pause();
            albumCoverView.stopAnimation();
        }
    }

    @Override
    public void onNextClick() {
        playerService.next();
    }

    @Override
    public void onPrevClick() {
        playerService.prev();
    }

    @Override
    public void onModelClick() {
        PlayerModel model = playerService.getPlayerModel();
        PlayerModel changeModel = model;
        switch (model){
            case SEQUENCE:{
                changeModel = PlayerModel.RANDOM;
                break;
            }
            case SINGLE:{
                changeModel = PlayerModel.SEQUENCE;
                break;
            }
            case RANDOM:{
                changeModel = PlayerModel.SINGLE;
                break;
            }
        }
        musicPlayControllerBar.updateModelStatue(changeModel);
        playerService.setPlayerModel(changeModel);
    }

    @Override
    public void onSeekBarChange(int progress) {
        playerService.seekTo(progress);
    }

    //IServiceUIRefreshListener
    @Override
    public void onRefreshMusicList(List<Song> songList) {

    }

    @Override
    public void onPublish(Song song, int progress) {
        musicPlayControllerBar.updatePlayProgress(progress);
        if (mLrcViewSingle.hasLrc()){
            mLrcViewSingle.updateTime(progress);
        }
        if (mLrcViewFull.hasLrc()){
            mLrcViewFull.updateTime(progress);
        }
    }

    @Override
    public void onBufferingUpdate(Song song, int percent) {

    }

    @Override
    public void onMusicChange(Song song) {
        updateMusicInfo(song);
    }

    @Override
    public void onChange(int volume) {
        if (volume >= 0){
            volumeSeekBar.setProgress(volume);
        }
    }
}
