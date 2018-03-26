package com.danny.player.ui.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.danny.media.library.music.model.Song;
import com.danny.media.library.player.IServiceUIRefreshListener;
import com.danny.media.library.music.player.MusicPlayerService;
import com.danny.media.library.player.PlayerService;
import com.danny.media.library.utils.LogUtil;
import com.danny.player.R;
import com.danny.player.adapter.MusicListAdpter;
import com.danny.player.adapter.OnItemClickListener;
import com.danny.player.application.PlayerApplication;
import com.danny.player.ui.MusicActivity;
import com.danny.player.ui.widget.MusicListControllerBar;
import com.danny.player.ui.widget.RecycleViewDivider;

import java.util.List;

import butterknife.BindView;

/**
 * Created by tingw on 2018/1/15.
 */

public class MusicMainFragment extends BaseFragment implements IServiceUIRefreshListener<Song>,OnItemClickListener<Song>, MusicListControllerBar.MusicControllerBarListener {
    private final static String TAG = MusicMainFragment.class.getSimpleName();
    @BindView(R.id.music_main_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.music_controller_bar)
    MusicListControllerBar musicListControllerBar;

    private MusicListAdpter musicListAdpter;

    private MusicServiceConnection musicServiceConnection;

    private PlayerService<Song> playerService;

    @Override
    protected int getLayout() {
        return R.layout.fragment_music_main;
    }

    @Override
    protected void initView() {
        super.initView();
        LogUtil.d(TAG,"initView");
        setToolBarTitle(getString(R.string.player_music));
        setToolBarBackStatue(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL));
        mRecyclerView.setLayoutManager(linearLayoutManager);
        musicListAdpter = new MusicListAdpter(getContext());
        musicListAdpter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(musicListAdpter);

        musicListControllerBar.setControllerBarListener(this);

        playerService = PlayerApplication.getApplication().getMusicPlayerService();
        if (playerService == null){
            bindPlayerService();
        }else{
            List<Song> songList = playerService.getPlaySourceList();
            setMusicListData(songList);
        }
    }

    @Override
    public void onResume() {
        LogUtil.i(TAG,"onResume");
        super.onResume();
        if (playerService != null){
            playerService.registerUIRefreshListener(MusicMainFragment.this);
            Song song = playerService.getPlaySource();
            updateMusicInfo(song);
        }
    }

    @Override
    public void onPause() {
        LogUtil.i(TAG,"onPause");
        super.onPause();
        if (playerService != null){
            playerService.unRegisterUIRefreshListener(MusicMainFragment.this);
        }
    }

    @Override
    public void onDestroyView() {
        unBindPlayerService();
        super.onDestroyView();
    }

    private void setMusicListData(List<Song> songList){
        LogUtil.i(TAG,"setMusicListData");
        if (songList == null || songList.isEmpty()){
            return;
        }

        if (playerService != null){
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
            mRecyclerView.scrollToPosition(position);
        }

    }

    private void bindPlayerService(){
        musicServiceConnection = new MusicServiceConnection();
        Intent intent = new Intent(getContext(), MusicPlayerService.class);
        getContext().bindService(intent, musicServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unBindPlayerService(){
        if (musicServiceConnection != null){
            getContext().unbindService(musicServiceConnection);
            musicServiceConnection = null;
        }
    }

    @Override
    public void onRefreshSourceList(final List<Song> songList) {
        setMusicListData(songList);
    }

    @Override
    public void onPublish(Song song, int progress) {
        LogUtil.d(TAG,"song " + song.getTitle() + " , progress : " + progress);
        if (isVisible()){
            musicListControllerBar.updateMusicProgress(progress);
        }
    }

    @Override
    public void onBufferingUpdate(Song song, int percent) {
        LogUtil.d(TAG,"song " + song.getTitle() + " , percent : " + percent);
    }

    @Override
    public void onSourceChange(Song song) {
        if (song == null || playerService == null){
            return;
        }
        LogUtil.d(TAG,"song " + song);
        updateMusicInfo(song);
    }

    @Override
    public void onItenClick(int position, Song song) {
        LogUtil.d(TAG,"onClick : " + position);
        if (playerService == null){
            return;
        }
        if (song != null){
            Song playSong = playerService.getPlaySource();
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
        Intent intent = new Intent(getContext(), MusicActivity.class);
        intent.putExtra(MusicActivity.PARAM_FRAGMENT, MusicActivity.MUSIC_PLAY_FRAGMENT);
        openActivity(intent);
    }

    private void updateMusicInfo(Song song){
        if (song == null){
            return;
        }

        musicListControllerBar.setMusicInfo(song);
        musicListControllerBar.setPlayStatue(playerService.isPlaying());
        musicListControllerBar.updateMusicProgress(playerService.getPlayProgress());
        int position = musicListAdpter.updateSelectedItem(song);
        if (position >= 0){
            mRecyclerView.scrollToPosition(position);
        }
    }

    private class MusicServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LogUtil.i(TAG,"onServiceConnected");
            if (iBinder != null && iBinder instanceof PlayerService.PlayerBinder){
                PlayerService.PlayerBinder playerBinder = (PlayerService.PlayerBinder) iBinder;
                playerService = playerBinder.getPlayerService();
                PlayerApplication.getApplication().setMusicPlayerService(playerService);
                playerService.registerUIRefreshListener(MusicMainFragment.this);
                List<Song> songList = playerService.getPlaySourceList();
                setMusicListData(songList);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            LogUtil.i(TAG,"onServiceDisconnected");
        }

        @Override
        public void onBindingDied(ComponentName name) {
            LogUtil.i(TAG,"onBindingDied");
        }
    }
}
