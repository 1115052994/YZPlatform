package com.xtzhangbinbin.jpq.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xtzhangbinbin.jpq.R;

public class CarProductJD extends AppCompatActivity {

    private WebView webView;
    private String url = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_product_jd);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                url = bundle.getString("String");
                if (!"".equals(url)){
                    if (webView == null)
                        webView = (WebView) findViewById(R.id.webView);
                    webView.setVisibility(View.VISIBLE);
                    webView.loadUrl(url);
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
                }
            }
        }

    }
}
