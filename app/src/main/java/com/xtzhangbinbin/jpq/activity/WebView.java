package com.xtzhangbinbin.jpq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.utils.JumpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebView extends BaseActivity {


    @BindView(R.id.webView)
    android.webkit.WebView webView;
    @BindView(R.id.progress_Bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String url = intent.getStringExtra("Url");
        if (url != null) {
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
            ws.supportMultipleWindows();  //多窗口
            ws.setLoadsImagesAutomatically(true);  //支持自动加载图片
            ws.setAllowFileAccess(true);  //设置可以访问文件
            ws.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
            ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
            ws.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
            ws.setLoadsImagesAutomatically(true); //支持自动加载图片
            ws.setDefaultTextEncodingName("utf-8");//设置编码格式
            ws.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//优先加载缓存
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
                    webView.loadUrl(url);
                    return true;
                }
            });
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //这是一个监听用的按键的方法，keyCode 监听用户的动作，如果是按了返回键，同时Webview要返回的话，WebView执行回退操作，因为mWebView.canGoBack()返回的是一个Boolean类型，所以我们把它返回为true
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onPause() {
        super.onPause();
        webView.pauseTimers();
        if (isFinishing()) {
            webView.loadUrl("about:blank");
            setContentView(new FrameLayout(this));
        }
    }

    @Override
    protected void onResume() {
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
