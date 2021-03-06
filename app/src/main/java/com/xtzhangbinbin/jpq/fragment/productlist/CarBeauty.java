package com.xtzhangbinbin.jpq.fragment.productlist;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mm.opensdk.utils.Log;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.activity.AddProductActivity;
import com.xtzhangbinbin.jpq.adapter.CarBeautyAdapter;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.CarBeautyBean;
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
public class CarBeauty extends Fragment {


    @BindView(R.id.car_beauty_list)
    RecyclerView carBeautyList;
    Unbinder unbinder;
    @BindView(R.id.radio)
    Button radio;
    @BindView(R.id.browsing_Cardeal_image)
    ImageView browsingCardealImage;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    private CarBeautyAdapter carBeautyAdapter;
    private ArrayList<String> arr = new ArrayList<>();
    private int pageIndex = 1;//第几页
    private int pageCount = 1;//总页数  默认
    private List<CarBeautyBean.DataBean.ResultBean>  result = new ArrayList<>();
    private String prod_service_type_item;

    @SuppressLint("ValidFragment")
    public CarBeauty(String prod_service_type_item) {
        this.prod_service_type_item = prod_service_type_item;
    }

    public CarBeauty(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_car_beauty, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        result.clear();
        //设置线性管理器
        carBeautyList.setLayoutManager(new LinearLayoutManager(getContext()));
        carBeautyAdapter = new CarBeautyAdapter(getContext(),result);
        carBeautyList.setAdapter(carBeautyAdapter);
        carBeautyAdapter.getCall(new CarBeautyAdapter.onCallBack() {
            @Override
            public void getprodid(View view, String prod_id) {
                android.util.Log.d("aaaaa", "getprodid: "+prod_id);
                getData(1,null);
                carBeautyAdapter.notifyDataSetChanged();
            }
        });
//        getData(1, null);
        radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddProductActivity.class);
                startActivity(intent);
            }
        });
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
        return inflate;
    }
    private void initview() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if(result != null){
            result.clear();
        }
        getData(1, null);
    }

    //得到数据
    private void getData(final int pageIndex, final RefreshLayout refreshlayout) {
        Map<String, String> map = new HashMap<>();
        map.put("prod_service_type_item", prod_service_type_item);
        map.put("pageIndex",String.valueOf(pageIndex));
        OKhttptils.post((Activity) getContext(), Config.COMPPRODUCT, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.d("aaaaa", "onResponse456: " + response);
                Gson gson = GsonFactory.create();
                CarBeautyBean carBeauty = gson.fromJson(response, CarBeautyBean.class);
                pageCount=carBeauty.getData().getPageCount();
                result.addAll(carBeauty.getData().getResult());
                carBeautyAdapter.notifyDataSetChanged();

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
                            carBeautyAdapter.notifyDataSetChanged();
                            refreshlayout.finishRefresh(2000);
                            refreshlayout.finishLoadmore(2000);
                        }
                    } else {
                        carBeautyAdapter.notifyDataSetChanged();
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
