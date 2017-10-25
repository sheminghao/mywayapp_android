package com.mywaytec.myway.activity;

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
import com.mywaytec.myway.view.ProgressWebView;

import butterknife.BindView;

/**
 * 曼威条款协议
 */
public class TermsAndPrivacyActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.webview)
    ProgressWebView webView;
    @BindView(R.id.activity_terms_and_privacy)
    LinearLayout layoutTermsAndPrivacy;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_terms_and_privacy;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.terms_and_privacy);
        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        layoutTermsAndPrivacy.setPadding(0, AppUtils.getStatusBar(), 0, 0);

        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        webView.setWebViewClient(new WebViewClient());
        //获得网页的加载进度并显示
//        webView.setWebChromeClient(new WebChromeClient());

        webView.loadUrl("http://www.mywaytec.cn/mobile/protocol.html");
    }

    @Override
    protected void updateViews() {

    }
}
