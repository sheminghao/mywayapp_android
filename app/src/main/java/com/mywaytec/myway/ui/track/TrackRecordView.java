package com.mywaytec.myway.ui.track;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/3/8.
 */

public interface TrackRecordView extends IBaseView {

    MapView getMapView();

    TextView getLichengTV();
    TextView getSpeedTV();
    TextView getTimeTV();
    TextView getGpsSignalTV();
    ImageView getGpsSignalImg();
    View getViewBackground();

    Context getContext();
}
