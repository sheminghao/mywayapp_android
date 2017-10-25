package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

/**
 * Created by shemh on 2017/5/8.
 */

public class UploadRouteBean extends BaseInfo{


    /**
     * obj : {"id":14,"image":null,"name":"测试","sceneryStar":5,"difficultyStar":5,"legend":10,"enduranceClaim":20,"intro":"测试","origin":"曼威","originBus":"曼威","destination":"曼威","destinationBus":"曼威","score":10,"createTime":"2017-05-08 14:41:37","title":"测试","uid":"5f86efdb1e7e11e7bc8600163e064d3e","isSuccess":true,"likeNum":null,"user":null,"paths":null}
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
         * id : 14
         * image : null
         * name : 测试
         * sceneryStar : 5
         * difficultyStar : 5
         * legend : 10
         * enduranceClaim : 20
         * intro : 测试
         * origin : 曼威
         * originBus : 曼威
         * destination : 曼威
         * destinationBus : 曼威
         * score : 10
         * createTime : 2017-05-08 14:41:37
         * title : 测试
         * uid : 5f86efdb1e7e11e7bc8600163e064d3e
         * isSuccess : true
         * likeNum : null
         * user : null
         * paths : null
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
        private String uid;
        private boolean isSuccess;
        private Object likeNum;
        private Object user;
        private Object paths;

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

        public Object getLikeNum() {
            return likeNum;
        }

        public void setLikeNum(Object likeNum) {
            this.likeNum = likeNum;
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
    }
}
