package com.mywaytec.myway.ui.mydynamic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.model.bean.DynamicListBean;
import com.mywaytec.myway.ui.blacklist.BlacklistActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MyDynamicActivity extends BaseActivity<MyDynamicPresenter> implements MyDynamicView {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.dynamic_recyclerview)
    LRecyclerView dynamicRecyclerview;
    @BindView(R.id.way_recyclerview)
    LRecyclerView wayRecyclerview;
    @BindView(R.id.tv_dongtai)
    TextView tvDongtai;
    @BindView(R.id.tv_luxian)
    TextView tvLuxian;
    @BindView(R.id.view_dongtai)
    View viewDongtai;
    @BindView(R.id.view_luxian)
    View viewLuxian;
    @BindView(R.id.img_right)
    ImageView imgRight;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_my_dynamic;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.i_release);
        mPresenter.initDynamicList(this);
        mPresenter.initWayList(this);
    }

    @Override
    protected void updateViews() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public LRecyclerView getDynamicList() {
        return dynamicRecyclerview;
    }

    @Override
    public LRecyclerView getWayList() {
        return wayRecyclerview;
    }

    @OnClick({R.id.layout_dongtai, R.id.layout_luxian, R.id.img_right})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.layout_dongtai://动态
                dynamicRecyclerview.setVisibility(View.VISIBLE);
                wayRecyclerview.setVisibility(View.GONE);
                tvDongtai.setTextColor(Color.parseColor("#000000"));
                tvLuxian.setTextColor(Color.parseColor("#666666"));
                viewDongtai.setVisibility(View.VISIBLE);
                viewLuxian.setVisibility(View.INVISIBLE);
                break;
            case R.id.layout_luxian://路线
                dynamicRecyclerview.setVisibility(View.GONE);
                wayRecyclerview.setVisibility(View.VISIBLE);
                tvDongtai.setTextColor(Color.parseColor("#666666"));
                tvLuxian.setTextColor(Color.parseColor("#000000"));
                viewDongtai.setVisibility(View.INVISIBLE);
                viewLuxian.setVisibility(View.VISIBLE);
                break;
            case R.id.img_right://黑名单
                startActivity(new Intent(MyDynamicActivity.this, BlacklistActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x131 && resultCode == RESULT_OK){
            DynamicListBean.ObjBean dynamic = (DynamicListBean.ObjBean) data.getSerializableExtra("dynamic");
            int position = data.getIntExtra("position", -1);
            if (null != dynamic) {
                mPresenter.refreshItem(dynamic, position);
            }
        }
    }
}
