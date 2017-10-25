package com.mywaytec.myway.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mywaytec.myway.base.Constant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shemh on 2017/4/10.
 */

public class DownloadUtil {

    /**
     * 图片下载
     * @param context
     * @param url
     */
    public static void downloadPhoto(Context context, String url) {
        if (!SystemUtil.isNetworkConnected()) {
            ToastUtils.showToast("请检查您的网络是否连接");
        }
        Glide.with(context)
        .load(url)
        .asBitmap()
        .into(new SimpleTarget<Bitmap>() {
              @Override
              public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                  if(null == bitmap){
                      ToastUtils.showToast("保存失败");
                      return;
                  }
                  try {
                      File file = new File(Constant.DEFAULT_PATH+ "/image/");
                      if (!file.exists()){
                          file.mkdirs();
                      }
                      FileOutputStream fos = new FileOutputStream(
                              Constant.DEFAULT_PATH+ "/image/"
                                      + sdf.format(new Date()) + ".jpg");
                      boolean b = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                      try {
                          fos.flush();
                      } catch (IOException e) {
                          e.printStackTrace();
                      }
                      try {
                          fos.close();
                      } catch (IOException e) {
                          e.printStackTrace();
                      }
                      StringBuffer buffer = new StringBuffer();
                      if (b)
                          buffer.append("保存成功,文件保存到"+file.getPath());
                      else {
                          buffer.append("保存失败 ");
                      }
                      ToastUtils.showToast(buffer.toString());

                  } catch (FileNotFoundException e) {
                      e.printStackTrace();
                  }
              }
        });
    }
}
