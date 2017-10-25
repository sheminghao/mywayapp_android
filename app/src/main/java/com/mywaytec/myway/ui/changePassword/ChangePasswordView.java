package com.mywaytec.myway.ui.changePassword;

import android.content.Context;

import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/3/8.
 */

public interface ChangePasswordView extends IBaseView {

    String getOldPassword();

    String getNewPassword();

    String getConfirmPassword();

    Context getContext();
}
