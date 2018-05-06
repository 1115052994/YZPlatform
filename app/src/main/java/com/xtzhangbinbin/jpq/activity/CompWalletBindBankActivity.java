package com.xtzhangbinbin.jpq.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.utils.ActivityUtil;
import com.xtzhangbinbin.jpq.utils.MyCountDownTimer;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.StringUtil;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.MyProgressDialog;
import com.xtzhangbinbin.jpq.view.OrdinaryDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/26 0026.
 */

@SuppressWarnings("all")
public class CompWalletBindBankActivity extends BaseActivity {

    @BindView(R.id.comp_wallet_bind_submit)
    Button comp_wallet_bind_submit;
    @BindView(R.id.comp_wallet_bind_checkcode)
    Button comp_wallet_bind_checkcode;
    @BindView(R.id.comp_wallet_bind_token_code)
    EditText comp_wallet_bind_token_code;
    @BindView(R.id.comp_wallet_bind_bank_name)
    EditText comp_wallet_bind_bank_name;
    @BindView(R.id.comp_wallet_bind_bank_username)
    EditText comp_wallet_bind_bank_username;
    @BindView(R.id.comp_wallet_bind_bank_code)
    EditText comp_wallet_bind_bank_code;
    @BindView(R.id.comp_wallet_bind_bank_address)
    EditText comp_wallet_bind_bank_address;

    private MyCountDownTimer countDownTimer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_wallet_bind_bank);
        ButterKnife.bind(this);
        ActivityUtil.addActivity(this);

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("绑定银行卡帐号");
    }

    public void init(){
        dialog = MyProgressDialog.createDialog(this);
        dialog.setMessage("正在提交信息，请稍候");
        comp_wallet_bind_checkcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCode();
            }
        });

        comp_wallet_bind_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StringUtil.isEmpty(comp_wallet_bind_bank_name.getText().toString())){
                    ToastUtil.show(getApplicationContext(), "请输入开户行名称");
                } else if(StringUtil.isEmpty(comp_wallet_bind_bank_username.getText().toString())){
                    ToastUtil.show(getApplicationContext(), "请输入开户人姓名");
                } else if(StringUtil.isEmpty(comp_wallet_bind_bank_code.getText().toString())){
                    ToastUtil.show(getApplicationContext(), "请输入银行卡号");
                } else if(StringUtil.isEmpty(comp_wallet_bind_bank_address.getText().toString())){
                    ToastUtil.show(getApplicationContext(), "请输入开户行地址");
                }else {
                    final OrdinaryDialog dialog = OrdinaryDialog.newInstance(CompWalletBindBankActivity.this).setMessage1("绑定银行帐号").setMessage2("您确定要绑定该帐号吗？").showDialog();
                    dialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick() {
                            submit();
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
    }

    /* 提交绑定信息 */
    private void submit() {
        dialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("card_bc_type", comp_wallet_bind_bank_name.getText().toString());//银行名称
        map.put("card_bc_no", comp_wallet_bind_bank_code.getText().toString());//银行卡号
        map.put("card_bc_name", comp_wallet_bind_bank_username.getText().toString());//开户人姓名
        map.put("card_bc_addr", comp_wallet_bind_bank_address.getText().toString());//开户行地址
        map.put("token_code", comp_wallet_bind_token_code.getText().toString());
        OKhttptils.post(CompWalletBindBankActivity.this, Config.COMP_WALLET_BIND_BANK, map, new OKhttptils.HttpCallBack() {
            @Override
            public String success(String response) {
                closeDialog();
                ToastUtil.show(CompWalletBindBankActivity.this, "银行帐户绑定成功！");
                CompWalletBindBankActivity.this.finish();
                return response;
            }

            @Override
            public void fail(String response) {
                closeDialog();
                try {
                    JSONObject obj = new JSONObject(response);
                    Toast.makeText(CompWalletBindBankActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    /* 获取验证码 */
    private void getCode() {
        countDownTimer = new MyCountDownTimer(comp_wallet_bind_checkcode, 60000, 1000);
        countDownTimer.start();
        Map<String, String> map = new HashMap<>();
        OKhttptils.post(CompWalletBindBankActivity.this, Config.COMP_WALLET_SEND_CHECKCODE, map, new OKhttptils.HttpCallBack() {
            @Override
            public String success(String response) {
                return response;
            }

            @Override
            public void fail(String response) {

            }
        });
    }


}
