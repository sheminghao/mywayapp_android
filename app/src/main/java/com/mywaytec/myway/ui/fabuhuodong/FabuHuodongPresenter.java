package com.mywaytec.myway.ui.fabuhuodong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.bigkoo.pickerview.TimePickerView;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.mywaytec.myway.R;
import com.mywaytec.myway.adapter.FullyGridLayoutManager;
import com.mywaytec.myway.adapter.GridImageAdapter;
import com.mywaytec.myway.adapter.GridOnlyImageAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CommonSubscriber;
import com.yalantis.ucrop.entity.LocalMedia;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by shemh on 2017/3/8.
 */

public class FabuHuodongPresenter extends RxPresenter<FabuHuodongView> {

    private RecyclerView recyclerView;
    RetrofitHelper retrofitHelper;
    private PackageInfo packageInfo;
    Context mContext;
    private GridOnlyImageAdapter adapter;
    private List<LocalMedia> selectMedia = new ArrayList<>();

    @Inject
    public FabuHuodongPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(FabuHuodongView view) {
        super.attachView(view);
        mContext = mView.getContext();
        recyclerView = mView.getRecyclerView();
        selectPicture();
    }


    /**
     *
     * @param address 地址
     * @param level   活动难度
     * @param title   活动名称
     * @param intro   活动简介
     * @param start   开始时间
     * @param end     结束时间
     * @param contact 联系方式
     * @param num     人数
     */
    public void publishActivity(PoiInfo address, String level, String title, String intro,
                                String start, String end, String contact, String num){
        if (selectMedia.size() == 0){
            ToastUtils.showToast("请选择图片");
            return;
        }
        if (TextUtils.isEmpty(num) && "0".equals(num)){
            ToastUtils.showToast("请填写人数");
            return;
        }
        if (null == address){
            ToastUtils.showToast("请选择集合地点");
            return;
        }
        if (TextUtils.isEmpty(title)){
            ToastUtils.showToast("请输入活动名称");
            return;
        }
        if (TextUtils.isEmpty(contact)){
            ToastUtils.showToast("请填写联系方式");
            return;
        }
        double latitude = 0;
        double longitude = 0;
        String country = "";
        String province = "";
        String city = "";
        String district = "";
        String street = "";
        String streetnum = "";
        String citycode = "";
        String adcode = "";
        String aoiname = "";
        Map<String, RequestBody> bodyMap = new HashMap<>();
        if (null != address){
            latitude = address.location.latitude;
            longitude = address.location.longitude;
            country= "";
            province="";
            city=address.city;
            district="";
            street=address.address;
            streetnum="";
            citycode="";
            adcode="";
            aoiname=address.name;
        }
        bodyMap.put("uid", toRequestBody(PreferencesUtils.getLoginInfo().getObj().getUid()));
        bodyMap.put("token", toRequestBody(PreferencesUtils.getLoginInfo().getObj().getToken()));
        bodyMap.put("level", toRequestBody(level));
        bodyMap.put("title", toRequestBody(title));
        bodyMap.put("intro", toRequestBody(intro));
        bodyMap.put("start", toRequestBody(start+":00"));
        bodyMap.put("end", toRequestBody(end+":00"));
        bodyMap.put("contact", toRequestBody(contact));
        bodyMap.put("num", toRequestBody(num));
        bodyMap.put("country", toRequestBody(country));
        bodyMap.put("province", toRequestBody(province));
        bodyMap.put("city", toRequestBody(city));
        bodyMap.put("district", toRequestBody(district));
        bodyMap.put("street", toRequestBody(street));
        bodyMap.put("streetnum", toRequestBody(streetnum));
        bodyMap.put("citycode", toRequestBody(citycode));
        bodyMap.put("adcode", toRequestBody(adcode));
        bodyMap.put("aoiname", toRequestBody(aoiname));
        for (int i = 0; i < selectMedia.size(); i++) {
            File file = new File(selectMedia.get(i).getCompressPath());
            bodyMap.put("mwfile"+"\"; filename=\""+file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
        }

        retrofitHelper.launchActivity(bodyMap, latitude, longitude)
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseInfo>(mView, mContext, true) {
                    @Override
                    public void onNext(BaseInfo baseInfo) {
                        if (baseInfo.getCode() == 1){
                            ToastUtils.showToast("发布成功");
                            Intent intent = new Intent();
                            ((Activity)mContext).setResult(Activity.RESULT_OK, intent);
                            ((Activity)mContext).finish();
                        }
                    }
                });
    }

