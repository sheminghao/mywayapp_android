package com.mywaytec.myway.ui.message;

import android.content.Context;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/3/8.
 */

public interface MessageView extends IBaseView {

    LRecyclerView getRecyclerView();

    TextView getNoneTV();

    Context getContext();

}
