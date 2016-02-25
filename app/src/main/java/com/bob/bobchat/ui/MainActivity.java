package com.bob.bobchat.ui;

import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.bob.bobchat.R;
import com.bob.bobchat.ui.fragment.BaseFragment;
import com.bob.bobchat.ui.fragment.ContactFragment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sliding_tabs.addTab(sliding_tabs.newTab().setText("Tab 1"));
        sliding_tabs.addTab(sliding_tabs.newTab().setText("Tab 2"));
        sliding_tabs.addTab(sliding_tabs.newTab().setText("Tab 3"));

        final Map<Integer, BaseFragment> mFragments = new HashMap<Integer, BaseFragment>();

        mFragments.put(0,new ContactFragment());
        mFragments.put(1,new ContactFragment());
        mFragments.put(2,new ContactFragment());

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };


        viewPager.setAdapter(fragmentPagerAdapter);
        sliding_tabs.setTabsFromPagerAdapter(fragmentPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(sliding_tabs));
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }
}
