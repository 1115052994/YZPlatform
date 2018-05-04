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
import com.xtzhangbinbin.jpq.adapter.InsuranceSupermarketAdapter;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.SupermarketBean;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InsuranceSupermarket extends BaseActivity {

    @BindView(R.id.bxcs)
    ListView bxcs;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.browsing_Cardeal_image)
    ImageView browsingCardealImage;
    private InsuranceSupermarketAdapter financialAdapter;
    private int pageIndex = 1;//第几页
    private int pageCount;//总页数
    private List<SupermarketBean.DataBean.ResultBean> result = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_supermarket);
        ButterKnife.bind(this);
        result.clear();
        financialAdapter = new InsuranceSupermarketAdapter(InsuranceSupermarket.this, result);
        bxcs.setAdapter(financialAdapter);
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
                    getData(++pageIndex, refreshlayout);
            }
        });
    }

    private void getData(final int pageIndex, final RefreshLayout refreshlayout) {
        Map<String, String> map = new HashMap<>();
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("pageSize", String.valueOf(15));
        OKhttptils.post(InsuranceSupermarket.this, Config.QUERYALLINSURANCE, map, new
                OKhttptils.HttpCallBack() {
                    @Override
                    public void success(String response) {
                        Log.d("aaaaa", "onResponse获取数据: " + response);
                        Gson gson = GsonFactory.create();
                        SupermarketBean enterprise = gson.fromJson(response, SupermarketBean.class);
                        pageCount=enterprise.getData().getPageCount();
                        result.addAll(enterprise.getData().getResult());
                        android.util.Log.d("aaaaaaa", "success: "+result.size());

                        financialAdapter.notifyDataSetChanged();
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
                                    financialAdapter.notifyDataSetChanged();
                                    refreshlayout.finishRefresh(2000);
                                    refreshlayout.finishLoadmore(2000);
                                }
                            } else {
                                financialAdapter.notifyDataSetChanged();
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
        setTitle("保险超市");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(InsuranceSupermarket.this);
            }
        });
    }
}
