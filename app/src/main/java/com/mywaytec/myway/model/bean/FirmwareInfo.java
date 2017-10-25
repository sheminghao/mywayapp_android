package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

import java.util.List;

/**
 * Created by shemh on 2017/6/21.
 */

public class FirmwareInfo extends BaseInfo{


    /**
     * obj : {"id":30,"version":0,"createTime":"2017-08-29 14:18:09","uid":"*","vehicleVersion":"SC01","firmwareVersion":101,"description":"描述：。。。","user":{"uid":"e8fbd8fd0f6b11e78cff3497f69570f3","phonenumber":"13717138840","countrycode":"86","email":"","password":"*","type":0,"level":2,"isactive":true,"isonline":true,"token":"*","signature":"就是这么吊","nickname":"myway_7jx38840","username":"","gender":true,"birthday":"2016-06-06","profession":"","address":"","imgeUrl":"http://120.77.249.52/group1/M00/00/04/rBKUqFlTIbCAXEq2AAD272CZeX4722.png","referralcode":null,"lastlogindate":"2017-08-30 12:26:31","registerdate":"2017-03-23 12:33:45","oauthUid":null,"space2":null,"integral":39,"glod":31,"isDel":false,"authType":1},"files":[{"id":7,"groupName":"group1","fileName":"M00/00/08/rBKUqFmlByCATeUtAABM22WVaSw700.bin","fileType":0,"fVersion":"master","fId":30,"url":"http://120.77.249.52/group1/M00/00/08/rBKUqFmlByCATeUtAABM22WVaSw700.bin"},{"id":8,"groupName":"group1","fileName":"M00/00/08/rBKUqFmlByCAJ5O0AABIHApM7bo086.bin","fileType":0,"fVersion":"slave01","fId":30,"url":"http://120.77.249.52/group1/M00/00/08/rBKUqFmlByCAJ5O0AABIHApM7bo086.bin"}]}
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
         * id : 30
         * version : 0
         * createTime : 2017-08-29 14:18:09
         * uid : *
         * vehicleVersion : SC01
         * firmwareVersion : 101
         * description : 描述：。。。
         * user : {"uid":"e8fbd8fd0f6b11e78cff3497f69570f3","phonenumber":"13717138840","countrycode":"86","email":"","password":"*","type":0,"level":2,"isactive":true,"isonline":true,"token":"*","signature":"就是这么吊","nickname":"myway_7jx38840","username":"","gender":true,"birthday":"2016-06-06","profession":"","address":"","imgeUrl":"http://120.77.249.52/group1/M00/00/04/rBKUqFlTIbCAXEq2AAD272CZeX4722.png","referralcode":null,"lastlogindate":"2017-08-30 12:26:31","registerdate":"2017-03-23 12:33:45","oauthUid":null,"space2":null,"integral":39,"glod":31,"isDel":false,"authType":1}
         * files : [{"id":7,"groupName":"group1","fileName":"M00/00/08/rBKUqFmlByCATeUtAABM22WVaSw700.bin","fileType":0,"fVersion":"master","fId":30,"url":"http://120.77.249.52/group1/M00/00/08/rBKUqFmlByCATeUtAABM22WVaSw700.bin"},{"id":8,"groupName":"group1","fileName":"M00/00/08/rBKUqFmlByCAJ5O0AABIHApM7bo086.bin","fileType":0,"fVersion":"slave01","fId":30,"url":"http://120.77.249.52/group1/M00/00/08/rBKUqFmlByCAJ5O0AABIHApM7bo086.bin"}]
         */

        private int id;
        private int version;
        private String createTime;
        private String uid;
        private String vehicleVersion;
        private int firmwareVersion;
        private String description;
        private UserBean user;
        private List<FilesBean> files;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getVehicleVersion() {
            return vehicleVersion;
        }

        public void setVehicleVersion(String vehicleVersion) {
            this.vehicleVersion = vehicleVersion;
        }

        public int getFirmwareVersion() {
            return firmwareVersion;
        }

        public void setFirmwareVersion(int firmwareVersion) {
            this.firmwareVersion = firmwareVersion;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public List<FilesBean> getFiles() {
            return files;
        }

        public void setFiles(List<FilesBean> files) {
            this.files = files;
        }

        public static class UserBean {
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
             * lastlogindate : 2017-08-30 12:26:31
             * registerdate : 2017-03-23 12:33:45
             * oauthUid : null
             * space2 : null
             * integral : 39
             * glod : 31
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

        public static class FilesBean {
            /**
             * id : 7
             * groupName : group1
             * fileName : M00/00/08/rBKUqFmlByCATeUtAABM22WVaSw700.bin
             * fileType : 0
             * fVersion : master
             * fId : 30
             * url : http://120.77.249.52/group1/M00/00/08/rBKUqFmlByCATeUtAABM22WVaSw700.bin
             */

            private int id;
            private String groupName;
            private String fileName;
            private int fileType;
            private String fVersion;
            private int fId;
            private String url;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getGroupName() {
                return groupName;
            }

            public void setGroupName(String groupName) {
                this.groupName = groupName;
            }

            public String getFileName() {
                return fileName;
            }

            public void setFileName(String fileName) {
                this.fileName = fileName;
            }

            public int getFileType() {
                return fileType;
            }

            public void setFileType(int fileType) {
                this.fileType = fileType;
            }

            public String getFVersion() {
                return fVersion;
            }

            public void setFVersion(String fVersion) {
                this.fVersion = fVersion;
            }

            public int getFId() {
                return fId;
            }

            public void setFId(int fId) {
                this.fId = fId;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
