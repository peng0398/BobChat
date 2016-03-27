package com.bob.bobchat;

import android.app.Application;
import android.content.Context;

import com.bob.bobchat.utils.AppModule;
import com.bob.bobchat.utils.ChatComponent;
import com.bob.bobchat.utils.ChatModule;
import com.bob.bobchat.utils.DaggerChatComponent;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;

/**
 * 作者 bob
 * 日期 16-2-17.
 */
public class BobApplication extends Application {

    private static Context context;

    public static  BobApplication application = null;

    public ChatComponent getBuild() {
        return build;
    }

    private ChatComponent build;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();

        application = this;

        build = DaggerChatComponent.builder()
                .chatModule(new ChatModule())
                .appModule(new AppModule())
                .build();

        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        //初始化
        EaseUI.getInstance().init(context, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }

    public static Context getAppContext() {
        return context;
    }
}
