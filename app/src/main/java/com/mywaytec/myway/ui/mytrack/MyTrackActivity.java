package com.mywaytec.myway.ui.mytrack;

import android.content.Context;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;

import butterknife.BindView;

public class MyTrackActivity extends BaseActivity<MyTrackPresenter> implements MytrackView {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lrecyclerview)
    LRecyclerView lRecyclerView;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_my_track;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.my_tracks);
        mPresenter.init(this);
    }

    @Override
    protected void updateViews() {

    }

    @Override
    public LRecyclerView getRecyclerView() {
        return lRecyclerView;
    }

    @Override
    public Context getContext() {
        return this;
    }
}
