package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

/**
 * Created by codeest on 16/10/10.
 */

public class VersionBean extends BaseInfo{

    /**
     * obj : {"id":1,"name":"v1.1","mainVersion":1,"subVersion1":0,"subVersion2":0,"description":"1.集成基本功能。/n 2.登录注册 /n 3.动态、路线","fid":"007aaaf5298911e7bc8600163e064d3e","platform":0,"createTime":"2017-05-09 10:52:14","appUrl":"http://120.77.249.52/group1/M00/00/01/rBKUqFj--v2AdOrsAAAMFIdqGiA632.apk"}
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
         * id : 1
         * name : v1.1
         * mainVersion : 1
         * subVersion1 : 0
         * subVersion2 : 0
         * description : 1.集成基本功能。/n 2.登录注册 /n 3.动态、路线
         * fid : 007aaaf5298911e7bc8600163e064d3e
         * platform : 0
         * createTime : 2017-05-09 10:52:14
         * appUrl : http://120.77.249.52/group1/M00/00/01/rBKUqFj--v2AdOrsAAAMFIdqGiA632.apk
         */

        private int id;
        private String name;
        private int mainVersion;
        private int subVersion1;
        private int subVersion2;
        private String description;
        private String fid;
        private int platform;
        private String createTime;
        private String appUrl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getMainVersion() {
            return mainVersion;
        }

        public void setMainVersion(int mainVersion) {
            this.mainVersion = mainVersion;
        }

        public int getSubVersion1() {
            return subVersion1;
        }

        public void setSubVersion1(int subVersion1) {
            this.subVersion1 = subVersion1;
        }

        public int getSubVersion2() {
            return subVersion2;
        }

        public void setSubVersion2(int subVersion2) {
            this.subVersion2 = subVersion2;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getFid() {
            return fid;
        }

        public void setFid(String fid) {
            this.fid = fid;
        }

        public int getPlatform() {
            return platform;
        }

        public void setPlatform(int platform) {
            this.platform = platform;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getAppUrl() {
            return appUrl;
        }

        public void setAppUrl(String appUrl) {
            this.appUrl = appUrl;
        }
    }
}
