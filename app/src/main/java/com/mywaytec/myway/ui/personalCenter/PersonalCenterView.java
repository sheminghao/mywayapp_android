package com.mywaytec.myway.ui.personalCenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/12/14.
 */

public interface PersonalCenterView extends IBaseView {

    Context getContext();

    RecyclerView getGuanzhuRecyclerView();

}
