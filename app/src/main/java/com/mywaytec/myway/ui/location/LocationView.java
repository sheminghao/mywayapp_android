package com.mywaytec.myway.ui.location;

import android.content.Context;

import com.baidu.mapapi.map.MapView;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/3/8.
 */

public interface LocationView extends IBaseView {

    LRecyclerView getRecyclerView();

    Context getContext();

    MapView getMapView();

}
