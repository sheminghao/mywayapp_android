package com.mywaytec.myway.ui.gprs.vehicleLocation;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.baidu.mapapi.map.MapView;
import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/8/24.
 */

public interface VehicleLocationView extends IBaseView {

    Context getContext();

    MapView getMapView();

    View getCoverView();

    String getVehicleName();

}
