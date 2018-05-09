package com.xtzhangbinbin.jpq.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.xtzhangbinbin.jpq.entity.CompWalletBindInfo;
import com.xtzhangbinbin.jpq.entity.CompWalletDetail;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.ActivityUtil;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.NetUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.StringUtil;
import com.xtzhangbinbin.jpq.view.FullyLinearLayoutManager;
import com.xtzhangbinbin.jpq.view.MyProgressDialog;
import com.xtzhangbinbin.jpq.view.OrdinaryDialog;

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
public class CompWalletActivity extends BaseActivity {

    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.comp_wallet_balance)
    TextView comp_wallet_balance;
    @BindView(R.id.comp_wallet_query)
    LinearLayout comp_wallet_query;
    @BindView(R.id.comp_wallet_bind_account)
    LinearLayout comp_wallet_bind_account;
    @BindView(R.id.comp_wallet_bind_presentation)
    LinearLayout comp_wallet_bind_presentation;
    @BindView(R.id.comp_wallet_presentation)
    LinearLayout comp_wallet_presentation;
    private CommonRecyclerAdapter cashListAdapter;
    private List<CompWalletDetail.DataBean.ResultBean> result;
    private int pageIndex = 1;//第几页
    private int pageCount;//总页数
    private List<String> accountList;//帐户数量

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_wallet);
        ButterKnife.bind(this);
        ActivityUtil.addActivity(this);
        init();
        initAdapter();
        initData();
        initDataList(1, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAccount();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(null != result){
            result.clear();
        }
        initData();
        initDataList(1, null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("我的钱包");
    }

    public void init(){
        dialog = MyProgressDialog.createDialog(this);
        showDialog("正在加载钱包信息，请稍候！");
        result = new ArrayList<>();
        comp_wallet_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().jumpRight(CompWalletActivity.this, CompWalletQueryActivity.class);
            }
        });
        comp_wallet_bind_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().jumpRight(CompWalletActivity.this, CompWalletBindAccountActivity.class);
            }
        });
        comp_wallet_presentation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().jumpRight(CompWalletActivity.this, CompWalletPresentationListActivity.class);
            }
        });
        comp_wallet_bind_presentation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != accountList && !accountList.isEmpty()){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("accountList", (ArrayList)accountList);
                    JumpUtil.newInstance().jumpRight(CompWalletActivity.this, CompWalletPresentationActivity.class, bundle);
                } else {
                    final OrdinaryDialog dialog = OrdinaryDialog.newInstance(CompWalletActivity.this).setMessage1("信息提示").setMessage2("您还没有绑定任何帐户，暂时无法申请提款！您现在去绑定帐户吗？").showDialog();
                    dialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick() {
                            JumpUtil.newInstance().jumpRight(CompWalletActivity.this, CompWalletBindAccountActivity.class);
                            dialog.dismiss();
                        }
                    });
                    dialog.setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
                        @Override
                        public void onNoClick() {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageIndex = 1;
                result.clear();
                initDataList(pageIndex, refreshlayout);
            }
        });
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                initDataList(++pageIndex, refreshlayout);
            }
        });
    }

    /**
     * 获取余额数据
     */
    public void initData(){
        Map<String, String> map = new HashMap<>();
        //获取余额数据
        if (NetUtil.isNetAvailable(this)) {
            OKhttptils.post(this, Config.COMP_WALLET_BALANCE, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if(null != obj){
                            comp_wallet_balance.setText(new DecimalFormat("#0.00").format(obj.getJSONObject("data").getDouble("result")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void fail(String response) {
                    Toast.makeText(CompWalletActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 获取主页的明细数据
     * @param pageIndex
     * @param refreshlayout
     */
    public void initDataList(final int pageIndex, final RefreshLayout refreshlayout) {
        Map<String, String> map = new HashMap<>();
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("pageSize", "");
        //获取明细数据
        if (NetUtil.isNetAvailable(this)) {
            OKhttptils.post(this, Config.COMP_WALLET_OPTION_DETAIL, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    closeDialog();
                    Gson gson = GsonFactory.create();
                    CompWalletDetail wallet = gson.fromJson(response, CompWalletDetail.class);
                    List<CompWalletDetail.DataBean.ResultBean> result2 = wallet.getData().getResult();
                    pageCount = wallet.getData().getPageCount();
                    result.addAll(result2);
                    //没有信息图片显示
                    if (refreshlayout != null) {
                        if (pageIndex > pageCount) {
                            refreshlayout.finishLoadmore(2000);
                        } else {
                            cashListAdapter.notifyDataSetChanged();
                            refreshlayout.finishRefresh(2000);
                            refreshlayout.finishLoadmore(2000);
                        }
                    } else {
                        cashListAdapter.notifyDataSetChanged();
                    }


                }

                @Override
                public void fail(String response) {
                    closeDialog();
                    Toast.makeText(CompWalletActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 初始化adapter
     */
    public void initAdapter(){
        cashListAdapter = new CommonRecyclerAdapter(this, result, R.layout.item_comp_wallet_cash_child) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                final CompWalletDetail.DataBean.ResultBean wallet = result.get(position);
                //图标，收，订单收入   提，提现
                ImageView item_comp_wallet_child_icon = holder.getView(R.id.item_comp_wallet_child_icon);
                //金额
                TextView item_comp_wallet_child_price = holder.getView(R.id.item_comp_wallet_child_price);
                if("sr".equals(result.get(position).getWallet_log_type())){
                    item_comp_wallet_child_icon.setImageResource(R.drawable.ic_comp_wallt_shoushou);
                    item_comp_wallet_child_price.setText("+" + new DecimalFormat("#0.00").format(Math.abs(result.get(position).getWallet_log_money())));
                } else {
                    item_comp_wallet_child_icon.setImageResource(R.drawable.ic_comp_wallt_ti);
                    item_comp_wallet_child_price.setText("-" + new DecimalFormat("#0.00").format(Math.abs(result.get(position).getWallet_log_money())));
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
                    if(!StringUtil.isEmpty(result.get(position).getWallet_log_date())){
                        item_comp_wallet_date.setText(new SimpleDateFormat("MM-dd  HH:mm").format((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(result.get(position).getWallet_log_date())));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        recyclerView.setLayoutManager(new FullyLinearLayoutManager(this));
        recyclerView.setAdapter(cashListAdapter);
    }

    /**
     * 获取帐户列表
     */
    public void initAccount(){
        if (NetUtil.isNetAvailable(this)) {
            Map<String ,String> map = new HashMap<>();
            OKhttptils.post(this, Config.COMP_WALLET_QUERY_ACCOUNT, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    Gson gson = GsonFactory.create();
                    CompWalletBindInfo bindInfo = gson.fromJson(response, CompWalletBindInfo.class);
                    accountList = bindInfo.getData().getResult().getData();

                }

                @Override
                public void fail(String response) {
                    Log.w("test", response);
                    Toast.makeText(CompWalletActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
