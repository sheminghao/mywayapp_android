package com.mywaytec.myway.ui.selectVenue;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;

import butterknife.BindView;

/**
 * 选择集合地点
 */
public class SelectVenueActivity extends BaseActivity<SelectVenuePresenter> implements SelectVenueView{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.mapview)
    MapView mapView;
    @BindView(R.id.lrecyclerView)
    LRecyclerView recyclerView;
    @BindView(R.id.tv_search)
    AutoCompleteTextView tvSearch;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_select_venue;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.select_gathering_location);
        mPresenter.init();
    }

    @Override
    protected void updateViews() {

    }


    @Override
    public LRecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public MapView getMapView() {
        return mapView;
    }

    @Override
    public AutoCompleteTextView getAutoCompleteTextView() {
        return tvSearch;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }
}
