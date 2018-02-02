package com.mywaytec.myway.ui.main;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/3/8.
 */

public interface MainView extends IBaseView {

    Context getContext();

    TextView getGoldTV();

    ImageView getSigninImg();

    TextView getSigninTV();

    ImageView getXiaoxiHintImg();

}
