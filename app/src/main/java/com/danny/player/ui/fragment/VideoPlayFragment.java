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

import butterknife.BindView;

/**
 * Created by tingw on 2018/1/25.
 */

public class VideoPlayFragment extends BaseFragment implements VideoSurfaceViewCreater {
    private final static String TAG = VideoPlayFragment.class.getSimpleName();

    @BindView(R.id.video_play_surfaceview)
    VideoSurfaceView videoSurfaceView;

    private VideoPlayerService playerService;

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
        Video video = (Video) arguments.getSerializable(VideoActivity.PARAM_VIDEO);
        if (video != null){
            LogUtil.i(TAG,"title : " + video.getTitle());
            setToolBarTitle(video.getTitle());

            playerService.play(video);
        }
    }

    @Override
    public SurfaceHolder onCreaterSurfaceHolder() {
        return videoSurfaceView.getHolder();
    }
}
