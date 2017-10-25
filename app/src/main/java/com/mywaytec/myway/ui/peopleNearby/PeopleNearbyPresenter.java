package com.mywaytec.myway.ui.peopleNearby;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.mywaytec.myway.R;
import com.mywaytec.myway.adapter.PeopleNearbyAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.bean.NearbyBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.ui.userDynamic.UserDynamicActivity;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.TimeUtil;
import com.mywaytec.myway.view.CommonSubscriber;
import com.mywaytec.myway.view.LoadingDialog;

import java.text.DecimalFormat;
import java.util.Calendar;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/8/3.
 */

public class PeopleNearbyPresenter extends RxPresenter<PeopleNearbyView> {

    private LoadingDialog loadingDialog;
    RetrofitHelper retrofitHelper;
    Context mContext;
    PeopleNearbyAdapter peopleNearbyAdapter;
    private BaiduMap baiduMap;
    public LocationClient mLocationClient;
    public BDAbstractLocationListener myListener = new MyLocationListener();
    private DecimalFormat df = new DecimalFormat("######0.00");
    private String otherUid;
    private boolean dingwei;

    @Inject
    public PeopleNearbyPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(PeopleNearbyView view) {
        super.attachView(view);
        mContext = mView.getContext();
    }

    public void isDingwei(boolean dingwei){
        this.dingwei = dingwei;
    }

    public void initRecyclerView(){
        loadingDialog = new LoadingDialog(mContext);
        loadingDialog.show();
        peopleNearbyAdapter = new PeopleNearbyAdapter(mContext);
        mView.getRecyclerView().setLayoutManager(new LinearLayoutManager(mContext));
        mView.getRecyclerView().setAdapter(peopleNearbyAdapter);

        initAMap();

    }

