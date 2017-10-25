package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

/**
 * Created by shemh on 2017/5/16.
 */

public class RouteDetailBean extends BaseInfo{

    RouteListBean.ObjBean obj;

    public RouteListBean.ObjBean getObj() {
        return obj;
    }

    public void setObj(RouteListBean.ObjBean obj) {
        this.obj = obj;
    }
}
