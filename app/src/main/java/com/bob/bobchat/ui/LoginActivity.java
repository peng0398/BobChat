package com.bob.bobchat.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.bob.bobchat.BobApplication;
import com.bob.bobchat.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    @Bind(R.id.et_user)
    EditText et_user;

    @Bind(R.id.et_pwd)
    EditText et_pwd;

    @OnClick(R.id.btn_regist)
    void regist() {
        Intent intent = new Intent(this, RegistActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_login)
    void login() {
        final String user = et_user.getText().toString().trim();
        final String pwd = et_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pwd)) {
            Toast.makeText(BobApplication.getAppContext(), "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
        }

        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {


                EMClient.getInstance().login(user, pwd, new EMCallBack() {//回调
                    @Override
                    public void onSuccess() {
                        subscriber.onNext("");
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }

                    @Override
                    public void onError(int code, String message) {
                        Log.d("main", "登陆聊天服务器失败！" + "code:" + code + "  message: " + message);
                        if (code == 202) {
                            Toast.makeText(BobApplication.getAppContext(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        compositeSubscription.add(
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {
                                compositeSubscription.remove(this);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(String s) {
                                EMClient.getInstance().groupManager().loadAllGroups();
                                EMClient.getInstance().chatManager().loadAllConversations();
                                Log.d("main", "登陆聊天服务器成功！");
                                Toast.makeText(BobApplication.getAppContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
        );
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }

}
