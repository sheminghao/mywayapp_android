package com.mywaytec.myway.ui.gradeAndGold;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.model.bean.LoginInfo;
import com.mywaytec.myway.utils.PreferencesUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class GradeAndGoldActivity extends BaseActivity<GradeAndGoldPresenter> implements GradeAndGoldView {

    @BindView(R.id.recyclerView_access)
    RecyclerView accessRecyclerView;
    @BindView(R.id.recyclerView_level)
    RecyclerView levelRecyclerView;
    @BindView(R.id.layout_access_text)
    LinearLayout layoutAccessText;
    @BindView(R.id.layout_level_text)
    LinearLayout layoutLevelText;
    @BindView(R.id.view_access)
    View viewAccess;
    @BindView(R.id.view_level)
    View viewLevel;
    @BindView(R.id.layout_access)
    LinearLayout layoutAccess;
    @BindView(R.id.layout_level)
    LinearLayout layoutLevel;
    @BindView(R.id.tv_access)
    TextView tvAccess;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.img_head_portrait)
    ImageView imgHeadPortrait;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_driver_level)
    TextView tvDriverLevel;
    @BindView(R.id.tv_gold)
    TextView tvGold;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.tv_jinfen)
    TextView tvJinfen;


    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_grade_and_gold;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        mPresenter.initRecyclerView();
        mPresenter.queryIntegralGold();

        LoginInfo.ObjBean userInfo = PreferencesUtils.getLoginInfo().getObj();
        if (userInfo.getGender()) {
            Glide.with(this).load(userInfo.getImgeUrl())
                    .error(R.mipmap.touxiang_boy_nor)
                    .centerCrop()
                    .into(imgHeadPortrait);
        }else {
            Glide.with(this).load(userInfo.getImgeUrl())
                    .error(R.mipmap.touxiang_girl_nor)
                    .centerCrop()
                    .into(imgHeadPortrait);
        }
        tvNickname.setText(userInfo.getNickname());
        tvGold.setText(userInfo.getGlod()+"");
        mPresenter.loadLevel(userInfo.getIntegral());
    }

    @Override
    protected void updateViews() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public RecyclerView getAccessRecyclerView() {
        return accessRecyclerView;
    }

    @Override
    public RecyclerView getLevelRecyclerView() {
        return levelRecyclerView;
    }

    @Override
    public TextView getDriverLevelTV() {
        return tvDriverLevel;
    }

    @Override
    public TextView getJinfenTV() {
        return tvJinfen;
    }

    @Override
    public TextView getGoldTV() {
        return tvGold;
    }

    @Override
    public ProgressBar getProgressBar() {
        return progressBar;
    }

    @OnClick({R.id.layout_access_text, R.id.layout_level_text, R.id.img_back})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.layout_access_text://增值攻略
                layoutAccess.setVisibility(View.VISIBLE);
                layoutLevel.setVisibility(View.GONE);
                viewAccess.setVisibility(View.VISIBLE);
                viewLevel.setVisibility(View.INVISIBLE);
                tvAccess.setTextColor(Color.parseColor("#333333"));
                tvLevel.setTextColor(Color.parseColor("#999999"));
                break;
            case R.id.layout_level_text://等级详情
                layoutAccess.setVisibility(View.GONE);
                layoutLevel.setVisibility(View.VISIBLE);
                viewAccess.setVisibility(View.INVISIBLE);
                viewLevel.setVisibility(View.VISIBLE);
                tvAccess.setTextColor(Color.parseColor("#999999"));
                tvLevel.setTextColor(Color.parseColor("#333333"));
                break;
            case R.id.img_back:
                finish();
                break;
        }
    }
}
