package cn.com.hwtc.videoplayer

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import cn.com.hwtc.video.listener.PlayerEventListener
import cn.com.hwtc.video.listener.PlayerMediaSourceEventListener
import com.google.android.exoplayer2.*
import kotlinx.android.synthetic.main.exoplayer_activity_main.*
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter

class ExoPlayerActivity : AppCompatActivity(), PlaybackPreparer {

    var shouldAutoPlay : Boolean = true
    lateinit var player : SimpleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exoplayer_activity_main)
    }

    override fun onResume() {
        super.onResume()

        initExoplayer()
    }

    override fun preparePlayback() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun initExoplayer(){
// 1.创建一个默认TrackSelector
        val mainHandler = Handler()
        val bandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        // 2.创建一个默认的LoadControl
        val loadControl = DefaultLoadControl()

        val renderersFactory = DefaultRenderersFactory(this)
        // 3.创建播放器
        player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl)

        player.addListener(PlayerEventListener())
        player.playWhenReady = shouldAutoPlay

        playerView.player = player
        playerView.setPlaybackPreparer(this)

        //      /mnt/usbhost/Storage01/战狼2HD高清国语中英双字.mkv
        //      /mnt/usbhost/Storage01/video/hey juice.mp4
        //      /mnt/usbhost/Storage01/video/DACA02视频/WMV2/wmv1/960X540P_3302kbps_虫族.wmv     不支持
        //      /mnt/usbhost/Storage01/video/DACA02视频/MOV4 test/848X448P_1973kbps_全民超人.mov
        //      /mnt/usbhost/Storage01/video/DACA02视频/VOB2 test/768X576P_6610kbps_火龙对决.vob      不支持
        //      /mnt/usbhost/Storage01/video/DACA02视频/MPG2/384X288P_1392kbps_蔡妍-两个人_韩国性感女神.mpg    不支持 没有画面
        val dataSourceFactory = DefaultDataSourceFactory(this, "exoplayer")
        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse("/mnt/usbhost/Storage01/战狼2HD高清国语中英双字.mkv"), mainHandler, PlayerMediaSourceEventListener())

        val loopingSource = LoopingMediaSource(mediaSource)
        player.prepare(loopingSource)
    }
}
