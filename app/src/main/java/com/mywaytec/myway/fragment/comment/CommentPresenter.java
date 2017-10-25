package com.mywaytec.myway.fragment.comment;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.mywaytec.myway.adapter.CommentAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.bean.CommentListBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.view.CommonSubscriber;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/3/9.
 */

public class CommentPresenter extends RxPresenter<CommentView> {

    RetrofitHelper mRetrofitHelper;

    private CommentAdapter commentAdapter;
    private LRecyclerViewAdapter lRecyclerViewAdapter;

    @Inject
    public CommentPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper =  mRetrofitHelper;
        registerEvent();
    }

    void registerEvent() {
        getData();
    }

    public void getData() {

    }

    public void init(int id){
        commentAdapter = new CommentAdapter(mView.getContext());
        mView.getRecyclerView().setLayoutManager(new LinearLayoutManager(mView.getContext()));
        lRecyclerViewAdapter = new LRecyclerViewAdapter(commentAdapter);
        mView.getRecyclerView().setAdapter(lRecyclerViewAdapter);
        mView.getRecyclerView().setPullRefreshEnabled(false);
        mView.getRecyclerView().setLoadMoreEnabled(false);

        mRetrofitHelper.getCommentList(id+"")
                .compose(RxUtil.<CommentListBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<CommentListBean>() {
                    @Override
                    public void onNext(CommentListBean commentListBean) {
                        commentAdapter.setDataList(commentListBean.getObj());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Log.i("TAG", "------CommentPresenter, 评论加载失败，"+e.getMessage());
                    }
                });
    }
}
