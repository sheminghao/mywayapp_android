package com.mywaytec.myway.ui.fingerMark;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BluetoothLeService;
import com.mywaytec.myway.base.IBaseView;
import com.mywaytec.myway.view.SlideUp;

import butterknife.BindView;

/**
 * Created by shemh on 2017/3/8.
 */

public interface FingerMarkView extends IBaseView {

    Context getContext();

    RecyclerView getRecyclerView();

    SlideUp getSlideUp();

    TextView getEnterCountTV();

    TextView getHintTextTV();

    ImageView getFingerWarkImg();

    TextView getCancelTV();

    String getDeviceAddress();

}
