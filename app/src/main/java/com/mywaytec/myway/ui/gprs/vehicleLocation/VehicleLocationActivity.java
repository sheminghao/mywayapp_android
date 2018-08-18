package com.mywaytec.myway.ui.gprs.vehicleLocation;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.base.Constant;
import com.mywaytec.myway.base.MqttService;

import butterknife.BindView;
import butterknife.OnClick;

import static com.mywaytec.myway.ui.gprs.GPRSActivity.SNCODE;
import static com.mywaytec.myway.ui.gprs.GPRSActivity.VEHICLE_NAME;

/**
 * 车辆定位
 * 显示设备端发送过来的坐标，实时显示车辆位置
 */
public class VehicleLocationActivity extends BaseActivity<VehicleLocationPresenter> implements VehicleLocationView {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.mapview)
    MapView mMapView;
    @BindView(R.id.img_tongzhi)
    ImageView imgTongzhi;
    @BindView(R.id.view_cover)
    View viewCover;

    String snCode;
    String vehicleName;
    String clientId;
    MqttService mMqttService;

    String dingweiTopic;
    String jingbaoTopic;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_vehicle_location;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.vehicle_position);
        snCode = getIntent().getStringExtra(SNCODE);
        clientId = "CarLoc_" + snCode;
        dingweiTopic = "device/car_report/gps/"+snCode;
        jingbaoTopic = "device/car_report/status/"+snCode;
        vehicleName = getIntent().getStringExtra(VEHICLE_NAME);
        mPresenter.init(snCode);

        Intent mqttServiceIntent = new Intent(this, MqttService.class);
        bindService(mqttServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

    }

    private static IntentFilter makeMqttUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MqttService.ACTION_MQTT);
        return intentFilter;
    }

    // 在Activity中，我们通过ServiceConnection接口来取得建立连接与连接意外丢失的回调
    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service){
            // 建立连接
            mMqttService = ((MqttService.MqttBinder)service).getService();

            mMqttService.connect(new String[]{dingweiTopic, jingbaoTopic}, "", Constant.MQTT.MQTT_BROKER, clientId,
                    new int[]{1, 1});
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
                // 连接断开
                if (mMqttService != null){
                    mMqttService.onDestroy();
                    mMqttService = null;
                }
            }
        };

    private final BroadcastReceiver mMqttUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (MqttService.ACTION_MQTT.equals(action)) {
                String topic = intent.getStringExtra("topic");
                byte[] data = intent.getByteArrayExtra(MqttService.EXTRA_DATA);
                if (dingweiTopic.equals(topic)) {
                    try {
                        mPresenter.updateVehicleInfo(data, vehicleName);
                    } catch (Exception e) {
                    }
                }else if (jingbaoTopic.equals(topic)){
                    if (data.length > 3){
                        if(data[3] == 1 && data[0] == 2){
                            imgTongzhi.setImageResource(R.mipmap.cheliangdingwei_tongzhi_xin);
                        }
                    }
                }
            }
        }
    };

    @Override
    protected void updateViews() {

    }

    @OnClick({R.id.img_tongzhi})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.img_tongzhi://消息通知
                mPresenter.showTongzhiDialog(snCode, vehicleName);
                imgTongzhi.setImageResource(R.mipmap.cheliangdingwei_tongzhi);
                break;
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public MapView getMapView() {
        return mMapView;
    }

    @Override
    public View getCoverView() {
        return viewCover;
    }

    @Override
    public String getVehicleName() {
        return vehicleName;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
        mMapView.onDestroy();
        mMapView = null;
        unbindService(mServiceConnection);
        if (mMqttService != null){
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        registerReceiver(mMqttUpdateReceiver, makeMqttUpdateIntentFilter());
    }
    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        unregisterReceiver(mMqttUpdateReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }
}
