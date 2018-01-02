package com.danny.player

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.os.Bundle
import com.danny.player.adapter.RecyclerViewAdpter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main_recyclerview.layoutManager = LinearLayoutManager(this)
        main_recyclerview.adapter = RecyclerViewAdpter(this)
    }
}
