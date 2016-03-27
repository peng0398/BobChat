package com.bob.bobchat.utils;

import com.bob.bobchat.BobApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Author: bob
 * Date: 16-3-26.
 */

@Module
public class AppModule {

    @Provides
    @Singleton
    BobApplication provideApplication(){
        return BobApplication.application;
    }
}
