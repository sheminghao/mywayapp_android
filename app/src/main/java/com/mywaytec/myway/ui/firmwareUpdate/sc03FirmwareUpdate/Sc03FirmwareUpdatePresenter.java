package com.mywaytec.myway.ui.firmwareUpdate.sc03FirmwareUpdate;

import android.util.Log;
import android.widget.TextView;

import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.bean.FirmwareInfo;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.view.CommonSubscriber;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/3/8.
 */

public class Sc03FirmwareUpdatePresenter extends RxPresenter<Sc03FirmwareUpdateView> {

    RetrofitHelper mRetrofitHelper;

    @Inject
    public Sc03FirmwareUpdatePresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    public RetrofitHelper getRetrofitHelper(){
        return mRetrofitHelper;
    }


    public void getVersionInfo(final String firmwareCode, final TextView tv) {
        //        if (null != firmwareCode && firmwareCode.length() > 4) {
        String carType = "";
        if (null != firmwareCode && firmwareCode.length() > 4) {
            carType = firmwareCode.substring(0, 4);
            mRetrofitHelper.getFirmware(carType)
                    .compose(RxUtil.<FirmwareInfo>rxSchedulerHelper())
                    .subscribe(new CommonSubscriber<FirmwareInfo>() {
                        @Override
                        public void onNext(FirmwareInfo firmwareInfo) {
                            if (firmwareInfo.getCode() == 1) {
                                Log.i("TAG", "------固件信息，" + firmwareInfo.getObj().getFiles());
                                tv.setText(firmwareInfo.getObj().getDescription());
                            }
                        }
                    });
        }
    }
}
