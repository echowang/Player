package cn.com.hwtc.videoplayer.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

class VideoView : LinearLayout {
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

    }
}