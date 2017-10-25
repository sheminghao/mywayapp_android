package com.mywaytec.myway.ui.message;

import android.content.Context;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;

import butterknife.BindView;

public class MessageActivity extends BaseActivity<MessagePresenter> implements MessageView{

    @BindView(R.id.tv_title)
    TextView tvTilte;
    @BindView(R.id.lrecyclerview)
    LRecyclerView lrecyclerview;
    @BindView(R.id.tv_none)
    TextView tvNone;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_message;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTilte.setText("消息中心");
        mPresenter.initList();
    }

    @Override
    protected void updateViews() {
    }

    @Override
    public LRecyclerView getRecyclerView() {
        return lrecyclerview;
    }

    @Override
    public TextView getNoneTV() {
        return tvNone;
    }

    @Override
    public Context getContext() {
        return this;
    }
}
