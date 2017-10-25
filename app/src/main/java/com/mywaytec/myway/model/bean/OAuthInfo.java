package com.mywaytec.myway.model.bean;

/**
 * Created by shemh on 2017/4/26.
 */

public class OAuthInfo {


    /**
     * unionid : oX5jzvoZbBiH6JRqYNPZWPdFYwc4
     * userID : oOh1qw82_hKluDlTxvV5u3UaIK_I
     * icon : http://wx.qlogo.cn/mmopen/ajNVdqHZLLAAwg1RJ4w4qQfViaYtnUAib7cohsnlC0Tnq7dgkWRBsO7mzPKia05TVQmcepsic0jAx793bsDQ6RfYQg/0
     * expiresTime : 1493204935528
     * nickname :
     * token : G9hfVPNG1EUsvdL9OqcbpXDM9_FW7XT5aQ0r9s41rnU1tLM7Gx8zqPKoB748si1Ic0Uzgb-fu2fEP2w_9D6d8cyYNUkYROc7_2OSM_LU0w8
     * city : Shenzhen
     * gender : 0
     * province : Guangdong
     * refresh_token : yk7LAp9fuujiO4bc41PvWxtIodXW_dhIlEDXNNF76O64N6wKJUuR6R3FIKjVoBejTgDsQvq8WeIPKLOvpVcvenTfmzdiuvepgdlFLJhAvZI
     * openid : oOh1qw82_hKluDlTxvV5u3UaIK_I
     * country : CN
     * expiresIn : 7200
     */

    private String unionid;
    private String userID;
    private String icon;
    private long expiresTime;
    private String nickname;
    private String token;
    private String city;
    private String gender;
    private String province;
    private String refresh_token;
    private String openid;
    private String country;
    private int expiresIn;

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(long expiresTime) {
        this.expiresTime = expiresTime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
