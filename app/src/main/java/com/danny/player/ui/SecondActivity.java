package com.danny.player.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.danny.player.R;

import butterknife.BindView;

/**
 * Created by tingw on 2018/1/24.
 */

public class SecondActivity extends BaseAcivity {
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
