package com.xtzhangbinbin.jpq.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.CommonRecyclerAdapter;
import com.xtzhangbinbin.jpq.adapter.ViewHolder;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.BespeakComp;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.ActivityUtil;
import com.xtzhangbinbin.jpq.utils.NetUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.view.MyProgressDialog;
import android.widget.Button;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018/4/25 0025.
 */

@SuppressWarnings("all")
public class BespeakCompActivity extends BaseActivity {

    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.bespeak_comp_default_image)
    ImageView defaultImage;
    private CommonRecyclerAdapter ordersCompAdapter;
    private List<BespeakComp.DataBean.ResultBean> result = new ArrayList<>();
    private int pageIndex = 1;//第几页
    private int pageCount;//总页数

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bespeak_comp);
        ButterKnife.bind(this);
        ActivityUtil.addActivity(this);
        init();
        getOrders(1, null);
    }

    public void init(){
        result = new ArrayList<>();
        defaultImage.setVisibility(View.GONE);
        pageIndex = 1;
        result.clear();
        getOrders( 1, smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageIndex = 1;
                result.clear();
                getOrders( 1, refreshlayout);
            }
        });
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getOrders(++pageIndex, refreshlayout);
            }
        });
        dialog = MyProgressDialog.createDialog(this);
        dialog.setMessage("正在加载数据，请稍候");
        dialog.show();
        initAdapter();
    }

    public void getOrders(final int pageIndex, final RefreshLayout refreshlayout) {
        Map<String, String> map = new HashMap<>();
        map.put("pageIndex", String.valueOf(pageIndex));
        if (NetUtil.isNetAvailable(this)) {
            OKhttptils.post((Activity) this, Config.COMP_BESPEAK_LIST, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    Log.w("test", response);
                    Gson gson = GsonFactory.create();
                    BespeakComp bespeak = gson.fromJson(response, BespeakComp.class);
                    List<BespeakComp.DataBean.ResultBean> result2 = bespeak.getData().getResult();
                    pageCount = bespeak.getData().getPageCount();
                    result.addAll(result2);
                    //没有信息图片显示
                    if (result.size() <= 0) {
                        defaultImage.setVisibility(View.VISIBLE);
                    } else {
                        if (refreshlayout != null) {
                            if (pageIndex > pageCount) {
                                refreshlayout.finishLoadmore(2000);
                            } else {
                                ordersCompAdapter.notifyDataSetChanged();
                                refreshlayout.finishRefresh(2000);
                                refreshlayout.finishLoadmore(2000);
                            }
                        } else {
                            ordersCompAdapter.notifyDataSetChanged();
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
                    defaultImage.setVisibility(View.VISIBLE);
                    Toast.makeText(BespeakCompActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void initAdapter(){
        ordersCompAdapter = new CommonRecyclerAdapter(this, result, R.layout.item_bespeak_comp_list) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                final BespeakComp.DataBean.ResultBean bespeak = result.get(position);
                ImageView icon = (ImageView) holder.getView(R.id.bespeak_comp_icon);
                OKhttptils.getPic(BespeakCompActivity.this, bespeak.getCar_1_file_id(), icon);
                TextView item_bespeak_car_name = holder.getView(R.id.item_bespeak_car_name);
                item_bespeak_car_name.setText(bespeak.getCar_name());
                TextView bespeak_comp_date = holder.getView(R.id.bespeak_comp_date);
                bespeak_comp_date.setText(bespeak.getOnline_date());
                Button item_bespeak_phone = holder.getView(R.id.item_bespeak_phone);
                item_bespeak_phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        call(bespeak.getOnline_phone());
                    }
                });
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ordersCompAdapter);
    }

    private RxPermissions rxPermission;

    private void call(final String phone) {
        // 申请定位权限
        rxPermission = new RxPermissions(this);
        rxPermission.request(Manifest.permission.CALL_PHONE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) { // 在android 6.0之前会默认返回true
                            // 已经获取权限
                            Intent intent = new Intent(); // 意图对象：动作 + 数据
                            intent.setAction(Intent.ACTION_CALL); // 设置动作
                            Uri data = Uri.parse("tel:" + phone); // 设置数据
                            intent.setData(data);
                            BespeakCompActivity.this.startActivity(intent); // 激活Activity组件
                        } else {
                            // 未获取权限
                            Toast.makeText(BespeakCompActivity.this, "您没有授权该权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if (Build.VERSION.SDK_INT >= 9) {
                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.setData(Uri.fromParts("package", getPackageName(), null));
                            } else if (Build.VERSION.SDK_INT <= 8) {
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                                intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
                            }
                            startActivity(intent);
                        }
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("预约信息");
    }

}