    //停止定位
    public void destroyLocationClient(){
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(myListener);
            if (mLocationClient.isStarted()){
                mLocationClient.stop();
            }
        }
    }

    //上传位置信息
    private void nearbyUpload(double latitude, double longitude,
                              String country, String province, String city, String district,
                              String street, String streetnum, String citycode,
                              String adcode, String aoiname){
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.nearbyUpload(uid, token, latitude, longitude, country, province, city,
                district, street, streetnum, citycode, adcode, aoiname)
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseInfo>() {
                    @Override
                    public void onNext(BaseInfo baseInfo) {
                        if (baseInfo.getCode() == 1){
                            nearbyList();
                        }
                    }
                });
    }

    //查看附近的人
    private void nearbyList(){
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.nearbyList(uid, token)
                .compose(RxUtil.<NearbyBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<NearbyBean>() {
                    @Override
                    public void onNext(NearbyBean nearbyBean) {
                        if (nearbyBean.getCode() == 1) {
                            peopleNearbyAdapter.setDataList(nearbyBean.getObj());
                            addp(nearbyBean);
                            if (null != loadingDialog){
                                loadingDialog.dismiss();
                            }
                        }
                    }
                });
    }


    private Marker lastMarker;
    //在地图上画出附近人的锚点
    private void addp(final NearbyBean nearbyBean){
        //构建Marker图标
        final BitmapDescriptor bitmap1 = BitmapDescriptorFactory
                .fromResource(R.mipmap.cebianlan_fujinderen_ditu_datouzhen);
        final BitmapDescriptor bitmap2 = BitmapDescriptorFactory
                .fromResource(R.mipmap.cebianlan_fujinderen_ditu_datouzhen_selected);
        for (int i = 0; i < nearbyBean.getObj().size(); i++) {
            double lat = nearbyBean.getObj().get(i).getLocation().getLatitude();
            double lng = nearbyBean.getObj().get(i).getLocation().getLongitude();
            //定义Maker坐标点
            LatLng point = new LatLng(lat, lng);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap1)
                    .zIndex(18);
            //在地图上添加Marker，并显示
            baiduMap.addOverlay(option);
        }

        //添加marker点击事件的监听
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (null != lastMarker){
                    lastMarker.setIcon(bitmap1);
                }
                lastMarker = marker;
                marker.setIcon(bitmap2);
                LatLng ll = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                MapStatus.Builder builder = new MapStatus.Builder();
                //设置缩放中心点；缩放比例；
                builder.target(ll);
                //给地图设置状态
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                mView.getInfoLayout().setVisibility(View.VISIBLE);
                for (int i = 0; i < nearbyBean.getObj().size(); i++) {
                    if (marker.getPosition().latitude == nearbyBean.getObj().get(i).getLocation().getLatitude()
                        && marker.getPosition().longitude == nearbyBean.getObj().get(i).getLocation().getLongitude()){
                        otherUid = nearbyBean.getObj().get(i).getUid();
                        mView.getNameTV().setText(nearbyBean.getObj().get(i).getUser().getNickname());
                        mView.getDistanceTV().setText(df.format(nearbyBean.getObj().get(i).getDistance()/1000.0)+" km");
                        mView.getTimeTV().setText(TimeUtil.getTimeDelay(nearbyBean.getObj().get(i).getCreatTime()));
                        mView.getAgeTV().setText(getAge(nearbyBean.getObj().get(i).getUser().getBirthday())+"");

                        if (nearbyBean.getObj().get(i).getUser().isGender()) {
                            mView.getGenderLayout().setBackgroundResource(R.mipmap.cebianlan_fujinren_nan_beijing_icon);
                            mView.getGenderImg().setImageResource(R.mipmap.cebianlan_fujinren_nan_icon);
                            Glide.with(mContext).load(nearbyBean.getObj().get(i).getUser().getImgeUrl())
                                    .error(R.mipmap.touxiang_boy_nor)
                                    .centerCrop()
                                    .into(mView.getHeadPortraitImg());
                        }else {
                            mView.getGenderLayout().setBackgroundResource(R.mipmap.cebianlan_fujinren_nv_beijing_icon);
                            mView.getGenderImg().setImageResource(R.mipmap.cebianlan_fujinren_nv_icon);
                            Glide.with(mContext).load(nearbyBean.getObj().get(i).getUser().getImgeUrl())
                                    .error(R.mipmap.touxiang_girl_nor)
                                    .centerCrop()
                                    .into(mView.getHeadPortraitImg());
                        }
                    }else {
                    }
                }
                return true;
            }
        });
    }

    //初始化地图
    public void initAMap(){
        baiduMap = mView.getMapview().getMap();
        //普通地图
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);
//        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
//        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
//                .fromResource(R.mipmap.ic_launcher);
//        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null);
//        baiduMap.setMyLocationConfiguration(config);

        //声明LocationClient类
        mLocationClient = new LocationClient(mContext);
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);

        initLocation();
    }

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
    //定位监听回调
    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(0)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            baiduMap.setMyLocationData(locData);

            if (isFirstLoc) {
                isFirstLoc = false;
                if (null != location) {
                    LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                    MapStatus.Builder builder = new MapStatus.Builder();
                    //设置缩放中心点；缩放比例；
                    builder.target(ll).zoom(18.0f);
                    //给地图设置状态
                    baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                    nearbyUpload(location.getLatitude(), location.getLongitude(), location.getCountry(),
                            location.getProvince(), location.getCity(), location.getDistrict(),
                            location.getStreet(), location.getStreetNumber(), location.getCityCode(),
                            "", "");
                }
            }

            if (dingwei){
                dingwei = false;
                if (null != location) {
                    LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                    MapStatus.Builder builder = new MapStatus.Builder();
                    //设置缩放中心点；缩放比例；
                    builder.target(ll).zoom(18.0f);
                    //给地图设置状态
                    baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }
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

            if (location.getLocType() == BDLocation.TypeGpsLocation){

                //当前为GPS定位结果，可获取以下信息
                location.getSpeed();    //获取当前速度，单位：公里每小时
                location.getSatelliteNumber();    //获取当前卫星数
                location.getAltitude();    //获取海拔高度信息，单位米
                location.getDirection();    //获取方向信息，单位度

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){

                //当前为网络定位结果，可获取以下信息
                location.getOperators();    //获取运营商信息

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                //当前为网络定位结果
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                //当前网络定位失败
                //可将定位唯一ID、IMEI、定位失败时间反馈至loc-bugs@baidu.com
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                //当前网络不通
                if (null != loadingDialog){
                    loadingDialog.dismiss();
                }
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                //当前缺少定位依据，可能是用户没有授权，建议弹出提示框让用户开启权限
                //可进一步参考onLocDiagnosticMessage中的错误返回码
            }
        }

        /**
         * 回调定位诊断信息，开发者可以根据相关信息解决定位遇到的一些问题
         * 自动回调，相同的diagnosticType只会回调一次
         *
         * @param locType           当前定位类型
         * @param diagnosticType    诊断类型（1~9）
         * @param diagnosticMessage 具体的诊断信息释义
         */
        public void onLocDiagnosticMessage(int locType, int diagnosticType, String diagnosticMessage) {
            if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_BETTER_OPEN_GPS) {

                //建议打开GPS

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_BETTER_OPEN_WIFI) {

                //建议打开wifi，不必连接，这样有助于提高网络定位精度！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_LOC_PERMISSION) {

                //定位权限受限，建议提示用户授予APP定位权限！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_NET) {

                //网络异常造成定位失败，建议用户确认网络状态是否异常！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CLOSE_FLYMODE) {

                //手机飞行模式造成定位失败，建议用户关闭飞行模式后再重试定位！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_INSERT_SIMCARD_OR_OPEN_WIFI) {

                //无法获取任何定位依据，建议用户打开wifi或者插入sim卡重试！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_OPEN_PHONE_LOC_SWITCH) {

                //无法获取有效定位依据，建议用户打开手机设置里的定位开关后重试！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_SERVER_FAIL) {

                //百度定位服务端定位失败
                //建议反馈location.getLocationID()和大体定位时间到loc-bugs@baidu.com

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_FAIL_UNKNOWN) {

                //无法获取有效定位依据，但无法确定具体原因
                //建议检查是否有安全软件屏蔽相关定位权限
                //或调用LocationClient.restart()重新启动后重试！
            }
        }

    }

    private int getAge(String birth){
        if (null != birth){
            String[] births = birth.split("-");
            if (null != births && births.length > 0) {
                int birthYear = Integer.parseInt(births[0]);
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
//                Log.i("TAG", "------birthYear,"+birthYear+",,currentYear"+currentYear);
                return currentYear - birthYear;
            }
        }
        return 0;
    }

    //查看用户动态
    public void interOtherDynamic(){
        if (null != otherUid) {
            Intent intent = new Intent(mContext, UserDynamicActivity.class);
            intent.putExtra("otherUid", otherUid);
            mContext.startActivity(intent);
        }
    }
}
