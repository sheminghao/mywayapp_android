package com.mywaytec.myway.ui.trackResult;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bumptech.glide.Glide;
import com.mywaytec.myway.APP;
import com.mywaytec.myway.DaoSession;
import com.mywaytec.myway.MyTrackDao;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.model.db.MyTrack;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.view.CircleImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 生成轨迹预览(完善路线信息)
 */
public class TrackResultActivity extends BaseActivity<TrackResultPresenter> implements TrackResultView,
        BaiduMap.OnMapTouchListener{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.mapview)
    MapView mMapView;
    @BindView(R.id.img_head_portrait)
    CircleImageView imgHeadPortrait;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.img_cover)
    ImageView imgCover;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.et_route_name)
    EditText etRouteName;
    @BindView(R.id.ratingBar_fengjing)
    RatingBar fengjingRatingBar;
    @BindView(R.id.ratingBar_nandu)
    RatingBar nanduRatingBar;
    @BindView(R.id.et_xuhangyaoqiu)
    EditText etXuhangyaoqiu;
    @BindView(R.id.et_luxianjianjie)
    EditText etLuxianjianjie;
    @BindView(R.id.et_qishididian)
    EditText etQishididian;
    @BindView(R.id.et_qishigongjiao)
    EditText etQishigongjiao;
    @BindView(R.id.et_jieshudidian)
    EditText etJieshudidian;
    @BindView(R.id.et_jieshugongjiao)
    EditText etJieshugongjiao;
    @BindView(R.id.tv_total_distance)
    TextView tvTotalDistance;
    @BindView(R.id.content_track_result)
    ScrollView mScrollView;

    private BaiduMap baiduMap;
    private float total;
    private Long trackkey;
    private int saveRouteId;
    private int duration;
    private DecimalFormat df = new DecimalFormat("######0.00");

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_track_result;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.track_profile);

        saveRouteId = getIntent().getIntExtra("save_route_id", 0);
        total = getIntent().getFloatExtra("total", 0);
        duration = getIntent().getIntExtra("duration", 0);
        trackkey = getIntent().getLongExtra("trackkey", 0);
        tvTotalDistance.setText(df.format(total/1000)+"");
        if(saveRouteId == 0) {
            tvRight.setText(R.string.save);
            initAMap();
        }else {
            etRouteName.setText(getIntent().getStringExtra("save_route_name"));
        }

        if (PreferencesUtils.getLoginInfo().getObj().getGender()){
            Glide.with(this).load(PreferencesUtils.getLoginInfo().getObj().getImgeUrl())
                    .error(R.mipmap.touxiang_boy_nor)
                    .into(imgHeadPortrait);
        }else{
            Glide.with(this).load(PreferencesUtils.getLoginInfo().getObj().getImgeUrl())
                    .error(R.mipmap.touxiang_girl_nor)
                    .into(imgHeadPortrait);
        }
        tvNickname.setText(PreferencesUtils.getLoginInfo().getObj().getNickname());
    }

    @Override
    protected void updateViews() {
    }

    @OnClick({R.id.img_cover, R.id.layout_upload_photo, R.id.tv_submit, R.id.tv_right})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.img_cover://选择封面
                mPresenter.editCover();
                break;
            case R.id.layout_upload_photo://选择照片
                mPresenter.selectPhoto();
                break;
            case R.id.tv_submit://提交
                String routename = etRouteName.getText().toString().trim();//路线名称
                String title = "";//标题
                int scenery_star = (int) fengjingRatingBar.getRating();//风景评分
                int difficulty_star = (int) nanduRatingBar.getRating();//难度评分
                int legend = (int) total;//里程（单位：m）
                int endurance_claim = 0;//续航要求
                if (etXuhangyaoqiu.getText().toString().trim().length() > 0) {
                    endurance_claim = Integer.parseInt(etXuhangyaoqiu.getText().toString().trim());
                }
                String intro = etLuxianjianjie.getText().toString().trim();//简介
                String origin = etQishididian.getText().toString().trim();//起点
                String origin_bus = etQishigongjiao.getText().toString().trim();//起点公交站
                String destination = etJieshudidian.getText().toString().trim();//终点
                String destination_bus = etJieshugongjiao.getText().toString().trim();//终点公交站
                String score = "";//综合评分
                if(saveRouteId == 0) {
                    mPresenter.shareRoutePath(routename, title, scenery_star, difficulty_star, legend,
                            endurance_claim, intro, origin, origin_bus, destination, destination_bus, score, latLngList);
                }else {
                    mPresenter.publicRoutePath(routename, title, scenery_star, difficulty_star, legend,
                            endurance_claim, intro, origin, origin_bus, destination, destination_bus, score,saveRouteId);
                }
                break;
            case R.id.tv_right://保存
                int legend2 = (int) total;//里程（单位：m）
                mPresenter.saveRoutePaths(legend2, duration, latLngList);
                break;
        }
    }

    private MyTrackDao myTrackDao;
    private LatLng pointstart;
    private LatLng pointend;
    private LatLngBounds bounds;
    private List<LatLng> latLngList;
    public void initAMap() {
        baiduMap = mMapView.getMap();
        baiduMap.setOnMapTouchListener(this);

        DaoSession daoSession = APP.getInstance().getDaoSession();
        myTrackDao = daoSession.getMyTrackDao();
        MyTrack myTrack = myTrackDao.load(trackkey);
        total = Float.parseFloat(myTrack.getTotalDistance());
        tvTotalDistance.setText(df.format(total / 1000.0) + "");

        latLngList = new ArrayList<>();
        for (int i = 0; i < myTrack.getTracks().size(); i++) {
            String[] latLng = myTrack.getTracks().get(i).split("-");
            if (null != latLng & latLng.length == 2) {
                String latitude = latLng[0];
                String longitude = latLng[1];
                latLngList.add(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)));
            }
        }

        baiduMap.addOverlay(new PolylineOptions().width(10)
                .color(getResources().getColor(R.color.trackColor)).points(latLngList));

        if (latLngList != null && 0 != latLngList.size()) {
            //获得起点和终点坐标
            pointstart = new LatLng(latLngList.get(0).latitude, latLngList.get(0).longitude);
            pointend = new LatLng(latLngList.get(latLngList.size() - 1).latitude,
                    latLngList.get(latLngList.size() - 1).longitude);
            OverlayOptions startOptions = new MarkerOptions().position(pointstart).anchor(0.0f, 1.0f)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.bangdingcheliang_cheliangguiji_qidian));
            OverlayOptions endOptions = new MarkerOptions().position(pointend).anchor(0.0f, 1.0f)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.bangdingcheliang_cheliangguiji_zhongdian));
            baiduMap.addOverlay(startOptions);
            baiduMap.addOverlay(endOptions);
        }
        // 设置所有maker显示在View中
        if (null != latLngList && 0 != latLngList.size()) {
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
    public Context getContext() {
        return this;
    }

    @Override
    public ImageView getCoverImg() {
        return imgCover;
    }

    @Override
    public RecyclerView getPhotoRecycler() {
        return recyclerView;
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
}
