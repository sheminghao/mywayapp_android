package com.mywaytec.myway.ui.userDynamic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/3/8.
 */

public interface UserDynamicView extends IBaseView {

    Context getContext();

    LRecyclerView getRecyclerView();



}
