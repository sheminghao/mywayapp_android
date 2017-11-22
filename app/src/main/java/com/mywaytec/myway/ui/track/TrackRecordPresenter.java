package com.mywaytec.myway.ui.track;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.mywaytec.myway.APP;
import com.mywaytec.myway.DaoSession;
import com.mywaytec.myway.MyTrackDao;
import com.mywaytec.myway.R;
import com.mywaytec.myway.TrackDao;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.db.MyTrack;
import com.mywaytec.myway.model.db.Track;
import com.mywaytec.myway.ui.trackResult.TrackResultActivity;
import com.mywaytec.myway.utils.ConversionUtil;
import com.mywaytec.myway.utils.TimeUtil;
import com.mywaytec.myway.utils.ToastUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import static com.baidu.location.BDLocation.GPS_ACCURACY_BAD;
import static com.baidu.location.BDLocation.GPS_ACCURACY_GOOD;
import static com.baidu.location.BDLocation.GPS_ACCURACY_MID;
import static com.baidu.location.BDLocation.GPS_ACCURACY_UNKNOWN;

/**
 * Created by shemh on 2017/3/8.
 */

public class TrackRecordPresenter extends RxPresenter<TrackRecordView> {

    private Context mContext;
    private BaiduMap baiduMap;
    public LocationClient mLocationClient;
    public BDAbstractLocationListener myListener = new MyLocationListener();
    //手动定位
    private boolean dingwei;
    //定位次数
    private int dingweiCount;
    private TrackDao trackDao;
    private DecimalFormat df = new DecimalFormat("######0.00");
    Overlay trackOverlay;

    @Inject
    public TrackRecordPresenter() {
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(TrackRecordView view) {
        super.attachView(view);
        mContext = mView.getContext();
    }

    public void isDingwei(boolean dingwei){
        this.dingwei = dingwei;
    }

    //初始化地图
    public void initAMap(){
        baiduMap = mView.getMapView().getMap();
        //普通地图
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);
//        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
//        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
//                .fromResource(R.mipmap.ic_launcher);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null);
        baiduMap.setMyLocationConfiguration(config);

        //声明LocationClient类
        mLocationClient = new LocationClient(mContext);
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);

        //初始化删除上次记录的轨迹点
        DaoSession daoSession = APP.getInstance().getDaoSession();
        trackDao = daoSession.getTrackDao();
        trackDao.deleteAll();
        
