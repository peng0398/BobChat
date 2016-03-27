package com.bob.bobchat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * 作者 bob
 * 日期 16-2-25.
 */
public class MainActivity extends BaseActivity {

    @Bind(R.id.sliding_tabs)
    TabLayout sliding_tabs;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Inject
    BobApplication application;

    private List<CharSequence> titles;


    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(application, "onMessageReceived", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            Toast.makeText(application, "onCmdMessageReceived", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
            Toast.makeText(application, "onMessageReadAckReceived", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
            Toast.makeText(application, "onMessageDeliveryAckReceived", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            Toast.makeText(application, "onMessageChanged", Toast.LENGTH_SHORT).show();

        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BobApplication.application.getBuild().inject(this);

        Toast.makeText(application, "I am BobApplication", Toast.LENGTH_SHORT).show();

//        registMessageListener();

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

}
