package com.mywaytec.myway.ui.messageDetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.bean.DynamicDetailBean;
import com.mywaytec.myway.model.bean.DynamicListBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.InputMethodUtils;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.TimeUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CommonSubscriber;
import com.mywaytec.myway.view.FamilyBookView;
import com.mywaytec.myway.view.ImageInfo;
import com.mywaytec.myway.view.preview.ImagePreviewActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by shemh on 2017/3/8.
 */

public class MessageDetailPresenter extends RxPresenter<MessageDetailView> implements View.OnClickListener {

    RetrofitHelper retrofitHelper;
    DynamicListBean.ObjBean dynamic;

    @Inject
    public MessageDetailPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    public void getData(int shid){
        retrofitHelper.getDynamicDetail(shid+"")
                .compose(RxUtil.<DynamicDetailBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DynamicDetailBean>() {
                    @Override
                    public void onNext(DynamicDetailBean dynamicDetailBean) {
                        if (dynamicDetailBean.getCode() == 323){
                            dynamic = dynamicDetailBean.getObj();
                            Glide.with(mView.getContext()).load(dynamic.getUser().getImgeUrl())
                                    .error(R.mipmap.icon_default)
                                    .centerCrop()
                                    .into(mView.getHeadPortraitImg());
                            mView.getNicknameTV().setText(dynamic.getUser().getNickname());
                            mView.getContentTV().setText(dynamic.getContent());
                            mView.getPublishTimeTV().setText(TimeUtil.getTimeDelay(dynamic.getCreateTime()));
                            mView.getCommentTV().setText(mView.getContext().getResources().getString(R.string.comment)+
                                    "("+dynamic.getCommentNum()+")");
                            mView.getPraiseTV().setText(dynamic.getLikeNum()+"");
                            if (dynamic.isIsLike()){
                                mView.getLikeImg().setImageResource(R.mipmap.dianzan_press);
                            }else{
                                mView.getLikeImg().setImageResource(R.mipmap.dianzan);
                            }
                            //默认打开评论模块
                            mView.getCommentTV().performClick();

                            ArrayList imgUrls = (ArrayList) dynamic.getImages();
                            if (imgUrls == null || imgUrls.size() == 0){
                                mView.getFamilybookview().setVisibility(View.GONE);
                            }
                            mView.getFamilybookview().setPics(imgUrls);
                            mView.getFamilybookview().setOnClickItemImage(new FamilyBookView.ClickItemImage() {
                                @Override
                                public void onClickItemImage(List<String> urls, int index) {
                                    List<ImageInfo> imgInfos = new ArrayList<>();
                                    for (int i = 0; i < urls.size(); i++) {
                                        ImageInfo imageInfo = new ImageInfo();
                                        imageInfo.setBigImageUrl(urls.get(i));
                                        imgInfos.add(imageInfo);
                                    }
                                    Intent intent = new Intent(mView.getContext(), ImagePreviewActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) imgInfos);
                                    bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, index);
                                    intent.putExtras(bundle);
                                    mView.getContext().startActivity(intent);
                                }
                            });
                        }
                    }
                });
    }

    public DynamicListBean.ObjBean getDynamic(){
        return dynamic;
    }

    public void like(final DynamicListBean.ObjBean dynamic){
        retrofitHelper.like(PreferencesUtils.getLoginInfo().getObj().getUid(), dynamic.getId()+"")
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseInfo>() {
                    @Override
                    public void onNext(BaseInfo baseInfo) {
                        if (baseInfo.getCode() == 308){
                            int likeNum = Integer.parseInt(mView.getLikeTV().getText().toString().trim());
                            mView.getLikeTV().setText(likeNum+1+"");
                            mView.getLikeImg().setImageResource(R.mipmap.dianzan_press);
                            dynamic.setLikeNum(likeNum+1);
                            dynamic.setIsLike(true);
                        }else if (baseInfo.getCode() == 310){
                            int likeNum = Integer.parseInt(mView.getLikeTV().getText().toString().trim());
                            mView.getLikeTV().setText(likeNum-1+"");
                            mView.getLikeImg().setImageResource(R.mipmap.dianzan);
                            dynamic.setLikeNum(likeNum-1);
                            dynamic.setIsLike(false);
                        }
                    }
                });
    }

    //发表评论
    public void publishComment(final DynamicListBean.ObjBean dynamic, final Context context,
                               final String shid, final EditText etComment){
        String content = etComment.getText().toString().trim();
        if (TextUtils.isEmpty(content)){
            ToastUtils.showToast(R.string.说点什么吧);
            return;
        }
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        retrofitHelper.publishComment(uid, shid, content)
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseInfo>() {
                    @Override
                    public void onNext(BaseInfo baseInfo) {
                        if (baseInfo.getCode() == 318){
//                            ToastUtils.showToast(R.string.published_successfully);
                            mView.getCommentFragment().initViews();
                            etComment.setText("");
                            getDynamicDetail(dynamic, shid);
                            InputMethodUtils.closeSoftKeyboard((Activity) context);
                        }else if (baseInfo.getCode() == 317){
                            ToastUtils.showToast(R.string.published_failure);
                        }
                    }
                });
    }

    public void getDynamicDetail(final DynamicListBean.ObjBean dynamic, String shid){
        retrofitHelper.getDynamicDetail(shid)
                .compose(RxUtil.<DynamicDetailBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DynamicDetailBean>() {
                    @Override
                    public void onNext(DynamicDetailBean dynamicDetailBean) {
                        if (dynamicDetailBean.getCode() == 323){
                            mView.getCommentTV().setText(mView.getContext().getResources().getString(R.string.comment)+
                                    "("+dynamicDetailBean.getObj().getCommentNum()+")");
                            dynamic.setCommentNum(dynamicDetailBean.getObj().getCommentNum());
                        }
                    }
                });
    }

    PopupWindow popupWindow;
    public void openPopupWindow(final Context mContext, View v) {
        //防止重复按按钮
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        //设置PopupWindow的View
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_share, null);
        LinearLayout layoutShareWechat = (LinearLayout) view.findViewById(R.id.layout_share_wechat);
        LinearLayout layoutShareWechatFriends = (LinearLayout) view.findViewById(R.id.layout_share_wechat_friends);
        LinearLayout layoutShareQQ = (LinearLayout) view.findViewById(R.id.layout_share_qq);
        LinearLayout layoutShareQQzone = (LinearLayout) view.findViewById(R.id.layout_share_qqzone);
        layoutShareWechat.setOnClickListener(this);
        layoutShareWechatFriends.setOnClickListener(this);
        layoutShareQQ.setOnClickListener(this);
        layoutShareQQzone.setOnClickListener(this);

        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置背景,这个没什么效果，不添加会报错
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击弹窗外隐藏自身
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置动画
        popupWindow.setAnimationStyle(R.style.PopupWindow);
        //设置位置
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        //设置消失监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(mContext, 1.0f);
            }
        });
        //设置背景色
        setBackgroundAlpha(mContext, 0.5f);
    }

    //设置屏幕背景透明效果
    public void setBackgroundAlpha(Context mContext, float alpha) {
        WindowManager.LayoutParams lp = ((Activity)mContext).getWindow().getAttributes();
        lp.alpha = alpha;
        ((Activity)mContext).getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_share_wechat://分享到微信
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                Platform.ShareParams wechatParams = new Wechat.ShareParams();
                wechatParams.setTitle("曼威");
                wechatParams.setImageUrl("http://www.mywaytec.com");
                wechatParams.setUrl("http://www.mywaytec.com");
                wechatParams.setText("深圳曼威科技有限公司");
                wechatParams.setShareType(Platform.SHARE_WEBPAGE);
                wechat.setPlatformActionListener(platformActionListener);
                wechat.share(wechatParams);
                break;
            case R.id.layout_share_wechat_friends://分享到微信朋友圈
                WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
                sp.setTitle("曼威");
                sp.setImageUrl("http://www.mywaytec.com");
                sp.setUrl("http://www.mywaytec.com");
                sp.setText("深圳曼威科技有限公司");
                sp.setShareType(Platform.SHARE_WEBPAGE);

                Platform wechatMoments = ShareSDK.getPlatform (WechatMoments.NAME);
                // 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wechatMoments.setPlatformActionListener (platformActionListener);
                // 执行图文分享
                wechatMoments.share(sp);
                break;
            case R.id.layout_share_qq://分享到QQ
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                Platform.ShareParams qqParams = new Wechat.ShareParams();
                qqParams.setTitle("曼威");
                qqParams.setUrl("http://www.mywaytec.com");
                qqParams.setText("深圳曼威科技有限公司");
                qq.setPlatformActionListener (platformActionListener);
                qq.share(qqParams);
                break;
            case R.id.layout_share_qqzone://分享到QQ空间
                QZone.ShareParams qZoneParams = new QZone.ShareParams();
                qZoneParams.setTitle("曼威");
                qZoneParams.setTitleUrl("http://www.mywaytec.com"); // 标题的超链接
                qZoneParams.setText("深圳曼威科技有限公司");
                qZoneParams.setImageUrl("http://www.someserver.com/测试图片网络地址.jpg");
                qZoneParams.setSite("深圳曼威科技有限公司");
                qZoneParams.setSiteUrl("http://www.mywaytec.com");

                Platform qzone = ShareSDK.getPlatform (QZone.NAME);
                // 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qzone.setPlatformActionListener (platformActionListener);
                // 执行图文分享
                qzone.share(qZoneParams);
                break;
        }
    }

    PlatformActionListener platformActionListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            ToastUtils.showToast(R.string.sign_up_successfully);
        }
        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            ToastUtils.showToast(R.string.shared_failed);
        }
        @Override
        public void onCancel(Platform platform, int i) {
            ToastUtils.showToast(R.string.shared_cancel);
        }
    };
}
