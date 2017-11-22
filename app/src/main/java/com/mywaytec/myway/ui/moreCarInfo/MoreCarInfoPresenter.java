package com.mywaytec.myway.ui.moreCarInfo;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BluetoothLeService;
import com.mywaytec.myway.base.Constant;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.bean.BleInfoBean;
import com.mywaytec.myway.model.bean.ObjStringBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.BleKitUtils;
import com.mywaytec.myway.utils.BleUtil;
import com.mywaytec.myway.utils.ConversionUtil;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxCountDown;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.SystemUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.utils.data.BleInfo;
import com.mywaytec.myway.view.CommonSubscriber;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by shemh on 2017/4/11.
 */

public class MoreCarInfoPresenter extends RxPresenter<MoreCarInfoView> implements PopupWindow.OnDismissListener {

    RetrofitHelper mRetrofitHelper;
    Context mContext;
    BleInfoBean bleInfoBean;

    @Inject
    public MoreCarInfoPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(MoreCarInfoView view) {
        super.attachView(view);
        this.mContext = mView.getContext();
        currentMode = BleInfo.getBleInfo().getDengdaimoshi();
        if (currentMode == 1) {//全彩流水
            mView.getDengdaiTV().setText(R.string.flowing);
        } else if (currentMode == 2) {//全彩呼吸
            mView.getDengdaiTV().setText(R.string.breathing);
        } else if (currentMode == 3) {//炫彩霓虹
            mView.getDengdaiTV().setText(R.string.neon);
        } else if (currentMode == 4) {//照明模式
            mView.getDengdaiTV().setText(R.string.lighting);
        } else if (currentMode == 5) {//
            mView.getDengdaiTV().setText(R.string.warning);
        } else if (currentMode == 0){//关闭
            mView.getDengdaiTV().setText(R.string.close);
        }
    }

    public RetrofitHelper getRetrofitHelper(){
        return mRetrofitHelper;
    }

    //修改密码对话框
    public void changeBlePasswordDialog(Context context, final String mDeviceAddress){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        // 创建对话框
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View view = View.inflate(context, R.layout.dialog_change_ble_password, null);
        TextView tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        final EditText etNewPassword = (EditText) view.findViewById(R.id.et_new_password);
        final EditText etConfirmPassword = (EditText) view.findViewById(R.id.et_confirm_password);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = etNewPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();
                if (TextUtils.isEmpty(newPassword)){
                    ToastUtils.showToast(R.string.please_enter_the_password);
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)){
                    ToastUtils.showToast(R.string.please_make_sure_the_password);
                    return;
                }
                if (newPassword.length() < 6){
                    ToastUtils.showToast(R.string.the_long_of_the_password_is_not_correct);
                    return;
                }
                if(!newPassword.equals(confirmPassword)){
                    ToastUtils.showToast(R.string.the_password_does_not_match_for_the_two_times);
                    return;
                }

