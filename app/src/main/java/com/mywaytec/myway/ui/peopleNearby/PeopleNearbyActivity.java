package com.mywaytec.myway.ui.peopleNearby;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class PeopleNearbyActivity extends BaseActivity<PeopleNearbyPresenter> implements PeopleNearbyView{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.mapview)
    MapView mMapView;
    @BindView(R.id.layout_map)
    LinearLayout layoutMap;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.img_head_portrait)
    ImageView imgHeadPortrait;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.layout_gender)
    RelativeLayout layoutGender;
    @BindView(R.id.img_gender)
    ImageView imgGender;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.layout_info)
    LinearLayout layoutInfo;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_people_nearby;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText("附近的人");
        mPresenter.initRecyclerView();

    }

    @Override
    protected void updateViews() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public MapView getMapview() {
        return mMapView;
    }

    @Override
    public ImageView getHeadPortraitImg() {
        return imgHeadPortrait;
    }

    @Override
    public TextView getNameTV() {
        return tvName;
    }

    @Override
    public TextView getDistanceTV() {
        return tvDistance;
    }

    @Override
    public RelativeLayout getGenderLayout() {
        return layoutGender;
    }

    @Override
    public ImageView getGenderImg() {
        return imgGender;
    }

    @Override
    public TextView getAgeTV() {
        return tvAge;
    }

    @Override
    public TextView getTimeTV() {
        return tvTime;
    }

    @Override
    public LinearLayout getInfoLayout() {
        return layoutInfo;
    }

    @OnClick({R.id.img_right, R.id.layout_info, R.id.img_ditu_dingwei})
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.img_right:
                if(layoutMap.getVisibility() == View.VISIBLE){
                    imgRight.setImageResource(R.mipmap.cebianlan_fujinren_ditu_icon);
                    recyclerView.setVisibility(View.VISIBLE);
                    layoutMap.setVisibility(View.GONE);
                }else {
                    imgRight.setImageResource(R.mipmap.cebianlan_fujinderen_liebiao_icon);
                    recyclerView.setVisibility(View.GONE);
                    layoutMap.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.layout_info://用户信息
                mPresenter.interOtherDynamic();
                break;
            case R.id.img_ditu_dingwei://地图定位
                mPresenter.isDingwei(true);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mPresenter.destroyLocationClient();
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
