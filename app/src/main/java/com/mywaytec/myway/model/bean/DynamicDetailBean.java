package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.model.BaseInfo;

/**
 * Created by shemh on 2017/5/4.
 */

public class DynamicDetailBean extends BaseInfo{

    private DynamicListBean.ObjBean obj;

    public DynamicListBean.ObjBean getObj() {
        return obj;
    }

    public void setObj(DynamicListBean.ObjBean obj) {
        this.obj = obj;
    }
}
