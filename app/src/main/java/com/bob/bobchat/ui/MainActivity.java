package com.bob.bobchat.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;

import com.bob.bobchat.R;
import com.bob.bobchat.ui.fragment.BaseFragment;
import com.bob.bobchat.ui.fragment.ContactFragment;
import com.bob.bobchat.ui.fragment.ConversationFragment;
import com.bob.bobchat.ui.fragment.SettingFragment;

import java.util.ArrayList;
import java.util.List;

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

        titles = new ArrayList<CharSequence>();
        titles.add("会话");
        titles.add("联系人");
        titles.add("设置");

        final SparseArray<BaseFragment> mFragments = new SparseArray<>();

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
        //替换默认的ViewPageOnTabSelectedListener，保证切换时没有scrolling效果
        sliding_tabs.setOnTabSelectedListener(new ViewPagerOnTabSelectedListener(viewPager));
        sliding_tabs.setTabMode(TabLayout.MODE_FIXED);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(sliding_tabs){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                toolbar.setTitle(titles.get(position));
            }
        });
    }

    @Override
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
            mViewPager.setCurrentItem(tab.getPosition(),false);
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
