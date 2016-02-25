package com.bob.bobchat.utils;

import com.hyphenate.chat.EMClient;

/**
 * 作者 bob
 * 日期 16-2-25.
 */
public class ChatHelper {

    private static ChatHelper instance;

    public synchronized static ChatHelper getInstance() {
        if (instance == null) {
            instance = new ChatHelper();
        }
        return instance;
    }

    /**
     * 是否登录成功过
     *
     * @return
     */
    public boolean isLoggedIn() {
        return EMClient.getInstance().isLoggedInBefore();
    }
}
