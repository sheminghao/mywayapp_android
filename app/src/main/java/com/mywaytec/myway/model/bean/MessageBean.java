package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

import java.util.List;

/**
 * Created by shemh on 2017/5/3.
 */

public class MessageBean extends BaseInfo{

    //type: 1.系统消息 2.评论消息 3.点赞消息 4.回复消息 5.广告消息

    private List<ObjBean> obj;

    public List<ObjBean> getObj() {
        return obj;
    }

    public void setObj(List<ObjBean> obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        /**
         * id : 121
         * title : 有人为你点赞
         * text : null
         * createTime : 2017-05-16 08:52:02
         * type : 2
         * fromUid : 796f11962bd111e7aa3f00163e064d3e
         * toUid : 5f86efdb1e7e11e7bc8600163e064d3e
         * isReaded : false
         * shId : 153
         * fromUser : {"uid":"796f11962bd111e7aa3f00163e064d3e","phonenumber":null,"countrycode":null,"email":null,"password":"*","type":3,"level":0,"isactive":true,"isonline":true,"token":"*","signature":"My way,My life!","nickname":"請說：''先生，你好！''","username":"hasaki","gender":true,"birthday":"1970-01-01","profession":"IT","address":null,"imgeUrl":"http://wx.qlogo.cn/mmopen/PiajxSqBRaEIyR2iaXnyDbDOI8uSBy6qGbHicibNHex3T3LwX9KyVL8pEBU0o8cPtk1eVmQTD7HIRmkVCpb2mMbW6w/0","referralcode":null,"lastlogindate":"2017-05-15 20:52:56","registerdate":"2017-04-28 13:13:54","oauthUid":"oOh1qw0lFVxhvvvn2MPZB-7Wlx2g","space2":null,"integral":0,"glod":0,"isDel":false}
         */

        private int id;
        private String title;
        private String text;
        private String createTime;
        private int type;
        private String fromUid;
        private String toUid;
        private boolean isReaded;
        private int shId;
        private FromUserBean fromUser;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getFromUid() {
            return fromUid;
        }

        public void setFromUid(String fromUid) {
            this.fromUid = fromUid;
        }

        public String getToUid() {
            return toUid;
        }

        public void setToUid(String toUid) {
            this.toUid = toUid;
        }

        public boolean isIsReaded() {
            return isReaded;
        }

        public void setIsReaded(boolean isReaded) {
            this.isReaded = isReaded;
        }

        public int getShId() {
            return shId;
        }

        public void setShId(int shId) {
            this.shId = shId;
        }

        public FromUserBean getFromUser() {
            return fromUser;
        }

        public void setFromUser(FromUserBean fromUser) {
            this.fromUser = fromUser;
        }

        public static class FromUserBean {
            /**
             * uid : 796f11962bd111e7aa3f00163e064d3e
             * phonenumber : null
             * countrycode : null
             * email : null
             * password : *
             * type : 3
             * level : 0
             * isactive : true
             * isonline : true
             * token : *
             * signature : My way,My life!
             * nickname : 請說：''先生，你好！''
             * username : hasaki
             * gender : true
             * birthday : 1970-01-01
             * profession : IT
             * address : null
             * imgeUrl : http://wx.qlogo.cn/mmopen/PiajxSqBRaEIyR2iaXnyDbDOI8uSBy6qGbHicibNHex3T3LwX9KyVL8pEBU0o8cPtk1eVmQTD7HIRmkVCpb2mMbW6w/0
             * referralcode : null
             * lastlogindate : 2017-05-15 20:52:56
             * registerdate : 2017-04-28 13:13:54
             * oauthUid : oOh1qw0lFVxhvvvn2MPZB-7Wlx2g
             * space2 : null
             * integral : 0
             * glod : 0
             * isDel : false
             */

            private String uid;
            private Object phonenumber;
            private Object countrycode;
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
            private String birthday;
            private String profession;
            private Object address;
            private String imgeUrl;
            private Object referralcode;
            private String lastlogindate;
            private String registerdate;
            private String oauthUid;
            private Object space2;
            private int integral;
            private int glod;
            private boolean isDel;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public Object getPhonenumber() {
                return phonenumber;
            }

            public void setPhonenumber(Object phonenumber) {
                this.phonenumber = phonenumber;
            }

            public Object getCountrycode() {
                return countrycode;
            }

            public void setCountrycode(Object countrycode) {
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

            public Object getAddress() {
                return address;
            }

            public void setAddress(Object address) {
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
        }
    }
}
