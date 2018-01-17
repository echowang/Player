package com.danny.player.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.danny.player.R;
import com.danny.player.ui.fragment.BaseFragment;
import com.danny.player.ui.fragment.MusicMainFragment;


/**
 * Created by tingw on 2018/1/3.
 */

public class MainActivity extends BaseAcivity implements BaseFragment.FragmentEventListener{
    private final static String TAG = MainActivity.class.getSimpleName();

    private Toolbar toolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.fragment_container,fragment,MusicMainFragment.class.getSimpleName());
            fragmentTransaction.addToBackStack("player");
            fragmentTransaction.commitAllowingStateLoss();
        }
    }
}
