package com.xtzhangbinbin.jpq.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.CompWalletBindInfo;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.ActivityUtil;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.NetUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.view.OrdinaryDialog;

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
public class CompWalletBindStyleActivity extends BaseActivity {

    @BindView(R.id.comp_wallet_bind_style_bank)
    CheckBox comp_wallet_bind_style_bank;
    @BindView(R.id.comp_wallet_bind_style_wechat)
    CheckBox comp_wallet_bind_style_wechat;
    @BindView(R.id.comp_wallet_bind_style_alipay)
    CheckBox comp_wallet_bind_style_alipay;

    @BindView(R.id.comp_wallet_bind_bank_layout)
    RelativeLayout comp_wallet_bind_bank_layout;
    @BindView(R.id.comp_wallet_bind_wechat_layout)
    RelativeLayout comp_wallet_bind_wechat_layout;
    @BindView(R.id.comp_wallet_bind_alipay_layout)
    RelativeLayout comp_wallet_bind_alipay_layout;

    private List<CheckBox> list;
    private List<String> accountList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_wallet_bind_style);
        ButterKnife.bind(this);
        ActivityUtil.addActivity(this);

        init();
        initData();
    }

    public void init(){
        list = new ArrayList<>();
        list.add(comp_wallet_bind_style_bank);
        list.add(comp_wallet_bind_style_wechat);
        list.add(comp_wallet_bind_style_alipay);

        comp_wallet_bind_style_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearStyle();
                list.get(0).setChecked(true);
                Bundle bundle = new Bundle();
                bundle.putString("comp_wallet_chioce_account", "bankcard");
                JumpUtil.newInstance().finishRightTrans(CompWalletBindStyleActivity.this, bundle, 0);
            }
        });
        comp_wallet_bind_style_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearStyle();
                list.get(1).setChecked(true);
                Bundle bundle = new Bundle();
                bundle.putString("comp_wallet_chioce_account", "wechat");
                JumpUtil.newInstance().finishRightTrans(CompWalletBindStyleActivity.this, bundle, 0);
            }
        });
        comp_wallet_bind_style_alipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearStyle();
                list.get(2).setChecked(true);
                Bundle bundle = new Bundle();
                bundle.putString("comp_wallet_chioce_account", "alipay");
                JumpUtil.newInstance().finishRightTrans(CompWalletBindStyleActivity.this, bundle, 0);
            }
        });
    }

    public void initData(){
        //获取所有已绑定的帐户名称
        if(null != getIntent().getSerializableExtra("accountList")){
            accountList = (List) getIntent().getSerializableExtra("accountList");
            if(accountList.contains("wechat")){
                comp_wallet_bind_wechat_layout.setVisibility(View.VISIBLE);
            }
            if(accountList.contains("alipay")){
                comp_wallet_bind_alipay_layout.setVisibility(View.VISIBLE);
            }
            if(accountList.contains("bankcard")){
                comp_wallet_bind_bank_layout.setVisibility(View.VISIBLE);
            }
        }
    }

    public void clearStyle(){
        if(null != list){
            for(CheckBox cb : list){
                cb.setChecked(false);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onStart() {
        super.onStart();
        setTitle("请选择提现方式");
    }

}
