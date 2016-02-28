package com.bob.bobchat.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * 作者 bob
 * 日期 16-2-25.
 */
public abstract class BaseFragment extends Fragment {

    boolean isFirst = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(initLayout(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirst) {
                isFirst = false;
                initView();
                initData();
            }
        }
    }

    protected abstract int initLayout();

    protected abstract void initView();

    protected abstract void initData();
}
