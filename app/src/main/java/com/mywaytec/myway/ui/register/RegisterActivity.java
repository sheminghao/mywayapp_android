package com.mywaytec.myway.ui.register;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.activity.TermsAndPrivacyActivity;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.ui.selectCountry.SelectCountryActivity;
import com.mywaytec.myway.utils.AppUtils;
import com.mywaytec.myway.utils.CodeUtil;
import com.mywaytec.myway.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends BaseActivity<RegisterPresenter> implements RegisterView, View.OnClickListener {

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
    @BindView(R.id.layout_regisyer)
    CoordinatorLayout layoutRegister;
    @BindView(R.id.tv_select_country)
    TextView tvSelectCountry;

    private CodeUtil codeUtil;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_register;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        layoutRegister.setPadding(0, AppUtils.getStatusBar(), 0, 0);
        tvTitle.setText(R.string.register);
        SMSSDK.registerEventHandler(eh); //注册短信回调
        codeUtil = new CodeUtil(this, 0, 0, getAuthcodeTV(), 60);
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
//                    ToastUtils.showToast("验证码已发送，请注意查收！");
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
//                            mPresenter.register();
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
//                            mPresenter.register();
                            ToastUtils.showToast(R.string.the_verification_is_not_correct);
                        }
                    });
                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    //当前手机号发送短信的数量超过限额
//                    tvGetAuthCode.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            ToastUtils.showToast(R.string.more_than_limit_the_number_of_the_current_mobile_phone_number_send_text_messages);
//                        }
//                    });
                }
            }
        }
    };

    @OnClick({R.id.tv_get_auth_code, R.id.tv_register, R.id.tv_agreement, R.id.tv_select_country})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_get_auth_code://获取验证码
                mPresenter.getAuthCode();
                break;
            case R.id.tv_register://注册
                if (TextUtils.isEmpty(getPhoneNumber())){
                    ToastUtils.showToast(R.string.please_enter_the_phone_number);
                    return;
                }
                if (TextUtils.isEmpty(getPassword1())){
                    ToastUtils.showToast(R.string.please_enter_the_password);
                    return;
                }
                if (TextUtils.isEmpty(getPassword2())){
                    ToastUtils.showToast(R.string.please_make_sure_the_password);
                    return;
                }
                if (!getPassword1().equals(getPassword2())){
                    ToastUtils.showToast(R.string.the_password_does_not_match_for_the_two_times);
                    return;
                }
                if (TextUtils.isEmpty(getAuthcode())){
                    ToastUtils.showToast(R.string.please_enter_the_verification_code);
                    return;
                }
//                SMSSDK.submitVerificationCode("86", getPhoneNumber(), getAuthcode());
                mPresenter.register(etAutoCode.getText().toString());
                break;
            case R.id.tv_agreement://条款协议
                startActivity(new Intent(this, TermsAndPrivacyActivity.class));
                break;
            case R.id.tv_select_country://国家区号
                Intent intent = new Intent(this, SelectCountryActivity.class);
                startActivityForResult(intent, SelectCountryActivity.SELCET_COUNTRY);
                break;
        }
    }

    @Override
    public String getPhoneNumber() {
        return etPhoneNumber.getText().toString().trim();
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
    public TextView getAuthcodeTV() {
        return tvGetAuthCode;
    }

    @Override
    public TextView getSelectCountryTV() {
        return tvSelectCountry;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public String getPassword1() {
        return etPassword1.getText().toString().trim();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SelectCountryActivity.SELCET_COUNTRY && resultCode == RESULT_OK){
            String code = data.getStringExtra("code");
            tvSelectCountry.setText("+"+code);
        }
    }
}
