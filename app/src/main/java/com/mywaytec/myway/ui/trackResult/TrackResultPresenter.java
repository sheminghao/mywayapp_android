package com.mywaytec.myway.ui.trackResult;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.mywaytec.myway.R;
import com.mywaytec.myway.adapter.PhotoAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.bean.PublicRoutePathsBean;
import com.mywaytec.myway.model.bean.UploadRouteBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.DialogUtils;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CommonSubscriber;
import com.mywaytec.myway.view.CustomLinearLayoutManager;
import com.mywaytec.myway.view.LoadingDialog;
import com.yalantis.ucrop.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by shemh on 2017/3/8.
 */

public class TrackResultPresenter extends RxPresenter<TrackResultView> {

    RetrofitHelper retrofitHelper;
    private Context mContext;
    private List<LocalMedia> coverImg = new ArrayList<>();
    private List<LocalMedia> routeImg = new ArrayList<>();
    PhotoAdapter photoAdapter;
    private LoadingDialog loadingDialog;
    List<LatLng> latLngList;

    @Inject
    public TrackResultPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(TrackResultView view) {
        super.attachView(view);
        mContext = mView.getContext();
    }

    //分享路线轨迹
    public void shareRoutePath(String name, String title, int scenery_star, int difficulty_star,
                               int legend, int endurance_claim, String intro, String origin, String origin_bus,
                               String destination, String destination_bus, String score, List<LatLng> latLngList){
        this.latLngList = latLngList;
        if (TextUtils.isEmpty(title)){
            ToastUtils.showToast(R.string.please_enter_title);
            return;
        }
        if (coverImg.size() == 0){
            ToastUtils.showToast(R.string.请选择封面图片);
            return;
        }
        if (routeImg.size() == 0){
            ToastUtils.showToast(R.string.请选择风景图片);
            return;
        }
        loadingDialog = new LoadingDialog(mContext);
        loadingDialog.show();
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        Map<String, RequestBody> bodyMap = new HashMap<>();
        bodyMap.put("uid", toRequestBody(uid));
        bodyMap.put("token", toRequestBody(token));
        bodyMap.put("name", toRequestBody(name));
        bodyMap.put("title", toRequestBody(title));
        bodyMap.put("intro", toRequestBody(intro));
        bodyMap.put("origin", toRequestBody(origin));
        bodyMap.put("originBus", toRequestBody(origin_bus));
        bodyMap.put("destination", toRequestBody(destination));
        bodyMap.put("destinationBus", toRequestBody(destination_bus));
        bodyMap.put("score", toRequestBody(score));
        if (latLngList.size() < 1000){
            for (int i = 0; i < latLngList.size(); i++) {
                bodyMap.put("paths["+i+"].latitude", toRequestBody(latLngList.get(i).latitude+""));
                bodyMap.put("paths["+i+"].longitude", toRequestBody(latLngList.get(i).longitude+""));
            }
        }else {
            for (int i = 0; i < 1000; i++) {
                bodyMap.put("paths["+i+"].latitude", toRequestBody(latLngList.get(i).latitude+""));
                bodyMap.put("paths["+i+"].longitude", toRequestBody(latLngList.get(i).longitude+""));
            }
        }
        //上传数据
        retrofitHelper.shareRoutePath(bodyMap, scenery_star, difficulty_star, legend, endurance_claim)
                .compose(RxUtil.<UploadRouteBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<UploadRouteBean>() {
                    @Override
                    public void onNext(UploadRouteBean uploadRouteBean) {
                        Log.i("TAG", "------上传路线"+uploadRouteBean.getCode());
                        if (uploadRouteBean.getCode() == 1){
                            if (coverImg.size() > 0) {
                                uploadRoutePath(uploadRouteBean.getObj().getId());
                            }
                        }else if(uploadRouteBean.getCode() == 19){
                            DialogUtils.reLoginDialog(mContext);
                        }else{
                            if (null != loadingDialog){
                                loadingDialog.dismiss();
                            }
                        }
                    }
                });
    }

