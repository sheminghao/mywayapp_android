package com.mywaytec.myway.ui.im.exitClub;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/12/1.
 */

public interface ExitClubView extends IBaseView{

    RecyclerView getRecyclerView();

    Context getContext();

    TextView getTitleTV();

    ImageView getClubHeadImg();

    TextView getClubNameTV();

    TextView getClubOfficialTV();

    TextView getMemberTV();

    TextView getAreaTV();

    TextView getJianjieTV();

    TextView getPeopleNumTV();

}
