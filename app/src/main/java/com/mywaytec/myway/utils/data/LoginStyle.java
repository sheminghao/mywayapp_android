package com.mywaytec.myway.utils.data;

import com.mywaytec.myway.APP;
import com.mywaytec.myway.utils.PreferencesUtils;

/**
 * Created by shemh on 2017/5/25.
 */

public class LoginStyle {

    public static String PHONE = "PHONE";
    public static String QQLOGIN = "QQLOGIN";
    public static String WECHAT = "WECHAT";

    public static String getLoginStyle(){
        return PreferencesUtils.getString(APP.getInstance(),"loginStyle");
    }

    public static void saveLoginStyle(String loginStyle){
        PreferencesUtils.putString(APP.getInstance(), "loginStyle", loginStyle);
    }

    public static void clearLoginStyle(){
        PreferencesUtils.removeValue(APP.getInstance(), "loginStyle");
    }

}
