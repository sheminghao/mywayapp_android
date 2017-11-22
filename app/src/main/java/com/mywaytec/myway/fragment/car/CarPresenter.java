package com.mywaytec.myway.fragment.car;

import android.content.Context;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.Constant;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.bean.BleInfoBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.BleKitUtils;
import com.mywaytec.myway.utils.BleUtil;
import com.mywaytec.myway.utils.ConversionUtil;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.utils.data.BleInfo;
import com.mywaytec.myway.view.CommonSubscriber;
import com.mywaytec.myway.view.LoadingDialog;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import static com.inuker.bluetooth.library.Constants.REQUEST_FAILED;
import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;

/**
 * Created by shemh on 2017/3/9.
 */

public class CarPresenter extends RxPresenter<CarView> {

    RetrofitHelper mRetrofitHelper;
    Context mContext;
    String uuid;
    String mDeviceAddress;

    @Inject
    public CarPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper =  mRetrofitHelper;
        registerEvent();
    }

    public RetrofitHelper getRetrofitHelper(){
        return  mRetrofitHelper;
    }

    void registerEvent() {
        getData();
    }

    @Override
    public void attachView(CarView view) {
        mView = view;
        mContext = mView.getContext();
        uuid = PreferencesUtils.getString(mContext, "uuid");
        mDeviceAddress = mView.getDeviceAddress();
    }

    public void getData() {
    }

    //使用车辆
    public void useCar(String snCode, int mileage){
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        mRetrofitHelper.useCar(uid, token, snCode, mileage)
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseInfo>() {
                    @Override
                    public void onNext(BaseInfo baseInfo) {
                        Log.i("TAG", "------使用车辆," + baseInfo.getCode());
                        if (baseInfo.getCode() == 1){
                            Log.i("TAG", "------使用车辆成功");
                        }
                    }
                });
    }

    AlertDialog verificationVehicleDialog;
    public void verificationVehiclePassword(final String mDeviceAddress){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        // 创建对话框
        verificationVehicleDialog = builder.create();
        verificationVehicleDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        View view = View.inflate(mContext, R.layout.dialog_verify_password, null);
        TextView tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        final TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        final EditText etPassword = (EditText) view.findViewById(R.id.et_password);
        final LinearLayout layoutVerifyPassword = (LinearLayout) view.findViewById(R.id.layout_verify_password);
        final LinearLayout layoutResetPassword = (LinearLayout) view.findViewById(R.id.layout_reset_password);
        TextView tvForgetPassword = (TextView) view.findViewById(R.id.tv_forget_password);
        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutVerifyPassword.setVisibility(View.GONE);
                layoutResetPassword.setVisibility(View.VISIBLE);
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString().trim();
                        Log.i("TAG", "------verificationVehiclePassword确认发送");
                        mView.setOther(true);
                        SystemClock.sleep(30);
                        if (layoutVerifyPassword.getVisibility() == View.VISIBLE) {
                            if (TextUtils.isEmpty(password)){
                                ToastUtils.showToast(R.string.please_enter_the_password);
                                return;
                            }
                            if (password.length() != 6){
                                ToastUtils.showToast(R.string.密码长度不正确);
                                return;
                            }
                            //车辆密码验证
                            BleKitUtils.writeP(mDeviceAddress, BleUtil.verificationVehiclePassword(password), new BleWriteResponse() {
                                @Override
                                public void onResponse(int code) {
                                    mView.setOther(false);
                                }
                            });
                        }else {
                            if (BleUtil.isChezhu(mRetrofitHelper)) {
                                //车辆密码复位
                                BleKitUtils.writeP(mDeviceAddress, Constant.BLE.VEHICLE_PASSWORD_RESET, new BleWriteResponse() {
                                    @Override
                                    public void onResponse(int code) {
                                        mView.setOther(false);
                                    }
                                });
                            }else {
                                //TODO 非车主操作提示
                                ToastUtils.showToast("非车主不可操作");
                            }
                        }

            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificationVehicleDialog.dismiss();
            }
        });
        verificationVehicleDialog.setView(view);
        verificationVehicleDialog.show();
    }

    /**
     * 蓝牙应答信息
     */
    public void displayData(byte[] data) {
        String info = ConversionUtil.byte2HexStr(data);
//        Log.i("TAG", "-----MoreCarInfo_info" + info);
        String[] infos = info.split(" ");
//        Log.i("TAG", "-----MoreCarInfo_infos" + infos.length);
        if (infos.length > 5) {
            //车辆密码验证
            if ("04".equals(infos[2]) && "02".equals(infos[3]) && "12".equals(infos[4])) {
                if ("01".equals(infos[6])) {//验证成功
                    Log.i("TAG", "------密码验证成功");
                    if (null != verificationVehicleDialog){
                        verificationVehicleDialog.dismiss();
                    }
                } else {//验证失败
                    Log.i("TAG", "------密码验证失败");
                    ToastUtils.showToast(R.string.password_verification_failed);
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
        }
    }

    LoadingDialog loadingDialog;
    //发送指令超时时间
    private int sendTime;
    public void sendZhiling(){
        //缓存有数据，就不重新发送指令，如果没有数据，就发送指令
        uuid = PreferencesUtils.getString(mContext, "uuid");
        mDeviceAddress = mView.getDeviceAddress();
        loadingDialog = new LoadingDialog(mContext);
        loadingDialog.show();
        programLocation();
    }

    private void programLocation(){
        SystemClock.sleep(80);
        if (!"00".equals(BleInfo.getBleInfo().getProgramLocation())){//不在应用程序中，查询程序运行位置
            //程序运行位置
            if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                BleKitUtils.writeP(mDeviceAddress, Constant.BLE.PROGRAM_LOCATION, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        if (code == REQUEST_SUCCESS){
                            sendTime = 0;
                            snCode();
                        }else if (code == REQUEST_FAILED){
                            if (sendTime > 10){
                                loadingDialog.dismiss();
                                return;
                            }
                            sendTime++;
                            programLocation();
                        }
                    }
                });
            } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.PROGRAM_LOCATION, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        if (code == REQUEST_SUCCESS) {
                            sendTime = 0;
                            snCode();
                        } else if (code == REQUEST_FAILED) {
                            if (sendTime > 10) {
                                loadingDialog.dismiss();
                                return;
                            }
                            sendTime++;
                            programLocation();
                        }
                    }
                });
            }
        }else {
            snCode();
        }
    }

    private void snCode() {
        SystemClock.sleep(80);
        if (TextUtils.isEmpty(BleInfo.getBleInfo().getSnCode())) {
            //查询车辆sn码
            if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                BleKitUtils.writeP(mDeviceAddress, Constant.BLE.SN_CODE, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        if (code == REQUEST_SUCCESS) {
                            sendTime = 0;
                            ruanjianbanbenhao();
                        } else if (code == REQUEST_FAILED) {
                            if (sendTime > 10) {
                                loadingDialog.dismiss();
                                return;
                            }
                            sendTime++;
                            snCode();
                        }
                    }
                });
            } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.SN_CODE, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        if (code == REQUEST_SUCCESS) {
                            sendTime = 0;
                            ruanjianbanbenhao();
                        } else if (code == REQUEST_FAILED) {
                            if (sendTime > 10) {
                                loadingDialog.dismiss();
                                return;
                            }
                            sendTime++;
                            snCode();
                        }
                    }
                });
            }
        }else {
            ruanjianbanbenhao();
        }
    }

    private void ruanjianbanbenhao() {
        if (TextUtils.isEmpty(BleInfo.getBleInfo().getRuanjianCode())) {
            //查询软件版本号
            if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                BleKitUtils.writeP(mDeviceAddress, Constant.BLE.SOFTWARE_VERSION_CODE, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        if (code == REQUEST_SUCCESS) {
                            sendTime = 0;
                            speedLimitValues();
                        } else if (code == REQUEST_FAILED) {
                            if (sendTime > 10) {
                                loadingDialog.dismiss();
                                return;
                            }
                            sendTime++;
                            ruanjianbanbenhao();
                        }
                    }
                });
            } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.SOFTWARE_VERSION_CODE, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        if (code == REQUEST_SUCCESS) {
                            sendTime = 0;
                            speedLimitValues();
                        } else if (code == REQUEST_FAILED) {
                            if (sendTime > 10) {
                                loadingDialog.dismiss();
                                return;
                            }
                            sendTime++;
                            ruanjianbanbenhao();
                        }
                    }
                });
            }
        }else {
            speedLimitValues();
        }
    }

    //车辆最高/低限速值
    private void speedLimitValues(){
        if (BleInfo.getBleInfo().getHighSpeed() == 0) {
            SystemClock.sleep(80);
            //车辆最高/低限速值
            if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                BleKitUtils.writeP(mDeviceAddress, Constant.BLE.SPEED_LIMIT_VALUES, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        if (code == REQUEST_SUCCESS) {
                            sendTime = 0;
                            carState();
                        } else if (code == REQUEST_FAILED) {
                            if (sendTime > 10) {
                                loadingDialog.dismiss();
                                return;
                            }
                            sendTime++;
                            speedLimitValues();
                        }
                    }
                });
            } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.SPEED_LIMIT_VALUES, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        if (code == REQUEST_SUCCESS) {
                            sendTime = 0;
                            carState();
                        } else if (code == REQUEST_FAILED) {
                            if (sendTime > 10) {
                                loadingDialog.dismiss();
                                return;
                            }
                            sendTime++;
                            speedLimitValues();
                        }
                    }
                });
            }
        }else {
            carState();
        }
    }

    //查询车辆状态
    private void carState(){
        SystemClock.sleep(80);
        //查询车辆状态
        if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
            BleKitUtils.writeP(mDeviceAddress, Constant.BLE.CAR_STATE, new BleWriteResponse() {
                @Override
                public void onResponse(int code) {
                    if (code == REQUEST_SUCCESS){
                        sendTime = 0;
                        firmwareVersionCode();
                    }else if (code == REQUEST_FAILED){
                        if (sendTime > 10){
                            loadingDialog.dismiss();
                            return;
                        }
                        sendTime++;
                        carState();
                    }
                }
            });
        } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
            BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.CAR_STATE, new BleWriteResponse() {
                @Override
                public void onResponse(int code) {
                    if (code == REQUEST_SUCCESS){
                        sendTime = 0;
                        firmwareVersionCode();
                    }else if (code == REQUEST_FAILED){
                        if (sendTime > 10){
                            loadingDialog.dismiss();
                            return;
                        }
                        sendTime++;
                        carState();
                    }
                }
            });
        }
    }

    private void firmwareVersionCode(){
        if (TextUtils.isEmpty(BleInfo.getBleInfo().getYingjianCode())) {
            SystemClock.sleep(80);
            //查询车辆硬件版本号
            if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                BleKitUtils.writeP(mDeviceAddress, Constant.BLE.FIRMWARE_VERSION_CODE, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        if (code == REQUEST_SUCCESS) {
                            sendTime = 0;
                            practicalLimitValues();
                        } else if (code == REQUEST_FAILED) {
                            if (sendTime > 10) {
                                loadingDialog.dismiss();
                                return;
                            }
                            sendTime++;
                            firmwareVersionCode();
                        }
                    }
                });
            } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.FIRMWARE_VERSION_CODE, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        if (code == REQUEST_SUCCESS) {
                            sendTime = 0;
                            practicalLimitValues();
                        } else if (code == REQUEST_FAILED) {
                            if (sendTime > 10) {
                                loadingDialog.dismiss();
                                return;
                            }
                            sendTime++;
                            firmwareVersionCode();
                        }
                    }
                });
            }
        }else {
            practicalLimitValues();
        }
    }

    private void practicalLimitValues(){
        if (BleInfo.getBleInfo().getSuduxianzhi() == 0) {
            SystemClock.sleep(80);
            //车辆实际限速值
            if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                BleKitUtils.writeP(mDeviceAddress, Constant.BLE.PRACTICAL_LIMIT_VALUES, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        if (code == REQUEST_SUCCESS) {
                            sendTime = 0;
                            loadingDialog.dismiss();
                            //发送速度请求指令
                            sendSpeedRequest();
                        } else if (code == REQUEST_FAILED) {
                            if (sendTime > 10) {
                                loadingDialog.dismiss();
                                return;
                            }
                            sendTime++;
                            practicalLimitValues();
                        }
                    }
                });
            } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.PRACTICAL_LIMIT_VALUES, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        if (code == REQUEST_SUCCESS) {
                            sendTime = 0;
                            loadingDialog.dismiss();
                            //发送速度请求指令
                            sendSpeedRequest();
                        } else if (code == REQUEST_FAILED) {
                            if (sendTime > 10) {
                                loadingDialog.dismiss();
                                return;
                            }
                            sendTime++;
                            practicalLimitValues();
                        }
                    }
                });
            }
        }else {
            loadingDialog.dismiss();
            //发送速度请求指令
            sendSpeedRequest();
        }
    }

    private Timer timer;
    private TimerTask timerTask;

    //发送速度请求指令，1秒发送8次
    public void sendSpeedRequest() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if ("00".equals(BleInfo.getBleInfo().getProgramLocation())) {//程序不在引導程序中發送速度請求
                    if (!mView.isOther()) {
                        if (null != BleUtil.getSpeedAndPower() || BleUtil.getSpeedAndPower().length > 0) {
                            Log.i("TAG", "-------发送速度请求指令");
                            if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                                BleKitUtils.writeP(mDeviceAddress, BleUtil.getSpeedAndPower(), new BleWriteResponse() {
                                    @Override
                                    public void onResponse(int code) {
                                    }
                                });
                            } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
//                            Log.i("TAG", "-------泰斗发送指令");
                                BleKitUtils.writeTaiTou(mDeviceAddress, BleUtil.getSpeedAndPower(), new BleWriteResponse() {
                                    @Override
                                    public void onResponse(int code) {
                                    }
                                });
                            }
                        }
                    }
                }
            }
        };
        timer.schedule(timerTask, 0, 125);
    }

    //停止发送速度请求指令
    public void stopSendSpeedRequest() {
        if (null != timerTask) {
            Log.i("TAG", "-------停止发送速度请求指令");
            timerTask.cancel();
            timerTask = null;
        }
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
    }
}
