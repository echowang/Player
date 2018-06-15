package cn.com.hwtc.video.player

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.view.SurfaceHolder
import cn.com.hwtc.video.bean.VideoBean

class VideoPlayer : MediaPlayer.OnSeekCompleteListener,MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener,MediaPlayer.OnErrorListener {
    var mediaPlayer : MediaPlayer = MediaPlayer()

    constructor(surfaceHolder: SurfaceHolder){
        mediaPlayer.setOnSeekCompleteListener(this)
        mediaPlayer.setOnPreparedListener(this)
        mediaPlayer.setOnCompletionListener(this)
        mediaPlayer.setOnErrorListener(this)
        mediaPlayer.setDisplay(surfaceHolder)
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.setScreenOnWhilePlaying(true)
    }

    fun play(context: Context,videoBean: VideoBean){
        mediaPlayer.reset()
        mediaPlayer.setDataSource(context, Uri.parse(videoBean.path))
        mediaPlayer.prepareAsync()
    }

    fun pause(){
        if (mediaPlayer.isPlaying){
            mediaPlayer.pause()
        }
    }

    fun stop(){
        if (mediaPlayer.isPlaying){
            mediaPlayer.stop()
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {

    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        return false
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mediaPlayer.start()
    }

    override fun onSeekComplete(mp: MediaPlayer?) {

    }

}