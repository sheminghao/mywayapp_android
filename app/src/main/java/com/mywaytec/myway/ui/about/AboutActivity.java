package com.mywaytec.myway.ui.about;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.activity.AboutMywayActivity;
import com.mywaytec.myway.activity.GongnengJieshaoActivity;
import com.mywaytec.myway.activity.TermsAndPrivacyActivity;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.utils.AppUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity<AboutPresenter> implements AboutView{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.layout_about)
    CoordinatorLayout layoutAbout;

    private PackageInfo packageInfo;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_about;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.about);
        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        layoutAbout.setPadding(0, AppUtils.getStatusBar(), 0, 0);
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String appVersionName = packageInfo.versionName; // 版本名
        tvVersion.setText("v"+appVersionName);

        tvTitle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG","------setOnClickListener");
            }
        });
    }

    @Override
    protected void updateViews() {

    }

    @OnClick({R.id.layout_update, R.id.layout_about_myway, R.id.layout_gongnengjieshao,
                R.id.layout_tiaokuan_xieyi})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.layout_gongnengjieshao://功能介绍
                startActivity(new Intent(AboutActivity.this, GongnengJieshaoActivity.class));
                break;
            case R.id.layout_tiaokuan_xieyi://条款协议
                startActivity(new Intent(AboutActivity.this, TermsAndPrivacyActivity.class));
                break;
            case R.id.layout_update://检查更新
                mPresenter.checkVersion();
                break;
            case R.id.layout_about_myway://关于曼威
                startActivity(new Intent(AboutActivity.this, AboutMywayActivity.class));
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.i("TAG","------dispatchTouchEvent_ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("TAG","------dispatchTouchEvent_ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.i("TAG","------dispatchTouchEvent_ACTION_UP");
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
