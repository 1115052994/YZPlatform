package com.plt.yzplatform.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ExpandableListView;

import com.plt.yzplatform.R;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.utils.OKhttptils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/* 个人中心 - 降价提醒 */
public class PriceWarnActivity extends BaseActivity {

    private static final String TAG = "降价提醒" ;
    @BindView(R.id.mList)
    ExpandableListView mList;
    @BindView(R.id.mRefresh)
    SmartRefreshLayout mRefresh;

    private Activity currtAcxtivity;

    private int pageIndex;
    private int pageTotal;



    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 001:
                    break;
                case 002:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_warn);
        ButterKnife.bind(this);
        currtAcxtivity = this;
        getData();
    }

    /* 获取数据 */
    private void getData() {
        Map<String,String> map = new HashMap<>();
        map.put("pageIndex","1");
        map.put("pageSize","");
        OKhttptils.post(currtAcxtivity, Config.PERS_PRICE_WARN, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.d(TAG, "success: " + response);
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String data = jsonObject.getString("data");
                    JSONObject object = new JSONObject(data);
                    pageTotal = object.getInt("pageCount");

                    Map<String,String> m = json2map(data);
                    Log.w(TAG, "success: " + m.toString() );
                    

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fail(String response) {
                Log.d(TAG, "fail: " + response);
            }
        });
    }


}
