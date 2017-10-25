package com.mywaytec.myway.ui.gprs.electronicFence;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.mywaytec.myway.R;
import com.mywaytec.myway.adapter.DianziweilanAdapter;
import com.mywaytec.myway.base.Constant;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.bean.DianziweilanBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.ConversionUtil;
import com.mywaytec.myway.utils.Gps;
import com.mywaytec.myway.utils.PositionUtil;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CommonSubscriber;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static android.content.Context.TELEPHONY_SERVICE;
import static com.mywaytec.myway.base.Constant.MQTT.MQTT_ACCOUNT;
import static com.mywaytec.myway.base.Constant.MQTT.MQTT_PASSWORD;

/**
 * Created by shemh on 2017/3/8.
 */

public class ElectronicFencePresenter extends RxPresenter<ElectronicFenceView> {

    RetrofitHelper retrofitHelper;
    Context mContext;
    private BaiduMap mBaiduMap;
    public BDAbstractLocationListener myListener = new MyLocationListener();// 定位客户端
    public LocationClient mLocationClient;
    private RecyclerView dianziweilanRecyclerView;
    private DianziweilanAdapter dianziweilanAdapter;

    /**
     * -1, 默认，无法选择   0, 圆形   1，自定义
     */
    private int weilanType = -1;

    @Inject
    public ElectronicFencePresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(ElectronicFenceView view) {
        super.attachView(view);
        mContext = mView.getContext();
        mBaiduMap = mView.getMapView().getMap();
        dianziweilanRecyclerView = mView.getDianziweilanRecyclerView();
        init();
    }

    public void initRecyclerView(String snCode){
        dianziweilanAdapter = new DianziweilanAdapter(mContext, retrofitHelper);
        dianziweilanRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        dianziweilanRecyclerView.setAdapter(dianziweilanAdapter);

        retrofitHelper.getVehicleFenceList(snCode)
                .compose(RxUtil.<DianziweilanBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DianziweilanBean>() {
                    @Override
                    public void onNext(DianziweilanBean dianziweilanBean) {
                        if (dianziweilanBean.getCode() == 1){
                            dianziweilanAdapter.setDataList(dianziweilanBean.getObj());
                            dianziweilanAdapter.notifyDataSetChanged();
                        }else {
                            ToastUtils.showToast(dianziweilanBean.getMsg());
                        }
                    }
                });
    }

    //修改名称更新列表
    public void updateName(String newName, int position){
        dianziweilanAdapter.getDataList().get(position).setName(newName);
        dianziweilanAdapter.notifyDataSetChanged();
    }

    //删除围栏更新列表
    public void updateList(int position){
        dianziweilanAdapter.getDataList().remove(position);
        dianziweilanAdapter.notifyDataSetChanged();
    }

    //获取电子围栏选择方式
    public int getWeilanType(){
        return weilanType;
    }

    //设置电子围栏选择方式
    public void setWeilanType(int weilanType) {
        if (weilanType == 0){
            if (zidingyiOverlay != null){
                zidingyiOverlay.remove();
            }
            if (zidingyiPoints.size() > 0){
                mBaiduMap.clear();
                zidingyiPoints.clear();
            }
        }else if(weilanType == 1){
            if (yuanOverlay != null){
                yuanOverlay.remove();
            }
            if (yuanMarker != null){
                yuanMarker.remove();
                yuanMarker = null;
            }
        }else if (weilanType == -1){
            mBaiduMap.clear();
        }
        this.weilanType = weilanType;
    }

    //自定义围栏重选
    public void chongxuan(){
        if (zidingyiOverlay != null){
            zidingyiOverlay.remove();
        }
        if (zidingyiPoints.size() > 0){
            mBaiduMap.clear();
            zidingyiPoints.clear();
        }
    }

    //自定义选择围栏撤销
    public void cexiao(){
        if (zidingyiMarkers != null && zidingyiMarkers.size() > 0){
            zidingyiMarkers.get(zidingyiMarkers.size() - 1).remove();
            zidingyiMarkers.remove(zidingyiMarkers.size() - 1);
        }
        if (zidingyiPoints.size()>0){
            zidingyiPoints.remove(zidingyiPoints.size()-1);
        }
        if (zidingyiOverlay != null) {
            zidingyiOverlay.remove();
        }
        if (zidingyiPoints.size()>2) {
            OverlayOptions polygonOption = new PolygonOptions()
                    .points(zidingyiPoints)
                    .stroke(new Stroke(5, 0xAA00FF00))
                    .fillColor(0xAAFFFF00);
            //在地图上添加多边形Option，用于显示
            zidingyiOverlay = mBaiduMap.addOverlay(polygonOption);
        }
    }

    public void destroy(){

    }

