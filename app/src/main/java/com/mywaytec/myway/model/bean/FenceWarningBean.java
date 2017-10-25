package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

import java.util.List;

/**
 * Created by shemh on 2017/9/13.
 */

public class FenceWarningBean extends BaseInfo{


    /**
     * obj : {"id":"59b361bf6df56328f5508cec","deviceId":"3F70343313059F464D559647","fenceWarnings":[{"id":"59b63508c6d2f7096417fd49","createTime":1505113352,"fence":{"id":"59b634a68c732b35551eec7d","deviceId":"3F70343313059F464D559647","name":"围栏二","fenceType":0,"radius":3192,"createTime":1505113254,"status":false,"locs":[{"latitude":0.0013566103282428362,"longitude":5.300031415236638E-4}]}},{"id":"59b6350ac6d2f7096417fd4a","createTime":1505113354,"fence":{"id":"59b634a68c732b35551eec7d","deviceId":"3F70343313059F464D559647","name":"围栏二","fenceType":0,"radius":3192,"createTime":1505113254,"status":false,"locs":[{"latitude":0.0013566103282428362,"longitude":5.300031415236638E-4}]}},{"id":"59b6350cc6d2f7096417fd4b","createTime":1505113356,"fence":{"id":"59b634a68c732b35551eec7d","deviceId":"3F70343313059F464D559647","name":"围栏二","fenceType":0,"radius":3192,"createTime":1505113254,"status":false,"locs":[{"latitude":0.0013566103282428362,"longitude":5.300031415236638E-4}]}}],"fence":{"id":"59b634a68c732b35551eec7d","deviceId":"3F70343313059F464D559647","name":"围栏二","fenceType":0,"radius":3192,"createTime":1505113254,"status":false,"locs":[{"latitude":0.0013566103282428362,"longitude":5.300031415236638E-4}]},"onOff":false,"fenceStatus":true}
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
         * id : 59b361bf6df56328f5508cec
         * deviceId : 3F70343313059F464D559647
         * fenceWarnings : [{"id":"59b63508c6d2f7096417fd49","createTime":1505113352,"fence":{"id":"59b634a68c732b35551eec7d","deviceId":"3F70343313059F464D559647","name":"围栏二","fenceType":0,"radius":3192,"createTime":1505113254,"status":false,"locs":[{"latitude":0.0013566103282428362,"longitude":5.300031415236638E-4}]}},{"id":"59b6350ac6d2f7096417fd4a","createTime":1505113354,"fence":{"id":"59b634a68c732b35551eec7d","deviceId":"3F70343313059F464D559647","name":"围栏二","fenceType":0,"radius":3192,"createTime":1505113254,"status":false,"locs":[{"latitude":0.0013566103282428362,"longitude":5.300031415236638E-4}]}},{"id":"59b6350cc6d2f7096417fd4b","createTime":1505113356,"fence":{"id":"59b634a68c732b35551eec7d","deviceId":"3F70343313059F464D559647","name":"围栏二","fenceType":0,"radius":3192,"createTime":1505113254,"status":false,"locs":[{"latitude":0.0013566103282428362,"longitude":5.300031415236638E-4}]}}]
         * fence : {"id":"59b634a68c732b35551eec7d","deviceId":"3F70343313059F464D559647","name":"围栏二","fenceType":0,"radius":3192,"createTime":1505113254,"status":false,"locs":[{"latitude":0.0013566103282428362,"longitude":5.300031415236638E-4}]}
         * onOff : false
         * fenceStatus : true
         */

        private String id;
        private String deviceId;
        private FenceBean fence;
        private boolean onOff;
        private boolean fenceStatus;
        private List<FenceWarningsBean> fenceWarnings;

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

        public FenceBean getFence() {
            return fence;
        }

        public void setFence(FenceBean fence) {
            this.fence = fence;
        }

        public boolean isOnOff() {
            return onOff;
        }

        public void setOnOff(boolean onOff) {
            this.onOff = onOff;
        }

        public boolean isFenceStatus() {
            return fenceStatus;
        }

        public void setFenceStatus(boolean fenceStatus) {
            this.fenceStatus = fenceStatus;
        }

        public List<FenceWarningsBean> getFenceWarnings() {
            return fenceWarnings;
        }

        public void setFenceWarnings(List<FenceWarningsBean> fenceWarnings) {
            this.fenceWarnings = fenceWarnings;
        }

        public static class FenceBean {
            /**
             * id : 59b634a68c732b35551eec7d
             * deviceId : 3F70343313059F464D559647
             * name : 围栏二
             * fenceType : 0
             * radius : 3192
             * createTime : 1505113254
             * status : false
             * locs : [{"latitude":0.0013566103282428362,"longitude":5.300031415236638E-4}]
             */

            private String id;
            private String deviceId;
            private String name;
            private int fenceType;
            private int radius;
            private int createTime;
            private boolean status;
            private List<LocsBean> locs;

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

            public int getCreateTime() {
                return createTime;
            }

            public void setCreateTime(int createTime) {
                this.createTime = createTime;
            }

            public boolean isStatus() {
                return status;
            }

            public void setStatus(boolean status) {
                this.status = status;
            }

            public List<LocsBean> getLocs() {
                return locs;
            }

            public void setLocs(List<LocsBean> locs) {
                this.locs = locs;
            }

            public static class LocsBean {
                /**
                 * latitude : 0.0013566103282428362
                 * longitude : 5.300031415236638E-4
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

        public static class FenceWarningsBean {
            /**
             * id : 59b63508c6d2f7096417fd49
             * createTime : 1505113352
             * fence : {"id":"59b634a68c732b35551eec7d","deviceId":"3F70343313059F464D559647","name":"围栏二","fenceType":0,"radius":3192,"createTime":1505113254,"status":false,"locs":[{"latitude":0.0013566103282428362,"longitude":5.300031415236638E-4}]}
             */

            private String id;
            private int createTime;
            private FenceBeanX fence;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getCreateTime() {
                return createTime;
            }

            public void setCreateTime(int createTime) {
                this.createTime = createTime;
            }

            public FenceBeanX getFence() {
                return fence;
            }

            public void setFence(FenceBeanX fence) {
                this.fence = fence;
            }

            public static class FenceBeanX {
                /**
                 * id : 59b634a68c732b35551eec7d
                 * deviceId : 3F70343313059F464D559647
                 * name : 围栏二
                 * fenceType : 0
                 * radius : 3192
                 * createTime : 1505113254
                 * status : false
                 * locs : [{"latitude":0.0013566103282428362,"longitude":5.300031415236638E-4}]
                 */

                private String id;
                private String deviceId;
                private String name;
                private int fenceType;
                private int radius;
                private int createTime;
                private boolean status;
                private List<LocsBeanX> locs;

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

                public int getCreateTime() {
                    return createTime;
                }

                public void setCreateTime(int createTime) {
                    this.createTime = createTime;
                }

                public boolean isStatus() {
                    return status;
                }

                public void setStatus(boolean status) {
                    this.status = status;
                }

                public List<LocsBeanX> getLocs() {
                    return locs;
                }

                public void setLocs(List<LocsBeanX> locs) {
                    this.locs = locs;
                }

                public static class LocsBeanX {
                    /**
                     * latitude : 0.0013566103282428362
                     * longitude : 5.300031415236638E-4
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
    }
}
