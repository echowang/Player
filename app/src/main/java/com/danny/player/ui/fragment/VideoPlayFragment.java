package com.danny.player.ui.fragment;

import android.os.Bundle;

import com.danny.media.library.model.Video;
import com.danny.media.library.service.video.VideoSurfaceView;
import com.danny.media.library.utils.LogUtil;
import com.danny.player.R;
import com.danny.player.ui.VideoActivity;

import butterknife.BindView;

/**
 * Created by tingw on 2018/1/25.
 */

public class VideoPlayFragment extends BaseFragment {
    private final static String TAG = VideoPlayFragment.class.getSimpleName();

    @BindView(R.id.video_play_surfaceview)
    VideoSurfaceView videoSurfaceView;

    @Override
    protected int getLayout() {
        return R.layout.fragment_video_play;
    }

    @Override
    protected void initView() {
        super.initView();

        Bundle arguments = getArguments();
        Video video = (Video) arguments.getSerializable(VideoActivity.PARAM_VIDEO);
        if (video != null){
            LogUtil.i(TAG,"title : " + video.getTitle());
            setToolBarTitle(video.getTitle());
        }
    }
}
