package com.mywaytec.myway.ui.location;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.mywaytec.myway.R;
import com.mywaytec.myway.adapter.LocationAdapter;
import com.mywaytec.myway.base.RxPresenter;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/7/8.
 */

public class LocationPresenter extends RxPresenter<LocationView>  {

    private Context mContext;
    private LRecyclerView lRecyclerView;
    private LocationAdapter locationAdapter;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private BaiduMap baiduMap;
    private LatLng currentLatLng;
    private String currentCity;
    LocationClient mLocationClient;
    BDAbstractLocationListener myListener = new MyLocationListener();
    PoiSearch mPoiSearch;
    GeoCoder mSearch;

    @Inject
    public LocationPresenter() {
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(LocationView view) {
        super.attachView(view);
        mContext = mView.getContext();
        lRecyclerView = mView.getRecyclerView();
    }

    public void init(){
        initAMap();
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
        locationAdapter = new LocationAdapter(mContext);

        //创建地理编码检索监听者
        mSearch = GeoCoder.newInstance();
        //设置地理编码检索监听者
        mSearch.setOnGetGeoCodeResultListener(geoCoderResultListener);

        lRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        lRecyclerViewAdapter = new LRecyclerViewAdapter(locationAdapter);
        lRecyclerView.setAdapter(lRecyclerViewAdapter);
        lRecyclerView.setPullRefreshEnabled(false);
        lRecyclerView.setLoadMoreEnabled(false);
        lRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.i("TAG", "=====isCanLoad"+isCanLoad+"start"+start);
                if (isCanLoad) {
                    start++;
                    loadPoi(currentLatLng);
                } else {
                    //the end
                    lRecyclerView.setNoMore(true);
                }
            }
        });
        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("address", locationAdapter.getDataList().get(position));
                ((Activity)mContext).setResult(Activity.RESULT_OK, intent);
                ((Activity)mContext).finish();
            }
        });
    }

    //创建地理编码检索监听者
    OnGetGeoCoderResultListener geoCoderResultListener = new OnGetGeoCoderResultListener() {
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有检索到结果
            }
            //获取地理编码结果
            Log.i("TAG", "------onGetGeoCodeResult"+result.getAddress());
            if (null != result.getLocation()) {
                LatLng ll = new LatLng(result.getLocation().latitude, result.getLocation().longitude);
                currentLatLng = ll;
                MapStatus.Builder builder = new MapStatus.Builder();
                //设置缩放中心点；缩放比例；
                builder.target(ll).zoom(18.0f);
                //给地图设置状态
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }
        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            mView.hideLoading();//隐藏缓冲条
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果
            }
            //获取反向地理编码结果
            locationAdapter.setDataList(result.getPoiList());
        }
    };

    //回收资源
    public void onDestroy(){
        if (null != mLocationClient)
            mLocationClient.stop();
    }

    /**
     * 是否还能继续加载
     */
    private boolean isCanLoad=true;
    private int start = 1;
    private int limit = 20;
    /**
     * 加载附近poi
     */
    private void loadPoi(LatLng latLng){
//        mPoiSearch.searchNearby((new PoiNearbySearchOption())
//                .location(latLng)
//                .keyword("")
//                .radius(1000)
//                .pageNum(start)
//                .pageCapacity(limit));
        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(latLng));
    }

    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener(){
        public void onGetPoiResult(com.baidu.mapapi.search.poi.PoiResult result){
            //获取POI检索结果
            if (result.error != SearchResult.ERRORNO.NO_ERROR) {
                //详情检索失败
                // result.error请参考SearchResult.ERRORNO
                Log.i("TAG", "-------result.getAllPoi(),详情检索失败");
                lRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                    @Override
                    public void reload() {
                        loadPoi(currentLatLng);
                    }
                });
            } else {
                lRecyclerView.refreshComplete(start);
                //检索成功
                if (result.getAllPoi().size() < limit){
                    isCanLoad = false;
                }
                if (start > 1){
                    locationAdapter.addAll(result.getAllPoi());
                }else {
                    locationAdapter.setDataList(result.getAllPoi());
                }
            }
        }
        public void onGetPoiDetailResult(PoiDetailResult result){
            //获取Place详情页检索结果
            Log.i("TAG", "-------onGetPoiDetailResult");
        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
            Log.i("TAG", "-------onGetPoiIndoorResult");
        }
    };


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

        baiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus arg0) {
                LatLng ptCenter = baiduMap.getMapStatus().target; //获取地图中心点坐标
                // 反Geo搜索
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(ptCenter));
            }

            @Override
            public void onMapStatusChange(MapStatus arg0) {

            }
        });

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
                if (null != location) {
                    LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                    currentLatLng = ll;
                    currentCity = location.getCity();
                    MapStatus.Builder builder = new MapStatus.Builder();
                    //设置缩放中心点；缩放比例；
                    builder.target(ll).zoom(18.0f);
                    //给地图设置状态
                    baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                    start = 1;
                    loadPoi(ll);
                }
            }
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
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                //当前缺少定位依据，可能是用户没有授权，建议弹出提示框让用户开启权限
                //可进一步参考onLocDiagnosticMessage中的错误返回码
            }
        }
    }
}