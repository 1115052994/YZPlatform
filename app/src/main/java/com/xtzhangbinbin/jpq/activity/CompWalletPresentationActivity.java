package com.xtzhangbinbin.jpq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.utils.ActivityUtil;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.NetUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.utils.StringUtil;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.MyProgressDialog;
import com.xtzhangbinbin.jpq.view.OrdinaryDialog;
import com.xtzhangbinbin.jpq.view.SingletonButtonDialog;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/26 0026.
 */

@SuppressWarnings("all")
public class CompWalletPresentationActivity extends BaseActivity {

    private List<String> accountList;
    @BindView(R.id.comp_wallet_per_icon)
    ImageView comp_wallet_per_icon;
    @BindView(R.id.comp_wallet_per_name)
    TextView comp_wallet_per_name;
    @BindView(R.id.comp_wallet_pre_card_layout)
    RelativeLayout comp_wallet_pre_card_layout;
    @BindView(R.id.comp_wallet_pre_balance)
    TextView comp_wallet_pre_balance;
    @BindView(R.id.comp_wallet_pre_submit)
    Button comp_wallet_balance_submit;
    @BindView(R.id.comp_wallet_pre_money)
    EditText comp_wallet_pre_money;
    //提款所使用帐户
    private String accountType;
    //余额
    private double balance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_wallet_presentation);
        ButterKnife.bind(this);
        ActivityUtil.addActivity(this);

        initData();
        //选择提款帐户
        choiceAccount();
        init();
    }

    public void init(){
        dialog = MyProgressDialog.createDialog(this);
        comp_wallet_pre_card_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("accountList", (ArrayList)accountList);
                JumpUtil.newInstance().jumpRight(CompWalletPresentationActivity.this, CompWalletBindStyleActivity.class, 0, bundle);
            }
        });

        comp_wallet_balance_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StringUtil.isEmpty(comp_wallet_pre_money.getText().toString())){
                    ToastUtil.show(CompWalletPresentationActivity.this, "请输入提现金额");
                } else if(Integer.parseInt(comp_wallet_pre_money.getText().toString()) < 100 ){
                    ToastUtil.show(CompWalletPresentationActivity.this, "提现金额不能小于100元");
                } else if(Integer.parseInt(comp_wallet_pre_money.getText().toString()) > balance ){
                    ToastUtil.show(CompWalletPresentationActivity.this, "提现金额不能大于余额");
                } else {
                    if (NetUtil.isNetAvailable(CompWalletPresentationActivity.this)) {
                        dialog.setMessage("正在提交提现申请，请稍候");
                        dialog.show();
                        Map<String ,String> map = new HashMap<>();
                        map.put("wallet_apply_money", comp_wallet_pre_money.getText().toString());
                        map.put("card_type", accountType);
                        OKhttptils.post(CompWalletPresentationActivity.this, Config.COMP_WALLET_GET_CASH_SUBMIT, map, new OKhttptils.HttpCallBack() {
                            @Override
                            public void success(String response) {
                                try {
                                    JSONObject object = new JSONObject(response);
                                    if("1".equals(object.getString("status"))){
                                        Prefs.with(getApplicationContext()).write("comp_wallet_account", accountType);
                                        final SingletonButtonDialog dialog = SingletonButtonDialog.newInstance(CompWalletPresentationActivity.this).setMessage1("信息提示").setMessage2("提现申请提交成功，财务人员会尽快将提现金额打入您的帐户！").showDialog();
                                        dialog.setYesOnclickListener(new SingletonButtonDialog.onYesOnclickListener() {
                                            @Override
                                            public void onYesClick() {
                                                dialog.dismiss();
                                                finish();
                                            }
                                        });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                closeDialog();
                            }

                            @Override
                            public void fail(String response) {
                                closeDialog();
                                Toast.makeText(CompWalletPresentationActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    public void initData(){
        //获取所有已绑定的帐户名称
        if(null != getIntent().getSerializableExtra("accountList")){
            accountList = (List) getIntent().getSerializableExtra("accountList");
        }
        //从缓存中获取上次提款时所使用的帐户
        accountType = Prefs.with(this).read("comp_wallet_account");
        if(StringUtil.isEmpty(accountType)){
            //如果之前没有提款，则使用第一个帐户信息
            accountType = accountList.get(0);
        }

        //获取余额
        Map<String, String> map = new HashMap<>();
        //获取余额数据
        if (NetUtil.isNetAvailable(this)) {
            OKhttptils.post(this, Config.COMP_WALLET_BALANCE, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if(null != obj){
                            comp_wallet_pre_balance.setText("￥" + new DecimalFormat("#0.00").format(obj.getJSONObject("data").getDouble("result")));
                            balance = obj.getJSONObject("data").getDouble("result");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void fail(String response) {
                    Toast.makeText(CompWalletPresentationActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("申请提现");
    }

    /**
     * 确认使用哪个帐户来提款
     */
    public void choiceAccount(){
        if(!StringUtil.isEmpty(accountType)){
            switch(accountType){
                case "wechat":
                    comp_wallet_per_icon.setImageResource(R.drawable.b_t_wechat);
                    comp_wallet_per_name.setText("微信");
                    break;
                case "alipay":
                    comp_wallet_per_icon.setImageResource(R.drawable.b_t_zfb);
                    comp_wallet_per_name.setText("支付宝");
                    break;
                case "bankcard":
                    comp_wallet_per_icon.setImageResource(R.drawable.qb_yhk);
                    comp_wallet_per_name.setText("银行卡");
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(null != data && !StringUtil.isEmpty(data.getStringExtra("comp_wallet_chioce_account"))){
            accountType = data.getStringExtra("comp_wallet_chioce_account");
        }
        choiceAccount();
    }
}
