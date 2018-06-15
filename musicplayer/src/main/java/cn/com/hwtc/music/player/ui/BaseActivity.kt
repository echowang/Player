package cn.com.hwtc.videoplayer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    abstract fun getLayout() : Int

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(getLayout())
     }
}