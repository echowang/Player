package com.danny.player.ui.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.danny.media.library.model.Song;
import com.danny.media.library.service.MusicPlayerService;
import com.danny.player.R;
import com.danny.player.adapter.MusicListAdpter;
import com.danny.player.ui.widget.RecycleViewDivider;

import java.util.List;

/**
 * Created by tingw on 2018/1/15.
 */

public class MusicMainFragment extends BaseFragment implements MusicPlayerService.IMusicUIRefreshListener,MusicListAdpter.OnMusicItemClick {
    private final static String TAG = MusicMainFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MusicListAdpter musicListAdpter;

    private PlayerServiceConnection playerServiceConnection;
    private MusicPlayerService.MusicPlayerBinder playerBinder;

    @Override
    protected int getLayout() {
        return R.layout.fragment_music_main;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        Log.d(TAG,"initView");
        mRecyclerView = view.findViewById(R.id.main_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL));
        mRecyclerView.setLayoutManager(linearLayoutManager);
        musicListAdpter = new MusicListAdpter(getContext());
        musicListAdpter.setOnMusicItemClick(this);
        mRecyclerView.setAdapter(musicListAdpter);

        bindService();
    }

    @Override
    public void onDestroyView() {
        if (playerServiceConnection != null){
            getContext().unbindService(playerServiceConnection);
        }
        super.onDestroyView();
    }

    private void setMusicListData(List<Song> songList){
        Log.i(TAG,"setMusicListData");
        if (songList == null && !songList.isEmpty()){
            return;
        }

        musicListAdpter.setSongList(songList);

        if (!songList.isEmpty() && !playerBinder.isPlaying()){
            Song song = songList.get(0);
            playerBinder.play(song);
        }

    }

    private void bindService(){
        playerServiceConnection = new MusicMainFragment.PlayerServiceConnection();
        Intent intent = new Intent(getContext(), MusicPlayerService.class);
        getContext().bindService(intent,playerServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onRefreshMusicList(List<Song> songList) {
        setMusicListData(songList);
    }

    @Override
    public void onPublish(Song song, int progress) {
        Log.d(TAG,"song " + song.getTitle() + " , progress : " + progress);
    }

    @Override
    public void onBufferingUpdate(Song song, int percent) {
        Log.d(TAG,"song " + song.getTitle() + " , percent : " + percent);
    }

    @Override
    public void onMusicChange(Song song) {

    }

    @Override
    public void onClick(int position, Song song) {
        Log.d(TAG,"onClick : " + position);
        if (song != null){
            playerBinder.play(song);
        }
    }

    private class PlayerServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(TAG,"onServiceConnected");
            if (iBinder != null && iBinder instanceof MusicPlayerService.MusicPlayerBinder){
                playerBinder = (MusicPlayerService.MusicPlayerBinder) iBinder;
                playerBinder.setIMusicUIRefreshListener(MusicMainFragment.this);
                List<Song> songList = playerBinder.getMusicList();
                setMusicListData(songList);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            playerBinder = null;
        }

        @Override
        public void onBindingDied(ComponentName name) {
            playerBinder = null;
        }
    }
}
