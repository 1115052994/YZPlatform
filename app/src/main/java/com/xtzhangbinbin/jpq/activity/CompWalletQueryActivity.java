package com.xtzhangbinbin.jpq.activity;

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
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.CommonRecyclerAdapter;
import com.xtzhangbinbin.jpq.adapter.ViewHolder;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.CompWalletQuery;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.ActivityUtil;
import com.xtzhangbinbin.jpq.utils.DateUtil;
import com.xtzhangbinbin.jpq.utils.NetUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.StringUtil;
import com.xtzhangbinbin.jpq.view.MyProgressDialog;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;

/**
 * Created by Administrator on 2018/4/26 0026.
 */

@SuppressWarnings("all")
public class CompWalletQueryActivity extends BaseActivity {

    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private CommonRecyclerAdapter cashQueryAdapter;
    private List<CompWalletQuery.DataBeanX.ResultBean.DataBean> result;
    @BindView(R.id.comp_wallet_query_date)
    ImageView comp_wallet_query_date;
    @BindView(R.id.comp_wallet_query_month)
    TextView comp_wallet_query_month;
    @BindView(R.id.comp_wallet_query_sr)
    TextView comp_wallet_query_sr;
    @BindView(R.id.comp_wallet_query_zc)
    TextView comp_wallet_query_zc;
    @BindView(R.id.no_collect_server_image)
    ImageView no_collect_server_image;

    private int pageIndex = 1;//第几页
    private int pageCount;//总页数
    private String year_month;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_wallet_query);
        ButterKnife.bind(this);
        ActivityUtil.addActivity(this);
        dialog = MyProgressDialog.createDialog(this);
        result = new ArrayList<>();
        init();
        initAdapter();
        initData(1, null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("综合查询");
    }

    public void init(){
        comp_wallet_query_month.setText(new SimpleDateFormat("yyyy-MM").format(new Date()));
        comp_wallet_query_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePicker picker = new DatePicker(CompWalletQueryActivity.this, DatePicker.YEAR_MONTH);
                picker.setCanceledOnTouchOutside(true);
                picker.setUseWeight(true);
                picker.setTopPadding(ConvertUtils.toPx(CompWalletQueryActivity.this, 10));
                picker.setRangeStart(2000, 1);
                picker.setRangeEnd(DateUtil.getYEAR(), DateUtil.getMONTH());
                picker.setSelectedItem(DateUtil.getYEAR(), DateUtil.getMONTH());
                picker.setResetWhileWheel(false);
                picker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
                    @Override
                    public void onDatePicked(String year, String month) {
                        year_month = year + "-" + month;
                        result.clear();
                        initData(1, null);
                    }
                });
                picker.show();
            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageIndex = 1;
                result.clear();
                initData(pageIndex, refreshlayout);
            }
        });
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                initData(++pageIndex, refreshlayout);
            }
        });

    }

    public void initData(final int pageIndex, final RefreshLayout refreshlayout) {
        showDialog("正在加载数据");
        Map<String, String> map = new HashMap<>();
        if(null != year_month){
            map.put("date", year_month);
        }
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("pageSize", "");
        if (NetUtil.isNetAvailable(this)) {
            OKhttptils.post(this, Config.COMP_WALLET_INTEGRATED_QUERY, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    Gson gson = GsonFactory.create();
                    CompWalletQuery wallet = gson.fromJson(response, CompWalletQuery.class);
                    List<CompWalletQuery.DataBeanX.ResultBean.DataBean> result2 = wallet.getData().getResult().getData();
                    comp_wallet_query_month.setText(wallet.getData().getResult().getDate());
                    comp_wallet_query_sr.setText("提现:" + new DecimalFormat("#0.00").format(wallet.getData().getResult().getSrmoney()));
                    comp_wallet_query_zc.setText("收入:" + new DecimalFormat("#0.00").format(wallet.getData().getResult().getZcmoney()));
                    pageCount = wallet.getData().getResult().getPageCount();
                    result.addAll(result2);
                    if(result.size() > 0){
                        //没有信息图片隐藏
                        no_collect_server_image.setVisibility(View.GONE);
                        if (refreshlayout != null) {
                            if (pageIndex > pageCount) {
                                refreshlayout.finishLoadmore(2000);
                            } else {
                                cashQueryAdapter.notifyDataSetChanged();
                                refreshlayout.finishRefresh(2000);
                                refreshlayout.finishLoadmore(2000);
                            }
                        } else {
                            cashQueryAdapter.notifyDataSetChanged();
                        }
                    } else {
                        cashQueryAdapter.notifyDataSetChanged();
                        no_collect_server_image.setVisibility(View.VISIBLE);
                    }
                    closeDialog();
                }

                @Override
                public void fail(String response) {
                    closeDialog();
                    Log.w("test", response);
                    Toast.makeText(CompWalletQueryActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 初始化adapter
     */
    public void initAdapter(){
        cashQueryAdapter = new CommonRecyclerAdapter(this, result, R.layout.item_comp_wallet_cash_child) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                final CompWalletQuery.DataBeanX.ResultBean.DataBean wallet = result.get(position);
                //图标，收，订单收入   提，提现
                ImageView item_comp_wallet_child_icon = holder.getView(R.id.item_comp_wallet_child_icon);
                //金额
                TextView item_comp_wallet_child_price = holder.getView(R.id.item_comp_wallet_child_price);
                if("sr".equals(result.get(position).getWallet_log_type())){
                    item_comp_wallet_child_icon.setImageResource(R.drawable.ic_comp_wallt_shoushou);
                    item_comp_wallet_child_price.setText("+" + result.get(position).getWallet_log_money());
                } else {
                    item_comp_wallet_child_icon.setImageResource(R.drawable.ic_comp_wallt_ti);
                    item_comp_wallet_child_price.setText("" + result.get(position).getWallet_log_money());
                }
                //提现方式，如果是收入，则不显示
                TextView item_comp_wallet_child_type = holder.getView(R.id.item_comp_wallet_child_type);
                //帐号，如果是收入，显示消费名称。
                TextView item_comp_wallet_child_account = holder.getView(R.id.item_comp_wallet_child_account);
                String name = "";
                String type = "";
                if(!StringUtil.isEmpty(result.get(position).getCard_type())){
                    switch(result.get(position).getCard_type()){
                        case "alipay":
                            name = result.get(position).getAlipay_id();
                            type = "(支付宝)";
                            break;
                        case "wechat":
                            name = result.get(position).getWechat_id();
                            type = "(微信)";
                            break;
                        case "bankcard":
                            name = result.get(position).getCard_bc_no();
                            type = "(银行卡)";
                            break;
                        default:
                            name = result.get(position).getCard_type();
                    }
                }
                item_comp_wallet_child_account.setText(name);
                item_comp_wallet_child_type.setText(type);
                //操作时间
                TextView item_comp_wallet_date = holder.getView(R.id.item_comp_wallet_date);
                try {
                    item_comp_wallet_date.setText(new SimpleDateFormat("MM-dd  HH:mm").format((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(result.get(position).getWallet_log_date())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cashQueryAdapter);
    }
}
