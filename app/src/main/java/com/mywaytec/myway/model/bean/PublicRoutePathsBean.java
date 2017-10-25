package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

/**
 * Created by shemh on 2017/10/19.
 */

public class PublicRoutePathsBean extends BaseInfo {


    /**
     * obj : {"id":248,"image":null,"name":"宝能科技园一日游","sceneryStar":2,"difficultyStar":3,"legend":2400,"enduranceClaim":3200,"intro":"深圳宝能科技园分为南北两个园区，现北区商务园区将于2012年10月竣工，北区东南临五和大道，西南为清丽路，西北为清祥路，东北为清新路，与之相邻为机荷高速，北区总建筑面积约43万平方米，包括办公、商业、地下室及相关配套设施。其中3-5栋为商业配套，面积约10万平米，6-9栋为商务办公区域，是集楼宇设备自动化系统、安防系统自动化系统、通信自动化系统、办公自动化系统、火灾自动报警和消防联动控制系统于一体的5A级纯写字楼集群。","origin":"清湖老村","originBus":"清湖老村公交站","destination":"宝能科技园","destinationBus":"清湖产业园公交站","score":7,"createTime":"2017-10-16 15:40:17","title":"宝能科技园太小了","uid":null,"isSuccess":true,"city":"深圳市","likeNum":null,"isLike":null,"user":null,"paths":null,"photos":null,"duration":null}
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
         * id : 248
         * image : null
         * name : 宝能科技园一日游
         * sceneryStar : 2
         * difficultyStar : 3
         * legend : 2400
         * enduranceClaim : 3200
         * intro : 深圳宝能科技园分为南北两个园区，现北区商务园区将于2012年10月竣工，北区东南临五和大道，西南为清丽路，西北为清祥路，东北为清新路，与之相邻为机荷高速，北区总建筑面积约43万平方米，包括办公、商业、地下室及相关配套设施。其中3-5栋为商业配套，面积约10万平米，6-9栋为商务办公区域，是集楼宇设备自动化系统、安防系统自动化系统、通信自动化系统、办公自动化系统、火灾自动报警和消防联动控制系统于一体的5A级纯写字楼集群。
         * origin : 清湖老村
         * originBus : 清湖老村公交站
         * destination : 宝能科技园
         * destinationBus : 清湖产业园公交站
         * score : 7
         * createTime : 2017-10-16 15:40:17
         * title : 宝能科技园太小了
         * uid : null
         * isSuccess : true
         * city : 深圳市
         * likeNum : null
         * isLike : null
         * user : null
         * paths : null
         * photos : null
         * duration : null
         */

        private int id;
        private Object image;
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
        private int score;
        private String createTime;
        private String title;
        private Object uid;
        private boolean isSuccess;
        private String city;
        private Object likeNum;
        private Object isLike;
        private Object user;
        private Object paths;
        private Object photos;
        private Object duration;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getImage() {
            return image;
        }

        public void setImage(Object image) {
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

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
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

        public Object getUid() {
            return uid;
        }

        public void setUid(Object uid) {
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

        public Object getLikeNum() {
            return likeNum;
        }

        public void setLikeNum(Object likeNum) {
            this.likeNum = likeNum;
        }

        public Object getIsLike() {
            return isLike;
        }

        public void setIsLike(Object isLike) {
            this.isLike = isLike;
        }

        public Object getUser() {
            return user;
        }

        public void setUser(Object user) {
            this.user = user;
        }

        public Object getPaths() {
            return paths;
        }

        public void setPaths(Object paths) {
            this.paths = paths;
        }

        public Object getPhotos() {
            return photos;
        }

        public void setPhotos(Object photos) {
            this.photos = photos;
        }

        public Object getDuration() {
            return duration;
        }

        public void setDuration(Object duration) {
            this.duration = duration;
        }
    }
}
