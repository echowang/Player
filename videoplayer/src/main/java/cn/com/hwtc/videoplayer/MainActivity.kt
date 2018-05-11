package cn.com.hwtc.videoplayer

import cn.com.hwtc.videoplayer.fragment.VideoMainFragment

class MainActivity : BaseFragmentActivity() {
    lateinit var videoMainFragment : VideoMainFragment

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun addRootFragment() {
        videoMainFragment = VideoMainFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_container,videoMainFragment,videoMainFragment.javaClass.simpleName)
        fragmentTransaction.addToBackStack("video")
        fragmentTransaction.commitAllowingStateLoss()
    }

}