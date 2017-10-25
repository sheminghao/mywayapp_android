package com.mywaytec.myway.fragment.praise;

import android.support.v7.widget.LinearLayoutManager;

import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.mywaytec.myway.adapter.PraiseAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.bean.CommentListBean;
import com.mywaytec.myway.model.bean.LikeListBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.view.CommonSubscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/3/9.
 */

public class PraisePresenter extends RxPresenter<PraiseView> {

    RetrofitHelper mRetrofitHelper;
    private PraiseAdapter praiseAdapter;
    private LRecyclerViewAdapter lRecyclerViewAdapter;

    @Inject
    public PraisePresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper =  mRetrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    public void getData(int id) {
        praiseAdapter = new PraiseAdapter(mView.getContext());
        mView.getRecyclerView().setLayoutManager(new LinearLayoutManager(mView.getContext()));
        lRecyclerViewAdapter = new LRecyclerViewAdapter(praiseAdapter);
        mView.getRecyclerView().setAdapter(lRecyclerViewAdapter);
        mView.getRecyclerView().setPullRefreshEnabled(false);
        mView.getRecyclerView().setLoadMoreEnabled(false);

        mRetrofitHelper.getLikeList(id+"")
                .compose(RxUtil.<LikeListBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<LikeListBean>() {
                    @Override
                    public void onNext(LikeListBean likeListBean) {
                        praiseAdapter.setDataList(likeListBean.getObj());
                    }
                });
    }
}
