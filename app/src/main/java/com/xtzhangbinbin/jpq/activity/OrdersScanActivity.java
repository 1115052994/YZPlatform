package com.xtzhangbinbin.jpq.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.CompWalletQuery;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.upgrade.UpgradeUtil;
import com.xtzhangbinbin.jpq.upgrade.UpgradetWindow;
import com.xtzhangbinbin.jpq.utils.ActivityUtil;
import com.xtzhangbinbin.jpq.utils.NetUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;

import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/5/1 0001.
 */
@SuppressWarnings("all")
public class OrdersScanActivity extends BaseActivity {

    @BindView(R.id.test)
    Button btn;
    private final int INSTALL_PACKAGES_REQUESTCODE = 0;
    private final int GET_UNKNOWN_APP_SOURCES = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_scan);
        ButterKnife.bind(this);
        ActivityUtil.addActivity(this);
        UpgradeUtil.getInstance(OrdersScanActivity.this).upgrade(btn);
//        initUpgrade();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpgradeUtil.getInstance(OrdersScanActivity.this).upgrade(btn);
//                upgrade();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("订单详情");
    }

}
