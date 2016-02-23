package com.bob.bobchat;

import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者 bob
 * 日期 16-2-23.
 */
public class RegistActivity extends BaseActivity {

    private static final String TAG = "RegistActivity";
    @Bind(R.id.et_user)
    EditText et_user;

    @Bind(R.id.et_pwd)
    EditText et_pwd;

    @OnClick(R.id.btn_regist)
    void login() {
        final String user = et_user.getText().toString().trim();
        final String pwd = et_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pwd)) {
            Toast.makeText(BobApplication.getAppContext(), "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
        }

        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                //注册失败会抛出HyphenateException
                try {
                    // 调用sdk注册方法
                    EMClient.getInstance().createAccount(user, pwd);//同步方法
                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });

        compositeSubscription.add(
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {

                            @Override
                            public void onCompleted() {
                                Log.i(TAG, ".......onCompleted..........");
                                compositeSubscription.remove(this);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.i(TAG, ".......onError..........");
                                //注册失败
                                Toast.makeText(getApplicationContext(), "注册失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(String s) {
                                Log.i(TAG, ".......onNext..........");
                                Toast.makeText(BobApplication.getAppContext(), "注册成功", Toast.LENGTH_SHORT).show();
                                //进行登陆操作
                                finish();
                            }
                        })
        );
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_regist;
    }
}
