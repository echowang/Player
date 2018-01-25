package com.danny.player.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by tingw on 2018/1/15.
 */

public abstract class BaseFragment extends Fragment implements FragmentEventListener {
    private FragmentEventListener fragmentEventListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(),container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    protected abstract int getLayout();

    protected void initView(){

    }

    public void setFragmentEventListener(FragmentEventListener fragmentEventListener){
        this.fragmentEventListener = fragmentEventListener;
    }

    @Override
    public void openFragment(BaseFragment fragment){
        if (fragmentEventListener != null){
            fragmentEventListener.openFragment(fragment);
        }
    }

    @Override
    public void openActivity(Intent intent) {
        if (fragmentEventListener != null){
            fragmentEventListener.openActivity(intent);
        }
    }

    @Override
    public void setToolBarTitle(String title){
        if (fragmentEventListener != null){
            fragmentEventListener.setToolBarTitle(title);
        }
    }

    @Override
    public void setToolBarBackStatue(boolean show){
        if (fragmentEventListener != null){
            fragmentEventListener.setToolBarBackStatue(show);
        }
    }

}
