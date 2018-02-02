package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

/**
 * Created by shemh on 2017/12/6.
 */

public class ClubMessageBean extends BaseInfo {

    /**
     * obj : {"createTime":"2017-12-04 13:11:28","status":0,"user":{"uid":"1b593d66347e11e7aa3f00163e064d3e","phonenumber":"18075746988","countrycode":"86","email":"","type":0,"level":0,"isactive":true,"isonline":true,"token":"OWE2ZTFmNmItYzkzNy00MmJkLWI0OTUtZjk0MWE4ZDkyZjRm","signature":"做更好的自己","nickname":"Sunny","username":"唐","gender":true,"birthday":"2017-01-28","profession":"设计","address":"北京 通州","imgeUrl":"aab74ae4651b11e78dd300163e064d3e","lastlogindate":"2017-11-29 13:44:34","registerdate":"2017-05-09 14:09:49","oauthUid":null,"integral":29,"glod":22,"authType":0},"club":{"groupid":1,"rong_gid":null,"groupname":"曼威车友交流","description":"各种骑车技术尽情分享","createTime":"2017-11-21 15:47:09","country":"中国","province":"深圳","city":"龙华","isOfficial":true,"imgUrl":null,"users":null,"userNum":5}}
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
         * createTime : 2017-12-04 13:11:28
         * status : 0
         * user : {"uid":"1b593d66347e11e7aa3f00163e064d3e","phonenumber":"18075746988","countrycode":"86","email":"","type":0,"level":0,"isactive":true,"isonline":true,"token":"OWE2ZTFmNmItYzkzNy00MmJkLWI0OTUtZjk0MWE4ZDkyZjRm","signature":"做更好的自己","nickname":"Sunny","username":"唐","gender":true,"birthday":"2017-01-28","profession":"设计","address":"北京 通州","imgeUrl":"aab74ae4651b11e78dd300163e064d3e","lastlogindate":"2017-11-29 13:44:34","registerdate":"2017-05-09 14:09:49","oauthUid":null,"integral":29,"glod":22,"authType":0}
         * club : {"groupid":1,"rong_gid":null,"groupname":"曼威车友交流","description":"各种骑车技术尽情分享","createTime":"2017-11-21 15:47:09","country":"中国","province":"深圳","city":"龙华","isOfficial":true,"imgUrl":null,"users":null,"userNum":5}
         */

        private String createTime;
        private int status;
        private UserBean user;
        private ClubBean club;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public ClubBean getClub() {
            return club;
        }

        public void setClub(ClubBean club) {
            this.club = club;
        }

        public static class UserBean {
            /**
             * uid : 1b593d66347e11e7aa3f00163e064d3e
             * phonenumber : 18075746988
             * countrycode : 86
             * email :
             * type : 0
             * level : 0
             * isactive : true
             * isonline : true
             * token : OWE2ZTFmNmItYzkzNy00MmJkLWI0OTUtZjk0MWE4ZDkyZjRm
             * signature : 做更好的自己
             * nickname : Sunny
             * username : 唐
             * gender : true
             * birthday : 2017-01-28
             * profession : 设计
             * address : 北京 通州
             * imgeUrl : aab74ae4651b11e78dd300163e064d3e
             * lastlogindate : 2017-11-29 13:44:34
             * registerdate : 2017-05-09 14:09:49
             * oauthUid : null
             * integral : 29
             * glod : 22
             * authType : 0
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
            private String birthday;
            private String profession;
            private String address;
            private String imgeUrl;
            private String lastlogindate;
            private String registerdate;
            private Object oauthUid;
            private int integral;
            private int glod;
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
        }

        public static class ClubBean {
            /**
             * groupid : 1
             * rong_gid : null
             * groupname : 曼威车友交流
             * description : 各种骑车技术尽情分享
             * createTime : 2017-11-21 15:47:09
             * country : 中国
             * province : 深圳
             * city : 龙华
             * isOfficial : true
             * imgUrl : null
             * users : null
             * userNum : 5
             */

            private int groupid;
            private Object rong_gid;
            private String groupname;
            private String description;
            private String createTime;
            private String country;
            private String province;
            private String city;
            private boolean isOfficial;
            private Object imgUrl;
            private Object users;
            private int userNum;

            public int getGroupid() {
                return groupid;
            }

            public void setGroupid(int groupid) {
                this.groupid = groupid;
            }

            public Object getRong_gid() {
                return rong_gid;
            }

            public void setRong_gid(Object rong_gid) {
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

            public boolean isIsOfficial() {
                return isOfficial;
            }

            public void setIsOfficial(boolean isOfficial) {
                this.isOfficial = isOfficial;
            }

            public Object getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(Object imgUrl) {
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
}
