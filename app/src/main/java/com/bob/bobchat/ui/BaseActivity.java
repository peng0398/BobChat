package com.bob.bobchat.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.bob.bobchat.R;

import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

/**
 * 作者 bob
 * 日期 16-2-23.
 */
public abstract class BaseActivity extends AppCompatActivity {

    FrameLayout fl_content;
    Toolbar toolbar;
    protected CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        fl_content = ((FrameLayout) findViewById(R.id.fl_content));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.fl_content.addView(View.inflate(this, initLayout(), null));
        ButterKnife.bind(this);
    }

    /**
     * 加载布局文件
     */
    protected abstract int initLayout();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消所有订阅
        compositeSubscription.unsubscribe();
    }
}
