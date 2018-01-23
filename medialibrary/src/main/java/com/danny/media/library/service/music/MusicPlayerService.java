package com.danny.media.library.service.music;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;

import com.danny.media.library.provider.MediaProviderFactory;
import com.danny.media.library.provider.MediaProviderListener;
import com.danny.media.library.provider.music.MusicProvider;
import com.danny.media.library.model.Song;
import com.danny.media.library.service.AudioFocusManager;
import com.danny.media.library.service.PlayerModel;
import com.danny.media.library.service.PlayerScheduleListener;
import com.danny.media.library.service.PlayerService;
import com.danny.media.library.utils.LogUtil;

import java.io.File;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by dannywang on 2017/12/29.
 */

public class MusicPlayerService extends PlayerService<Song> implements PlayerScheduleListener<Song> {
    private final static String TAG = MusicPlayerService.class.getSimpleName();

    private MusicProvider musicProvider;
    private MusicPlayer musicPlayer;
    private List<Song> songList;
    private int playSongPosition;
    private boolean autoPlay = true;

    private PlayerModel playerModel = PlayerModel.SEQUENCE;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.i(TAG,"onCreate");

        initMusicPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.i(TAG,"onStartCommand");

        MediaScannerConnection.scanFile(this,
                new String[] { Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Observable.just(1)
                                .subscribeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Integer>() {
                                    @Override
                                    public void accept(Integer integer) throws Exception {
                                        if (musicProvider != null){
                                            musicProvider.loadMediaResources();
                                        }
                                    }
                                });
                    }
                });

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        LogUtil.i(TAG,"onDestroy");

        AudioFocusManager.getInstance().abanodAudioFocus(this);
        super.onDestroy();
    }

    private void initMusicPlayer(){
        musicProvider = MediaProviderFactory.getInstance().getMusciProvideo(this);
        musicProvider.setMediaProviderListener(new MediaProviderListener() {
            @Override
            public void onStartScan() {
                LogUtil.i(TAG,"onStartScan");
            }

            @Override
            public void onScanFinish() {
                LogUtil.i(TAG,"onScanFinish");
                songList = musicProvider.getMusicList();
                if (uiRefreshListener != null){
                    uiRefreshListener.onRefreshSourceList(songList);
                }
            }
        });

        AudioFocusManager.getInstance().requestAudioFocus(this);
        AudioFocusManager.getInstance().getObservable().subscribe(new Observer<Boolean>() {
            private boolean needPlay;

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean focus) {
                if (focus){
                    if (musicPlayer != null){
                        if (musicPlayer.isPausing() && needPlay){
                            needPlay = false;
                            resume();
                        }
                    }
                }else{
                    if (musicPlayer != null && musicPlayer.isPlaying()){
                        needPlay = true;
                        pause();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        musicPlayer = new MusicPlayer(this);
    }

    //    PlayerService
    /**
     * 设置当前的播放模式
     * @param model
     */
    @Override
    public void setPlayerModel(PlayerModel model){
        this.playerModel = model;
    }

    /**
     * 读取当前的播放模式
     * @return
     */
    @Override
    public PlayerModel getPlayerModel() {
        return playerModel;
    }

    @Override
    public List<Song> getPlaySourceList() {
        return songList;
    }

    @Override
    public Song getPlaySource() {
        return musicPlayer.getPlaySource();
    }

    @Override
    public void play(Song song) {
        if (musicPlayer.isPlaying()){
            musicPlayer.stop();
        }
        playSongPosition = songList.indexOf(song);
        musicPlayer.play(song);
    }

    @Override
    public void pause() {
        if (musicPlayer.isPlaying()){
            autoPlay = false;
            musicPlayer.pause();
        }

    }

    @Override
    public void resume() {
        if (musicPlayer.isPausing()){
            autoPlay = true;
            musicPlayer.resume();
        }
    }

    @Override
    public void stop() {
        musicPlayer.stop();
    }

    @Override
    public void next() {
        switch (playerModel){
            case RANDOM:{
                Random random = new Random();
                int randomInt = random.nextInt(songList.size());
                while (randomInt == playSongPosition){
                    randomInt = random.nextInt(songList.size());
                }
                playSongPosition = randomInt;
                break;
            }
            case SINGLE:{
                break;
            }
            case SEQUENCE:{
                int max  = songList.size() - 1;
                if (playSongPosition == max){
                    playSongPosition = 0;
                }else if(playSongPosition >= 0 && playSongPosition < max){
                    playSongPosition++;
                }else{
                    playSongPosition = 0;
                }
                break;
            }
        }
        Song song = songList.get(playSongPosition);
        musicPlayer.play(song);
    }

    @Override
    public void prev() {
        switch (playerModel){
            case SEQUENCE:{
                int max  = songList.size() - 1;
                if (playSongPosition == 0){
                    playSongPosition = max;
                }else if (playSongPosition > 0 && playSongPosition <= max){
                    playSongPosition--;
                }else{
                    playSongPosition = 0;
                }
                break;
            }
            case SINGLE:{
                break;
            }
            case RANDOM:{
                Random random = new Random();
                int randomInt = random.nextInt(songList.size());
                while (randomInt == playSongPosition){
                    randomInt = random.nextInt(songList.size());
                }
                playSongPosition = randomInt;
                break;
            }
        }
        Song song = songList.get(playSongPosition);
        musicPlayer.play(song);
    }

    @Override
    public void seekTo(int progress) {
        if (progress >= 0){
            musicPlayer.seekTo(progress);
        }
    }

    @Override
    public int getPlayProgress() {
        return musicPlayer.getPlayProgress();
    }

    @Override
    public boolean isPlaying() {
        return musicPlayer.isPlaying();
    }

    @Override
    public boolean isAutoPlay() {
        return autoPlay;
    }

//    PlayerScheduleListener
    @Override
    public void onPublish(int progress) {
        if (uiRefreshListener != null){
            Song song = musicPlayer.getPlaySource();
            uiRefreshListener.onPublish(song,progress);
        }
    }

    @Override
    public void onBufferingUpdate(int percent) {
        if (uiRefreshListener != null){
            Song song = musicPlayer.getPlaySource();
            uiRefreshListener.onBufferingUpdate(song,percent);
        }
    }

    @Override
    public void OnCompletion() {
        next();
    }

    @Override
    public void OnChangeSource(Song song) {
        if (uiRefreshListener != null){
            uiRefreshListener.onSourceChange(song);
        }
    }
}
