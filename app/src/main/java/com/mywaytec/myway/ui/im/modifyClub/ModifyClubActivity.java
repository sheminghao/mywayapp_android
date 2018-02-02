package com.mywaytec.myway.ui.im.modifyClub;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.utils.DialogUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ModifyClubActivity extends BaseActivity<ModifyClubPresenter> implements ModifyClubView {

    public static final int MODIFY_CLUB = 0x171;

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

    private String country = "";
    private String province = "";
    private String city = "";

    private String gid;
    private String clubHead;
    private String clubName;
    private String clubArea;
    private String clubJianjie;

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
        tvTitle.setText(R.string.modify_club);
        tvRight.setText(R.string.finish);

        gid = getIntent().getStringExtra("gid");
        clubHead = getIntent().getStringExtra("clubHead");
        clubName = getIntent().getStringExtra("clubName");
        clubArea = getIntent().getStringExtra("clubArea");
        clubJianjie = getIntent().getStringExtra("clubJianjie");
        Glide.with(this).load(clubHead).centerCrop().into(imgClubHead);
        etClubName.setText(clubName);
        tvClubAddress.setText(clubArea);
        etJianjie.setText(clubJianjie);
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
                mPresenter.modifyClub(etClubName.getText().toString().trim(),
                        etJianjie.getText().toString().trim(),
                        country, province, city, Integer.parseInt(gid.substring(3)));
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
