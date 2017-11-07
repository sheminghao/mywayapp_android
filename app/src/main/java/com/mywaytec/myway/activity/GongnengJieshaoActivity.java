package com.mywaytec.myway.activity;

import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.utils.AppUtils;
import com.mywaytec.myway.utils.PreferencesUtils;

import java.util.Locale;

import butterknife.BindView;

public class GongnengJieshaoActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.activity_gongneng_jieshao)
    LinearLayout layoutGongnengJieshao;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_gongneng_jieshao;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initViews() {
        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        layoutGongnengJieshao.setPadding(0, AppUtils.getStatusBar(), 0, 0);
        tvTitle.setText("功能介绍");

        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        Log.i("TAG", "------language,"+language);

        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        webView.setWebViewClient(new WebViewClient());
        //获得网页的加载进度并显示
        webView.setWebChromeClient(new WebChromeClient());

        String languagePre = PreferencesUtils.getString(this, "language", "aotu");
        if("zh".equals(languagePre)){
            webView.loadUrl("file:///android_asset/gongnengjieshao.png");
        }else if("en".equals(languagePre)){
            webView.loadUrl("file:///android_asset/gongnengjieshao_en.png");
        }else {
            if ("zh".equals(language)){
                webView.loadUrl("file:///android_asset/gongnengjieshao.png");
            }else {
                webView.loadUrl("file:///android_asset/gongnengjieshao_en.png");
            }
        }

    }

    @Override
    protected void updateViews() {

    }
}
