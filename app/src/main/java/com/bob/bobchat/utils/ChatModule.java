package com.bob.bobchat.utils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Author: bob
 * Date: 16-3-26.
 */

@Module
public class ChatModule {

    @Provides
    @Singleton
    ChatHelper getChatHelper(){
        return new ChatHelper();
    }

}
