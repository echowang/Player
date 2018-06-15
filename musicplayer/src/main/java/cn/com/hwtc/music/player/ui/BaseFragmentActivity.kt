package cn.com.hwtc.videoplayer

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.WindowManager

abstract class BaseFragmentActivity : FragmentActivity(){
    abstract fun getLayout() : Int
    abstract fun addRootFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSystemBarTransparent()

        setContentView(getLayout())

        addRootFragment()
    }

    fun setSystemBarTransparent(){
        val window = window
        if (window != null) {
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //透明导航栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }

    fun showNavigationBar(show : Boolean){
        if (show){
            val attr = window.attributes
            attr.flags = attr.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
            window.attributes = attr
        }else{
            val lp = window.attributes
            lp.flags = lp.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
            window.attributes = lp
        }
    }
}