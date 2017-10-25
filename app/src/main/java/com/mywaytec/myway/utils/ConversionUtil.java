package com.mywaytec.myway.utils;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by shemh on 2017/3/9.
 */

public class ConversionUtil {

    public static String getSpeedPerHour(float speed){
        double s = speed;
        s = s * 3600 / 1000;
        String result = String.format("%.2f", s);
        return result;
    }

    public static String getKM(float m){
        double km = m;
        String result = String.format("%.2f", km/1000);
        return result;
    }

    public static String getHour(int time){
        String h = String.valueOf(time / 3600);
        String m = String.valueOf(time % 3600 / 60);
        String s = String.valueOf(time % 3600 % 60);
        if (h.length() < 2){
            h = "0"+h;
        }
        if (m.length() < 2){
            m = "0"+m;
        }
        if (s.length() < 2){
            s = "0"+s;
        }
        return h+":"+m+":"+s;
    }

    /**
     * bytes转换成十六进制字符串
     * @param  b byte数组
     * @return String 每个Byte值之间空格分隔
     */
    public static String byte2HexStr(byte[] b)
    {
        String stmp="";
        StringBuilder sb = new StringBuilder("");
        for (int n=0;n<b.length;n++)
        {
            stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length()==1)? "0"+stmp : stmp);
            sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
    }

    /**
     * 字符串转换为Byte值
     * @para String src Byte字符串，每个Byte之间没有分隔符
     * @return byte[]
     */
    public static byte[] hexStr2Bytes(String inputStr)
    {
        if (inputStr == null || inputStr.equals("")) {
            return null;
        }
        inputStr = inputStr.toUpperCase();
        int length = inputStr.length() / 2;
        char[] hexChars = inputStr.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 字符串转换成十六进制字符串
     * @param str 待转换的ASCII字符串
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
     */
    public static String str2HexStr(String str)
    {

        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++)
        {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }


    private static double PI = 3.14159265358979324;
    private double X_PI = 3.14159265358979324 * 3000.0 / 180.0;
    public static LatLng gcj_decrypt(double lat, double lon) {
        LatLng d = delta(lat, lon);
        double latitude = lat-d.latitude;
        double longitude = lon-d.longitude;
        LatLng location = new LatLng(latitude, longitude);
        return location;
    }

    public static LatLng delta(double lat, double lon){
        double a = 6378245.0; // a: 卫星椭球坐标投影到平面地图坐标系的投影因子。
        double ee = 0.00669342162296594323; // ee: 椭球的偏心率。
        double dLat = functionLat(lon - 105.0, lat - 35.0);
        double dLon = functionLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * PI);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * PI);
        LatLng loc = new LatLng(dLat, dLon);
        return loc;
    }

    private static double functionLat (double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double functionLon (double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

}
