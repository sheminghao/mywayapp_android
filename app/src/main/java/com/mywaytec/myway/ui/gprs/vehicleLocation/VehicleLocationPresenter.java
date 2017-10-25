package com.mywaytec.myway.ui.gprs.vehicleLocation;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.mywaytec.myway.R;
import com.mywaytec.myway.adapter.TongzhiAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.bean.FenceWarningBean;
import com.mywaytec.myway.model.bean.VehicleLastLocationBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.ConversionUtil;
import com.mywaytec.myway.utils.Gps;
import com.mywaytec.myway.utils.PositionUtil;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.SystemUtil;
import com.mywaytec.myway.utils.TimeUtil;
import com.mywaytec.myway.view.CommonSubscriber;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/3/8.
 */

public class VehicleLocationPresenter extends RxPresenter<VehicleLocationView> {

    RetrofitHelper retrofitHelper;
    Context mContext;
    private BaiduMap mBaiduMap;
    public BDAbstractLocationListener myListener = new MyLocationListener();// 定位客户端
    public LocationClient mLocationClient;
    private BitmapDescriptor bitmap;
    //车辆位置marker
    Marker mLocationMarker;
    private DecimalFormat df = new DecimalFormat("######0.000000");

    private boolean newLoc;

    @Inject
    public VehicleLocationPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(VehicleLocationView view) {
        super.attachView(view);
        mContext = mView.getContext();
        mBaiduMap = mView.getMapView().getMap();
    }

    public void destroy(){

    }

    public void init(String snCode) {
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap = mView.getMapView().getMap();

        // 普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, null));

        mBaiduMap.setMapStatus(msu);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位服务的客户端
        mLocationClient = new LocationClient(mContext);
        // 注册监听函数
        mLocationClient.registerLocationListener(myListener);
        // 配置LocationClient这个定位客户端
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
//        option.setCoorType("bd09ll");// 设置坐标类型
        option.setAddrType("all");
        option.setScanSpan(1000);//
        // 配置客户端
        mLocationClient.setLocOption(option);
        // 启动定位
        mLocationClient.start();

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (null != marker && marker == mLocationMarker){
                    if (null == mInfoWindow){
                        mInfoWindow = initInfoWindow(new LatLng(marker.getPosition().latitude,
                                marker.getPosition().longitude), speed);
                    }
                    //显示InfoWindow
                    mBaiduMap.showInfoWindow(mInfoWindow);

                }
                return true;
            }
        });

        retrofitHelper.vehicleLastLocation(snCode)
                .compose(RxUtil.<VehicleLastLocationBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<VehicleLastLocationBean>() {
                    @Override
                    public void onNext(VehicleLastLocationBean vehicleLastLocationBean) {
                        if (vehicleLastLocationBean.getCode() == 1){
                            if (!newLoc){
                                List<Double> coordinates = vehicleLastLocationBean.getObj().getLoc().getCoordinates();
                                if (null != coordinates && coordinates.size()>2) {
                                    LatLng latLng = new LatLng(coordinates.get(1), coordinates.get(0));
                                    initInfoWindow(latLng, 0);
                                }
                            }
                        }
                    }
                });

    }

    String vehicleName;
    InfoWindow mInfoWindow;
    private InfoWindow initInfoWindow(LatLng latLng, int speed){
        newLoc = true;
        //创建InfoWindow展示的view
        View view = View.inflate(mContext, R.layout.window_gps_location, null);
        TextView tvClose = (TextView) view.findViewById(R.id.tv_close);
        TextView tvVahicleName = (TextView) view.findViewById(R.id.tv_vahicle_name);
        tvVahicleName.setText(vehicleName);
        TextView tvTime = (TextView) view.findViewById(R.id.tv_time);
        TextView tvLocation = (TextView) view.findViewById(R.id.tv_location);
        TextView tvSpeed = (TextView) view.findViewById(R.id.tv_speed);
        tvTime.setText(TimeUtil.toYMDHMSTime(TimeUtil.getTime()));
        tvLocation.setText(df.format(latLng.latitude)+", "+df.format(latLng.longitude));
        tvSpeed.setText(speed/10.0+"km/h");
        //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
        mInfoWindow = new InfoWindow(view, latLng, -SystemUtil.dp2px(18));
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mBaiduMap)
                    mBaiduMap.hideInfoWindow();
            }
        });
        return mInfoWindow;
    }

    //是否第一次定位
    private boolean isFirstLoc = true;
    //定位监听回调
    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
