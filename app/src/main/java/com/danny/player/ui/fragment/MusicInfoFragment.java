package com.danny.player.ui.fragment;

import android.view.View;

import com.danny.player.R;

/**
 * Created by tingw on 2018/1/17.
 */

public class MusicInfoFragment extends BaseFragment {
    @Override
    protected int getLayout() {
        return R.layout.fragment_music_info;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

        setToolBarBackStatue(true);
    }


}
