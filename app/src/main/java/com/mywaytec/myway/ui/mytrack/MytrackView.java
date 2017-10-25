package com.mywaytec.myway.ui.mytrack;

import android.content.Context;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/3/8.
 */

public interface MytrackView extends IBaseView {

    LRecyclerView getRecyclerView();

    Context getContext();

}
