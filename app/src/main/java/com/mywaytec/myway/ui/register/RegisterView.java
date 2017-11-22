package com.mywaytec.myway.ui.register;

import android.content.Context;
import android.widget.TextView;

import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/3/8.
 */

public interface RegisterView extends IBaseView {

    String getPhoneNumber();

    String getPassword1();

    String getPassword2();

    String getAuthcode();

    TextView getAuthcodeTV();

    TextView getSelectCountryTV();

    Context getContext();

}
