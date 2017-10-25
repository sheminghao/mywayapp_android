package com.mywaytec.myway.ui.gprs.editFence;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.WindowManager;

import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.bean.VersionBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.ui.about.AboutActivity;
import com.mywaytec.myway.ui.about.AboutView;
import com.mywaytec.myway.ui.bluetooth.BluetoothActivity;
import com.mywaytec.myway.utils.DialogUtils;
import com.mywaytec.myway.utils.DownloadFileUtil;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CommonSubscriber;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/3/8.
 */

public class EditFencePresenter extends RxPresenter<EditFenceView> {

    RetrofitHelper retrofitHelper;
    Context mContext;

    @Inject
    public EditFencePresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(EditFenceView view) {
        super.attachView(view);
        mContext = mView.getContext();
    }

    //修改电子围栏名称
    public void changeName(String objectId, final String newName, final int position){
        retrofitHelper.updateVehicleFence(objectId, newName)
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseInfo>() {
                    @Override
                    public void onNext(BaseInfo baseInfo) {
                        if (baseInfo.getCode() == 1){
                            Intent intent = new Intent();
                            intent.putExtra("newName", newName);
                            intent.putExtra("position", position);
                            intent.putExtra("type", "change");
                            ((EditFenceActivity) mContext).setResult(Activity.RESULT_OK, intent);
                            ((EditFenceActivity) mContext).finish();
                        }else {
                            ToastUtils.showToast(baseInfo.getMsg());
                        }
                    }
                });
    }

    //删除电子围栏
    public void deleteFence(String objectId, final int position){
        retrofitHelper.deleteVehicleFence(objectId)
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseInfo>() {
                    @Override
                    public void onNext(BaseInfo baseInfo) {
                        if (baseInfo.getCode() == 1){
                            Intent intent = new Intent();
                            intent.putExtra("position", position);
                            intent.putExtra("type", "delete");
                            ((EditFenceActivity) mContext).setResult(Activity.RESULT_OK, intent);
                            ((EditFenceActivity) mContext).finish();
                        }else {
                            ToastUtils.showToast(baseInfo.getMsg());
                        }
                    }
                });
    }
}
