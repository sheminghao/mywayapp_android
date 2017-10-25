package com.mywaytec.myway.ui.faultDetect;

import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.ui.wayDetail.WayDetailView;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/3/8.
 */

public class FaultDetectPresenter extends RxPresenter<FaultDetectView> {

    @Inject
    public FaultDetectPresenter() {
        registerEvent();
    }

    void registerEvent() {
    }
}
