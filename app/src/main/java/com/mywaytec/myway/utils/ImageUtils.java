package com.mywaytec.myway.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mywaytec.myway.R;

import java.util.HashMap;

/**
 * Created by shemh on 2017/5/27.
 */

public class ImageUtils {

    /**
     * 动态设置imageView的大小(可以设置到父级如:ViewGroup)  根据下载的图片大小 计算高度(宽度是全屏宽)
     *
     * @param context
     * @param url
     * @param imageView
     * @param srceenWidth
     * @param topMargin
     * @param layoutParamsType 0 LinearLayout.LayoutParams   1 FrameLayout$LayoutParams
     */
    public static void getPicByGlideAndScale(@NonNull final Context context, @NonNull final String url,
                                             @NonNull final ImageView imageView,
                                             final float srceenWidth, final int topMargin,
                                             final View parentView, final int layoutParamsType) {
        Glide.with(context).load(url).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                int height = resource.getIntrinsicHeight();
                int width = resource.getIntrinsicWidth();
                float height_temp_1 = srceenWidth / width;
                if (layoutParamsType == 0) {
                    final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) srceenWidth, (int) (height_temp_1 * height));
                    layoutParams.topMargin = topMargin;
                    imageView.setLayoutParams(layoutParams);
                } else if (layoutParamsType == 1) {
                    final FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int) srceenWidth, (int) (height_temp_1 * height));
                    layoutParams.topMargin = topMargin;
                    imageView.setLayoutParams(layoutParams);
                }
                Glide.with(context)
                        .load(url)
//                                .override(layoutParams.width, layoutParams.height)
                        .error(R.mipmap.icon_default)
                        .into(imageView);
                imageView.invalidate();
                if (parentView != null) {
                    ViewGroup.LayoutParams groupParams = parentView.getLayoutParams();
                    groupParams.height = (int) (height_temp_1 * height);
                    groupParams.width = (int) srceenWidth;
                    parentView.invalidate();
                }
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

}