    //上传路线轨迹
    public void publicRoutePath(String name, String title, int scenery_star, int difficulty_star,
                               int legend, int endurance_claim, String intro, String origin, String origin_bus,
                               String destination, String destination_bus, String score, int saveRouteId){
        this.latLngList = latLngList;
        if (coverImg.size() == 0){
            ToastUtils.showToast(R.string.请选择封面图片);
            return;
        }
        if (routeImg.size() == 0){
            ToastUtils.showToast(R.string.请选择风景图片);
            return;
        }
        loadingDialog = new LoadingDialog(mContext);
        loadingDialog.show();
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        Map<String, RequestBody> bodyMap = new HashMap<>();
        bodyMap.put("uid", toRequestBody(uid));
        bodyMap.put("token", toRequestBody(token));
        bodyMap.put("name", toRequestBody(name));
        bodyMap.put("title", toRequestBody(title));
        bodyMap.put("intro", toRequestBody(intro));
        bodyMap.put("origin", toRequestBody(origin));
        bodyMap.put("originBus", toRequestBody(origin_bus));
        bodyMap.put("destination", toRequestBody(destination));
        bodyMap.put("destinationBus", toRequestBody(destination_bus));
        bodyMap.put("city", toRequestBody(""));
        //上传数据
        retrofitHelper.publicRoutePaths(bodyMap, scenery_star, difficulty_star, legend, endurance_claim, 0, saveRouteId)
                .compose(RxUtil.<PublicRoutePathsBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<PublicRoutePathsBean>() {
                    @Override
                    public void onNext(PublicRoutePathsBean publicRoutePathsBean) {
                        Log.i("TAG", "------上传路线PublicRoutePathsBean,"+publicRoutePathsBean.getCode());
                        if (publicRoutePathsBean.getCode() == 1){
                            if (coverImg.size() > 0) {
                                uploadCoverImg(publicRoutePathsBean.getObj().getId());
                            }
                        }else if(publicRoutePathsBean.getCode() == 19){
                            DialogUtils.reLoginDialog(mContext);
                        }else{
                            if (null != loadingDialog){
                                loadingDialog.dismiss();
                            }
                        }
                    }
                });
    }

    int count = 1;

