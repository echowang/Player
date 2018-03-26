package com.danny.player.ui.fragment;

import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.danny.media.library.player.IServiceUIRefreshListener;
import com.danny.media.library.video.model.Video;
import com.danny.media.library.video.player.VideoPlayerService;
import com.danny.media.library.video.player.VideoSurfaceViewCreater;
import com.danny.media.library.utils.LogUtil;
import com.danny.player.R;
import com.danny.player.application.PlayerApplication;
import com.danny.player.ui.VideoActivity;

import java.util.List;

import butterknife.BindView;

/**
 * Created by tingw on 2018/1/25.
 */

public class VideoPlayFragment extends BaseFragment implements VideoSurfaceViewCreater,IServiceUIRefreshListener<Video> {
    private final static String TAG = VideoPlayFragment.class.getSimpleName();

    @BindView(R.id.video_play_surfaceview)
    SurfaceView videoSurfaceView;

    private VideoPlayerService playerService;

    private Video video;

    @Override
    protected int getLayout() {
        return R.layout.fragment_video_play;
    }

    @Override
    protected void initView() {
        super.initView();

        playerService = PlayerApplication.getApplication().getVideoPlayerService();
        playerService.setSurfaceViewCreater(this);
        playerService.registerUIRefreshListener(this);

        Bundle arguments = getArguments();
        video = (Video) arguments.getSerializable(VideoActivity.PARAM_VIDEO);

        videoSurfaceView.getHolder().setKeepScreenOn(true);
        videoSurfaceView.getHolder().addCallback(new VideoSurfaceHolder());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        playerService.unRegisterUIRefreshListener(this);
    }

    @Override
    public SurfaceHolder onCreaterSurfaceHolder() {
        return videoSurfaceView.getHolder();
    }

    @Override
    public void onRefreshSourceList(List<Video> songList) {

    }

    @Override
    public void onPublish(Video video, int progress) {
        LogUtil.i(TAG,"video : " + video.getTitle() + " , progress : " + progress);
    }

    @Override
    public void onBufferingUpdate(Video video, int percent) {

    }

    @Override
    public void onSourceChange(Video video) {

    }

    public class VideoSurfaceHolder implements SurfaceHolder.Callback{

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            LogUtil.i(TAG,"surfaceCreated");
            if (video != null){
                LogUtil.i(TAG,"title : " + video.getTitle());
                setToolBarTitle(video.getTitle());

                playerService.play(video);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            LogUtil.i(TAG,"surfaceChanged");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            LogUtil.i(TAG,"surfaceDestroyed");
            playerService.stop();
        }
    }
}
