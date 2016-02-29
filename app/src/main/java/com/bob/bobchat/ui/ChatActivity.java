package com.bob.bobchat.ui;

import android.text.TextUtils;

import com.bob.bobchat.R;

/**
 * 作者 bob
 * 日期 16-2-29.
 */
public class ChatActivity extends BaseActivity{
    @Override
    protected int initLayout() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initToolBar() {
        String user_info = getIntent().getStringExtra("user_info");
        if (!TextUtils.isEmpty(user_info)){
            toolbar.setTitle(user_info);
        }
    }
}
