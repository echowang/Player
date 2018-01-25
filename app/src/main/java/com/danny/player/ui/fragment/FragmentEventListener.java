package com.danny.player.ui.fragment;

import android.content.Intent;

/**
 * Created by tingw on 2018/1/25.
 */

public interface FragmentEventListener{
    void setToolBarTitle(String title);
    void setToolBarBackStatue(boolean show);
    void openFragment(BaseFragment fragment);
    void openActivity(Intent intent);
}
