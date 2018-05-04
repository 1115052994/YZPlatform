package com.xtzhangbinbin.jpq.activity;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.CompWalletBindInfo;
import com.xtzhangbinbin.jpq.entity.CompWalletQuery;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.ActivityUtil;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.NetUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.view.OrdinaryDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/26 0026.
 */

@SuppressWarnings("all")
public class CompWalletBindAccountActivity extends BaseActivity {

    private boolean isBindAlipay;
    private boolean isBindWechat;
    private boolean isBindBank;
    @BindView(R.id.comp_wallet_bind_bank_msg)
    TextView comp_wallet_bind_bank_msg;
    @BindView(R.id.comp_wallet_bind_wechat_msg)
    TextView comp_wallet_bind_wechat_msg;
    @BindView(R.id.comp_wallet_bind_alipay_msg)
    TextView comp_wallet_bind_alipay_msg;
    @BindView(R.id.comp_wallet_bind_bank_layout)
    RelativeLayout comp_wallet_bind_bank_layout;
    @BindView(R.id.comp_wallet_bind_alipay_layout)
    RelativeLayout comp_wallet_bind_alipay_layout;
    @BindView(R.id.comp_wallet_bind_wechat_layout)
    RelativeLayout comp_wallet_bind_wechat_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_wallet_bind_account);
        ButterKnife.bind(this);
        ActivityUtil.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    public void initData(){
        if (NetUtil.isNetAvailable(this)) {
            Map<String ,String> map = new HashMap<>();
            OKhttptils.post(this, Config.COMP_WALLET_QUERY_ACCOUNT, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    Gson gson = GsonFactory.create();
                    CompWalletBindInfo bindInfo = gson.fromJson(response, CompWalletBindInfo.class);
                    if(null != bindInfo.getData().getResult().getData()){
                        if(bindInfo.getData().getResult().getData().contains("wechat")){
                            isBindWechat = true;
                            comp_wallet_bind_wechat_msg.setVisibility(View.VISIBLE);
                        }
                        if(bindInfo.getData().getResult().getData().contains("alipay")){
                            isBindAlipay = true;
                            comp_wallet_bind_alipay_msg.setVisibility(View.VISIBLE);
                        }
                        if(bindInfo.getData().getResult().getData().contains("bankcard")){
                            isBindBank = true;
                            comp_wallet_bind_bank_msg.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void fail(String response) {
                    Log.w("test", response);
                    Toast.makeText(CompWalletBindAccountActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                }
            });
        }

        comp_wallet_bind_wechat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isBindWechat){
                    final OrdinaryDialog dialog = OrdinaryDialog.newInstance(CompWalletBindAccountActivity.this).setMessage1("绑定微信帐号").setMessage2("您已经绑定了微信帐号，要更换新的帐号吗？").showDialog();
                    dialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick() {
                            JumpUtil.newInstance().jumpRight(CompWalletBindAccountActivity.this, CompWalletBindWechatActivity.class);
                            dialog.dismiss();
                        }
                    });
                    dialog.setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
                        @Override
                        public void onNoClick() {
                            dialog.dismiss();
                        }
                    });
                } else {
                    JumpUtil.newInstance().jumpRight(CompWalletBindAccountActivity.this, CompWalletBindWechatActivity.class);
                }
            }
        });

        comp_wallet_bind_alipay_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isBindAlipay){
                    final OrdinaryDialog dialog = OrdinaryDialog.newInstance(CompWalletBindAccountActivity.this).setMessage1("绑定支付宝帐号").setMessage2("您已经绑定了支付宝帐号，要更换新的帐号吗？").showDialog();
                    dialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick() {
                            JumpUtil.newInstance().jumpRight(CompWalletBindAccountActivity.this, CompWalletBindAlipayActivity.class);
                            dialog.dismiss();
                        }
                    });
                    dialog.setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
                        @Override
                        public void onNoClick() {
                            dialog.dismiss();
                        }
                    });
                } else {
                    JumpUtil.newInstance().jumpRight(CompWalletBindAccountActivity.this, CompWalletBindAlipayActivity.class);
                }
            }
        });

        comp_wallet_bind_bank_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isBindWechat){
                    final OrdinaryDialog dialog = OrdinaryDialog.newInstance(CompWalletBindAccountActivity.this).setMessage1("绑定银行帐号").setMessage2("您已经绑定了银行帐号，要更换新的帐号吗？").showDialog();
                    dialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick() {
                            JumpUtil.newInstance().jumpRight(CompWalletBindAccountActivity.this, CompWalletBindBankActivity.class);
                            dialog.dismiss();
                        }
                    });
                    dialog.setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
                        @Override
                        public void onNoClick() {
                            dialog.dismiss();
                        }
                    });
                } else {
                    JumpUtil.newInstance().jumpRight(CompWalletBindAccountActivity.this, CompWalletBindBankActivity.class);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("绑定帐户");
    }

}
