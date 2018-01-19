package com.danny.player.ui.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.danny.media.library.model.Song;
import com.danny.media.library.service.MusicPlayerService;
import com.danny.media.library.service.PlayerService;
import com.danny.player.R;
import com.danny.player.adapter.MusicListAdpter;
import com.danny.player.application.PlayerApplication;
import com.danny.player.ui.widget.MusicListControllerBar;
import com.danny.player.ui.widget.RecycleViewDivider;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by tingw on 2018/1/15.
 */

public class MusicMainFragment extends BaseFragment implements PlayerService.IServiceUIRefreshListener<Song>,MusicListAdpter.OnMusicItemClick, MusicListControllerBar.MusicControllerBarListener {
    private final static String TAG = MusicMainFragment.class.getSimpleName();
    @BindView(R.id.main_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.music_controller_bar)
    MusicListControllerBar musicListControllerBar;

    private MusicListAdpter musicListAdpter;

    private PlayerServiceConnection playerServiceConnection;

    private PlayerService<Song> playerService;

    @Override
    protected int getLayout() {
        return R.layout.fragment_music_main;
    }

    @Override
    protected void initView() {
        super.initView();
        Log.d(TAG,"initView");
        setToolBarTitle(getString(R.string.player_music));
        setToolBarBackStatue(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL));
        mRecyclerView.setLayoutManager(linearLayoutManager);
        musicListAdpter = new MusicListAdpter(getContext());
        musicListAdpter.setOnMusicItemClick(this);
        mRecyclerView.setAdapter(musicListAdpter);

        musicListControllerBar.setControllerBarListener(this);

        playerService = PlayerApplication.getApplication().getMusicPlayerService();
        if (playerService == null){
            bindPlayerService();
        }else{
            playerService.setUIRefreshListener(MusicMainFragment.this);
            List<Song> songList = playerService.getPlaySourceList();
            setMusicListData(songList);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (playerService != null){
            Song song = playerService.getPlaySource();
            updateMusicInfo(song);
        }
    }

    @Override
    public void onDestroyView() {
        unBindPlayerService();
        super.onDestroyView();
    }

    private void setMusicListData(List<Song> songList){
        Log.i(TAG,"setMusicListData");
        if (songList == null && !songList.isEmpty()){
            return;
        }

        if (!songList.isEmpty() && playerService != null){
            Song song = playerService.getPlaySource();
            if (song == null){
                song = songList.get(0);
            }

            boolean isPlaying = playerService.isPlaying();
            if (!isPlaying){
                if (playerService.isAutoPlay()){
                    playerService.play(song);
                    isPlaying = true;
                }
            }

            musicListControllerBar.setMusicInfo(song);
            musicListControllerBar.setPlayStatue(isPlaying);
            musicListControllerBar.updateMusicProgress(playerService.getPlayProgress());
            int position = songList.indexOf(song);
            musicListAdpter.setSongList(songList,position,isPlaying);
        }

    }

    private void bindPlayerService(){
        playerServiceConnection = new MusicMainFragment.PlayerServiceConnection();
        Intent intent = new Intent(getContext(), MusicPlayerService.class);
        getContext().bindService(intent,playerServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unBindPlayerService(){
        if (playerServiceConnection != null){
            getContext().unbindService(playerServiceConnection);
            playerServiceConnection = null;
        }
    }

    @Override
    public void onRefreshMusicList(List<Song> songList) {
        setMusicListData(songList);
    }

    @Override
    public void onPublish(Song song, int progress) {
        Log.d(TAG,"song " + song.getTitle() + " , progress : " + progress);
        if (isVisible()){
            musicListControllerBar.updateMusicProgress(progress);
        }
    }

    @Override
    public void onBufferingUpdate(Song song, int percent) {
        Log.d(TAG,"song " + song.getTitle() + " , percent : " + percent);
    }

    @Override
    public void onMusicChange(Song song) {
        if (song == null || playerService == null){
            return;
        }
        Log.d(TAG,"song " + song);
        updateMusicInfo(song);
    }

    @Override
    public void onMusicItenClick(int position, Song song) {
        Log.d(TAG,"onClick : " + position);
        if (playerService == null){
            return;
        }
        if (song != null){
            Song playSong = (Song) playerService.getPlaySource();
            if (!song.equals(playSong)){
                playerService.play(song);
            }
        }
    }

    @Override
    public void onNextClick() {
        if (playerService == null){
            return;
        }
        playerService.next();
    }

    @Override
    public void onPlayOrPauseClick() {
        if (playerService == null){
            return;
        }
        if (playerService.isPlaying()){
            musicListAdpter.updatePlayAnimationStatue(false);
            playerService.pause();
            musicListControllerBar.setPlayStatue(false);
        }else{
            musicListAdpter.updatePlayAnimationStatue(true);
            playerService.resume();
            musicListControllerBar.setPlayStatue(true);
        }
    }

    @Override
    public void OnControllerBarClick() {
        BaseFragment fragment = new MusicPlayFragment();
        startFragment(fragment);
    }

    private void updateMusicInfo(Song song){
        if (song == null){
            return;
        }

        musicListControllerBar.setMusicInfo(song);
        musicListControllerBar.setPlayStatue(playerService.isPlaying());
        musicListControllerBar.updateMusicProgress(playerService.getPlayProgress());
        musicListAdpter.updateSelectedItem(song);
    }

    private class PlayerServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(TAG,"onServiceConnected");
            if (iBinder != null && iBinder instanceof PlayerService.PlayerBinder){
                PlayerService.PlayerBinder playerBinder = (PlayerService.PlayerBinder) iBinder;
                playerService = playerBinder.getPlayerService();
                PlayerApplication.getApplication().setMusicPlayerService(playerService);
                playerService.setUIRefreshListener(MusicMainFragment.this);
                List<Song> songList = playerService.getPlaySourceList();
                setMusicListData(songList);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }

        @Override
        public void onBindingDied(ComponentName name) {

        }
    }
}
