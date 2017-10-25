package com.mywaytec.myway.ui.rankingList;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.IBaseView;

import butterknife.BindView;

/**
 * Created by shemh on 2017/3/8.
 */

public interface RankingListView extends IBaseView {

    Context getContext();

    LRecyclerView getLRecyclerView();

    ImageView getHeadPortraitImg();
    TextView getNicknameTV();
    TextView getRankPecentTV();
    TextView getMyRanking();
    TextView getCarType();
    TextView getMileage();

}
