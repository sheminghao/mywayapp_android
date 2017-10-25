package com.mywaytec.myway.ui.wayDetail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bumptech.glide.Glide;
import com.mywaytec.myway.APP;
import com.mywaytec.myway.R;
import com.mywaytec.myway.adapter.WayDetailImgAdapter;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.model.bean.RouteDetailBean;
import com.mywaytec.myway.model.bean.RouteListBean;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CircleImageView;
import com.mywaytec.myway.view.CommonSubscriber;
import com.mywaytec.myway.view.CustomLinearLayoutManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class WayDetailActivity extends BaseActivity<WayDetailPresenter> implements WayDetailView, BaiduMap.OnMapTouchListener {

    public static final String ROUTE_DATA = "routeData";

    @BindView(R.id.img_top)
    ImageView imgTop;
    @BindView(R.id.img_head_portrait)
    CircleImageView imgHeadPortrait;
    @BindView(R.id.tv_route_name)
    TextView tvRouteName;
    @BindView(R.id.tv_xunhang)
    TextView tvXunhang;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.tv_scenery_star)
    TextView tvSceneryStar;
    @BindView(R.id.tv_intro)
    TextView tvIntro;
    @BindView(R.id.tv_origin)
    TextView tvOrigin;
    @BindView(R.id.tv_originBus)
    TextView tvOriginBus;
    @BindView(R.id.tv_destination)
    TextView tvDestination;
    @BindView(R.id.tv_destination_bus)
    TextView tvDestinationBus;
    @BindView(R.id.mapview)
    MapView mMapView;
    @BindView(R.id.content_way_detail)
    RelativeLayout contentWayDetail;
    @BindView(R.id.scrollView)
    ScrollView mScrollView;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.img_like)
    ImageView imgLike;
    @BindView(R.id.tv_zonglicheng)
    TextView tvZonglicheng;
    @BindView(R.id.layout_way_detail)
    LinearLayout layoutWayDetail;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_degree_of_difficulty)
    TextView tvDegreeOfDifficulty;

    private BaiduMap baiduMap;
    private RouteListBean.ObjBean routeData;
    private DecimalFormat df = new DecimalFormat("######0.00");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_way_detail;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        routeData = (RouteListBean.ObjBean) getIntent().getExtras().get(ROUTE_DATA);

