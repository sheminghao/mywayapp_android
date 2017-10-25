package com.mywaytec.myway.utils.data;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.mywaytec.myway.APP;
import com.mywaytec.myway.model.bean.DynamicListBean;
import com.mywaytec.myway.utils.PreferencesUtils;

import java.util.List;

/**
 * Created by shemh on 2017/4/25.
 */

public class DynamicData {

    public static DynamicListBean getDynamicData(){
        String info=(String) PreferencesUtils.getString(APP.getInstance(),"dynamicData","");
        if (!TextUtils.isEmpty(info)) {
            return (DynamicListBean) new Gson().fromJson(info, DynamicListBean.class);
        }
        return null;
    }

    public static void saveDynamicData(List<DynamicListBean.ObjBean> obj){
        DynamicListBean dynamicListBean = new DynamicListBean();
        dynamicListBean.setObj(obj);
        if (null != dynamicListBean) {
            PreferencesUtils.putString(APP.getInstance(), "dynamicData", new Gson().toJson(dynamicListBean));
        }
    }

    public static void clearDynamicData(){
        PreferencesUtils.removeValue(APP.getInstance(), "dynamicData");
    }

}
