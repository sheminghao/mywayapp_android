package com.mywaytec.myway.ui.im.clubVerify;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;

import butterknife.BindView;

/**
 * 俱乐部申请验证消息
 */
public class ClubVerifyActivity extends BaseActivity<ClubVerifyPresenter> implements ClubVerifyView{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView_verify)
    RecyclerView verifyRecyclerView;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_club_verify;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.verification_message);
        mPresenter.initRecyclerView();
    }

    @Override
    protected void updateViews() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return verifyRecyclerView;
    }
}
