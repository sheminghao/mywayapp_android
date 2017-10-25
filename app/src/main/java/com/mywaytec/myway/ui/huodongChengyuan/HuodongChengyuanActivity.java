package com.mywaytec.myway.ui.huodongChengyuan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;

import butterknife.BindView;

public class HuodongChengyuanActivity extends BaseActivity<HuodongChengyuanPresenter> implements HuodongChengyuanView{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.img_head_portrait)
    ImageView imgHeadPortrait;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.img_gender)
    ImageView imgGander;
    @BindView(R.id.layout_duizhang)
    LinearLayout layoutDuizhang;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_huodong_chengyuan;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.activity_member);

        String aid = getIntent().getStringExtra("aid");
        Log.i("TAG", "------aid,"+aid);
        mPresenter.initRecyclerView(aid);
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
    public ImageView getHeadPortraitImg() {
        return imgHeadPortrait;
    }

    @Override
    public TextView getNameTV() {
        return tvName;
    }

    @Override
    public ImageView getGanderImg() {
        return imgGander;
    }

    @Override
    public LinearLayout getDuizhangLayout() {
        return layoutDuizhang;
    }
}
