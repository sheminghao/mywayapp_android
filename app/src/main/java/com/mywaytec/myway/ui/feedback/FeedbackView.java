package com.mywaytec.myway.ui.feedback;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/3/8.
 */

public interface FeedbackView extends IBaseView {

    Context getContext();

    RecyclerView getRecyclerView();

}
