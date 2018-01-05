package com.danny.media.library.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.danny.media.library.file.MediaProviderFactory;
import com.danny.media.library.file.MusicProvider;
import com.danny.media.library.model.Song;

import java.util.List;

/**
 * Created by dannywang on 2017/12/29.
 */

public class MusicPlayerService extends Service implements PlayerScheduleListener {
    private final static String TAG = MusicPlayerService.class.getSimpleName();

    private MusicProvider musicProvider;
    private IMusicUIRefreshListener refreshListener;
    private MusicPlayerBinder playerBinder;
    private MusicPlayer musicPlayer;
    private List<Song> songList;
    private int playSongPosition;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"onCreate");

        musicProvider = MediaProviderFactory.getInstance().getMusciProvideo(this);
        musicProvider.setIMusicScanListener(new MusicProvider.IMusicScanListener() {
            @Override
            public void onStartScan() {
                Log.i(TAG,"onStartScan");
            }

            @Override
            public void onScanFinish() {
                Log.i(TAG,"onScanFinish");
                songList = musicProvider.getMusicList();
                if (refreshListener != null){
                    refreshListener.onRefreshMusicList(songList);
                }
            }
        });

        musicPlayer = new MusicPlayer(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"onStartCommand");

        if (musicProvider != null){
            musicProvider.loadMusic();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"onBind");
        if (playerBinder == null){
            playerBinder = new MusicPlayerBinder();
        }
        return playerBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG,"onUnbind");
        playerBinder = null;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"onDestroy");
        super.onDestroy();
    }

    public void setRefreshListener(IMusicUIRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    @Override
    public void onPublish(int progress) {

    }

    @Override
    public void onBufferingUpdate(int percent) {

    }

    @Override
    public void OnCompletion() {
        nextMusic();
    }

    private void playMusic(Song song){
        playSongPosition = songList.indexOf(song);
        musicPlayer.play(song);
    }

    private void pauseMusic(){
        musicPlayer.pause();
    }

    private void stopMusic(){
        musicPlayer.stop();
    }

    private void prevMusic(){

    }

    private void nextMusic(){
        int max  = songList.size() - 1;
        if (playSongPosition == max){
            playSongPosition = 0;
        }else if(playSongPosition >= 0 && playSongPosition < max){
            playSongPosition++;
        }else{
            playSongPosition = 0;
        }
        Song song = songList.get(playSongPosition);
        musicPlayer.play(song);
    }

    public class MusicPlayerBinder extends Binder{
        public void setIMusicUIRefreshListener(IMusicUIRefreshListener refreshListener){
            setRefreshListener(refreshListener);
        }

        public List<Song> getMusicList(){
            return songList;
        }

        public Song getPlayingMusic(){
            return musicPlayer.getPlaySong();
        }

        public void play(Song song){
            playMusic(song);
        }

        public void pause(){
            pauseMusic();
        }

        public void stop(){
            stopMusic();
        }

        public void prev(){
            prevMusic();
        }

        public void next(){
            nextMusic();
        }
    }

    public interface IMusicUIRefreshListener{
        void onRefreshMusicList(List<Song> songList);
        void onPublish(Song song,int progress);
        void onBufferingUpdate(Song song,int percent);
        void onMusicChange(Song song);
    }
}
