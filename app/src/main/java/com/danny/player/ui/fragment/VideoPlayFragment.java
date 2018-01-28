package com.danny.player.ui.fragment;

import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.danny.media.library.model.Video;
import com.danny.media.library.service.video.VideoPlayerService;
import com.danny.media.library.service.video.VideoSurfaceView;
import com.danny.media.library.service.video.VideoSurfaceViewCreater;
import com.danny.media.library.utils.LogUtil;
import com.danny.player.R;
import com.danny.player.application.PlayerApplication;
import com.danny.player.ui.VideoActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by tingw on 2018/1/25.
 */

public class VideoPlayFragment extends BaseFragment implements VideoSurfaceViewCreater {
    private final static String TAG = VideoPlayFragment.class.getSimpleName();

    @BindView(R.id.video_play_surfaceview)
    VideoSurfaceView videoSurfaceView;

    private VideoPlayerService playerService;
    private Video playVideo;

    @Override
    protected int getLayout() {
        return R.layout.fragment_video_play;
    }

    @Override
    protected void initView() {
        super.initView();

        playerService = PlayerApplication.getApplication().getVideoPlayerService();
        playerService.setSurfaceViewCreater(this);

        Bundle arguments = getArguments();
        playVideo = (Video) arguments.getSerializable(VideoActivity.PARAM_VIDEO);
        if (playVideo != null){
            LogUtil.i(TAG,"title : " + playVideo.getTitle());
            setToolBarTitle(playVideo.getTitle());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Observable.timer(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        playerService.play(playVideo);
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();

        if (playerService != null){
            playerService.stop();
        }
    }

    @Override
    public SurfaceHolder onCreaterSurfaceHolder() {
        return videoSurfaceView.getHolder();
    }
}
