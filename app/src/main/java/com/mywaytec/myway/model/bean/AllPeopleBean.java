package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

import java.util.List;

/**
 * Created by shemh on 2017/8/15.
 */

public class AllPeopleBean extends BaseInfo{

    /**
     * msg : null
     * obj : {"leader":{"uid":"56c44d794f1a11e78dd300163e064d3e","phonenumber":"","countrycode":null,"email":"","password":"*","type":2,"level":0,"isactive":true,"isonline":true,"token":"*","signature":"前方路灯不亮，试着换个方向。莫紧张，莫迷茫。","nickname":"q","username":"","gender":true,"birthday":null,"profession":"","address":"","imgeUrl":"http://120.77.249.52/group1/M00/00/04/rBKUqFk-Aq2ATY50AAFek7Xvvbo645.png","referralcode":null,"lastlogindate":"2017-08-11 11:02:04","registerdate":"2017-06-12 10:53:40","oauthUid":"AE06F627A1E15AD0B8A9F32228241231","space2":null,"integral":64,"glod":48,"isDel":false,"authType":0},"participant":[{"uid":"e8fbd8fd0f6b11e78cff3497f69570f3","phonenumber":"13717138840","countrycode":"86","email":"","password":"*","type":0,"level":2,"isactive":true,"isonline":true,"token":"*","signature":"就是这么吊","nickname":"myway_7jx38840","username":"","gender":true,"birthday":"2016-06-06","profession":"","address":"","imgeUrl":"http://120.77.249.52/group1/M00/00/04/rBKUqFlTIbCAXEq2AAD272CZeX4722.png","referralcode":null,"lastlogindate":"2017-08-10 11:22:05","registerdate":"2017-03-23 12:33:45","oauthUid":null,"space2":null,"integral":38,"glod":30,"isDel":false,"authType":1}]}
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
         * leader : {"uid":"56c44d794f1a11e78dd300163e064d3e","phonenumber":"","countrycode":null,"email":"","password":"*","type":2,"level":0,"isactive":true,"isonline":true,"token":"*","signature":"前方路灯不亮，试着换个方向。莫紧张，莫迷茫。","nickname":"q","username":"","gender":true,"birthday":null,"profession":"","address":"","imgeUrl":"http://120.77.249.52/group1/M00/00/04/rBKUqFk-Aq2ATY50AAFek7Xvvbo645.png","referralcode":null,"lastlogindate":"2017-08-11 11:02:04","registerdate":"2017-06-12 10:53:40","oauthUid":"AE06F627A1E15AD0B8A9F32228241231","space2":null,"integral":64,"glod":48,"isDel":false,"authType":0}
         * participant : [{"uid":"e8fbd8fd0f6b11e78cff3497f69570f3","phonenumber":"13717138840","countrycode":"86","email":"","password":"*","type":0,"level":2,"isactive":true,"isonline":true,"token":"*","signature":"就是这么吊","nickname":"myway_7jx38840","username":"","gender":true,"birthday":"2016-06-06","profession":"","address":"","imgeUrl":"http://120.77.249.52/group1/M00/00/04/rBKUqFlTIbCAXEq2AAD272CZeX4722.png","referralcode":null,"lastlogindate":"2017-08-10 11:22:05","registerdate":"2017-03-23 12:33:45","oauthUid":null,"space2":null,"integral":38,"glod":30,"isDel":false,"authType":1}]
         */

        private LeaderBean leader;
        private List<ParticipantBean> participant;

        public LeaderBean getLeader() {
            return leader;
        }

        public void setLeader(LeaderBean leader) {
            this.leader = leader;
        }

        public List<ParticipantBean> getParticipant() {
            return participant;
        }

        public void setParticipant(List<ParticipantBean> participant) {
            this.participant = participant;
        }

        public static class LeaderBean {
            /**
             * uid : 56c44d794f1a11e78dd300163e064d3e
             * phonenumber :
             * countrycode : null
             * email :
             * password : *
             * type : 2
             * level : 0
             * isactive : true
             * isonline : true
             * token : *
             * signature : 前方路灯不亮，试着换个方向。莫紧张，莫迷茫。
             * nickname : q
             * username :
             * gender : true
             * birthday : null
             * profession :
             * address :
             * imgeUrl : http://120.77.249.52/group1/M00/00/04/rBKUqFk-Aq2ATY50AAFek7Xvvbo645.png
             * referralcode : null
             * lastlogindate : 2017-08-11 11:02:04
             * registerdate : 2017-06-12 10:53:40
             * oauthUid : AE06F627A1E15AD0B8A9F32228241231
             * space2 : null
             * integral : 64
             * glod : 48
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
            private Object birthday;
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

        public static class ParticipantBean {
            /**
             * uid : e8fbd8fd0f6b11e78cff3497f69570f3
             * phonenumber : 13717138840
             * countrycode : 86
             * email :
             * password : *
             * type : 0
             * level : 2
             * isactive : true
             * isonline : true
             * token : *
             * signature : 就是这么吊
             * nickname : myway_7jx38840
             * username :
             * gender : true
             * birthday : 2016-06-06
             * profession :
             * address :
             * imgeUrl : http://120.77.249.52/group1/M00/00/04/rBKUqFlTIbCAXEq2AAD272CZeX4722.png
             * referralcode : null
             * lastlogindate : 2017-08-10 11:22:05
             * registerdate : 2017-03-23 12:33:45
             * oauthUid : null
             * space2 : null
             * integral : 38
             * glod : 30
             * isDel : false
             * authType : 1
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
            private Object referralcode;
            private String lastlogindate;
            private String registerdate;
            private Object oauthUid;
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
        }
    }
}
