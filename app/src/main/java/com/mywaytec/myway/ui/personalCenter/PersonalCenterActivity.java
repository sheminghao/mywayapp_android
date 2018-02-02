package com.mywaytec.myway.ui.personalCenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.model.bean.LoginInfo;
import com.mywaytec.myway.ui.myprofile.MyProfileActivity;
import com.mywaytec.myway.utils.PreferencesUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.mywaytec.myway.ui.myprofile.MyProfileActivity.MYPROFILE_CODE;

public class PersonalCenterActivity extends BaseActivity<PersonalCenterPresenter> implements PersonalCenterView{

    public static final int PERSONAL_CENTER = 0x128;

    @BindView(R.id.img_head_portrait)
    ImageView imgHeadPortrait;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.img_level)
    ImageView imgLevel;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_guanzhu)
    TextView tvGuanzhu;
    @BindView(R.id.tv_fensi)
    TextView tvFensi;
    @BindView(R.id.view_guanzhu)
    View viewGuanzhu;
    @BindView(R.id.view_fensi)
    View viewFensi;
    @BindView(R.id.recyclerView_guanzhu)
    RecyclerView guanzhuRecyclerView;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_personal_center;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        loadData();
    }

    private void loadData(){
        LoginInfo.ObjBean objBean = PreferencesUtils.getLoginInfo().getObj();
        if (objBean.isGender()){
            Glide.with(this).load(objBean.getImgeUrl())
                    .error(R.mipmap.touxiang_boy_nor)
                    .centerCrop()
                    .into(imgHeadPortrait);
        }else {
            Glide.with(this).load(objBean.getImgeUrl())
                    .error(R.mipmap.touxiang_girl_nor)
                    .centerCrop()
                    .into(imgHeadPortrait);
        }
        tvNickname.setText(objBean.getNickname());
        mPresenter.loadLevel(imgLevel, tvLevel, objBean.getIntegral());
    }

    @Override
    protected void updateViews() {

    }

    @OnClick({R.id.layout_guanzhu, R.id.layout_fensi, R.id.img_back, R.id.img_modify_info})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.layout_guanzhu://关注
                tvGuanzhu.setTextColor(Color.parseColor("#0774DD"));
                tvFensi.setTextColor(Color.parseColor("#666666"));
                viewGuanzhu.setVisibility(View.VISIBLE);
                viewFensi.setVisibility(View.INVISIBLE);
                mPresenter.attentionList();
                break;
            case R.id.layout_fensi://粉丝
                tvGuanzhu.setTextColor(Color.parseColor("#666666"));
                tvFensi.setTextColor(Color.parseColor("#0774DD"));
                viewGuanzhu.setVisibility(View.INVISIBLE);
                viewFensi.setVisibility(View.VISIBLE);
                mPresenter.fansList();
                break;
            case R.id.img_back:
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.img_modify_info:
                startActivityForResult(new Intent(PersonalCenterActivity.this, MyProfileActivity.class), MYPROFILE_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MYPROFILE_CODE && resultCode == RESULT_OK) {
            loadData();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public RecyclerView getGuanzhuRecyclerView() {
        return guanzhuRecyclerView;
    }
}
