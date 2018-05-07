package com.xtzhangbinbin.jpq.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mm.opensdk.utils.Log;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.EtcAdapter;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.EtcBean;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SOS extends BaseActivity {

    @BindView(R.id.car_deal_listview)
    ListView carDealListview;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.browsing_Cardeal_image)
    ImageView browsingCardealImage;
    private int pageIndex = 1;//第几页
    private int pageCount;//总页数
    private List<EtcBean.DataBean.ResultBean> result = new ArrayList<>();
    private EtcAdapter etcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etc);
        ButterKnife.bind(this);
        result.clear();
        getData(1, null);
        etcAdapter = new EtcAdapter(SOS.this, result);
        carDealListview.setAdapter(etcAdapter);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageIndex = 1;
                result.clear();
                getData(1, refreshlayout);
                refreshlayout.finishRefresh(2000);
            }
        });
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if(pageIndex<pageCount){
                    getData(++pageIndex, refreshlayout);
                }else {
                    refreshlayout.finishLoadmore(2000);
                }

            }
        });

    }

    //数据请求
    private void getData(final int pageIndex, final RefreshLayout refreshlayout) {
        Map<String, String> map = new HashMap<>();
//        map.put("car_id", "15985");
        OKhttptils.post(SOS.this, Config.SELECTROADSIDE, map, new OKhttptils.HttpCallBack() {
                    @Override
                    public void success(String response) {
                        Log.d("aaaaa", "onResponse获取数据: " + response);
                        Gson gson = GsonFactory.create();
                        EtcBean enterprise = gson.fromJson(response, EtcBean.class);
                        result.addAll(enterprise.getData().getResult());
                        etcAdapter.notifyDataSetChanged();

                        //没有信息图片显示
                        if (result.size() <= 0) {
                            browsingCardealImage.setVisibility(View.VISIBLE);
                            if (refreshlayout != null) {
                                refreshlayout.finishRefresh(2000);
                            }
                        } else {
                            browsingCardealImage.setVisibility(View.GONE);
                            if (refreshlayout != null) {
                                if (pageIndex > pageCount) {
                                    refreshlayout.finishLoadmore(2000);
                                } else {
                                    etcAdapter.notifyDataSetChanged();
                                    refreshlayout.finishRefresh(2000);
                                    refreshlayout.finishLoadmore(2000);
                                }
                            } else {
                                etcAdapter.notifyDataSetChanged();
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
    protected void onStart() {
        super.onStart();
        setTitle("紧急救援");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(SOS.this);
            }
        });
    }
}
