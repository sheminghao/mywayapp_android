package com.mywaytec.myway.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mywaytec.myway.R;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt_test);

        TextView tvSend = (TextView) findViewById(R.id.tv_send);
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publish();
            }
        });
    }

    public static void publish(){
        //消息的类型
        String topic        = "android_test";
        //消息内容
        String content      = "android端发来消息";
        //消息发送的模式   选择消息发送的次数，依据不同的使用环境使用不同的模式
        int qos             = 2;
        //服务器地址
        String broker       = "tcp://121.43.180.66:1883";
        //客户端的唯一标识
        String clientId     = "CLIENTID_2000012";
        //消息缓存的方式  内存缓存
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            //创建MQTT客户端
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            //消息的配置参数
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName("admin");
            connOpts.setPassword("public".toCharArray());
            //不记忆上一次会话
            connOpts.setCleanSession(true);
            Log.i("TAG", "------Connecting to broker: "+broker);
            //链接服务器
            sampleClient.connect(connOpts);
            Log.i("TAG", "------Connected");
            Log.i("TAG", "------Publishing message: "+content);
            sampleClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                }
                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    Log.i("TAG","-----deliveryComplete-->>"+iMqttDeliveryToken.isComplete());
                }
            });
            //创建消息
            MqttMessage message = new MqttMessage(content.getBytes());
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
