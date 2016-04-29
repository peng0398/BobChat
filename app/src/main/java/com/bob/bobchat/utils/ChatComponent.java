package com.bob.bobchat.utils;

import com.bob.bobchat.ui.MainActivity;
import com.bob.bobchat.ui.SplashActivity;
import com.bob.bobchat.ui.fragment.ContactFragment;
import com.bob.bobchat.ui.fragment.SettingFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Author: bob
 * Date: 16-3-22.
 */
@Singleton
@Component(modules = {ChatModule.class,AppModule.class})
public interface ChatComponent {
    void inject(SplashActivity activity);
    void inject(MainActivity activity);
    void inject(ContactFragment fragment);
    void inject(SettingFragment fragment);
    // void inject(MyService service);
}

