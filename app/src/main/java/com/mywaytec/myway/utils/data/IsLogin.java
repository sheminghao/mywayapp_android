package com.mywaytec.myway.utils.data;

import com.mywaytec.myway.APP;
import com.mywaytec.myway.utils.PreferencesUtils;

/**
 * Created by shemh on 2017/4/25.
 */

public class IsLogin {

    public static boolean isLogin(){
        return PreferencesUtils.getBoolean(APP.getInstance(),"isLogin",false);
    }

    public static void saveDynamicData(boolean isLogin){
        PreferencesUtils.putBoolean(APP.getInstance(), "isLogin", isLogin);
    }

    public static void clearIsLogin(){
        PreferencesUtils.removeValue(APP.getInstance(), "isLogin");
    }

}
