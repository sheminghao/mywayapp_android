package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

/**
 * Created by shemh on 2017/11/29.
 */

public class RongTokenBean extends BaseInfo{


    /**
     * obj : {"uid":"e8fbd8fd0f6b11e78cff3497f69570f3","rongToken":"T7FFpo3T/7eg0BsSEvnFdujknWtgvAeaF1p0ep74nylGmIh4bwF3m9hOGPAJocpzYy1G3FEV7gLwd11DXjU1nB3cTfEx1jNEoDcI/i94l0maFLosJa3BoDods/YwvMWe"}
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
         * uid : e8fbd8fd0f6b11e78cff3497f69570f3
         * rongToken : T7FFpo3T/7eg0BsSEvnFdujknWtgvAeaF1p0ep74nylGmIh4bwF3m9hOGPAJocpzYy1G3FEV7gLwd11DXjU1nB3cTfEx1jNEoDcI/i94l0maFLosJa3BoDods/YwvMWe
         */

        private String uid;
        private String rongToken;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getRongToken() {
            return rongToken;
        }

        public void setRongToken(String rongToken) {
            this.rongToken = rongToken;
        }
    }
}
