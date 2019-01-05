package com.example.kushal.represent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Kushal on 9/27/18.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    Bundle args;

    public PagerAdapter(FragmentManager fm, int NumOfTabs, Bundle var) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.args = var;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TabFragment1 tab1 = new TabFragment1();
                tab1.setArguments(args);
                return tab1;
            case 1:
                TabFragment2 tab2 = new TabFragment2();
                tab2.setArguments(args);
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
