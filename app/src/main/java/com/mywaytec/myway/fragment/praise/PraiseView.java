package com.mywaytec.myway.fragment.praise;

import android.content.Context;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/3/9.
 */

public interface PraiseView extends IBaseView {

    LRecyclerView getRecyclerView();

    Context getContext();

}
