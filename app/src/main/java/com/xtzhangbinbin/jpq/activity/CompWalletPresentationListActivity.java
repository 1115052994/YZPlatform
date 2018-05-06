package com.xtzhangbinbin.jpq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.xtzhangbinbin.jpq.entity.CompWalletDepositLog;
import com.xtzhangbinbin.jpq.entity.CompWalletQuery;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.ActivityUtil;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.NetUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.utils.StringUtil;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.MyProgressDialog;
import com.xtzhangbinbin.jpq.view.SingletonButtonDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/26 0026.
 */

@SuppressWarnings("all")
public class CompWalletPresentationListActivity extends BaseActivity {

    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.comp_wallet_default_image)
    ImageView comp_wallet_default_image;
    private CommonRecyclerAdapter compWallPreAdapter;
    private List<CompWalletDepositLog.DataBean.ResultBean> result;
    private int pageIndex = 1;//第几页
    private int pageCount;//总页数

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_wallet_presentation_list);
        ButterKnife.bind(this);
        ActivityUtil.addActivity(this);

        init();
        initAdapter();
        initData(1, null);
    }

    public void init(){
        result = new ArrayList<>();
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
        Map<String, String> map = new HashMap<>();
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("pageSize", "");
        if (NetUtil.isNetAvailable(this)) {
            OKhttptils.post(this, Config.COMP_WALLET_DEPOST_LOG, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    Gson gson = new Gson();
                    CompWalletDepositLog log = gson.fromJson(response, CompWalletDepositLog.class);
                    List<CompWalletDepositLog.DataBean.ResultBean> result2 = log.getData().getResult();
                    pageCount = log.getData().getPageCount();
                    result.addAll(result2);
                    if(result.size() > 0){
                        //没有信息图片隐藏
                        comp_wallet_default_image.setVisibility(View.GONE);
                        if (refreshlayout != null) {
                            if (pageIndex > pageCount) {
                                refreshlayout.finishLoadmore(2000);
                            } else {
                                compWallPreAdapter.notifyDataSetChanged();
                                refreshlayout.finishRefresh(2000);
                                refreshlayout.finishLoadmore(2000);
                            }
                        } else {
                            compWallPreAdapter.notifyDataSetChanged();
                        }
                    } else {
                        comp_wallet_default_image.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void fail(String response) {
                    Log.w("test", response);
                    Toast.makeText(CompWalletPresentationListActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 初始化adapter
     */
    public void initAdapter() {
        compWallPreAdapter = new CommonRecyclerAdapter(this, result, R.layout.item_comp_wallet_presentation_list) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                final CompWalletDepositLog.DataBean.ResultBean log = result.get(position);
                TextView item_comp_wallet_deposit_account = holder.getView(R.id.item_comp_wallet_presentation_account);
                if(!StringUtil.isEmpty(log.getCard_type())){
                    switch(log.getCard_type()){
                        case "alipay":
                            item_comp_wallet_deposit_account.setText(log.getCard_alipay_id() + " (支付宝)");
                            break;
                        case "wechat":
                            item_comp_wallet_deposit_account.setText(log.getCard_wechat_id() + " (微信)");
                            break;
                        case "bankcard":
                            item_comp_wallet_deposit_account.setText(log.getCard_bc_no() + " (银行卡)");
                            break;
                    }
                }
                TextView item_comp_wallet_deposit_money = holder.getView(R.id.item_comp_wallet_presentation_money);
                item_comp_wallet_deposit_money.setText("￥" + new DecimalFormat("#0.00").format(log.getWallet_apply_money()));
                TextView item_comp_wallet_deposit_date = holder.getView(R.id.item_comp_wallet_presentation_date);
                item_comp_wallet_deposit_date.setText(log.getWallet_apply_date());
                TextView item_comp_wallet_deposit_state = holder.getView(R.id.item_comp_wallet_presentation_state);
                item_comp_wallet_deposit_state.setText(log.getWallet_apply_audit_state_cn());
                TextView item_comp_wallet_deposit_remark = holder.getView(R.id.item_comp_wallet_presentation_remark);
                if(StringUtil.isEmpty(log.getWallet_apply_audit_msg())){
                    item_comp_wallet_deposit_remark.setVisibility(View.GONE);
                } else {
                    item_comp_wallet_deposit_remark.setText(log.getWallet_apply_audit_msg());
                }
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(compWallPreAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("提现记录");
    }
}
