package com.xtzhangbinbin.jpq.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/20.
 */

public class CollectAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragment;
    private ArrayList<String> str;

    public CollectAdapter(FragmentManager fm, ArrayList<Fragment> fragment, ArrayList<String> str) {
        super(fm);
        this.fragment = fragment;
        this.str = str;
    }

    @Override
    public Fragment getItem(int position) {
        return fragment.get(position);
    }

    @Override
    public int getCount() {
        return fragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return str.get(position);
    }
}
