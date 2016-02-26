package com.bob.bobchat.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bob.bobchat.BobApplication;

/**
 * 作者 bob
 * 日期 16-2-25.
 */
public abstract class BaseFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return View.inflate(BobApplication.getAppContext(), initLayout(),null);
    }

    protected abstract int initLayout();
}
