package com.mywaytec.myway.utils;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.mywaytec.myway.APP;
import com.mywaytec.myway.base.Constant;

import java.util.UUID;

/**
 * Created by shemh on 2017/10/26.
 */

public class BleKitUtils {

    private static BluetoothClient bluetoothClient;

    //获取蓝牙客户端
    public static BluetoothClient getBluetoothClient(){
        if (bluetoothClient == null) {
            synchronized (APP.class) {
                if (bluetoothClient == null) {
                    bluetoothClient = new BluetoothClient(APP.getInstance());
                }
            }
        }
        return bluetoothClient;
    }

    //平衡车写数据
    public static void writeP(String MAC, byte[] bytes, final BleWriteResponse bleWriteResponse, long delayInMillis){
        getBluetoothClient().write(MAC, UUID.fromString(Constant.BLE.WRITE_SERVICE_UUID),
                UUID.fromString(Constant.BLE.WRITE_CHARACTERISTIC_UUID), bytes, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        if (bleWriteResponse != null){
                            bleWriteResponse.onResponse(code);
                        }
                    }
                }, delayInMillis);
    }

    //平衡车写数据
    public static void writeP(String MAC, byte[] bytes, final BleWriteResponse bleWriteResponse){
        getBluetoothClient().write(MAC, UUID.fromString(Constant.BLE.WRITE_SERVICE_UUID),
                UUID.fromString(Constant.BLE.WRITE_CHARACTERISTIC_UUID), bytes, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        if (bleWriteResponse != null){
                            bleWriteResponse.onResponse(code);
                        }
                    }
                }, 100);
    }

    //泰斗写数据
    public static void writeTaiTou(String MAC, byte[] bytes, final BleWriteResponse bleWriteResponse){
        getBluetoothClient().write(MAC, UUID.fromString(Constant.BLE.TAIDOU_WRITE_SERVICE_UUID),
                UUID.fromString(Constant.BLE.TAIDOU_WRITE_CHARACTERISTIC_UUID), bytes, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        if (bleWriteResponse != null){
                            bleWriteResponse.onResponse(code);
                        }
                    }
        }, 0);
    }

    //平衡车写数据无反馈
    public static void writeNoRspP(String MAC, byte[] bytes, final BleWriteResponse bleWriteResponse){
        getBluetoothClient().writeNoRsp(MAC, UUID.fromString(Constant.BLE.WRITE_SERVICE_UUID),
                UUID.fromString(Constant.BLE.WRITE_CHARACTERISTIC_UUID), bytes, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        if (bleWriteResponse != null){
                            bleWriteResponse.onResponse(code);
                        }
                    }
                });
    }

    //泰斗写数据无反馈
    public static void writeNoRspTaiTou(String MAC, byte[] bytes, final BleWriteResponse bleWriteResponse){
        getBluetoothClient().writeNoRsp(MAC, UUID.fromString(Constant.BLE.TAIDOU_WRITE_SERVICE_UUID),
                UUID.fromString(Constant.BLE.TAIDOU_WRITE_CHARACTERISTIC_UUID), bytes, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        if (bleWriteResponse != null){
                            bleWriteResponse.onResponse(code);
                        }
                    }
                });
    }

    //泰斗通知
    public static void notifyTaiTou(String MAC, final BleNotifyResponse bleNotifyResponse) {
        getBluetoothClient().notify(MAC, UUID.fromString(Constant.BLE.TAIDOU_NOTIFY_SERVICE_UUID),
                UUID.fromString(Constant.BLE.TAIDOU_NOTIFY_CHARACTERISTIC_UUID), new BleNotifyResponse() {
                    @Override
                    public void onNotify(UUID service, UUID character, byte[] value) {
                        bleNotifyResponse.onNotify(service, character, value);
                    }

                    @Override
                    public void onResponse(int code) {
                        bleNotifyResponse.onResponse(code);
                    }
                });
    }

    //平衡车通知
    public static void notifyP(String MAC, final BleNotifyResponse bleNotifyResponse) {
        getBluetoothClient().notify(MAC, UUID.fromString(Constant.BLE.NOTIFY_SERVICE_UUID),
                UUID.fromString(Constant.BLE.NOTIFY_CHARACTERISTIC_UUID), new BleNotifyResponse() {
                    @Override
                    public void onNotify(UUID service, UUID character, byte[] value) {
                        bleNotifyResponse.onNotify(service, character, value);
                    }

                    @Override
                    public void onResponse(int code) {
                        bleNotifyResponse.onResponse(code);
                    }
                });
    }

    //泰斗取消通知
    public static void unnotifyTaiTou(String MAC, final BleUnnotifyResponse bleUnnotifyResponse) {
        getBluetoothClient().unnotify(MAC, UUID.fromString(Constant.BLE.TAIDOU_NOTIFY_SERVICE_UUID),
                UUID.fromString(Constant.BLE.TAIDOU_NOTIFY_CHARACTERISTIC_UUID), new BleUnnotifyResponse() {
                    @Override
                    public void onResponse(int code) {
                        bleUnnotifyResponse.onResponse(code);
                    }
                });
    }

    //平衡车取消通知
    public static void unnotifyP(String MAC, final BleUnnotifyResponse bleUnnotifyResponse) {
        getBluetoothClient().unnotify(MAC, UUID.fromString(Constant.BLE.NOTIFY_SERVICE_UUID),
                UUID.fromString(Constant.BLE.NOTIFY_CHARACTERISTIC_UUID), new BleUnnotifyResponse() {
                    @Override
                    public void onResponse(int code) {
                        bleUnnotifyResponse.onResponse(code);
                    }
                });
    }
}