    // 构造折线点坐标
    List<LatLng> zidingyiPoints = new ArrayList<>();
    List<Marker> zidingyiMarkers = new ArrayList<>();
    Overlay zidingyiOverlay;
    Marker yuanMarker;
    Overlay yuanOverlay;

    private void init() {
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap = mView.getMapView().getMap();

        mView.getMapView().showZoomControls(false);
        mView.getMapView().showScaleControl(false);

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

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (weilanType == 1) {//自定义
                    setLatLng(latLng);
                    zidingyiPoints.add(latLng);
                    if (zidingyiPoints.size() > 2) {
                        if (zidingyiOverlay != null) {
                            zidingyiOverlay.remove();
                        }
                        OverlayOptions polygonOption = new PolygonOptions()
                                .points(zidingyiPoints)
                                .stroke(new Stroke(5, 0xAA00FF00))
                                .fillColor(0xAAFFFF00);
                        //在地图上添加多边形Option，用于显示
                        zidingyiOverlay = mBaiduMap.addOverlay(polygonOption);
                    }
                }else if (weilanType == 0) {
                    mView.getSelectHintTV().setText(R.string.see_in_the_map);
                    if (yuanMarker == null) {
                        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.dianziweilan_dingwei);
                        OverlayOptions optionss = new MarkerOptions().position(latLng).icon(bitmap);
                        yuanMarker = (Marker) mBaiduMap.addOverlay(optionss);
                    }else {
                        yuanMarker.setPosition(latLng);
                    }

                    if (yuanOverlay != null){
                        yuanOverlay.remove();
                    }

                    CircleOptions circleOptions = new CircleOptions()
                            .center(latLng)
                            .stroke(new Stroke(5, 0xAA00FF00))
                            .fillColor(0xAAFFFF00)
                            .radius(mView.getSeekBar().getProgress()+100);
                    //在地图上添加多边形Option，用于显示
                    yuanOverlay= mBaiduMap.addOverlay(circleOptions);
                }
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    public void setLatLng(LatLng latLng) {
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.dianziweilan_dingwei);
        OverlayOptions optionss = new MarkerOptions().position(latLng).icon(
                bitmap);
        Marker zidingyiMarker = (Marker) mBaiduMap.addOverlay(optionss);
        zidingyiMarkers.add(zidingyiMarker);
    }

    //更新圆形区域
    public void updateYuan(int radius){
        if (yuanMarker == null) {
//            ToastUtils.showToast("请选择中心点");
            return;
        }

        if (yuanOverlay != null){
            yuanOverlay.remove();
        }

        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(yuanMarker.getPosition().latitude, yuanMarker.getPosition().longitude))
                .stroke(new Stroke(5, 0xAA00FF00))
                .fillColor(0xAAFFFF00)
                .radius(radius);
        //在地图上添加多边形Option，用于显示
        yuanOverlay= mBaiduMap.addOverlay(circleOptions);
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
//                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection())
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            if (null != location) {
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                if (isFirstLoc) {//第一次定位时
                    isFirstLoc = false;
                    MapStatus.Builder builder = new MapStatus.Builder();
                    //设置缩放中心点；缩放比例；
                    builder.target(ll).zoom(18.0f);
                    //给地图设置状态
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }
            }
        }
    }


    //添加圆形电子围栏
    public void addYuanDianziweilan(final String snCode, String name) {
        if (yuanMarker == null){
            ToastUtils.showToast(R.string.please_select_a_central_point);
            return;
        }
        Log.i("TAG", "------deviceId,"+snCode);
        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("deviceId", snCode);
        bodyMap.put("name", name);
        Gps gps = PositionUtil.gcj_To_Gps84(yuanMarker.getPosition().latitude, yuanMarker.getPosition().longitude);
        bodyMap.put("locs[" + 0 + "].latitude", gps.getWgLat() + "");
        bodyMap.put("locs[" + 0 + "].longitude", gps.getWgLon() + "");
        int radius = mView.getSeekBar().getProgress()+100;
        retrofitHelper.addVehicleFence(bodyMap, weilanType, radius)
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseInfo>() {
                    @Override
                    public void onNext(BaseInfo baseInfo) {
                        if (baseInfo.getCode() == 1){
                            initRecyclerView(snCode);
                            mView.getSelectDianziweilanLayout().setVisibility(View.GONE);
                            mBaiduMap.clear();
                            weilanType = -1;
                        }else {
                            ToastUtils.showToast(baseInfo.getMsg());
                        }
                    }
                });
    }

    //添加自定义电子围栏
    public void addZidingyiDianziweilan(final String snCode, String name) {
        if (zidingyiPoints.size() < 3){
            ToastUtils.showToast(R.string.please_select_3_or_more_points);
            return;
        }
        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("deviceId", snCode);
        bodyMap.put("name", name);
        for (int i = 0; i < zidingyiPoints.size(); i++) {
            Gps gps = PositionUtil.gcj_To_Gps84(zidingyiPoints.get(i).latitude, zidingyiPoints.get(i).longitude);
            bodyMap.put("locs[" + i + "].latitude", gps.getWgLat() + "");
            bodyMap.put("locs[" + i + "].longitude", gps.getWgLon() + "");
        }
        int radius = 0;//自定义电子围栏没有半径
        retrofitHelper.addVehicleFence(bodyMap, weilanType, radius)
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseInfo>() {
                    @Override
                    public void onNext(BaseInfo baseInfo) {
                        Log.i("TAG", "------code"+baseInfo.getCode());
                        Log.i("TAG", "------msg"+baseInfo.getMsg());
                        if (baseInfo.getCode() == 1){
                            initRecyclerView(snCode);
                            mView.getSelectDianziweilanLayout().setVisibility(View.GONE);
                            mBaiduMap.clear();
                            weilanType = -1;
                        }else {
                            ToastUtils.showToast(baseInfo.getMsg());
                        }
                    }
                });
    }

    public RequestBody toRequestBody(String value){
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }

    List<Byte> contentList;
    //发送电子围栏
    public void sendYuanDianziweilan(){
        if (yuanMarker == null){
            ToastUtils.showToast(R.string.please_select_a_central_point);
            return;
        }
        contentList = new ArrayList<>();
        TelephonyManager TelephonyMgr = (TelephonyManager)mContext.getSystemService(TELEPHONY_SERVICE);
        String szImei = TelephonyMgr.getDeviceId();

        //消息的类型
        String topic = "device/settings/fence/000000000000000000000000";

        int cmd = 1;
        byte cmd1 = (byte) cmd;
        contentList.add(cmd1);
        int points = 0;
        byte points1 = (byte) points;
        contentList.add(points1);
        int radius = (mView.getSeekBar().getProgress()+100)/100;
        byte radius2 = (byte) (radius>>8);
        byte radius1 = (byte) radius;
        contentList.add(radius2);
        contentList.add(radius1);

        Gps gps = PositionUtil.gcj_To_Gps84(yuanMarker.getPosition().latitude, yuanMarker.getPosition().longitude);
        int lat = (int) (gps.getWgLat()*1000000);
        int lng = (int) (gps.getWgLon()*1000000);
        byte lng4 = (byte) (lng>>24);
        byte lng3 = (byte) (lng>>16);
        byte lng2 = (byte) (lng>>8);
        byte lng1 = (byte) lng;
        byte lat4 = (byte) (lat>>24);
        byte lat3 = (byte) (lat>>16);
        byte lat2 = (byte) (lat>>8);
        byte lat1 = (byte) lat;
        contentList.add(lng4);
        contentList.add(lng3);
        contentList.add(lng2);
        contentList.add(lng1);
        contentList.add(lat4);
        contentList.add(lat3);
        contentList.add(lat2);
        contentList.add(lat1);


        byte[] content=new byte[contentList.size()];
        for (int i = 0; i < contentList.size(); i++) {
            content[i] = contentList.get(i);
        }

        Log.i("TAG", "------content,"+ ConversionUtil.byte2HexStr(content));

        publishMqtt(topic , content, szImei, 2);
    }

    /**
     * @param topic 消息的类型
     * @param content 消息内容
     * @param clientId 客户端的唯一标识
     * @param qos 消息发送的模式   选择消息发送的次数，依据不同的使用环境使用不同的模式
     */
    public void publishMqtt(String topic, byte[] content, String clientId, int qos){


        //消息缓存的方式  内存缓存
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            //创建MQTT客户端
            MqttClient sampleClient = new MqttClient(Constant.MQTT.MQTT_BROKER, clientId, persistence);
            //消息的配置参数
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(MQTT_ACCOUNT);
            connOpts.setPassword(MQTT_PASSWORD.toCharArray());
            //不记忆上一次会话
            connOpts.setCleanSession(true);
            Log.i("TAG", "------Connecting to broker: "+Constant.MQTT.MQTT_BROKER);
            //链接服务器
            sampleClient.connect(connOpts);
            Log.i("TAG", "------Connected");
            Log.i("TAG", "------Publishing message: "+content);
            //创建消息
            MqttMessage message = new MqttMessage(content);
            //给消息设置发送的模式
            message.setQos(qos);
            //发布消息到服务器
            sampleClient.publish(topic, message);
            Log.i("TAG", "------Message published");
            //断开链接
            sampleClient.disconnect();
            Log.i("TAG", "------Disconnected");
        } catch(MqttException me) {
            Log.i("TAG", "------reason "+me.getReasonCode());
            Log.i("TAG", "------msg "+me.getMessage());
            Log.i("TAG", "------loc "+me.getLocalizedMessage());
            Log.i("TAG", "------cause "+me.getCause());
            Log.i("TAG", "------excep "+me);
            me.printStackTrace();
        }
    }

}
