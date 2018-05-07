package com.xtzhangbinbin.jpq.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TableLayout;

import com.androidkun.xtablayout.XTabLayout;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.CarFinancialAdapter;
import com.xtzhangbinbin.jpq.fragment.CarFinancialPersonal;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CarFinancial extends AppCompatActivity {

    @BindView(R.id.tab)
    XTabLayout tab;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private ArrayList<Fragment> fragments=new ArrayList<>();
    private ArrayList<String> arrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_financial);
        ButterKnife.bind(this);
        fragments.add(new CarFinancialPersonal());
//        fragments.add(new CarFinancialPersonal());
        arrayList.add("个人");
//        arrayList.add("企业");
        viewPager.setAdapter(new CarFinancialAdapter(getSupportFragmentManager(),fragments, arrayList));
        tab.setxTabDisplayNum(1);
        tab.setupWithViewPager(viewPager);

    }
}

