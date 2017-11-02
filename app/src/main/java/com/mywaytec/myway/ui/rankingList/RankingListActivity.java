package com.mywaytec.myway.ui.rankingList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.ui.bindingCar.BindingCarActivity;
import com.mywaytec.myway.utils.PreferencesUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 排行榜
 */
public class RankingListActivity extends BaseActivity<RankingListPresenter> implements RankingListView {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lrecyclerView)
    LRecyclerView lrecyclerView;
    @BindView(R.id.layout_day_ranking)
    LinearLayout layoutDayRanking;
    @BindView(R.id.layout_month_ranking)
    LinearLayout layoutMonthRanking;
    @BindView(R.id.layout_total_ranking)
    LinearLayout layoutTotalRanking;
    @BindView(R.id.tv_day_ranking)
    TextView tvDayRanking;
    @BindView(R.id.tv_month_ranking)
    TextView tvMonthRanking;
    @BindView(R.id.tv_total_ranking)
    TextView tvTotalRanking;
    @BindView(R.id.view_day_ranking)
    View viewDayRanking;
    @BindView(R.id.view_month_ranking)
    View viewMonthRanking;
    @BindView(R.id.view_total_ranking)
    View viewTotalRanking;
    @BindView(R.id.img_head_portrait)
    ImageView imgHeadPortrait;
    @BindView(R.id.tv_ranking_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_rank_pecent)
    TextView tvRankPecent;
    @BindView(R.id.tv_my_ranking)
    TextView tvMyRanking;
    @BindView(R.id.tv_car_type)
    TextView tvCarType;
    @BindView(R.id.tv_mileage)
    TextView tvMileage;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_ranking_list;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.ranking);
        mPresenter.initRankingList(this);
        String nickname = PreferencesUtils.getLoginInfo().getObj().getNickname();
        tvNickname.setText(nickname+"");
        Glide.with(this).load(PreferencesUtils.getLoginInfo().getObj().getImgeUrl())
                .error(R.mipmap.icon_default)
                .centerCrop()
                .into(imgHeadPortrait);
    }

    @Override
    protected void updateViews() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public LRecyclerView getLRecyclerView() {
        return lrecyclerView;
    }

    @Override
    public ImageView getHeadPortraitImg() {
        return imgHeadPortrait;
    }

    @Override
    public TextView getNicknameTV() {
        return tvNickname;
    }

    @Override
    public TextView getRankPecentTV() {
        return tvRankPecent;
    }

    @Override
    public TextView getMyRanking() {
        return tvMyRanking;
    }

    @Override
    public TextView getCarType() {
        return tvCarType;
    }

    @Override
    public TextView getMileage() {
        return tvMileage;
    }

    @OnClick({R.id.layout_day_ranking, R.id.layout_month_ranking, R.id.layout_total_ranking,
            R.id.tv_rank_pecent})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_day_ranking://日榜
                viewDayRanking.setVisibility(View.VISIBLE);
                viewMonthRanking.setVisibility(View.INVISIBLE);
                viewTotalRanking.setVisibility(View.INVISIBLE);
                tvDayRanking.setTextColor(Color.WHITE);
                tvMonthRanking.setTextColor(Color.parseColor("#bcc0c8"));
                tvTotalRanking.setTextColor(Color.parseColor("#bcc0c8"));
                mPresenter.getDayRankingList();
                break;
            case R.id.layout_month_ranking://月榜
                viewDayRanking.setVisibility(View.INVISIBLE);
                viewMonthRanking.setVisibility(View.VISIBLE);
                viewTotalRanking.setVisibility(View.INVISIBLE);
                tvDayRanking.setTextColor(Color.parseColor("#bcc0c8"));
                tvMonthRanking.setTextColor(Color.WHITE);
                tvTotalRanking.setTextColor(Color.parseColor("#bcc0c8"));
                mPresenter.getMonthRankingList();
                break;
            case R.id.layout_total_ranking://总榜
                viewDayRanking.setVisibility(View.INVISIBLE);
                viewMonthRanking.setVisibility(View.INVISIBLE);
                viewTotalRanking.setVisibility(View.VISIBLE);
                tvDayRanking.setTextColor(Color.parseColor("#bcc0c8"));
                tvMonthRanking.setTextColor(Color.parseColor("#bcc0c8"));
                tvTotalRanking.setTextColor(Color.WHITE);
                mPresenter.getTotalRankingList();
                break;
            case R.id.tv_rank_pecent://
                startActivity(new Intent(RankingListActivity.this, BindingCarActivity.class));
                break;
        }
    }
}