//                    .accuracy(0)
//                    // 此处设置开发者获取到的方向信息，顺时针0-360
//                    .direction(100)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            if (null != location) {
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                if (isFirstLoc) {//第一次定位时
                    isFirstLoc = false;
//                    MapStatus.Builder builder = new MapStatus.Builder();
//                    //设置缩放中心点；缩放比例；
//                    builder.target(ll).zoom(18.0f);
//                    //给地图设置状态
//                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }
            }
        }
    }

    public void setLatLng(LatLng latLng) {
        OverlayOptions optionss = new MarkerOptions().position(latLng).icon(
                bitmap);
        mBaiduMap.addOverlay(optionss);
    }


    AlertDialog tongzhiDialog;
    TongzhiAdapter tongzhiAdapter;
    public void showTongzhiDialog(final String deviceId, String vehicleName){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogStyle);
        View view = View.inflate(mContext, R.layout.dialog_tongzhi, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        tongzhiAdapter = new TongzhiAdapter(mContext, vehicleName);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(tongzhiAdapter);

        //删除警报
        tongzhiAdapter.setOnDelListener(new TongzhiAdapter.onSwipeListener() {
            @Override
            public void onDel(final int pos) {
                retrofitHelper.deleteVehicleFenceWarning(deviceId, tongzhiAdapter.getDataList().get(pos).getId())
                        .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                        .subscribe(new CommonSubscriber<BaseInfo>() {
                            @Override
                            public void onNext(BaseInfo baseInfo) {
                                if (baseInfo.getCode() == 1){
                                    tongzhiAdapter.remove(pos);
                                    tongzhiAdapter.notifyDataSetChanged();
                                }
                            }
                        });
            }
        });
        //获取围栏警报列表
        retrofitHelper.getVehicleFenceWarningList(deviceId)
                .compose(RxUtil.<FenceWarningBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<FenceWarningBean>() {
                    @Override
                    public void onNext(FenceWarningBean fenceWarningBean) {
                        tongzhiAdapter.setDataList(fenceWarningBean.getObj().getFenceWarnings());

                    }
                });

        ImageView imgClose = (ImageView) view.findViewById(R.id.img_close);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tongzhiDialog.dismiss();
            }
        });
        builder.setView(view);
        tongzhiDialog = builder.show();
        tongzhiDialog.setCanceledOnTouchOutside(false);
        tongzhiDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mView.getCoverView().setVisibility(View.GONE);
            }
        });
        mView.getCoverView().setVisibility(View.VISIBLE);
    }

    //设置屏幕背景透明效果
    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = ((Activity)mContext).getWindow().getAttributes();
        lp.alpha = alpha;
        ((Activity)mContext).getWindow().setAttributes(lp);
    }


    //是否第一次显示车辆信息
    private boolean isFirstUpdateVehicle = true;
    //车辆速度
    int speed;
    //更新车辆信息
    public void updateVehicleInfo(byte[] data, String vehicleName){
        this.vehicleName = vehicleName;
        String info = ConversionUtil.byte2HexStr(data);
        Log.i("TAG", "-----MQTT," + info);
        String[] infos = info.split(" ");
        if(data.length == 32){
            int type = Integer.parseInt(infos[0], 16);
            int status = Integer.parseInt(infos[1], 16);
            speed = Integer.parseInt(infos[2]+infos[3], 16);
            long lng = Long.parseLong(infos[4]+infos[5]+infos[6]+infos[7], 16);
            long lat = Long.parseLong(infos[8]+infos[9]+infos[10]+infos[11], 16);
            long altitude = Long.parseLong(infos[12]+infos[13]+infos[14]+infos[15], 16);
            long frameTime = Long.parseLong(infos[16]+infos[17]+infos[18]+infos[19]
                    +infos[20]+infos[21]+infos[22]+infos[23], 16);
            long index = Long.parseLong(infos[24]+infos[25]+infos[26]+infos[27]
                    +infos[28]+infos[29]+infos[30]+infos[31], 16);
            Log.i("TAG", "-----type," + type +"; status,"+status +"; speed,"+speed +"; lng,"+lng
                    +"; lat,"+lat + "; altitude,"+altitude + "; frameTime,"+frameTime
                    + "; index,"+index);

            double latitude = lat/1000000.0;
            double longitude = lng/1000000.0;
            Gps gps = PositionUtil.gps84_To_Gcj02(latitude, longitude);
            LatLng latLng = new LatLng(gps.getWgLat(), gps.getWgLon());

            if (isFirstUpdateVehicle) {
                isFirstUpdateVehicle = false;
                MarkerOptions markerOptions = new MarkerOptions().flat(true).anchor(0.5f, 1.0f)
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.cheliangdingwei_mark))
                        .position(latLng);
                mLocationMarker = (Marker)mBaiduMap.addOverlay(markerOptions);
            }else {
                mLocationMarker.setPosition(latLng);
            }

            MapStatus.Builder builder = new MapStatus.Builder();
            //设置缩放中心点；缩放比例；
            builder.target(latLng).zoom(16.0f);
            //给地图设置状态
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

            mBaiduMap.hideInfoWindow();
            mInfoWindow = initInfoWindow(latLng, speed);
            //显示InfoWindow
            mBaiduMap.showInfoWindow(mInfoWindow);

        }
    }
}
