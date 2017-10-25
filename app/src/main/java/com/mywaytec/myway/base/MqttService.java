package com.mywaytec.myway.base;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by shemh on 2017/9/6.
 */

public class MqttService extends Service{

    public final static String ACTION_MQTT = "com.mywaytec.mqtt.ACTION_MQTT";
    public final static String EXTRA_DATA = "com.mywaytec.mqtt.EXTRA_DATA";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MqttBinder();
    }

    public class MqttBinder extends Binder {
        public MqttService getService() {
            return MqttService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    MqttClient sampleClient;
    String[] topic;
    /**
     *
     * @param topic 消息的类型
     * @param content 消息内容
     * @param broker 服务器地址
     * @param clientId 客户端的唯一标识
     * @param qos 消息发送的模式   选择消息发送的次数，依据不同的使用环境使用不同的模式
     */
    public void connect(final String[] topic, final String content, final String broker, final String clientId, final int[] qos){
        this.topic = topic;
//       消息缓存的方式  内存缓存
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            //创建MQTT客户端
            sampleClient = new MqttClient(broker, clientId, persistence);
            //消息的配置参数
            final MqttConnectOptions connOpts = new MqttConnectOptions();
            //设置连接的用户名
            connOpts.setUserName(Constant.MQTT.MQTT_ACCOUNT);
            //设置连接的密码
            connOpts.setPassword(Constant.MQTT.MQTT_PASSWORD.toCharArray());
            // 设置超时时间 单位为秒
            connOpts.setConnectionTimeout(10);
            //是否自动重新连接
            connOpts.setAutomaticReconnect(true);
            // 设置会话心跳时间  单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
//            connOpts.setKeepAliveInterval(20);
            //不记忆上一次会话
            connOpts.setCleanSession(true);
            Log.i("TAG", "------MqttService: "+broker);
            sampleClient.setCallback(new MqttCallback() {

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    byte[] data = message.getPayload();
//            Log.i("TAG","-----messageArrived-->>"+new String(data));
                    Log.i("TAG", "------Topic, "+topic);
                    Intent intent = new Intent(ACTION_MQTT);
                    intent.putExtra(EXTRA_DATA, data);
                    intent.putExtra("topic", topic);
                    sendBroadcast(intent);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken arg0) {
                    Log.i("TAG","------deliveryComplete-->>"+arg0.isComplete());
                }

                @Override
                public void connectionLost(Throwable arg0) {
                    // 失去连接
                    Log.i("TAG","------connectionLost失去连接,"+arg0.getLocalizedMessage());
                }
            });
            //链接服务器
            sampleClient.connect(connOpts);
            // 订阅myTopic话题
            sampleClient.subscribe(topic, qos);
        } catch(MqttException me) {
            Log.i("TAG", "------reason "+me.getReasonCode());
            Log.i("TAG", "------msg "+me.getMessage());
            Log.i("TAG", "------loc "+me.getLocalizedMessage());
            Log.i("TAG", "------cause "+me.getCause());
            Log.i("TAG", "------excep "+me);
            me.printStackTrace();
        }
    }

    // MQTT监听并且接受消息
    private MqttCallback mqttCallback = new MqttCallback() {

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            byte[] data = message.getPayload();
//            Log.i("TAG","-----messageArrived-->>"+new String(data));
            Log.i("TAG", "------Topic, "+topic);
            Intent intent = new Intent(ACTION_MQTT);
            intent.putExtra(EXTRA_DATA, data);
            intent.putExtra("topic", topic);
            sendBroadcast(intent);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {
            Log.i("TAG","------deliveryComplete-->>"+arg0.isComplete());
        }

        @Override
        public void connectionLost(Throwable arg0) {
            // 失去连接，重连
            Log.i("TAG","------connectionLost失去连接");
        }
    };

    // MQTT是否连接成功
    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken arg0) {
            Log.i("TAG", "------连接成功 ");
//            try {
//                // 订阅myTopic话题
////                sampleClient.subscribe(topic, );
//            } catch (MqttException e) {
//                e.printStackTrace();
//            }
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            arg1.printStackTrace();
            Log.i("TAG", "------连接失败");
            // 连接失败，重连
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sampleClient != null){
            try {
                sampleClient.disconnect();
                sampleClient.close();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }
}
