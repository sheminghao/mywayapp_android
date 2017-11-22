/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mywaytec.myway.base;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.mywaytec.myway.ui.moreCarInfo.MoreCarInfoActivity;
import com.mywaytec.myway.utils.BleUtil;
import com.mywaytec.myway.utils.PreferencesUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static android.bluetooth.BluetoothGatt.CONNECTION_PRIORITY_HIGH;

/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
public class BluetoothLeService extends Service {
    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;

    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED           = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED        = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE           = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA                      = "com.example.bluetooth.le.EXTRA_DATA";

    public final static UUID UUID_HEART_RATE_MEASUREMENT       = UUID.fromString(Constant.BLE.NOTIFY_CHARACTERISTIC_UUID);

    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i("TAG", "------BluetoothGatt,, status="+status+",newState="+newState);
            String intentAction;
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    intentAction = ACTION_GATT_CONNECTED;
                    mConnectionState = STATE_CONNECTED;
                    PreferencesUtils.putInt(BluetoothLeService.this, "mConnectionState", STATE_CONNECTED);
                    //                sendHeartbeatRequest();
                    broadcastUpdate(intentAction);
                    Log.i(TAG, "Connected to GATT server.");
                    // Attempts to discover services after successful connection.
                    Log.i(TAG, "Attempting to start service discovery:" +
                            mBluetoothGatt.discoverServices());
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    intentAction = ACTION_GATT_DISCONNECTED;
                    mConnectionState = STATE_DISCONNECTED;
                    PreferencesUtils.putInt(BluetoothLeService.this, "mConnectionState", STATE_DISCONNECTED);
                    Log.i(TAG, "Disconnected from GATT server.");
                    close();//关闭gatt
                    stopSendHeartbeatRequest();
                    broadcastUpdate(intentAction);
                }
            } else {
                Log.d(TAG, "onConnectionStateChange received: " + status);
//                intentAction = BluetoothConstants.GATT_STATUS_133;
//                mBLEConnectionState = BluetoothConstants.BLE_STATE_DISCONNECTED;
                close(); // 防止出现status 133
//                broadcastUpdate(intentAction);
//                connect(reConnectAddress);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        // This is special handling for the Heart Rate Measurement profile.  Data parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
//        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
//            int flag = characteristic.getProperties();
//            int format = -1;
//            if ((flag & 0x01) != 0) {
//                format = BluetoothGattCharacteristic.FORMAT_UINT16;
//                Log.d(TAG, "Heart rate format UINT16.");
//            } else {
//                format = BluetoothGattCharacteristic.FORMAT_UINT8;
//                Log.d(TAG, "Heart rate format UINT8.");
//            }
//            final int heartRate = characteristic.getIntValue(format, 1);
//            Log.d(TAG, String.format("Received heart rate: %d", heartRate));
//            intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
//        } else {
//            // For all other profiles, writes the data formatted in HEX.
//            final byte[] data = characteristic.getValue();
////            if (data != null && data.length > 0) {
////                final StringBuilder stringBuilder = new StringBuilder(data.length);
////                for(byte byteChar : data)
////                    stringBuilder.append(String.format("%02X ", byteChar));
////                intent.putExtra(EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
////            }
//            intent.putExtra(EXTRA_DATA, new String(data));
//        }
        final byte[] data = characteristic.getValue();
        intent.putExtra(EXTRA_DATA, data);
        sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     *
     * @return Return true if the connection is initiated successfully. The connection result
     *         is reported asynchronously through the
     *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     *         callback.
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            try {
                if (mBluetoothGatt.connect()) {
                    mConnectionState = STATE_CONNECTING;
                    return true;
                } else {
                    return false;
                }
            }catch (Exception e){
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
        stopSendHeartbeatRequest();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        Log.i("TAG", "------mBluetoothGatt，close()");
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    public void readCharacteristic(String serviceUUID, String cUUID, byte[] value) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        BluetoothGattService service = mBluetoothGatt.getService(UUID.fromString(serviceUUID));
        BluetoothGattCharacteristic characteristic= service.getCharacteristic(UUID.fromString(cUUID));
        characteristic.setValue(value);
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    public void writeCharacteristic(String serviceUUID, String cUUID, byte[] value) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        try {
            BluetoothGattService service = mBluetoothGatt.getService(UUID.fromString(serviceUUID));
            if(null != service) {
                BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(cUUID));
                if (null != characteristic) {
                    characteristic.setValue(value);
                    mBluetoothGatt.writeCharacteristic(characteristic);
                }
            }
        }catch (Exception e){

        }
    }

    public void writeCharacteristic(String serviceUUID, String wUUID, String rUUID, byte[] value) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }

        try {
            BluetoothGattService service = mBluetoothGatt.getService(UUID.fromString(serviceUUID));
            if(null != service) {
                BluetoothGattCharacteristic wCharacteristic = service.getCharacteristic(UUID.fromString(wUUID));
                BluetoothGattCharacteristic rCharacteristic = service.getCharacteristic(UUID.fromString(rUUID));
                if (null != wCharacteristic && null != rCharacteristic) {
                    wCharacteristic.setValue(value);
                    rCharacteristic.setValue(value);
                    mBluetoothGatt.setCharacteristicNotification(rCharacteristic, true);
                    mBluetoothGatt.writeCharacteristic(wCharacteristic);
                }
            }
        }catch (Exception e){
        }
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        BluetoothGattService service = mBluetoothGatt.getService(UUID.fromString(Constant.BLE.NOTIFY_SERVICE_UUID));
        if (null != service) {
            BluetoothGattCharacteristic characteristic1 = service.getCharacteristic(UUID.fromString(Constant.BLE.NOTIFY_CHARACTERISTIC_UUID));
            if (null != characteristic1) {
                mBluetoothGatt.setCharacteristicNotification(characteristic1, enabled);
            }
        }

        // This is specific to Heart Rate Measurement.
