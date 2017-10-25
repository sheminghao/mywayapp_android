package com.mywaytec.myway.ui.gprs;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.base.Constant;
import com.mywaytec.myway.base.MqttService;
import com.mywaytec.myway.ui.gprs.electronicFence.ElectronicFenceActivity;
import com.mywaytec.myway.ui.gprs.vehicleLocation.VehicleLocationActivity;
import com.mywaytec.myway.ui.gprs.vehicleTrack.VehicleTrackActivity;
import com.mywaytec.myway.view.LoadingDialog;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import butterknife.BindView;
import butterknife.OnClick;

public class GPRSActivity extends BaseActivity implements View.OnClickListener {

    public static final String SNCODE = "SNCODE";
    public static final String VEHICLE_NAME = "VEHICLE_NAME";

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_yuancheng_jiesuo)
    LinearLayout layoutYuanchengJiesuo;
    @BindView(R.id.img_yuancheng_jiesuo)
    ImageView imgYuanchengJiesuo;
    @BindView(R.id.tv_yuancheng_jiesuo)
    TextView tvYuanchengJiesuo;

    String sncode;
    String clientId;
    String vehicleName;
    MqttService mMqttService;
    LoadingDialog loadingDialog;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_gprs;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initViews() {
        tvTitle.setText("GPS/GPRS");
        sncode = getIntent().getStringExtra(SNCODE);
        clientId = "APP_" + sncode;//发送消息CliendId
        vehicleName = getIntent().getStringExtra(VEHICLE_NAME);

    }

    @Override
    protected void updateViews() {

    }

    @OnClick({R.id.layout_dianzi_weilan, R.id.layout_cheliang_dingwei, R.id.layout_cheliang_guiji})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.layout_dianzi_weilan://电子围栏
                Intent intent = new Intent(GPRSActivity.this, ElectronicFenceActivity.class);
                intent.putExtra(SNCODE, sncode);
                startActivity(intent);
                tvYuanchengJiesuo.setTextColor(Color.parseColor("#DADADA"));
                layoutYuanchengJiesuo.setOnClickListener(null);
                break;
            case R.id.layout_cheliang_dingwei://车辆定位
                Intent intent2 = new Intent(GPRSActivity.this, VehicleLocationActivity.class);
                intent2.putExtra(SNCODE, sncode);
                intent2.putExtra(VEHICLE_NAME, vehicleName);
                startActivity(intent2);
                tvYuanchengJiesuo.setTextColor(Color.parseColor("#DADADA"));
                layoutYuanchengJiesuo.setOnClickListener(null);
                break;
            case R.id.layout_cheliang_guiji://车辆轨迹
                Intent intent3 = new Intent(GPRSActivity.this, VehicleTrackActivity.class);
                intent3.putExtra(SNCODE, sncode);
                startActivity(intent3);
                tvYuanchengJiesuo.setTextColor(Color.parseColor("#DADADA"));
                layoutYuanchengJiesuo.setOnClickListener(null);
                break;
            case R.id.layout_yuancheng_jiesuo://远程解锁
                loadingDialog = new LoadingDialog(this);
                loadingDialog.show();
                if(getResources().getString(R.string.remote_unlock).equals(tvYuanchengJiesuo.getText().toString())){
                    //解锁指令
                    byte[] unLockState = {0x00, 0x02};
                    sendMqttInfo("device/app_request/status/"+sncode, unLockState, 1, clientId);
                }else if (getResources().getString(R.string.remote_lock).equals(tvYuanchengJiesuo.getText().toString())){
                    //锁车指令
                    byte[] lockState = {0x00, 0x01};
                    sendMqttInfo("device/app_request/status/"+sncode, lockState, 1, clientId);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent mqttServiceIntent = new Intent(this, MqttService.class);
        bindService(mqttServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        registerReceiver(mMqttUpdateReceiver, makeMqttUpdateIntentFilter());
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
            // 获取服务的操作对象
            mMqttService = ((MqttService.MqttBinder)service).getService();

            //返回车辆解锁状态topic
            String topic = "device/car_report/status/"+sncode;
            mMqttService.connect(new String[]{topic}, "", Constant.MQTT.MQTT_BROKER, "Car_"+sncode,
                    new int[]{1});

            new Thread(new Runnable() {
                @Override
                public void run() {
                    //查询锁车状态
                    byte[] demandLockState = {0x02, 0x00};
                    sendMqttInfo("device/app_request/status/" + sncode, demandLockState, 1, clientId);
                }
            }).start();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 连接断开
        }
    };

    private BroadcastReceiver mMqttUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (MqttService.ACTION_MQTT.equals(action)) {//获取手机解锁状态
                byte[] data = intent.getByteArrayExtra(MqttService.EXTRA_DATA);
                if (data != null && data.length > 3){
                    Log.i("TAG", "------device/car_report/status/,data[0],"+data[0]);
                    Log.i("TAG", "------device/car_report/status/,data[1],"+data[1]);
                    Log.i("TAG", "------device/car_report/status/,data[2],"+data[2]);
                    Log.i("TAG", "------device/car_report/status/,data[3],"+data[3]);
                    if (data[3] == 2) {
                        if (null != loadingDialog) {
                            loadingDialog.dismiss();
                        }
                        tvYuanchengJiesuo.setTextColor(Color.BLACK);
                        layoutYuanchengJiesuo.setOnClickListener(GPRSActivity.this);
                        if (data[2] == 1) {
                            Log.i("TAG", "------车辆为锁车状态");
                            imgYuanchengJiesuo.setImageResource(R.mipmap.icon_yuanchenjiesuo);
                            tvYuanchengJiesuo.setText(R.string.remote_unlock);
                        } else if (data[2] == 2) {
                            Log.i("TAG", "------车辆为解锁状态");
                            imgYuanchengJiesuo.setImageResource(R.mipmap.icon_yuanchengsuoche);
                            tvYuanchengJiesuo.setText(R.string.remote_lock);
                        }
                    }
                }
            }
        }
    };

    /**
     * @param apptopic 消息的类型
     * @param content 消息内容
     * @param qos 消息发送的模式   选择消息发送的次数，依据不同的使用环境使用不同的模式
     * @param clientId 客户端的唯一标识
     */
    public void sendMqttInfo(final String apptopic, final byte[] content, int qos, String clientId) {
        //消息缓存的方式  内存缓存
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            //创建MQTT客户端
            MqttClient sampleClient = new MqttClient(Constant.MQTT.MQTT_BROKER, clientId, persistence);
            //消息的配置参数
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(Constant.MQTT.MQTT_ACCOUNT);
            connOpts.setPassword(Constant.MQTT.MQTT_PASSWORD.toCharArray());
            //不记忆上一次会话
            connOpts.setCleanSession(true);
            // 设置超时时间 单位为秒
//            connOpts.setConnectionTimeout(5);
            //是否自动重新连接
//            connOpts.setAutomaticReconnect(true);
            //链接服务器
            sampleClient.connect(connOpts);
            sampleClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                    Log.i("TAG","------GPRSActivity, connectionLost失去连接,"+throwable.getLocalizedMessage());
                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    byte[] data = mqttMessage.getPayload();
                    if (data != null && data.length > 3){
                        if (data[3] == 2) {
                            tvYuanchengJiesuo.setTextColor(Color.BLACK);
                            layoutYuanchengJiesuo.setOnClickListener(GPRSActivity.this);
                            if (data[2] == 1) {
                                Log.i("TAG", "------车辆为锁车状态");
                                imgYuanchengJiesuo.setImageResource(R.mipmap.icon_yuanchenjiesuo);
                                tvYuanchengJiesuo.setText("远程解锁");
                            } else if (data[2] == 2) {
                                Log.i("TAG", "------车辆为解锁状态");
                                imgYuanchengJiesuo.setImageResource(R.mipmap.icon_yuanchengsuoche);
                                tvYuanchengJiesuo.setText("远程锁车");
                            }
                        }
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    Log.i("TAG", "-----deliveryComplete-->>" + iMqttDeliveryToken.isComplete());
                    if (!iMqttDeliveryToken.isComplete()){//发送失败
//                        if (("device/app_request/status/" + sncode).equals(topic)) {//车辆是否锁车
//                            byte[] demandLockState = {0x02, 0x00};
//                            sendMqttInfo("device/app_request/status/" + sncode, demandLockState, 2, sncode);
//                        }else if (("device/car_report/status/"+sncode).equals(topic)){//解锁/锁车
//                        }
                    }else {//发送成功
                         if (("device/app_request/status/"+sncode).equals(apptopic)){//解锁/锁车
                            if (null != content && content.length > 1){
                                if (content[1] == 0x01){//锁车指令
                                    Log.i("TAG", "------锁车指令发送成功");
                                }else {//解锁指令
                                    Log.i("TAG", "------解锁指令发送成功");
                                }
                            }
                        }
                    }
                }
            });
            //创建消息
            MqttMessage message = new MqttMessage(content);
            //给消息设置发送的模式
            message.setQos(qos);
            //发布消息到服务器
            sampleClient.publish(apptopic, message);
            //断开链接
            sampleClient.disconnect();
        } catch (MqttException me) {
            me.printStackTrace();
            if (null != loadingDialog) {
                loadingDialog.dismiss();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mMqttUpdateReceiver);
        unbindService(mServiceConnection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("TAG", "------GPRSActivity, onDestroy()");
        if (mMqttService != null){
            mMqttService.onDestroy();
        }
    }
}
