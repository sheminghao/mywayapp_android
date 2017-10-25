package com.mywaytec.myway.ui.register;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CommonSubscriber;

import org.xutils.common.util.MD5;

import javax.inject.Inject;

import cn.smssdk.SMSSDK;

/**
 * Created by shemh on 2017/3/8.
 */

public class RegisterPresenter extends RxPresenter<RegisterView> {

    RetrofitHelper mRetrofitHelper;

    @Inject
    public RegisterPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
        registerEvent();
    }

    void registerEvent() {

    }

    //获取验证码
    public void getAuthCode(){
        if (TextUtils.isEmpty(mView.getPhoneNumber())){
            ToastUtils.showToast(R.string.please_enter_the_phone_number);
            return;
        }
        if (mView.getPhoneNumber().length() != 11){
            ToastUtils.showToast(R.string.please_enter_a_correct_phone_number);
            return;
        }
        SMSSDK.getVerificationCode("86", mView.getPhoneNumber());
    }

    //注册
    public void register(String code) {
        String pwd = mView.getPassword1();
        if (pwd.length() < 8 || pwd.length() > 20){
            ToastUtils.showToast(R.string.the_long_of_the_password_is_not_correct);
            return;
        }
        //MD5加密
        pwd = MD5.md5(pwd);
        mRetrofitHelper.registerInfo(mView.getPhoneNumber(), "86",pwd, code)
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseInfo>(mView, mView.getContext(), true) {
                    @Override
                    public void onNext(BaseInfo baseInfo) {
                        Log.i("TAG", "-----" + baseInfo.getMsg());
                        if (baseInfo.getCode() == 200){
                            ToastUtils.showToast(R.string.registered_successfully);
                            ((Activity)mView.getContext()).finish();
                        }else if (baseInfo.getCode() == 201){
                            ToastUtils.showToast(R.string.registration_failed);
                        }else if (baseInfo.getCode() == 202){
                            ToastUtils.showToast(R.string.the_phone_or_email_is_not_correct);
                        }else if (baseInfo.getCode() == -100){
                            ToastUtils.showToast(R.string.the_phone_is_already_registered);
                        }else {
                            ToastUtils.showToast(baseInfo.getMsg());
                        }
                    }
                });
    }
}
