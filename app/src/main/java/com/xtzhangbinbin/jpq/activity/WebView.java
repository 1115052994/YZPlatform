package com.xtzhangbinbin.jpq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.utils.JumpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebView extends BaseActivity {

    @BindView(R.id.webView)
    android.webkit.WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String url = intent.getStringExtra("Url");
        if(url!=null){
            webView.loadUrl(url);
            //        //声明WebSettings子类
            WebSettings ws = webView.getSettings();
            ws.setSupportZoom(true);//支持缩放  默认true
            ws.setBuiltInZoomControls(true);//设置内置webview的缩放控件
            ws.setDisplayZoomControls(false);//隐藏原生的缩放控件
            ws.setDomStorageEnabled(true);//设置可以使用localStorage.
            ws.setJavaScriptEnabled(true);//如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript   这个必须写
            //设置自适应屏幕，两者合用
            ws.setUseWideViewPort(true); //将图片调整到适合webview的大小
            ws.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
            ws.setAllowFileAccess(true); //设置可以访问文件
            ws.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
            ws.setLoadsImagesAutomatically(true); //支持自动加载图片
            ws.setDefaultTextEncodingName("utf-8");//设置编码格式
            ws.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//优先加载缓存
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
                    webView.loadUrl(url);
                    return true;
                }
            });
        }



    }
    @Override
    protected void onPause(){
        super.onPause();

        webView.pauseTimers();
        if(isFinishing()){
            webView.loadUrl("about:blank");
            setContentView(new FrameLayout(this));
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        webView.resumeTimers();
    }
    @Override
    protected void onStart() {
        super.onStart();
        setTitle("详情");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(WebView.this);
            }
        });
    }
}
