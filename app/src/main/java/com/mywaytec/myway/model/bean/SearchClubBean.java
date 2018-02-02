package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shemh on 2017/12/4.
 */

public class SearchClubBean extends BaseInfo {


    private List<ObjBean> obj;

    public List<ObjBean> getObj() {
        return obj;
    }

    public void setObj(List<ObjBean> obj) {
        this.obj = obj;
    }

    public static class ObjBean implements Serializable{
        /**
         * groupid : 1
         * rong_gid : mw_1
         * groupname : 曼威车友交流
         * description : 各种骑车技术尽情分享
         * createTime : 2017-11-21 15:47:09
         * country : 中国
         * province : 深圳
         * city : 龙华
         * isOfficial : true
         * imgUrl : http://120.77.249.52/group1/M00/00/0B/rBKUqFoWf5CAasSaAAFcAG36RMM548.jpg
         * users : null
         * userNum : 3
         */

        private int groupid;
        private String rong_gid;
        private String groupname;
        private String description;
        private String createTime;
        private String country;
        private String province;
        private String city;
        private boolean isOfficial;
        private String imgUrl;
        private Object users;
        private int userNum;

        public boolean isOfficial() {
            return isOfficial;
        }

        public void setOfficial(boolean official) {
            isOfficial = official;
        }

        public int getGroupid() {
            return groupid;
        }

        public void setGroupid(int groupid) {
            this.groupid = groupid;
        }

        public String getRong_gid() {
            return rong_gid;
        }

        public void setRong_gid(String rong_gid) {
            this.rong_gid = rong_gid;
        }

        public String getGroupname() {
            return groupname;
        }

        public void setGroupname(String groupname) {
            this.groupname = groupname;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
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

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public Object getUsers() {
            return users;
        }

        public void setUsers(Object users) {
            this.users = users;
        }

        public int getUserNum() {
            return userNum;
        }

        public void setUserNum(int userNum) {
            this.userNum = userNum;
        }
    }
}
