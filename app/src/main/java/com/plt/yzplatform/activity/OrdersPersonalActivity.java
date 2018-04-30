package com.plt.yzplatform.activity;

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
import com.plt.yzplatform.R;
import com.plt.yzplatform.adapter.CollectAdapter;
import com.plt.yzplatform.alipay.AlipayUtil;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.entity.ShowProductDetaile;
import com.plt.yzplatform.fragment.orderspersonal.OrdersPersonalAllFragment;
import com.plt.yzplatform.gson.factory.GsonFactory;
import com.plt.yzplatform.utils.ActivityUtil;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.OKhttptils;
import com.plt.yzplatform.utils.StringUtil;
import com.plt.yzplatform.utils.TabUtil;
import com.plt.yzplatform.utils.ToastUtil;

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
        str.add("全部订单");
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
