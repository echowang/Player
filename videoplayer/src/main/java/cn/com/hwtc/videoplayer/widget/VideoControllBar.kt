package cn.com.hwtc.videoplayer.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import cn.com.hwtc.videoplayer.R

class VideoControllBar : LinearLayout {
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
        layoutInflater.inflate(R.layout.player_video_controller_bar,this,true)
    }
}