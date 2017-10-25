package com.mywaytec.myway.ui.feedback;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.DialogUtils;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CommonSubscriber;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/3/8.
 */

public class FeedbackPresenter extends RxPresenter<FeedbackView> {

    RetrofitHelper retrofitHelper;

    @Inject
    public FeedbackPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    public void feedback(final Context context, String content){
        if (TextUtils.isEmpty(content)){
            ToastUtils.showToast(R.string.说点什么吧);
            return;
        }
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.feedback(uid, token, content, true)
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseInfo>() {
                    @Override
                    public void onNext(BaseInfo baseInfo) {
                        Log.i("TAG", "------意见反馈"+baseInfo.getCode());
                        if (baseInfo.getCode() == 1){
                            ToastUtils.showToast(R.string.submit_successfully);
                            ((Activity)context).finish();
                        }else if (baseInfo.getCode() == 233){
                            DialogUtils.reLoginDialog(mView.getContext());
                        }
                    }
                });
    }
}
