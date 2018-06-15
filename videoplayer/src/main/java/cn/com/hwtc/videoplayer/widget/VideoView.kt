package cn.com.hwtc.videoplayer.widget

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.widget.LinearLayout
import cn.com.hwtc.video.bean.VideoBean
import cn.com.hwtc.video.player.VideoPlayer
import cn.com.hwtc.videoplayer.R
import kotlinx.android.synthetic.main.player_video_view.view.*

class VideoView : LinearLayout,SurfaceHolder.Callback,AudioManager.OnAudioFocusChangeListener {

    lateinit var videoBean : VideoBean
    lateinit var videoPlayer : VideoPlayer

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    fun initView(context: Context){
        val layoutInflater = LayoutInflater.from(context)
        layoutInflater.inflate(R.layout.player_video_view,this,true)
        video_surfaceview.holder.addCallback(this)

        val mAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        mAudioManager.requestAudioFocus(this,AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
    }

    fun setVideo(video : VideoBean) {
        videoBean = video
    }

    fun play(){
        Log.i("video","play")
        videoPlayer.play(context,videoBean)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        Log.i("video","surfaceChanged")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        Log.i("video","surfaceDestroyed")
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        Log.i("video","surfaceCreated")
        videoPlayer = VideoPlayer(video_surfaceview.holder)
        play()
    }

    override fun onAudioFocusChange(focusChange: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}