package com.mywaytec.myway.ui.login;

import android.content.Context;

import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/3/8.
 */

public interface LoginView extends IBaseView {

    String getAccountName();

    String getPassword();

    Context getContext();

}
