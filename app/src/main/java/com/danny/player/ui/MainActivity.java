package com.danny.player.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.danny.player.R;
import com.danny.player.ui.fragment.BaseFragment;
import com.danny.player.ui.fragment.MusicMainFragment;

import butterknife.BindView;


/**
 * Created by tingw on 2018/1/3.
 */

public class MainActivity extends BaseAcivity implements BaseFragment.FragmentEventListener{
    private final static String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.player_drawerlavout)
    DrawerLayout drawerLayout;
    @BindView(R.id.lv_left_menu)
    ListView menuListView;

    private String[] lvs = {"音乐", "视频", "图片查看器"};
    private ArrayAdapter arrayAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
        menuListView.setAdapter(arrayAdapter);

        BaseFragment musicMainFragment = new MusicMainFragment();
        startFragment(musicMainFragment);
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
    public void startFragment(BaseFragment fragment) {
        if (fragment != null){
            fragment.setFragmentEventListener(this);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_up,R.anim.fragment_slide_down,R.anim.fragment_slide_up,R.anim.fragment_slide_down);
            fragmentTransaction.replace(R.id.fragment_container,fragment,fragment.getClass().getSimpleName());
            fragmentTransaction.addToBackStack("player");
            fragmentTransaction.commitAllowingStateLoss();
        }
    }
}
