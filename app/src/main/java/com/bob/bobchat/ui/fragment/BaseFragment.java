package com.bob.bobchat.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bob.bobchat.BobApplication;
import com.bob.bobchat.R;

/**
 * 作者 bob
 * 日期 16-2-25.
 */
public class BaseFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return View.inflate(BobApplication.getAppContext(), R.layout.activity_login,null);
    }
}
