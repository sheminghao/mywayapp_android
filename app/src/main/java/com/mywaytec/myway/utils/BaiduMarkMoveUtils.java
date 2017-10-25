package com.mywaytec.myway.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shemh on 2017/8/24.
 */

public class BaiduMarkMoveUtils {

        //起始经度
        private static double sl0 ;// = 125.30227000000002;
        //起始纬度
        private static double   slt;//= 43.88362;
        //结束点经度
        private static double   el0;//= 129.40227000000002;
        //结束点纬度
        private static double elt;// = 49.68362;
        //决定小数点后取几位的参数
        private static double param ;//= 1000;
        private BaiduMap mBaiduMap;
        private Polyline mPolyline;
        private Marker mMoveMarker;
        //移动处理的 Handler
        private Handler mHandler;
        private MapView mMapView;
        //把起始点和结束点等分成十份的点的容器
        private static List<LatLng> latlngs;
        //每移动一次的时间间隔(通过设置间隔时间和距离可以控制速度和图标移动的距离)
        private double time_interval = 8;
        private Context context;

        private final String ACTION_NAME = "发送广播";



        /**
         *
         * @param sl0
         * @param slt
         * @param param
         * @param mMapView
         */
        public BaiduMarkMoveUtils(double sl0,double slt,double param,MapView mMapView,Marker marker,Context context
        ){
            this.sl0 = sl0;
            this.slt = slt;
            this.param = param;
            this.mMapView = mMapView;
            this.mBaiduMap = mMapView.getMap();
            this.mMoveMarker = marker;
            this.context = context;
            mHandler = new Handler(Looper.getMainLooper());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(new LatLng(slt,sl0));
            builder.zoom(11.0f);
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }


        /**
         * 获取等分的十个点的集合
         * @param sl0
         * @param slt
         * @param el0
         * @param elt
         */
        private static void getLatlngs(double sl0, double slt, double el0, double elt){
            Log.e("CCC",el0+":"+elt);
            double a = elt - slt;
            double b = el0 - sl0;
            double c = a/param;
            double d = b/param;
            latlngs = new ArrayList<>();
            for(int i = 0;i < param;i++){
                double lat = slt + c * i;
                double lon = sl0 + d * i;
                latlngs.add(new LatLng(lat,lon) );
            }
        }

        /**
         * 根据点画出线
         */
        public void drawPolyLine() {
            List<LatLng> polylines = new ArrayList<>();
            for (int index = 0; index < latlngs.size(); index++) {
                polylines.add(latlngs.get(index));
            }
            polylines.add(latlngs.get(0));
            PolylineOptions polylineOptions = new PolylineOptions().points(polylines).width(10).color(Color.RED);
            mPolyline = (Polyline) mBaiduMap.addOverlay(polylineOptions);
        }

        /**
         * 根据点获取图标转的角度
         */
        private double getAngle(int startIndex) {
            if ((startIndex + 1) >= mPolyline.getPoints().size()) {
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
        private double getDistance(){
            double distance = DistanceUtil.getDistance(latlngs.get(0), latlngs.get((int) param -1));
            double v =  distance /10000;
            double  theDistance = v * (time_interval/1000);
            return  theDistance;
        }

        /**
         * 循环进行移动逻辑
         */
        public void moveLooper() {
            new Thread() {

                public void run() {

//                while (true) {
                    Log.e("TAG", "start time:"+getCurrentTime());
                    for (int i = 0; i < latlngs.size() - 1; i++) {
                        final LatLng startPoint = latlngs.get(i);
                        final LatLng endPoint = latlngs.get(i+1);
                        mMoveMarker
                                .setPosition(startPoint);

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                // refresh marker's rotate
                                if (mMapView == null) {
                                    return;
                                }
                                mMoveMarker.setRotate((float) getAngle(startPoint,
                                        endPoint));
                            }
                        });
                        double slope = getSlope(startPoint, endPoint);
                        // 是不是正向的标示
                        boolean isReverse = (startPoint.latitude > endPoint.latitude);
                        double intercept = getInterception(slope, startPoint);
                        double xMoveDistance = isReverse ? getDistance() :
                                -1 * getDistance();
                        for (double j = startPoint.latitude; !((j > endPoint.latitude) ^ isReverse);
                             j = j - xMoveDistance) {
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
                                    if (mMapView == null) {
                                        return;
                                    }
                                    mMoveMarker.setPosition(finalLatLng);
                                }
                            });
                            try {
                                Thread.sleep((int)time_interval);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    Log.e("TAG", "end time:"+getCurrentTime());
                    sl0 = el0;
                    slt = elt;
                    Intent mIntent = new Intent(ACTION_NAME);
                    mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
                    //发送广播
                    context.sendBroadcast(mIntent);
//                }
                }
            }.start();
        }

        public static void start(LatLng latLng){
            el0 = latLng.longitude;
            elt =latLng.latitude;
            getLatlngs(sl0,slt,el0,elt);
        }
        private String getCurrentTime(){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String str = formatter.format(curDate);
            return str;
        }

//    private Marker createMarker(LatLng latLng){
//        OverlayOptions markerOptions = new MarkerOptions().flat(true).anchor(0.5f, 0.5f)
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow)).position(latLng)
//                .rotate(0);
//        return (Marker) mBaiduMap.addOverlay(markerOptions);
//    }

}
