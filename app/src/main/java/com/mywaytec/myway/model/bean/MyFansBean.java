package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

import java.util.List;

/**
 * Created by shemh on 2017/12/20.
 */

public class MyFansBean extends BaseInfo {

    private List<ObjBean> obj;

    public List<ObjBean> getObj() {
        return obj;
    }

    public void setObj(List<ObjBean> obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        /**
         * state : 0
         * is_mutual : true
         * user : {"uid":"c24de8f7a32a11e78dd300163e064d3e","phonenumber":"10000000002","countrycode":"86","email":"","type":0,"level":0,"isactive":true,"isonline":true,"token":"*","signature":"","nickname":"myway_uuld0002","username":"","gender":true,"birthday":null,"profession":"","address":"","imgeUrl":"http://120.77.249.52/group1/M00/00/0B/rBKUqFoWjjuAGMEoAAFgr5umynQ258.png","lastlogindate":"2017-12-18 08:45:58","registerdate":"2017-09-27 10:22:47","oauthUid":null,"integral":45,"glod":41,"authType":0,"globalState":0}
         */

        private int state;
        private boolean is_mutual;
        private UserBean user;

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public boolean isIs_mutual() {
            return is_mutual;
        }

        public void setIs_mutual(boolean is_mutual) {
            this.is_mutual = is_mutual;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * uid : c24de8f7a32a11e78dd300163e064d3e
             * phonenumber : 10000000002
             * countrycode : 86
             * email :
             * type : 0
             * level : 0
             * isactive : true
             * isonline : true
             * token : *
             * signature :
             * nickname : myway_uuld0002
             * username :
             * gender : true
             * birthday : null
             * profession :
             * address :
             * imgeUrl : http://120.77.249.52/group1/M00/00/0B/rBKUqFoWjjuAGMEoAAFgr5umynQ258.png
             * lastlogindate : 2017-12-18 08:45:58
             * registerdate : 2017-09-27 10:22:47
             * oauthUid : null
             * integral : 45
             * glod : 41
             * authType : 0
             * globalState : 0
             */

            private String uid;
            private String phonenumber;
            private String countrycode;
            private String email;
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
            private String lastlogindate;
            private String registerdate;
            private Object oauthUid;
            private int integral;
            private int glod;
            private int authType;
            private int globalState;

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

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
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

            public Object getOauthUid() {
                return oauthUid;
            }

            public void setOauthUid(Object oauthUid) {
                this.oauthUid = oauthUid;
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

            public int getAuthType() {
                return authType;
            }

            public void setAuthType(int authType) {
                this.authType = authType;
            }

            public int getGlobalState() {
                return globalState;
            }

            public void setGlobalState(int globalState) {
                this.globalState = globalState;
            }
        }
    }
}
