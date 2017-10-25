package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shemh on 2017/4/11.
 */

public class DynamicListBean extends BaseInfo implements Serializable{


    private List<ObjBean> obj;

    public List<ObjBean> getObj() {
        return obj;
    }

    public void setObj(List<ObjBean> obj) {
        this.obj = obj;
    }

    public static class ObjBean implements Serializable{
        /**
         * id : 147
         * content : djdjdj
         * lid : 106
         * createTime : 1493975652000
         * uid : 5f86efdb1e7e11e7bc8600163e064d3e
         * isSuccess : true
         * location : {"id":106,"latitude":22.674165,"longitude":114.068215,"country":"","province":"å¹¿ä¸\u009cç\u009c\u0081","city":"æ·±å\u009c³å¸\u0082","district":"","street":"","streetnum":"","citycode":"0755","adcode":"440309","aoiname":"","createTime":"2017-05-05 17:14:12"}
         * likeNum : 0
         * user : {"uid":"5f86efdb1e7e11e7bc8600163e064d3e","phonenumber":"18664565413","countrycode":"86","email":null,"password":"*","type":0,"level":0,"isactive":true,"isonline":true,"token":"*","signature":"123456","nickname":"myway_kibj5413","username":"","gender":true,"birthday":null,"profession":"","address":"","imgeUrl":"http://120.77.249.52/group1/M00/00/02/rBKUqFkKyw2AABiDAAF-CctvA7o851.jpg","referralcode":null,"lastlogindate":"2017-05-05 17:14:55","registerdate":"2017-04-11 14:16:18","space1":null,"space2":null,"isVip":false}
         * isLike : false
         * images : null
         * commentNum : 0
         */

        private int id;
        private String content;
        private int lid;
        private String createTime;
        private String uid;
        private boolean isSuccess;
        private LocationBean location;
        private int likeNum;
        private UserBean user;
        private boolean isLike;
        private boolean isVideo;
        private List<String> images;
        private int commentNum;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getLid() {
            return lid;
        }

        public void setLid(int lid) {
            this.lid = lid;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public boolean isIsSuccess() {
            return isSuccess;
        }

        public void setIsSuccess(boolean isSuccess) {
            this.isSuccess = isSuccess;
        }

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public int getLikeNum() {
            return likeNum;
        }

        public void setLikeNum(int likeNum) {
            this.likeNum = likeNum;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public boolean isIsLike() {
            return isLike;
        }

        public void setIsLike(boolean isLike) {
            this.isLike = isLike;
        }

        public boolean isVideo() {
            return isVideo;
        }

        public void setVideo(boolean video) {
            isVideo = video;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public int getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(int commentNum) {
            this.commentNum = commentNum;
        }

        public static class LocationBean implements Serializable{
            /**
             * id : 106
             * latitude : 22.674165
             * longitude : 114.068215
             * country :
             * province : å¹¿ä¸ç
             * city : æ·±å³å¸
             * district :
             * street :
             * streetnum :
             * citycode : 0755
             * adcode : 440309
             * aoiname :
             * createTime : 2017-05-05 17:14:12
             */

            private int id;
            private double latitude;
            private double longitude;
            private String country;
            private String province;
            private String city;
            private String district;
            private String street;
            private String streetnum;
            private String citycode;
            private String adcode;
            private String aoiname;
            private String createTime;

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

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
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

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public String getStreet() {
                return street;
            }

            public void setStreet(String street) {
                this.street = street;
            }

            public String getStreetnum() {
                return streetnum;
            }

            public void setStreetnum(String streetnum) {
                this.streetnum = streetnum;
            }

            public String getCitycode() {
                return citycode;
            }

            public void setCitycode(String citycode) {
                this.citycode = citycode;
            }

            public String getAdcode() {
                return adcode;
            }

            public void setAdcode(String adcode) {
                this.adcode = adcode;
            }

            public String getAoiname() {
                return aoiname;
            }

            public void setAoiname(String aoiname) {
                this.aoiname = aoiname;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }
        }

        public static class UserBean implements Serializable{
            /**
             * uid : 5f86efdb1e7e11e7bc8600163e064d3e
             * phonenumber : 18664565413
             * countrycode : 86
             * email : null
             * password : *
             * type : 0
             * level : 0
             * isactive : true
             * isonline : true
             * token : *
             * signature : 123456
             * nickname : myway_kibj5413
             * username :
             * gender : true
             * birthday : null
             * profession :
             * address :
             * imgeUrl : http://120.77.249.52/group1/M00/00/02/rBKUqFkKyw2AABiDAAF-CctvA7o851.jpg
             * referralcode : null
             * lastlogindate : 2017-05-05 17:14:55
             * registerdate : 2017-04-11 14:16:18
             * space1 : null
             * space2 : null
             * isVip : false
             */

            private String uid;
            private String phonenumber;
            private String countrycode;
            private Object email;
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
            private Object birthday;
            private String profession;
            private String address;
            private String imgeUrl;
            private Object referralcode;
            private String lastlogindate;
            private String registerdate;
            private Object space1;
            private Object space2;
            private boolean isVip;

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

            public String getCountrycode() {
                return countrycode;
            }

            public void setCountrycode(String countrycode) {
                this.countrycode = countrycode;
            }

            public Object getEmail() {
                return email;
            }

            public void setEmail(Object email) {
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

            public Object getBirthday() {
                return birthday;
            }

            public void setBirthday(Object birthday) {
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

            public Object getSpace1() {
                return space1;
            }

            public void setSpace1(Object space1) {
                this.space1 = space1;
            }

            public Object getSpace2() {
                return space2;
            }

            public void setSpace2(Object space2) {
                this.space2 = space2;
            }

            public boolean isIsVip() {
                return isVip;
            }

            public void setIsVip(boolean isVip) {
                this.isVip = isVip;
            }
        }
    }
}
