package com.xtzhangbinbin.jpq.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.CollectAdapter;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.fragment.orderscomp.OrdersCompAllFragment;
import com.xtzhangbinbin.jpq.fragment.orderscomp.OrdersCompUnUseFragment;
import com.xtzhangbinbin.jpq.fragment.orderscomp.OrdersCompUseFragment;
import com.xtzhangbinbin.jpq.fragment.orderspersonal.OrdersPersonalAllFragment;
import com.xtzhangbinbin.jpq.fragment.orderspersonal.OrdersPersonalCancelFragment;
import com.xtzhangbinbin.jpq.fragment.orderspersonal.OrdersPersonalNoPayFragment;
import com.xtzhangbinbin.jpq.fragment.orderspersonal.OrdersPersonalPayFragment;
import com.xtzhangbinbin.jpq.fragment.orderspersonal.OrdersPersonalUseFragment;
import com.xtzhangbinbin.jpq.utils.ActivityUtil;
import com.xtzhangbinbin.jpq.utils.TabUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/25 0025.
 */

public class OrdersCompListActivity extends BaseActivity {

    @BindView(R.id.orders_comp_tab)
    TabLayout ordersTab;
    @BindView(R.id.orders_comp_pager)
    ViewPager ordersPager;
    private ArrayList<Fragment> fragment = new ArrayList<>();
    private ArrayList<String> str = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_comp_listl);
        ButterKnife.bind(this);
        ActivityUtil.addActivity(this);
        initFragment();
    }

    public void initFragment(){
        fragment.add(new OrdersCompAllFragment(this));
        fragment.add(new OrdersCompUseFragment(this));
        fragment.add(new OrdersCompUnUseFragment(this));
        str.add("全部订单");
        str.add("已消费");
        str.add("未消费");
        ordersPager.setAdapter(new CollectAdapter(getSupportFragmentManager(),fragment,str));
        ordersTab.setupWithViewPager(ordersPager);
        ordersTab.setTabMode(TabLayout.MODE_FIXED);
        TabUtil.showTabTextAdapteIndicator(ordersTab);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("我的订单");
    }

}
