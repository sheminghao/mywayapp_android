package com.mywaytec.myway.ui.im.modifyClub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.ui.im.createClub.CreateClubView;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CommonSubscriber;
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
 * Created by shemh on 2017/8/10.
 */

public class ModifyClubPresenter extends RxPresenter<ModifyClubView> {

    private RetrofitHelper retrofitHelper;
    private List<LocalMedia> selectMedia = new ArrayList<>();
    private Context mContext;

    @Inject
    public ModifyClubPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(ModifyClubView view) {
        super.attachView(view);
        mContext = mView.getContext();
    }

    public RetrofitHelper getRetrofitHelper(){
        return retrofitHelper;
    }

    /**
     * 创建俱乐部
     * @param name 俱乐部名称
     * @param description 俱乐部简介
     * @param country 国家
     * @param province 省
     * @param city 城市
     */
    public void modifyClub(String name, String description, String country, String province, String city, int gid){
//        if (TextUtils.isEmpty(name)){
//            ToastUtils.showToast(R.string.note_down_club_name);
//            return;
//        }
//        if (TextUtils.isEmpty(description)){
//            ToastUtils.showToast(R.string.note_down_club_profile);
//            return;
//        }
//        if (TextUtils.isEmpty(country) && TextUtils.isEmpty(province) && TextUtils.isEmpty(city)){
//            ToastUtils.showToast(R.string.choose_area);
//            return;
//        }
        if (selectMedia.size() == 0){
            ToastUtils.showToast(R.string.choose_club_photo);
            return;
        }

        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        Map<String, RequestBody> bodyMap = new HashMap<>();
        bodyMap.put("uid", toRequestBody(uid));
        bodyMap.put("token", toRequestBody(token));
        bodyMap.put("name", toRequestBody(name));
        bodyMap.put("description", toRequestBody(description));
        bodyMap.put("country", toRequestBody(country));
        bodyMap.put("province", toRequestBody(province));
        bodyMap.put("city", toRequestBody(city));

        String clubHead = "";
        if (selectMedia.size() > 0){
            clubHead = selectMedia.get(0).getCompressPath();
            File file = new File(clubHead);
            bodyMap.put("mwfile"+"\"; filename=\""+file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
        }
        retrofitHelper.updateRongClub(bodyMap, gid)
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseInfo>(mView, mContext, true) {
                    @Override
                    public void onNext(BaseInfo baseInfo) {
                        if (baseInfo.getCode() == 1){
                            Intent intent = new Intent();
                            ((Activity)mContext).setResult(Activity.RESULT_OK, intent);
                            ((Activity)mContext).finish();
                        }else {
                            Log.i("TAG", "------updateRongClub, " + baseInfo.getMsg());
                            ToastUtils.showToast(baseInfo.getMsg());
                        }
                    }
                });
    }

    public RequestBody toRequestBody(String value){
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }

    public void selectHeadPortrait(){
        // 进入相册
        int selector = R.drawable.select_cb;
        FunctionOptions config = new FunctionOptions.Builder().create();
        config.setCheckedBoxDrawable(selector);
        config.setType(1);//1图片 or 2视频
        config.setCropMode(FunctionConfig.CROP_MODEL_1_1);
        config.setCompress(true);
        config.setEnablePixelCompress(true);
        config.setEnableQualityCompress(true);
        config.setMaxSelectNum(9);
        config.setSelectMode(FunctionConfig.MODE_SINGLE);
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
            selectMedia = resultList;
            Log.i("callBack_result", selectMedia.size() + "");
        }

        @Override
        public void onSelectSuccess(LocalMedia localMedia) {
            Log.i("TAG", "-----单张"+localMedia.getPath() + "");
            selectMedia.add(localMedia);
            Glide.with(mContext).load(localMedia.getPath())
                    .error(R.mipmap.icon_default)
                    .into(mView.getClubHeadImg());
        }
    };
}
