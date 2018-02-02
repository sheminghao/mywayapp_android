package com.mywaytec.myway.fragment.club;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/11/28.
 */

public interface ClubView extends IBaseView {

    Context getContext();

    RecyclerView getCreateRecyclerView();

    RecyclerView getJoinRecyclerView();

    LinearLayout getNoclubHintLayout();

    TextView getCreateTV();

    TextView getJoinTV();

}
