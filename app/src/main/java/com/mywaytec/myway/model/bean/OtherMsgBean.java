package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

/**
 * Created by shemh on 2017/5/17.
 */

public class OtherMsgBean extends BaseInfo{

    /**
     * obj : {"isSign":"true","Ranking":"-1","MsgUnread":"0"}
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
         * isSign : true
         * Ranking : -1
         * MsgUnread : 0
         */

        private String isSign;
        private String Ranking;
        private String MsgUnread;

        public String getIsSign() {
            return isSign;
        }

        public void setIsSign(String isSign) {
            this.isSign = isSign;
        }

        public String getRanking() {
            return Ranking;
        }

        public void setRanking(String Ranking) {
            this.Ranking = Ranking;
        }

        public String getMsgUnread() {
            return MsgUnread;
        }

        public void setMsgUnread(String MsgUnread) {
            this.MsgUnread = MsgUnread;
        }
    }
}
