package com.mywaytec.myway.ui.gprs.vehicleTrack;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.bigkoo.pickerview.TimePickerView;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

import static com.mywaytec.myway.ui.gprs.GPRSActivity.SNCODE;

/**
 * 车辆轨迹
 * 回放选择时间段内的轨迹
 */
public class VehicleTrackActivity extends BaseActivity<VehicleTrackPresenter> implements VehicleTrackView{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.mapview)
    MapView mMapView;
    @BindView(R.id.tv_riqi)
    TextView tvRiqi;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;

    String snCode;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_vehicle_track;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.vehicle_track);
        snCode = getIntent().getStringExtra(SNCODE);
        tvRiqi.setText(mPresenter.getTime1(Calendar.getInstance().getTime()));
        tvStartTime.setText(mPresenter.getTime2(Calendar.getInstance().getTime()));
        tvEndTime.setText(mPresenter.getTime2(Calendar.getInstance().getTime()));
    }

    @Override
    protected void updateViews() {

    }

    @OnClick({R.id.tv_riqi, R.id.layout_start_time, R.id.layout_end_time, R.id.tv_find})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_riqi://日期
                mPresenter.selectTime(TimePickerView.Type.YEAR_MONTH_DAY, 1, tvRiqi);
                break;
            case R.id.layout_start_time://开始时间
                mPresenter.selectTime(TimePickerView.Type.HOURS_MINS, 2, tvStartTime);
                break;
            case R.id.layout_end_time://结束时间
                mPresenter.selectTime(TimePickerView.Type.HOURS_MINS, 3, tvEndTime);
                break;
            case R.id.tv_find://查找
                mPresenter.findVehicleTrack(snCode);
                break;
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public MapView getMapView() {
        return mMapView;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mPresenter.destroy();
        mMapView.onDestroy();
        mMapView = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }
}
