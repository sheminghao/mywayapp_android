package com.mywaytec.myway.ui.fabuhuodong;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.ui.selectVenue.SelectVenueActivity;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class FabuHuodongActivity extends BaseActivity<FabuHuodongPresenter> implements FabuHuodongView {

    public static final int FABUHUODONG = 0x140;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_gather_site)
    TextView tvGatherSite;
    @BindView(R.id.et_activity_name)
    EditText etActivityName;
    @BindView(R.id.et_num)
    EditText etNum;
    @BindView(R.id.et_contact)
    EditText etContact;
    @BindView(R.id.et_intro)
    EditText etIntro;
    @BindView(R.id.recycler_select_photo)
    RecyclerView recyclerSelectPhoto;

    PoiInfo address;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_fabu_huodong;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.publish_activity);
        tvStartTime.setText(mPresenter.getTime(new Date()));
        tvEndTime.setText(mPresenter.getTime(new Date()));
    }

    @Override
    protected void updateViews() {

    }

    @OnClick({R.id.layout_start_time, R.id.layout_end_time, R.id.tv_publish, R.id.layout_gather_site})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.layout_start_time://选择开始时间
                mPresenter.selectBirthday(tvStartTime);
                break;
            case R.id.layout_end_time://选择结束时间
                mPresenter.selectBirthday(tvEndTime);
                break;
            case R.id.tv_publish://发布
                mPresenter.publishActivity(address, "", etActivityName.getText().toString(),
                        etIntro.getText().toString(), tvStartTime.getText().toString(),
                        tvEndTime.getText().toString(), etContact.getText().toString(),
                        etNum.getText().toString());
                break;
            case R.id.layout_gather_site://集合地点
                Intent intent = new Intent(FabuHuodongActivity.this, SelectVenueActivity.class);
                startActivityForResult(intent, 0);
                break;
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerSelectPhoto;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                address = data.getParcelableExtra("address");
                tvGatherSite.setText(address.address + address.name);
            }
        }
    }
}
