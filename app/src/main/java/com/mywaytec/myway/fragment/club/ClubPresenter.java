package com.mywaytec.myway.fragment.club;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.mywaytec.myway.adapter.RongClubCreateAdapter;
import com.mywaytec.myway.adapter.RongClubJoinAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.bean.RongClubListBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.DialogUtils;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.view.CommonSubscriber;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/11/28.
 */

public class ClubPresenter extends RxPresenter<ClubView>{

    private RetrofitHelper retrofitHelper;
    private Context mContext;
    private RongClubCreateAdapter rongClubCreateAdapter;
    private RongClubJoinAdapter rongClubJoinAdapter;

    @Inject
    public ClubPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
    }

    @Override
    public void attachView(ClubView view) {
        super.attachView(view);
        mContext = mView.getContext();
    }

    public void initData(){
        rongClubCreateAdapter = new RongClubCreateAdapter(mContext);
        mView.getCreateRecyclerView().setLayoutManager(new LinearLayoutManager(mContext));
        mView.getCreateRecyclerView().setAdapter(rongClubCreateAdapter);

        rongClubJoinAdapter = new RongClubJoinAdapter(mContext);
        mView.getJoinRecyclerView().setLayoutManager(new LinearLayoutManager(mContext));
        mView.getJoinRecyclerView().setAdapter(rongClubJoinAdapter);

        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.rongClubList(uid, token)
                .compose(RxUtil.<RongClubListBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<RongClubListBean>() {
                    @Override
                    public void onNext(RongClubListBean rongClubListBean) {
                        if (rongClubListBean.getCode() == 1){
                            if (null != rongClubListBean.getObj().getCreate()
                                    && null != rongClubListBean.getObj().getJoin()) {
                                if (rongClubListBean.getObj().getCreate().size() == 0
                                    && rongClubListBean.getObj().getJoin().size() == 0){
                                    mView.getNoclubHintLayout().setVisibility(View.VISIBLE);
                                }else {
                                    mView.getNoclubHintLayout().setVisibility(View.GONE);
                                }
                                if (rongClubListBean.getObj().getCreate().size() == 0){
                                    mView.getCreateTV().setVisibility(View.GONE);
                                }else {
                                    mView.getCreateTV().setVisibility(View.VISIBLE);
                                }
                                if (rongClubListBean.getObj().getJoin().size() == 0){
                                    mView.getJoinTV().setVisibility(View.GONE);
                                }else {
                                    mView.getJoinTV().setVisibility(View.VISIBLE);
                                }
                                rongClubCreateAdapter.setDataList(rongClubListBean.getObj().getCreate());
                                rongClubJoinAdapter.setDataList(rongClubListBean.getObj().getJoin());
                            }
                        }else if (rongClubListBean.getCode() == 19) {
                            DialogUtils.reLoginDialog(mContext);
                        }
                    }
                });

    }
}
