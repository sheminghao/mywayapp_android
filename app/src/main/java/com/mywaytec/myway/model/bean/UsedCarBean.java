package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

import java.util.List;

/**
 * Created by shemh on 2017/7/14.
 */

public class UsedCarBean extends BaseInfo {


    private List<ObjBean> obj;

    public List<ObjBean> getObj() {
        return obj;
    }

    public void setObj(List<ObjBean> obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        /**
         * id : 3
         * series : RA01
         * configure : null
         * name : 22E9C50D
         * password : 0000
         * snCode : ABCD3
         * uid : da2314b35afd11e78dd300163e064d3e
         * createTime : 2017-07-04 17:15:27
         * todayMileage : 0
         * weekMileage : 1200
         * monthMileage : 6000
         * yearMileage : 6000
         * totalMileage : 6000
         */

        private int id;
        private String series;
        private Object configure;
        private String name;
        private String password;
        private String snCode;
        private String uid;
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

        public Object getConfigure() {
            return configure;
        }

        public void setConfigure(Object configure) {
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

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
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
