package com.mywaytec.myway.ui.track;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.ui.mytrack.MyTrackActivity;
import com.mywaytec.myway.utils.AppUtils;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.SlideUp;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 记录轨迹页面
 */
public class TrackRecordActivity extends BaseActivity<TrackRecordPresenter> implements TrackRecordView, View.OnClickListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.mapview)
    MapView mMapView;
    @BindView(R.id.tv_licheng)
    TextView tvLicheng;
    @BindView(R.id.tv_speed)
    TextView tvSpeed;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.slideview)
    LinearLayout slideview;
    @BindView(R.id.img_open_menu)
    ImageView imgOpenMenu;
    @BindView(R.id.img_open_map)
    ImageView imgOpenMap;
    @BindView(R.id.tv_gps_signal)
    TextView tvGpsSignal;
    @BindView(R.id.img_gps_signal)
    ImageView imgGpsSignal;
    @BindView(R.id.layout_track_record)
    LinearLayout layoutTrackRecord;
    @BindView(R.id.view_background)
    View viewBackground;
    @BindView(R.id.img_back)
    ImageView imgBack;

    SlideUp slideUp;

    private boolean isRecord;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_track_record;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        layoutTrackRecord.setPadding(0, AppUtils.getStatusBar(), 0, 0);
        init();
        imgOpenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideUp.hide();
            }
        });

        slideUp = new SlideUp.Builder(slideview)
                .withStartGravity(Gravity.TOP)
                .withLoggingEnabled(true)
                .withStartState(SlideUp.State.SHOWED)
                .build();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecord) {
                    ToastUtils.showToast(getResources().getString(R.string.please_firstly_end_the_track_record));
                }else {
                    finish();
                }
            }
        });
    }

    @Override
    protected void updateViews() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText(R.string.record_track);
    }

    private void init(){
        tvStart.setOnClickListener(this);
        mPresenter.initAMap();
        setFouts();
    }

    //设置字体
    private void setFouts(){
        AssetManager mgr=getAssets();//得到AssetManager
        Typeface tf=Typeface.createFromAsset(mgr, "fonts/Swis721_Cn_BT.ttf");//根据路径得到Typeface
        tvSpeed.setTypeface(tf);//设置字体
        tvTime.setTypeface(tf);
        tvLicheng.setTypeface(tf);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        mPresenter.onDestroy();
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

    @OnClick({R.id.img_open_menu, R.id.img_my_profile, R.id.img_ditu_dingwei})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_start://开始or结束
                if (getResources().getString(R.string.start).equals(tvStart.getText().toString())){
                    isRecord = true;
                    tvStart.setText(R.string.end);
                    tvStart.setBackgroundResource(R.mipmap.jiluguiji_btn_jieshu);
                    mPresenter.start();
                }else if(getResources().getString(R.string.end).equals(tvStart.getText().toString())){
                    mPresenter.stopPop(tvStart, TrackRecordActivity.this);
                }
                break;
            case R.id.img_open_menu:
                slideUp.show();
                break;
            case R.id.img_ditu_dingwei://手动定位
                mPresenter.isDingwei(true);
                break;
            case R.id.img_my_profile://我的轨迹
//                DialogUtils.confirmDialog(this, "正在录制轨迹，是否退出录制？",
//                        new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                startActivity(new Intent(TrackRecordActivity.this, MyTrackActivity.class));
//                            }
//                        });
                if (isRecord) {
                    ToastUtils.showToast(getResources().getString(R.string.please_firstly_end_the_track_record));
                }else {
                    startActivity(new Intent(TrackRecordActivity.this, MyTrackActivity.class));
                }
                break;
        }
    }

    @Override
    public MapView getMapView() {
        return mMapView;
    }

    @Override
    public TextView getLichengTV() {
        return tvLicheng;
    }

    @Override
    public TextView getSpeedTV() {
        return tvSpeed;
    }

    @Override
    public TextView getTimeTV() {
        return tvTime;
    }

    @Override
    public TextView getGpsSignalTV() {
        return tvGpsSignal;
    }

    @Override
    public ImageView getGpsSignalImg() {
        return imgGpsSignal;
    }

    @Override
    public View getViewBackground() {
        return viewBackground;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if (isRecord) {
                ToastUtils.showToast(getResources().getString(R.string.please_firstly_end_the_track_record));
            }else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
