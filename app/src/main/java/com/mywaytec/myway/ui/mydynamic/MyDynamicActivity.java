package com.mywaytec.myway.ui.mydynamic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.model.bean.DynamicListBean;
import com.mywaytec.myway.ui.blacklist.BlacklistActivity;
import com.mywaytec.myway.ui.track.TrackRecordActivity;
import com.mywaytec.myway.utils.PreferencesUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.mywaytec.myway.fragment.dynamic.DynamicFragment.PUBLISH;
import static com.mywaytec.myway.ui.myprofile.MyProfileActivity.MYPROFILE_CODE;

public class MyDynamicActivity extends BaseActivity<MyDynamicPresenter> implements MyDynamicView {

    @BindView(R.id.dynamic_recyclerview)
    LRecyclerView dynamicRecyclerview;
    @BindView(R.id.way_recyclerview)
    LRecyclerView wayRecyclerview;
    @BindView(R.id.tv_dongtai)
    TextView tvDongtai;
    @BindView(R.id.tv_luxian)
    TextView tvLuxian;
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

    @OnClick({R.id.tv_dongtai, R.id.tv_luxian, R.id.img_right})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_dongtai://动态
                dynamicRecyclerview.setVisibility(View.VISIBLE);
                wayRecyclerview.setVisibility(View.GONE);
                tvDongtai.setTextColor(Color.parseColor("#ffffff"));
                tvLuxian.setTextColor(Color.parseColor("#B7B7B7"));
                break;
            case R.id.tv_luxian://路线
                dynamicRecyclerview.setVisibility(View.GONE);
                wayRecyclerview.setVisibility(View.VISIBLE);
                tvDongtai.setTextColor(Color.parseColor("#B7B7B7"));
                tvLuxian.setTextColor(Color.parseColor("#ffffff"));
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
