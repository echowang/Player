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
import com.danny.player.ui.widget.MusicControllerBar;
import com.danny.player.ui.widget.RecycleViewDivider;

import java.util.List;

/**
 * Created by tingw on 2018/1/15.
 */

public class MusicMainFragment extends BaseFragment implements MusicPlayerService.IMusicUIRefreshListener,MusicListAdpter.OnMusicItemClick,MusicControllerBar.MusicControllerBarListener {
    private final static String TAG = MusicMainFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MusicListAdpter musicListAdpter;

    private MusicControllerBar musicControllerBar;

    private PlayerServiceConnection playerServiceConnection;
    private MusicPlayerService.MusicPlayerBinder playerBinder;

    private int playProgress = 0;

    @Override
    protected int getLayout() {
        return R.layout.fragment_music_main;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        Log.d(TAG,"initView");
        setToolBarTitle(getString(R.string.player_music));
        setToolBarBackStatue(false);

        mRecyclerView = view.findViewById(R.id.main_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL));
        mRecyclerView.setLayoutManager(linearLayoutManager);
        musicListAdpter = new MusicListAdpter(getContext());
        musicListAdpter.setOnMusicItemClick(this);
        mRecyclerView.setAdapter(musicListAdpter);

        musicControllerBar = view.findViewById(R.id.music_controller_bar);
        musicControllerBar.setControllerBarListener(this);

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

        if (!songList.isEmpty() && playerBinder != null){
            Song song = playerBinder.getPlayingMusic();
            if (song == null){
                song = songList.get(0);
            }

            boolean isPlaying = playerBinder.isPlaying();
            if (!isPlaying){
                if (playerBinder.isAutoPlay()){
                    playerBinder.play(song);
                    isPlaying = true;
                }
            }

            musicControllerBar.setMusicInfo(song);
            musicControllerBar.setPlayStatue(isPlaying);
            musicControllerBar.updateMusicProgress(playProgress);
            int position = songList.indexOf(song);
            musicListAdpter.setSongList(songList,position,isPlaying);
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
        playProgress = progress;
        if (isVisible()){
            musicControllerBar.updateMusicProgress(progress);
        }
    }

    @Override
    public void onBufferingUpdate(Song song, int percent) {
        Log.d(TAG,"song " + song.getTitle() + " , percent : " + percent);
    }

    @Override
    public void onMusicChange(Song song) {
        if (song == null || playerBinder == null){
            return;
        }
        Log.d(TAG,"song " + song);
        playProgress = 0;
        musicControllerBar.setMusicInfo(song);
        musicControllerBar.setPlayStatue(true);
        musicControllerBar.updateMusicProgress(playProgress);
        musicListAdpter.updateSelectedItem(song);
    }

    @Override
    public void onMusicItenClick(int position, Song song) {
        Log.d(TAG,"onClick : " + position);
        if (playerBinder == null){
            return;
        }
        if (song != null){
            Song playSong = playerBinder.getPlayingMusic();
            if (!song.equals(playSong)){
                playerBinder.play(song);
            }
        }
    }

    @Override
    public void onNextClick() {
        if (playerBinder == null){
            return;
        }
        playerBinder.next();
    }

    @Override
    public void onPlayOrPauseClick() {
        if (playerBinder == null){
            return;
        }
        if (playerBinder.isPlaying()){
            musicListAdpter.updatePlayAnimationStatue(false);
            playerBinder.pause();
            musicControllerBar.setPlayStatue(false);
        }else{
            musicListAdpter.updatePlayAnimationStatue(true);
            playerBinder.resume();
            musicControllerBar.setPlayStatue(true);
        }
    }

    @Override
    public void OnControllerBarClick() {
        BaseFragment fragment = new MusicInfoFragment();
        startFragment(fragment);
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
