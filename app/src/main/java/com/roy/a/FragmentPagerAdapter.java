package com.roy.a;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.roy.R;
import com.roy.ui.FApps;
import com.roy.ui.FLens;
import com.roy.ui.FSettings;

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_PAGES = 3;

    private final Context mContext;

    public FragmentPagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FLens.newInstance();
            case 1:
                return FApps.newInstance();
            case 2:
                return FSettings.newInstance();
        }
        return new Fragment();
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getResources().getString(R.string.tab_lens);
            case 1:
                return mContext.getResources().getString(R.string.tab_apps);
            case 2:
                return mContext.getResources().getString(R.string.tab_settings);
        }
        return super.getPageTitle(position);
    }
}