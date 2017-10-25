package com.mywaytec.myway.ui.gprs.vehicleTrack;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.bigkoo.pickerview.TimePickerView;
import com.mywaytec.myway.APP;
import com.mywaytec.myway.DaoSession;
import com.mywaytec.myway.MyTrackDao;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.bean.VehicleTrackBean;
import com.mywaytec.myway.model.db.MyTrack;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.ui.peopleNearby.PeopleNearbyPresenter;
import com.mywaytec.myway.utils.Gps;
import com.mywaytec.myway.utils.PositionUtil;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CommonSubscriber;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/3/8.
 */

public class VehicleTrackPresenter extends RxPresenter<VehicleTrackView> {

    RetrofitHelper retrofitHelper;
    Context mContext;
    private BaiduMap mBaiduMap;
    private LocationClient locClient;// 定位客户端
    private double latitude, longitude; // 经纬度
    private boolean isFirstLoc = true;// 记录是否第一次定位
    private BitmapDescriptor bitmap;

    private List<List<LatLng>> huabanche = new ArrayList<>();
    private List<LatLng> latlngs;// 车辆经纬度集合
    // 通过设置间隔时间和距离可以控制速度和图标移动的距离
    private static final int TIME_INTERVAL = 80;
    private static final double DISTANCE = 0.00009;
    private Handler mHandler;
    Polyline mPolyline;
    private boolean stopThread;
    private Date riqiDate;
    private Date startDate;
    private Date endDate;

    @Inject
    public VehicleTrackPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(VehicleTrackView view) {
        super.attachView(view);
        mContext = mView.getContext();
        mBaiduMap = mView.getMapView().getMap();
        mHandler = new Handler(Looper.getMainLooper());
        initMap();
//        initData();// 经纬度数据源,测试用
        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.huodongyueban_faqihuodong_jihedidian);
    }

    public void destroy(){
        //停止轨迹回放
        stopThread = true;
    }

    private void initMap() {
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap = mView.getMapView().getMap();

        // 不显示地图上比例尺
        mView.getMapView().showScaleControl(false);

        // 不显示地图缩放控件（按钮控制栏）
        mView.getMapView().showZoomControls(false);

        // 普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        mBaiduMap.setMapStatus(msu);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位服务的客户端
        locClient = new LocationClient(mContext);
        // 注册监听函数
        locClient.registerLocationListener(locListener);
        // 配置LocationClient这个定位客户端
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
//        option.setCoorType("bd09ll");// 设置坐标类型
        option.setAddrType("all");
        option.setScanSpan(1000);//
        // 配置客户端
        locClient.setLocOption(option);
        // 启动定位
        locClient.start();

        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, null));

    }

    BDLocationListener locListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mView.getMapView() == null)
                return;
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(0)//
                    .direction(location.getDirection())// 方向
                    .latitude(location.getLatitude())//
                    .longitude(location.getLongitude())//
                    .build();
            // 设置定位数据

            latitude = location.getLatitude();
            longitude = location.getLongitude();
            mBaiduMap.setMyLocationData(locData);
            // 第一次定位的时候，那地图中心店显示为定位到的位置
            LatLng cenpt = new LatLng(latitude, longitude);
            Log.e("TAG", latitude + "/" + longitude);
