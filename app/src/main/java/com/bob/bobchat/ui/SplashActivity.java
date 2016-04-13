package com.bob.bobchat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import com.bob.bobchat.BobApplication;
import com.bob.bobchat.R;
import com.bob.bobchat.utils.ChatHelper;
import com.hyphenate.chat.EMClient;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 开屏页
 */
public class SplashActivity extends BaseActivity {

    private static final int sleepTime = 2000;

    @Inject
    ChatHelper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((BobApplication) getApplication()).getBuild().inject(this);
        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.splash_root);
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(1500);
        rootLayout.startAnimation(animation);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initToolBar() {
        toolbar.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                if (helper.isLoggedIn()) {
                    // ** 免登陆情况 加载所有本地群和会话
                    //不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
                    //加上的话保证进了主页面会话和群组都已经load完毕
                    long start = System.currentTimeMillis();
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    long costTime = System.currentTimeMillis() - start;
                    //等待sleeptime时长
                    if (sleepTime - costTime > 0) {
                        try {
                            Thread.sleep(sleepTime - costTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //进入主页面
                    subscriber.onNext("");
                } else {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    subscriber.onNext("");
                }
            }
        });

        compositeSubscription.add(observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        //进入主页面
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                }));
    }

}
