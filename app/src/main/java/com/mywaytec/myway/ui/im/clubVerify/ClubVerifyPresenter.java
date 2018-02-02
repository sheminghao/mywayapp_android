package com.mywaytec.myway.ui.im.clubVerify;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.mywaytec.myway.adapter.ClubVerifyAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.bean.ClubJoinListBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.view.CommonSubscriber;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/12/5.
 */

public class ClubVerifyPresenter extends RxPresenter<ClubVerifyView> {

    private RetrofitHelper retrofitHelper;
    private Context mContext;

    private ClubVerifyAdapter clubVerifyAdapter;

    @Inject
    public ClubVerifyPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
    }

    @Override
    public void attachView(ClubVerifyView view) {
        super.attachView(view);
        mContext = mView.getContext();
    }

    public void initRecyclerView(){
        clubVerifyAdapter = new ClubVerifyAdapter(mContext);
        mView.getRecyclerView().setLayoutManager(new LinearLayoutManager(mContext));
        mView.getRecyclerView().setAdapter(clubVerifyAdapter);

        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.clubJoinList(uid, token)
                .compose(RxUtil.<ClubJoinListBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ClubJoinListBean>() {
                    @Override
                    public void onNext(ClubJoinListBean clubJoinListBean) {
                        if (clubJoinListBean.getCode() == 1){
                            clubVerifyAdapter.setDataList(clubJoinListBean.getObj());
                        }
                    }
                });

        //俱乐部申请审核
        clubVerifyAdapter.setOnApplyListener(new ClubVerifyAdapter.OnApplyListener() {
            @Override
            public void agreement(final int pos) {
                String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
                String token = PreferencesUtils.getLoginInfo().getObj().getToken();
                retrofitHelper.agreeJoinClub(uid, token, clubVerifyAdapter.getDataList().get(pos).getUser().getUid(),
                        clubVerifyAdapter.getDataList().get(pos).getClub().getGroupid())
                        .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                        .subscribe(new CommonSubscriber<BaseInfo>() {
                            @Override
                            public void onNext(BaseInfo baseInfo) {
                                if (baseInfo.getCode() == 1){
                                    clubVerifyAdapter.getDataList().get(pos).setStatus(1);
                                    clubVerifyAdapter.notifyItemChanged(pos);
                                }
                            }
                        });
            }

            @Override
            public void jujue(final int pos) {
                String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
                String token = PreferencesUtils.getLoginInfo().getObj().getToken();
                retrofitHelper.denyJoinClub(uid, token, clubVerifyAdapter.getDataList().get(pos).getUser().getUid(),
                        clubVerifyAdapter.getDataList().get(pos).getClub().getGroupid())
                        .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                        .subscribe(new CommonSubscriber<BaseInfo>() {
                            @Override
                            public void onNext(BaseInfo baseInfo) {
                                if (baseInfo.getCode() == 1){
                                    clubVerifyAdapter.getDataList().get(pos).setStatus(2);
                                    clubVerifyAdapter.notifyItemChanged(pos);
                                }
                            }
                        });
            }
        });
    }
}
