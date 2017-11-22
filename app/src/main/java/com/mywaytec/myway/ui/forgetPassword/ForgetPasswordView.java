package com.mywaytec.myway.ui.forgetPassword;

import android.content.Context;
import android.widget.TextView;

import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/3/8.
 */

public interface ForgetPasswordView extends IBaseView {

    String getPhoneNumber();

    String getPassword1();
    String getPassword2();

    String getAuthcode();

    Context getContext();

    TextView getSelectCountryTV();

}
