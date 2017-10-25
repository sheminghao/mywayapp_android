package com.mywaytec.myway.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.ListBaseAdapter;
import com.mywaytec.myway.base.SuperViewHolder;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.bean.DynamicListBean;
import com.mywaytec.myway.model.bean.LoginInfo;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.ui.dynamicDetail.DynamicDetailActivity;
import com.mywaytec.myway.ui.main.MainActivity;
import com.mywaytec.myway.utils.Base64_2;
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

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by shemh on 2017/2/23.
 * 动态页面适配器
 */

public class MyDynamicAdapter extends ListBaseAdapter<DynamicListBean.ObjBean> implements View.OnClickListener {

    private Context context;
    private RetrofitHelper retrofitHelper;

    public MyDynamicAdapter(Context context, RetrofitHelper retrofitHelper) {
        super(context);
        this.context = context;
        this.retrofitHelper = retrofitHelper;
    }

    /**
     * viewType=0;表示视频 ， viewType=1;表示图文，
     */
    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SuperViewHolder holder = null;
        switch (viewType) {
            case 0:
                holder = new SuperViewHolder(LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.item_dynamic_video, parent, false));
                break;
            case 1:
                holder = new SuperViewHolder(LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.item_dynamic, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_dynamic;
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataList.get(position).isVideo()) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    ArrayList<String> pics;
    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        pics = new ArrayList<>();
        if (mDataList.get(position).getImages() != null){
            for (int i = 0; i < mDataList.get(position).getImages().size(); i++) {
                pics.add(mDataList.get(position).getImages().get(i));
            }
        }
        switch (holder.getItemViewType()) {
            case 0:
                showShiPin(holder, position);
                break;
            case 1:
                showTuWen(holder, position);
                break;
        }

        LoginInfo.ObjBean userInfo = PreferencesUtils.getLoginInfo().getObj();
        ImageView imgHeadPortrait = holder.getView(R.id.img_head_portrait);
        if (userInfo.isGender()){
            Glide.with(mContext).load(userInfo.getImgeUrl())
                    .error(R.mipmap.touxiang_boy_nor)
                    .into(imgHeadPortrait);
        }else {
            Glide.with(mContext).load(userInfo.getImgeUrl())
                    .error(R.mipmap.touxiang_girl_nor)
                    .into(imgHeadPortrait);
        }

        TextView tvNickname = holder.getView(R.id.tv_nickname);
        tvNickname.setText(userInfo.getNickname());
        TextView tvPublishTime = holder.getView(R.id.tv_publish_time);
        String publishInfo = "";
        if (null != mDataList.get(position).getLocation()
                && null != mDataList.get(position).getLocation().getCity()){
            publishInfo = TimeUtil.getTimeDelay(mDataList.get(position).getCreateTime())+"·"+
                    mDataList.get(position).getLocation().getCity();
        }else{
            publishInfo = TimeUtil.getTimeDelay(mDataList.get(position).getCreateTime());
        }
        tvPublishTime.setText(publishInfo);
        TextView tvContent = holder.getView(R.id.tv_content);
        try {
            //显示表情
            tvContent.setText(new String(Base64_2.decode(mDataList.get(position).getContent())));
        }catch (Exception e){
            tvContent.setText(mDataList.get(position).getContent());
        }
        final ImageView imgPraise = holder.getView(R.id.img_praise);
        final TextView tvPraise = holder.getView(R.id.tv_praise);
        tvPraise.setText(mDataList.get(position).getLikeNum()+"");
        TextView tvComment = holder.getView(R.id.tv_comment);
        tvComment.setText(mDataList.get(position).getCommentNum()+"");
        if (mDataList.get(position).isIsLike()){//是否点赞
            imgPraise.setImageResource(R.mipmap.dianzan_press);
        }else{
            imgPraise.setImageResource(R.mipmap.dianzan);
        }

        LinearLayout layoutPraise = holder.getView(R.id.layout_praise);
        layoutPraise.setOnClickListener(new View.OnClickListener() {//点赞
            @Override
            public void onClick(View v) {
                if (null != onClickLike)
                onClickLike.clickLick(tvPraise, imgPraise, position);
            }
        });