//        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic1.getUuid())) {
//            BluetoothGattDescriptor descriptor = characteristic1.getDescriptor(
//                    UUID.fromString(BleUtil.WRITE_CHARACTERISTIC_UUID));
//            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//            mBluetoothGatt.writeDescriptor(descriptor);
//        }
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();
    }

    public int getConnectionState(){
        return mConnectionState;
    }


    private Timer timer;
    private TimerTask timerTask;
    //发送心跳包，2秒发送1次
    private void sendHeartbeatRequest(){
        String uuid = PreferencesUtils.getString(this, "uuid");
        //平衡才需要发送心跳包
        if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (mConnectionState == STATE_CONNECTED) {
                        Log.i("TAG", "-------车辆连接，发送心跳包指令");
                        if (!PreferencesUtils.getBoolean(BluetoothLeService.this, "isFirmwareUpdate", false)) {
                            Log.i("TAG", "-------没有固件升级，发送心跳包指令");
                            String uuid = PreferencesUtils.getString(BluetoothLeService.this, "uuid");
                            if (null != BleUtil.getSpeedAndPower() || BleUtil.getSpeedAndPower().length > 0) {
                                if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                                    writeCharacteristic(Constant.BLE.WRITE_SERVICE_UUID,
                                            Constant.BLE.WRITE_CHARACTERISTIC_UUID, Constant.BLE.HEARTBEAT_PACKET);
                                    setCharacteristicNotification(null, true);
                                } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                                    writeCharacteristic(Constant.BLE.TAIDOU_WRITE_SERVICE_UUID,
                                            Constant.BLE.TAIDOU_WRITE_CHARACTERISTIC_UUID,
                                            Constant.BLE.TAIDOU_NOTIFY_CHARACTERISTIC_UUID,
                                            Constant.BLE.HEARTBEAT_PACKET);
                                }
                            }
                        }
                    }
                }
            };
            timer.schedule(timerTask, 0, 5000);
        }
    }

    //停止发送速度请求指令
    private void stopSendHeartbeatRequest(){
        Log.i("TAG", "------BluetoothService停止发送心跳包");
        if (null != timerTask) {
            timerTask.cancel();
            timerTask = null;
        }
        if (null != timer){
            timer.cancel();
            timer = null;
        }
    }
}
