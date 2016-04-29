package com.bob.bobchat.ui.fragment;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bob.bobchat.BobApplication;
import com.bob.bobchat.R;
import com.bob.bobchat.ui.LoginActivity;
import com.bob.bobchat.utils.ChatHelper;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * 作者 bob
 * 日期 16-2-25.
 */
public class SettingFragment extends BaseFragment {

    @Inject
    ChatHelper helper;

    @Bind(R.id.btn_logout)
    Button btn_logout;

    @Override
    protected int initLayout() {
        return R.layout.fragment_setting;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application application = getActivity().getApplication();
        ((BobApplication) application).getBuild().inject(this);
    }

    @Override
    protected void initView() {

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EMClient.getInstance().logout(true, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }

                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(BobApplication.application, s, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });
    }

    @Override
    protected void initData() {

    }
}
