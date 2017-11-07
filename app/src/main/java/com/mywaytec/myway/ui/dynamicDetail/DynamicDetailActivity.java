package com.mywaytec.myway.ui.dynamicDetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.fragment.comment.CommentFragment;
import com.mywaytec.myway.fragment.praise.PraiseFragment;
import com.mywaytec.myway.model.bean.DynamicListBean;
import com.mywaytec.myway.utils.Base64_2;
import com.mywaytec.myway.utils.ImageUtils;
import com.mywaytec.myway.utils.TimeUtil;
import com.mywaytec.myway.view.FamilyBookView;
import com.mywaytec.myway.view.ImageInfo;
import com.mywaytec.myway.view.preview.ImagePreviewActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayerStandard;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class DynamicDetailActivity extends BaseActivity<DynamicDetailPresenter> implements DynanicDetailView, View.OnClickListener {

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
    @BindView(R.id.layout_comment)
    LinearLayout layoutComment;
    @BindView(R.id.layout_praise)
    LinearLayout layoutPraise;
    @BindView(R.id.playerview)
    JZVideoPlayerStandard mPlayerView;

    private FragmentManager fragmentManager;
    DynamicListBean.ObjBean dynamic;
    int position;

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
        dynamic = (DynamicListBean.ObjBean) getIntent().getSerializableExtra("dynamic");
        position = getIntent().getIntExtra("position", -1);
        if (dynamic.getUser().isGender()) {
            Glide.with(this).load(dynamic.getUser().getImgeUrl())
                    .error(R.mipmap.touxiang_boy_nor)
                    .centerCrop()
                    .into(imgHeadPortrait);
        } else {
            Glide.with(this).load(dynamic.getUser().getImgeUrl())
                    .error(R.mipmap.touxiang_girl_nor)
                    .centerCrop()
                    .into(imgHeadPortrait);
        }

        if (dynamic.isVideo()) {
            mPlayerView.setVisibility(View.VISIBLE);
            familybookview.setVisibility(View.GONE);
//            if (mPlayerView != null) {
//                mPlayerView.release();
//            }
            mPlayerView.setUp(dynamic.getImages().get(0), JZVideoPlayerStandard.SCREEN_LAYOUT_LIST, "");
            Observable.create(new Observable.OnSubscribe<Bitmap>() {
                @Override
                public void call(Subscriber<? super Bitmap> subscriber) {
                    //异步操作相关代码
                    subscriber.onNext(ImageUtils.getNetVideoBitmap(dynamic.getImages().get(0)));
                    subscriber.onCompleted();
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Bitmap>(){
                        @Override
                        public void call(Bitmap data) {
                            // 主线程操作
                            if (null != data) {
                                mPlayerView.thumbImageView.setImageBitmap(data);
                            }
                        }
                    });
        }else {
            mPlayerView.setVisibility(View.GONE);
            familybookview.setVisibility(View.VISIBLE);

            ArrayList imgUrls = getIntent().getStringArrayListExtra("ImgUrl");
            if (imgUrls == null || imgUrls.size() == 0){
                familybookview.setVisibility(View.GONE);
            }
            familybookview.setPics(imgUrls);
        }

        tvLike1.setText(getResources().getString(R.string.praise)+"(");
        tvNickname.setText(dynamic.getUser().getNickname());
        try {
            //显示表情
            tvContent.setText(new String(Base64_2.decode(dynamic.getContent())));
        }catch (Exception e){
            tvContent.setText(dynamic.getContent());
        }
        tvPublishTime.setText(TimeUtil.getTimeDelay(dynamic.getCreateTime()));
        tvComment.setText(getResources().getString(R.string.comment)+"("+dynamic.getCommentNum()+")");
        tvPraise.setText(dynamic.getLikeNum()+"");
        if (dynamic.isIsLike()){
            imgLike.setImageResource(R.mipmap.dianzan_press);
        }else{
            imgLike.setImageResource(R.mipmap.dianzan);
        }
        //默认打开评论模块
        layoutComment.performClick();

        familybookview.setOnClickItemImage(new FamilyBookView.ClickItemImage() {
            @Override
            public void onClickItemImage(List<String> urls, int index) {
                List<ImageInfo> imgInfos = new ArrayList<>();
                for (int i = 0; i < urls.size(); i++) {
                    ImageInfo imageInfo = new ImageInfo();
                    imageInfo.setBigImageUrl(urls.get(i));
                    imgInfos.add(imageInfo);
                }
                Intent intent = new Intent(DynamicDetailActivity.this, ImagePreviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) imgInfos);
                bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, index);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("dynamic", dynamic);
                intent.putExtra("position", position);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void updateViews() {

    }

    CommentFragment commentFragment;
    PraiseFragment praiseFragment;

    @OnClick({R.id.layout_comment, R.id.layout_praise, R.id.img_like, R.id.tv_publish_comment,
              R.id.img_share})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_comment://评论列表
                viewLine1.setVisibility(View.VISIBLE);
                viewLine2.setVisibility(View.INVISIBLE);
                tvLike1.setTextColor(Color.parseColor("#bcc0c8"));
                tvLike2.setTextColor(Color.parseColor("#bcc0c8"));
                tvPraise.setTextColor(Color.parseColor("#bcc0c8"));
                tvComment.setTextColor(Color.parseColor("#2984DF"));
                commentFragment = CommentFragment.newInstance(dynamic.getId());
                fragmentManager.beginTransaction().replace(R.id.layout_fragment, commentFragment).commit();
                break;
            case R.id.layout_praise://点赞列表
                viewLine1.setVisibility(View.INVISIBLE);
                viewLine2.setVisibility(View.VISIBLE);
                tvLike1.setTextColor(Color.parseColor("#2984DF"));
                tvLike2.setTextColor(Color.parseColor("#2984DF"));
                tvPraise.setTextColor(Color.parseColor("#2984DF"));
                tvComment.setTextColor(Color.parseColor("#bcc0c8"));
                praiseFragment = PraiseFragment.newInstance(dynamic.getId());
                fragmentManager.beginTransaction().replace(R.id.layout_fragment, praiseFragment).commit();
                break;
            case R.id.img_like://点赞
                if(null != dynamic) {
                    mPresenter.like(dynamic);
                }
                break;
            case R.id.tv_publish_comment://发表评论
                mPresenter.publishComment(dynamic, this, dynamic.getId()+"", etComment);
                break;
            case R.id.img_share://分享
                mPresenter.openPopupWindow(this, imgShare);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            Intent intent = new Intent();
            intent.putExtra("dynamic", dynamic);
            intent.putExtra("position", position);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
    public LinearLayout getPraiseLayout() {
        return layoutPraise;
    }

    @Override
    public Context getContext() {
        return this;
    }


    @Override
    public void onPause() {
        super.onPause();
        JZVideoPlayerStandard.releaseAllVideos();
    }
}
