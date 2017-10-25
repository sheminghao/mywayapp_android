package com.mywaytec.myway.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shemh on 2017/9/4.
 */

public class DianziweilanBean implements Serializable{


    /**
     * code : 1
     * msg : 查询成功
     * obj : [{"id":"59a7834f9aaf9d1cc8578cdb","deviceId":"3F70343313059F464D559647","name":"电子围栏一","fenceType":0,"radius":1200,"createTime":1504150351,"locs":[{"latitude":22.001,"longitude":114.002}]},{"id":"59aa6253c6d2f712b4df66a3","deviceId":"3F70343313059F464D559647","name":"电子围栏三","fenceType":1,"radius":13500,"createTime":1504338515,"locs":[{"latitude":22.001,"longitude":114.001},{"latitude":22.002,"longitude":114.002},{"latitude":22.003,"longitude":114.003}]}]
     */

    private int code;
    private String msg;
    private List<ObjBean> obj;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ObjBean> getObj() {
        return obj;
    }

    public void setObj(List<ObjBean> obj) {
        this.obj = obj;
    }

    public static class ObjBean implements Serializable{
        /**
         * id : 59a7834f9aaf9d1cc8578cdb
         * deviceId : 3F70343313059F464D559647
         * name : 电子围栏一
         * fenceType : 0
         * radius : 1200
         * createTime : 1504150351
         * locs : [{"latitude":22.001,"longitude":114.002}]
         */

        private String id;
        private String deviceId;
        private String name;
        private int fenceType;
        private int radius;
        private long createTime;
        private List<LocsBean> locs;
        private boolean status;

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getFenceType() {
            return fenceType;
        }

        public void setFenceType(int fenceType) {
            this.fenceType = fenceType;
        }

        public int getRadius() {
            return radius;
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public List<LocsBean> getLocs() {
            return locs;
        }

        public void setLocs(List<LocsBean> locs) {
            this.locs = locs;
        }

        public static class LocsBean implements Serializable{
            /**
             * latitude : 22.001
             * longitude : 114.002
             */

            private double latitude;
            private double longitude;

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }
        }
    }
}
