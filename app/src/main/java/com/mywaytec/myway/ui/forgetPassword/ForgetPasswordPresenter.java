package com.mywaytec.myway.ui.forgetPassword;

import android.app.Activity;
import android.text.TextUtils;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CommonSubscriber;

import org.xutils.common.util.MD5;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/3/8.
 */

public class ForgetPasswordPresenter extends RxPresenter<ForgetPasswordView> {

    RetrofitHelper mRetrofitHelper;

    @Inject
    public ForgetPasswordPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    public void resetPassword() {
        if (TextUtils.isEmpty(mView.getPhoneNumber())){
            ToastUtils.showToast(R.string.please_enter_the_phone_number);
            return;
        }
        if (TextUtils.isEmpty(mView.getAuthcode())){
            ToastUtils.showToast(R.string.please_enter_the_verification_code);
            return;
        }
        if (TextUtils.isEmpty(mView.getPassword1())){
            ToastUtils.showToast(R.string.please_enter_the_password);
            return;
        }
        if (TextUtils.isEmpty(mView.getPassword1())){
            ToastUtils.showToast(R.string.please_make_sure_the_password);
            return;
        }

        String pwd = mView.getPassword1();
        //MD5加密
        pwd = MD5.md5(pwd);
        String countryCode = "86";//手机区号默认为中国
        if (mView.getSelectCountryTV().getText().toString().length() > 0) {
            countryCode = mView.getSelectCountryTV().getText().toString().substring(1);
        }
        mRetrofitHelper.findPassword(mView.getPhoneNumber(), countryCode, mView.getAuthcode(), pwd)
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseInfo>(mView, mView.getContext(), true) {
                    @Override
                    public void onNext(BaseInfo baseInfo) {
                        if (baseInfo.getCode() == 220){
                            ToastUtils.showToast(R.string.找回密码成功);
                            ((Activity)mView.getContext()).finish();
                        }else if (baseInfo.getCode() == 221){
                            ToastUtils.showToast(R.string.找回密码失败);
                        }else if (baseInfo.getCode() == 211){
                            ToastUtils.showToast(R.string.用户不存在);
                        }else if (baseInfo.getCode() == 218){
                            ToastUtils.showToast(R.string.验证码校验失败);
                        }else {
                            ToastUtils.showToast(baseInfo.getMsg());
                        }
                    }
                });
    }
}
