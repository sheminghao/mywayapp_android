package com.mywaytec.myway.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.http.SslError;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;

import butterknife.BindView;

/**
 * 商城
 */
@SuppressLint("JavascriptInterface")
public class StoreActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
//    @BindView(R.id.img)
//    CircleImageView img;
    @BindView(R.id.webview)
    WebView webView;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_store;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initViews() {
        tvTitle.setText("商城");

//        Animation animation = AnimationUtils.loadAnimation(this, R.anim.enlarge);
//        img.setAnimation(animation);

        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
//        webSettings.setUseWideViewPort(false); //将图片调整到适合webview的大小
//        webSettings.setLoadWithOverviewMode(false); // 缩放至屏幕的大小

        //缩放操作
//        webSettings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
//        webSettings.setBuiltInZoomControls(false); //设置内置的缩放控件。若为false，则该WebView不可缩放
//        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
//        webSettings.setAllowFileAccess(true); //设置可以访问文件
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
//        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
//        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
//        webSettings.setDatabaseEnabled(true);//启用数据库
//        webSettings.setGeolocationEnabled(true);//启用地理定位
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setAllowContentAccess(true);

//        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        webSettings.setUseWideViewPort(true);
//        webSettings.setLoadWithOverviewMode(true);

        webSettings.setAppCacheMaxSize(1024 * 1024 * 9);// 设置缓冲大小
        webSettings.setAppCacheEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url){
                Location location = getGPSLocation(StoreActivity.this);
                final String centerURL ="javascript:initMap("+location.getLatitude()+","+ location.getLongitude()+")";
                webView.loadUrl(centerURL);
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                sslErrorHandler.proceed();// 接受所有网站的证书
            }
        });
        //获得网页的加载进度并显示
        webView.setWebChromeClient(new WebChromeClient() {
              @Override
              public void onProgressChanged(WebView view, int newProgress) {
            }
         });

//        webView.loadUrl("https://shop363904086.taobao.com/index.htm?spm=a1z10.1-c.w5002-15641786646.2.4e1bfe17U0xDYW");
//        webView.loadUrl("http://www.google.cn/maps");

        webView.loadUrl("file:///android_asset/googlemaps.html");
    }

    @Override
    protected void updateViews() {

    }

    /**
     * 获取最好的定位方式
     */
    public Location getBestLocation(Context context, Criteria criteria) {
        Location location;
        LocationManager manager = getLocationManager(context);
        if (criteria == null) {
            criteria = new Criteria();
        }
        String provider = manager.getBestProvider(criteria, true);
        if (TextUtils.isEmpty(provider)) {
            //如果找不到最适合的定位，使用network定位
            location = getNetWorkLocation(context);
        } else {
            //高版本的权限检查
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            //获取最适合的定位方式的最后的定位权限
            location = manager.getLastKnownLocation(provider);
        }
        return location;
    }

    private LocationManager getLocationManager(@NonNull Context context) {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * network获取定位方式
     */
    public Location getNetWorkLocation(Context context) {
        Location location = null;
        LocationManager manager = getLocationManager(context);
        //高版本的权限检查
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {//是否支持Network定位
            //获取最后的network定位信息
            location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        return location;
    }

    /**
     * GPS获取定位方式
     */
    public Location getGPSLocation(@NonNull Context context) {
        Location location = null;
        LocationManager manager = getLocationManager(context);
        //高版本的权限检查
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {//是否支持GPS定位
            //获取最后的GPS定位信息，如果是第一次打开，一般会拿不到定位信息，一般可以请求监听，在有效的时间范围可以获取定位信息
            location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        return location;
    }


}
