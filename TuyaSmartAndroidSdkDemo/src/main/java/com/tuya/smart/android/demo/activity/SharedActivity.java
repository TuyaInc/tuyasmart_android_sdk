package com.tuya.smart.android.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.fragment.SharedReceivedFragment;
import com.tuya.smart.android.demo.fragment.SharedSentFragment;
import com.tuya.smart.android.demo.utils.ViewUtils;
import com.tuya.smart.android.demo.widget.PagerSlidingTabStrip;
import com.tuya.smart.android.demo.widget.ScrollViewPager;


/**
 * Created by leaf on 15/12/17.
 * 共享设备
 */
public class SharedActivity extends BaseActivity {

    private static final String TAG = "SharedActivity";

    public static final String CURRENT_TAB = "current_tab";
    public static final int TABS_COUNT = 2;
    public static final int TAB_SENT = 0;
    public static final int TAB_RECEIVED = 1;

    PagerSlidingTabStrip mSlidingTab;
    ScrollViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared);
        mSlidingTab = (PagerSlidingTabStrip) findViewById(R.id.pager_sliding_tab);
        mPager = (ScrollViewPager) findViewById(R.id.pager);
        initToolbar();
        initMenu();
        initPagerView();
    }

    private void initMenu() {
        setTitle(getString(R.string.shared_title));
        setDisplayHomeAsUpEnabled();
    }

    private void initPagerView() {
        int currentTab = TAB_SENT;
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(CURRENT_TAB)) {
            currentTab = intent.getIntExtra(CURRENT_TAB, 0);
        }

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mPager.setLocked(false);
        mPager.setOffscreenPageLimit(TABS_COUNT);
        mPager.setAdapter(adapter);
        mSlidingTab.setViewPager(mPager);
        mSlidingTab.setSelectedColor(ViewUtils.getColor(this, R.color.navbar_font_color));
        mSlidingTab.setAllCaps(false);
        mPager.setCurrentItem(currentTab);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case TAB_SENT:
                    return SharedSentFragment.newInstance();
                case TAB_RECEIVED:
                    return SharedReceivedFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return TABS_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case TAB_SENT:
                    return getString(R.string.ty_add_share_tab1);
                case TAB_RECEIVED:
                    return getString(R.string.ty_add_share_tab2);
                default:
                    return null;
            }

        }
    }
}