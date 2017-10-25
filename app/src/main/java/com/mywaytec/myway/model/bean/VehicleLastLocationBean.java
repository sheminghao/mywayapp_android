package com.mywaytec.myway.model.bean;


import com.mywaytec.myway.model.BaseInfo;

import java.util.List;

/**
 * Created by shemh on 2017/10/17.
 */

public class VehicleLastLocationBean extends BaseInfo {


    /**
     * obj : {"id":"59af5e5a4feef21213c9e512","deviceId":"3F70343313059F464D559647","deviceType":"32","frameTime":1504084959,"index":3,"loc":{"type":"Point","coordinates":[114.036558,22.404731]}}
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
         * id : 59af5e5a4feef21213c9e512
         * deviceId : 3F70343313059F464D559647
         * deviceType : 32
         * frameTime : 1504084959
         * index : 3
         * loc : {"type":"Point","coordinates":[114.036558,22.404731]}
         */

        private String id;
        private String deviceId;
        private String deviceType;
        private int frameTime;
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

        public int getFrameTime() {
            return frameTime;
        }

        public void setFrameTime(int frameTime) {
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
             * coordinates : [114.036558,22.404731]
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
