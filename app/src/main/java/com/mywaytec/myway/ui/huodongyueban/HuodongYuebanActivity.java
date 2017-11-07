package com.mywaytec.myway.ui.huodongyueban;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.model.bean.NearbyActivityBean;
import com.mywaytec.myway.ui.fabuhuodong.FabuHuodongActivity;

import butterknife.BindView;
import butterknife.OnClick;

import static com.mywaytec.myway.ui.fabuhuodong.FabuHuodongActivity.FABUHUODONG;
import static com.mywaytec.myway.ui.huodongXiangqing.HuodongXiangqingActivity.ACTIVITY_INFO;

public class HuodongYuebanActivity extends BaseActivity<HuodongYuebanPresenter> implements HuodongYuebanView{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.fujin_recyclerView)
    LRecyclerView fujinRecyclerView;
    @BindView(R.id.wode_recyclerView)
    LRecyclerView wodeRecyclerView;
    @BindView(R.id.tv_fujin_huodong)
    TextView tvFujinHuodong;
    @BindView(R.id.tv_wode_huodong)
    TextView tvWodeHuodong;
    @BindView(R.id.view_fujin_huodong)
    View viewFujinHuodong;
    @BindView(R.id.view_wode_huodong)
    View viewWodeHuodong;

    MapView mapView;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_huodong_yueban;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.activity_and);
        imgRight.setImageResource(R.mipmap.yueban_icon_fabu);
        mapView = new MapView(this);
        mPresenter.initNearby();
    }

    @Override
    protected void updateViews() {

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
    public LRecyclerView getFujinRecyclerView() {
        return fujinRecyclerView;
    }

    @Override
    public LRecyclerView getWodeRecyclerView() {
        return wodeRecyclerView;
    }

    @OnClick({R.id.img_right, R.id.layout_fujin_huodong, R.id.layout_wode_huodong})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.img_right://发布活动
                Intent intent = new Intent(HuodongYuebanActivity.this, FabuHuodongActivity.class);
                startActivityForResult(intent, FABUHUODONG);
                break;
            case R.id.layout_fujin_huodong://附近活动
                tvFujinHuodong.setTextColor(Color.parseColor("#000000"));
                tvWodeHuodong.setTextColor(Color.parseColor("#767676"));
                viewFujinHuodong.setVisibility(View.VISIBLE);
                viewWodeHuodong.setVisibility(View.INVISIBLE);
                fujinRecyclerView.setVisibility(View.VISIBLE);
                wodeRecyclerView.setVisibility(View.GONE);
                mPresenter.initAMap();
                break;
            case R.id.layout_wode_huodong://我的活动
                tvFujinHuodong.setTextColor(Color.parseColor("#767676"));
                tvWodeHuodong.setTextColor(Color.parseColor("#000000"));
                viewFujinHuodong.setVisibility(View.INVISIBLE);
                viewWodeHuodong.setVisibility(View.VISIBLE);
                fujinRecyclerView.setVisibility(View.GONE);
                wodeRecyclerView.setVisibility(View.VISIBLE);
                mPresenter.loadWode();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
        if(null != mPresenter.getLocationClient()){
            mPresenter.getLocationClient().stop();
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FABUHUODONG && resultCode == RESULT_OK){
            mPresenter.initNearby();
        }else if (requestCode == 0x141 && resultCode == RESULT_OK){
            NearbyActivityBean.ObjBean activityInfo = (NearbyActivityBean.ObjBean) data.getSerializableExtra(ACTIVITY_INFO);
            int position = data.getIntExtra("position", -1);

            if (position != -1 && mPresenter.getNearbyActivityAdapter().getDataList().size() > position) {
                NearbyActivityBean.ObjBean objBean = mPresenter.getNearbyActivityAdapter().getDataList().get(position);
                objBean.setCurrentNum(activityInfo.getCurrentNum());
                objBean.setSign(activityInfo.isSign());
                objBean.setParticipant(activityInfo.isParticipant());
            }
        }
    }
}
