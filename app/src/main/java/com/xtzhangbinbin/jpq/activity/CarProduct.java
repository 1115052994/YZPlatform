package com.xtzhangbinbin.jpq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.CommonRecyclerAdapter;
import com.xtzhangbinbin.jpq.adapter.ViewHolder;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.CheyongpinBean;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.squareup.picasso.Picasso;
import com.xtzhangbinbin.jpq.view.MyProgressDialog;
import com.youth.banner.Banner;

import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CarProduct extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;

    private CommonRecyclerAdapter adapter;
    private List<CheyongpinBean.DataBean.ResultBean> list = new ArrayList<>();
    private String type = "YZcarYPcyplxetzy";
    private int index = 1;

    private MyProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_product);
        ButterKnife.bind(this);
        type = "YZcarYPcyplxetzy";
//        Intent intent = getIntent();
//        if (intent!=null){
//            Bundle bundle = intent.getExtras();
//            if (bundle!=null) {
//                if (!"".equals(bundle.getString("String","")))
//                    type = bundle.getString("String","");
//            }
//        }
        initView();
//        scrollView.fullScroll(ScrollView.FOCUS_UP);//滚动到顶部
//        scrollView.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部
        getData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("车用品");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.newInstance().finishRightTrans(CarProduct.this);
            }
        });
    }

    private void initView() {
        adapter = new CommonRecyclerAdapter(this, list, R.layout.item_cheyongpin) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                CheyongpinBean.DataBean.ResultBean bean = list.get(position);
                ImageView imageView = holder.getView(R.id.image);
//                Picasso.with(getApplicationContext()).load(bean.getJd_sp_imageurl()).into(imageView);
                Picasso.get().load(bean.getJd_sp_imageurl()).into(imageView);
                TextView name = holder.getView(R.id.tv_name);
                name.setText(bean.getJd_sp_warename());
                TextView grade = holder.getView(R.id.tv_grade);
                grade.setText("评分 " + bean.getJd_sp_good() + "%好评");
                TextView price = holder.getView(R.id.tv_price);
                price.setText("￥" + bean.getJd_sp_price());
                TextView marketPrice = holder.getView(R.id.tv_marketPrice);
                marketPrice.setText("市场价￥" + bean.getJd_sp_market_price());
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if (!"".equals(list.get(position).getJd_sp_wareurl())) {
                    dialog = MyProgressDialog.createDialog(CarProduct.this);
                    dialog.setMessage("正在加载中,请稍候");
                    dialog.show();
                    JumpUtil.newInstance().jumpLeft(CarProduct.this, CarProductJD.class, list.get(position).getJd_sp_wareurl());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    },1000);
                }
            }
        });

        // 处理滑动冲突
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setNestedScrollingEnabled(false);
        // 上啦加载
//        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
//            @Override
//            public void onLoadmore(RefreshLayout refreshlayout) {
//                getDataMore(refreshlayout);
//            }
//        });
        smartRefreshLayout.setEnableLoadmore(false);
        smartRefreshLayout.setEnableRefresh(false);
    }

    private void getData() {
        index = 1;
        Map<String, String> map = new HashMap<>();
        map.put("goodSort", "goodDesc");//goodDesc降序 goodAsc升序
        map.put("type", type);
        map.put("pageIndex", index+"");
        map.put("pageSize", "");
        map.put("priceSort", "priceDesc");
        OKhttptils.post(this, Config.SELECTJDSP, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Gson gson = new Gson();
                CheyongpinBean bean = gson.fromJson(response, CheyongpinBean.class);
                List<CheyongpinBean.DataBean.ResultBean> resultBeanList = bean.getData().getResult();
                list.clear();
                for (CheyongpinBean.DataBean.ResultBean resultBean : resultBeanList) {
                    list.add(resultBean);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void fail(String response) {

            }
        });
    }

    private void getDataMore(final RefreshLayout refreshlayout) {
        Map<String, String> map = new HashMap<>();
        map.put("goodSort", "");
        map.put("type", type);
        map.put("pageIndex", ++index+"");
        map.put("pageSize", "");
        map.put("priceSort", "");
        OKhttptils.post(this, Config.SELECTJDSP, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Gson gson = new Gson();
                CheyongpinBean bean = gson.fromJson(response, CheyongpinBean.class);
                List<CheyongpinBean.DataBean.ResultBean> resultBeanList = bean.getData().getResult();
                for (CheyongpinBean.DataBean.ResultBean resultBean : resultBeanList) {
                    list.add(resultBean);
                }
                adapter.notifyDataSetChanged();
                refreshlayout.finishLoadmore();
            }

            @Override
            public void fail(String response) {

            }
        });
    }

    @OnClick({R.id.ly_etzy, R.id.ly_xcjly, R.id.ly_qczd, R.id.ly_dcld, R.id.ly_dcld2, R.id.ly_xcjly2, R.id.ly_qczd2, R.id.ly_etzy2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ly_etzy:
            case R.id.ly_etzy2:
                type = "YZcarYPcyplxetzy";
                break;
            case R.id.ly_xcjly:
            case R.id.ly_xcjly2:
                type = "YZcarYPcyplxxcjly";
                break;
            case R.id.ly_qczd:
            case R.id.ly_qczd2:
                type = "YZcarYPcyplxqczd";
                break;
            case R.id.ly_dcld:
            case R.id.ly_dcld2:
                type = "YZcarYPcyplxdcld";
                break;
        }
        // 获取数据
        dialog = MyProgressDialog.createDialog(CarProduct.this);
        dialog.setMessage("正在加载中,请稍候");
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        },1000);
        JumpUtil.newInstance().jumpLeft(this,CarProductSX.class,type);
    }
}
