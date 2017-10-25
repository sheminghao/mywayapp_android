package com.mywaytec.myway.fragment.car;

import android.content.Context;
import android.widget.TextView;

import com.mywaytec.myway.base.BluetoothLeService;
import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/3/9.
 */

public interface CarView extends IBaseView {

    TextView getTextView();

    Context getContext();

    BluetoothLeService getBluetoothLeService();

    boolean isOther();

    void setOther(boolean isOther);

}
