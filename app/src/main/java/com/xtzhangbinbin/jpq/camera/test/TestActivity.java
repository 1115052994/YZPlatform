package com.xtzhangbinbin.jpq.camera.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.xtzhangbinbin.jpq.R;


/**
 * Created by Administrator on 2018/4/23 0023.
 */

public class TestActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

//        try {
//            BufferedReader br = new BufferedReader(new InputStreamReader(this.getAssets().open("json/aaa.json"), "gbk"));
//
//            String str = br.readLine();
//            Log.w("test", str);
//            JSONObject object = new JSONObject(str);
//            Log.w("test", object.getJSONObject("jingdong_new_ware_mobilebigfield_get_responce").getString("result"));
//            TextView test = (TextView) findViewById(R.id.testView);
//            test.setText(Html.fromHtml(object.getJSONObject("jingdong_new_ware_mobilebigfield_get_responce").getString("result")));
//            br.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl("https://item.m.jd.com/product/11796006087.html");
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 在APP内部打开链接，不要调用系统浏览器
                view.loadUrl(url);
                return true;
            }
        });

        TextView back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestActivity.this.finish();
            }
        });
    }
}
