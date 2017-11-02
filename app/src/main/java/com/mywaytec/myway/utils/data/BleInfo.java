package com.mywaytec.myway.utils.data;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.mywaytec.myway.APP;
import com.mywaytec.myway.model.bean.BleInfoBean;
import com.mywaytec.myway.model.bean.DynamicListBean;
import com.mywaytec.myway.utils.PreferencesUtils;

import java.util.List;

/**
 * Created by shemh on 2017/11/1.
 */

public class BleInfo {

    public static BleInfoBean getBleInfo(){
        String info=(String) PreferencesUtils.getString(APP.getInstance(),"bleInfo","");
        if (!TextUtils.isEmpty(info)) {
            return (BleInfoBean) new Gson().fromJson(info, BleInfoBean.class);
        }
        return new BleInfoBean();
    }

    public static void saveBleInfo(BleInfoBean bleInfoBean){
        if (null != bleInfoBean) {
            PreferencesUtils.putString(APP.getInstance(), "bleInfo", new Gson().toJson(bleInfoBean));
        }
    }

    public static void clearBleInfo(){
        PreferencesUtils.removeValue(APP.getInstance(), "bleInfo");
    }

}
