package com.mywaytec.myway.model.bean;

/**
 * Created by shemh on 2017/10/31.
 */

public class BleInfoBean {

    /**
     * 车辆地址
     */
    private String mac;

    /**
     * 锁车状态
     */
    private int suocheState;

    /**
     * 速度限制
     */
    private int suduxianzhi;

    /**
     * 最高限速值
     */
    private int highSpeed;

    /**
     * 最低限速值
     */
    private int lowSpeed;

    /**
     * 前灯
     */
    private int qianteng;

    /**
     * 尾灯
     */
    private int weideng;

    /**
     * 滑行启动
     */
    private int huaxingqidong;

    /**
     * 定速巡航
     */
    private int dingsuxunhang;

    /**
     * 灯带模式
     */
    private int dengdaimoshi;

    /**
     * 硬件版本号
     */
    private String yingjianCode;

    /**
     * 软件版本号
     */
    private String ruanjianCode;

    /**
     * 车辆状态
     */
    private byte[] carState;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getSuocheState() {
        return suocheState;
    }

    public void setSuocheState(int suocheState) {
        this.suocheState = suocheState;
    }

    public int getSuduxianzhi() {
        return suduxianzhi;
    }

    public void setSuduxianzhi(int suduxianzhi) {
        this.suduxianzhi = suduxianzhi;
    }

    public int getHighSpeed() {
        return highSpeed;
    }

    public void setHighSpeed(int highSpeed) {
        this.highSpeed = highSpeed;
    }

    public int getLowSpeed() {
        return lowSpeed;
    }

    public void setLowSpeed(int lowSpeed) {
        this.lowSpeed = lowSpeed;
    }

    public int getQianteng() {
        return qianteng;
    }

    public void setQianteng(int qianteng) {
        this.qianteng = qianteng;
    }

    public int getWeideng() {
        return weideng;
    }

    public void setWeideng(int weideng) {
        this.weideng = weideng;
    }

    public int getHuaxingqidong() {
        return huaxingqidong;
    }

    public void setHuaxingqidong(int huaxingqidong) {
        this.huaxingqidong = huaxingqidong;
    }

    public int getDingsuxunhang() {
        return dingsuxunhang;
    }

    public void setDingsuxunhang(int dingsuxunhang) {
        this.dingsuxunhang = dingsuxunhang;
    }

    public int getDengdaimoshi() {
        return dengdaimoshi;
    }

    public void setDengdaimoshi(int dengdaimoshi) {
        this.dengdaimoshi = dengdaimoshi;
    }

    public String getYingjianCode() {
        return yingjianCode;
    }

    public void setYingjianCode(String yingjianCode) {
        this.yingjianCode = yingjianCode;
    }

    public String getRuanjianCode() {
        return ruanjianCode;
    }

    public void setRuanjianCode(String ruanjianCode) {
        this.ruanjianCode = ruanjianCode;
    }

    public byte[] getCarState() {
        return carState;
    }

    public void setCarState(byte[] carState) {
        this.carState = carState;
    }
}
