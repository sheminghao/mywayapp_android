package com.mywaytec.myway.ui.myprofile;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.bean.ChangeHeadImgBean;
import com.mywaytec.myway.model.bean.LoginInfo;
import com.mywaytec.myway.model.bean.UserInfo;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.utils.data.LoginStyle;
import com.mywaytec.myway.view.CommonSubscriber;
import com.mywaytec.myway.view.LoadingDialog;
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

public class MyProfilePresenter extends RxPresenter<MyProfileView> {

    RetrofitHelper retrofitHelper;
    private Context mContext;
    private List<LocalMedia> selectMedia = new ArrayList<>();
    LoadingDialog loadingDialog;

    @Inject
    public MyProfilePresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(MyProfileView view) {
        super.attachView(view);
        mContext = mView.getContext();
    }

    //获取用户信息
    public void getUserInfo(){
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        retrofitHelper.getUserInfo(uid)
                .compose(RxUtil.<UserInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<UserInfo>(mView, mContext, true) {
                    @Override
                    public void onNext(UserInfo userInfo) {
                         if (userInfo.getCode() == 240){//查询完成
                             //修改保存的logininfo
                             LoginInfo loginInfo = PreferencesUtils.getLoginInfo();
                             loginInfo.getObj().setGender(userInfo.getObj().getGender());
                             loginInfo.getObj().setNickname(userInfo.getObj().getNickname());
                             loginInfo.getObj().setSignature(userInfo.getObj().getSignature());
                             loginInfo.getObj().setAddress(userInfo.getObj().getAddress());
                             PreferencesUtils.saveLoginInfo(loginInfo);
                             if (userInfo.getObj().getGender()) {
                                 Glide.with(mContext).load(userInfo.getObj().getImgeUrl())
                                         .error(R.mipmap.touxiang_boy_nor)
                                         .centerCrop()
                                         .into(mView.getHeadPortraitImg());
                             }else {
                                 Glide.with(mContext).load(userInfo.getObj().getImgeUrl())
                                         .error(R.mipmap.touxiang_girl_nor)
                                         .centerCrop()
                                         .into(mView.getHeadPortraitImg());
                             }
                             mView.getSignatureET().setText(userInfo.getObj().getSignature());
                             mView.getNicknameET().setText(userInfo.getObj().getNickname());
                             mView.getNameET().setText(userInfo.getObj().getUsername());
                             if (userInfo.getObj().getGender()){
                                 mView.getSexTV().setText(R.string.male);
                             }else {
                                 mView.getSexTV().setText(R.string.female);
                             }
                             mView.getBirthdayTV().setText(userInfo.getObj().getBirthday());
                             mView.getZhiyeET().setText(userInfo.getObj().getProfession());
                             mView.getAddressET().setText(userInfo.getObj().getAddress());
                             mView.getEmailET().setText(userInfo.getObj().getEmail());
                             mView.getPhoneNumET().setText(userInfo.getObj().getPhonenumber());
//                             mView.getReferralTV().setText(userInfo.getObj().getReferralcode());
                             mView.getNicknameTV().setText(userInfo.getObj().getNickname());
                             mView.getGoldTV().setText(userInfo.getObj().getGlod()+"");
                             mView.getIntegralTV().setText(userInfo.getObj().getIntegral()+"");
                         }else {//

                         }
                    }
                });
    }

