package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

import java.util.List;

/**
 * Created by shemh on 2017/7/14.
 */

public class DayRankingBean extends BaseInfo{


    /**
     * obj : {"myRankingList":[{"ranking":1,"uname":"myway_7jx38840","imgSrc":"http://120.77.249.52/group1/M00/00/04/rBKUqFlTIbCAXEq2AAD272CZeX4722.png","series":"RA01","vname":"2AEF456A","sumMileage":0,"uType":"平衡车","rankPecent":1}],"rankingList":[{"ranking":1,"uname":"myway_7jx38840","imgSrc":"http://120.77.249.52/group1/M00/00/04/rBKUqFlTIbCAXEq2AAD272CZeX4722.png","series":"RA01","vname":"22E9C50D","sumMileage":0,"uType":"平衡车","rankPecent":null},{"ranking":2,"uname":"myway_7jx38840","imgSrc":"http://120.77.249.52/group1/M00/00/04/rBKUqFlTIbCAXEq2AAD272CZeX4722.png","series":"RA01","vname":"22E9C50A","sumMileage":0,"uType":"平衡车","rankPecent":null},{"ranking":3,"uname":"myway_7jx38840","imgSrc":"http://120.77.249.52/group1/M00/00/04/rBKUqFlTIbCAXEq2AAD272CZeX4722.png","series":"RA01","vname":"2AEF456A","sumMileage":0,"uType":"平衡车","rankPecent":null}]}
     */

    private ObjBean obj;

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        private List<MyRankingListBean> myRankingList;
        private List<RankingListBean> rankingList;

        public List<MyRankingListBean> getMyRankingList() {
            return myRankingList;
        }

        public void setMyRankingList(List<MyRankingListBean> myRankingList) {
            this.myRankingList = myRankingList;
        }

        public List<RankingListBean> getRankingList() {
            return rankingList;
        }

        public void setRankingList(List<RankingListBean> rankingList) {
            this.rankingList = rankingList;
        }

        public static class MyRankingListBean {
            /**
             * ranking : 1
             * uname : myway_7jx38840
             * imgSrc : http://120.77.249.52/group1/M00/00/04/rBKUqFlTIbCAXEq2AAD272CZeX4722.png
             * series : RA01
             * vname : 2AEF456A
             * sumMileage : 0
             * uType : 平衡车
             * rankPecent : 1
             */

            private int ranking;
            private String uname;
            private String imgSrc;
            private String series;
            private String vname;
            private int sumMileage;
            private String uType;
            private double rankPecent;

            public int getRanking() {
                return ranking;
            }

            public void setRanking(int ranking) {
                this.ranking = ranking;
            }

            public String getUname() {
                return uname;
            }

            public void setUname(String uname) {
                this.uname = uname;
            }

            public String getImgSrc() {
                return imgSrc;
            }

            public void setImgSrc(String imgSrc) {
                this.imgSrc = imgSrc;
            }

            public String getSeries() {
                return series;
            }

            public void setSeries(String series) {
                this.series = series;
            }

            public String getVname() {
                return vname;
            }

            public void setVname(String vname) {
                this.vname = vname;
            }

            public int getSumMileage() {
                return sumMileage;
            }

            public void setSumMileage(int sumMileage) {
                this.sumMileage = sumMileage;
            }

            public String getUType() {
                return uType;
            }

            public void setUType(String uType) {
                this.uType = uType;
            }

            public double getRankPecent() {
                return rankPecent;
            }

            public void setRankPecent(double rankPecent) {
                this.rankPecent = rankPecent;
            }
        }

        public static class RankingListBean {
            /**
             * ranking : 1
             * uname : myway_7jx38840
             * imgSrc : http://120.77.249.52/group1/M00/00/04/rBKUqFlTIbCAXEq2AAD272CZeX4722.png
             * series : RA01
             * vname : 22E9C50D
             * sumMileage : 0
             * uType : 平衡车
             * rankPecent : null
             */

            private int ranking;
            private String uname;
            private String imgSrc;
            private String series;
            private String vname;
            private int sumMileage;
            private String uType;
            private Object rankPecent;

            public int getRanking() {
                return ranking;
            }

            public void setRanking(int ranking) {
                this.ranking = ranking;
            }

            public String getUname() {
                return uname;
            }

            public void setUname(String uname) {
                this.uname = uname;
            }

            public String getImgSrc() {
                return imgSrc;
            }

            public void setImgSrc(String imgSrc) {
                this.imgSrc = imgSrc;
            }

            public String getSeries() {
                return series;
            }

            public void setSeries(String series) {
                this.series = series;
            }

            public String getVname() {
                return vname;
            }

            public void setVname(String vname) {
                this.vname = vname;
            }

            public int getSumMileage() {
                return sumMileage;
            }

            public void setSumMileage(int sumMileage) {
                this.sumMileage = sumMileage;
            }

            public String getUType() {
                return uType;
            }

            public void setUType(String uType) {
                this.uType = uType;
            }

            public Object getRankPecent() {
                return rankPecent;
            }

            public void setRankPecent(Object rankPecent) {
                this.rankPecent = rankPecent;
            }
        }
    }
}
