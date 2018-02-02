package com.mywaytec.myway.ui.moreCarInfo;

import android.content.Context;
import android.widget.TextView;

import com.mywaytec.myway.base.IBaseView;
import com.mywaytec.myway.view.SpeedSeekBar;

/**
 * Created by shemh on 2017/3/8.
 */

public interface MoreCarInfoView extends IBaseView {

    Context getContext();

    TextView getDengdaiTV();

    SpeedSeekBar getSpeedSeekBar();

}
