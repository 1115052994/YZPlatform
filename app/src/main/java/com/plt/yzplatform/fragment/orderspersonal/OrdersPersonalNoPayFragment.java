package com.plt.yzplatform.fragment.orderspersonal;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.plt.yzplatform.R;
import com.plt.yzplatform.adapter.OrdersPersonalAdapter;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.entity.Orders;
import com.plt.yzplatform.enums.OrderPersState;
import com.plt.yzplatform.gson.factory.GsonFactory;
import com.plt.yzplatform.utils.NetUtil;
import com.plt.yzplatform.utils.OKhttptils;
import com.plt.yzplatform.utils.Prefs;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class OrdersPersonalNoPayFragment extends Fragment {
    @BindView(R.id.orders_personal_default_image)
    ImageView defaultImage;
    @BindView(R.id.orders_personal_listview)
    ListView ordersListView;
    Unbinder unbinder;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;


    private Context context;
    private List<Orders.DataBean.ResultBean> result = new ArrayList<>();
    private OrdersPersonalAdapter ordersPersonalAdapter;
    private int pageIndex = 1;//第几页
    private int pageCount;//总页数

    public OrdersPersonalNoPayFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_orders_personal_all, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        result = new ArrayList<>();
        ordersPersonalAdapter = new OrdersPersonalAdapter(context, result);
        ordersListView.setAdapter(ordersPersonalAdapter);
        defaultImage.setVisibility(View.GONE);
//        //  浏览记录地址
//        getOrders(Config.ORDERS_GET_LIST, 1, null);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageIndex = 1;
                result.clear();
                getOrders(Config.ORDERS_GET_LIST, 1, refreshlayout);
            }
        });
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getOrders(Config.ORDERS_GET_LIST, ++pageIndex, refreshlayout);
            }
        });
        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        getOrders(Config.ORDERS_GET_LIST, 1, null);
    }

    public void getOrders(String url, final int pageIndex, final RefreshLayout refreshlayout) {
        Map<String, String> map = new HashMap<>();
        map.put("order_type", OrderPersState.wzf.toString());
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("pageSize", "");
        if (NetUtil.isNetAvailable(context)) {
            OKhttptils.post((Activity) context, url, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    Gson gson = GsonFactory.create();
                    Orders orders = gson.fromJson(response, Orders.class);
                    pageCount = orders.getData().getPageCount();
                    List<Orders.DataBean.ResultBean> result2 = orders.getData().getResult();
                    result.addAll(result2);
                    //没有信息图片显示
                    if (OrdersPersonalNoPayFragment.this.result.size() <= 0) {
                        defaultImage.setVisibility(View.VISIBLE);
                    } else {
                        if (refreshlayout != null) {
                            if (pageIndex > pageCount) {
                                refreshlayout.finishLoadmore(2000);
                            } else {
                                ordersPersonalAdapter.notifyDataSetChanged();
                                refreshlayout.finishRefresh(2000);
                                refreshlayout.finishLoadmore(2000);
                            }
                        } else {
                            ordersPersonalAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void fail(String response) {
                    Toast.makeText(context, "查询失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
