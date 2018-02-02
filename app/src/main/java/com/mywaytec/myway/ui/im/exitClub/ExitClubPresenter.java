package com.mywaytec.myway.ui.im.exitClub;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.bumptech.glide.Glide;
import com.mywaytec.myway.R;
import com.mywaytec.myway.adapter.ClubDetaiMemberAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.bean.ClubDetailBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.DialogUtils;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.view.CommonSubscriber;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/12/1.
 */

public class ExitClubPresenter extends RxPresenter<ExitClubView>{

    private RetrofitHelper retrofitHelper;
    private Context mContext;
    ClubDetaiMemberAdapter clubDetaiMemberAdapter;
    private String clubHead;

    @Inject
    public ExitClubPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
    }

    @Override
    public void attachView(ExitClubView view) {
        super.attachView(view);
        mContext = mView.getContext();
    }

    public String getClubHead(){
        return clubHead;
    }

    public void loadData(int gid){
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
                            clubHead = clubDetailBean.getObj().getImgUrl();
                            mView.getTitleTV().setText(clubDetailBean.getObj().getGroupname());
                            Glide.with(mContext).load(clubDetailBean.getObj().getImgUrl())
                                    .centerCrop()
                                    .into(mView.getClubHeadImg());
                            mView.getClubNameTV().setText(clubDetailBean.getObj().getGroupname());
                            if (clubDetailBean.getObj().isIsOfficial()){
                                mView.getClubOfficialTV().setVisibility(View.VISIBLE);
                            }else {
                                mView.getClubOfficialTV().setVisibility(View.GONE);
                            }
                            mView.getAreaTV().setText(clubDetailBean.getObj().getCountry()
                                    + clubDetailBean.getObj().getProvince() + clubDetailBean.getObj().getCity());
                            mView.getJianjieTV().setText(clubDetailBean.getObj().getDescription());
                            if (null != clubDetailBean.getObj().getUsers()) {
                                mView.getMemberTV().setText(clubDetailBean.getObj().getUsers().size()
                                        + mContext.getResources().getString(R.string.member));
                                mView.getPeopleNumTV().setText(clubDetailBean.getObj().getUsers().size()
                                        + mContext.getResources().getString(R.string.people));
                            }

                            clubDetaiMemberAdapter.setDataList(clubDetailBean.getObj().getUsers());
                        }
                    }
                });
    }

    //退出俱乐部
    public void exitClub(int gid){
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.exitRongClub(uid, token, gid)
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseInfo>(mView, mContext, true) {
                    @Override
                    public void onNext(BaseInfo baseInfo) {
                        if (baseInfo.getCode() == 1){
//                            ToastUtils.showToast(R.string.successful_applying);
                        }else if (baseInfo.getCode() == 19){
                            DialogUtils.reLoginDialog(mContext);
                        }
                    }
                });
    }
}
