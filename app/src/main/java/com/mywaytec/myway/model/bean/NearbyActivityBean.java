package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shemh on 2017/8/10.
 */

public class NearbyActivityBean extends BaseInfo implements Serializable{

    private List<ObjBean> obj;

    public List<ObjBean> getObj() {
        return obj;
    }

    public void setObj(List<ObjBean> obj) {
        this.obj = obj;
    }

    public static class ObjBean implements Serializable{
        /**
         * id : 4
         * lid : 2
         * level : 1
         * title : 测试标题
         * intro : 这是测试
         * createTime : 2017-07-27 15:43:48
         * start : 2017-03-27 13:24:55
         * end : 2017-06-27 13:24:55
         * contact : 13717138842
         * num : 10
         * distance : 63236.9
         * photos : null
         * location : {"id":2,"latitude":22.673478,"longitude":114.066569,"country":"","province":"广东省","city":"深圳市","district":"","street":"","streetnum":"","citycode":"0755","adcode":"440309","aoiname":"","createTime":"2017-07-27 14:09:59"}
         * currentNum : 1
         */

        private int id;
        private int lid;
        private int level;
        private String title;
        private String intro;
        private String createTime;
        private String start;
        private String end;
        private String contact;
        private int num;
        private double distance;
        private List<String> photos;
        private LocationBean location;
        private int currentNum;
        private boolean isSign;
        private boolean isParticipant;
        private boolean isLanucher;

        public boolean isLanucher() {
            return isLanucher;
        }

        public void setLanucher(boolean lanucher) {
            isLanucher = lanucher;
        }

        public boolean isParticipant() {
            return isParticipant;
        }

        public void setParticipant(boolean participant) {
            isParticipant = participant;
        }

        public boolean isSign() {
            return isSign;
        }

        public void setSign(boolean sign) {
            isSign = sign;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLid() {
            return lid;
        }

        public void setLid(int lid) {
            this.lid = lid;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public List<String> getPhotos() {
            return photos;
        }

        public void setPhotos(List<String> photos) {
            this.photos = photos;
        }

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public int getCurrentNum() {
            return currentNum;
        }

        public void setCurrentNum(int currentNum) {
            this.currentNum = currentNum;
        }

        public static class LocationBean implements Serializable{
            /**
             * id : 2
             * latitude : 22.673478
             * longitude : 114.066569
             * country :
             * province : 广东省
             * city : 深圳市
             * district :
             * street :
             * streetnum :
             * citycode : 0755
             * adcode : 440309
             * aoiname :
             * createTime : 2017-07-27 14:09:59
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
    }
}
