package com.xtzhangbinbin.jpq.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.PersonalSetting;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.DataConcealUtil;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.OrdinaryDialog;
import com.xtzhangbinbin.jpq.view.ZQImageViewRoundOval;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonalCenterActivity extends BaseActivity {

    private static final String TAG = "个人中心" ;
    @BindView(R.id.mIcon)
    ZQImageViewRoundOval mIcon;
    @BindView(R.id.mPhone)
    TextView mPhone;
    private String phone;
    private String head_file_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        ButterKnife.bind(this);
        getData();
    }

    /* 获取数据 */
    private void getData() {
            /* 获取数据 */
            Map<String, String> map = new HashMap<>();
            OKhttptils.post(PersonalCenterActivity.this, Config.GET_PERS_INFO, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    Log.d(TAG, "success个人设置: " + response);
                    Gson gson = GsonFactory.create();
                    PersonalSetting setting = gson.fromJson(response, PersonalSetting.class);
                    PersonalSetting.DataBean.ResultBean resultBean = setting.getData().getResult();
                    head_file_id = resultBean.getPers_head_file_id();
                    phone = resultBean.getPers_phone();
                    mPhone.setText(DataConcealUtil.toConceal(phone));
                    if (head_file_id == "") {


                    } else {
                        OKhttptils.getPic(PersonalCenterActivity.this, head_file_id, mIcon);
                    }

                }

                @Override
                public void fail(String response) {
                    ToastUtil.noNAR(PersonalCenterActivity.this);
                }
            });
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
                JumpUtil.newInstance().jumpRight(this,MyFavorite.class);
                break;
            case R.id.mTrack:
                //我的足迹
                JumpUtil.newInstance().jumpRight(this,MyBrowsing.class);
                break;
            case R.id.mNewPhone:
                //换绑手机
                JumpUtil.newInstance().jumpRight(PersonalCenterActivity.this,ChangeOldPhoneActivity.class);
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
