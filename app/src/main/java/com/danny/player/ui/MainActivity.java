package com.danny.player.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.danny.player.R;
import com.danny.player.application.PlayerApplication;
import com.danny.player.ui.fragment.BaseFragment;
import com.danny.player.ui.fragment.MusicMainFragment;
import com.danny.player.ui.fragment.VideoMainFragment;

import butterknife.BindView;


/**
 * Created by tingw on 2018/1/3.
 */

public class MainActivity extends BaseAcivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.player_drawerlavout)
    DrawerLayout drawerLayout;
    @BindView(R.id.lv_left_menu)
    ListView menuListView;

    private String[] lvs;
    private ArrayAdapter arrayAdapter;

    private BaseFragment cureentFragment;
    private BaseFragment musicMainFragment;
    private BaseFragment videoMainFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用

        musicMainFragment = new MusicMainFragment();
        musicMainFragment.setFragmentEventListener(this);
        videoMainFragment = new VideoMainFragment();
        videoMainFragment.setFragmentEventListener(this);

        lvs = new String[]{getString(R.string.player_music), getString(R.string.player_video), getString(R.string.player_picture)};
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
        menuListView.setAdapter(arrayAdapter);
        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:{
                        openFragment(musicMainFragment);
                        drawerLayout.closeDrawers();
                        break;
                    }
                    case 1:{
                        PlayerApplication.getApplication().getMusicPlayerService().stop();
                        openFragment(videoMainFragment);
                        drawerLayout.closeDrawers();
                        break;
                    }
                }
            }
        });

        openFragment(musicMainFragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    FragmentEventListener
    @Override
    public void setToolBarTitle(String title) {
        if (TextUtils.isEmpty(title)){
            return;
        }
        toolbar.setTitle(title);
    }

    @Override
    public void setToolBarBackStatue(boolean show) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(show);
    }

    @Override
    public void openFragment(BaseFragment fragment) {
        if (fragment != null){
            if (fragment.equals(cureentFragment)){
                return;
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_up,R.anim.fragment_slide_down,R.anim.fragment_slide_up,R.anim.fragment_slide_down);

            if (!fragment.isAdded()){
                fragmentTransaction.add(R.id.fragment_container,fragment,fragment.getClass().getSimpleName());
                fragmentTransaction.show(fragment);
                cureentFragment = fragment;
            }else{
                fragmentTransaction.hide(cureentFragment);
                fragmentTransaction.show(fragment);
                cureentFragment = fragment;
            }
            fragmentTransaction.addToBackStack("player");
            fragmentTransaction.commitAllowingStateLoss();

        }
    }

    @Override
    public void openActivity(Intent intent) {
        if (intent != null){
            startActivity(intent);
        }
    }
}
