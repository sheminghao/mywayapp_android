package com.mywaytec.myway.utils;

import android.util.Log;

import com.mywaytec.myway.APP;
import com.mywaytec.myway.base.Constant;

import java.util.HashMap;

/**
 * Created by shemh on 2017/3/1.
 */

public class BleUtil {

    /** 信驰达蓝牙模块 **/
//    public static final String ServiceUUIDString1 = "FFF0";
//    public static final String ServiceUUIDString2 = "FFB0";
//
//    public static final String ServiceForAnti_Hijacking = "FFC0";  // 防劫持（蓝牙密码的修改）
//    public static final String CharacteristicReadForAnti_Hijacking = "FFC2";
//    public static final String CharacteristicWriteForAnti_Hijacking = "FFC1";
//
//    public static final String ServiceForReadUUIDString = "FFE0";
//    public static final String CharacteristicReadUUIDString1 = "FFE4";
//    public static final String ServiceForwriteUUIDString = "FFE5";
//    public static final String CharacteristicWriteUUIDString1 = "FFE9";

    /**
     * 蓝牙地址
     */
    private static final String BLE_ADDRESS = "BLE_ADDRESS";

    /**
     * 获得蓝牙地址
     * @return
     */
    public static String getBleAddress() {
        return PreferencesUtils.getString(APP.getInstance(), BLE_ADDRESS);
    }

    /**设置蓝牙地址*/
    public static void setBleAddress(String bleAddress) {
        if(bleAddress == null) {
            PreferencesUtils.removeValue(APP.getInstance(), BLE_ADDRESS);
        } else {
            PreferencesUtils.putString(APP.getInstance(), BLE_ADDRESS, bleAddress);
        }
    }

    /**
     * 蓝牙名称
     */
    private static final String BLE_NAME = "BLE_NAME";

    /**
     * 获取蓝牙名称
     * @return
     */
    public static String getBleName() {
        return PreferencesUtils.getString(APP.getInstance(), BLE_NAME);
    }

    /**设置蓝牙名称*/
    public static void setBleName(String name) {
        if(name == null) {
            PreferencesUtils.removeValue(APP.getInstance(), BLE_NAME);
        } else {
            PreferencesUtils.putString(APP.getInstance(), BLE_NAME, name);
        }
    }

