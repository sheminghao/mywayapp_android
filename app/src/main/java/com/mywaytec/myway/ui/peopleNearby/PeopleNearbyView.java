package com.mywaytec.myway.ui.peopleNearby;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/8/3.
 */

public interface PeopleNearbyView extends IBaseView {

    Context getContext();

    RecyclerView getRecyclerView();

    MapView getMapview();

    ImageView getHeadPortraitImg();
    TextView getNameTV();
    TextView getDistanceTV();
    RelativeLayout getGenderLayout();
    ImageView getGenderImg();
    TextView getAgeTV();
    TextView getTimeTV();
    LinearLayout getInfoLayout();

}
