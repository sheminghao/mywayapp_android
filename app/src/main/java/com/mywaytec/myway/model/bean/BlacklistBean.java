package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

import java.util.List;

/**
 * Created by shemh on 2017/7/31.
 */

public class BlacklistBean extends BaseInfo {

    private List<ObjBean> obj;

    public List<ObjBean> getObj() {
        return obj;
    }

    public void setObj(List<ObjBean> obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        /**
         * uid : 58ce7ff2364311e7aa3f00163e064d3e
         * shieldUid : 2b3c427c3eca11e78dd300163e064d3e
         * isShield : true
         * shieldType : 0
         * createTime : 2017-08-15 08:55:54
         * user : {"uid":"2b3c427c3eca11e78dd300163e064d3e","phonenumber":"","countrycode":null,"email":"","password":"*","type":3,"level":0,"isactive":true,"isonline":true,"token":"*","signature":"大象电焊钳","nickname":"哦哦哦嗯","username":"","gender":false,"birthday":"2016-06-06","profession":"","address":"","imgeUrl":"http://120.77.249.52/group1/M00/00/07/rBKUqFmNavmANBxaAABsWP4dOA0415.jpg","referralcode":null,"lastlogindate":"2017-08-11 15:49:12","registerdate":"2017-05-22 16:39:29","oauthUid":"oOh1qw0lFVxhvvvn2MPZB-7Wlx2g","space2":null,"integral":109,"glod":57,"isDel":false,"authType":0}
         */

        private String uid;
        private String shieldUid;
        private boolean isShield;
        private int shieldType;
        private String createTime;
        private UserBean user;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getShieldUid() {
            return shieldUid;
        }

        public void setShieldUid(String shieldUid) {
            this.shieldUid = shieldUid;
        }

        public boolean isIsShield() {
            return isShield;
        }

        public void setIsShield(boolean isShield) {
            this.isShield = isShield;
        }

        public int getShieldType() {
            return shieldType;
        }

        public void setShieldType(int shieldType) {
            this.shieldType = shieldType;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * uid : 2b3c427c3eca11e78dd300163e064d3e
             * phonenumber :
             * countrycode : null
             * email :
             * password : *
             * type : 3
             * level : 0
             * isactive : true
             * isonline : true
             * token : *
             * signature : 大象电焊钳
             * nickname : 哦哦哦嗯
             * username :
             * gender : false
             * birthday : 2016-06-06
             * profession :
             * address :
             * imgeUrl : http://120.77.249.52/group1/M00/00/07/rBKUqFmNavmANBxaAABsWP4dOA0415.jpg
             * referralcode : null
             * lastlogindate : 2017-08-11 15:49:12
             * registerdate : 2017-05-22 16:39:29
             * oauthUid : oOh1qw0lFVxhvvvn2MPZB-7Wlx2g
             * space2 : null
             * integral : 109
             * glod : 57
             * isDel : false
             * authType : 0
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
            private int authType;

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

            public int getAuthType() {
                return authType;
            }

            public void setAuthType(int authType) {
                this.authType = authType;
            }
        }
    }
}
