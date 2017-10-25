package com.mywaytec.myway.ui.about;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.WindowManager;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.bean.VersionBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.DialogUtils;
import com.mywaytec.myway.utils.DownloadFileUtil;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CommonSubscriber;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/3/8.
 */

public class AboutPresenter extends RxPresenter<AboutView> {

    RetrofitHelper retrofitHelper;
    private PackageInfo packageInfo;
    Context mContext;

    @Inject
    public AboutPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(AboutView view) {
        super.attachView(view);
        mContext = mView.getContext();
    }

    //检查版本更新
    public void checkVersion(){
        retrofitHelper.getAppInfo()
                .compose(RxUtil.<VersionBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<VersionBean>() {
                    @Override
                    public void onNext(final VersionBean versionBean) {
                        if (versionBean.getCode() == 1) {
                            try {
                                packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                            String appVersionName = packageInfo.versionName; // 版本名
                            int currentVersionCode = packageInfo.versionCode; // 版本号

                            if (null != versionBean && currentVersionCode < versionBean.getObj().getMainVersion()) {
                                DialogUtils.confirmDialog(mContext, mContext.getResources().getString(R.string.a_new_version_has_been_found),
                                        new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (null != DialogUtils.confirmDialog) {
                                            DialogUtils.confirmDialog.dismiss();
                                        }
                                        //屏幕常亮
                                        ((AboutActivity)mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                                        DownloadFileUtil.init(mContext).start(versionBean.getObj().getAppUrl(), "MYWAY.apk").isProgressBar(true);
                                    }
                                });
                            }else {
                                ToastUtils.showToast(R.string.it_is_already_the_latest_version);
                            }
                        }
                    }
                });
    }
}
