package com.plt.yzplatform.fragment;


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
import com.plt.yzplatform.adapter.CallCollect;
import com.plt.yzplatform.adapter.CarListAdapter;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.entity.QueryCarList;
import com.plt.yzplatform.gson.factory.GsonFactory;
import com.plt.yzplatform.utils.NetUtil;
import com.plt.yzplatform.utils.OKhttptils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class CollectCardeal extends Fragment {
    @BindView(R.id.car_deal_listview)
    ListView carDealListview;
    Unbinder unbinder;
    @BindView(R.id.no_collect_server_image)
    ImageView noCollectServerImage;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    private Context context;
    private CarListAdapter carListAdapter;
    private List<QueryCarList.DataBean.ResultBean> result = new ArrayList<>();
    private int pageCount;//总页数
    private int pageIndex = 1;//第几页
    private HashMap<String, String> map = new HashMap<>();

    public CollectCardeal(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_collect_cardeal, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        PostCar(1, null);
        carListAdapter = new CarListAdapter(context, result);
        carDealListview.setAdapter(carListAdapter);
        carListAdapter.getCallCar(new CallCollect() {
            @Override
            public void getCallcollect(View view, int id) {
                result.clear();
                PostCar(1, null);
            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageIndex=1;
                result.clear();
                PostCar(1, refreshlayout);
            }
        });
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                PostCar(pageIndex++, refreshlayout);
            }
        });
        return inflate;
    }

    public void PostCar(final int pageIndex, final RefreshLayout refreshlayout) {
        map.clear();
        if (NetUtil.isNetAvailable(context)) {
            map.put("pageIndex",String.valueOf(pageIndex));
//            map.put("coll_content_id","3");
            OKhttptils.post((Activity) context, Config.CHECKCAR, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    Log.i("aaaaa", "查询二手车收藏: " + response);
                    Gson gson = GsonFactory.create();
                    QueryCarList querystar = gson.fromJson(response, QueryCarList.class);
                    pageCount = querystar.getData().getPageCount();
                    List<QueryCarList.DataBean.ResultBean> result2 = querystar.getData().getResult();
                    CollectCardeal.this.result.addAll(result2);
                    carListAdapter.notifyDataSetChanged();
                    // 没有信息图片显示
                    if (CollectCardeal.this.result.size() <= 0) {
                        noCollectServerImage.setVisibility(View.VISIBLE);
                    }else {
                        if(refreshlayout!=null){
                            if (pageIndex > pageCount) {
                                refreshlayout.finishLoadmore(2000);
                            } else {
                                carListAdapter.notifyDataSetChanged();
                                refreshlayout.finishRefresh(2000);
                                refreshlayout.finishLoadmore(2000);
                            }
                        }else {
                            carListAdapter.notifyDataSetChanged();
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