    //修改用户信息
    public void changeUserInfo(){
        loadingDialog = new LoadingDialog(mContext);
        loadingDialog.show();
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String phoneNum = mView.getPhoneNumET().getText().toString().trim();
        String email = mView.getEmailET().getText().toString().trim();
        String signature = mView.getSignatureET().getText().toString().trim();
        String nickname = mView.getNicknameET().getText().toString().trim();
        boolean gender = true;
        if (mContext.getResources().getString(R.string.female).equals(mView.getSexTV().getText().toString().trim())){
            gender = false;
        }
        String username = mView.getNameET().getText().toString().trim();
        String profession = mView.getZhiyeET().getText().toString().trim();
        String birthday = mView.getBirthdayTV().getText().toString().trim();
        String address = mView.getAddressET().getText().toString().trim();

        if (LoginStyle.PHONE.equals(LoginStyle.getLoginStyle())) {//手机登录
            retrofitHelper.changeUserInfo(uid, email, signature, nickname, gender, username, profession, birthday, address)
                    .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                    .subscribe(new CommonSubscriber<BaseInfo>() {
                        @Override
                        public void onNext(BaseInfo baseInfo) {
                            Log.i("TAG", "--------" + baseInfo.getMsg());
                            if (baseInfo.getCode() == 245) {
                                getUserInfo();
                                if (selectMedia.size() > 0) {
                                    changeUserHeadportrait();
                                } else {
                                    ToastUtils.showToast(R.string.修改成功);
                                    if (loadingDialog != null) {
                                        loadingDialog.dismiss();
                                    }
                                }
                            } else {
                                ToastUtils.showToast(baseInfo.getMsg());
                                if (loadingDialog != null) {
                                    loadingDialog.dismiss();
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            Log.i("TAG", "--------changeUserInfo," + e.toString());
                            ToastUtils.showToast(R.string.修改失败);
                            loadingDialog.dismiss();
                        }
                    });
        }else {//第三方登录
            retrofitHelper.changeOauthUserInfo(uid, phoneNum, email, signature, nickname, gender, username, profession, birthday, address)
                    .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                    .subscribe(new CommonSubscriber<BaseInfo>() {
                        @Override
                        public void onNext(BaseInfo baseInfo) {
                            Log.i("TAG", "--------" + baseInfo.getMsg());
                            if (baseInfo.getCode() == 245) {
                                getUserInfo();
                                if (selectMedia.size() > 0) {
                                    changeUserHeadportrait();
                                } else {
                                    ToastUtils.showToast(R.string.修改成功);
                                    if (loadingDialog != null) {
                                        loadingDialog.dismiss();
                                    }
                                }
                            } else {
                                ToastUtils.showToast(baseInfo.getMsg());
                                if (loadingDialog != null) {
                                    loadingDialog.dismiss();
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            Log.i("TAG", "--------changeUserInfo," + e.toString());
                            ToastUtils.showToast(R.string.修改失败);
                            loadingDialog.dismiss();
                        }
                    });
        }
    }

    //修改用户头像
    public void changeUserHeadportrait(){
        if (selectMedia.size() > 0) {
            File file = new File(selectMedia.get(0).getCompressPath());
            String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
            String token = PreferencesUtils.getLoginInfo().getObj().getToken();
            Map<String, RequestBody> bodyMap = new HashMap<>();
            bodyMap.put("mwfile"+"\"; filename=\""+file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
            bodyMap.put("uid", RequestBody.create(MediaType.parse("text/plain"), uid));
            bodyMap.put("token", RequestBody.create(MediaType.parse("text/plain"), token));
            retrofitHelper.changeUserHeadportrait(bodyMap)
                    .compose(RxUtil.<ChangeHeadImgBean>rxSchedulerHelper())
                    .subscribe(new CommonSubscriber<ChangeHeadImgBean>() {
                        @Override
                        public void onNext(ChangeHeadImgBean changeHeadImgBean) {
                            Log.i("TAG", "--------Msg"+changeHeadImgBean.getMsg()+",code"+changeHeadImgBean.getCode());
                            if (loadingDialog != null){
                                loadingDialog.dismiss();
                            }
                            if (changeHeadImgBean.getCode() == 232){
                                ToastUtils.showToast(R.string.修改成功);
                                //修改保存的logininfo头像url
                                Log.i("TAG", "--------changeUserHeadportrait,"+changeHeadImgBean.getObj());
                                LoginInfo loginInfo = PreferencesUtils.getLoginInfo();
                                loginInfo.getObj().setImgeUrl(changeHeadImgBean.getObj());
                                PreferencesUtils.saveLoginInfo(loginInfo);
                                if (loginInfo.getObj().getGender()) {
                                    Glide.with(mContext).load(changeHeadImgBean.getObj())
                                            .error(R.mipmap.touxiang_boy_nor)
                                            .centerCrop()
                                            .into(mView.getHeadPortraitImg());
                                }else {
                                    Glide.with(mContext).load(changeHeadImgBean.getObj())
                                            .error(R.mipmap.touxiang_girl_nor)
                                            .centerCrop()
                                            .into(mView.getHeadPortraitImg());
                                }
                            }else {
                                ToastUtils.showToast(changeHeadImgBean.getMsg());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            Log.i("TAG", "--------changeUserHeadportrait,"+e.toString());
                            loadingDialog.dismiss();
                        }
                    });
        }
    }

    public void selectHeadPortrait(){
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
        config.setSelectMode(FunctionConfig.MODE_SINGLE);
        config.setShowCamera(true);
        config.setEnablePreview(true);
        config.setEnableCrop(true);
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
            selectMedia = resultList;
            Log.i("callBack_result", selectMedia.size() + "");
            if (null != resultList && resultList.size() > 0) {
                Glide.with(mContext).load(resultList.get(0).getCompressPath())
                        .error(R.mipmap.icon_default)
                        .into(mView.getHeadPortraitImg());
            }
        }

        @Override
        public void onSelectSuccess(LocalMedia localMedia) {
            Log.i("TAG", "-----单张"+localMedia.getPath() + "");
            selectMedia.add(localMedia);
            Glide.with(mContext).load(localMedia.getPath())
                    .error(R.mipmap.icon_default)
                    .into(mView.getHeadPortraitImg());
        }
    };

    /**
     * 选择性别
     */
    int index;
    public void switchSex(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle(R.string.请选择性别);
        final String[] sex = {mContext.getResources().getString(R.string.male),
                mContext.getResources().getString(R.string.female)};
        //  设置一个单项选择下拉框
        /**
         * 第一个参数指定我们要显示的一组下拉单选框的数据集合
         * 第二个参数代表索引，指定默认哪一个单选框被勾选上，1表示默认'跟随系统' 会被勾选上
         * 第三个参数给每一个单选项绑定一个监听器
         */
        builder.setSingleChoiceItems(sex, 0, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                index = which;
            }
        });
        builder.setPositiveButton(mContext.getResources().getString(R.string.dialog_confirm),
                new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                mView.getSexTV().setText(sex[index]);
            }
        });
        builder.setNegativeButton(mContext.getResources().getString(R.string.dialog_cancel),
                new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){

            }
        });
        builder.show();
    }

    public void selectBirthday(){
        //时间选择器
        TimePickerView pvTime = new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                mView.getBirthdayTV().setText(getTime(date));
            }
        })
        .setType(TimePickerView.Type.YEAR_MONTH_DAY)//默认全部显示
        .isCyclic(true)//是否循环滚动
        .build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
}