        ImageView imgXiala = holder.getView(R.id.img_xiala);
        imgXiala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xialacaidan(mDataList.get(position), position);
            }
        });

        LinearLayout layoutItem = holder.getView(R.id.layout_item);
        layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//进入详情页
                pics = new ArrayList<>();
                if (mDataList.get(position).getImages() != null){
                    for (int i = 0; i < mDataList.get(position).getImages().size(); i++) {
                        pics.add(mDataList.get(position).getImages().get(i));
                    }
                }
                Intent intent = new Intent(mContext, DynamicDetailActivity.class);
                intent.putStringArrayListExtra("ImgUrl", pics);
                intent.putExtra("dynamic", mDataList.get(position));
                intent.putExtra("position", position);
                ((Activity)mContext).startActivityForResult(intent, 0x130);
            }
        });
        LinearLayout layoutComment = holder.getView(R.id.layout_comment);
        layoutComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点击评论
                pics = new ArrayList<>();
                if (mDataList.get(position).getImages() != null){
                    for (int i = 0; i < mDataList.get(position).getImages().size(); i++) {
                        pics.add(mDataList.get(position).getImages().get(i));
                    }
                }
                Intent intent = new Intent(mContext, DynamicDetailActivity.class);
                intent.putStringArrayListExtra("ImgUrl", pics);
                intent.putExtra("dynamic", mDataList.get(position));
                mContext.startActivity(intent);
            }
        });

        final LinearLayout layoutShare = holder.getView(R.id.layout_share);
        layoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareSDK.initSDK(mContext);
                openPopupWindow(layoutShare);
            }
        });

    }

    /**
     * 展示图文信息
     * @param holder
     */
    private void showTuWen(SuperViewHolder holder, int position) {
        FamilyBookView familyBookView = holder.getView(R.id.familybookview);
        familyBookView.setTag(position);
        if (mDataList.get(position).getImages() == null || mDataList.get(position).getImages().size() == 0) {
            familyBookView.setVisibility(View.GONE);
        }else{
            familyBookView.setVisibility(View.VISIBLE);
        }

        if ((int)familyBookView.getTag() == position) {
            familyBookView.setPics(pics);
        }
        familyBookView.setOnClickItemImage(new FamilyBookView.ClickItemImage() {
            @Override
            public void onClickItemImage(List<String> urls, int index) {
                List<ImageInfo> imgInfos = new ArrayList<>();
                for (int i = 0; i < urls.size(); i++) {
                    ImageInfo imageInfo = new ImageInfo();
                    imageInfo.setBigImageUrl(urls.get(i));
                    imgInfos.add(imageInfo);
                }
                Intent intent = new Intent(mContext, ImagePreviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable)imgInfos);
                bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, index);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    /**
     * 展示视频类的信息
     * @param holder
     */
    private void showShiPin(SuperViewHolder holder, final int position) {
        final JCVideoPlayerStandard mPlayerView = holder.getView(R.id.playerview);
        if (mPlayerView != null) {
            mPlayerView.release();
        }
        mPlayerView.setUp(mDataList.get(position).getImages().get(0), JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
//        Glide.with(mContext).load(getVideoThumb(mDataList.get(position).getImages().get(0))).into(mPlayerView.thumbImageView);
//        mPlayerView.thumbImageView.setImageBitmap(getNetVideoBitmap(mDataList.get(position).getImages().get(0)));
        Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                //异步操作相关代码
                subscriber.onNext(getNetVideoBitmap(mDataList.get(position).getImages().get(0)));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>(){
                    @Override
                    public void call(Bitmap data) {
                        // 主线程操作
                        mPlayerView.thumbImageView.setImageBitmap(data);
                    }
                });
    }

    /**
     *  服务器返回url，通过url去获取视频的第一帧
     *  Android 原生给我们提供了一个MediaMetadataRetriever类
     *  提供了获取url视频第一帧的方法,返回Bitmap对象
     *
     *  @param videoUrl
     *  @return
     */
    public static Bitmap getNetVideoBitmap(String videoUrl) {
        Bitmap bitmap = null;

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据url获取缩略图
            retriever.setDataSource(videoUrl, new HashMap());
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
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
            ToastUtils.showToast(R.string.shared_successfully);
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


    private OnClickLike onClickLike;
    public void setOnClickLike(OnClickLike onClickLike){
        this.onClickLike = onClickLike;
    }

    public interface OnClickLike{
        void clickLick(TextView tvLike, ImageView imgLike, int position);
    }

    /**
     * 动态更多功能弹出菜单
     */
    public void xialacaidan(final DynamicListBean.ObjBean objBean, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // 创建对话框
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View view = View.inflate(context, R.layout.dialog_xiala_caidan_my, null);
        TextView tvJubao = (TextView) view.findViewById(R.id.tv_jubao);
        TextView tvDetele = (TextView) view.findViewById(R.id.tv_detele);
        tvJubao.setVisibility(View.GONE);
        tvJubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
                String token = PreferencesUtils.getLoginInfo().getObj().getToken();
                retrofitHelper.report(uid, token, objBean.getId()+"", "")
                        .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                        .subscribe(new CommonSubscriber<BaseInfo>() {
                            @Override
                            public void onNext(BaseInfo baseInfo) {
                                ToastUtils.showToast(baseInfo.getMsg());
                                alertDialog.dismiss();
                            }
                        });
            }
        });
        tvDetele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
                String token = PreferencesUtils.getLoginInfo().getObj().getToken();
                retrofitHelper.deleteDynamic(uid, token, objBean.getId()+"")
                        .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                        .subscribe(new CommonSubscriber<BaseInfo>() {
                            @Override
                            public void onNext(BaseInfo baseInfo) {
                                if (baseInfo.getCode() == 329){
                                    mDataList.remove(position);
                                    notifyDataSetChanged();
                                }
                                ToastUtils.showToast(baseInfo.getMsg());
                                alertDialog.dismiss();
                            }
                        });
            }
        });
        alertDialog.setView(view);
        alertDialog.show();
    }

    PopupWindow popupWindow;
    private void openPopupWindow(View v) {
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
                setBackgroundAlpha(1.0f);
            }
        });
        //设置背景色
        setBackgroundAlpha(0.5f);
    }

    //设置屏幕背景透明效果
    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = ((Activity)mContext).getWindow().getAttributes();
        lp.alpha = alpha;
        ((Activity)mContext).getWindow().setAttributes(lp);
    }
}
