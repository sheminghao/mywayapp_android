package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

import java.util.List;

/**
 * Created by shemh on 2017/12/15.
 */

public class DeleteClubUsersBean extends BaseInfo{

    /**
     * obj : {"delete_success":["04ead564a32b11e78dd300163e064d3e","086df9e1a32b11e78dd300163e064d3e","0c1d25cea32b11e78dd300163e064d3e"],"delete_fail":[]}
     */

    private ObjBean obj;

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        private List<String> delete_success;
        private List<?> delete_fail;

        public List<String> getDelete_success() {
            return delete_success;
        }

        public void setDelete_success(List<String> delete_success) {
            this.delete_success = delete_success;
        }

        public List<?> getDelete_fail() {
            return delete_fail;
        }

        public void setDelete_fail(List<?> delete_fail) {
            this.delete_fail = delete_fail;
        }

        @Override
        public String toString() {
            return "ObjBean{" +
                    "delete_success=" + delete_success +
                    ", delete_fail=" + delete_fail +
                    '}';
        }
    }
}
