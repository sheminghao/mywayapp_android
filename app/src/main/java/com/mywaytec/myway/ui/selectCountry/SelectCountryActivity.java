package com.mywaytec.myway.ui.selectCountry;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.utils.indexlib.IndexBar.widget.IndexBar;

import butterknife.BindView;

public class SelectCountryActivity extends BaseActivity<SelectCountryPresenter> implements SelectCountryView{

    public static final int SELCET_COUNTRY = 0x161;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.indexBar)
    IndexBar indexBar;
    @BindView(R.id.tvSideBarHint)
    TextView tvSideBarHint;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_select_country;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText("手机区号选择");
        mPresenter.init();
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
        return recyclerView;
    }

    @Override
    public IndexBar getIndexBar() {
        return indexBar;
    }

    @Override
    public TextView getSideBarHintTV() {
        return tvSideBarHint;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
