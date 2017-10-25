package com.mywaytec.myway.ui.gradeAndGold;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/3/8.
 */

public interface GradeAndGoldView extends IBaseView {

    Context getContext();

    RecyclerView getAccessRecyclerView();

    RecyclerView getLevelRecyclerView();

    TextView getDriverLevelTV();

    TextView getJinfenTV();

    TextView getGoldTV();

    ProgressBar getProgressBar();

}
