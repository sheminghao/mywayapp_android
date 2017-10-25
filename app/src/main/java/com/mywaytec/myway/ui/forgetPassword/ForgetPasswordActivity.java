package com.mywaytec.myway.ui.forgetPassword;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.utils.AppUtils;
import com.mywaytec.myway.utils.CodeUtil;
import com.mywaytec.myway.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ForgetPasswordActivity extends BaseActivity<ForgetPasswordPresenter> implements ForgetPasswordView {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_auto_code)
    EditText etAutoCode;
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.et_password_1)
    EditText etPassword1;
    @BindView(R.id.et_password_2)
    EditText etPassword2;
    @BindView(R.id.tv_get_auth_code)
    TextView tvGetAuthCode;
    @BindView(R.id.layout_forget_password)
    CoordinatorLayout layoutForgetPassword;

    private CodeUtil codeUtil;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        layoutForgetPassword.setPadding(0, AppUtils.getStatusBar(), 0, 0);
        tvTitle.setText(getResources().getString(R.string.forget_password));
        codeUtil = new CodeUtil(this, 0, 0, tvGetAuthCode, 60);
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    @Override
    protected void updateViews() {

    }

    EventHandler eh=new EventHandler(){
        @Override
        public void afterEvent(int event, int result, Object data) {
            Log.i("TAG", "-------result"+result+"==data"+data.toString()+"==event"+event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
                    Log.i("TAG", "-------result"+result);
                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    //获取验证码成功
                    Log.i("TAG", "-------result"+result);
                    codeUtil.start();
                    boolean smart = (Boolean)data;
                    if(smart) {
                        //通过智能验证
                    } else {
                        //依然走短信验证
                    }
                }else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                    //返回支持发送验证码的国家列表
                    Log.i("TAG", "-------result"+result);
                }else if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                    //校验验证码，返回校验的手机和国家代码
                    Log.i("TAG", "-------result"+result+"==data"+data.toString());
                    //验证码校验成功
                    tvGetAuthCode.post(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                }
            }else{
                ((Throwable)data).printStackTrace();
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                    //验证码校验失败，验证码错误
                    tvGetAuthCode.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(R.string.the_verification_is_not_correct);
                        }
                    });
                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    //当前手机号发送短信的数量超过限额
                    tvGetAuthCode.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(R.string.more_than_limit_the_number_of_the_current_mobile_phone_number_send_text_messages);
                        }
                    });
                }
            }
        }
    };

    @OnClick({R.id.tv_get_auth_code, R.id.tv_confirm})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_get_auth_code://获取验证码
                if (TextUtils.isEmpty(etPhoneNumber.getText().toString().trim())){
                    ToastUtils.showToast(R.string.please_enter_the_phone_number);
                    return;
                }
                if (etPhoneNumber.getText().toString().trim().length() != 11){
                    ToastUtils.showToast(R.string.please_enter_a_correct_phone_number);
                    return;
                }
                codeUtil.start();
                SMSSDK.getVerificationCode("86", getPhoneNumber());
                break;
            case R.id.tv_confirm://确定
                mPresenter.resetPassword();
                break;
        }
    }

    @Override
    public String getPhoneNumber() {
        return etPhoneNumber.getText().toString().trim();
    }

    @Override
    public String getPassword1() {
        return etPassword1.getText().toString().trim();
    }

    @Override
    public String getPassword2() {
        return etPassword2.getText().toString().trim();
    }

    @Override
    public String getAuthcode() {
        return etAutoCode.getText().toString().trim();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }
}
