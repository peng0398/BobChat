package com.bob.bobchat.utils;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * 作者 bob
 * 日期 16-2-25.
 */

public class ChatHelper {

    /**
     * 是否登录成功过
     *
     * @return
     */
    public boolean isLoggedIn() {
        return EMClient.getInstance().isLoggedInBefore();
    }

    /**
     * 获取联系人列表
     */
    public Observable<List<String>> getContacts() {

        return Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                try {
                    List<String> allContactsFromServer = EMClient.getInstance().contactManager().getAllContactsFromServer();

                    //TODO 数据库存储操作
                    //传递给页面
                    subscriber.onNext(allContactsFromServer);

                } catch (HyphenateException e) {
                    subscriber.onError(e);
                }
                subscriber.onCompleted();
            }
        });
    }
}