                int password = Integer.parseInt(confirmPassword);
                BleKitUtils.writeP(mDeviceAddress, BleUtil.setVehiclePassword(password+""), new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {

                    }
                });
                alertDialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(view);
        alertDialog.show();
    }

    //灯带模式
    PopupWindow dengdaiPopupWindow;
    public void openDengdaiPopupWindow(View v, String mDeviceAddress) {
        //防止重复按按钮
        if (dengdaiPopupWindow != null && dengdaiPopupWindow.isShowing()) {
            return;
        }
        //设置PopupWindow的View
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_dengdai, null);
        dengdaiPopupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置背景,这个没什么效果，不添加会报错
        dengdaiPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击弹窗外隐藏自身
        dengdaiPopupWindow.setFocusable(true);
        dengdaiPopupWindow.setOutsideTouchable(true);
        //设置动画
        dengdaiPopupWindow.setAnimationStyle(R.style.PopupWindow);
        //设置位置
        dengdaiPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        //设置消失监听
        dengdaiPopupWindow.setOnDismissListener(this);
        //设置PopupWindow的View点击事件
        setOnPopupViewClick(view, mDeviceAddress);
        //设置背景色
        setBackgroundAlpha(0.5f);
    }

    //设置屏幕背景透明效果
    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = ((Activity)mContext).getWindow().getAttributes();
        lp.alpha = alpha;
        ((Activity)mContext).getWindow().setAttributes(lp);
    }

    ImageView imgRight1;
    ImageView imgRight2;
    ImageView imgRight3;
    ImageView imgRight4;
    ImageView imgRight5;
    int currentMode = 0;
    private void setOnPopupViewClick(View view, final String mDeviceAddress) {
        TextView tvClose = (TextView) view.findViewById(R.id.tv_close);
        LinearLayout layoutDengdai1 = (LinearLayout) view.findViewById(R.id.layout_dengdai_1);
        LinearLayout layoutDengdai2 = (LinearLayout) view.findViewById(R.id.layout_dengdai_2);
        LinearLayout layoutDengdai3 = (LinearLayout) view.findViewById(R.id.layout_dengdai_3);
        LinearLayout layoutDengdai4 = (LinearLayout) view.findViewById(R.id.layout_dengdai_4);
        LinearLayout layoutDengdai5 = (LinearLayout) view.findViewById(R.id.layout_dengdai_5);
        imgRight1 = (ImageView) view.findViewById(R.id.img_right_1);
        imgRight2 = (ImageView) view.findViewById(R.id.img_right_2);
        imgRight3 = (ImageView) view.findViewById(R.id.img_right_3);
        imgRight4 = (ImageView) view.findViewById(R.id.img_right_4);
        imgRight5 = (ImageView) view.findViewById(R.id.img_right_5);
        if (currentMode == 1){
            imgRight1.setVisibility(View.VISIBLE);
        }else if (currentMode == 2){
            imgRight2.setVisibility(View.VISIBLE);
        }else if (currentMode == 3){
            imgRight3.setVisibility(View.VISIBLE);
        }else if (currentMode == 4){
            imgRight4.setVisibility(View.VISIBLE);
        }else if (currentMode == 5){
            imgRight5.setVisibility(View.VISIBLE);
        }
        //关闭
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BleKitUtils.writeP(mDeviceAddress, Constant.BLE.CLOSE_DENGDAI_MODE, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {

                    }
                });
            }
        });
        //全彩流水
        layoutDengdai1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BleKitUtils.writeP(mDeviceAddress, BleUtil.dengdaiMode(1), new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {

                    }
                });
            }
        });
        //全彩呼吸
        layoutDengdai2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BleKitUtils.writeP(mDeviceAddress, BleUtil.dengdaiMode(2), new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {

                    }
                });
            }
        });
        //炫彩霓虹
        layoutDengdai3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BleKitUtils.writeP(mDeviceAddress, BleUtil.dengdaiMode(3), new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {

                    }
                });
            }
        });
        //照明模式
        layoutDengdai4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BleKitUtils.writeP(mDeviceAddress, BleUtil.dengdaiMode(4), new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {

                    }
                });
            }
        });
        //警灯模式
        layoutDengdai5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BleKitUtils.writeP(mDeviceAddress, BleUtil.dengdaiMode(5), new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {

                    }
                });
            }
        });
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1);
    }

    StringBuilder snCode1;
    StringBuilder snCode2;
    /**
     * 蓝牙应答信息
     */
    public void displayData(byte[] data) {
        String info = ConversionUtil.byte2HexStr(data);
        Log.i("TAG", "-----MoreCarInfo_info" + info);
        String[] infos = info.split(" ");
        Log.i("TAG", "-----MoreCarInfo_infos" + infos.length);
        if (infos.length > 5) {
            //油门霍尔校准
            if ("04".equals(infos[2]) && "03".equals(infos[3]) && "02".equals(infos[4])) {
                if (null != layoutIsContinue && null != layoutStartAdjust)
                    if ("01".equals(infos[6])) {//成功
                        layoutIsContinue.setVisibility(View.GONE);
                        layoutStartAdjust.setVisibility(View.VISIBLE);
                    } else if ("00".equals(infos[6])) {//失败
                        layoutIsContinue.setVisibility(View.VISIBLE);
                        layoutStartAdjust.setVisibility(View.GONE);
                    }
            }
            //油门霍尔校准确认
            else if ("04".equals(infos[2]) && "03".equals(infos[3]) && "06".equals(infos[4])) {
                if ("01".equals(infos[6])) {//成功
                    if (null != layoutStartAdjust && null != layoutProgress && null != progressBar &&
                            null != layoutAdjustConfirm && null != tvYoumenTishi && null != tvStartAdjust
                            && null != tvProgress) {
                        layoutStartAdjust.setVisibility(View.GONE);
                        layoutProgress.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        RxCountDown.countdown(5).subscribe(new Subscriber<Integer>() {
                            @Override
                            public void onCompleted() {
                                if (youmenCount == 2) {
                                    layoutProgress.setVisibility(View.GONE);
                                    progressBar.setVisibility(View.GONE);
                                    layoutAdjustConfirm.setVisibility(View.VISIBLE);
                                    progressBar.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (null != youmenPopupWindow) {
                                                youmenPopupWindow.dismiss();
                                            }
                                        }
                                    }, 2000);
                                } else if (youmenCount == 1) {
                                    youmenCount = 2;
                                    layoutStartAdjust.setVisibility(View.VISIBLE);
                                    layoutProgress.setVisibility(View.GONE);
                                    progressBar.setVisibility(View.GONE);
                                    tvYoumenTishi.setText(R.string.release_accelerator);
                                    tvStartAdjust.setText(R.string.start_second_calibration);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onNext(Integer integer) {
                                progressBar.setProgress(100 - 20 * integer);
                                tvProgress.setText((100 - 20 * integer) + "%");
                            }
                        });
                    }
                } else if ("00".equals(infos[6])) {//失败
                }
            }
            //设置灯带模式
            else if ("04".equals(infos[2]) && "02".equals(infos[3]) && "11".equals(infos[4])){
                if ("01".equals(infos[6])) {//全彩流水
                    currentMode = 1;
                    mView.getDengdaiTV().setText(R.string.flowing);
                    bleInfoBean = BleInfo.getBleInfo();
                    bleInfoBean.setDengdaimoshi(currentMode);
                    BleInfo.saveBleInfo(bleInfoBean);
                } else if ("02".equals(infos[6])) {//全彩呼吸
                    currentMode = 2;
                    mView.getDengdaiTV().setText(R.string.breathing);
                    bleInfoBean = BleInfo.getBleInfo();
                    bleInfoBean.setDengdaimoshi(currentMode);
                    BleInfo.saveBleInfo(bleInfoBean);
                } else if ("03".equals(infos[6])) {//炫彩霓虹
                    currentMode = 3;
                    mView.getDengdaiTV().setText(R.string.neon);
                    bleInfoBean = BleInfo.getBleInfo();
                    bleInfoBean.setDengdaimoshi(currentMode);
                    BleInfo.saveBleInfo(bleInfoBean);
                } else if ("04".equals(infos[6])) {//照明模式
                    currentMode = 4;
                    mView.getDengdaiTV().setText(R.string.lighting);
                    bleInfoBean = BleInfo.getBleInfo();
                    bleInfoBean.setDengdaimoshi(currentMode);
                    BleInfo.saveBleInfo(bleInfoBean);
                } else if ("05".equals(infos[6])) {//
                    currentMode = 5;
                    mView.getDengdaiTV().setText(R.string.warning);
                    bleInfoBean = BleInfo.getBleInfo();
                    bleInfoBean.setDengdaimoshi(currentMode);
                    BleInfo.saveBleInfo(bleInfoBean);
                } else if ("FF".equals(infos[6])){//关闭
                    currentMode = 0;
                    mView.getDengdaiTV().setText(R.string.close);
                    bleInfoBean = BleInfo.getBleInfo();
                    bleInfoBean.setDengdaimoshi(currentMode);
                    BleInfo.saveBleInfo(bleInfoBean);
                }
                if (null != dengdaiPopupWindow && dengdaiPopupWindow.isShowing()) {
                    if (null != imgRight1 && null != imgRight2 && null != imgRight3
                            && null != imgRight4 && null != imgRight5) {
                        if ("01".equals(infos[6])) {//全彩流水
                            currentMode = 1;
                            imgRight1.setVisibility(View.VISIBLE);
                            imgRight2.setVisibility(View.GONE);
                            imgRight3.setVisibility(View.GONE);
                            imgRight4.setVisibility(View.GONE);
                            imgRight5.setVisibility(View.GONE);
                            bleInfoBean = BleInfo.getBleInfo();
                            bleInfoBean.setDengdaimoshi(currentMode);
                            BleInfo.saveBleInfo(bleInfoBean);
                        } else if ("02".equals(infos[6])) {//全彩呼吸
                            currentMode = 2;
                            imgRight1.setVisibility(View.GONE);
                            imgRight2.setVisibility(View.VISIBLE);
                            imgRight3.setVisibility(View.GONE);
                            imgRight4.setVisibility(View.GONE);
                            imgRight5.setVisibility(View.GONE);
                            bleInfoBean = BleInfo.getBleInfo();
                            bleInfoBean.setDengdaimoshi(currentMode);
                            BleInfo.saveBleInfo(bleInfoBean);
                        } else if ("03".equals(infos[6])) {//炫彩霓虹
                            currentMode = 3;
                            imgRight1.setVisibility(View.GONE);
                            imgRight2.setVisibility(View.GONE);
                            imgRight3.setVisibility(View.VISIBLE);
                            imgRight4.setVisibility(View.GONE);
                            imgRight5.setVisibility(View.GONE);
                            bleInfoBean = BleInfo.getBleInfo();
                            bleInfoBean.setDengdaimoshi(currentMode);
                            BleInfo.saveBleInfo(bleInfoBean);
                        } else if ("04".equals(infos[6])) {//照明模式
                            currentMode = 4;
                            imgRight1.setVisibility(View.GONE);
                            imgRight2.setVisibility(View.GONE);
                            imgRight3.setVisibility(View.GONE);
                            imgRight4.setVisibility(View.VISIBLE);
                            imgRight5.setVisibility(View.GONE);
                            bleInfoBean = BleInfo.getBleInfo();
                            bleInfoBean.setDengdaimoshi(currentMode);
                            BleInfo.saveBleInfo(bleInfoBean);
                        } else if ("05".equals(infos[6])) {//
                            currentMode = 5;
                            imgRight1.setVisibility(View.GONE);
                            imgRight2.setVisibility(View.GONE);
                            imgRight3.setVisibility(View.GONE);
                            imgRight4.setVisibility(View.GONE);
                            imgRight5.setVisibility(View.VISIBLE);
                            bleInfoBean = BleInfo.getBleInfo();
                            bleInfoBean.setDengdaimoshi(currentMode);
                            BleInfo.saveBleInfo(bleInfoBean);
                        } else if ("FF".equals(infos[6])){//关闭
                            currentMode = 0;
                            imgRight1.setVisibility(View.GONE);
                            imgRight2.setVisibility(View.GONE);
                            imgRight3.setVisibility(View.GONE);
                            imgRight4.setVisibility(View.GONE);
                            imgRight5.setVisibility(View.GONE);
                            bleInfoBean = BleInfo.getBleInfo();
                            bleInfoBean.setDengdaimoshi(currentMode);
                            BleInfo.saveBleInfo(bleInfoBean);
                        }else {//失败
                        }
                    }
                }
            }
            //氛围灯模式
            else if ("04".equals(infos[2]) && "01".equals(infos[3]) && "09".equals(infos[4])){
                currentMode = data[6];
                bleInfoBean = BleInfo.getBleInfo();
                bleInfoBean.setDengdaimoshi(currentMode);
                BleInfo.saveBleInfo(bleInfoBean);
                Log.i("TAG", "------灯带当前模式，" + currentMode);
                if (null != imgRight1 && null != imgRight2 && null != imgRight3 &&
                        null != imgRight4 && null != imgRight5) {
                    if (currentMode == 1) {
                        imgRight1.setVisibility(View.VISIBLE);
                        mView.getDengdaiTV().setText(R.string.flowing);
                    } else if (currentMode == 2) {
                        imgRight2.setVisibility(View.VISIBLE);
                        mView.getDengdaiTV().setText(R.string.breathing);
                    } else if (currentMode == 3) {
                        imgRight3.setVisibility(View.VISIBLE);
                        mView.getDengdaiTV().setText(R.string.neon);
                    } else if (currentMode == 4) {
                        imgRight4.setVisibility(View.VISIBLE);
                        mView.getDengdaiTV().setText(R.string.lighting);
                    } else if (currentMode == 5) {
                        imgRight5.setVisibility(View.VISIBLE);
                        mView.getDengdaiTV().setText(R.string.warning);
                    }
                }
            }
            //硬件版本号
            else if ("04".equals(infos[2]) && "01".equals(infos[3]) && "06".equals(infos[4])) {
                StringBuilder code = new StringBuilder();
                char code1 = (char) Integer.parseInt(infos[13], 16);
                code.append(code1);
                char code2 = (char) Integer.parseInt(infos[12], 16);
                code.append(code2);
                char code3 = (char) Integer.parseInt(infos[11], 16);
                code.append(code3);
                char code4 = (char) Integer.parseInt(infos[10], 16);
                code.append(code4);
                char code5 = (char) Integer.parseInt(infos[9], 16);
                code.append(code5);
                char code6 = (char) Integer.parseInt(infos[8], 16);
                code.append(code6);
                char code7 = (char) Integer.parseInt(infos[7], 16);
                code.append(code7);
                char code8 = (char) Integer.parseInt(infos[6], 16);
                code.append(code8);
                String hardwareCode = code.toString();
                Log.i("TAG", "------硬件版本号," + hardwareCode);
                BleInfoBean bleInfoBean = BleInfo.getBleInfo();
                bleInfoBean.setYingjianCode(hardwareCode);
                BleInfo.saveBleInfo(bleInfoBean);
            }
            //车辆SN码
            else if ("04".equals(infos[2]) && "01".equals(infos[3]) && "03".equals(infos[4])
                    || !("4D".equals(infos[0]) && "57".equals(infos[1]))) {
                Log.i("TAG", "-----车辆SN码" + info);
                if (infos.length == 20) {
                    snCode1 = new StringBuilder();
                    String code1 = infos[19].substring(1);
                    snCode1.append(code1);
                    String code2 = infos[18].substring(1);
                    snCode1.append(code2);
                    String code3 = infos[17].substring(1);
                    snCode1.append(code3);
                    String code4 = infos[16].substring(1);
                    snCode1.append(code4);
                    String code5 = infos[15].substring(1);
                    snCode1.append(code5);
                    String code6 = infos[14].substring(1);
                    snCode1.append(code6);
                    String code7 = infos[13].substring(1);
                    snCode1.append(code7);
                    String code8 = infos[12].substring(1);
                    snCode1.append(code8);
                    String code9 = infos[11].substring(1);
                    snCode1.append(code9);
                    String code10 = infos[10].substring(1);
                    snCode1.append(code10);
                    String code11 = infos[9].substring(1);
                    snCode1.append(code11);
                    String code12 = infos[8].substring(1);
                    snCode1.append(code12);
                    String code13 = infos[7].substring(1);
                    snCode1.append(code13);
                    String code14 = infos[6].substring(1);
                    snCode1.append(code14);
                }else {
                    snCode2 = new StringBuilder();
                    String code1 = infos[9].substring(1);
                    snCode2.append(code1);
                    String code2 = infos[8].substring(1);
                    snCode2.append(code2);
                    String code3 = infos[7].substring(1);
                    snCode2.append(code3);
                    String code4 = infos[6].substring(1);
                    snCode2.append(code4);
                    String code5 = infos[5].substring(1);
                    snCode2.append(code5);
                    String code6 = infos[4].substring(1);
                    snCode2.append(code6);
                    String code7 = infos[3].substring(1);
                    snCode2.append(code7);
                    String code8 = infos[2].substring(1);
                    snCode2.append(code8);
                    String code9 = infos[1].substring(1);
                    snCode2.append(code9);
                    String code10 = infos[0].substring(1);
                    snCode2.append(code10);
                }

                String snCodeStr = snCode1.toString() + snCode2.toString();
                if (snCodeStr.length() == 24) {
                    BleInfoBean bleInfoBean = BleInfo.getBleInfo();
                    bleInfoBean.setSnCode(snCodeStr);
                    BleInfo.saveBleInfo(bleInfoBean);
                    if (SystemUtil.isNetworkConnected()) {
                        Log.i("TAG", "------判断车主，" + snCodeStr);
                        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
                        mRetrofitHelper.vehicleUserOwner(uid, snCodeStr)
                                .compose(RxUtil.<ObjStringBean>rxSchedulerHelper())
                                .subscribe(new CommonSubscriber<ObjStringBean>() {
                                    @Override
                                    public void onNext(ObjStringBean objStringBean) {
                                        Log.i("TAG", "------ObjStringBean," + objStringBean.getObj());
                                        if (objStringBean.getCode() == 1) {
                                            BleInfoBean bleInfoBean = BleInfo.getBleInfo();
                                            bleInfoBean.setIsChezhu(objStringBean.getObj());
                                            BleInfo.saveBleInfo(bleInfoBean);
                                        }
                                    }
                                });
                    } else {
                    }
                }
            }
        }
    }


    PopupWindow youmenPopupWindow;
    LinearLayout layoutIsContinue;
    LinearLayout layoutTishi;
    LinearLayout layoutStartAdjust;
    LinearLayout layoutProgress;
    TextView tvYoumenTishi;
    TextView tvStartAdjust;
    LinearLayout layoutAdjustConfirm;
    ProgressBar progressBar;
    TextView tvProgress;
    //第几次油门校准
    private int youmenCount = 1;

    /**
     * 油门校准弹框
     */
    public void showYoumenAlignmentPop(View v, final String mDeviceAddress) {
        //防止重复按按钮
        if (youmenPopupWindow != null && youmenPopupWindow.isShowing()) {
            return;
        }
        //设置PopupWindow的View
        final View view = LayoutInflater.from(mContext).inflate(R.layout.popup_youmen_alignment, null);
        layoutIsContinue = (LinearLayout) view.findViewById(R.id.layout_is_continue);
        layoutTishi = (LinearLayout) view.findViewById(R.id.layout_tishi);
        layoutStartAdjust = (LinearLayout) view.findViewById(R.id.layout_start_adjust);
        layoutProgress = (LinearLayout) view.findViewById(R.id.layout_progress);
        tvYoumenTishi = (TextView) view.findViewById(R.id.tv_youmen_tishi);
        layoutAdjustConfirm = (LinearLayout) view.findViewById(R.id.layout_adjust_confirm);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        TextView tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvStartAdjust = (TextView) view.findViewById(R.id.tv_start_adjust);
        tvProgress = (TextView) view.findViewById(R.id.tv_progress);
        tvProgress.setVisibility(View.VISIBLE);

        //继续
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uuid = PreferencesUtils.getString(mContext, "uuid");
                    if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                        BleKitUtils.writeP(mDeviceAddress, Constant.BLE.YOUMEN_ALIGNMENT, new BleWriteResponse() {
                            @Override
                            public void onResponse(int code) {

                            }
                        });
                    } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {

                    }
            }
        });
        //取消
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (youmenPopupWindow != null && youmenPopupWindow.isShowing()) {
                    youmenPopupWindow.dismiss();
                }
            }
        });
        //开始校准
        tvStartAdjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uuid = PreferencesUtils.getString(mContext, "uuid");
                    if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                        BleKitUtils.writeP(mDeviceAddress, Constant.BLE.YOUMEN_ALIGNMENT_CONFIRM, new BleWriteResponse() {
                            @Override
                            public void onResponse(int code) {

                            }
                        });
                    } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {

                    }
            }
        });
        youmenPopupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置背景,这个没什么效果，不添加会报错
        youmenPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击弹窗外隐藏自身
        youmenPopupWindow.setFocusable(true);
        youmenPopupWindow.setOutsideTouchable(true);
        //设置动画
        youmenPopupWindow.setAnimationStyle(R.style.PopupWindow);
        //设置位置
        youmenPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }
}
