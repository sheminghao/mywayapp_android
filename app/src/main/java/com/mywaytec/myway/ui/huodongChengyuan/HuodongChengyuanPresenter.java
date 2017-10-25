package com.mywaytec.myway.ui.huodongChengyuan;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mywaytec.myway.R;
import com.mywaytec.myway.adapter.HuodongChengyuanAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.bean.AllPeopleBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.ui.userDynamic.UserDynamicActivity;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CommonSubscriber;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/8/15.
 */

public class HuodongChengyuanPresenter extends RxPresenter<HuodongChengyuanView> {

    RetrofitHelper retrofitHelper;
    Context mContext;
    HuodongChengyuanAdapter huodongChengyuanAdapter;

    @Inject
    public HuodongChengyuanPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(HuodongChengyuanView view) {
        super.attachView(view);
        mContext = mView.getContext();
    }

    public void initRecyclerView(String aid){
        huodongChengyuanAdapter = new HuodongChengyuanAdapter(mContext);
        mView.getRecyclerView().setLayoutManager(new LinearLayoutManager(mContext));
        mView.getRecyclerView().setAdapter(huodongChengyuanAdapter);

        retrofitHelper.allPeopleActivity(aid)
                .compose(RxUtil.<AllPeopleBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<AllPeopleBean>() {
                    @Override
                    public void onNext(final AllPeopleBean allPeopleBean) {
                        if (allPeopleBean.getCode() == 1){
                            huodongChengyuanAdapter.setDataList(allPeopleBean.getObj().getParticipant());
                            mView.getNameTV().setText(allPeopleBean.getObj().getLeader().getNickname());

                            ImageView imgHeadPortrait = mView.getHeadPortraitImg();
                            ImageView imgGender = mView.getGanderImg();
                            if (allPeopleBean.getObj().getLeader().isGender()) {
                                Glide.with(mContext).load(R.mipmap.cebianlan_fujinren_nan_icon)
                                        .into(imgGender);
                                Glide.with(mContext).load(allPeopleBean.getObj().getLeader().getImgeUrl())
                                        .error(R.mipmap.touxiang_boy_nor)
                                        .into(imgHeadPortrait);
                            }else {
                                Glide.with(mContext).load(R.mipmap.cebianlan_fujinren_nv_icon)
                                        .into(imgGender);
                                Glide.with(mContext).load(allPeopleBean.getObj().getLeader().getImgeUrl())
                                        .error(R.mipmap.touxiang_girl_nor)
                                        .into(imgHeadPortrait);
                            }

                            mView.getDuizhangLayout().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mContext, UserDynamicActivity.class);
                                    intent.putExtra("otherUid", allPeopleBean.getObj().getLeader().getUid());
                                    mContext.startActivity(intent);
                                }
                            });
                        }else {
                            ToastUtils.showToast(allPeopleBean.getMsg());
                        }
                    }
                });
    }
}
