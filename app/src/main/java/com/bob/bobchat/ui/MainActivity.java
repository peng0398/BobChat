package com.bob.bobchat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.widget.Toast;

import com.bob.bobchat.BobApplication;
import com.bob.bobchat.R;
import com.bob.bobchat.ui.fragment.ContactFragment;
import com.bob.bobchat.ui.fragment.SettingFragment;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * 作者 bob
 * 日期 16-2-25.
 */
public class MainActivity extends BaseActivity {

    private static final int ONMESSAGE_RECEIVER = 1;
    private static final int ONCMDMESSAGE_RECEIVER = 2;
    private static final int ONMESSAGE_READACKRECEIVED = 3;
    private static final int ONMESSAGEDELIVERYACK_RECEIVED = 4;
    private static final int ONMESSAGE_CHANGED = 5;

    @Bind(R.id.sliding_tabs)
    TabLayout sliding_tabs;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Inject
    BobApplication application;

    long clickback_Time = 0;

    private List<CharSequence> titles;

    static class MainHandler extends Handler {

        WeakReference<MainActivity> reference;

        MainActivity activity;

        public MainHandler(MainActivity activity) {
            this.reference = new WeakReference<MainActivity>(activity);
            this.activity = reference.get();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ONMESSAGE_RECEIVER:
                    Toast.makeText(activity.application, "onMessageReceived", Toast.LENGTH_SHORT).show();
                    break;
                case ONCMDMESSAGE_RECEIVER:
                    Toast.makeText(activity.application, "onCmdMessageReceived", Toast.LENGTH_SHORT).show();
                    break;
                case ONMESSAGE_READACKRECEIVED:
                    Toast.makeText(activity.application, "onmessage_readackreceived", Toast.LENGTH_SHORT).show();
                    break;
                case ONMESSAGEDELIVERYACK_RECEIVED:
                    Toast.makeText(activity.application, "onmessagedeliveryack_received", Toast.LENGTH_SHORT).show();
                    break;
                case ONMESSAGE_CHANGED:
                    Toast.makeText(activity.application, "onmessage_changed", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private MainHandler mainHandler = new MainHandler(this);


    EMMessageListener msgListener = new EMMessageListener() {


        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            mainHandler.sendEmptyMessage(ONMESSAGE_RECEIVER);
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            mainHandler.sendEmptyMessage(ONCMDMESSAGE_RECEIVER);
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
            mainHandler.sendEmptyMessage(ONMESSAGE_READACKRECEIVED);
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
            mainHandler.sendEmptyMessage(ONMESSAGEDELIVERYACK_RECEIVED);
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            mainHandler.sendEmptyMessage(ONMESSAGE_CHANGED);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BobApplication.application.getBuild().inject(this);

        titles = new ArrayList<CharSequence>();
        titles.add("会话");
        titles.add("联系人");
        titles.add("设置");

        final SparseArray<Fragment> mFragments = new SparseArray<>();

        //添加会话fragment
        EaseConversationListFragment conversationListFragment = new EaseConversationListFragment();

        conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("user_info", conversation.getUserName());
                startActivity(intent);
            }
        });

        mFragments.put(0, conversationListFragment);
        //添加联系人列表fragment
        mFragments.put(1, new ContactFragment());
        //添加设置fragment
        mFragments.put(2, new SettingFragment());

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        };

        viewPager.setAdapter(fragmentPagerAdapter);
        sliding_tabs.setupWithViewPager(viewPager);
        //替换默认的ViewPageOnTabSelectedListener，保证切换时没有scrolling效果
        sliding_tabs.setOnTabSelectedListener(new ViewPagerOnTabSelectedListener(viewPager));
        sliding_tabs.setTabMode(TabLayout.MODE_FIXED);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(sliding_tabs) {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                toolbar.setTitle(titles.get(position));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        EaseUI.getInstance().pushActivity(this);
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }


    @Override
    protected void onStop() {
        super.onStop();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        EaseUI.getInstance().popActivity(this);
    }


    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initToolBar() {
        toolbar.setTitle("会话");
    }


    public class ViewPagerOnTabSelectedListener implements TabLayout.OnTabSelectedListener {
        private final ViewPager mViewPager;

        public ViewPagerOnTabSelectedListener(ViewPager viewPager) {
            mViewPager = viewPager;
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            //覆盖之前的方法:传入false，保证fragment切换时不会有scrollinng效果
            mViewPager.setCurrentItem(tab.getPosition(), false);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            // No-op
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            // No-op
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 退出应用程序
     */
    private void exit() {

        if (System.currentTimeMillis() - clickback_Time > 2000) {
            clickback_Time = System.currentTimeMillis();
            Toast.makeText(application, "再按一次退出程序", Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }
}