//            OverlayOptions options = new MarkerOptions().position(cenpt).icon(
//                    bitmap);
//            mBaiduMap.addOverlay(options);

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());

                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                        .newMapStatus(builder.build()));

            }
        }

    };

    public void setLatLng(LatLng latLng) {
        OverlayOptions optionss = new MarkerOptions().position(latLng).icon(
                bitmap);
        mBaiduMap.addOverlay(optionss);
    }

    /**
     * 根据经纬度获得车辆位置信息
     *
     * @param lng
     * @param lat
     */
    public void initOverlay(String lng, String lat) {
        LatLng point = new LatLng(Double.parseDouble(lat),
                Double.parseDouble(lng));
        OverlayOptions option = new MarkerOptions().position(point)
                .icon(bitmap).zIndex(5);
        mBaiduMap.addOverlay(option);
    }

    //查找车辆轨迹
    public void findVehicleTrack(String deviceId){
        if (null == riqiDate){
            riqiDate = Calendar.getInstance().getTime();
        }
        if (null == startDate){
            startDate = Calendar.getInstance().getTime();
        }
        if (null == endDate){
            endDate = Calendar.getInstance().getTime();
        }
        if (endDate.getTime() < startDate.getTime()){
            ToastUtils.showToast(R.string.time_is_not_right);
            return;
        }
        String riqi = getTime1(riqiDate);
        String start = getTime3(startDate);
        String end = getTime3(endDate);
        start = riqi+start;
        end = riqi+end;

        retrofitHelper.getVehicleTrackList(deviceId, start, end)
                .compose(RxUtil.<VehicleTrackBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<VehicleTrackBean>(mView, mContext, true) {
                    @Override
                    public void onNext(VehicleTrackBean vehicleTrackBean) {
                        if (vehicleTrackBean.getCode() == 1){
                            initData(vehicleTrackBean);
                        }
                    }
                });
    }

    // 车辆经纬度集合
    public void initData(VehicleTrackBean vehicleTrackBean) {
        huabanche.clear();
        mBaiduMap.clear();
        List<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < vehicleTrackBean.getObj().size(); i++) {
            if (indexList.size() == 0 && i == 0){
                indexList.add(vehicleTrackBean.getObj().get(i).getIndex());
            }
            for (int j = 0; j < indexList.size(); j++) {
                if (vehicleTrackBean.getObj().get(i).getIndex() == indexList.get(j)){
                    break;
                }
                if (j == indexList.size()-1){
                    indexList.add(vehicleTrackBean.getObj().get(i).getIndex());
                }
            }
        }

        for (int i = 0; i < indexList.size(); i++) {
            latlngs = new ArrayList<>();
            for (int j = 0; j < vehicleTrackBean.getObj().size(); j++) {
                if (indexList.get(i) == vehicleTrackBean.getObj().get(j).getIndex()) {
                    VehicleTrackBean.ObjBean.LocBean locBean = vehicleTrackBean.getObj().get(i).getLoc();
                    if (null != locBean) {
                        if (locBean.getCoordinates() != null && locBean.getCoordinates().size() > 1) {
                            double lat = locBean.getCoordinates().get(1);
                            double lng = locBean.getCoordinates().get(0);
                            Gps gps = PositionUtil.gps84_To_Gcj02(lat, lng);
                            latlngs.add(new LatLng(gps.getWgLat(), gps.getWgLon()));

                            if (i == 0 && j == 0){
                                OverlayOptions startOptions = new MarkerOptions().position(new LatLng(gps.getWgLat(), gps.getWgLon())).anchor(0.0f, 1.0f)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.bangdingcheliang_cheliangguiji_qidian));
                                mBaiduMap.addOverlay(startOptions);
                            }else if (j == vehicleTrackBean.getObj().size() - 1 && i == indexList.size()-1){
                                OverlayOptions endOptions = new MarkerOptions().position(new LatLng(gps.getWgLat(), gps.getWgLon())).anchor(0.0f, 1.0f)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.bangdingcheliang_cheliangguiji_zhongdian));
                                mBaiduMap.addOverlay(endOptions);
                            }
                        }
                    }
                }
            }
            huabanche.add(latlngs);
        }

        if (null != latlngs && latlngs.size() > 0) {
            LatLng ll = new LatLng(latlngs.get(0).latitude,
                    latlngs.get(0).longitude);
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(16.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                    .newMapStatus(builder.build()));
        }

        if (huabanche != null & huabanche.size() > 0){
            //绘制所有轨迹
            for (int i = 0; i < huabanche.size(); i++) {
                drawPolyLine(huabanche.get(i));
            }

            //整体回放
            List<LatLng> total = new ArrayList<>();
            for (int i = 0; i <huabanche.size(); i++) {
                total.addAll(huabanche.get(i));
            }
            OverlayOptions markerOptions = new MarkerOptions().flat(true).anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.huabanche))
                    .position(total.get(0))
                    .rotate((float) getAngle(0));
            //mMoveMarker局部变量，一辆小车一个移动箭头
            Marker mMoveMarker = (Marker) mBaiduMap.addOverlay(markerOptions);

            moveLooper(total,mMoveMarker);
        }

    }

    // 车辆经纬度集合
    public void initData() {

        DaoSession daoSession = APP.getInstance().getDaoSession();
        MyTrackDao myTrackDao = daoSession.getMyTrackDao();
        List<MyTrack> list = myTrackDao.queryBuilder()
                .orderDesc(MyTrackDao.Properties.Time).list();

        huabanche = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            latlngs = new ArrayList<>();
            for (int j = 0; j < list.get(i).getTracks().size(); j++) {
                String[] latLng = list.get(i).getTracks().get(j).split("-");
                if (null != latLng & latLng.length == 2) {
                    String latitude = latLng[0];
                    String longitude = latLng[1];
                    latlngs.add(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)));

                    if (i == 0 && j == 0){
                        OverlayOptions startOptions = new MarkerOptions().position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude))).anchor(0.0f, 1.0f)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.bangdingcheliang_cheliangguiji_qidian));
                        mBaiduMap.addOverlay(startOptions);
                    }else if (i == list.size() - 1 && j == list.get(i).getTracks().size()-1){
                        OverlayOptions endOptions = new MarkerOptions().position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude))).anchor(0.0f, 1.0f)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.bangdingcheliang_cheliangguiji_zhongdian));
                        mBaiduMap.addOverlay(endOptions);
                    }
                }
            }
            huabanche.add(latlngs);
        }

        if (null != latlngs && latlngs.size() > 0) {
            LatLng ll = new LatLng(latlngs.get(0).latitude,
                    latlngs.get(0).longitude);
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(16.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                    .newMapStatus(builder.build()));
        }

        if (huabanche != null & huabanche.size() > 0){
            //绘制所有轨迹
            for (int i = 0; i < huabanche.size(); i++) {
                drawPolyLine(huabanche.get(i));
            }

            //整体回放
            List<LatLng> total = new ArrayList<>();
            for (int i = 0; i <huabanche.size(); i++) {
                total.addAll(huabanche.get(i));
            }
            OverlayOptions markerOptions = new MarkerOptions().flat(true).anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.huabanche))
                    .position(total.get(0))
                    .rotate((float) getAngle(0));
            //mMoveMarker局部变量，一辆小车一个移动箭头
            Marker mMoveMarker = (Marker) mBaiduMap.addOverlay(markerOptions);

            moveLooper(total,mMoveMarker);
        }
    }

    /**
     * 根据点获取图标转的角度
     */
    private double getAngle(int startIndex) {
        if ((startIndex + 1) >= mPolyline.getPoints().size()) {
            Log.i("TAG", "------getAngle, index out of bonds");
            throw new RuntimeException("index out of bonds");
        }
        LatLng startPoint = mPolyline.getPoints().get(startIndex);
        LatLng endPoint = mPolyline.getPoints().get(startIndex + 1);
        return getAngle(startPoint, endPoint);
    }


    /**
     * 根据两点算取图标转的角度
     */
    private double getAngle(LatLng fromPoint, LatLng toPoint) {
        double slope = getSlope(fromPoint, toPoint);
        if (slope == Double.MAX_VALUE) {
            if (toPoint.latitude > fromPoint.latitude) {
                return 0;
            } else {
                return 180;
            }
        }
        float deltAngle = 0;
        if ((toPoint.latitude - fromPoint.latitude) * slope < 0) {
            deltAngle = 180;
        }
        double radio = Math.atan(slope);
        double angle = 180 * (radio / Math.PI) + deltAngle - 90;
        return angle;
    }

    /**
     * 根据点和斜率算取截距
     */
    private double getInterception(double slope, LatLng point) {

        double interception = point.latitude - slope * point.longitude;
        return interception;
    }

    /**
     * 算斜率
     */
    private double getSlope(LatLng fromPoint, LatLng toPoint) {
        if (toPoint.longitude == fromPoint.longitude) {
            return Double.MAX_VALUE;
        }
        double slope = ((toPoint.latitude - fromPoint.latitude) / (toPoint.longitude - fromPoint.longitude));
        return slope;

    }

    /**
     * 计算x方向每次移动的距离
     */
    private double getXMoveDistance(double slope) {
        if (slope == Double.MAX_VALUE) {
            return DISTANCE;
        }
        return Math.abs((DISTANCE * slope) / Math.sqrt(1 + slope * slope));
    }

    private void drawPolyLine(List<LatLng> polylines) {

//		polylines.add(polylines.get(0));//将轨迹连成一个完整的环形

        PolylineOptions polylineOptions = new PolylineOptions().points(polylines).width(10).color(Color.parseColor("#ff6c00"));
        mPolyline = (Polyline) mBaiduMap.addOverlay(polylineOptions);

    }

    /**
     * 循环进行移动逻辑
     */
    public void moveLooper(final List<LatLng> polylines,
                           final Marker mMoveMarker) {
        new Thread() {
            public void run() {
                while (true) {
                    for (int i = 0; i < polylines.size() - 1; i++) {
                        final LatLng startPoint = polylines.get(i);
                        final LatLng endPoint = polylines.get(i + 1);
//                        Log.i("TAG", "------mMoveMarker.setPosition(startPoint)");
                        mMoveMarker.setPosition(startPoint);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                // refresh marker's rotate
                                if (mView.getMapView() == null) {
                                    return;
                                }
                                mMoveMarker.setRotate((float) getAngle(
                                        startPoint, endPoint));
                            }
                        });
                        double slope = getSlope(startPoint, endPoint);
                        if (slope == 0){
                            continue;
                        }
                        // 是不是正向的标示
                        boolean isReverse = (startPoint.latitude > endPoint.latitude);

                        double intercept = getInterception(slope, startPoint);

                        double xMoveDistance = isReverse ? getXMoveDistance(slope)
                                : -1 * getXMoveDistance(slope);
                        for (double j = startPoint.latitude; !((j > endPoint.latitude) ^ isReverse); j = j
                                - xMoveDistance) {
                            LatLng latLng = null;
                            if (slope == Double.MAX_VALUE) {
                                latLng = new LatLng(j, startPoint.longitude);
                            } else {
                                latLng = new LatLng(j, (j - intercept) / slope);
                            }

                            final LatLng finalLatLng = latLng;
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (mView.getMapView() == null) {
                                        return;
                                    }
                                    mMoveMarker.setPosition(finalLatLng);// 走完一圈回到起点
                                }
                            });
                            try {
                                Thread.sleep(TIME_INTERVAL);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if (stopThread){
                            return;
                        }
                    }
                }
            }
        }.start();
    }

    //选择时间
    public void selectTime(TimePickerView.Type type, final int tag, final TextView textView){
        //时间选择器
        TimePickerView pvTime = new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                if (tag == 1){
                    textView.setText(getTime1(date));
                    riqiDate = date;
                }else if (tag == 2){
                    textView.setText(getTime2(date));
                    startDate = date;
                }else if (tag == 3){
                    textView.setText(getTime2(date));
                    endDate = date;
                }
            }
        }).setType(type)//默认全部显示
                .isCyclic(false)//是否循环滚动
                .build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }

    public String getTime1(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public String getTime2(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    public String getTime3(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat(" HH:mm:ss");
        return format.format(date);
    }
}
