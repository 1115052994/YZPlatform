package com.xtzhangbinbin.jpq.activity;
/**
 * @Author jiangjiang
 * @CreateTime 2017/6/26
 * .欢迎界面（实现第一次启动引导界面，后面则自动进入主界面）
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.utils.OKhttptils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Welcome extends AppCompatActivity {

    @BindView(R.id.image_bg)
    ImageView imageBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        //启动一个延迟线程
        //new Thread(this).start();
        getData();
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        OKhttptils.post(this, Config.SELECTBOOTPAGE, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if ("1".equals(status)) {
                        JSONObject data = object.getJSONObject("data");
                        String fieldId = data.getString("result");
                        OKhttptils.getPic(Welcome.this, fieldId, imageBg);
                        // 开始启动
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startAPP();
                            }
                        }, 3000);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {

            }
        });
    }

    private void startAPP() {

        SharedPreferences preferences = getSharedPreferences("count", 0); // 存在则打开它，否则创建新的Preferences
        int count = preferences.getInt("count", 0); // 取出数据
        /**
         *如果用户不是第一次使用则直接调转到显示界面,否则调转到引导界面
         */
        if (count == 0) {
            Intent intent1 = new Intent();
            intent1.setClass(Welcome.this, Guide.class);
            startActivity(intent1);
        } else {
            Intent intent2 = new Intent();
            intent2.setClass(Welcome.this, LoginActivity.class);
            startActivity(intent2);
        }
        finish();

        //实例化Editor对象
        SharedPreferences.Editor editor = preferences.edit();
        //存入数据
        editor.putInt("count", 1); // 存入数据
        //提交修改
        editor.commit();
    }

}
