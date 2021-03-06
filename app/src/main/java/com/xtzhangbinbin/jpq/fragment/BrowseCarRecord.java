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

import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.BrowsingAdapter;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.QueryCarRecord;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.NetUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
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
    private MyProgressDialog dialog;
    public BrowseCarRecord(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_browse_record, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        dialog = MyProgressDialog.createDialog(getContext());
        dialog.setMessage("正在加载数据，请稍候！");
        dialog.show();
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
                        if(pageIndex < pageCount){
                            PostRecord(Config.BROWSECARCOOKIES,++pageIndex,refreshlayout);
                        }else {
                            refreshlayout.finishLoadmore();
                        }

                    }
                });

        browsingAdapter.getBrowsingCall(new BrowsingAdapter.CallBrowsing() {
            @Override
            public void getCallBrowsing(View view, String is, final int position) {
                Map<String, String> map = new HashMap<>();
                map.put("log_id",result.get(position).getLog_id());
                OKhttptils.post((Activity) getContext(), Config.BROWSEDLOG, map, new OKhttptils.HttpCallBack() {
                    @Override
                    public void success(String response) {
                        Log.d("test", "onResponse获取数据: " + response);
                        result.remove(position);
                        browsingAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void fail(String response) {
                        Log.d("aaaa", "fail: " + response);
                    }
                });
            }
        });
        return inflate;
    }

    public void PostRecord(String url, final int pageIndex, final RefreshLayout refreshlayout) {
        map.clear();
        map.put("pageIndex",String.valueOf(pageIndex));
        map.put("pageSize", String.valueOf("20"));
        if (NetUtil.isNetAvailable(context)) {
            OKhttptils.post((Activity) context, url, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {

                    Gson gson = GsonFactory.create();
                    QueryCarRecord queryRecord = gson.fromJson(response, QueryCarRecord.class);
                    pageCount=queryRecord.getData().getPageCount();
                    result.addAll(queryRecord.getData().getResult());
                    if(refreshlayout==null){
                        browsingAdapter.notifyDataSetChanged();
                    }
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
                    if(null != dialog && dialog.isShowing()){
                        dialog.dismiss();
                    }
                }

                @Override
                public void fail(String response) {
                    if(null != dialog && dialog.isShowing()){
                        dialog.dismiss();
                    }
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