        initLocation();
    }


    //定位基本设置
    private void initLocation(){

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

//        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        option.setScanSpan(1000);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

//        option.setIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        option.setWifiCacheTimeOut(5*60*1000);
        //可选，7.2版本新增能力，如果您设置了这个接口，首次启动定位时，会先判断当前WiFi是否超出有效期，超出有效期的话，会先重新扫描WiFi，然后再定位

        mLocationClient.setLocOption(option);

        //开始定位
        mLocationClient.start();
    }

    //是否第一次定位
    private boolean isFirstLoc = true;
    Marker mLocationMarker;
    //定位监听回调
    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            dingweiCount++;
            //GPS信号强度
            int gpsStatus = location.getGpsAccuracyStatus();
            if (GPS_ACCURACY_GOOD == gpsStatus) {//GPS 质量判断好
                mView.getGpsSignalTV().setText(R.string.strong);
                mView.getGpsSignalImg().setImageResource(R.mipmap.jiluguiji_icon_c_gps);
            } else if (GPS_ACCURACY_MID == gpsStatus) {//GPS 质量判断中等
                mView.getGpsSignalImg().setImageResource(R.mipmap.jiluguiji_icon_b_gps);
            } else if (GPS_ACCURACY_BAD == gpsStatus) {//GPS 质量判断差
                mView.getGpsSignalTV().setText(R.string.weak);
                mView.getGpsSignalImg().setImageResource(R.mipmap.jiluguiji_icon_a_gps);
            } else if (GPS_ACCURACY_UNKNOWN == gpsStatus) {//GPS 质量判断未知
                mView.getGpsSignalTV().setText(R.string.no_signal);
                mView.getGpsSignalImg().setImageResource(R.mipmap.jiluguiji_icon_gps);
            }

            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(0)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            baiduMap.setMyLocationData(locData);

            if (null != location) {
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());

                if (isFirstLoc) {//第一次定位时
                    isFirstLoc = false;
                    if (null != location) {
                        MapStatus.Builder builder = new MapStatus.Builder();
                        //设置缩放中心点；缩放比例；
                        builder.target(ll).zoom(18.0f);
                        //给地图设置状态
                        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                    }
                }

                if (dingwei) {//手动定位
                    dingwei = false;
                    if (null != location) {
                        MapStatus.Builder builder = new MapStatus.Builder();
                        //设置缩放中心点；缩放比例；
                        builder.target(ll).zoom(18.0f);
                        //给地图设置状态
                        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                    }
                }

                if (isStart && !isStop && dingweiCount % 2 == 0) {
                    drawTrack(location);
                }

                //获取定位结果
                location.getTime();    //获取定位时间
                location.getLocationID();    //获取定位唯一ID，v7.2版本新增，用于排查定位问题
                location.getLocType();    //获取定位类型
                location.getLatitude();    //获取纬度信息
                location.getLongitude();    //获取经度信息
                location.getRadius();    //获取定位精准度
                location.getAddrStr();    //获取地址信息
                location.getCountry();    //获取国家信息
                location.getCountryCode();    //获取国家码
                location.getCity();    //获取城市信息
                location.getCityCode();    //获取城市码
                location.getDistrict();    //获取区县信息
                location.getStreet();    //获取街道信息
                location.getStreetNumber();    //获取街道码
                location.getLocationDescribe();    //获取当前位置描述信息
                location.getPoiList();    //获取当前位置周边POI信息

                location.getBuildingID();    //室内精准定位下，获取楼宇ID
                location.getBuildingName();    //室内精准定位下，获取楼宇名称
                location.getFloor();    //室内精准定位下，获取当前位置所处的楼层信息

                if (location.getLocType() == BDLocation.TypeGpsLocation) {

                    //当前为GPS定位结果，可获取以下信息
                    location.getSpeed();    //获取当前速度，单位：公里每小时
                    location.getSatelliteNumber();    //获取当前卫星数
                    location.getAltitude();    //获取海拔高度信息，单位米
                    location.getDirection();    //获取方向信息，单位度

                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {

                    //当前为网络定位结果，可获取以下信息
                    location.getOperators();    //获取运营商信息

                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                    //当前为网络定位结果
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    //当前网络定位失败
                    //可将定位唯一ID、IMEI、定位失败时间反馈至loc-bugs@baidu.com
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    //当前网络不通
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    //当前缺少定位依据，可能是用户没有授权，建议弹出提示框让用户开启权限
                    //可进一步参考onLocDiagnosticMessage中的错误返回码
                }
            }
        }
    }

    //是否开始记录
    private boolean isStart;
    //是否停止记录
    private boolean isStop;
    //轨迹线设置方法
    PolylineOptions polylineOptions;
    //上一次的位置
    private LatLng oldlatLng;
    //测试用
    private double flag = 0;
    //总里程
    private float total = 0;
    //总时间
    private int time = 0;

    // 轨迹点
    List<LatLng> points = new ArrayList<>();
    /**
     * 画路径轨迹
     */
    private void drawTrack(BDLocation location){
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        float speed = location.getSpeed();
        //测试用
//        latitude += flag;
//        longitude += flag * new Random().nextDouble();

//        Log.i("TAG", "----"+latitude+"-"+longitude);
        LatLng latLng = new LatLng(latitude, longitude);
        if (null != oldlatLng) {
            double distance = DistanceUtil.getDistance(oldlatLng, latLng);
            if (distance > 8) {//如果两点间的距离大于8m，就记录
                points.add(latLng);
                if (null != trackOverlay) {
                    trackOverlay.remove();
                }
                polylineOptions = new PolylineOptions().width(10)
                        .color(mContext.getResources().getColor(R.color.trackColor)).points(points);
                trackOverlay = baiduMap.addOverlay(polylineOptions);

                total += distance;//总里程等于每次定位的两点之间距离加起来
                mView.getLichengTV().setText(ConversionUtil.getKM(total) + "");
                mView.getSpeedTV().setText(df.format(speed) + "");
                oldlatLng = latLng;
                trackDao.insert(new Track(null, latitude, longitude, speed));
            }
        }else {
            oldlatLng = latLng;
        }
        //测试用
//        flag = flag+0.0001;

        mView.getSpeedTV().setText(df.format(speed)+"");
    }

    /**
     * 开始记录轨迹
     */
    public void start(){
        isStart = true;
        keepTime();
    }

    /**
     * 停止记录轨迹
     */
    public void stop(){
        isStop = true;
        timer.cancel();
        timer = null;
    }

    private Timer timer;

    /**
     * 计时
     */
    public void keepTime(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                time++;
                mView.getTimeTV().post(new Runnable() {
                    @Override
                    public void run() {
                        mView.getTimeTV().setText(ConversionUtil.getHour(time));
                    }
                });
            }
        }, 1000, 1000);
    }


    PopupWindow popupWindow;
    /**
     * 停止记录弹框
     */
    public void stopPop(View v, final Context mContext){
        //防止重复按按钮
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        //设置PopupWindow的View
        final View view = LayoutInflater.from(mContext).inflate(R.layout.popup_stop_record, null);
        TextView tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              stop();
              DaoSession daoSession = APP.getInstance().getDaoSession();
              TrackDao trackDao = daoSession.getTrackDao();
              List<Track> list = trackDao.queryBuilder().list();
              if (null != list && list.size()>0) {
                  String newTime = TimeUtil.getYMDHMSTime();
                  List<String> tracks = new ArrayList<>();
                  for (int i = 0; i < list.size(); i++) {
                      tracks.add(list.get(i).getLatitude()+"-"+list.get(i).getLongitude());
                  }
                  MyTrackDao myTrackDao = daoSession.getMyTrackDao();
                  MyTrack myTrack = new MyTrack(null, "", newTime, time+"", total+"", tracks);
                  myTrackDao.insert(myTrack);
                  Long trackkey = myTrackDao.getKey(myTrack);

                  Intent intent = new Intent(mContext, TrackResultActivity.class);
                  intent.putExtra("total", total);
                  intent.putExtra("duration", time);
                  intent.putExtra("trackkey", trackkey);
                  mContext.startActivity(intent);
              }else{
                   ToastUtils.showToast(R.string.没有记录到轨迹);
              }
              ((TrackRecordActivity)mContext).finish();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    mView.getViewBackground().setVisibility(View.GONE);
                }
            }
        });
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置背景,这个没什么效果，不添加会报错
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击弹窗外隐藏自身
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置动画
        popupWindow.setAnimationStyle(R.style.PopupWindow);
        //设置位置
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        mView.getViewBackground().setVisibility(View.VISIBLE);
    }

    //设置屏幕背景透明效果
    public void setBackgroundAlpha(Context mContext, float alpha) {
        WindowManager.LayoutParams lp = ((Activity)mContext).getWindow().getAttributes();
        lp.alpha = alpha;
        ((Activity)mContext).getWindow().setAttributes(lp);
    }

    public void onDestroy(){
        if (null != mLocationClient){
            mLocationClient.stop();
        }
    }

}
