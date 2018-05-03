package com.xtzhangbinbin.jpq.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.view.OrdinaryDialog;
import com.xtzhangbinbin.jpq.view.ZQImageViewRoundOval;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonalCenterActivity extends BaseActivity {

    @BindView(R.id.mIcon)
    ZQImageViewRoundOval mIcon;
    @BindView(R.id.mPhone)
    TextView mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.mBack, R.id.mSetting, R.id.mPriceWarn, R.id.mSubscribe, R.id.mOrders, R.id.mCollect, R.id.mTrack, R.id.mNewPhone, R.id.mKefu, R.id.mBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mBack:
                JumpUtil.newInstance().finishRightTrans(PersonalCenterActivity.this);
                break;
            case R.id.mSetting:
                JumpUtil.newInstance().jumpRight(this,PersonalSettingActivity.class);
                break;
            case R.id.mPriceWarn:
                JumpUtil.newInstance().jumpRight(this,PriceWarnActivity.class);
                break;
            case R.id.mSubscribe:
                //订阅车源
                break;
            case R.id.mOrders:
                //我的订单
                break;
            case R.id.mCollect:
                //我的收藏
                break;
            case R.id.mTrack:
                //我的足迹
                break;
            case R.id.mNewPhone:
                //换绑手机
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
                        JumpUtil.newInstance().jumpRight(PersonalCenterActivity.this,LoginActivity.class);
                    }
                });
                break;
        }
    }
}