//        tvNickname.setText(routeData.getUser().getNickname());
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        final int routeId = routeData.getId();
        //获取路线详情
        mPresenter.getRetrofitHelper().getRouteDetail(uid, token, routeId)
                .compose(RxUtil.<RouteDetailBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<RouteDetailBean>(this, this, true) {
                    @Override
                    public void onNext(RouteDetailBean routeDetailBean) {
                        if (routeDetailBean.getCode() == 1){
                            Log.i("TAG", "-----轨迹点"+routeDetailBean.getObj().getPaths().size());
                            initView(routeDetailBean.getObj());
                        }else{
                            ToastUtils.showToast(routeDetailBean.getMsg());
                        }
                    }
                });
    }

    private void initView(RouteListBean.ObjBean routeDetail){
        APP.loadImg(routeDetail.getImage(), imgTop);
        if (routeDetail.isLike()){
            imgLike.setImageResource(R.mipmap.luxianxiangqing_dianzan_select);
        }else{
//            btnLike.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4398F5")));
            imgLike.setImageResource(R.mipmap.luxianxiangqing_dianzang);
        }
        if (routeDetail.getUser().isGender()){
            Glide.with(this).load(routeDetail.getUser().getImgeUrl())
                    .error(R.mipmap.touxiang_boy_nor)
                    .into(imgHeadPortrait);
        }else {
            Glide.with(this).load(routeDetail.getUser().getImgeUrl())
                    .error(R.mipmap.touxiang_girl_nor)
                    .into(imgHeadPortrait);
        }
        tvZonglicheng.setText(df.format(routeDetail.getLegend()/1000.0));
        tvRouteName.setText(routeDetail.getName());
        tvXunhang.setText(routeDetail.getEnduranceClaim()+"");
        tvSceneryStar.setText(routeDetail.getSceneryStar()+"");
        tvDegreeOfDifficulty.setText(routeDetail.getDifficultyStar()+"");
//        ratingBar.setProgress(routeDetail.getSceneryStar());
        Log.i("TAG", "------SceneryStar,"+routeDetail.getSceneryStar());
        ratingBar.setRating(routeDetail.getSceneryStar());
        tvIntro.setText(routeDetail.getIntro());
        tvOrigin.setText(routeDetail.getOrigin());
        tvOriginBus.setText(routeDetail.getOriginBus());
        tvDestination.setText(routeDetail.getDestination());
        tvDestinationBus.setText(routeDetail.getDestinationBus());

        List<String> photoList = routeDetail.getPhotos();

        wayDetailImgAdapter = new WayDetailImgAdapter(this);
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(this));
        recyclerView.setAdapter(wayDetailImgAdapter);
        wayDetailImgAdapter.setDataList(photoList);

        initAMap(routeDetail.getPaths());
    }

    WayDetailImgAdapter wayDetailImgAdapter;

    @Override
    protected void updateViews() {

    }

    @OnClick({R.id.img_back, R.id.img_like})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.img_like:
                mPresenter.like(routeData.getId()+"");
                break;
        }
    }

    private LatLng pointstart;
    private LatLng pointend;
    private LatLngBounds bounds;
    public void initAMap(List<RouteListBean.ObjBean.PathsBean> pathList) {
        baiduMap = mMapView.getMap();
        baiduMap.setOnMapTouchListener(this);

        List<LatLng> latLngList = new ArrayList<>();
        for (int i = 0; i < pathList.size(); i++) {
            latLngList.add(new LatLng(pathList.get(i).getLatitude(), pathList.get(i).getLongitude()));
        }
        baiduMap.addOverlay(new PolylineOptions().width(10)
                .color(Color.parseColor("#ff6c00")).points(latLngList));

        if (pathList != null && 0 != pathList.size()) {
            //获得起点和终点坐标
            pointstart = new LatLng(pathList.get(0).getLatitude(), pathList.get(0).getLongitude());
            pointend = new LatLng(pathList.get(pathList.size() - 1).getLatitude(),
                    pathList.get(pathList.size() - 1).getLongitude());

            OverlayOptions startOptions = new MarkerOptions().position(pointstart).anchor(0.0f, 1.0f)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.bangdingcheliang_cheliangguiji_qidian));
            OverlayOptions endOptions = new MarkerOptions().position(pointend).anchor(0.0f, 1.0f)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.bangdingcheliang_cheliangguiji_zhongdian));
            baiduMap.addOverlay(startOptions);
            baiduMap.addOverlay(endOptions);
        }
        // 设置所有maker显示在View中
        if (null != pathList && 0 != pathList.size()) {
            double midlat = (pointstart.latitude + pointend.latitude) / 2;
            double midlon = (pointstart.longitude + pointend.longitude) / 2;
            LatLng point = new LatLng(midlat, midlon);// 中点

            //地图缩放等级
            int zoomLevel[] = { 2000000, 1000000, 500000, 200000, 100000, 50000,
                    25000, 20000, 10000, 5000, 2000, 1000, 500, 100, 50, 20, 0 };
            // 计算两点之间的距离，重新设定缩放值，让全部marker显示在屏幕中。
            int jl = (int) DistanceUtil.getDistance(pointstart, pointend);

            int i;
            for (i = 0; i < 17; i++) {
                if (zoomLevel[i] < jl) {
                    break;
                }
            }
            //根据起点和终点的坐标点计算出距离来对比缩放等级获取最佳的缩放值，用来得到最佳的显示折线图的缩放等级
            float zoom = i + 4;
            // 设置当前位置显示在地图中心
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(point, zoom);// 设置缩放比例
            baiduMap.animateMapStatus(u);

            baiduMap.getUiSettings().setZoomGesturesEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
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

    @Override
    public void onTouch(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                mScrollView.requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mScrollView.requestDisallowInterceptTouchEvent(true);
                break;
        }
    }

    @Override
    public ImageView getLikeImg() {
        return imgLike;
    }
}