    private static HashMap<String, String> attributes = new HashMap();
    static {
        // Sample Services.
        attributes.put("0000180d-0000-1000-8000-00805f9b34fb", "Heart Rate Service");
        attributes.put("0000180a-0000-1000-8000-00805f9b34fb", "Device Information Service");
        // Sample Characteristics.
        attributes.put(Constant.BLE.NOTIFY_SERVICE_UUID, "Heart Rate SERVICE_UUID");
        attributes.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
    }
    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }

    /**
     * 已经校验位
     * */
    public static byte[] getAllCmd(byte[] bytes) {
//        byte[] allBytes = new byte[bytes.length + 2];
//        allBytes[0] = (byte)(allBytes.length - 1);
//        allBytes[0] = (byte) 0xff;
//        System.arraycopy(bytes, 0, allBytes, 1, bytes.length);
//        allBytes[allBytes.length - 1] = getChecksum(allBytes);
//        Log.i("TAG", "-----crc:"+getChecksum(bytes)[0] + getChecksum(bytes)[1]);
        byte[] allBytes = new byte[bytes.length + 2];
        System.arraycopy(bytes, 0, allBytes, 0, bytes.length);
        allBytes[allBytes.length - 2] = getChecksum(bytes)[0];
        allBytes[allBytes.length - 1] = getChecksum(bytes)[1];
        return allBytes;
    }

    /**
     * 获取校验位
     * @param bytes
     * @return
     */
    public static byte[] getChecksum(byte[] bytes) {
//        byte[] aBytes = new byte[bytes.length - 2];
//        System.arraycopy(bytes, 2, aBytes, 0, aBytes.length);
        int checksum = CRC16Util.calcCrc16(bytes, 2, bytes.length-2);
        return intTo2ByteArray(checksum);
    }

    /**
     * int到byte[]
     * @param i
     * @return
     */
    public static byte[] intTo2ByteArray(int i) {
        byte[] result = new byte[2];
        result[0] = (byte)((i >> 8) & 0xFF);
        result[1] = (byte)(i & 0xFF);
        return result;
    }

    /**
     * 返回数据进行CRC校验
     * @return
     */
    public static boolean receiveDataCRCCheck(byte[] data){
        if (data.length > 4){
            byte[] aBytes = new byte[data.length - 2];
            System.arraycopy(data, 0, aBytes, 0, aBytes.length);
            byte[] checkdata = getChecksum(aBytes);
            if (checkdata[0] == data[data.length-2] && checkdata[1] == data[data.length - 1]){
                return true;
            }
        }
        return false;
    }

    //校验后速度指令
    public static byte[] getSpeedAndPower(){
        return getAllCmd(Constant.BLE.SPEED_AND_POWER);
//        return Constant.BLE.SPEED_AND_POWER;
    }

    //开灯
    public static byte[] getOpenLightFront(){
        return getAllCmd(Constant.BLE.OPEN_LIGHT_FRONT);
    }

    //限速设置指令
    public static byte[] getSpeedLimit(int speed){
        String speedStr = Integer.toHexString(speed);
        byte speedByte = Byte.parseByte(speedStr, 16);
        byte[] newByte = new byte[Constant.BLE.LIMIT_SPEED.length+1];
        System.arraycopy(Constant.BLE.LIMIT_SPEED, 0, newByte, 0, Constant.BLE.LIMIT_SPEED.length);
        newByte[newByte.length-1] = speedByte;
        return getAllCmd(newByte);
    }

    //限速设置指令
    public static byte[] deleteFingerWark(byte fingerWarkId){
        byte[] newByte = new byte[Constant.BLE.DETELE_FINGER_WARK.length+1];
        System.arraycopy(Constant.BLE.DETELE_FINGER_WARK, 0, newByte, 0, Constant.BLE.DETELE_FINGER_WARK.length);
        newByte[newByte.length-1] = fingerWarkId;
        return getAllCmd(newByte);
    }

    //灯带模式
    public static byte[] dengdaiMode(int mode){
        String speedStr = Integer.toHexString(mode);
        byte speedByte = Byte.parseByte(speedStr, 16);
        byte[] newByte = new byte[Constant.BLE.DENGDAI_MODE.length+1];
        System.arraycopy(Constant.BLE.DENGDAI_MODE, 0, newByte, 0, Constant.BLE.DENGDAI_MODE.length);
        newByte[newByte.length-1] = speedByte;
        return getAllCmd(newByte);
    }

    //车辆密码验证
    public static byte[] verificationVehiclePassword(String password){
//        String speedStr = Integer.toHexString(password);
        byte[] speedByte = new byte[password.length()];
        byte[] newByte = new byte[Constant.BLE.VEHICLE_PASSWORD_VERIFICATION.length+speedByte.length];
        for (int i = 0; i < password.toCharArray().length; i++) {
            String speedStr = Integer.toHexString(Integer.parseInt(String.valueOf(password.toCharArray()[i])));
            byte psd = Byte.parseByte(speedStr, 16);
            speedByte[i] = psd;
        }
        System.arraycopy(Constant.BLE.VEHICLE_PASSWORD_VERIFICATION, 0, newByte, 0,
                Constant.BLE.VEHICLE_PASSWORD_VERIFICATION.length);
        System.arraycopy(speedByte, 0, newByte, Constant.BLE.VEHICLE_PASSWORD_VERIFICATION.length, speedByte.length);
        Log.i("TAG", "------VEHICLE_PASSWORD_VERIFICATION,"+ConversionUtil.byte2HexStr(getAllCmd(newByte)));
        return getAllCmd(newByte);
    }

    //车辆密码设置
    public static byte[] setVehiclePassword(String password){
        byte[] speedByte = new byte[password.length()];
        byte[] newByte = new byte[Constant.BLE.VEHICLE_PASSWORD_SETTING.length+speedByte.length];
        for (int i = 0; i < password.toCharArray().length; i++) {
            String speedStr = Integer.toHexString(Integer.parseInt(String.valueOf(password.toCharArray()[i])));
            byte psd = Byte.parseByte(speedStr, 16);
            speedByte[i] = psd;
        }
        System.arraycopy(Constant.BLE.VEHICLE_PASSWORD_SETTING, 0, newByte, 0,
                Constant.BLE.VEHICLE_PASSWORD_SETTING.length);
        System.arraycopy(speedByte, 0, newByte, Constant.BLE.VEHICLE_PASSWORD_SETTING.length, speedByte.length);
        Log.i("TAG", "------VEHICLE_PASSWORD_SETTING,"+ConversionUtil.byte2HexStr(getAllCmd(newByte)));
        return getAllCmd(newByte);
    }

}
