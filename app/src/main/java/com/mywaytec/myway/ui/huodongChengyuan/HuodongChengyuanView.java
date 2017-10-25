package com.mywaytec.myway.ui.huodongChengyuan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/8/15.
 */

public interface HuodongChengyuanView extends IBaseView {

    Context getContext();

    RecyclerView getRecyclerView();

    ImageView getHeadPortraitImg();

    TextView getNameTV();

    ImageView getGanderImg();

    LinearLayout getDuizhangLayout();

}
