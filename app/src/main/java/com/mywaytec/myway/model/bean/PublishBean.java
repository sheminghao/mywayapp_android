package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

/**
 * Created by shemh on 2017/4/13.
 */

public class PublishBean extends BaseInfo {


    /**
     * obj : {"id":22,"content":"01/11第一条说说","lid":12,"createTime":"2017-04-11 09:56:38","uid":"fbf454c412d011e7a6dc3497f69570f3","likeNum":null,"location":null,"images":null}
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
         * id : 22
         * content : 01/11第一条说说
         * lid : 12
         * createTime : 2017-04-11 09:56:38
         * uid : fbf454c412d011e7a6dc3497f69570f3
         * likeNum : null
         * location : null
         * images : null
         */

        private int id;
        private String content;
        private int lid;
        private String createTime;
        private String uid;
        private Object likeNum;
        private Object location;
        private Object images;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getLid() {
            return lid;
        }

        public void setLid(int lid) {
            this.lid = lid;
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

        public Object getLikeNum() {
            return likeNum;
        }

        public void setLikeNum(Object likeNum) {
            this.likeNum = likeNum;
        }

        public Object getLocation() {
            return location;
        }

        public void setLocation(Object location) {
            this.location = location;
        }

        public Object getImages() {
            return images;
        }

        public void setImages(Object images) {
            this.images = images;
        }
    }
}