    public void selectBirthday(final TextView tv){
        //时间选择器
        TimePickerView pvTime = new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                tv.setText(getTime(date));
            }
        }).setType(TimePickerView.Type.YEAR_MONTH_DAY_HOUR_MIN)//默认全部显示
          .isCyclic(true)//是否循环滚动
          .build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }

    public String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    public void selectPicture(){
        FullyGridLayoutManager manager = new FullyGridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridOnlyImageAdapter(mContext, onAddPicClickListener);
        adapter.setSelectMax(4);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridOnlyImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                // 这里可预览图片
                PictureConfig.getInstance().externalPicturePreview((Activity)mContext, position, selectMedia);
            }
        });
    }

    /**
     * 图片回调接口
     */
    private GridOnlyImageAdapter.onAddPicClickListener onAddPicClickListener = new GridOnlyImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick(int type, int position) {
            switch (type) {
                case 0:
                    // 进入相册
                    /**
                     * type           --> 1图片 or 2视频
                     * copyMode       --> 裁剪比例，默认、1:1、3:4、3:2、16:9
                     * maxSelectNum   --> 可选择图片的数量
                     * selectMode     --> 单选 or 多选
                     * isShow         --> 是否显示拍照选项 这里自动根据type 启动拍照或录视频
                     * isPreview      --> 是否打开预览选项
                     * isCrop         --> 是否打开剪切选项
                     * isPreviewVideo --> 是否预览视频(播放) mode or 多选有效
                     * ThemeStyle     --> 主题颜色
                     * CheckedBoxDrawable -->图片勾选样式
                     * cropW -->裁剪宽度 值不能小于100 如果值大于图片原始宽高 将返回原图大小
                     * cropH -->裁剪高度 值不能小于100
                     * isCompress     --> 是否压缩图片
                     * setEnablePixelCompress 是否启用像素压缩
                     * setEnableQualityCompress 是否启用质量压缩
                     * setRecordVideoSecond 录视频的秒数，默认不限制
                     * setRecordVideoDefinition 视频清晰度 Constants.HIGH 清晰 Constants.ORDINARY 低质量
                     * setImageSpanCount -->每行显示个数
                     * setCheckNumMode 是否显示QQ选择风格(带数字效果)
                     * setPreviewColor 预览文字颜色
                     * setCompleteColor 完成文字颜色
                     * setPreviewBottomBgColor 预览界面底部背景色
                     * setBottomBgColor 选择图片页面底部背景色
                     * setCompressQuality 设置裁剪质量，默认无损裁剪
                     * setSelectMedia 已选择的图片
                     * setCompressFlag 1为系统自带压缩 2为第三方luban压缩
                     * 注意-->type为2时 设置isPreview or isCrop 无效
                     * 注意：Options可以为空，默认标准模式
                     */

                    int selector = R.drawable.select_cb;
                    FunctionOptions config = new FunctionOptions.Builder().create();
                    config.setCheckedBoxDrawable(selector);
                    config.setCompress(true);
                    config.setEnablePixelCompress(true);
                    config.setEnableQualityCompress(true);
                    config.setMaxSelectNum(4);
                    config.setShowCamera(true);
                    config.setEnablePreview(true);
                    config.setEnableCrop(false);
                    config.setPreviewVideo(true);
                    config.setRecordVideoDefinition(FunctionConfig.HIGH);// 视频清晰度
                    config.setRecordVideoSecond(60);// 视频秒数
                    config.setCompressQuality(100);
                    config.setImageSpanCount(4);
                    config.setSelectMedia(selectMedia);
                    config.setCompressFlag(1);
                    config.setGif(true);// 是否显示gif图片，默认不显示
                    // 先初始化参数配置，在启动相册
                    PictureConfig.getInstance().init(config);
                    PictureConfig.getInstance().openPhoto((Activity)mContext, resultCallback);
                    break;
                case 1:
                    // 删除图片
                    selectMedia.remove(position);
                    adapter.notifyItemRemoved(position);
                    break;
            }
        }
    };

    /**
     * 图片回调方法
     */
    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> resultList) {
            selectMedia = resultList;
            Log.i("callBack_result", selectMedia.size() + "");
            if (selectMedia != null) {
                adapter.setList(selectMedia);
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onSelectSuccess(LocalMedia localMedia) {

        }
    };

    public RequestBody toRequestBody(String value){
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }
}
