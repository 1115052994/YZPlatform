package com.xtzhangbinbin.jpq.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidkun.xtablayout.XTabLayout;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.CarFinancialAdapter;
import com.xtzhangbinbin.jpq.fragment.CarFinancialPersonal;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by glp on 2018/4/18.
 * 描述：车金融
 */

public class CarMoneyFragment extends Fragment {
    @BindView(R.id.tab)
    XTabLayout tab;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    Unbinder unbinder;
    private ArrayList<Fragment> fragments=new ArrayList<>();
    private ArrayList<String> arrayList=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.activity_car_financial, container, false);
        unbinder = ButterKnife.bind(this, inflate);


        fragments.add(new CarFinancialPersonal());
//        fragments.add(new CarFinancialPersonal());
        arrayList.add("个人");//
//        arrayList.add("企业");
        viewPager.setAdapter(new CarFinancialAdapter(getChildFragmentManager(),fragments, arrayList));
        tab.setxTabDisplayNum(1);
        tab.setupWithViewPager(viewPager);
        tab.setVisibility(View.GONE);
        return inflate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
