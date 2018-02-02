package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shemh on 2017/12/5.
 */

public class ClubDetailBean extends BaseInfo{

    /**
     * obj : {"groupid":1,"groupname":"俱乐部测试一下","uid":"e8fbd8fd0f6b11e78cff3497f69570f3","description":"俱乐部测试","country":"中国","province":"深圳","city":"龙华","fid":null,"state":0,"isOfficial":false,"imgUrl":"http://120.77.249.52/group1/M00/00/0B/rBKUqFoWf5CAasSaAAFcAG36RMM548.jpg","users":[{"uid":"da2314b35afd11e78dd300163e064d3e","phonenumber":"13717138842","countrycode":"86","email":"","password":"*","type":0,"level":0,"isactive":true,"isonline":true,"token":"*","signature":"","nickname":"myway_wojm8842","username":"","gender":false,"birthday":null,"profession":"","address":"","imgeUrl":null,"referralcode":null,"lastlogindate":"2017-11-27 09:07:11","registerdate":"2017-06-27 13:59:59","oauthUid":null,"space2":null,"integral":1,"glod":1,"isDel":false,"authType":0},{"uid":"e8fbd8fd0f6b11e78cff3497f69570f3","phonenumber":"13717138840","countrycode":"86","email":"","password":"*","type":0,"level":2,"isactive":true,"isonline":true,"token":"*","signature":"就是这么吊","nickname":"myway_7jx38840","username":"","gender":true,"birthday":"1972-02-29","profession":"","address":"","imgeUrl":"http://120.77.249.52/group1/M00/00/08/rBKUqFmmbtmAS5KJAAEqOUPVYuk220.png","referralcode":null,"lastlogindate":"2017-11-27 15:07:52","registerdate":"2017-03-23 12:33:45","oauthUid":null,"space2":null,"integral":94,"glod":63,"isDel":false,"authType":0}]}
     */

    private ObjBean obj;

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
        this.obj = obj;
    }

    public static class ObjBean implements Serializable{
        /**
         * groupid : 1
         * groupname : 俱乐部测试一下
         * uid : e8fbd8fd0f6b11e78cff3497f69570f3
         * description : 俱乐部测试
         * country : 中国
         * province : 深圳
         * city : 龙华
         * fid : null
         * state : 0
         * isOfficial : false
         * imgUrl : http://120.77.249.52/group1/M00/00/0B/rBKUqFoWf5CAasSaAAFcAG36RMM548.jpg
         * users : [{"uid":"da2314b35afd11e78dd300163e064d3e","phonenumber":"13717138842","countrycode":"86","email":"","password":"*","type":0,"level":0,"isactive":true,"isonline":true,"token":"*","signature":"","nickname":"myway_wojm8842","username":"","gender":false,"birthday":null,"profession":"","address":"","imgeUrl":null,"referralcode":null,"lastlogindate":"2017-11-27 09:07:11","registerdate":"2017-06-27 13:59:59","oauthUid":null,"space2":null,"integral":1,"glod":1,"isDel":false,"authType":0},{"uid":"e8fbd8fd0f6b11e78cff3497f69570f3","phonenumber":"13717138840","countrycode":"86","email":"","password":"*","type":0,"level":2,"isactive":true,"isonline":true,"token":"*","signature":"就是这么吊","nickname":"myway_7jx38840","username":"","gender":true,"birthday":"1972-02-29","profession":"","address":"","imgeUrl":"http://120.77.249.52/group1/M00/00/08/rBKUqFmmbtmAS5KJAAEqOUPVYuk220.png","referralcode":null,"lastlogindate":"2017-11-27 15:07:52","registerdate":"2017-03-23 12:33:45","oauthUid":null,"space2":null,"integral":94,"glod":63,"isDel":false,"authType":0}]
         */

        private int groupid;
        private String groupname;
        private String uid;
        private String description;
        private String country;
        private String province;
        private String city;
        private Object fid;
        private int state;
        private boolean isOfficial;
        private String imgUrl;
        private List<UsersBean> users;

        public int getGroupid() {
            return groupid;
        }

        public void setGroupid(int groupid) {
            this.groupid = groupid;
        }

        public String getGroupname() {
            return groupname;
        }

        public void setGroupname(String groupname) {
            this.groupname = groupname;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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

        public Object getFid() {
            return fid;
        }

        public void setFid(Object fid) {
            this.fid = fid;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public boolean isIsOfficial() {
            return isOfficial;
        }

        public void setIsOfficial(boolean isOfficial) {
            this.isOfficial = isOfficial;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public List<UsersBean> getUsers() {
            return users;
        }

        public void setUsers(List<UsersBean> users) {
            this.users = users;
        }

        public static class UsersBean implements Serializable{
            /**
             * uid : da2314b35afd11e78dd300163e064d3e
             * phonenumber : 13717138842
             * countrycode : 86
             * email :
             * password : *
             * type : 0
             * level : 0
             * isactive : true
             * isonline : true
             * token : *
             * signature :
             * nickname : myway_wojm8842
             * username :
             * gender : false
             * birthday : null
             * profession :
             * address :
             * imgeUrl : null
             * referralcode : null
             * lastlogindate : 2017-11-27 09:07:11
             * registerdate : 2017-06-27 13:59:59
             * oauthUid : null
             * space2 : null
             * integral : 1
             * glod : 1
             * isDel : false
             * authType : 0
             */

            private String uid;
            private String phonenumber;
            private String countrycode;
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
            private Object birthday;
            private String profession;
            private String address;
            private Object imgeUrl;
            private Object referralcode;
            private String lastlogindate;
            private String registerdate;
            private Object oauthUid;
            private Object space2;
            private int integral;
            private int glod;
            private boolean isDel;
            private int authType;
            private int globalState;
            private boolean isDeleteSelect;

            public boolean isDeleteSelect() {
                return isDeleteSelect;
            }

            public void setDeleteSelect(boolean isDeleteSelect) {
                this.isDeleteSelect = isDeleteSelect;
            }

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

            public Object getImgeUrl() {
                return imgeUrl;
            }

            public void setImgeUrl(Object imgeUrl) {
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

            public Object getOauthUid() {
                return oauthUid;
            }

            public void setOauthUid(Object oauthUid) {
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

            public int getGlobalState() {
                return globalState;
            }

            public void setGlobalState(int globalState) {
                this.globalState = globalState;
            }
        }
    }
}
