package com.danny.player.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.danny.media.library.model.Song;
import com.danny.media.library.service.MusicPlayerService;
import com.danny.player.R;
import com.danny.player.adapter.MusicListAdpter;

import java.util.List;

/**
 * Created by tingw on 2018/1/3.
 */

public class MainActivity extends BaseAcivity implements MusicPlayerService.IMusicUIRefreshListener {
    private final static String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MusicListAdpter musicListAdpter;

    private PlayerServiceConnection playerServiceConnection;
    private MusicPlayerService.MusicPlayerBinder playerBinder;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        Log.d(TAG,"initView");
        mRecyclerView = findViewById(R.id.main_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        musicListAdpter = new MusicListAdpter(this);
        mRecyclerView.setAdapter(musicListAdpter);

        bindService();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        if (playerServiceConnection != null){
            unbindService(playerServiceConnection);
        }
        super.onDestroy();
    }

    private void bindService(){
        playerServiceConnection = new PlayerServiceConnection();
        Intent intent = new Intent(this, MusicPlayerService.class);
        bindService(intent,playerServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setMusicListData(List<Song> songList){
        Log.i(TAG,"setMusicListData");
        if (songList == null && !songList.isEmpty()){
            return;
        }

        musicListAdpter.setSongList(songList);

        if (!songList.isEmpty()){
            Song song = songList.get(0);
            playerBinder.play(song);
        }

    }

    @Override
    public void onRefreshMusicList(List<Song> songList) {
        setMusicListData(songList);
    }

    @Override
    public void onPublish(Song song, int progress) {

    }

    @Override
    public void onBufferingUpdate(Song song, int percent) {

    }

    @Override
    public void onMusicChange(Song song) {

    }

    private class PlayerServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(TAG,"onServiceConnected");
            if (iBinder != null && iBinder instanceof MusicPlayerService.MusicPlayerBinder){
                playerBinder = (MusicPlayerService.MusicPlayerBinder) iBinder;
                playerBinder.setIMusicUIRefreshListener(MainActivity.this);
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
