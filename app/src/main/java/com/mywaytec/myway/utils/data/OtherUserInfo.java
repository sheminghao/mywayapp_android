package com.mywaytec.myway.utils.data;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.mywaytec.myway.APP;
import com.mywaytec.myway.model.bean.DynamicListBean;
import com.mywaytec.myway.model.bean.UserInfo;
import com.mywaytec.myway.utils.PreferencesUtils;

import java.util.List;

/**
 * Created by shemh on 2017/4/25.
 */

public class OtherUserInfo {

    public static UserInfo getOtherUserInfo(){
        String info=(String) PreferencesUtils.getString(APP.getInstance(),"otherUserInfo","");
        if (!TextUtils.isEmpty(info)) {
            return (UserInfo) new Gson().fromJson(info, UserInfo.class);
        }
        return null;
    }

    public static void saveOtherUserInfo(UserInfo userInfo){
        if (null != userInfo) {
            PreferencesUtils.putString(APP.getInstance(), "otherUserInfo", new Gson().toJson(userInfo));
        }
    }

    public static void clearDynamicData(){
        PreferencesUtils.removeValue(APP.getInstance(), "otherUserInfo");
    }

}
