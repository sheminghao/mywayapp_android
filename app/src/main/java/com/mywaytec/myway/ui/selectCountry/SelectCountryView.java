package com.mywaytec.myway.ui.selectCountry;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.mywaytec.myway.base.IBaseView;
import com.mywaytec.myway.utils.indexlib.IndexBar.widget.IndexBar;

/**
 * Created by shemh on 2017/8/17.
 */

public interface SelectCountryView extends IBaseView {

    Context getContext();

    RecyclerView getRecyclerView();

    IndexBar getIndexBar();

    TextView getSideBarHintTV();

}
