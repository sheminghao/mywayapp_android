package com.mywaytec.myway.ui.im.JoinClub;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.model.bean.SearchClubBean;

import butterknife.BindView;
import butterknife.OnClick;

public class JoinClubActivity extends BaseActivity<JoinClubPresenter> implements JoinClubView {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_apply_to_join)
    TextView tvApplytoJoin;
    @BindView(R.id.img_club_head)
    ImageView imgClubHead;
    @BindView(R.id.tv_club_name)
    TextView tvClubName;
    @BindView(R.id.tv_club_official)
    TextView tvClubOfficial;
    @BindView(R.id.tv_member)
    TextView tvMember;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_jianjie)
    TextView tvJianjie;
    @BindView(R.id.tv_people_num)
    TextView tvPeopleNum;

    private SearchClubBean.ObjBean clubDetail;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_join_club;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        clubDetail = (SearchClubBean.ObjBean) getIntent().getSerializableExtra("clubDetail");

        tvTitle.setText(clubDetail.getGroupname());
        Glide.with(this).load(clubDetail.getImgUrl())
                .centerCrop()
                .into(imgClubHead);
        tvClubName.setText(clubDetail.getGroupname());
        if (clubDetail.isOfficial()){
            tvClubOfficial.setVisibility(View.VISIBLE);
        }else {
            tvClubOfficial.setVisibility(View.GONE);
        }
        tvMember.setText(clubDetail.getUserNum() + getResources().getString(R.string.member));
        tvArea.setText(clubDetail.getCountry() + clubDetail.getProvince() + clubDetail.getCity());
        tvJianjie.setText(clubDetail.getDescription());
        tvPeopleNum.setText(clubDetail.getUserNum() + getResources().getString(R.string.people));

        mPresenter.loadMember(clubDetail.getGroupid());
    }

    @Override
    protected void updateViews() {

    }

    @OnClick({R.id.tv_apply_to_join})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_apply_to_join://申请加入
                mPresenter.joinClub(clubDetail.getGroupid());
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
}
