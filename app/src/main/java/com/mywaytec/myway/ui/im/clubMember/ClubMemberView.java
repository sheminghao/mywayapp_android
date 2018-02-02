package com.mywaytec.myway.ui.im.clubMember;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/12/7.
 */

public interface ClubMemberView extends IBaseView {

    Context getContext();

    RecyclerView getMemberRecyclerView();

    RecyclerView getManagerRecyclerView();

    TextView getDeleteTV();

}
