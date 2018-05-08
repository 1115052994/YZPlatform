package com.xtzhangbinbin.jpq.fragment.productlist;


import android.app.Activity;
import android.content.Intent;
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
import com.xtzhangbinbin.jpq.activity.AddCarProduct;
import com.xtzhangbinbin.jpq.activity.AddProductActivity;
import com.xtzhangbinbin.jpq.adapter.WeisjAdapter;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.WeisjBean;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.view.MyProgressDialog;

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
public class WeiSJ extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.radio)
    Button radio;
    @BindView(R.id.browsing_Cardeal_image)
    ImageView browsingCardealImage;
    @BindView(R.id.weisj_list)
    RecyclerView weisjList;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;

    private List<WeisjBean.DataBean.ResultBean> result = new ArrayList<>();
    private WeisjAdapter weisjAdapter;
    private int pageIndex = 1;//第几页
    private int pageCount;//总页数
    private MyProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_weisj, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        result.clear();
        //设置线性管理器
        weisjList.setLayoutManager(new LinearLayoutManager(getContext()));
        weisjAdapter = new WeisjAdapter(getContext(), result);
        weisjList.setAdapter(weisjAdapter);
        dialog = MyProgressDialog.createDialog(getContext());
        dialog.setMessage("正在加载数据，请稍候！");
        dialog.show();
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
                if(pageIndex < pageCount){
                    getData(++pageIndex, refreshlayout);
                }else {
                    refreshlayout.finishLoadmore();
                }

            }
        });
        radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddCarProduct.class);
                startActivity(intent);
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
        return inflate;
    }

    private void initview() {

    }

    private void getData(final int pageIndex, final RefreshLayout refreshlayout) {
        Map<String, String> map = new HashMap<>();
        map.put("car_deal_state", "wsj");
        map.put("pageIndex",String.valueOf(pageIndex));
        OKhttptils.post((Activity) getContext(), Config.COMPCAR, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.d("aaaaa", "onResponse获取数据: " + response);
                Gson gson = GsonFactory.create();
                WeisjBean weisjBean = gson.fromJson(response, WeisjBean.class);
                pageCount=weisjBean.getData().getPageCount();
                result.addAll(weisjBean.getData().getResult());
                weisjAdapter.notifyDataSetChanged();

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
                            weisjAdapter.notifyDataSetChanged();
                            refreshlayout.finishRefresh(2000);
                            refreshlayout.finishLoadmore(2000);
                        }
                    } else {
                        weisjAdapter.notifyDataSetChanged();
                    }
                }
                if(dialog != null && dialog.isShowing()){
                    dialog.dismiss();
                }

            }

            @Override
            public void fail(String response) {
                Log.d("aaaa", "fail: " + response);
                if(dialog != null && dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }
}
