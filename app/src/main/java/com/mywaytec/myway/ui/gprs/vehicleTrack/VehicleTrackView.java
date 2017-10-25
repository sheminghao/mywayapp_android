package com.mywaytec.myway.ui.gprs.vehicleTrack;

import android.content.Context;

import com.baidu.mapapi.map.MapView;
import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/8/24.
 */

public interface VehicleTrackView extends IBaseView {

    Context getContext();

    MapView getMapView();

}
