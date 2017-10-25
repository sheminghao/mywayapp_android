package com.mywaytec.myway.ui.login;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mywaytec.myway.AppManager;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.ui.forgetPassword.ForgetPasswordActivity;
import com.mywaytec.myway.ui.register.RegisterActivity;
import com.mywaytec.myway.utils.AppUtils;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.data.LoginStyle;
import com.zhy.autolayout.AutoRelativeLayout;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginView, View.OnClickListener, TextWatcher {

    /**
     * 账号
     */
    public static final String ACCOUNT = "ACCOUNT";

    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_password)
    TextView tvPassword;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.img_account_clear)
    ImageView imgAccountClear;
    @BindView(R.id.img_pwd_clear)
    ImageView imgPwdClear;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @BindView(R.id.img_weixin_login)
    ImageView imgWeixinLogin;
    @BindView(R.id.img_qq_login)
    ImageView imgQqLogin;
    @BindView(R.id.layout_login_logo)
    AutoRelativeLayout layoutLoginLogo;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        layoutLoginLogo.setPadding(0, AppUtils.getStatusBar(), 0, 0);
        //初始化ShareSDK
        ShareSDK.initSDK(this);

        etAccount.addTextChangedListener(this);
        etPassword.addTextChangedListener(this);

        String account = PreferencesUtils.getString(this, ACCOUNT);
        if (!TextUtils.isEmpty(account)){
            etAccount.setText(account);
        }
    }

    @Override
    protected void updateViews() {
    }

    @OnClick({R.id.tv_login, R.id.img_account_clear, R.id.img_pwd_clear, R.id.tv_register,
            R.id.tv_forget_password, R.id.img_weixin_login, R.id.img_qq_login})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_login://登陆
                mPresenter.login();
                LoginStyle.saveLoginStyle(LoginStyle.PHONE);
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                finish();
                break;
            case R.id.img_account_clear://清除账户
                etAccount.setText("");
                break;
            case R.id.img_pwd_clear://清除密码
                etPassword.setText("");
                break;
            case R.id.tv_register://注册
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.tv_forget_password://忘记密码
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
            case R.id.img_weixin_login://微信登录
                mPresenter.wechatLogin();
                LoginStyle.saveLoginStyle(LoginStyle.WECHAT);
                break;
            case R.id.img_qq_login://QQ登录
                mPresenter.qqLogin();
                LoginStyle.saveLoginStyle(LoginStyle.QQLOGIN);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(s.toString())){
            if (s.toString().equals(etAccount.getText().toString())){
//                imgAccountClear.setVisibility(View.VISIBLE);
            }else if(s.toString().equals(etPassword.getText().toString())){
//                imgPwdClear.setVisibility(View.VISIBLE);
            }
        }else {
            if (TextUtils.isEmpty(etAccount.getText().toString())){
                imgAccountClear.setVisibility(View.GONE);
            }else if(TextUtils.isEmpty(etPassword.getText().toString())){
                imgPwdClear.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public String getAccountName() {
        return etAccount.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return etPassword.getText().toString().trim();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), R.string.press_again_to_quit_app, Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                AppManager.getAppManager().AppExit(LoginActivity.this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
