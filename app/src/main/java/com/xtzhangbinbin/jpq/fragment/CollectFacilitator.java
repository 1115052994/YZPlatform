package com.xtzhangbinbin.jpq.fragment;


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
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.CallCollect;
import com.xtzhangbinbin.jpq.adapter.ServiceListAdapter;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.QueryCollectServer;
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
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class CollectFacilitator extends Fragment {
    @BindView(R.id.service_provider_listview)
    ListView serviceProviderListview;
    Unbinder unbinder;
    @BindView(R.id.no_collect_car_image)
    ImageView noCollectCarImage;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    private Context context;
    private ServiceListAdapter serviceListAdapter;
    private List<QueryCollectServer.DataBean.ResultBean> result = new ArrayList<>();
    private int pageCount;//总页数
    private int pageIndex = 1;//第几页
    private HashMap<String,String> map=new HashMap<>();


    public CollectFacilitator(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_collect_facilitator, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        PostFaci(1,null);
        serviceListAdapter = new ServiceListAdapter(context, result);
        serviceProviderListview.setAdapter(serviceListAdapter);
        serviceListAdapter.getServerCall(new CallCollect() {
            @Override
            public void getCallcollect(View view, int id,int position) {
                result.remove(position);
                serviceListAdapter.notifyDataSetChanged();
            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageIndex=1;
                result.clear();
                PostFaci(1,refreshlayout);
            }
        });
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if(pageIndex < pageCount){
                    PostFaci(++pageIndex,refreshlayout);
                }else {
                    refreshlayout.finishLoadmore();
                }

            }
        });

        return inflate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void PostFaci(final int pageIndex, final RefreshLayout refreshlayout) {
        map.clear();
        if (NetUtil.isNetAvailable(context)) {
            map.put("pageIndex",String.valueOf(pageIndex));
//            map.put("coll_content_id","3");
            OKhttptils.post((Activity) context, Config.CHECKCOMP, map, new OKhttptils.HttpCallBack() {
                @Override
                public String success(String response) {
                    Log.i("aaaaa", "查询服务商收藏: " + response);
                    Gson gson = GsonFactory.create();
                    QueryCollectServer querystar = gson.fromJson(response, QueryCollectServer.class);
                    pageCount = querystar.getData().getPageCount();
                    List<QueryCollectServer.DataBean.ResultBean> result2 = querystar.getData().getResult();
                    result.addAll(result2);
                    //没有信息图片显示
                    if (result.size() <= 0) {
                        noCollectCarImage.setVisibility(View.VISIBLE);
                    }else {
                        if(refreshlayout!=null){
                            if (pageIndex > pageCount) {
                                refreshlayout.finishLoadmore(2000);
                            } else {
                                serviceListAdapter.notifyDataSetChanged();
                                refreshlayout.finishRefresh(2000);
                                refreshlayout.finishLoadmore(2000);
                            }
                        }else {
                            serviceListAdapter.notifyDataSetChanged();
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

}
