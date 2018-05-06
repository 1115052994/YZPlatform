package com.xtzhangbinbin.jpq.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.Enterprise;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.view.OrdinaryDialog;
import com.xtzhangbinbin.jpq.view.ZQImageViewRoundOval;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompanyCenterActivity extends BaseActivity {

    private static final String TAG = "企业中心";
    @BindView(R.id.mAuto)
    LinearLayout mAuto;
    @BindView(R.id.mIcon)
    ZQImageViewRoundOval mIcon;
    @BindView(R.id.mName)
    TextView mName;
    @BindView(R.id.mState)
    TextView mState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_center);
        ButterKnife.bind(this);
        initView();
        getData();
    }

    /* 设置 */
    private void initView() {
        mIcon.setRoundRadius(5);
        mIcon.setType(ZQImageViewRoundOval.TYPE_ROUND);
    }

    /* 获取数据 */
    private void getData() {
        Map<String, String> map = new HashMap<>();
        OKhttptils.post(this, Config.GETCOMP_INFO, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Gson gson = GsonFactory.create();
                Enterprise enterprise = gson.fromJson(response, Enterprise.class);
                Enterprise.DataBean dataBean = enterprise.getData();
                if (null == dataBean.getResult()) {
                    mState.setText("未审核");
                } else {
                    Enterprise.DataBean.ResultBean resultBean = enterprise.getData().getResult();
                    //审核状态
                    if ("1".equals(resultBean.getAuth_audit_state())) {
                        mState.setText("审核拒绝");
                    } else if ("2".equals(resultBean.getAuth_audit_state())) {
                        mState.setText("审核通过");
                    } else if ("3".equals(resultBean.getAuth_audit_state())) {
                        mState.setText("正在审核");
                    }
                    //赋值
                    mName.setText(resultBean.getAuth_comp_name());
                    String mentou = resultBean.getAuth_comp_img_head_file_id();
                    Log.d(TAG, "success: " + mentou);
                    Prefs.with(getApplicationContext()).write("门头照", mentou);
                    OKhttptils.getPic(CompanyCenterActivity.this, mentou, mIcon);
                }
            }

            @Override
            public void fail(String response) {

            }
        });
    }

    @OnClick({R.id.mBack, R.id.mAuto, R.id.mProduct, R.id.mOrders, R.id.mWallet, R.id.mBook, R.id.mAppraise, R.id.mClean, R.id.mKefu, R.id.mBtn, R.id.mNewPhone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mBack:
                JumpUtil.newInstance().finishRightTrans(this);
                break;
            case R.id.mAuto:
                JumpUtil.newInstance().jumpRight(this, EnterpriseActivity.class);
                break;
            case R.id.mProduct:
                //产品管理
                JumpUtil.newInstance().jumpRight(this, ProductList.class);
                break;
            case R.id.mOrders:
                //订单管理
                break;
            case R.id.mWallet:
                //钱包
                break;
            case R.id.mBook:
                //预约信息
                break;
            case R.id.mAppraise:
                //评价管理
                break;
            case R.id.mClean:
                //清除缓存
                break;
            case R.id.mNewPhone:
                //换绑手机号
                JumpUtil.newInstance().jumpRight(CompanyCenterActivity.this,ChangeOldPhoneActivity.class);
                break;
            case R.id.mKefu:
                //联系客服
                break;
            case R.id.mBtn:
                //退出登录

                final OrdinaryDialog dialog = OrdinaryDialog.newInstance(this).setMessage1("退出登录").setMessage2("确定退出当前账号吗").showDialog();
                dialog.setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        dialog.dismiss();
                    }
                });

                dialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        dialog.dismiss();
                        Prefs.with(getApplicationContext()).clear();
                        JumpUtil.newInstance().jumpRight(CompanyCenterActivity.this, LoginActivity.class);
                    }
                });
                break;
        }
    }
}
