package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

/**
 * Created by shemh on 2017/4/18.
 */

public class UserInfo extends BaseInfo {


    /**
     * obj : {"uid":"e8fbd8fd0f6b11e78cff3497f69570f3","phonenumber":"13717138840","countrycode":"86","email":null,"password":null,"type":0,"level":0,"isactive":true,"isonline":false,"token":null,"signature":null,"nickname":null,"username":null,"gender":null,"birthday":null,"profession":null,"address":null,"imgeUrl":"group1/M00/00/00/rBKUqFjd9X2AByR8AAAqZ4keEdo452.jpg","referralcode":null,"lastlogindate":"2017-04-05 15:14:03","registerdate":"2017-03-23 12:33:45","space1":null,"space2":null}
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
         * uid : e8fbd8fd0f6b11e78cff3497f69570f3
         * phonenumber : 13717138840
         * countrycode : 86
         * email : null
         * password : null
         * type : 0
         * level : 0
         * isactive : true
         * isonline : false
         * token : null
         * signature : null
         * nickname : null
         * username : null
         * gender : null
         * birthday : null
         * profession : null
         * address : null
         * imgeUrl : group1/M00/00/00/rBKUqFjd9X2AByR8AAAqZ4keEdo452.jpg
         * referralcode : null
         * lastlogindate : 2017-04-05 15:14:03
         * registerdate : 2017-03-23 12:33:45
         * space1 : null
         * space2 : null
         * integral:14,
         * glod:10,
         * isDel:false
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
        private String birthday;
        private String profession;
        private String address;
        private String imgeUrl;
        private String referralcode;
        private String lastlogindate;
        private String registerdate;
        private String space1;
        private String space2;
        private int integral;
        private int glod;
        private boolean isDel;
        private int authType;

        public int getAuthType() {
            return authType;
        }

        public void setAuthType(int authType) {
            this.authType = authType;
        }

        public boolean isGender() {
            return gender;
        }

        public boolean isonline() {
            return isonline;
        }

        public boolean isactive() {
            return isactive;
        }

        public boolean isDel() {
            return isDel;
        }

        public void setDel(boolean del) {
            isDel = del;
        }

        public int getGlod() {
            return glod;
        }

        public void setGlod(int glod) {
            this.glod = glod;
        }

        public int getIntegral() {
            return integral;
        }

        public void setIntegral(int integral) {
            this.integral = integral;
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

        public boolean getGender() {
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

        public String getReferralcode() {
            return referralcode;
        }

        public void setReferralcode(String referralcode) {
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

        public String getSpace1() {
            return space1;
        }

        public void setSpace1(String space1) {
            this.space1 = space1;
        }

        public String getSpace2() {
            return space2;
        }

        public void setSpace2(String space2) {
            this.space2 = space2;
        }
    }
}
