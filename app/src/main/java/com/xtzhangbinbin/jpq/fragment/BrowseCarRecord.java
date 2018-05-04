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
import com.xtzhangbinbin.jpq.adapter.BrowsingAdapter;
import com.xtzhangbinbin.jpq.adapter.CallBrowsing;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.QueryCarRecord;
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
public class BrowseCarRecord extends Fragment {
    @BindView(R.id.browsing_Cardeal_image)
    ImageView browsing_Cardeal_image;
    @BindView(R.id.browsing_listview)
    ListView browsingListview;
    Unbinder unbinder;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    private HashMap<String,String> map=new HashMap<>();
    private Context context;
    private List<QueryCarRecord.DataBean.ResultBean> result = new ArrayList<>();
    private BrowsingAdapter browsingAdapter;
    private int pageIndex = 1;//第几页
    private int pageCount;//总页数

    public BrowseCarRecord(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_browse_record, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        //  浏览记录地址
        PostRecord(Config.BROWSECARCOOKIES,1,null);
        browsingAdapter = new BrowsingAdapter(context, result);
        browsingListview.setAdapter(browsingAdapter);
                smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(RefreshLayout refreshlayout) {
                        pageIndex = 1;
                        result.clear();
                        PostRecord(Config.BROWSECARCOOKIES,1,refreshlayout);
                    }
                });
                smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
                    @Override
                    public void onLoadmore(RefreshLayout refreshlayout) {
                        PostRecord(Config.BROWSECARCOOKIES,++pageIndex,refreshlayout);
                    }
                });

        browsingAdapter.getBrowsingCall(new CallBrowsing() {
            @Override
            public void getCallBrowsing(View view, String is) {
                   //浏览记录地址
                        result.clear();
                        PostRecord(Config.BROWSECARCOOKIES,1,null);
            }
        });
        return inflate;
    }

    public void PostRecord(String url, final int pageIndex, final RefreshLayout refreshlayout) {
        map.clear();
        map.put("pageIndex",String.valueOf(pageIndex));
        if (NetUtil.isNetAvailable(context)) {
            OKhttptils.post((Activity) context, url, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    Log.i("aaaaa", "查询浏览记录: " + response);
                    Gson gson = GsonFactory.create();
                    QueryCarRecord queryRecord = gson.fromJson(response, QueryCarRecord.class);
                    pageCount=queryRecord.getData().getPageCount();
                    List<QueryCarRecord.DataBean.ResultBean> result2 = queryRecord.getData().getResult();
                    result.addAll(result2);
                    //没有信息图片显示
                    if (BrowseCarRecord.this.result.size() <= 0) {
                        browsing_Cardeal_image.setVisibility(View.VISIBLE);
                    }else {
                        if(refreshlayout!=null){
                            if (pageIndex > pageCount) {
                                refreshlayout.finishLoadmore(2000);
                            } else {
                                browsingAdapter.notifyDataSetChanged();
                                refreshlayout.finishRefresh(2000);
                                refreshlayout.finishLoadmore(2000);
                            }
                        }else {
                            browsingAdapter.notifyDataSetChanged();
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
