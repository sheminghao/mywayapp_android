package com.mywaytec.myway.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by shemh on 2017/5/5.
 */

public class CustomLinearLayoutManager extends LinearLayoutManager {
    public CustomLinearLayoutManager(Context context) {
        super(context);
    }
    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return false;
    }
}
