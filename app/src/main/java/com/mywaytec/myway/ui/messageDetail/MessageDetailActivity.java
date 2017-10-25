package com.mywaytec.myway.ui.messageDetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.fragment.comment.CommentFragment;
import com.mywaytec.myway.fragment.praise.PraiseFragment;
import com.mywaytec.myway.model.bean.DynamicDetailBean;
import com.mywaytec.myway.model.bean.DynamicListBean;
import com.mywaytec.myway.utils.TimeUtil;
import com.mywaytec.myway.view.FamilyBookView;
import com.mywaytec.myway.view.ImageInfo;
import com.mywaytec.myway.view.preview.ImagePreviewActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MessageDetailActivity extends BaseActivity<MessageDetailPresenter> implements MessageDetailView, View.OnClickListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.tv_praise)
    TextView tvPraise;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.img_head_portrait)
    ImageView imgHeadPortrait;
    @BindView(R.id.img_like)
    ImageView imgLike;
    @BindView(R.id.img_share)
    ImageView imgShare;
    @BindView(R.id.tv_publish_time)
    TextView tvPublishTime;
    @BindView(R.id.familybookview)
    FamilyBookView familybookview;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.et_comment)
    EditText etComment;
    @BindView(R.id.tv_publish_comment)
    TextView tvPublishComment;
    @BindView(R.id.view_line1)
    View viewLine1;
    @BindView(R.id.view_line2)
    View viewLine2;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_like1)
    TextView tvLike1;
    @BindView(R.id.tv_like2)
    TextView tvLike2;

    private FragmentManager fragmentManager;
    int shid;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_denamic_detail;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.detail);
        scrollView.smoothScrollTo(0, 0);

        fragmentManager = getSupportFragmentManager();
        shid = getIntent().getIntExtra("shid", 0);

        mPresenter.getData(shid);
    }

    @Override
    protected void updateViews() {

    }

    CommentFragment commentFragment;
    PraiseFragment praiseFragment;

    @OnClick({R.id.tv_comment, R.id.tv_praise, R.id.img_like, R.id.tv_publish_comment,
              R.id.img_share})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_comment://评论列表
                viewLine1.setVisibility(View.VISIBLE);
                viewLine2.setVisibility(View.INVISIBLE);
                tvLike1.setTextColor(Color.parseColor("#bcc0c8"));
                tvLike2.setTextColor(Color.parseColor("#bcc0c8"));
                tvPraise.setTextColor(Color.parseColor("#bcc0c8"));
                tvComment.setTextColor(Color.parseColor("#2984DF"));
                int id1 = 0;
                if(null != mPresenter.getDynamic()) {
                    id1 = mPresenter.getDynamic().getId();
                }
                commentFragment = CommentFragment.newInstance(id1);
                fragmentManager.beginTransaction().replace(R.id.layout_fragment, commentFragment).commit();
                break;
            case R.id.tv_praise://点赞列表
                viewLine1.setVisibility(View.INVISIBLE);
                viewLine2.setVisibility(View.VISIBLE);
                tvLike1.setTextColor(Color.parseColor("#2984DF"));
                tvLike2.setTextColor(Color.parseColor("#2984DF"));
                tvPraise.setTextColor(Color.parseColor("#2984DF"));
                tvComment.setTextColor(Color.parseColor("#bcc0c8"));
                int id2 = 0;
                if(null != mPresenter.getDynamic()) {
                    id2 = mPresenter.getDynamic().getId();
                }
                praiseFragment = PraiseFragment.newInstance(id2);
                fragmentManager.beginTransaction().replace(R.id.layout_fragment, praiseFragment).commit();
                break;
            case R.id.img_like://点赞
                if(null != mPresenter.getDynamic()) {
                    mPresenter.like(mPresenter.getDynamic());
                }
                break;
            case R.id.tv_publish_comment://发表评论
                if(null != mPresenter.getDynamic()) {
                    mPresenter.publishComment(mPresenter.getDynamic(), this, mPresenter.getDynamic().getId() + "", etComment);
                }
                break;
            case R.id.img_share://分享
                mPresenter.openPopupWindow(this, imgShare);
                break;
        }
    }

    @Override
    public CommentFragment getCommentFragment() {
        return commentFragment;
    }

    @Override
    public TextView getCommentTV() {
        return tvComment;
    }

    @Override
    public ImageView getLikeImg() {
        return imgLike;
    }

    @Override
    public TextView getLikeTV() {
        return tvPraise;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public ImageView getHeadPortraitImg() {
        return imgHeadPortrait;
    }

    @Override
    public TextView getNicknameTV() {
        return tvNickname;
    }

    @Override
    public TextView getContentTV() {
        return tvContent;
    }

    @Override
    public TextView getPublishTimeTV() {
        return tvPublishTime;
    }

    @Override
    public TextView getPraiseTV() {
        return tvPraise;
    }

    @Override
    public FamilyBookView getFamilybookview() {
        return familybookview;
    }
}
