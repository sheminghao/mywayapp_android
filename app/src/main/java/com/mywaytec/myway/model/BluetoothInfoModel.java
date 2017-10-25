package com.mywaytec.myway.model;

import android.bluetooth.BluetoothDevice;

/**
 * Created by shemh on 2017/2/22.
 */

public class BluetoothInfoModel {

    private BluetoothDevice device;
    private int rssi;

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }
}
