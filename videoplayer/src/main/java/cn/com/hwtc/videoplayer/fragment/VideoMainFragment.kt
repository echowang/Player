package cn.com.hwtc.videoplayer.fragment

import android.os.Bundle
import cn.com.hwtc.video.bean.VideoBean
import cn.com.hwtc.videoplayer.R
import kotlinx.android.synthetic.main.fragment_video_main.*

class VideoMainFragment : BaseFragment() {
    override fun getLayout(): Int {
        return R.layout.fragment_video_main
    }

    override fun initView(savedInstanceState: Bundle?) {
        val videoBoolean = VideoBean("战狼2HD高清国语中英双字",7374184,1280,720,1378377684,"/mnt/usbhost/Storage01/战狼2HD高清国语中英双字.mkv")
        viewInfoView.setVideo(videoBoolean)
        videoView.setVideo(videoBoolean)
    }
}