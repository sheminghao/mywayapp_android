package com.mywaytec.myway.ui.gprs.electronicFence;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/8/24.
 */

public interface ElectronicFenceView extends IBaseView {

    Context getContext();

    MapView getMapView();

    SeekBar getSeekBar();

    TextView getSelectHintTV();

    RecyclerView getDianziweilanRecyclerView();

    LinearLayout getSelectDianziweilanLayout();

}
