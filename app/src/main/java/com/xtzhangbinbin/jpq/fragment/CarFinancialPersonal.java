package com.xtzhangbinbin.jpq.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.mm.opensdk.utils.Log;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.activity.CarCredit;
import com.xtzhangbinbin.jpq.activity.InsuranceSupermarket;
import com.xtzhangbinbin.jpq.activity.WebView;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.adapter.FinancialAdapter;
import com.xtzhangbinbin.jpq.entity.FinancialBean;
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
public class CarFinancialPersonal extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.car_cx)
    LinearLayout carCx;
    @BindView(R.id.car_bx)
    LinearLayout carBx;
    @BindView(R.id.car_cd)
    LinearLayout carCd;
    @BindView(R.id.dkjsq)
    LinearLayout dkjsq;
    @BindView(R.id.xyd)
    LinearLayout xyd;
    @BindView(R.id.bxc)
    LinearLayout bxc;
    private FinancialAdapter financialAdapter;
    private List<FinancialBean.DataBean.ResultBean> result = new ArrayList<>();
    private String cxurl, cdurl, bxurl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_car_financial_personal, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        //设置线性管理器
        result.clear();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);//禁止滑动
        recyclerView.setFocusableInTouchMode(false);//让recycler取消焦点  一进来不不会自动跳转到RecyclerView
        financialAdapter = new FinancialAdapter(getContext(), result);
        recyclerView.setAdapter(financialAdapter);
        getData();
        bxc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), InsuranceSupermarket.class));
            }
        });
        //跳转到Webview
        carCx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cxurl!=null){
                    Intent intent = new Intent(getActivity(), WebView.class);
                    intent.putExtra("Url",cxurl);
                    startActivity(intent);
                }else {
                    Toast.makeText(getContext(), "cxurl不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        carBx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bxurl!=null){
                    Intent intent = new Intent(getActivity(), WebView.class);
                    intent.putExtra("Url",bxurl);
                    startActivity(intent);
                }else {
                    Toast.makeText(getContext(), "bxurl不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        carCd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cdurl!=null){
                    Intent intent = new Intent(getActivity(), WebView.class);
                    intent.putExtra("Url",cdurl);
                    startActivity(intent);
                }else {
                    Toast.makeText(getContext(), "cdurl不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        xyd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CarCredit.class));
            }
        });
        //贷款计算器
        dkjsq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebView.class);
                intent.putExtra("Url","http://newhouse.fang.com/jsq/tq.htm");
                startActivity(intent);
            }
        });

        return inflate;
    }

    //数据请求
    private void getData() {
        Map<String, String> map = new HashMap<>();
//        map.put("car_id","15985");
        OKhttptils.post((Activity) getContext(), Config.SELECTJRTOAPP, map, new
                OKhttptils.HttpCallBack() {
                    @Override
                    public void success(String response) {
                        Log.d("aaaaa", "onResponse获取数据:22222 " + response);
                        Gson gson = GsonFactory.create();
                        FinancialBean enterprise = gson.fromJson(response, FinancialBean.class);
                        result.addAll(enterprise.getData().getResult());
                        financialAdapter.notifyDataSetChanged();
                        for (int i = 0; i < result.size(); i++) {
                            switch (result.get(i).getJr_name()) {
                                case "车险":
                                    cxurl = result.get(i).getJr_turn_url();
                                    break;
                                case "保险服务":
                                    bxurl = result.get(i).getJr_turn_url();
                                    break;
                                case "平安银行新一贷":
                                    cdurl = result.get(i).getJr_turn_url();
                                    break;
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
