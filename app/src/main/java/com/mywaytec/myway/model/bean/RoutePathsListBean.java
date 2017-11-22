package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shemh on 2017/10/18.
 */

public class RoutePathsListBean extends BaseInfo implements Serializable{


    private List<ObjBean> obj;

    public List<ObjBean> getObj() {
        return obj;
    }

    public void setObj(List<ObjBean> obj) {
        this.obj = obj;
    }

    public static class ObjBean implements Serializable{
        /**
         * id : 248
         * image : null
         * name : 未命名路线1
         * sceneryStar : null
         * difficultyStar : null
         * legend : 320
         * enduranceClaim : null
         * intro : null
         * origin : null
         * originBus : null
         * destination : null
         * destinationBus : null
         * score : null
         * createTime : 2017-10-16 14:44:13
         * title : null
         * uid : e8fbd8fd0f6b11e78cff3497f69570f3
         * isSuccess : false
         * city : null
         * likeNum : null
         * isLike : null
         * user : null
         * paths : null
         * photos : null
         * duration : 00:20:34
         */

        private int id;
        private Object image;
        private String name;
        private Object sceneryStar;
        private Object difficultyStar;
        private int legend;
        private Object enduranceClaim;
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
        private Object likeNum;
        private Object isLike;
        private Object user;
        private Object paths;
        private Object photos;
        private String duration;

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

        public Object getSceneryStar() {
            return sceneryStar;
        }

        public void setSceneryStar(Object sceneryStar) {
            this.sceneryStar = sceneryStar;
        }

        public Object getDifficultyStar() {
            return difficultyStar;
        }

        public void setDifficultyStar(Object difficultyStar) {
            this.difficultyStar = difficultyStar;
        }

        public int getLegend() {
            return legend;
        }

        public void setLegend(int legend) {
            this.legend = legend;
        }

        public Object getEnduranceClaim() {
            return enduranceClaim;
        }

        public void setEnduranceClaim(Object enduranceClaim) {
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

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        @Override
        public String toString() {
            return "ObjBean{" +
                    "id=" + id +
                    ", image=" + image +
                    ", name='" + name + '\'' +
                    ", sceneryStar=" + sceneryStar +
                    ", difficultyStar=" + difficultyStar +
                    ", legend=" + legend +
                    ", enduranceClaim=" + enduranceClaim +
                    ", intro=" + intro +
                    ", origin=" + origin +
                    ", originBus=" + originBus +
                    ", destination=" + destination +
                    ", destinationBus=" + destinationBus +
                    ", score=" + score +
                    ", createTime='" + createTime + '\'' +
                    ", title=" + title +
                    ", uid='" + uid + '\'' +
                    ", isSuccess=" + isSuccess +
                    ", city=" + city +
                    ", likeNum=" + likeNum +
                    ", isLike=" + isLike +
                    ", user=" + user +
                    ", paths=" + paths +
                    ", photos=" + photos +
                    ", duration='" + duration + '\'' +
                    '}';
        }
    }
}
