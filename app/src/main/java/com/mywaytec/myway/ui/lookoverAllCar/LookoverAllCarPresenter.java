package com.mywaytec.myway.ui.lookoverAllCar;

import android.content.Context;

import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.ui.switchUnit.SwitchUnitView;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/3/8.
 */

public class LookoverAllCarPresenter extends RxPresenter<LookoverAllCarView> {

    RetrofitHelper retrofitHelper;
    Context mContext;

    @Inject
    public LookoverAllCarPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }
}
