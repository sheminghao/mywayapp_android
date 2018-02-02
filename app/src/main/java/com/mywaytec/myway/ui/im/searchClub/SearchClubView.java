package com.mywaytec.myway.ui.im.searchClub;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/11/30.
 */

public interface SearchClubView extends IBaseView{

    RecyclerView getRecyclerView();

    Context getContext();

    TextView getTuijianTV();

}
