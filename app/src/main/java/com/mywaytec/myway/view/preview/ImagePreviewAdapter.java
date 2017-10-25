package com.mywaytec.myway.view.preview;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mywaytec.myway.R;
import com.mywaytec.myway.utils.DownloadUtil;
import com.mywaytec.myway.view.ImageInfo;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 图片预览适配器
 */
public class ImagePreviewAdapter extends PagerAdapter implements PhotoViewAttacher.OnPhotoTapListener {

    private List<ImageInfo> imageInfo;
    private Context context;
    private View currentView;

    public ImagePreviewAdapter(Context context, @NonNull List<ImageInfo> imageInfo) {
        super();
        this.imageInfo = imageInfo;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageInfo.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        currentView = (View) object;
    }

    public View getPrimaryItem() {
        return currentView;
    }

    public ImageView getPrimaryImageView() {
        return (ImageView) currentView.findViewById(R.id.pv);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photoview, container, false);
        final ProgressBar pb = (ProgressBar) view.findViewById(R.id.pb);
        final PhotoView imageView = (PhotoView) view.findViewById(R.id.pv);

        final ImageInfo info = this.imageInfo.get(position);
        imageView.setOnPhotoTapListener(this);
//        showExcessPic(info, imageView);

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDialog(info.getBigImageUrl());
                return false;
            }
        });

        pb.setVisibility(View.VISIBLE);
        Glide.with(context).load(info.bigImageUrl)//
                .placeholder(R.mipmap.icon_default)//
                .error(R.mipmap.icon_default)//
                .diskCacheStrategy(DiskCacheStrategy.ALL)//
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        pb.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        pb.setVisibility(View.GONE);
                        return false;
                    }
                }).into(imageView);
        container.addView(view);
        return view;
    }

//    /** 展示过度图片 */
//    private void showExcessPic(ImageInfo imageInfo, PhotoView imageView) {
//        //先获取大图的缓存图片
//        Bitmap cacheImage = NineGridView.getImageLoader().getCacheImage(imageInfo.bigImageUrl);
//        //如果大图的缓存不存在,在获取小图的缓存
//        if (cacheImage == null) cacheImage = NineGridView.getImageLoader().getCacheImage(imageInfo.thumbnailUrl);
//        //如果没有任何缓存,使用默认图片,否者使用缓存
//        if (cacheImage == null) {
//            imageView.setImageResource(R.drawable.ic_default_color);
//        } else {
//            imageView.setImageBitmap(cacheImage);
//        }
//    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /** 单击屏幕关闭 */
    @Override
    public void onPhotoTap(View view, float x, float y) {
        ((ImagePreviewActivity) context).finishActivityAnim();
    }

    public void showDialog(final String url){
         /*
            这里使用了 android.support.v7.app.AlertDialog.Builder
            可以直接在头部写 import android.support.v7.app.AlertDialog
            那么下面就可以写成 AlertDialog.Builder
        */
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setItems(new String[]{"保存"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DownloadUtil.downloadPhoto(context, url);
            }
        });
        builder.show();
    }
}