package com.xtzhangbinbin.jpq.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/5/4.
 */

public class CarFinancialAdapter extends FragmentPagerAdapter{
    private ArrayList<Fragment> fragments;
    private ArrayList<String> arrayList;
    public CarFinancialAdapter(FragmentManager fm, ArrayList<Fragment> fragments, ArrayList<String> arrayList) {
        super(fm);
        this.fragments = fragments;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return arrayList.get(position);
    }
}
