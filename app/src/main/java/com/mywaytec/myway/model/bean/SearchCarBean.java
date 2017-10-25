package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

/**
 * Created by shemh on 2017/7/25.
 */

public class SearchCarBean extends BaseInfo {


    /**
     * obj : {"id":7,"series":"RA01","configure":"AR","name":"2AB56CDA1","password":"1234","snCode":"ABCD4","uid":null,"createTime":"2017-07-12 17:42:14","todayMileage":0,"weekMileage":0,"monthMileage":0,"yearMileage":0,"totalMileage":0}
     */

    private ObjBean obj;

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        /**
         * id : 7
         * series : RA01
         * configure : AR
         * name : 2AB56CDA1
         * password : 1234
         * snCode : ABCD4
         * uid : null
         * createTime : 2017-07-12 17:42:14
         * todayMileage : 0
         * weekMileage : 0
         * monthMileage : 0
         * yearMileage : 0
         * totalMileage : 0
         */

        private int id;
        private String series;
        private String configure;
        private String name;
        private String password;
        private String snCode;
        private Object uid;
        private String createTime;
        private int todayMileage;
        private int weekMileage;
        private int monthMileage;
        private int yearMileage;
        private int totalMileage;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSeries() {
            return series;
        }

        public void setSeries(String series) {
            this.series = series;
        }

        public String getConfigure() {
            return configure;
        }

        public void setConfigure(String configure) {
            this.configure = configure;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getSnCode() {
            return snCode;
        }

        public void setSnCode(String snCode) {
            this.snCode = snCode;
        }

        public Object getUid() {
            return uid;
        }

        public void setUid(Object uid) {
            this.uid = uid;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getTodayMileage() {
            return todayMileage;
        }

        public void setTodayMileage(int todayMileage) {
            this.todayMileage = todayMileage;
        }

        public int getWeekMileage() {
            return weekMileage;
        }

        public void setWeekMileage(int weekMileage) {
            this.weekMileage = weekMileage;
        }

        public int getMonthMileage() {
            return monthMileage;
        }

        public void setMonthMileage(int monthMileage) {
            this.monthMileage = monthMileage;
        }

        public int getYearMileage() {
            return yearMileage;
        }

        public void setYearMileage(int yearMileage) {
            this.yearMileage = yearMileage;
        }

        public int getTotalMileage() {
            return totalMileage;
        }

        public void setTotalMileage(int totalMileage) {
            this.totalMileage = totalMileage;
        }
    }
}
