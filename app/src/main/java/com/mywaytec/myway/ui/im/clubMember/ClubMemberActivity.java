package com.mywaytec.myway.ui.im.clubMember;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ClubMemberActivity extends BaseActivity<ClubMemberPresenter> implements ClubMemberView{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.recyclerView_manager)
    RecyclerView managerRecyclerView;
    @BindView(R.id.recyclerView_member)
    RecyclerView memberRecyclerView;
    @BindView(R.id.tv_manager)
    TextView tvManager;
    @BindView(R.id.tv_member)
    TextView tvMember;

    /**
     * 俱乐部ID
     */
    String gid;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_club_member;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.member_lists);

        gid = getIntent().getStringExtra("gid");

        mPresenter.initData(Integer.parseInt(gid.substring(3)));
    }

    @Override
    protected void updateViews() {

    }

    @OnClick({R.id.tv_cancel, R.id.tv_delete})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_cancel://取消
                imgBack.setVisibility(View.VISIBLE);
                tvCancel.setVisibility(View.GONE);
                tvDelete.setText(R.string.delete);
                mPresenter.setEdit(false);
                break;
            case R.id.tv_delete://删除
                if (imgBack.getVisibility() == View.VISIBLE) {
                    imgBack.setVisibility(View.GONE);
                    tvCancel.setVisibility(View.VISIBLE);
                    tvDelete.setText(getResources().getString(R.string.delete) + "(" + 0 + ")");
                    mPresenter.setEdit(true);
                }else {
                    mPresenter.deleteClubUsers(Integer.parseInt(gid.substring(3)),
                    imgBack, tvCancel, tvDelete);
                }
                break;
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public RecyclerView getMemberRecyclerView() {
        return memberRecyclerView;
    }

    @Override
    public RecyclerView getManagerRecyclerView() {
        return managerRecyclerView;
    }

    @Override
    public TextView getDeleteTV() {
        return tvDelete;
    }
}
