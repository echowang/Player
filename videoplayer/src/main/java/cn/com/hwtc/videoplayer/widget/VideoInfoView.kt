package cn.com.hwtc.videoplayer.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import cn.com.hwtc.video.bean.VideoBean
import cn.com.hwtc.videoplayer.R
import kotlinx.android.synthetic.main.widget_video_information.view.*

class VideoInfoView : LinearLayout {

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
        layoutInflater.inflate(R.layout.widget_video_information,this,true)
    }

    fun setVideo(video : VideoBean){
        text_video_name.text = video.name
        text_video_time.text = video.parseTotalTimeToString()
        text_video_resolution.text = video.parseResolutionToString()
        text_video_size.text = video.parseSizeToString()
    }
}