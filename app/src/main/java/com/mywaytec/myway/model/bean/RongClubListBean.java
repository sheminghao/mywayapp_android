package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

import java.util.List;

/**
 * Created by shemh on 2017/11/29.
 */

public class RongClubListBean extends BaseInfo {


    /**
     * obj : {"join":[{"groupid":3,"groupname":"俱乐部测试1","description":"俱乐部测试一下","createTime":"2017-11-23 15:47:13","country":"中国","province":"深圳","city":"龙华","isOfficial":false,"imgUrl":"http://120.77.249.52/group1/M00/00/0B/rBKUqFoWhG2AXMkSAAFcAG36RMM188.jpg","users":null}],"create":[{"groupid":4,"groupname":"曼威相亲平台","description":"帅哥美女聊嗨了","createTime":"2017-11-28 10:47:20","country":"中国","province":"深圳","city":"龙华","isOfficial":false,"imgUrl":"http://120.77.249.52/group1/M00/00/0B/rBKUqFoc-bKAT1nVAAAlDHxi95o255.jpg","users":null},{"groupid":1,"groupname":"俱乐部测试一下","description":"俱乐部测试","createTime":"2017-11-21 15:47:09","country":"中国","province":"深圳","city":"龙华","isOfficial":false,"imgUrl":"http://120.77.249.52/group1/M00/00/0B/rBKUqFoWf5CAasSaAAFcAG36RMM548.jpg","users":null}]}
     */

    private ObjBean obj;

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        private List<JoinBean> join;
        private List<CreateBean> create;

        public List<JoinBean> getJoin() {
            return join;
        }

        public void setJoin(List<JoinBean> join) {
            this.join = join;
        }

        public List<CreateBean> getCreate() {
            return create;
        }

        public void setCreate(List<CreateBean> create) {
            this.create = create;
        }

        public static class JoinBean {
            /**
             * groupid : 3
             * groupname : 俱乐部测试1
             * description : 俱乐部测试一下
             * createTime : 2017-11-23 15:47:13
             * country : 中国
             * province : 深圳
             * city : 龙华
             * isOfficial : false
             * imgUrl : http://120.77.249.52/group1/M00/00/0B/rBKUqFoWhG2AXMkSAAFcAG36RMM188.jpg
             * users : null
             */

            private int groupid;
            private String rong_gid;
            private String groupname;
            private String description;
            private String createTime;
            private String country;
            private String province;
            private String city;
            private boolean isOfficial;
            private String imgUrl;
            private Object users;

            public int getGroupid() {
                return groupid;
            }

            public void setGroupid(int groupid) {
                this.groupid = groupid;
            }

            public String getRong_gid() {
                return rong_gid;
            }

            public void setRong_gid(String rong_gid) {
                this.rong_gid = rong_gid;
            }

            public String getGroupname() {
                return groupname;
            }

            public void setGroupname(String groupname) {
                this.groupname = groupname;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public boolean isIsOfficial() {
                return isOfficial;
            }

            public void setIsOfficial(boolean isOfficial) {
                this.isOfficial = isOfficial;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public Object getUsers() {
                return users;
            }

            public void setUsers(Object users) {
                this.users = users;
            }
        }

        public static class CreateBean {
            /**
             * groupid : 4
             * groupname : 曼威相亲平台
             * description : 帅哥美女聊嗨了
             * createTime : 2017-11-28 10:47:20
             * country : 中国
             * province : 深圳
             * city : 龙华
             * isOfficial : false
             * imgUrl : http://120.77.249.52/group1/M00/00/0B/rBKUqFoc-bKAT1nVAAAlDHxi95o255.jpg
             * users : null
             */

            private int groupid;
            private String rong_gid;
            private String groupname;
            private String description;
            private String createTime;
            private String country;
            private String province;
            private String city;
            private boolean isOfficial;
            private String imgUrl;
            private Object users;

            public int getGroupid() {
                return groupid;
            }

            public void setGroupid(int groupid) {
                this.groupid = groupid;
            }

            public String getRong_gid() {
                return rong_gid;
            }

            public void setRong_gid(String rong_gid) {
                this.rong_gid = rong_gid;
            }

            public String getGroupname() {
                return groupname;
            }

            public void setGroupname(String groupname) {
                this.groupname = groupname;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public boolean isIsOfficial() {
                return isOfficial;
            }

            public void setIsOfficial(boolean isOfficial) {
                this.isOfficial = isOfficial;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public Object getUsers() {
                return users;
            }

            public void setUsers(Object users) {
                this.users = users;
            }
        }
    }
}
