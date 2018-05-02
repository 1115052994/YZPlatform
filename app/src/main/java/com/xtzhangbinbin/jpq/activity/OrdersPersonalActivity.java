package com.xtzhangbinbin.jpq.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.CollectAdapter;
import com.xtzhangbinbin.jpq.alipay.AlipayUtil;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.ShowProductDetaile;
import com.xtzhangbinbin.jpq.fragment.orderspersonal.OrdersPersonalAllFragment;
import com.xtzhangbinbin.jpq.fragment.orderspersonal.OrdersPersonalCancelFragment;
import com.xtzhangbinbin.jpq.fragment.orderspersonal.OrdersPersonalNoPayFragment;
import com.xtzhangbinbin.jpq.fragment.orderspersonal.OrdersPersonalPayFragment;
import com.xtzhangbinbin.jpq.fragment.orderspersonal.OrdersPersonalUseFragment;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.ActivityUtil;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.StringUtil;
import com.xtzhangbinbin.jpq.utils.TabUtil;
import com.xtzhangbinbin.jpq.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/25 0025.
 */

public class OrdersPersonalActivity extends BaseActivity {

    @BindView(R.id.orders_personal_tab)
    TabLayout ordersTab;
    @BindView(R.id.orders_personal_pager)
    ViewPager ordersPager;
    private ArrayList<Fragment> fragment = new ArrayList<>();
    private ArrayList<String> str = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_personal);
        ButterKnife.bind(this);
        ActivityUtil.addActivity(this);
        initFragment();
    }

    public void initFragment(){
        fragment.add(new OrdersPersonalAllFragment(this));
        fragment.add(new OrdersPersonalNoPayFragment(this));
        fragment.add(new OrdersPersonalPayFragment(this));
        fragment.add(new OrdersPersonalUseFragment(this));
        fragment.add(new OrdersPersonalCancelFragment(this));
        str.add("全部订单");
        str.add("未支付");
        str.add("待使用");
        str.add("已使用");
        str.add("已取消");
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
