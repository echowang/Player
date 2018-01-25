package com.danny.player.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.danny.player.R;
import com.danny.player.ui.fragment.BaseFragment;
import com.danny.player.ui.fragment.VideoPlayFragment;

import butterknife.BindView;

/**
 * Created by tingw on 2018/1/24.
 */

public class VideoActivity extends BaseAcivity {
    public final static String PARAM_FRAGMENT = "PARAM_FRAGMENT";
    public final static String PARAM_VIDEO = "PARAM_VIDEO";
    public final static String PARAM_BUNNDLE = "PARAM_BUNNDLE";
    public final static String VIDEO_PLAY_FRAGMENT = "VIDEO_PLAY_FRAGMENT";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_second;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用

        Intent intent = getIntent();
        if (intent != null){
            String param_fragment = intent.getStringExtra(PARAM_FRAGMENT);
            if (VIDEO_PLAY_FRAGMENT.equalsIgnoreCase(param_fragment)){
                Bundle bundle = intent.getBundleExtra(PARAM_BUNNDLE);
                BaseFragment videoPlayFragment = new VideoPlayFragment();
                videoPlayFragment.setArguments(bundle);
                openFragment(videoPlayFragment);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return true;
    }

    @Override
    public void setToolBarTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void setToolBarBackStatue(boolean show) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(show);
    }

    @Override
    public void openFragment(BaseFragment fragment) {
        if (fragment != null){
            fragment.setFragmentEventListener(this);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_up,R.anim.fragment_slide_down,R.anim.fragment_slide_up,R.anim.fragment_slide_down);
            fragmentTransaction.replace(R.id.fragment_container,fragment,fragment.getClass().getSimpleName());
            fragmentTransaction.addToBackStack("video");
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void openActivity(Intent intent) {

    }
}
