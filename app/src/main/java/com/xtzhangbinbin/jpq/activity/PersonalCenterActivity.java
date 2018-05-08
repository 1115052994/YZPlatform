package com.xtzhangbinbin.jpq.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;
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
import io.reactivex.functions.Consumer;

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
                    if (resultBean!=null) {
                        head_file_id = resultBean.getPers_head_file_id();
                        phone = resultBean.getPers_phone();
                        mPhone.setText(DataConcealUtil.toConceal(phone));
                        if (head_file_id == "") {

                        } else {
                            OKhttptils.getPic(PersonalCenterActivity.this, head_file_id, mIcon);
                        }
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
                JumpUtil.newInstance().jumpRight(this, OrdersPersonalActivity.class);
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
                call("4001198698");
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


    /* 拨打客服电话 */
    private RxPermissions rxPermission;

    private void call(final String phone) {
        // 申请定位权限
        rxPermission = new RxPermissions(this);
        rxPermission.request(Manifest.permission.CALL_PHONE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) { // 在android 6.0之前会默认返回true
                            // 已经获取权限
                            Intent intent = new Intent(); // 意图对象：动作 + 数据
                            intent.setAction(Intent.ACTION_CALL); // 设置动作
                            Uri data = Uri.parse("tel:" + phone); // 设置数据
                            intent.setData(data);
                            startActivity(intent); // 激活Activity组件
                        } else {
                            // 未获取权限
                            Toast.makeText(PersonalCenterActivity.this, "您没有授权该权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if (Build.VERSION.SDK_INT >= 9) {
                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.setData(Uri.fromParts("package", getPackageName(), null));
                            } else if (Build.VERSION.SDK_INT <= 8) {
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                                intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
                            }
                            startActivity(intent);
                        }
                    }
                });

    }
}
