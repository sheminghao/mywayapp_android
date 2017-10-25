package com.mywaytec.myway.ui.switchUnit;

import android.content.Context;

import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.ui.switchLanguage.SwitchLanguageView;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/3/8.
 */

public class SwitchUnitPresenter extends RxPresenter<SwitchUnitView> {

    RetrofitHelper retrofitHelper;
    Context mContext;

    @Inject
    public SwitchUnitPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }
}
