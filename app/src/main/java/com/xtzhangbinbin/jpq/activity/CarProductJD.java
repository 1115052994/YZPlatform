package com.xtzhangbinbin.jpq.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
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
                    webView.loadUrl(url);
                    webView.setVisibility(View.VISIBLE);
                    WebSettings ws = webView.getSettings();
                    ws.setSupportZoom(true);//支持缩放  默认true
                    //ws.setBuiltInZoomControls(true);//设置内置webview的缩放控件
                    //ws.setDisplayZoomControls(false);//隐藏原生的缩放控件
                    //ws.setDomStorageEnabled(true);//设置可以使用localStorage.
                    ws.setJavaScriptEnabled(true);//如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript   这个必须写
                    //设置自适应屏幕，两者合用
                    ws.setUseWideViewPort(true); //将图片调整到适合webview的大小
                    // ws.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
                    //ws.setAllowFileAccess(true); //设置可以访问文件
                    ws.supportMultipleWindows();  //多窗口
                    ws.setLoadsImagesAutomatically(true);  //支持自动加载图片
                    // ws.setAllowFileAccess(true);  //设置可以访问文件
                    ws.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
                    ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
                    ws.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
                    ws.setLoadsImagesAutomatically(true); //支持自动加载图片
                    ws.setDefaultTextEncodingName("utf-8");//设置编码格式
                    ws.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//优先加载缓存
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
