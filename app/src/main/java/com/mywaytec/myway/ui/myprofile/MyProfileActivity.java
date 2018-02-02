package com.mywaytec.myway.ui.myprofile;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.ui.changePassword.ChangePasswordActivity;
import com.mywaytec.myway.utils.data.LoginStyle;
import com.mywaytec.myway.view.CircleImageView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的资料
 */
public class MyProfileActivity extends BaseActivity<MyProfilePresenter> implements MyProfileView {

    public static final String MYPROFILE = "MYPROFILE";
    public static final int MYPROFILE_CODE = 0x132;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.img_head_portrait)
    CircleImageView imgHeadPortrait;
    @BindView(R.id.et_nickname)
    EditText etNickname;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_signature)
    EditText etSignature;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.et_profession)
    EditText etProfession;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.layout_sex)
    LinearLayout layoutSex;
    @BindView(R.id.layout_birthday)
    LinearLayout layoutBirthday;
    @BindView(R.id.layout_select_head)
    LinearLayout layoutSelectHead;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_gold)
    TextView tvGold;
    @BindView(R.id.tv_integral)
    TextView tvIntegral;
    @BindView(R.id.view_change_password)
    View viewChangePassword;
    @BindView(R.id.layout_change_password)
    LinearLayout layoutChangePassword;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_my_profile;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.my_profile);
        tvRight.setText(R.string.complete);
        setEditable(true);
        mPresenter.getUserInfo();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void updateViews() {

    }

    @OnClick({R.id.layout_select_head, R.id.tv_right, R.id.layout_sex, R.id.layout_birthday,
            R.id.layout_change_password})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.layout_select_head://选择头像
                mPresenter.selectHeadPortrait();
                break;
            case R.id.tv_right:
                if (getResources().getString(R.string.amend).equals(tvRight.getText().toString())){
                    tvRight.setText(R.string.complete);
                    setEditable(true);
                }else if (getResources().getString(R.string.complete).equals(tvRight.getText().toString())){
                    tvRight.setText(R.string.complete);
                    mPresenter.changeUserInfo();
                    setEditable(true);
                }
                break;
            case R.id.layout_sex://选择性别
                mPresenter.switchSex();
                break;
            case R.id.layout_birthday://选择生日
                mPresenter.selectBirthday();
                break;
            case R.id.layout_change_password://修改密码
                startActivity(new Intent(MyProfileActivity.this, ChangePasswordActivity.class));
                break;
            case R.id.img_back://返回

                break;
        }
    }

    /**
     * 是否可编辑
     */
    private void setEditable(boolean isEditable){
        etSignature.setEnabled(isEditable);
        etNickname.setEnabled(isEditable);
        etName.setEnabled(isEditable);
        tvSex.setEnabled(isEditable);
        tvBirthday.setEnabled(isEditable);
        etProfession.setEnabled(isEditable);
        etAddress.setEnabled(isEditable);
        etEmail.setEnabled(isEditable);
        layoutSelectHead.setEnabled(isEditable);
        layoutSex.setEnabled(isEditable);
        layoutBirthday.setEnabled(isEditable);
        if (LoginStyle.PHONE.equals(LoginStyle.getLoginStyle())){
            etPhoneNumber.setEnabled(false);
            viewChangePassword.setVisibility(View.VISIBLE);
            layoutChangePassword.setVisibility(View.VISIBLE);
        }else {
            etPhoneNumber.setEnabled(isEditable);
            viewChangePassword.setVisibility(View.GONE);
            layoutChangePassword.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            Intent intent = new Intent();
            intent.putExtra(MYPROFILE, MYPROFILE);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public ImageView getHeadPortraitImg() {
        return imgHeadPortrait;
    }

    @Override
    public EditText getSignatureET() {
        return etSignature;
    }

    @Override
    public EditText getNicknameET() {
        return etNickname;
    }

    @Override
    public EditText getNameET() {
        return etName;
    }

    @Override
    public TextView getSexTV() {
        return tvSex;
    }

    @Override
    public EditText getZhiyeET() {
        return etProfession;
    }

    @Override
    public EditText getAddressET() {
        return etAddress;
    }

    @Override
    public EditText getEmailET() {
        return etEmail;
    }

    @Override
    public EditText getPhoneNumET() {
        return etPhoneNumber;
    }

    @Override
    public TextView getBirthdayTV() {
        return tvBirthday;
    }

    @Override
    public TextView getNicknameTV() {
        return tvNickname;
    }

    @Override
    public TextView getGoldTV() {
        return tvGold;
    }

    @Override
    public TextView getIntegralTV() {
        return tvIntegral;
    }
}