    //上传路线的轨迹(线)
    public void uploadRoutePath(final int routeId){
        Log.i("TAG", "---------上传轨迹个数"+latLngList.size());
        Log.i("TAG", "---------上传轨迹个数"+latLngList.size()/1000);
        if (latLngList.size()/1000 < count || (latLngList.size() - count * 1000) == 0){
            Log.i("TAG", "---------上传轨迹完成"+count);
            uploadCoverImg(routeId);
            return;
        }
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        Map<String, RequestBody> bodyMap = new HashMap<>();
        bodyMap.put("uid", toRequestBody(uid));
        bodyMap.put("token", toRequestBody(token));
        if ((latLngList.size() - count * 1000) < 1000) {
            for (int i = 0; i < latLngList.size()%1000 ; i++) {
                bodyMap.put("paths[" + i + "].latitude", toRequestBody(latLngList.get(count * 1000 + i).latitude + ""));
                bodyMap.put("paths[" + i + "].longitude", toRequestBody(latLngList.get(count * 1000 + i).longitude + ""));
            }
        }else {
            for (int i = 0; i < 1000; i++) {
                bodyMap.put("paths[" + i + "].latitude", toRequestBody(latLngList.get(count * 1000 + i).latitude + ""));
                bodyMap.put("paths[" + i + "].longitude", toRequestBody(latLngList.get(count * 1000 + i).longitude + ""));
            }
        }
        retrofitHelper.uploadRoutePath(bodyMap, routeId)
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseInfo>() {
                    @Override
                    public void onNext(BaseInfo baseInfo) {
                        if (baseInfo.getCode() == 1){
                            Log.i("TAG", "---------上传轨迹次数"+count);
                            count++;
                            uploadRoutePath(routeId);
                        }
                    }
                });
    }

    //上传封面图片
    public void uploadCoverImg(final int routeId){
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        Map<String, RequestBody> bodyMap = new HashMap<>();
        bodyMap.put("uid", toRequestBody(uid));
        bodyMap.put("token", toRequestBody(token));
        File file = new File(coverImg.get(0).getCompressPath());
        bodyMap.put("mwfile"+"\"; filename=\""+file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
        retrofitHelper.uploadCoverImg(bodyMap, routeId)
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseInfo>() {
                    @Override
                    public void onNext(BaseInfo baseInfo) {
                        Log.i("TAG", "------上传封面"+baseInfo.getCode());
                         if (baseInfo.getCode() == 1){
                             if (routeImg.size() > 0) {
                                 uploadRouteImg(routeId);
                             }
                         }else{
                             ToastUtils.showToast(baseInfo.getMsg());
                             if (null != loadingDialog){
                                 loadingDialog.dismiss();
                             }
                         }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Log.i("TAG", "------上传封面错误"+e.getMessage());
                        if (null != loadingDialog){
                            loadingDialog.dismiss();
                        }
                    }
                });
    }

    //上传路线的图片
    public void uploadRouteImg(int routeId){
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        Map<String, RequestBody> bodyMap = new HashMap<>();
        bodyMap.put("uid", toRequestBody(uid));
        bodyMap.put("token", toRequestBody(token));
        for (int i = 0; i < routeImg.size(); i++) {
            File file = new File(routeImg.get(i).getCompressPath());
            bodyMap.put("mwfile"+"\"; filename=\""+file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
        }
        retrofitHelper.uploadRouteImg(bodyMap, routeId)
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseInfo>() {
                    @Override
                    public void onNext(BaseInfo baseInfo) {
                        Log.i("TAG", "------上传风景"+baseInfo.getCode());
                        if (null != loadingDialog) {
                            loadingDialog.dismiss();
                        }
                        if (baseInfo.getCode() == 1){
                            ToastUtils.showToast(R.string.shared_successfully);
                            ((TrackResultActivity)mContext).finish();
                        }else{
                            ToastUtils.showToast(baseInfo.getMsg());
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Log.i("TAG", "------上传风景错误"+e.getMessage());
                        if (null != loadingDialog){
                            loadingDialog.dismiss();
                        }
                    }
                });
    }

    //保存轨迹到服务端
    public void saveRoutePaths(int legend, int duration, List<LatLng> latLngList){
        this.latLngList = latLngList;
        loadingDialog = new LoadingDialog(mContext);
        loadingDialog.show();
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        Map<String, RequestBody> bodyMap = new HashMap<>();
        bodyMap.put("uid", toRequestBody(uid));
        bodyMap.put("token", toRequestBody(token));
        if (latLngList.size() < 1000){
            for (int i = 0; i < latLngList.size(); i++) {
                bodyMap.put("paths["+i+"].latitude", toRequestBody(latLngList.get(i).latitude+""));
                bodyMap.put("paths["+i+"].longitude", toRequestBody(latLngList.get(i).longitude+""));
            }
        }else {
            for (int i = 0; i < 1000; i++) {
                bodyMap.put("paths["+i+"].latitude", toRequestBody(latLngList.get(i).latitude+""));
                bodyMap.put("paths["+i+"].longitude", toRequestBody(latLngList.get(i).longitude+""));
            }
        }
        //上传数据
        retrofitHelper.saveRoutePaths(bodyMap, legend, duration)
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseInfo>() {
                    @Override
                    public void onNext(BaseInfo baseInfo) {
                        Log.i("TAG", "------上传路线"+baseInfo.getCode());
                        if (baseInfo.getCode() == 1){
                            ((TrackResultActivity)mContext).finish();
                        }else if(baseInfo.getCode() == 19){
                            DialogUtils.reLoginDialog(mContext);
                        }else{
                            if (null != loadingDialog){
                                loadingDialog.dismiss();
                            }
                        }
                    }
                });
    }

    public RequestBody toRequestBody(String value){
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }

    private int mode;

    //编辑封面
    public void editCover(){
        mode = FunctionConfig.MODE_SINGLE;
        selectImgSetting(FunctionConfig.MODE_SINGLE, coverImg);
    }

    //选择风景照
    public void selectPhoto(){
        mode = FunctionConfig.MODE_MULTIPLE;
        selectImgSetting(FunctionConfig.MODE_MULTIPLE, routeImg);
    }

    //选择图片
    public void selectImgSetting(int mode, List<LocalMedia> selectMedia){
        // 进入相册
        /**
         * type -->
         * copyMode     --> 裁剪比例，默认、1:1、3:4、3:2、16:9
         * maxSelectNum --> 可选择图片的数量
         * selectMode   --> 单选 or 多选
         * isShow       --> 是否显示拍照选项 这里自动根据type 启动拍照或录视频
         * isPreview    --> 是否打开预览选项
         * isCrop       --> 是否打开剪切选项
         * isPreviewVideo -->是否预览视频(播放) mode or 多选有效
         * ThemeStyle -->主题颜色
         * CheckedBoxDrawable -->图片勾选样式
         * cropW-->裁剪宽度 值不能小于100  如果值大于图片原始宽高 将返回原图大小
         * cropH-->裁剪高度 值不能小于100
         * isCompress -->是否压缩图片
         * setEnablePixelCompress 是否启用像素压缩
         * setEnableQualityCompress 是否启用质量压缩
         * setRecordVideoSecond 录视频的秒数，默认不限制
         * setRecordVideoDefinition 视频清晰度  Constants.HIGH 清晰  Constants.ORDINARY 低质量
         * setImageSpanCount -->每行显示个数
         * setCheckNumMode 是否显示QQ选择风格(带数字效果)
         * setPreviewColor 预览文字颜色
         * setCompleteColor 完成文字颜色
         * setPreviewBottomBgColor 预览界面底部背景色
         * setBottomBgColor 选择图片页面底部背景色
         * setCompressQuality 设置裁剪质量，默认无损裁剪
         * setSelectMedia 已选择的图片
         * setCompressFlag 1为系统自带压缩  2为第三方luban压缩
         * 注意-->type为2时 设置isPreview or isCrop 无效
         * 注意：Options可以为空，默认标准模式
         */

        int selector = R.drawable.select_cb;
        FunctionOptions config = new FunctionOptions.Builder().create();
        config.setCheckedBoxDrawable(selector);
        config.setType(1);//1图片 or 2视频
        config.setCropMode(FunctionConfig.CROP_MODEL_1_1);
        config.setCompress(true);
        config.setEnablePixelCompress(true);
        config.setEnableQualityCompress(true);
        config.setMaxSelectNum(9);
        config.setSelectMode(mode);
        config.setShowCamera(true);
        config.setEnablePreview(true);
        config.setEnableCrop(false);
        config.setPreviewVideo(true);
        config.setRecordVideoDefinition(FunctionConfig.HIGH);// 视频清晰度
        config.setRecordVideoSecond(60);// 视频秒数
//      config.setCheckNumMode(isCheckNumMode);
        config.setCompressQuality(100);
        config.setImageSpanCount(4);
        config.setSelectMedia(selectMedia);
        config.setCompressFlag(1);
        // 先初始化参数配置，在启动相册
        PictureConfig.getInstance().init(config);
        PictureConfig.getInstance().openPhoto((Activity)mContext, resultCallback);
    }

    /**
     * 图片回调方法
     */
    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> resultList) {
            Log.i("callBack_result", resultList.size() + "");
            if (null != resultList) {
                if (resultList.size() == 1 && mode == FunctionConfig.MODE_SINGLE) {
                    coverImg = resultList;
                    Glide.with(mContext).load(resultList.get(0).getCompressPath())
                            .error(R.mipmap.icon_default)
                            .placeholder(R.mipmap.icon_default)
                            .into(mView.getCoverImg());
                }else{
                    routeImg = resultList;
                    photoAdapter = new PhotoAdapter(mContext);
                    mView.getPhotoRecycler().setLayoutManager(new CustomLinearLayoutManager(mContext));
                    mView.getPhotoRecycler().setAdapter(photoAdapter);
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < routeImg.size(); i++) {
                        list.add(routeImg.get(i).getCompressPath());
                    }
                    photoAdapter.setDataList(list);
                }
            }
        }

        @Override
        public void onSelectSuccess(LocalMedia localMedia) {
            coverImg.clear();
            coverImg.add(localMedia);
            Glide.with(mContext).load(localMedia.getCompressPath())
                    .error(R.mipmap.icon_default)
                    .placeholder(R.mipmap.icon_default)
                    .into(mView.getCoverImg());
        }
    };

    public void shareRoute(){

    }
}
