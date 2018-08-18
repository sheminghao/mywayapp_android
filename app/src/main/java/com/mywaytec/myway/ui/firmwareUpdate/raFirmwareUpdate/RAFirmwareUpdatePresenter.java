package com.mywaytec.myway.ui.firmwareUpdate.raFirmwareUpdate;

import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.http.RetrofitHelper;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/3/8.
 */

public class RAFirmwareUpdatePresenter extends RxPresenter<RAFirmwareUpdateView> {

    RetrofitHelper mRetrofitHelper;

    @Inject
    public RAFirmwareUpdatePresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    public RetrofitHelper getRetrofitHelper(){
        return mRetrofitHelper;
    }
}
