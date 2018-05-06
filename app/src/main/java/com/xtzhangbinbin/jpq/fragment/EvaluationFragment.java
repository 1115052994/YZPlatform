package com.xtzhangbinbin.jpq.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.EvaluationAdapter;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.QueryEvaluate;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.NetUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
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
 * Created by Administrator on 2018/4/22.
 */


@SuppressLint("ValidFragment")
public class EvaluationFragment extends Fragment {
    @BindView(R.id.evaluation_list_image)
    ImageView evaluationListImage;
    @BindView(R.id.evaluation_list_listview)
    RecyclerView evaluationListListview;
    Unbinder unbinder;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    private EvaluationAdapter evaluationAdapter;
    private Context context;
    private String index;
    private HashMap<String, String> map = new HashMap<>();
    private List<QueryEvaluate.DataBean.ResultBean> result = new ArrayList<>();
    private int pageCount;//总页数
    private int pageIndex = 1;//第几页

    public EvaluationFragment(Context context, String index) {
        this.context = context;
        this.index = index;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_evaluation_list, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        result.clear();
        //设置线性管理器
        evaluationListListview.setLayoutManager(new LinearLayoutManager(getContext()));
        evaluationAdapter = new EvaluationAdapter(context, result);
        evaluationListListview.setAdapter(evaluationAdapter);
        Log.d("aaaaa", "onCreateView: ");
        switch (index) {
            case "1":
                loading(Config.ALLCOMPEVALUATE);
                break;
            case "2":
                loading(Config.PRAISECOMPEVALUATE);
                break;
            case "3":
                loading(Config.MEDIUMCOMPEVALUATE);
                break;
            case "4":
                loading(Config.POORREVIEWCOMPEVALUATE);
                break;
        }

        return inflate;
    }
    public void loading(final String url){
        PostCar(url,1,null);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageIndex = 1;
                result.clear();
                PostCar(url,1,refreshlayout);
            }
        });
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if(pageIndex < pageCount){
                    PostCar(url,++pageIndex,refreshlayout);
                }else {
                    refreshlayout.finishLoadmore();
                }

            }
        });
    }

    public void PostCar(String url, final int pageIndex, final RefreshLayout refreshlayout) {
        map.clear();
        map.put("pageIndex",String.valueOf(pageIndex));
        if (NetUtil.isNetAvailable(context)) {
            OKhttptils.post((Activity) context, url, map, new OKhttptils.HttpCallBack() {
                @Override
                public String success(String response) {
                    Log.i("aaaaa", "查询评价: " + response);
                    Gson gson = GsonFactory.create();
                    QueryEvaluate queryEvaluate = gson.fromJson(response, QueryEvaluate.class);
                    pageCount=queryEvaluate.getData().getPageCount();
                    result.addAll(queryEvaluate.getData().getResult());
                    if(refreshlayout==null){
                        evaluationAdapter.notifyDataSetChanged();
                    }

                    //没有信息图片显示
                    if (result.size() <= 0) {
                        evaluationListImage.setVisibility(View.VISIBLE);
                    }else {
                        if(refreshlayout!=null){
                            if (pageIndex > pageCount) {
                                Log.d("aaaaa", "过界了小心点: "+pageIndex+"pageCount"+pageCount);
                                refreshlayout.finishLoadmore(1000);
                            } else {
                                Log.d("aaaaa", "没过界: "+pageIndex);
                                evaluationAdapter.notifyDataSetChanged();
                                refreshlayout.finishRefresh(1000);
                                refreshlayout.finishLoadmore(1000);
                            }
                        }else {
                            Log.d("aaaaa", "刷新: ");
                            evaluationAdapter.notifyDataSetChanged();
                        }
                    }
                    return response;
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

