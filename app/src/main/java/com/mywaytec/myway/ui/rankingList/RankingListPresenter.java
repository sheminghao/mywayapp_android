package com.mywaytec.myway.ui.rankingList;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.mywaytec.myway.R;
import com.mywaytec.myway.adapter.RankingListAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.bean.DayRankingBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CommonSubscriber;

import java.text.DecimalFormat;
import java.text.MessageFormat;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/7/8.
 */

public class RankingListPresenter extends RxPresenter<RankingListView> {

    RankingListAdapter rankingListAdapter;
    LRecyclerViewAdapter lRecyclerViewAdapter;
    RetrofitHelper mRetrofitHelper;
    private DecimalFormat df = new DecimalFormat("######0.00");

    @Inject
    public RankingListPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    public void initRankingList(Context mContent) {
        rankingListAdapter = new RankingListAdapter(mContent);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(rankingListAdapter);
        mView.getLRecyclerView().setLayoutManager(new LinearLayoutManager(mContent));
        mView.getLRecyclerView().setAdapter(lRecyclerViewAdapter);
        mView.getLRecyclerView().setPullRefreshEnabled(false);
        mView.getLRecyclerView().setLoadMoreEnabled(false);

        getDayRankingList();
    }

    //日榜
    public void getDayRankingList() {
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        Log.i("TAG", "------uid," + uid);
        mRetrofitHelper.dayRanking(uid)
                .compose(RxUtil.<DayRankingBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DayRankingBean>() {
                    @Override
                    public void onNext(DayRankingBean dayRankingBean) {
                        Log.i("TAG", "------日榜dayRankingBean," + dayRankingBean.getCode());
                        rankingListAdapter.setDataList(dayRankingBean.getObj().getRankingList());
                        rankingListAdapter.notifyDataSetChanged();
                        lRecyclerViewAdapter.notifyDataSetChanged();

                        if (dayRankingBean.getCode() == 1) {
                            DayRankingBean.ObjBean.MyRankingListBean myRankingListBean = dayRankingBean.getObj().getMyRankingList().get(0);
                            mView.getRankPecentTV().setText(MessageFormat.format(mView.getContext()
                                    .getResources().getString(R.string.you_win_mw_players), df.format(myRankingListBean.getRankPecent())));
                            mView.getRankPecentTV().setEnabled(false);
                            mView.getMyRanking().setText(MessageFormat.format(mView.getContext()
                                    .getResources().getString(R.string.no), myRankingListBean.getRanking()));
                            mView.getCarType().setText(myRankingListBean.getSeries());
                            mView.getMileage().setText(myRankingListBean.getSumMileage() + "");
                        } else if (dayRankingBean.getCode() == 12) {
                            mView.getRankPecentTV().setText(R.string.you_can_be_ranked_and_get_scores_only_after_vehicle_verification);
                            mView.getRankPecentTV().setEnabled(true);
                            mView.getMyRanking().setText(R.string.未参与排名);
                            mView.getCarType().setText(R.string.无);
                            mView.getMileage().setText(R.string.无);
                        }
                    }
                });
    }

    //月榜
    public void getMonthRankingList() {
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        Log.i("TAG", "------uid," + uid);
        mRetrofitHelper.monthRanking(uid)
                .compose(RxUtil.<DayRankingBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DayRankingBean>() {
                    @Override
                    public void onNext(DayRankingBean dayRankingBean) {
                        Log.i("TAG", "------月榜dayRankingBean," + dayRankingBean.getCode());
                        rankingListAdapter.setDataList(dayRankingBean.getObj().getRankingList());
                        rankingListAdapter.notifyDataSetChanged();
                        lRecyclerViewAdapter.notifyDataSetChanged();

                        if (dayRankingBean.getCode() == 1) {
                            DayRankingBean.ObjBean.MyRankingListBean myRankingListBean = dayRankingBean.getObj().getMyRankingList().get(0);
                            mView.getRankPecentTV().setText("您已击败了" + df.format(myRankingListBean.getRankPecent())
                                    + "%的车手");
                            mView.getRankPecentTV().setEnabled(false);
                            mView.getMyRanking().setText("第" + myRankingListBean.getRanking() + "名");
                            mView.getCarType().setText(myRankingListBean.getSeries());
                            mView.getMileage().setText(myRankingListBean.getSumMileage() + "");
                        } else if (dayRankingBean.getCode() == 12) {
                            mView.getRankPecentTV().setText("车辆认证后才能参加排名比拼获得奖励哦~");
                            mView.getRankPecentTV().setEnabled(true);
                            mView.getMyRanking().setText("未参与排名");
                            mView.getCarType().setText("无");
                            mView.getMileage().setText("无");
                        }
                    }
                });
    }

    //总榜
    public void getTotalRankingList() {
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        Log.i("TAG", "------uid," + uid);
        mRetrofitHelper.totalRanking(uid)
                .compose(RxUtil.<DayRankingBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DayRankingBean>() {
                    @Override
                    public void onNext(DayRankingBean dayRankingBean) {
                        Log.i("TAG", "------总榜dayRankingBean," + dayRankingBean.getCode());

                        rankingListAdapter.setDataList(dayRankingBean.getObj().getRankingList());
                        rankingListAdapter.notifyDataSetChanged();
                        lRecyclerViewAdapter.notifyDataSetChanged();

                        if (dayRankingBean.getCode() == 1) {
                            DayRankingBean.ObjBean.MyRankingListBean myRankingListBean = dayRankingBean.getObj().getMyRankingList().get(0);
                            mView.getRankPecentTV().setText("您已击败了" + df.format(myRankingListBean.getRankPecent())
                                    + "%的车手");
                            mView.getRankPecentTV().setEnabled(false);
                            mView.getMyRanking().setText("第" + myRankingListBean.getRanking() + "名");
                            mView.getCarType().setText(myRankingListBean.getSeries());
                            mView.getMileage().setText(myRankingListBean.getSumMileage() + "");
                        } else if (dayRankingBean.getCode() == 12) {
                            mView.getRankPecentTV().setText("车辆认证后才能参加排名比拼获得奖励哦~");
                            mView.getRankPecentTV().setEnabled(true);
                            mView.getMyRanking().setText("未参与排名");
                            mView.getCarType().setText("无");
                            mView.getMileage().setText("无");
                        }else {
                            ToastUtils.showToast(dayRankingBean.getMsg());
                        }
                    }
                });
    }
}
