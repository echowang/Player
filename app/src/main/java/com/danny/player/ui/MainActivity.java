package com.danny.player.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;

import com.danny.player.R;
import com.danny.player.ui.fragment.MusicMainFragment;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;


/**
 * Created by tingw on 2018/1/3.
 */

public class MainActivity extends BaseAcivity{
    private final static String TAG = MainActivity.class.getSimpleName();

    private QMUITopBarLayout topBarLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        topBarLayout = (QMUITopBarLayout) findViewById(R.id.topbar);
        topBarLayout.setTitle(R.string.app_name);
        topBarLayout.setTitleGravity(Gravity.CENTER);
        topBarLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        Fragment musicMainFragment = new MusicMainFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container,musicMainFragment,MusicMainFragment.class.getSimpleName());
        fragmentTransaction.addToBackStack("player");
        fragmentTransaction.commitAllowingStateLoss();
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
    protected void onDestroy() {
        super.onDestroy();
    }

}
