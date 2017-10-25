package com.mywaytec.myway.ui.changePassword;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePasswordActivity extends BaseActivity<ChangePasswordPresenter> implements ChangePasswordView {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_old_password)
    EditText etOldPassword;
    @BindView(R.id.et_new_password)
    EditText etNewPassword;
    @BindView(R.id.et_confirm_password)
    EditText etConfirmPassword;
    @BindView(R.id.tv_right)
    TextView tvRight;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.change_password);
        tvRight.setText(R.string.dialog_confirm);
    }

    @Override
    protected void updateViews() {

    }

    @OnClick({R.id.tv_right})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_right://确定
                mPresenter.changePassword();
                break;
        }
    }

    @Override
    public String getOldPassword() {
        return etOldPassword.getText().toString().trim();
    }

    @Override
    public String getNewPassword() {
        return etNewPassword.getText().toString().trim();
    }

    @Override
    public String getConfirmPassword() {
        return etConfirmPassword.getText().toString().trim();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
