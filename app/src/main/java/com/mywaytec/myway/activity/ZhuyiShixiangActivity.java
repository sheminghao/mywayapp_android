package com.mywaytec.myway.activity;

import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;

import butterknife.BindView;

public class ZhuyiShixiangActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_zhuyi_shixiang;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.attention_matters);
    }

    @Override
    protected void updateViews() {

    }

}
