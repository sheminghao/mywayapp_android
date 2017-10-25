package com.mywaytec.myway.utils.data;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.mywaytec.myway.APP;
import com.mywaytec.myway.model.bean.DynamicListBean;
import com.mywaytec.myway.model.bean.RouteListBean;
import com.mywaytec.myway.utils.PreferencesUtils;

import java.util.List;

/**
 * Created by shemh on 2017/4/25.
 */

public class RouteData {

    public static RouteListBean getRouteData(){
        String info=(String) PreferencesUtils.getString(APP.getInstance(),"routeData","");
        if (!TextUtils.isEmpty(info)) {
            return (RouteListBean) new Gson().fromJson(info, RouteListBean.class);
        }
        return null;
    }

    public static void saveRouteData(List<RouteListBean.ObjBean> obj){
        RouteListBean routeListBean = new RouteListBean();
        routeListBean.setObj(obj);
        if (null != routeListBean) {
            PreferencesUtils.putString(APP.getInstance(), "routeData", new Gson().toJson(routeListBean));
        }
    }

    public static void clearRouteData(){
        PreferencesUtils.removeValue(APP.getInstance(), "routeData");
    }

}
