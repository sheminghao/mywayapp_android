package com.mywaytec.myway.ui.blacklist;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;

import com.luck.picture.lib.decoration.RecycleViewDivider;
import com.mywaytec.myway.adapter.BlacklistAdapter;
import com.mywaytec.myway.adapter.MycarInfoAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.bean.BlacklistBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.DialogUtils;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.utils.indexlib.DividerItemDecoration;
import com.mywaytec.myway.utils.indexlib.suspension.SuspensionDecoration;
import com.mywaytec.myway.view.CommonSubscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/3/8.
 */

public class BlacklistPresenter extends RxPresenter<BlacklistView> {

    RetrofitHelper retrofitHelper;
    Context mContext;
    BlacklistAdapter blacklistAdapter;
    LinearLayoutManager mManager;
    private SuspensionDecoration mDecoration;

    @Inject
    public BlacklistPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(BlacklistView view) {
        super.attachView(view);
        mContext = mView.getContext();
    }

    String uid;
    String token;
    public void initRecyclerView(){
        uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        token = PreferencesUtils.getLoginInfo().getObj().getToken();
        blacklistAdapter = new BlacklistAdapter(mView.getContext());
        mView.getRecyclerView().setLayoutManager(mManager = new LinearLayoutManager(mView.getContext()));
        mView.getRecyclerView().setAdapter(blacklistAdapter);
        blacklistAdapter.setOnDelListener(new BlacklistAdapter.onSwipeListener() {
            @Override
            public void onDel(final int pos) {
                retrofitHelper.deleteBlackList(uid, token, blacklistAdapter.getDataList().get(pos).getShieldUid())
                        .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                        .subscribe(new CommonSubscriber<BaseInfo>() {
                            @Override
                            public void onNext(BaseInfo baseInfo) {
                                if (baseInfo.getCode() == 1){
                                    blacklistAdapter.remove(pos);
                                }else if (baseInfo.getCode() == 19){
                                    DialogUtils.reLoginDialog(mContext);
                                }
                            }
                        });
            }
        });

        retrofitHelper.blackList(uid, token)
                .compose(RxUtil.<BlacklistBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BlacklistBean>() {
                    @Override
                    public void onNext(BlacklistBean blacklistBean) {
                        if (blacklistBean.getCode() == 1){
                            blacklistAdapter.setDataList(blacklistBean.getObj());
                        }else {
                            ToastUtils.showToast(blacklistBean.getMsg());
                        }
                    }
                });


//        mView.getRecyclerView().addItemDecoration(mDecoration = new SuspensionDecoration(mView.getContext(), list));
//        //如果add两个，那么按照先后顺序，依次渲染。
//        mView.getRecyclerView().addItemDecoration(new DividerItemDecoration(mView.getContext(), DividerItemDecoration.VERTICAL_LIST));

        //indexbar初始化
//        mView.getIndexBar().setmPressedShowTextView(mView.getSideBarHintTV())//设置HintTextView
//                .setNeedRealIndex(false)//设置需要真实的索引
//                .setmLayoutManager(mManager);//设置RecyclerView的LayoutManager
//        mView.getIndexBar().getDataHelper().sortSourceDatas(list);
//        blacklistAdapter.setDataList(list);
//        mView.getIndexBar()
//                .setmSourceDatas(list)//设置数据
//                .invalidate();
//        mDecoration.setmDatas(list);
    }
}
