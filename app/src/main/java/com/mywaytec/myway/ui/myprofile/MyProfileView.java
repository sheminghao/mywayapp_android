package com.mywaytec.myway.ui.myprofile;

import android.content.Context;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/3/8.
 */

public interface MyProfileView extends IBaseView {

    Context getContext();

    ImageView getHeadPortraitImg();

    EditText getSignatureET();

    EditText getNicknameET();

    EditText getNameET();

    TextView getSexTV();

    EditText getZhiyeET();

    EditText getAddressET();

    EditText getEmailET();

    EditText getPhoneNumET();

    TextView getBirthdayTV();

//    TextView getReferralTV();

    TextView getNicknameTV();

    TextView getGoldTV();

    TextView getIntegralTV();

}
