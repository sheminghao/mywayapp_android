package com.mywaytec.myway.ui.im.JoinClub;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.mywaytec.myway.R;
import com.mywaytec.myway.adapter.ClubDetaiMemberAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.bean.ClubDetailBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.DialogUtils;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CommonSubscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/12/1.
 */

public class JoinClubPresenter extends RxPresenter<JoinClubView>{

    private RetrofitHelper retrofitHelper;
    private Context mContext;
    ClubDetaiMemberAdapter clubDetaiMemberAdapter;

    @Inject
    public JoinClubPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
    }

    @Override
    public void attachView(JoinClubView view) {
        super.attachView(view);
        mContext = mView.getContext();
    }

    public void loadMember(int gid){
        clubDetaiMemberAdapter = new ClubDetaiMemberAdapter(mContext);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        mView.getRecyclerView().setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mView.getRecyclerView().setAdapter(clubDetaiMemberAdapter);

        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.rongClubDetail(uid, token, gid)
                .compose(RxUtil.<ClubDetailBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ClubDetailBean>() {
                    @Override
                    public void onNext(ClubDetailBean clubDetailBean) {
                        if (clubDetailBean.getCode() == 1){
                            clubDetaiMemberAdapter.setDataList(clubDetailBean.getObj().getUsers());
                        }
                    }
                });
    }

    //申请加入俱乐部
    public void joinClub(int gid){
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.joinRongClub(uid, token, gid)
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseInfo>(mView, mContext, true) {
                    @Override
                    public void onNext(BaseInfo baseInfo) {
                        if (baseInfo.getCode() == 1){
                            ToastUtils.showToast(R.string.successful_applying);
                        }else if (baseInfo.getCode() == 19){
                            DialogUtils.reLoginDialog(mContext);
                        }else if (baseInfo.getCode() == -2) {
                            ToastUtils.showToast(R.string.you_are_a_member_already);
                        }
                    }
                });
    }
}
