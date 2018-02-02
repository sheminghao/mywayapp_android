package com.mywaytec.myway.ui.im.searchClub;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchClubActivity extends BaseActivity<SearchClubPresenter> implements SearchClubView{

    @BindView(R.id.recyclerView_search)
    RecyclerView recyclerView;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_tuijian)
    TextView tvTuijian;
    @BindView(R.id.img_clear)
    ImageView imgClear;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_search_club;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        mPresenter.initRecyclerView();
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())){
                    imgClear.setVisibility(View.GONE);
                }else {
                    imgClear.setVisibility(View.VISIBLE);
                }
                mPresenter.searchClub(s.toString().trim());
            }
        });
    }

    @Override
    protected void updateViews() {

    }

    @OnClick({R.id.tv_cancel, R.id.img_clear})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_cancel://取消
                finish();
                break;
            case R.id.img_clear:
                etSearch.setText("");
                break;
        }
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public TextView getTuijianTV() {
        return tvTuijian;
    }
}
