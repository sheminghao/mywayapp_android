package com.mywaytec.myway.ui.firmwareUpdate;

import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.http.RetrofitHelper;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/3/8.
 */

public class FirmwareUpdatePresenter extends RxPresenter<FirmwareUpdateView> {

    RetrofitHelper mRetrofitHelper;

    @Inject
    public FirmwareUpdatePresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    public RetrofitHelper getRetrofitHelper(){
        return mRetrofitHelper;
    }
}
