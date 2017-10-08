package com.example.android.newsbox;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by victo on 9/27/2017.
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    private Context context;

    public FragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentHighlights();
            case 1:
                return new FragmentTechnology();
            case 2:
                return new FragmentScience();
            case 3:
                return new FragmentHealth();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.fragment_1);
            case 1:
                return context.getString(R.string.fragment_2);
            case 2:
                return context.getString(R.string.fragment_3);
            case 3:
                return context.getString(R.string.fragment_4);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
