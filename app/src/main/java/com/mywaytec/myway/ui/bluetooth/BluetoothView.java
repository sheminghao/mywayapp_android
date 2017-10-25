package com.mywaytec.myway.ui.bluetooth;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.mywaytec.myway.base.IBaseView;
import com.zhy.autolayout.AutoLinearLayout;

/**
 * Created by shemh on 2017/3/8.
 */

public interface BluetoothView extends IBaseView {

    RecyclerView getMycarRecyclerView();

    Context getContext();

    AutoLinearLayout getLayoutMycarHave();

    AutoLinearLayout getLayoutMycarNo();

    LinearLayout getLayoutMoreCarInfo();

}
