package com.mywaytec.myway.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.bean.LoginInfo;
import com.mywaytec.myway.model.bean.OAuthInfo;
import com.mywaytec.myway.model.bean.QQLoginInfo;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.ui.main.MainActivity;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.utils.data.IsLogin;
import com.mywaytec.myway.utils.data.LoginStyle;
import com.mywaytec.myway.view.CommonSubscriber;
import com.mywaytec.myway.view.LoadingDialog;

import org.xutils.common.util.MD5;

import java.util.HashMap;

import javax.inject.Inject;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import static com.mywaytec.myway.ui.login.LoginActivity.ACCOUNT;

/**
 * Created by shemh on 2017/3/8.
 */

public class LoginPresenter extends RxPresenter<LoginView> {

    private RetrofitHelper retrofitHelper;
    private LoadingDialog loadingDialog;

    @Inject
    public LoginPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    public void login() {
        final String account = mView.getAccountName();
        String pwd = mView.getPassword();
        if (TextUtils.isEmpty(account)) {
            ToastUtils.showToast(R.string.please_enter_the_phone_number);
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showToast(R.string.please_enter_the_password);
            return;
        }

        //MD5加密
        pwd = MD5.md5(pwd);
        retrofitHelper.loginInfo(account, pwd)
                .compose(RxUtil.<LoginInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<LoginInfo>(mView, mView.getContext(), true) {
                    @Override
                    public void onNext(LoginInfo loginInfo) {
                        Log.i("TAG", "-----" + loginInfo.getMsg());
                        if (loginInfo.getCode() == 210) {
                            ToastUtils.showToast(R.string.login_successful);
                            Log.i("TAG", "-----uid" + loginInfo.getObj().getUid()+",token:"+loginInfo.getObj().getToken());
                            PreferencesUtils.saveLoginInfo(loginInfo);
                            IsLogin.saveDynamicData(true);
                            PreferencesUtils.putString(mView.getContext(), ACCOUNT, account);
                            mView.getContext().startActivity(new Intent(mView.getContext(), MainActivity.class));
                            ((Activity) mView.getContext()).finish();
                        } else if (loginInfo.getCode() == 211) {
                            ToastUtils.showToast(R.string.account_or_password_is_not_correct);
                        }
                    }
                });
    }

    /**
     * 微信登录
     */
    public void wechatLogin() {
        platform = 3;
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        if (!wechat.isClientValid()) {
            ToastUtils.showToast("请安装微信客户端");
            return;
        }
        loadingDialog = new LoadingDialog(mView.getContext());
        loadingDialog.show();
        wechat.setPlatformActionListener(paListener);
        //关闭SSO授权
        wechat.SSOSetting(true);
        wechat.showUser(null);//执行登录，登录后在回调里面获取用户资料
        wechat.removeAccount(true);
    }

    /**
     * QQ登录
     */
    public void qqLogin() {
        platform = 2;
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
//        if (!qq.isClientValid()) {
//            ToastUtils.showToast("请安装QQ客户端");
//            return;
//        }
        loadingDialog = new LoadingDialog(mView.getContext());
        loadingDialog.show();
        qq.setPlatformActionListener(paListener);
        qq.showUser(null);//执行登录，登录后在回调里面获取用户资料
        qq.removeAccount(true);
    }

    private int platform = 2;

    PlatformActionListener paListener = new PlatformActionListener() {

        @Override
        public void onError(Platform arg0, int arg1, Throwable arg2) {
            Log.i("TAG", "======获取第三方登录信息失败");
            if (null != loadingDialog)
                loadingDialog.dismiss();
//            arg2.printStackTrace();
            Log.i("TAG", "======" + arg2.getMessage());
        }

        @Override
        public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
            Log.i("TAG", "-----获取第三方登录信息成功" + arg0.getDb().exportData());
            //输出所有授权信息
            if(LoginStyle.WECHAT.equals(LoginStyle.getLoginStyle())) {
                OAuthInfo oAuthInfo = new Gson().fromJson(arg0.getDb().exportData(), OAuthInfo.class);
                if (null != oAuthInfo) {
                    retrofitHelper.oauthLogin(oAuthInfo.getUnionid(), oAuthInfo.getNickname(),
                            oAuthInfo.getIcon(), platform + "")
                            .compose(RxUtil.<LoginInfo>rxSchedulerHelper())
                            .subscribe(new CommonSubscriber<LoginInfo>() {
                                @Override
                                public void onNext(LoginInfo loginInfo) {
                                    Log.i("TAG", "-----wechatLogin" + loginInfo.getCode());
                                    if (loginInfo.getCode() == 210) {
                                        ToastUtils.showToast(loginInfo.getMsg());
                                        Log.i("TAG", "-----uid" + loginInfo.getObj().getUid() + ",token:" + loginInfo.getObj().getToken());
                                        PreferencesUtils.saveLoginInfo(loginInfo);
                                        IsLogin.saveDynamicData(true);
                                        mView.getContext().startActivity(new Intent(mView.getContext(), MainActivity.class));
                                        ((Activity) mView.getContext()).finish();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                    Log.i("TAG", "-----wechatLoginError" + e.getMessage());
                                }
                            });
                } else {
                    Log.i("TAG", "-----oAuthInfo为空");
                }
            }else if(LoginStyle.QQLOGIN.equals(LoginStyle.getLoginStyle())){
                QQLoginInfo qqLoginInfo = new Gson().fromJson(arg0.getDb().exportData(), QQLoginInfo.class);
                if (null != qqLoginInfo) {
                    retrofitHelper.oauthLogin(qqLoginInfo.getUserID(), qqLoginInfo.getNickname(),
                            qqLoginInfo.getIcon(), platform + "")
                            .compose(RxUtil.<LoginInfo>rxSchedulerHelper())
                            .subscribe(new CommonSubscriber<LoginInfo>() {
                                @Override
                                public void onNext(LoginInfo loginInfo) {
                                    Log.i("TAG", "-----qqLogin" + loginInfo.getCode());
                                    if (loginInfo.getCode() == 210) {
                                        ToastUtils.showToast(loginInfo.getMsg());
                                        Log.i("TAG", "-----uid" + loginInfo.getObj().getUid() + ",token:" + loginInfo.getObj().getToken());
                                        PreferencesUtils.saveLoginInfo(loginInfo);
                                        IsLogin.saveDynamicData(true);
                                        mView.getContext().startActivity(new Intent(mView.getContext(), MainActivity.class));
                                        ((Activity) mView.getContext()).finish();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                    Log.i("TAG", "-----wechatLoginError" + e.getMessage());
                                }
                            });
                } else {
                    Log.i("TAG", "-----oAuthInfo为空");
                }
            }
            if (null != loadingDialog) {
                loadingDialog.dismiss();
            }
        }

        @Override
        public void onCancel(Platform arg0, int arg1) {
            if (null != loadingDialog)
                loadingDialog.dismiss();
            Log.i("TAG", "======取消第三方登录" + arg0.getDb().exportData());
        }
    };
}
