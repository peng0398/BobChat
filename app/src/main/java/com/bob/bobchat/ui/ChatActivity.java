package com.bob.bobchat.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bob.bobchat.R;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;

/**
 * 作者 bob
 * 日期 16-2-29.
 */
public class ChatActivity extends BaseActivity{

    private String user_id;

    @Override
    protected int initLayout() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initToolBar() {
        user_id = getIntent().getStringExtra("user_info");
        if (!TextUtils.isEmpty(user_id)){
            toolbar.setTitle(user_id);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //new出EaseChatFragment或其子类的实例
        EaseChatFragment chatFragment = new EaseChatFragment();
        //传入参数
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, user_id);
        chatFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

    }
}
