package com.xtzhangbinbin.jpq.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mm.opensdk.utils.Log;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.CarCreditAdapter;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.CreditBean;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CarCredit extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.browsing_Cardeal_image)
    ImageView browsingCardealImage;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    private  List<CreditBean.DataBean.ResultBean> result = new ArrayList<>();
    private CarCreditAdapter carCreditAdapter;
    private int pageIndex=1;
    private int pageTotal;//总页数
    private String auth_comp_city;
    private String dict_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_credit);
        ButterKnife.bind(this);

        //设置线性管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        carCreditAdapter = new CarCreditAdapter(this, result);
        recyclerView.setAdapter(carCreditAdapter);
        result.clear();
        getData(1,null);
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
                if(pageIndex < pageTotal){
                    getData(++pageIndex, refreshlayout);
                }else {
                    refreshlayout.finishLoadmore();
                }

            }
        });
    }


    //数据请求
    private void getData(final int pageIndex, final RefreshLayout refreshlayout) {
        Map<String, String> map = new HashMap<>();

        map.put("pageIndex", String.valueOf(pageIndex));
        OKhttptils.post(this, Config.QUERYALLTWO, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.d("aaaaa", "onResponse获取数据: " + response);
                Gson gson = GsonFactory.create();
                CreditBean enterprise = gson.fromJson(response, CreditBean.class);
                pageTotal=enterprise.getData().getPageTotal();
                result.addAll(enterprise.getData().getResult());
                carCreditAdapter.notifyDataSetChanged();
                //没有信息图片显示
                if (result.size() <= 0) {
                    browsingCardealImage.setVisibility(View.VISIBLE);
                } else {
                    if (refreshlayout != null) {
                        if (pageIndex > pageTotal) {
                            refreshlayout.finishLoadmore(2000);
                        } else {
                            carCreditAdapter.notifyDataSetChanged();
                            refreshlayout.finishRefresh(2000);
                            refreshlayout.finishLoadmore(2000);
                        }
                    } else {
                        carCreditAdapter.notifyDataSetChanged();
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
        setTitle("车信贷");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(CarCredit.this);
            }
        });
    }

}
