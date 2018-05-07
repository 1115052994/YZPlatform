package com.xtzhangbinbin.jpq.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.BespeakComp;
import com.xtzhangbinbin.jpq.entity.Enterprise;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.DataCleanManager;
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
import io.reactivex.functions.Consumer;

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
                JumpUtil.newInstance().jumpRight(this, CompOrderListsActivity.class);
                break;
            case R.id.mWallet:
                //钱包
                JumpUtil.newInstance().jumpRight(this, CompWalletActivity.class);
                break;
            case R.id.mBook:
                //预约信息
                JumpUtil.newInstance().jumpRight(this, BespeakComp.class);
                break;
            case R.id.mAppraise:
                //评价管理
                break;
            case R.id.mClean:
                //清除缓存
                final OrdinaryDialog ordinaryDialog = OrdinaryDialog.newInstance(this).setMessage1("清除缓存").setMessage2("清除后图片等多媒体消息需要重新下载查看，确定清除？").showDialog();
                ordinaryDialog.setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        ordinaryDialog.dismiss();
                    }
                });
                ordinaryDialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        DataCleanManager.clearAllCache(CompanyCenterActivity.this);
                        ordinaryDialog.dismiss();
//                        try {
//                            String s = DataCleanManager.getTotalCacheSize(CompanyCenterActivity.this);
//                            clean.setText("有" + s + "缓存可以清除");
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                    }
                });
                break;
            case R.id.mNewPhone:
                //换绑手机号
                JumpUtil.newInstance().jumpRight(CompanyCenterActivity.this,ChangeOldPhoneActivity.class);
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
                        JumpUtil.newInstance().jumpRight(CompanyCenterActivity.this, LoginActivity.class);
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
                            Toast.makeText(CompanyCenterActivity.this, "您没有授权该权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
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
