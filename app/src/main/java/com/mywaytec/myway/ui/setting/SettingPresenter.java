package com.mywaytec.myway.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.WindowManager;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.bean.VersionBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.ui.about.AboutActivity;
import com.mywaytec.myway.ui.about.AboutView;
import com.mywaytec.myway.ui.main.MainActivity;
import com.mywaytec.myway.utils.DialogUtils;
import com.mywaytec.myway.utils.DownloadFileUtil;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CommonSubscriber;

import javax.inject.Inject;

import butterknife.OnClick;

/**
 * Created by shemh on 2017/3/8.
 */

public class SettingPresenter extends RxPresenter<SettingView> {

    RetrofitHelper retrofitHelper;
    Context mContext;

    @Inject
    public SettingPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(SettingView view) {
        super.attachView(view);
        mContext = mView.getContext();
    }
}
