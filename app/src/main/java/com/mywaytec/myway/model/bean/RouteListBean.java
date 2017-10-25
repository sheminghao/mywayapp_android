package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shemh on 2017/5/3.
 */

public class RouteListBean extends BaseInfo {


    private List<ObjBean> obj;

    public List<ObjBean> getObj() {
        return obj;
    }

    public void setObj(List<ObjBean> obj) {
        this.obj = obj;
    }

    public static class ObjBean implements Serializable{
        /**
         * id : 20
         * image : 989807df34ab11e7aa3f00163e064d3e
         * name : 测试
         * sceneryStar : 5
         * difficultyStar : 5
         * legend : 0
         * enduranceClaim : 0
         * intro :
         * origin :
         * originBus :
         * destination :
         * destinationBus :
         * score : null
         * createTime : 2017-05-09 19:35:26
         * title :
         * uid : 5f86efdb1e7e11e7bc8600163e064d3e
         * isSuccess : true
         * city : null
         * likeNum : 0
         * user : {"uid":"5f86efdb1e7e11e7bc8600163e064d3e","phonenumber":"18664565413","countrycode":"86","email":null,"password":"*","type":0,"level":0,"isactive":true,"isonline":true,"token":"*","signature":"123456","nickname":"myway_kibj5413","username":"","gender":true,"birthday":null,"profession":"","address":"","imgeUrl":"http://120.77.249.52/group1/M00/00/02/rBKUqFkKyw2AABiDAAF-CctvA7o851.jpg","referralcode":null,"lastlogindate":"2017-05-11 15:05:56","registerdate":"2017-04-11 14:16:18","oauthUid":null,"space2":null,"integral":0,"glod":0,"isDel":false}
         * paths : [{"id":150,"longitude":113.1,"latitude":22.1,"routeId":20,"createTime":"2017-05-09 19:35:26"},{"id":151,"longitude":113.1,"latitude":22.1,"routeId":20,"createTime":"2017-05-09 19:35:26"},{"id":152,"longitude":113.1,"latitude":22.1,"routeId":20,"createTime":"2017-05-09 19:35:26"},{"id":153,"longitude":113.1,"latitude":22.1,"routeId":20,"createTime":"2017-05-09 19:35:26"},{"id":154,"longitude":113.1,"latitude":22.1,"routeId":20,"createTime":"2017-05-09 19:35:26"},{"id":155,"longitude":113.1,"latitude":22.1,"routeId":20,"createTime":"2017-05-09 19:35:26"},{"id":156,"longitude":113.1,"latitude":22.1,"routeId":20,"createTime":"2017-05-09 19:35:26"},{"id":157,"longitude":113.1,"latitude":22.1,"routeId":20,"createTime":"2017-05-09 19:35:26"},{"id":158,"longitude":113.1,"latitude":22.1,"routeId":20,"createTime":"2017-05-09 19:35:26"},{"id":159,"longitude":113.1,"latitude":22.1,"routeId":20,"createTime":"2017-05-09 19:35:26"}]
         * photos : ["http://120.77.249.52/group1/M00/00/02/rBKUqFkRqX6ACN9LAAFWd3_aBUQ919.png","http://120.77.249.52/group1/M00/00/02/rBKUqFkRqX6AFyKZAAGK1EQTvxA479.png"]
         */

        private int id;
        private String image;
        private String name;
        private int sceneryStar;
        private int difficultyStar;
        private int legend;
        private int enduranceClaim;
        private String intro;
        private String origin;
        private String originBus;
        private String destination;
        private String destinationBus;
        private Object score;
        private String createTime;
        private String title;
        private String uid;
        private boolean isSuccess;
        private String city;
        private int likeNum;
        private boolean isLike;
        private UserBean user;
        private List<PathsBean> paths;
        private List<String> photos;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSceneryStar() {
            return sceneryStar;
        }

        public void setSceneryStar(int sceneryStar) {
            this.sceneryStar = sceneryStar;
        }

        public int getDifficultyStar() {
            return difficultyStar;
        }

        public void setDifficultyStar(int difficultyStar) {
            this.difficultyStar = difficultyStar;
        }

        public int getLegend() {
            return legend;
        }

        public void setLegend(int legend) {
            this.legend = legend;
        }

        public int getEnduranceClaim() {
            return enduranceClaim;
        }

        public void setEnduranceClaim(int enduranceClaim) {
            this.enduranceClaim = enduranceClaim;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getOriginBus() {
            return originBus;
        }

        public void setOriginBus(String originBus) {
            this.originBus = originBus;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public String getDestinationBus() {
            return destinationBus;
        }

        public void setDestinationBus(String destinationBus) {
            this.destinationBus = destinationBus;
        }

        public Object getScore() {
            return score;
        }

        public void setScore(Object score) {
            this.score = score;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public boolean isIsSuccess() {
            return isSuccess;
        }

        public void setIsSuccess(boolean isSuccess) {
            this.isSuccess = isSuccess;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public boolean isLike() {
            return isLike;
        }

        public void setIsLike(boolean isLike) {
            this.isLike = isLike;
        }

        public int getLikeNum() {
            return likeNum;
        }

        public void setLikeNum(int likeNum) {
            this.likeNum = likeNum;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public List<PathsBean> getPaths() {
            return paths;
        }

        public void setPaths(List<PathsBean> paths) {
            this.paths = paths;
        }

        public List<String> getPhotos() {
            return photos;
        }

        public void setPhotos(List<String> photos) {
            this.photos = photos;
        }

        public static class UserBean implements Serializable{
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
             * signature : 123456
             * nickname : myway_kibj5413
             * username :
             * gender : true
             * birthday : null
             * profession :
             * address :
             * imgeUrl : http://120.77.249.52/group1/M00/00/02/rBKUqFkKyw2AABiDAAF-CctvA7o851.jpg
             * referralcode : null
             * lastlogindate : 2017-05-11 15:05:56
             * registerdate : 2017-04-11 14:16:18
             * oauthUid : null
             * space2 : null
             * integral : 0
             * glod : 0
             * isDel : false
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
            private Object oauthUid;
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
        }

        public static class PathsBean implements Serializable{
            /**
             * id : 150
             * longitude : 113.1
             * latitude : 22.1
             * routeId : 20
             * createTime : 2017-05-09 19:35:26
             */

            private int id;
            private double longitude;
            private double latitude;
            private int routeId;
            private String createTime;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public int getRouteId() {
                return routeId;
            }

            public void setRouteId(int routeId) {
                this.routeId = routeId;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }
        }
    }
}
