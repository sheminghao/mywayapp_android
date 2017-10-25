package com.mywaytec.myway.ui.publish;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.ui.location.LocationActivity;
import com.mywaytec.myway.utils.AppUtils;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.MaxByteLengthEditText;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 发表动态
 */
public class PublishActivity extends BaseActivity<PublishPresenter> implements PublishView, View.OnClickListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.layout_location)
    LinearLayout layoutLocation;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.et_moment_add_content)
    MaxByteLengthEditText etMomentAddContent;
    @BindView(R.id.tv_text_num)
    TextView tvTextNum;
    @BindView(R.id.layout_publish)
    CoordinatorLayout layoutPublish;

    PoiInfo address;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_publish;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        layoutPublish.setPadding(0, AppUtils.getStatusBar(), 0, 0);
        tvTitle.setText(R.string.update_dynamic);
        etMomentAddContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int current = etMomentAddContent.getText().toString().length();
                tvTextNum.setText(current + "/255");
                if (current > 255) {
                    String content = etMomentAddContent.getText().toString();
                    content = content.substring(0, 255);
                    etMomentAddContent.setText(content);
                    ToastUtils.showToast(getResources().getString(R.string.the_number_of_words_has_reached_the_upper_limit));
                }
            }
        });
    }

    @Override
    protected void updateViews() {
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @OnClick({R.id.layout_location, R.id.tv_publish})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_location://我的位置
                Intent intent = new Intent(PublishActivity.this, LocationActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.tv_publish://发布动态
                mPresenter.piblish(etMomentAddContent.getText().toString().trim(), address);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                address = data.getParcelableExtra("address");
                tvLocation.setText(address.address+address.name);
            }
        }
    }
}
