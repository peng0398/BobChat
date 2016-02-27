package com.bob.bobchat.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.bob.bobchat.R;
import com.bob.bobchat.ui.fragment.BaseFragment;
import com.bob.bobchat.ui.fragment.ContactFragment;
import com.bob.bobchat.ui.fragment.ConversationFragment;
import com.bob.bobchat.ui.fragment.SettingFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private List<CharSequence> titles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbar.setTitle("BobChat 1.0");
        titles = new ArrayList<CharSequence>();

        titles.add("对话");
        titles.add("联系人");
        titles.add("设置");

        final Map<Integer, BaseFragment> mFragments = new HashMap<Integer, BaseFragment>();

        mFragments.put(0, new ConversationFragment());
        mFragments.put(1, new ContactFragment());
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
        sliding_tabs.setTabMode(TabLayout.MODE_FIXED);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(sliding_tabs));
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }
}
