package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

import java.util.List;

/**
 * Created by shemh on 2017/4/17.
 */

public class LikeListBean extends BaseInfo {

    private List<ObjBean> obj;

    public List<ObjBean> getObj() {
        return obj;
    }

    public void setObj(List<ObjBean> obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        /**
         * id : 17
         * uid : 5f86efdb1e7e11e7bc8600163e064d3e
         * lastOperationTime : 2017-04-25 18:21:59
         * shId : 114
         * islike : true
         * user : {"uid":"5f86efdb1e7e11e7bc8600163e064d3e","phonenumber":"18664565413","countrycode":"86","email":null,"password":"*","type":0,"level":0,"isactive":true,"isonline":true,"token":"*","signature":"","nickname":"myway_kibj5413","username":"","gender":true,"birthday":null,"profession":"","address":"","imgeUrl":"http://120.77.249.52/group1/M00/00/01/rBKUqFj-5ROASWBgAAGGzuFPlGk161.jpg","referralcode":null,"lastlogindate":"2017-04-25 18:48:39","registerdate":"2017-04-11 14:16:18","space1":null,"space2":null}
         */

        private int id;
        private String uid;
        private String lastOperationTime;
        private int shId;
        private boolean islike;
        private UserBean user;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getLastOperationTime() {
            return lastOperationTime;
        }

        public void setLastOperationTime(String lastOperationTime) {
            this.lastOperationTime = lastOperationTime;
        }

        public int getShId() {
            return shId;
        }

        public void setShId(int shId) {
            this.shId = shId;
        }

        public boolean isIslike() {
            return islike;
        }

        public void setIslike(boolean islike) {
            this.islike = islike;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
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
             * signature :
             * nickname : myway_kibj5413
             * username :
             * gender : true
             * birthday : null
             * profession :
             * address :
             * imgeUrl : http://120.77.249.52/group1/M00/00/01/rBKUqFj-5ROASWBgAAGGzuFPlGk161.jpg
             * referralcode : null
             * lastlogindate : 2017-04-25 18:48:39
             * registerdate : 2017-04-11 14:16:18
             * space1 : null
             * space2 : null
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
        }
    }
}
