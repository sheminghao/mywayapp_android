package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

import java.util.List;

/**
 * Created by shemh on 2017/9/5.
 */

public class VehicleTrackBean extends BaseInfo{


    private List<ObjBean> obj;

    public List<ObjBean> getObj() {
        return obj;
    }

    public void setObj(List<ObjBean> obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        /**
         * id : 59a65422b8097c217042422f
         * deviceId : 3F70343313059F464D559647
         * deviceType : 32
         * frameTime : 1504034761
         * index : 8
         * loc : {"type":"Point","coordinates":[114.03754,22.406328]}
         */

        private String id;
        private String deviceId;
        private String deviceType;
        private long frameTime;
        private int index;
        private LocBean loc;

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

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public long getFrameTime() {
            return frameTime;
        }

        public void setFrameTime(long frameTime) {
            this.frameTime = frameTime;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public LocBean getLoc() {
            return loc;
        }

        public void setLoc(LocBean loc) {
            this.loc = loc;
        }

        public static class LocBean {
            /**
             * type : Point
             * coordinates : [114.03754,22.406328]
             */

            private String type;
            private List<Double> coordinates;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public List<Double> getCoordinates() {
                return coordinates;
            }

            public void setCoordinates(List<Double> coordinates) {
                this.coordinates = coordinates;
            }
        }
    }
}
