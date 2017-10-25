package com.mywaytec.myway.ui.huodongyueban;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.mywaytec.myway.adapter.NearbyActivityAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.bean.NearbyActivityBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.view.CommonSubscriber;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/8/10.
 */

public class HuodongYuebanPresenter extends RxPresenter<HuodongYuebanView> {

    RetrofitHelper retrofitHelper;
    Context mContext;
    NearbyActivityAdapter nearbyActivityAdapter;
    NearbyActivityAdapter wodeActivityAdapter;
    LRecyclerViewAdapter nearbyLRecyclerViewAdapter;
    LRecyclerViewAdapter wodeLRecyclerViewAdapter;
    private BaiduMap baiduMap;
    LocationClient mLocationClient;
    BDAbstractLocationListener myListener = new MyLocationListener();

    @Inject
    public HuodongYuebanPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(HuodongYuebanView view) {
        super.attachView(view);
        mContext = mView.getContext();
    }

    //初始化附近的活动
    public void initNearby(){
        nearbyActivityAdapter = new NearbyActivityAdapter(mContext);
        nearbyLRecyclerViewAdapter = new LRecyclerViewAdapter(nearbyActivityAdapter);
        mView.getFujinRecyclerView().setLayoutManager(new LinearLayoutManager(mContext));
        mView.getFujinRecyclerView().setAdapter(nearbyLRecyclerViewAdapter);
        mView.getFujinRecyclerView().setLoadMoreEnabled(false);
        mView.getFujinRecyclerView().setPullRefreshEnabled(true);
        mView.getFujinRecyclerView().setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                isFirstLoc = true;
            }
        });
        initAMap();

        wodeActivityAdapter = new NearbyActivityAdapter(mContext);
        wodeLRecyclerViewAdapter = new LRecyclerViewAdapter(wodeActivityAdapter);
        mView.getWodeRecyclerView().setLayoutManager(new LinearLayoutManager(mContext));
        mView.getWodeRecyclerView().setAdapter(wodeLRecyclerViewAdapter);
        mView.getWodeRecyclerView().setLoadMoreEnabled(false);
        mView.getWodeRecyclerView().setPullRefreshEnabled(true);
        mView.getWodeRecyclerView().setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWode();
            }
        });
        loadWode();
    }

    //附近活动
    private void loadNearbyData(double latitude, double longitude){
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.nearbyActivity(uid, token, latitude, longitude)
                .compose(RxUtil.<NearbyActivityBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<NearbyActivityBean>() {
                    @Override
                    public void onNext(NearbyActivityBean nearbyActivityBean) {
                        mView.getFujinRecyclerView().refreshComplete(0);
                        if (nearbyActivityBean.getCode() == 1){
                            nearbyActivityAdapter.setDataList(nearbyActivityBean.getObj());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.getFujinRecyclerView().refreshComplete(0);
                    }
                });
    }

    private void loadWode(){
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.myActivity(uid, token)
                .compose(RxUtil.<NearbyActivityBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<NearbyActivityBean>() {
                    @Override
                    public void onNext(NearbyActivityBean nearbyActivityBean) {
                        mView.getWodeRecyclerView().refreshComplete(0);
                        if (nearbyActivityBean.getCode() == 1){
                            wodeActivityAdapter.setDataList(nearbyActivityBean.getObj());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.getWodeRecyclerView().refreshComplete(0);
                    }
                });
    }


    //初始化地图
    public void initAMap(){
        baiduMap = mView.getMapView().getMap();
        //普通地图
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);
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
            if (isFirstLoc) {
                isFirstLoc = false;
                if (null != location)
                loadNearbyData(location.getLatitude(), location.getLongitude());
            }

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
    public LocationClient getLocationClient(){
        return mLocationClient;
    }
}
