package com.xtzhangbinbin.jpq.fragment.productlist;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mm.opensdk.utils.Log;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.YiwcAdapter;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.WeisjBean;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.OKhttptils;

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
public class YiWC extends Fragment {


    @BindView(R.id.yiwc_list)
    RecyclerView yiwcList;
    Unbinder unbinder;
    @BindView(R.id.radio)
    Button radio;
    @BindView(R.id.browsing_Cardeal_image)
    ImageView browsingCardealImage;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;

    private List<WeisjBean.DataBean.ResultBean> result = new ArrayList<>();
    private YiwcAdapter yiwcAdapter;
    private int pageIndex = 1;//第几页
    private int pageCount;//总页数


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_yiwc, container, false);
        unbinder = ButterKnife.bind(this, view);
        result.clear();
        //设置线性管理器
        yiwcList.setLayoutManager(new LinearLayoutManager(getContext()));
        yiwcAdapter = new YiwcAdapter(getContext(), result);
        yiwcList.setAdapter(yiwcAdapter);
        getData(1, null);

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageIndex = 1;
                result.clear();
                getData(1, refreshlayout);
            }
        });
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if(pageCount>pageIndex){
                    getData(++pageIndex, refreshlayout);
                }
                refreshlayout.finishLoadmore();
            }
        });
        //        browsingAdapter.getBrowsingCall(new CallBrowsing() {
//            @Override
//            public void getCallBrowsing(View view, String is) {
//                //删除
//                result.clear();
//                getData(1, null);
//            }
//        });

        initview();

        return view;
    }
    private void initview() {

    }
    private void getData(final int pageIndex, final RefreshLayout refreshlayout) {
        Map<String, String> map = new HashMap<>();
        map.put("car_deal_state", "ywc");
        map.put("pageIndex",String.valueOf(pageIndex));
        OKhttptils.post((Activity) getContext(), Config.COMPCAR, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.d("aaaaa", "onResponse获取数据: " + response);
                Gson gson = GsonFactory.create();
                WeisjBean weisjBean = gson.fromJson(response, WeisjBean.class);
                pageCount=weisjBean.getData().getPageCount();
                result.addAll(weisjBean.getData().getResult());
                yiwcAdapter.notifyDataSetChanged();

                //没有信息图片显示
                if (result.size() <= 0) {
                    browsingCardealImage.setVisibility(View.VISIBLE);
                    if(refreshlayout != null){
                        refreshlayout.finishRefresh(2000);
                    }
                } else {
                    browsingCardealImage.setVisibility(View.GONE);
                    if (refreshlayout != null) {
                        if (pageIndex > pageCount) {
                            refreshlayout.finishLoadmore(2000);
                        } else {
                            yiwcAdapter.notifyDataSetChanged();
                            refreshlayout.finishRefresh(2000);
                            refreshlayout.finishLoadmore(2000);
                        }
                    } else {
                        yiwcAdapter.notifyDataSetChanged();
                    }
                }


            }

            @Override
            public void fail(String response) {
                Log.d("aaaa", "fail: " + response);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
