package com.danny.media.library.service.video;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.danny.media.library.model.Video;
import com.danny.media.library.provider.MediaProviderFactory;
import com.danny.media.library.provider.MediaProviderListener;
import com.danny.media.library.provider.video.VideoProvider;
import com.danny.media.library.service.PlayerModel;
import com.danny.media.library.service.PlayerScheduleListener;
import com.danny.media.library.service.PlayerService;
import com.danny.media.library.utils.LogUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by dannywang on 2017/12/29.
 */

public class VideoPlayerService extends PlayerService<Video> implements PlayerScheduleListener<Video> {
    private final static String TAG = VideoPlayerService.class.getSimpleName();

    private VideoProvider videoProvider;
    private VideoPlayer videoPlayer;

    private PlayerModel playerModel = PlayerModel.SEQUENCE;

    private List<Video> videoList;

    @Override
    public void onCreate() {
        super.onCreate();

        initVideoPlayer();
    }

    private void initVideoPlayer(){
        videoProvider = MediaProviderFactory.getInstance().getVideoProvider(this);
        videoPlayer = new VideoPlayer(this);

        videoProvider.setMediaProviderListener(new MediaProviderListener() {
            @Override
            public void onStartScan() {
                LogUtil.i(TAG,"onStartScan");
            }

            @Override
            public void onScanFinish() {
                LogUtil.i(TAG,"onScanFinish");
                Observable.just(1)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                videoList = videoProvider.getMediaList();
                                if (uiRefreshListener != null){
                                    uiRefreshListener.onRefreshSourceList(videoList);
                                }
                            }
                        });

            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Observable.timer(1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        videoProvider.loadMediaResources();
                    }
                });
        return super.onBind(intent);
    }

    //    PlayerService
    @Override
    public List<Video> getPlaySourceList() {
        return null;
    }

    @Override
    public Video getPlaySource() {
        return null;
    }

    @Override
    public void play(Video video) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void next() {

    }

    @Override
    public void prev() {

    }

    @Override
    public void seekTo(int mesc) {
        videoPlayer.seekTo(mesc);
    }

    @Override
    public int getPlayProgress() {
        return videoPlayer.getPlayProgress();
    }

    @Override
    public boolean isPlaying() {
        return videoPlayer.isPlaying();
    }

    @Override
    public boolean isAutoPlay() {
        return false;
    }

    @Override
    public void setPlayerModel(PlayerModel model) {
        playerModel = model;
    }

    @Override
    public PlayerModel getPlayerModel() {
        return playerModel;
    }

//    PlayerScheduleListener
    @Override
    public void onPublish(int progress) {

    }

    @Override
    public void onBufferingUpdate(int percent) {

    }

    @Override
    public void OnCompletion() {

    }

    @Override
    public void OnChangeSource(Video video) {

    }
}
