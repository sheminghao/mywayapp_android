package com.mywaytec.myway.ui.im.createClub;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.utils.DialogUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class CreateClubActivity extends BaseActivity<CreateClubPresenter> implements CreateClubView {

    public static final int CREATE_CLUB = 0x170;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.img_club_head)
    ImageView imgClubHead;
    @BindView(R.id.et_club_name)
    EditText etClubName;
    @BindView(R.id.tv_club_address)
    TextView tvClubAddress;
    @BindView(R.id.et_jianjie)
    EditText etJianjie;

    private String country;
    private String province;
    private String city;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_create_club;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.set_in_a_club);
        tvRight.setText(R.string.finish);
    }

    @Override
    protected void updateViews() {

    }

    @OnClick({R.id.img_club_head, R.id.layout_address, R.id.tv_right})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.img_club_head://选择俱乐部头像
                mPresenter.selectHeadPortrait();
                break;
            case R.id.layout_address://选择地点
                DialogUtils.selectCity(this, new DialogUtils.OnCitySelectListener() {
                    @Override
                    public void citySelect(String s1, String s2, String s3) {
                        Log.i("TAG", "------"+s1 + ", "+s2 +", "+s3);
                        country = s1;
                        province = s2;
                        city = s3;
                        if (TextUtils.isEmpty(s1)){
                            country = "";
                        }
                        if (TextUtils.isEmpty(s2)){
                            province = "";
                        }
                        if (TextUtils.isEmpty(s3)){
                            city = "";
                        }
                        tvClubAddress.setText(country+province+city);
                    }
                });
                break;
            case R.id.tv_right://完成
                mPresenter.createClub(etClubName.getText().toString().trim(),
                        etJianjie.getText().toString().trim(),
                        country, province, city);
                break;
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public ImageView getClubHeadImg() {
        return imgClubHead;
    }
}
