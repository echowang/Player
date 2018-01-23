package com.danny.media.library.service.video;

import com.danny.media.library.model.Video;
import com.danny.media.library.provider.MediaProviderFactory;
import com.danny.media.library.provider.MediaProviderListener;
import com.danny.media.library.provider.video.VideoProvider;
import com.danny.media.library.service.PlayerModel;
import com.danny.media.library.service.PlayerScheduleListener;
import com.danny.media.library.service.PlayerService;
import com.danny.media.library.utils.LogUtil;

import java.util.List;

/**
 * Created by dannywang on 2017/12/29.
 */

public class VideoPlayerService extends PlayerService<Video> implements PlayerScheduleListener<Video> {
    private final static String TAG = VideoPlayerService.class.getSimpleName();

    private VideoProvider videoProvider;
    private VideoPlayer videoPlayer;

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
                videoList = videoProvider.getMediaList();
                if (uiRefreshListener != null){
                    uiRefreshListener.onRefreshSourceList(videoList);
                }
            }
        });
        videoProvider.loadMediaResources();
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
    public void seekTo(int progress) {

    }

    @Override
    public int getPlayProgress() {
        return 0;
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public boolean isAutoPlay() {
        return false;
    }

    @Override
    public void setPlayerModel(PlayerModel model) {

    }

    @Override
    public PlayerModel getPlayerModel() {
        return null;
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
