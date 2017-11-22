package com.mywaytec.myway.ui.message;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MessageActivity extends BaseActivity<MessagePresenter> implements MessageView{

    @BindView(R.id.tv_title)
    TextView tvTilte;
    @BindView(R.id.img_right)
    ImageView imgRight;
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
        tvTilte.setText(R.string.message_center);
        mPresenter.initList();
    }

    @Override
    protected void updateViews() {
    }

    @OnClick({R.id.img_right})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.img_right://一键读取
                mPresenter.yijianduqu();
                break;
        }
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

    @Override
    public ImageView getRightImg() {
        return imgRight;
    }
}
