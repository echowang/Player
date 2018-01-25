package com.danny.player.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.danny.player.application.PlayerApplication;
import com.danny.player.ui.fragment.BaseFragment;
import com.danny.player.ui.fragment.FragmentEventListener;

import butterknife.ButterKnife;

/**
 * Created by tingw on 2018/1/3.
 */

public abstract class BaseAcivity extends AppCompatActivity implements FragmentEventListener {
    protected abstract int getLayoutId();
    protected abstract void initView(@Nullable Bundle savedInstanceState);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        ButterKnife.bind(this);

        initView(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 1){
            fragmentManager.popBackStack();
        }else{
            finish();
        }
    }

    @Override
    public void setToolBarTitle(String title) {

    }

    @Override
    public void setToolBarBackStatue(boolean show) {

    }

    @Override
    public void openFragment(BaseFragment fragment) {

    }

    @Override
    public void openActivity(Intent intent) {

    }
}
