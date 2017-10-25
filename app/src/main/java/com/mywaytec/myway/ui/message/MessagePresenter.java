package com.mywaytec.myway.ui.message;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.mywaytec.myway.adapter.MessageAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.bean.MessageBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.ui.messageDetail.MessageDetailActivity;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CommonSubscriber;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/4/11.
 */

public class MessagePresenter extends RxPresenter<MessageView> {

    RetrofitHelper retrofitHelper;
    MessageAdapter messageAdapter;
    LRecyclerViewAdapter lRecyclerViewAdapter;
    LRecyclerView lRecyclerView;
    Context mContext;

    @Inject
    public MessagePresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(MessageView view) {
        super.attachView(view);
        lRecyclerView = mView.getRecyclerView();
        mContext = mView.getContext();
    }

    public void initList(){
        messageAdapter = new MessageAdapter(mContext, retrofitHelper);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(messageAdapter);
        lRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        lRecyclerView.setAdapter(lRecyclerViewAdapter);
        lRecyclerView.setPullRefreshEnabled(false);
        lRecyclerView.setLoadMoreEnabled(false);
        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
                retrofitHelper.readMessage(uid, messageAdapter.getDataList().get(position).getId()+"")
                        .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                        .subscribe(new CommonSubscriber<BaseInfo>() {
                            @Override
                            public void onNext(BaseInfo baseInfo) {
                                Log.i("TAG", "----阅读消息"+baseInfo.getCode());
                                if (baseInfo.getCode() == 355){
                                    Intent intent = new Intent(mContext, MessageDetailActivity.class);
                                    intent.putExtra("shid", messageAdapter.getDataList().get(position).getShId());
                                    mContext.startActivity(intent);
                                }
                            }
                        });
            }
        });

        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        retrofitHelper.getUnreadMessageList(uid)
                .compose(RxUtil.<MessageBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<MessageBean>(mView, mContext, true) {
                    @Override
                    public void onNext(MessageBean messageBean) {
                        if (messageBean.getCode() == 350){
                            if (null != messageBean.getObj() && messageBean.getObj().size() > 0){
                                mView.getNoneTV().setVisibility(View.GONE);
                            }else {
                                mView.getNoneTV().setVisibility(View.VISIBLE);
                            }
                            messageAdapter.setDataList(messageBean.getObj());
                        }else{
                            ToastUtils.showToast(messageBean.getMsg());
                        }
                    }
                });
    }

}
