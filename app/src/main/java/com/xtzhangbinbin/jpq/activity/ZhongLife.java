package com.xtzhangbinbin.jpq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.squareup.picasso.Picasso;
import com.xtzhangbinbin.jpq.AppraiseInterface;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.BrandGVAdapter;
import com.xtzhangbinbin.jpq.adapter.CommonRecyclerAdapter;
import com.xtzhangbinbin.jpq.adapter.ViewHolder;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.MeishiBean;
import com.xtzhangbinbin.jpq.entity.MeishiListBean;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.view.ExpandableGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ZhongLife extends BaseActivity {

    @BindView(R.id.grideView)
    ExpandableGridView grideView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;

    private List<Map<String, String>> grideViewList = new ArrayList<>();
    private BrandGVAdapter grideAdapter;

    private CommonRecyclerAdapter adapter;
    private List<MeishiListBean.DataBean.ResultBean> list = new ArrayList<>();

    private String type = "YZzshms";//默认美食
    private String cityId = "";

    private String smallType = "";// 蛋糕

    private int index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhong_life);
        ButterKnife.bind(this);
        initView();
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                type = bundle.getString("String");
                cityId = bundle.getString("cityId");
                Log.i("cityId", cityId);
            }
        }
        // 获取Item
        getItem();
    }

    @Override
    protected void onStart() {
        super.onStart();
        switch (type) {
            case "YZzshxxyl":
                setTitle("休闲娱乐");
                break;
            case "YZzshshfu":
                setTitle("生活服务");
                break;
            case "YZzshms":
                setTitle("美食");
                break;
            case "YZzshjd":
                setTitle("酒店");
                break;
        }
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.newInstance().finishRightTrans(ZhongLife.this);
            }
        });
    }

    private void initView() {
        //tv_carbrand image_carbrand
        grideAdapter = new BrandGVAdapter(this, grideViewList);
        grideAdapter.setOnItemClickListener(new AppraiseInterface() {
            @Override
            public void onClick(View view, int position) {
                Map<String, String> map = grideViewList.get(position);
                smallType = map.get("value");
                Log.i("smallTpye", "smallType=" + smallType);
                // 重新刷新列表
                getPageInfo();
            }
        });
        grideView.setAdapter(grideAdapter);

        adapter = new CommonRecyclerAdapter(this, list, R.layout.item_zhongshenghuo) {

            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                MeishiListBean.DataBean.ResultBean bean = list.get(position);
                ImageView imageView = holder.getView(R.id.imageView);
//                OKhttptils.getPic(getActivity(),bean.getZsh_img(),imageView);
//                Picasso.with(ZhongLife.this).load(bean.getZsh_img()).into(imageView);
                Picasso.get().load(bean.getZsh_img()).into(imageView);
                TextView tvName = holder.getView(R.id.tv_name);
                tvName.setText(bean.getZsh_name());
                TextView tvPrice = holder.getView(R.id.tv_price);
                tvPrice.setText("人均￥" + bean.getZsh_avg_price() + "元");
                TextView tvLoc = holder.getView(R.id.tv_loc);
                tvLoc.setText(bean.getZsh_addr());
                int level = (int) Double.parseDouble(bean.getZsh_level());
                ImageView star1 = holder.getView(R.id.star1);
                ImageView star2 = holder.getView(R.id.star2);
                ImageView star3 = holder.getView(R.id.star3);
                ImageView star4 = holder.getView(R.id.star4);
                ImageView star5 = holder.getView(R.id.star5);
                switch (level) {
                    case 5:
                        star5.setImageResource(R.drawable.pj_pinkstar);
                    case 4:
                        star4.setImageResource(R.drawable.pj_pinkstar);
                    case 3:
                        star3.setImageResource(R.drawable.pj_pinkstar);
                    case 2:
                        star2.setImageResource(R.drawable.pj_pinkstar);
                    case 1:
                        star1.setImageResource(R.drawable.pj_pinkstar);
                        break;
                }
            }
        };
        adapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                JumpUtil.newInstance().jumpLeft(ZhongLife.this, ZhongLifeDetail.class, list.get(position).getZsh_id());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getPageInfoMore(refreshlayout);
            }
        });
    }

    private void getItem() {
        Map<String, String> map = new HashMap<>();
        map.put("dict_id", type);
        OKhttptils.post(this, Config.GETUSUALLY, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Gson gson = new Gson();
                MeishiBean meishiBean = gson.fromJson(response, MeishiBean.class);
                List<MeishiBean.DataBean.ResultBean> list = meishiBean.getData().getResult();
                grideViewList.clear();
                for (MeishiBean.DataBean.ResultBean bean : list) {
                    Map<String, String> map = new HashMap<>();
                    //tv_carbrand image_carbrand
                    map.put("tv_carbrand", bean.getDict_desc());
                    map.put("image_carbrand", bean.getDict_10());
                    map.put("value", bean.getDict_value());
                    grideViewList.add(map);
                    if (grideViewList.size() == 1) {
                        smallType = bean.getDict_value();
                        getPageInfo();
                    }
                }
                grideAdapter.notifyDataSetChanged();
            }

            @Override
            public void fail(String response) {

            }
        });
    }

    private void getPageInfo() {
        index = 1;
        Map<String, String> map = new HashMap<>();
        map.put("dict_value", smallType);
        map.put("zsh_city", cityId);
        map.put("pageIndex", index + "");
        map.put("pageSize", "20");
        OKhttptils.post(ZhongLife.this, Config.GETPAGEINFO, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i("getData===", response);
                Gson gson = new Gson();
                MeishiListBean meishiListBean = gson.fromJson(response, MeishiListBean.class);
                list.clear();
                List<MeishiListBean.DataBean.ResultBean> resultBeanList = meishiListBean.getData().getResult();
                for (MeishiListBean.DataBean.ResultBean bean : resultBeanList) {
                    list.add(bean);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void fail(String response) {

            }
        });
    }

    private void getPageInfoMore(final RefreshLayout refreshlayout) {
        Map<String, String> map = new HashMap<>();
        map.put("dict_value", smallType);
        map.put("zsh_city", cityId);
        map.put("pageIndex", ++index + "");
        map.put("pageSize", "20");
        OKhttptils.post(ZhongLife.this, Config.GETPAGEINFO, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i("getData===", response);
                Gson gson = new Gson();
                MeishiListBean meishiListBean = gson.fromJson(response, MeishiListBean.class);
                List<MeishiListBean.DataBean.ResultBean> resultBeanList = meishiListBean.getData().getResult();
                for (MeishiListBean.DataBean.ResultBean bean : resultBeanList) {
                    list.add(bean);
                }
                adapter.notifyDataSetChanged();
                refreshlayout.finishLoadmore();
            }

            @Override
            public void fail(String response) {

            }
        });
    }
}
