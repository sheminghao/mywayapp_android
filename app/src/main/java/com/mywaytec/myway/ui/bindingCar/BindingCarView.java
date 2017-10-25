package com.mywaytec.myway.ui.bindingCar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/3/8.
 */

public interface BindingCarView extends IBaseView {

    Context getContext();

    RecyclerView getRecyclerView();

    TextView getBindingCarTV();

}
