package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

import java.util.List;

/**
 * Created by shemh on 2017/8/2.
 */

public class NearbyBean extends BaseInfo {


    private List<ObjBean> obj;

    public List<ObjBean> getObj() {
        return obj;
    }

    public void setObj(List<ObjBean> obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        /**
         * uid : 7b96e829518e11e78dd300163e064d3e
         * lid : 6
         * creatTime : 2017-07-28 13:54:42
         * location : {"id":6,"latitude":22.661987,"longitude":114.039796,"country":null,"province":"广东","city":"深圳市","district":null,"street":"龙华人民医院","streetnum":null,"citycode":null,"adcode":null,"aoiname":null,"createTime":null}
         * user : {"uid":"7b96e829518e11e78dd300163e064d3e","phonenumber":"","countrycode":null,"email":"","password":"*","type":3,"level":0,"isactive":true,"isonline":true,"token":"*","signature":"","nickname":"曼威小仙女","username":"","gender":true,"birthday":"1995-12-25","profession":"","address":"","imgeUrl":"http://120.77.249.52/group1/M00/00/04/rBKUqFlCJSaAS8GOAADv1ML9t8I947.jpg","referralcode":null,"lastlogindate":"2017-07-13 09:12:06","registerdate":"2017-06-15 13:50:06","oauthUid":"oOh1qw4RF7UEm_gJ0jaSxKsABaYw","space2":null,"integral":28,"glod":2,"isDel":false}
         * distance : 4358.6
         */

        private String uid;
        private int lid;
        private String creatTime;
        private LocationBean location;
        private UserBean user;
        private double distance;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public int getLid() {
            return lid;
        }

        public void setLid(int lid) {
            this.lid = lid;
        }

        public String getCreatTime() {
            return creatTime;
        }

        public void setCreatTime(String creatTime) {
            this.creatTime = creatTime;
        }

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public static class LocationBean {
            /**
             * id : 6
             * latitude : 22.661987
             * longitude : 114.039796
             * country : null
             * province : 广东
             * city : 深圳市
             * district : null
             * street : 龙华人民医院
             * streetnum : null
             * citycode : null
             * adcode : null
             * aoiname : null
             * createTime : null
             */

            private int id;
            private double latitude;
            private double longitude;
            private Object country;
            private String province;
            private String city;
            private Object district;
            private String street;
            private Object streetnum;
            private Object citycode;
            private Object adcode;
            private Object aoiname;
            private Object createTime;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public Object getCountry() {
                return country;
            }

            public void setCountry(Object country) {
                this.country = country;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public Object getDistrict() {
                return district;
            }

            public void setDistrict(Object district) {
                this.district = district;
            }

            public String getStreet() {
                return street;
            }

            public void setStreet(String street) {
                this.street = street;
            }

            public Object getStreetnum() {
                return streetnum;
            }

            public void setStreetnum(Object streetnum) {
                this.streetnum = streetnum;
            }

            public Object getCitycode() {
                return citycode;
            }

            public void setCitycode(Object citycode) {
                this.citycode = citycode;
            }

            public Object getAdcode() {
                return adcode;
            }

            public void setAdcode(Object adcode) {
                this.adcode = adcode;
            }

            public Object getAoiname() {
                return aoiname;
            }

            public void setAoiname(Object aoiname) {
                this.aoiname = aoiname;
            }

            public Object getCreateTime() {
                return createTime;
            }

            public void setCreateTime(Object createTime) {
                this.createTime = createTime;
            }
        }

        public static class UserBean {
            /**
             * uid : 7b96e829518e11e78dd300163e064d3e
             * phonenumber :
             * countrycode : null
             * email :
             * password : *
             * type : 3
             * level : 0
             * isactive : true
             * isonline : true
             * token : *
             * signature :
             * nickname : 曼威小仙女
             * username :
             * gender : true
             * birthday : 1995-12-25
             * profession :
             * address :
             * imgeUrl : http://120.77.249.52/group1/M00/00/04/rBKUqFlCJSaAS8GOAADv1ML9t8I947.jpg
             * referralcode : null
             * lastlogindate : 2017-07-13 09:12:06
             * registerdate : 2017-06-15 13:50:06
             * oauthUid : oOh1qw4RF7UEm_gJ0jaSxKsABaYw
             * space2 : null
             * integral : 28
             * glod : 2
             * isDel : false
             */

            private String uid;
            private String phonenumber;
            private Object countrycode;
            private String email;
            private String password;
            private int type;
            private int level;
            private boolean isactive;
            private boolean isonline;
            private String token;
            private String signature;
            private String nickname;
            private String username;
            private boolean gender;
            private String birthday;
            private String profession;
            private String address;
            private String imgeUrl;
            private Object referralcode;
            private String lastlogindate;
            private String registerdate;
            private String oauthUid;
            private Object space2;
            private int integral;
            private int glod;
            private boolean isDel;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getPhonenumber() {
                return phonenumber;
            }

            public void setPhonenumber(String phonenumber) {
                this.phonenumber = phonenumber;
            }

            public Object getCountrycode() {
                return countrycode;
            }

            public void setCountrycode(Object countrycode) {
                this.countrycode = countrycode;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public boolean isIsactive() {
                return isactive;
            }

            public void setIsactive(boolean isactive) {
                this.isactive = isactive;
            }

            public boolean isIsonline() {
                return isonline;
            }

            public void setIsonline(boolean isonline) {
                this.isonline = isonline;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public String getSignature() {
                return signature;
            }

            public void setSignature(String signature) {
                this.signature = signature;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public boolean isGender() {
                return gender;
            }

            public void setGender(boolean gender) {
                this.gender = gender;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public String getProfession() {
                return profession;
            }

            public void setProfession(String profession) {
                this.profession = profession;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getImgeUrl() {
                return imgeUrl;
            }

            public void setImgeUrl(String imgeUrl) {
                this.imgeUrl = imgeUrl;
            }

            public Object getReferralcode() {
                return referralcode;
            }

            public void setReferralcode(Object referralcode) {
                this.referralcode = referralcode;
            }

            public String getLastlogindate() {
                return lastlogindate;
            }

            public void setLastlogindate(String lastlogindate) {
                this.lastlogindate = lastlogindate;
            }

            public String getRegisterdate() {
                return registerdate;
            }

            public void setRegisterdate(String registerdate) {
                this.registerdate = registerdate;
            }

            public String getOauthUid() {
                return oauthUid;
            }

            public void setOauthUid(String oauthUid) {
                this.oauthUid = oauthUid;
            }

            public Object getSpace2() {
                return space2;
            }

            public void setSpace2(Object space2) {
                this.space2 = space2;
            }

            public int getIntegral() {
                return integral;
            }

            public void setIntegral(int integral) {
                this.integral = integral;
            }

            public int getGlod() {
                return glod;
            }

            public void setGlod(int glod) {
                this.glod = glod;
            }

            public boolean isIsDel() {
                return isDel;
            }

            public void setIsDel(boolean isDel) {
                this.isDel = isDel;
            }
        }
    }
}
