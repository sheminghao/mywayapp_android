package com.mywaytec.myway.ui.changePassword;

import android.app.Activity;
import android.text.TextUtils;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.ui.main.MainView;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CommonSubscriber;
import com.mywaytec.myway.view.LoadingDialog;

import org.xutils.common.util.MD5;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by shemh on 2017/3/8.
 */

public class ChangePasswordPresenter extends RxPresenter<ChangePasswordView> {

    RetrofitHelper retrofitHelper;
    LoadingDialog loadingDialog;

    @Inject
    public ChangePasswordPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    public void changePassword(){
        String oldPassword = mView.getOldPassword();
        String newPassword = mView.getNewPassword();
        if(TextUtils.isEmpty(oldPassword)){
            ToastUtils.showToast(R.string.please_enter_the_password);
            return;
        }
        if(TextUtils.isEmpty(newPassword)){
            ToastUtils.showToast(R.string.please_enter_the_new_password);
            return;
        }
        if(TextUtils.isEmpty(mView.getConfirmPassword())){
            ToastUtils.showToast(R.string.please_make_sure_the_new_password);
            return;
        }
        if (!newPassword.equals(mView.getConfirmPassword())){
            ToastUtils.showToast(R.string.the_password_does_not_match_for_the_two_times);
            return;
        }
        oldPassword = MD5.md5(oldPassword);
        newPassword = MD5.md5(newPassword);

        String phoneNumber = "";
        if (null != PreferencesUtils.getLoginInfo()) {
            phoneNumber = PreferencesUtils.getLoginInfo().getObj().getPhonenumber();
        }

        retrofitHelper.changePassword(phoneNumber, oldPassword, newPassword)
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseInfo>(mView, mView.getContext(), true) {
                    @Override
                    public void onNext(BaseInfo baseInfo) {
                        if (baseInfo.getCode() == 215){
                            ToastUtils.showToast(R.string.修改成功);
                            ((Activity)mView.getContext()).finish();
                        }else if (baseInfo.getCode() == 214){
                            ToastUtils.showToast(R.string.修改失败);
                        }else if (baseInfo.getCode() == 211){
                            ToastUtils.showToast(R.string.account_or_password_is_not_correct);
                        }
                    }
                });
    }
}
