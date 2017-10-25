package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

import java.util.List;

/**
 * Created by shemh on 2017/4/12.
 */

public class CommentListBean extends BaseInfo {


    private List<ObjBean> obj;

    public List<ObjBean> getObj() {
        return obj;
    }

    public void setObj(List<ObjBean> obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        /**
         * cid : 1
         * comContent : 雷猴啊
         * comCreateTime : 1491360149000
         * fromUid : e8fbd8fd0f6b11e78cff3497f69570f3
         * shId : 13
         * parentId : 0
         * toUid : null
         * fromUser : {"uid":"e8fbd8fd0f6b11e78cff3497f69570f3","phonenumber":"13717138840","countrycode":"86","email":null,"password":"*","type":0,"level":0,"isactive":true,"isonline":true,"token":"*","signature":"就是这么吊","nickname":"你猜","username":null,"gender":null,"birthday":null,"profession":null,"address":null,"imgeUrl":"52e9d6ee15da11e7bc8600163e064d3e","referralcode":null,"lastlogindate":1491381007000,"registerdate":1490243625000,"space1":null,"space2":null}
         * toUser : null
         * subComments : []
         */

        private int cid;
        private String comContent;
        private String comCreateTime;
        private String fromUid;
        private int shId;
        private int parentId;
        private Object toUid;
        private FromUserBean fromUser;
        private Object toUser;
        private List<?> subComments;

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public String getComContent() {
            return comContent;
        }

        public void setComContent(String comContent) {
            this.comContent = comContent;
        }

        public String getComCreateTime() {
            return comCreateTime;
        }

        public void setComCreateTime(String comCreateTime) {
            this.comCreateTime = comCreateTime;
        }

        public String getFromUid() {
            return fromUid;
        }

        public void setFromUid(String fromUid) {
            this.fromUid = fromUid;
        }

        public int getShId() {
            return shId;
        }

        public void setShId(int shId) {
            this.shId = shId;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public Object getToUid() {
            return toUid;
        }

        public void setToUid(Object toUid) {
            this.toUid = toUid;
        }

        public FromUserBean getFromUser() {
            return fromUser;
        }

        public void setFromUser(FromUserBean fromUser) {
            this.fromUser = fromUser;
        }

        public Object getToUser() {
            return toUser;
        }

        public void setToUser(Object toUser) {
            this.toUser = toUser;
        }

        public List<?> getSubComments() {
            return subComments;
        }

        public void setSubComments(List<?> subComments) {
            this.subComments = subComments;
        }

        public static class FromUserBean {
            /**
             * uid : e8fbd8fd0f6b11e78cff3497f69570f3
             * phonenumber : 13717138840
             * countrycode : 86
             * email : null
             * password : *
             * type : 0
             * level : 0
             * isactive : true
             * isonline : true
             * token : *
             * signature : 就是这么吊
             * nickname : 你猜
             * username : null
             * gender : null
             * birthday : null
             * profession : null
             * address : null
             * imgeUrl : 52e9d6ee15da11e7bc8600163e064d3e
             * referralcode : null
             * lastlogindate : 1491381007000
             * registerdate : 1490243625000
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
            private String birthday;
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
