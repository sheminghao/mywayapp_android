package com.mywaytec.myway.ui.mydynamic;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/3/9.
 */

public interface MyDynamicView extends IBaseView {

    LRecyclerView getDynamicList();

    LRecyclerView getWayList();

}
