package com.bob.bobchat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

/**
 * 作者 bob
 * 日期 16-2-23.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected CompositeSubscription compositeSubscription = new CompositeSubscription();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        ButterKnife.bind(this);
    }

    /**
     * 加载布局文件
     * @return
     */
    protected abstract int initLayout();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消所有订阅
        compositeSubscription.unsubscribe();
    }
}
