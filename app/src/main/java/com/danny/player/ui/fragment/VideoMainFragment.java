package com.danny.player.ui.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.danny.media.library.model.Video;
import com.danny.media.library.service.IServiceUIRefreshListener;
import com.danny.media.library.service.PlayerService;
import com.danny.media.library.service.video.VideoPlayerService;
import com.danny.player.R;
import com.danny.player.ui.widget.RecycleViewDivider;

import java.util.List;

import butterknife.BindView;

/**
 * Created by tingw on 2018/1/23.
 */

public class VideoMainFragment extends BaseFragment implements IServiceUIRefreshListener<Video> {
    @BindView(R.id.video_main_recyclerview)
    RecyclerView mRecyclerView;

    private VideoServiceConnection serviceConnection;
    private PlayerService<Video> playerService;

    @Override
    protected int getLayout() {
        return R.layout.fragment_video_main;
    }

    @Override
    protected void initView() {
        super.initView();
        setToolBarTitle(getString(R.string.player_video));
        setToolBarBackStatue(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL));
        mRecyclerView.setLayoutManager(linearLayoutManager);

        bindVideoService();
    }

    @Override
    public void onDestroyView() {
        unBindVideoService();
        super.onDestroyView();
    }

    private void bindVideoService(){
        serviceConnection = new VideoServiceConnection();
        Intent intent = new Intent(getContext(), VideoPlayerService.class);
        getContext().bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unBindVideoService(){
        if (serviceConnection != null){
            getContext().unbindService(serviceConnection);
            serviceConnection = null;
        }
    }

    private void updateVideoSource(List<Video> videoList){

    }

    @Override
    public void onRefreshSourceList(List songList) {

    }

    @Override
    public void onPublish(Video video, int progress) {

    }

    @Override
    public void onBufferingUpdate(Video video, int percent) {

    }

    @Override
    public void onSourceChange(Video video) {

    }

    public class VideoServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            PlayerService.PlayerBinder playerBinder = (PlayerService.PlayerBinder) iBinder;
            playerService = playerBinder.getPlayerService();
            playerService.registerUIRefreshListener(VideoMainFragment.this);
            List<Video> videos = playerService.getPlaySourceList();
            updateVideoSource(videos);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            if (playerService != null){
                playerService.unRegisterUIRefreshListener(VideoMainFragment.this);
            }
        }

        @Override
        public void onBindingDied(ComponentName name) {

        }
    }
}
