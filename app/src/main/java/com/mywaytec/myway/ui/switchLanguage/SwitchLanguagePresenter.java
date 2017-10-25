package com.mywaytec.myway.ui.switchLanguage;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;

import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.bean.VersionBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.ui.about.AboutView;
import com.mywaytec.myway.utils.DialogUtils;
import com.mywaytec.myway.utils.DownloadFileUtil;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CommonSubscriber;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/3/8.
 */

public class SwitchLanguagePresenter extends RxPresenter<SwitchLanguageView> {

    RetrofitHelper retrofitHelper;
    Context mContext;

    @Inject
    public SwitchLanguagePresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }
}
