package com.mywaytec.myway.ui.selectVenue;

import android.content.Context;
import android.widget.AutoCompleteTextView;

import com.baidu.mapapi.map.MapView;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/8/17.
 */

public interface SelectVenueView extends IBaseView {

    LRecyclerView getRecyclerView();

    Context getContext();

    MapView getMapView();

    AutoCompleteTextView getAutoCompleteTextView();

}
