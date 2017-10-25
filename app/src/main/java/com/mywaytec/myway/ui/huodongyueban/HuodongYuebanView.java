package com.mywaytec.myway.ui.huodongyueban;

import android.content.Context;

import com.baidu.mapapi.map.MapView;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/3/8.
 */

public interface HuodongYuebanView extends IBaseView {

    Context getContext();

    MapView getMapView();

    LRecyclerView getFujinRecyclerView();

    LRecyclerView getWodeRecyclerView();

}
