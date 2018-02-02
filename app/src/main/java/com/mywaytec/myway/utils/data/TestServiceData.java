package com.mywaytec.myway.utils.data;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.mywaytec.myway.APP;
import com.mywaytec.myway.model.bean.DynamicListBean;
import com.mywaytec.myway.model.bean.TestServiceBean;
import com.mywaytec.myway.utils.PreferencesUtils;

import java.util.List;

/**
 * Created by shemh on 2017/4/25.
 */

public class TestServiceData {

    public static final String TEXT_HOST = "TEXT_HOST";

    public static TestServiceBean getTestServiceData(){
        String info=(String) PreferencesUtils.getString(APP.getInstance(),TEXT_HOST,"");
        if (!TextUtils.isEmpty(info)) {
            return (TestServiceBean) new Gson().fromJson(info, TestServiceBean.class);
        }
        return null;
    }

    public static void saveTestServiceData(TestServiceBean obj){
        if (null != obj) {
            PreferencesUtils.putString(APP.getInstance(), TEXT_HOST, new Gson().toJson(obj));
        }
    }

    public static void clearDynamicData(){
        PreferencesUtils.removeValue(APP.getInstance(), TEXT_HOST);
    }

}
