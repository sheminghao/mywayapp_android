package com.mywaytec.myway.ui.publish;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/3/8.
 */

public interface PublishView extends IBaseView {

    RecyclerView getRecyclerView();

    Context getContext();

}
