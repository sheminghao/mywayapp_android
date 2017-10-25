package com.mywaytec.myway.ui.huodongXiangqing;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CommonSubscriber;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/8/14.
 */

public class HuodongXiangqingPresenter extends RxPresenter<HuodongXiangqingView> {

    RetrofitHelper retrofitHelper;
    Context mContext;

    @Inject
    public HuodongXiangqingPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(HuodongXiangqingView view) {
        super.attachView(view);
        mContext = mView.getContext();
    }

    //参加活动
    public void joinActivity(final TextView tvCanyu, final TextView tvSignin, String aid){
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.joinActivity(uid, token, aid)
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseInfo>(mView, mContext, true) {
                    @Override
                    public void onNext(BaseInfo baseInfo) {
                        if (baseInfo.getCode() == 1){
                            tvCanyu.setText("退出");
                            tvSignin.setVisibility(View.VISIBLE);
                        }else {
                            ToastUtils.showToast(baseInfo.getMsg());
                        }
                    }
                });
    }

    //签到
    public void signin(String aid, final TextView tvSignin){
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.signinActivity(uid, token, aid)
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseInfo>() {
                    @Override
                    public void onNext(BaseInfo baseInfo) {
                        if (baseInfo.getCode() == 1){
                            tvSignin.setText("已签到");
                            tvSignin.setBackgroundResource(R.mipmap.yueban_btn_yiguoqi);
                        }else {
                            ToastUtils.showToast(baseInfo.getMsg());
                        }
                    }
                });
    }

    //退出活动
    public void exitActivity(final TextView tvCanyu, final TextView tvSignin, String aid){
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.exitActivity(uid, token, aid)
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseInfo>() {
                    @Override
                    public void onNext(BaseInfo baseInfo) {
                        if (baseInfo.getCode() == 1){
                            tvCanyu.setText("参与活动");
                            tvSignin.setVisibility(View.GONE);
                        }else {
                            ToastUtils.showToast(baseInfo.getMsg());
                        }
                    }
                });
    }
}
