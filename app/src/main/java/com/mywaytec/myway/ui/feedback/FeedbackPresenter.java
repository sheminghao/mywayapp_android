package com.mywaytec.myway.ui.feedback;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.mywaytec.myway.R;
import com.mywaytec.myway.adapter.FullyGridLayoutManager;
import com.mywaytec.myway.adapter.GridOnlyImageAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.DialogUtils;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CommonSubscriber;
import com.yalantis.ucrop.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/3/8.
 */

public class FeedbackPresenter extends RxPresenter<FeedbackView> {

    RetrofitHelper retrofitHelper;
    private Context mContext;
    private RecyclerView recyclerView;

    private GridOnlyImageAdapter adapter;
    private List<LocalMedia> selectMedia = new ArrayList<>();

    @Inject
    public FeedbackPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(FeedbackView view) {
        super.attachView(view);
        mContext = mView.getContext();
        recyclerView = mView.getRecyclerView();
        selectPicture();
    }

    public void feedback(final Context context, String content){
        if (TextUtils.isEmpty(content)){
            ToastUtils.showToast(R.string.please_enter_feedback);
            return;
        }
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.feedback(uid, token, content, true)
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseInfo>() {
                    @Override
                    public void onNext(BaseInfo baseInfo) {
                        Log.i("TAG", "------意见反馈"+baseInfo.getCode());
                        if (baseInfo.getCode() == 1){
                            ToastUtils.showToast(R.string.submit_successfully);
                            ((Activity)context).finish();
                        }else if (baseInfo.getCode() == 233){
                            DialogUtils.reLoginDialog(mView.getContext());
                        }
                    }
                });
    }

    public void selectPicture(){
        FullyGridLayoutManager manager = new FullyGridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridOnlyImageAdapter(mContext, onAddPicClickListener);
//        adapter.setSelectMax(9);
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
                    if (position == -2) {//选择视频
                        config.setType(2);
                        config.setMaxSelectNum(1);
                        adapter.setSelectMax(1);
                    }else {
                        config.setType(1);
                        config.setMaxSelectNum(9);
                        adapter.setSelectMax(9);
                    }
//                    config.setCopyMode(copyMode);
                    config.setCompress(true);
                    config.setEnablePixelCompress(true);
                    config.setEnableQualityCompress(true);
//                    config.setSelectMode(selectMode);
                    config.setShowCamera(true);
                    config.setEnablePreview(true);
                    config.setEnableCrop(false);
                    config.setPreviewVideo(true);
                    config.setRecordVideoDefinition(FunctionConfig.ORDINARY);// 视频清晰度
//                    config.setRecordVideoSecond(60);// 视频秒数
//                    config.setCheckNumMode(isCheckNumMode);
                    config.setCompressQuality(100);
                    config.setImageSpanCount(4);
                    config.setSelectMedia(selectMedia);
                    config.setCompressFlag(2);
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
            for (int i = 0; i < resultList.size(); i++) {
                if (resultList.get(i).getType() == 1){
                    Log.i("TAG", "------resultList.get(i).getType()," + i + "==" + resultList.get(i).getType());
                    if (resultList.get(i).getPath().endsWith(".gif")) {
                        File file = new File(resultList.get(i).getPath());
                        if (file.length() > 5 * 1024 * 1024) {
                            ToastUtils.showToast(R.string.gif图片不能超过5M);
                            return;
                        }
                    } else {
                        File file = new File(resultList.get(i).getCompressPath());
                        if (file.length() > 256 * 1024) {
                            ToastUtils.showToast(R.string.图片不能超过256k);
                            return;
                        }
                    }
                }else {
                    File file = new File(resultList.get(i).getPath());
                    if (file.length() > 20 * 1024 * 1024) {
                        ToastUtils.showToast(R.string.视频不能超过20M);
                        return;
                    }
                }
            }
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
}
