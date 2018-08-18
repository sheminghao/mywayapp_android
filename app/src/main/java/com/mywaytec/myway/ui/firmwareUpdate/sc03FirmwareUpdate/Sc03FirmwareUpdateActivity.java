package com.mywaytec.myway.ui.firmwareUpdate.sc03FirmwareUpdate;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.SystemClock;
import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luck.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.luck.bluetooth.library.connect.response.BleNotifyResponse;
import com.luck.bluetooth.library.connect.response.BleWriteResponse;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.base.Constant;
import com.mywaytec.myway.model.bean.BleInfoBean;
import com.mywaytec.myway.model.bean.FirmwareInfo;
import com.mywaytec.myway.utils.AppUtils;
import com.mywaytec.myway.utils.BleKitUtils;
import com.mywaytec.myway.utils.CRC16Util;
import com.mywaytec.myway.utils.CleanMessageUtil;
import com.mywaytec.myway.utils.ConversionUtil;
import com.mywaytec.myway.utils.DownloadFileUtil;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.SystemUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.utils.data.BleInfo;
import com.mywaytec.myway.view.CommonSubscriber;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

import static com.luck.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.luck.bluetooth.library.Constants.STATUS_DEVICE_DISCONNECTED;
import static com.luck.bluetooth.library.Constants.STATUS_DISCONNECTED;

/**
 * SC滑板车固件升级
 */
public class Sc03FirmwareUpdateActivity extends BaseActivity<Sc03FirmwareUpdatePresenter> implements Sc03FirmwareUpdateView {

    @BindView(R.id.layout_firmware_update)
    CoordinatorLayout layoutFirmwareUpdate;
    @BindView(R.id.tv_upload)
    TextView tvUpload;
    @BindView(R.id.tv_firmware_code)
    TextView tvFirmwareCode;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.tv_already_new)
    TextView tvAlreadyNew;

    int progress = 0;
    PopupWindow popupWindow;
    LinearLayout layoutIsContinue;
    LinearLayout layoutTishi;
    TextView tvTishi;
    LinearLayout layoutStartAdjust;
    ProgressBar progressBar;
    LinearLayout layoutAdjustConfirm;
    private String mDeviceAddress;
    //程序运行位置， 引导程序中：01 ， 应用程序中：00 ；
    private String programLocation;

    //
    private String firmwareCode;
    String uuid;
    /**
     * 更新状态 0：第一个从固件（就是第一个固件） 1：第二个从固件（第二个更新）  2：主固件（第三个更新）
     */
    private int updateState = 0;
    /**
     * 发送第几个包
     */
    private int sendCount = 1;
    private int sendDataEndCount = 1;
    /**
     * 发送包的类型，默认为-2
     * -1.开始固件升级
     * 0.发送起始包，128个字节
     * 1.发送数据包，每个为1024个字节
     * 2.发送数据包的最后一个包，具体大小由计算得出
     * 3.发送数据包最后一个包的最后一个小包，具体大小由计算得出
     * 4.发送EOT
     * 5.发送结束包
     */
    private int firmwareSendType = -2;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_firmware_update;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        layoutFirmwareUpdate.setPadding(0, AppUtils.getStatusBar(), 0, 0);
        //屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        uuid = PreferencesUtils.getString(Sc03FirmwareUpdateActivity.this, "uuid");
        mDeviceAddress = getIntent().getStringExtra("mDeviceAddress");
        firmwareCode = getIntent().getStringExtra("firmwareCode");
        String firmwareUpdataInfo = getIntent().getStringExtra("firmwareUpdataInfo");
        tvDescription.setText(firmwareUpdataInfo);
        programLocation = BleInfo.getBleInfo().getProgramLocation();
        hardwareCode = BleInfo.getBleInfo().getYingjianCode();
        if (TextUtils.isEmpty(hardwareCode)){
            mPresenter.getVersionInfo(hardwareCode, tvDescription);
        }
        if (null != firmwareCode) {
            tvFirmwareCode.setText(getResources().getString(R.string.current_firmware_version) + "：" + firmwareCode);
        }

        if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
            BleKitUtils.notifyP(mDeviceAddress, new BleNotifyResponse() {
                @Override
                public void onNotify(UUID service, UUID character, byte[] value) {
                    displayData(value);
                }
                @Override
                public void onResponse(int code) {
                }
            });
        } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
            BleKitUtils.notifyTaiTou(mDeviceAddress, new BleNotifyResponse() {
                @Override
                public void onNotify(UUID service, UUID character, byte[] value) {
                    displayData(value);
                }
                @Override
                public void onResponse(int code) {
                }
            });
        }

        SystemClock.sleep(120);
        if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
            BleKitUtils.writeP(mDeviceAddress, Constant.BLE.FIRMWARE_VERSION_CODE, new BleWriteResponse() {
                @Override
                public void onResponse(int code) {
                }
            });
        } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
            BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.FIRMWARE_VERSION_CODE, new BleWriteResponse() {
                @Override
                public void onResponse(int code) {
                }
            });
        }
    }

    @Override
    protected void updateViews() {

    }

    @OnClick({R.id.tv_upload})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_upload://更新
                if (getResources().getString(R.string.update_finish).equals(tvUpload.getText().toString())) {
//                    startActivity(new Intent(ScFirmwareUpdateActivity.this, MainActivity.class));
                    finish();
                } else {
                    if (SystemUtil.isNetworkConnected()) {//检查网络是否连接
                        if ("00".equals(programLocation)) {
                            update();
                        }else {
                                //TODO 引导程序中下载固件
                                BleInfoBean bleInfoBean = BleInfo.getBleInfo();
                                final String hardwareCode = bleInfoBean.getYingjianCode();
                                if (!TextUtils.isEmpty(hardwareCode) && hardwareCode.length() > 4) {
                                    String carType = hardwareCode.substring(0, 4);
                                    mPresenter.getRetrofitHelper().getFirmware(carType)
                                            .compose(RxUtil.<FirmwareInfo>rxSchedulerHelper())
                                            .subscribe(new CommonSubscriber<FirmwareInfo>() {
                                                @Override
                                                public void onNext(FirmwareInfo firmwareInfo) {
                                                    if (firmwareInfo.getCode() == 1){
                                                        if ("SC".equals(hardwareCode.substring(0, 2)) && firmwareInfo.getObj().getFiles() != null
                                                                && firmwareInfo.getObj().getFiles().size() > 1) {
                                                            String masterUrl = "";
                                                            String slave01Url = "";
                                                            String slave02Url = "";
                                                            for (int i = 0; i < firmwareInfo.getObj().getFiles().size(); i++) {
                                                                if ("master".equals(firmwareInfo.getObj().getFiles().get(i).getFVersion())) {
                                                                    masterUrl = firmwareInfo.getObj().getFiles().get(i).getUrl();
                                                                } else if ("slave01".equals(firmwareInfo.getObj().getFiles().get(i).getFVersion())) {
                                                                    slave01Url = firmwareInfo.getObj().getFiles().get(i).getUrl();
                                                                }else if ("slave02".equals(firmwareInfo.getObj().getFiles().get(i).getFVersion())){
                                                                    slave02Url = firmwareInfo.getObj().getFiles().get(i).getUrl();
                                                                }
                                                            }
                                                            downloadSCNew(masterUrl, slave01Url, slave02Url);
                                                        }
                                                    }
                                                }
                                            });
                            }
                        }
                    }else {
                        ToastUtils.showToast(R.string.请检查网络是否连接);
                    }
                }
        }
    }

    //下载SC固件
    private void downloadSCNew(final String masterUrl, final String slave01Url, final String slave02Url) {
        Log.i("TAG", "------下载固件，" + masterUrl + "," + masterUrl);
        if (TextUtils.isEmpty(masterUrl) || TextUtils.isEmpty(slave01Url)) {
            return;
        }
        //清除残留固件
        String filePath = Constant.DEFAULT_PATH+"/download/";
        File file = new File(filePath);
        if (file.exists()){
            CleanMessageUtil.deleteFile(file);
        }
        //下载固件
        DownloadFileUtil.init(this).start(masterUrl, "master.bin").isInstall(false)
                .setOnDownloadComplete(new DownloadFileUtil.OnDownloadComplete() {
                    @Override
                    public void downloadComplete(String path, File result) {
                        Log.i("TAG", "------下载固件完成，" + path);
                        DownloadFileUtil.init(Sc03FirmwareUpdateActivity.this).start(slave01Url, "slave01.bin").isInstall(false)
                                .setOnDownloadComplete(new DownloadFileUtil.OnDownloadComplete() {
                                    @Override
                                    public void downloadComplete(String path, File result) {
                                        Log.i("TAG", "------下载固件完成，" + path);
                                        if (TextUtils.isEmpty(slave02Url)){
                                            update();
                                        }else {
                                            DownloadFileUtil.init(Sc03FirmwareUpdateActivity.this).start(slave02Url, "slave02.bin").isInstall(false)
                                                    .setOnDownloadComplete(new DownloadFileUtil.OnDownloadComplete() {
                                                        @Override
                                                        public void downloadComplete(String path, File result) {
                                                            Log.i("TAG", "------下载固件SC03完成，" + path);
                                                            update();
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }
                });
    }

    //更新固件
    private void update(){
        Log.i("TAG", "------firmwareUpdate, 更新");
        showFirmwareUpdatePop(tvUpload);

        //发送固件更新确认指令
        firmwareSendType = -2;
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.i("TAG", "------发送固件确认指令");
                BleKitUtils.writeP(mDeviceAddress, Constant.BLE.FIRMWARE_UPDATE_CONFIRM, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    //硬件版本号
    String hardwareCode;
    /**
     * 是否是重发
     */
    private boolean isRESend;
    /**
     * 重发的次数
     */
    private int resend;

    //蓝牙设备返回信息
    private void displayData(byte[] data) {
        if (data != null) {
            String info = ConversionUtil.byte2HexStr(data);
            Log.i("TAG", "-----firmware" + info);
            String[] infos = info.split(" ");
//            Log.i("TAG", "-----firmware" + infos.length);
            if (infos.length > 5) {
                if ("04".equals(infos[2]) && "03".equals(infos[3]) && "03".equals(infos[4])) {//固件更新
                    Log.i("TAG", "------固件更新反馈," + infos[6]);
                    if ("00".equals(infos[6])){//车辆非静止

                    }else if ("02".equals(infos[6])){//正在充电，禁止操作
                        ToastUtils.showToast("正在充电，禁止操作");
                    }
                } else if ("04".equals(infos[2]) && "03".equals(infos[3]) && "07".equals(infos[4])) {//固件更新
                    Log.i("TAG", "------固件更新确认反馈," + infos[6]);
                    if ("01".equals(infos[6])) {//操作成功
                        firmwareSendType = -1;
                        tvUpload.setText(R.string.updating);
                    } else if ("00".equals(infos[6])) {//车辆非静止，禁止操作
                        layoutIsContinue.setVisibility(View.GONE);
                        layoutTishi.setVisibility(View.VISIBLE);
                        //TODO 车辆非静止状态提示
                    } else if ("02".equals(infos[6])) {//正在充电，禁止操作
                        ToastUtils.showToast("正在充电，禁止操作");
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
                    hardwareCode = code.toString();
                    Log.i("TAG", "------硬件版本号," + hardwareCode);
                    tvFirmwareCode.setText(getResources().getString(R.string.current_firmware_version) + "：" + hardwareCode);
                    BleInfoBean bleInfoBean = BleInfo.getBleInfo();
                    bleInfoBean.setYingjianCode(hardwareCode);
                    BleInfo.saveBleInfo(bleInfoBean);

                    mPresenter.getVersionInfo(hardwareCode, tvDescription);
                }
            }
            //设备回应
            if ("06".equals(infos[0])) {
                isRESend = false;
                resend = 0;
                if (firmwareSendType == 0) {//发送起始包
                    firmwareSendType = 1;
                    if (updateState == 0) {//发送第一个固件更新包
                        byte[] slave02Byte = getBytes(Constant.DEFAULT_PATH + "/download/slave02.bin");
                        firmwareUpdatedata(slave02Byte);
                    } else if (updateState == 1) {
                        byte[] slave01Byte = getBytes(Constant.DEFAULT_PATH + "/download/slave01.bin");
                        firmwareUpdatedata(slave01Byte);
                    }else if (updateState == 2) {
                        byte[] masterByte = getBytes(Constant.DEFAULT_PATH + "/download/master.bin");
                        firmwareUpdatedata(masterByte);
                    }
                } else if (firmwareSendType == 1) {//发送1024的数据包
                    sendCount++;
                    if (updateState == 0) {//发送第一个固件更新包
                        byte[] slave02Byte = getBytes(Constant.DEFAULT_PATH + "/download/slave02.bin");
                        firmwareUpdatedata(slave02Byte);
                    } else if (updateState == 1) {
                        byte[] slave01Byte = getBytes(Constant.DEFAULT_PATH + "/download/slave01.bin");
                        firmwareUpdatedata(slave01Byte);
                    }else if (updateState == 2) {
                        byte[] masterByte = getBytes(Constant.DEFAULT_PATH + "/download/master.bin");
                        firmwareUpdatedata(masterByte);
                    }
                } else if (firmwareSendType == 2) {//发送128的数据小包
                    sendDataEndCount++;
                    firmwareUpdateDataEnd();
                } else if (firmwareSendType == 3) {
                    firmwareSendType = 4;
                    byte[] b = new byte[]{0x04};
                    sendBleDate(b);
                    Log.i("TAG", "------发送EOT");
                } else if (firmwareSendType == 4) {//发送EOT反馈
                    firmwareSendType = 5;
                    firmwareUpdateEnd();
                    Log.i("TAG", "------发送EOT反馈");
                } else if (firmwareSendType == 5) {//数据发送完毕，更新完成
                    if (updateState == 2) {//第三次更新完成
                        layoutProgress.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        layoutAdjustConfirm.setVisibility(View.VISIBLE);
                        PreferencesUtils.putBoolean(Sc03FirmwareUpdateActivity.this, "isFirmwareUpdate", false);
                        tvUpload.setText(R.string.update_finish);
                        tvUpload.setEnabled(true);
                        tvUpload.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (null != popupWindow) {
                                    popupWindow.dismiss();
                                }
                            }
                        }, 2000);
                    } else if (updateState == 1) {//第二次更新完成
                        firmwareSendType = -2;
                        updateState = 2;
                        sendCount = 1;
                        sendDataEndCount = 1;
                        timer = new Timer();
                        timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                Log.i("TAG", "------发送固件确认指令");
                                Log.i("TAG", "------mBluetoothLeService,发送固件确认指令");
                                BleKitUtils.writeP(mDeviceAddress, Constant.BLE.FIRMWARE_UPDATE_CONFIRM, new BleWriteResponse() {
                                    @Override
                                    public void onResponse(int code) {
                                    }
                                });
                            }
                        };
                        timer.schedule(timerTask, 0, 2000);
                    } else if (updateState == 0) {//第一次更新完成
                        firmwareSendType = -2;
                        updateState = 1;
                        sendCount = 1;
                        sendDataEndCount = 1;
                        timer = new Timer();
                        timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                Log.i("TAG", "------发送固件确认指令");
                                Log.i("TAG", "------mBluetoothLeService,发送固件确认指令");
                                BleKitUtils.writeP(mDeviceAddress, Constant.BLE.FIRMWARE_UPDATE_CONFIRM, new BleWriteResponse() {
                                    @Override
                                    public void onResponse(int code) {
                                    }
                                });
                            }
                        };
                        timer.schedule(timerTask, 0, 2000);
                    }
                }
            }

            if (firmwareSendType == -2) {//进入引导程序
                boolean c = false;
                boolean d = false;
                for (int i = 0; i < infos.length; i++) {
                    if ("43".equals(infos[i])) {
                        c = true;
                    }
                    if ("44".equals(infos[i])) {
                        d = true;
                    }
                    if (c && d) {
                        firmwareSendType = -1;
                        return;
                    }
                }
            }

            boolean isC = false;
            if (!"06".equals(infos[0])) {
                for (int i = 0; i < infos.length; i++) {
                    if ("43".equals(infos[i])) {//请求数据包
                        isC = true;
                        break;
                    }
                }
            }
            if (isC) {
                if (resend >= 20) {
                    layoutProgress.setVisibility(View.GONE);
                    tvTishi.setText(R.string.update_unsuccessfully_please_restart_vehicle_to_update_firmware_again);
                    layoutTishi.setVisibility(View.VISIBLE);
                    tvUpload.setText(R.string.update);
                    return;
                }
                if (firmwareSendType == -1) {//开始发送数据
                    Log.i("TAG", "------SC开始固件更新");
                    isRESend = false;
                    if (null != timerTask) {
                        timerTask.cancel();
                        timerTask = null;
                    }
                    if (null != timer) {
                        timer.cancel();
                        timer = null;
                    }
                    if (updateState == 0) {//发送第一个固件更新包
                        Log.i("TAG", "------SC开始固件更新,发送第一个固件更新包");
                        firmwareUpdateStart();
                    } else if (updateState == 1) {//发送第二个固件更新包
                        Log.i("TAG", "------SC开始固件更新,发送第二个固件更新包,");
                        secondFirmwareUpdateStart();
                    }else if (updateState == 2) {//发送第三个固件更新包
                        Log.i("TAG", "------SC开始固件更新,发送第三个固件更新包,");
                        thirdFirmwareUpdateStart();
                    }
                } else if (firmwareSendType == 0) {
                    isRESend = true;
                    resend++;
                    if (updateState == 0) {//发送第一个固件更新包
                        Log.i("TAG", "------SC发送起始包,发送第一个固件更新包,");
                        firmwareUpdateStart();
                    } else if (updateState == 1) {//发送第二个固件更新包
                        Log.i("TAG", "------SC发送起始包,发送第二个固件更新包,");
                        secondFirmwareUpdateStart();
                    }else if (updateState == 2) {//发送第三个固件更新包
                        Log.i("TAG", "------SC开始固件更新,发送第三个固件更新包,");
                        thirdFirmwareUpdateStart();
                    }
                } else if (firmwareSendType == 1) {
                    isRESend = true;
                    resend++;
                    if (updateState == 0) {//发送第一个固件更新包
                        Log.i("TAG", "------SC发送数据包，每个为1024个字节,发送第一个固件更新包,");
                        byte[] slave02Byte = getBytes(Constant.DEFAULT_PATH + "/download/slave02.bin");
                        firmwareUpdatedata(slave02Byte);
                    } else if (updateState == 1) {
                        Log.i("TAG", "------SC发送数据包，每个为1024个字节,发送第二个固件更新包,");
                        byte[] slave01Byte = getBytes(Constant.DEFAULT_PATH + "/download/slave01.bin");
                        firmwareUpdatedata(slave01Byte);
                    }else if (updateState == 2) {
                        Log.i("TAG", "------SC发送数据包，每个为1024个字节,发送第三个固件更新包,");
                        byte[] masterByte = getBytes(Constant.DEFAULT_PATH + "/download/master.bin");
                        firmwareUpdatedata(masterByte);
                    }
                } else if (firmwareSendType == 2) {
                    Log.i("TAG", "------SC发送数据包的最后一个包");
                    isRESend = true;
                    resend++;
                    firmwareUpdateDataEnd();
                } else if (firmwareSendType == 3) {
                    Log.i("TAG", "------SC发送数据包最后一个包的最后一个小包");
                    isRESend = true;
                    resend++;
                    firmwareUpdateDataEnd();
                } else if (firmwareSendType == 4) {
                    Log.i("TAG", "------SC发送EOT");
                    isRESend = true;
                    resend++;
                    byte[] b = new byte[]{0x04};
                    sendBleDate(b);
                } else if (firmwareSendType == 5) {
                    Log.i("TAG", "------SC发送结束包");
                    isRESend = true;
                    resend++;
                    firmwareUpdateEnd();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        BleKitUtils.getBluetoothClient().registerConnectStatusListener(mDeviceAddress, mBleConnectStatusListener);
        if (BleKitUtils.getBluetoothClient().getConnectStatus(mDeviceAddress) == STATUS_DEVICE_DISCONNECTED){
            finish();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        BleKitUtils.getBluetoothClient().unregisterConnectStatusListener(mDeviceAddress, mBleConnectStatusListener);
        if (null != timerTask) {
            timerTask.cancel();
            timerTask = null;
        }
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
    }

    private final BleConnectStatusListener mBleConnectStatusListener = new BleConnectStatusListener() {

        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if (status == STATUS_CONNECTED) {
            } else if (status == STATUS_DISCONNECTED) {
                if (null != popupWindow && popupWindow.isShowing()){
                    popupWindow.dismiss();
                    popupWindow = null;
                }else {
                    SystemClock.sleep(3000);
                    finish();
                }
                finish();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferencesUtils.putBoolean(Sc03FirmwareUpdateActivity.this, "isFirmwareUpdate", false);
    }

    @Override
    public Context getContext() {
        return this;
    }

    Timer timer;
    TimerTask timerTask;
    LinearLayout layoutProgress;
    TextView tvProgress;

    /**
     * 固件更新弹框
     */
    public void showFirmwareUpdatePop(View v) {
        //防止重复按按钮
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        //设置PopupWindow的View
        final View view = LayoutInflater.from(Sc03FirmwareUpdateActivity.this).inflate(R.layout.popup_horizontal_alignment, null);
        layoutIsContinue = (LinearLayout) view.findViewById(R.id.layout_is_continue);
        layoutTishi = (LinearLayout) view.findViewById(R.id.layout_tishi);
        layoutStartAdjust = (LinearLayout) view.findViewById(R.id.layout_start_adjust);
        layoutAdjustConfirm = (LinearLayout) view.findViewById(R.id.layout_adjust_confirm);
        layoutProgress = (LinearLayout) view.findViewById(R.id.layout_progress);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        tvProgress = (TextView) view.findViewById(R.id.tv_progress);
        tvProgress.setVisibility(View.VISIBLE);
        layoutIsContinue.setVisibility(View.GONE);
        layoutStartAdjust.setVisibility(View.GONE);
        layoutTishi.setVisibility(View.GONE);
        layoutAdjustConfirm.setVisibility(View.GONE);
        layoutProgress.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        TextView tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tvStartAdjust = (TextView) view.findViewById(R.id.tv_start_adjust);
        tvStartAdjust.setText(R.string.start_update);
        tvTishi = (TextView) view.findViewById(R.id.tv_tishi);
        tvTishi.setText(R.string.firmware_cant_update_because_the_vehicle_is_not_still);
        TextView tvWancheng = (TextView) view.findViewById(R.id.tv_wancheng);
        tvWancheng.setText(R.string.update_successfully_please_restart_vehicle);
        //继续
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BleKitUtils.writeP(mDeviceAddress, Constant.BLE.FIRMWARE_UPDATE, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                    }
                });
            }
        });

        //取消
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
        //开始更新
        tvStartAdjust
        .setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 firmwareSendType = -2;
                                                 layoutStartAdjust.setVisibility(View.GONE);
                                                 layoutProgress.setVisibility(View.VISIBLE);
                                                 progressBar.setVisibility(View.VISIBLE);

                                                 timer = new Timer();
                                                 timerTask = new TimerTask() {
                                                     @Override
                                                     public void run() {
                                                         Log.i("TAG", "------发送固件确认指令");
                                                         BleKitUtils.writeP(mDeviceAddress, Constant.BLE.FIRMWARE_UPDATE_CONFIRM, new BleWriteResponse() {
                                                             @Override
                                                             public void onResponse(int code) {
                                                             }
                                                         });
                                                     }
                                                 };
                                                 timer.schedule(timerTask, 0, 1000);
                                             }
                                         }

        );
        popupWindow = new
                PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置背景,这个没什么效果，不添加会报错
        popupWindow.setBackgroundDrawable(new
                BitmapDrawable()
        );
        //设置点击弹窗外隐藏自身
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        //设置动画
        popupWindow.setAnimationStyle(R.style.PopupWindow);
        //设置位置
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 获得指定文件的byte数组
     */
    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    int totalProgress;

    //发送第一个固件升级包起始包
    public void firmwareUpdateStart() {
        firmwareSendType = 0;
        File masterFile = new File(Constant.DEFAULT_PATH + "/download/master.bin");
        File slave01File = new File(Constant.DEFAULT_PATH + "/download/slave01.bin");
        File slave02File = new File(Constant.DEFAULT_PATH + "/download/slave02.bin");
        //起始帧的数据
        byte[] masterByte = getBytes(Constant.DEFAULT_PATH + "/download/master.bin");//主固件6d61737465722e62696e
        byte[] slave01Byte = getBytes(Constant.DEFAULT_PATH + "/download/slave01.bin");//第一个从级固件736c61766530312e62696e
        byte[] slave02Byte = getBytes(Constant.DEFAULT_PATH + "/download/slave02.bin");//第二个从级固件
        int masterProgress = 133 * 2 + masterByte.length + (masterByte.length / 1024 + 1) * 5;
        int slave01Progress = 133 * 2 + slave01Byte.length + (slave01Byte.length / 1024 + 1) * 5;
        int slave02Progress = 133 * 2 + slave02Byte.length + (slave02Byte.length / 1024 + 1) * 5;
        totalProgress = masterProgress + slave01Progress + + slave02Progress;
        if (null != progressBar) {
            progressBar.setMax(totalProgress);
        }

        //先更新从级固件包
        byte[] soh = new byte[133];//SOH开头3个字节，加上128字节的数据，总共131字节
        soh[0] = 0x01;
        soh[1] = 0x00;
        soh[2] = (byte) 0xFF;
        byte[] s1 = new byte[]{0x73, 0x6c, 0x61, 0x76, 0x65, 0x30, 0x31,
                0x2e, 0x62, 0x69, 0x6e, 0x00};//SOH开头加上文件名
        String len = slave02Byte.length + "";
        char[] charArray = len.toCharArray();
        byte[] s2 = new byte[len.length() + 1];//文件大小
        for (int i = 0; i < s2.length; i++) {
            if (i == s2.length - 1) {
                s2[i] = 0x00;
            } else {
                s2[i] = (byte) charArray[i];
            }
        }
        byte[] s = new byte[128];
        System.arraycopy(s1, 0, s, 0, s1.length);
        System.arraycopy(s2, 0, s, s1.length, s2.length);
        for (int i = s1.length + s2.length; i < s.length; i++) {//SOH为128个字节，不足128字节用0x00补上
            s[i] = 0x00;
        }
        int c = CRC16Util.CRC_XModem(s);//CRC校验
        soh[131] = (byte) (c >> 8);
        soh[132] = (byte) c;

        System.arraycopy(s, 0, soh, 3, s.length);
        int count = soh.length / 20 + 1;
        //发送文件名和文件大小， 分包发送，每个包20个字节
        sendDatePackage(soh, true, "起始包数据");
    }

    //发送第二个固件升级包起始包
    public void secondFirmwareUpdateStart() {
        firmwareSendType = 0;
        //起始帧的数据
        byte[] slave01Byte = getBytes(Constant.DEFAULT_PATH + "/download/slave01.bin");//主固件6d61737465722e62696e

        //先更新从级固件包
        byte[] soh = new byte[133];//SOH开头3个字节，加上128字节的数据，总共131字节
        soh[0] = 0x01;
        soh[1] = 0x00;
        soh[2] = (byte) 0xFF;
        byte[] s1 = new byte[]{0x6d, 0x61, 0x73, 0x74, 0x65, 0x72, 0x2e,
                0x62, 0x69, 0x6e, 0x00};//SOH开头加上文件名
        String len = slave01Byte.length + "";
        char[] charArray = len.toCharArray();
        byte[] s2 = new byte[len.length() + 1];//文件大小
        for (int i = 0; i < s2.length; i++) {
            if (i == s2.length - 1) {
                s2[i] = 0x00;
            } else {
                s2[i] = (byte) charArray[i];
            }
        }
        byte[] s = new byte[128];
        System.arraycopy(s1, 0, s, 0, s1.length);
        System.arraycopy(s2, 0, s, s1.length, s2.length);
        for (int i = s1.length + s2.length; i < s.length; i++) {//SOH为128个字节，不足128字节用0x00补上
            s[i] = 0x00;
        }
        int c = CRC16Util.CRC_XModem(s);//CRC校验
        soh[131] = (byte) (c >> 8);
        soh[132] = (byte) c;

        System.arraycopy(s, 0, soh, 3, s.length);
        int count = soh.length / 20 + 1;
        //发送文件名和文件大小， 分包发送，每个包20个字节
        sendDatePackage(soh, true, "起始包数据");
    }

    //发送第三个固件升级包起始包
    public void thirdFirmwareUpdateStart() {
        firmwareSendType = 0;
        //起始帧的数据
        byte[] masterByte = getBytes(Constant.DEFAULT_PATH + "/download/master.bin");//主固件6d61737465722e62696e

        //先更新从级固件包
        byte[] soh = new byte[133];//SOH开头3个字节，加上128字节的数据，总共131字节
        soh[0] = 0x01;
        soh[1] = 0x00;
        soh[2] = (byte) 0xFF;
        byte[] s1 = new byte[]{0x6d, 0x61, 0x73, 0x74, 0x65, 0x72, 0x2e,
                0x62, 0x69, 0x6e, 0x00};//SOH开头加上文件名
        String len = masterByte.length + "";
        char[] charArray = len.toCharArray();
        byte[] s2 = new byte[len.length() + 1];//文件大小
        for (int i = 0; i < s2.length; i++) {
            if (i == s2.length - 1) {
                s2[i] = 0x00;
            } else {
                s2[i] = (byte) charArray[i];
            }
        }
        byte[] s = new byte[128];
        System.arraycopy(s1, 0, s, 0, s1.length);
        System.arraycopy(s2, 0, s, s1.length, s2.length);
        for (int i = s1.length + s2.length; i < s.length; i++) {//SOH为128个字节，不足128字节用0x00补上
            s[i] = 0x00;
        }
        int c = CRC16Util.CRC_XModem(s);//CRC校验
        soh[131] = (byte) (c >> 8);
        soh[132] = (byte) c;

        System.arraycopy(s, 0, soh, 3, s.length);
        int count = soh.length / 20 + 1;
        //发送文件名和文件大小， 分包发送，每个包20个字节
        sendDatePackage(soh, true, "起始包数据");
    }

    //数据包的最后一个包
    private byte[] stxEnd;

    //发送数据包
    private void firmwareUpdatedata(byte[] dataByte) {
        Log.i("TAG", "-----firmwareByte的大小，" + dataByte.length);
        int total = (dataByte.length / 1024) + 1;
        if (sendCount <= total) {
            if (sendCount == total) {//数据包的最后一个包
                Log.i("TAG", "-----文件数据最后一个包入口" + sendCount);
                firmwareSendType = 2;
                int stxEndCount = dataByte.length - (sendCount - 1) * 1024;
                stxEnd = new byte[stxEndCount];//最后一个包
                System.arraycopy(dataByte, (sendCount - 1) * 1024, stxEnd, 0, stxEnd.length);
                firmwareUpdateDataEnd();
            } else {
                byte[] stx = new byte[1029];//整包数据1029个字节，
                stx[0] = 0x02;
                stx[1] = (byte) Integer.parseInt(Integer.toHexString(sendCount), 16);
                stx[2] = (byte) Integer.parseInt(Integer.toHexString(255 - sendCount), 16);
                byte[] stxData = new byte[1024];//每包数据1024个字节，
                System.arraycopy(dataByte, (sendCount - 1) * 1024, stxData, 0, stxData.length);
                int c = CRC16Util.CRC_XModem(stxData);//CRC校验
                stx[1027] = (byte) (c >> 8);
                stx[1028] = (byte) c;

                System.arraycopy(stxData, 0, stx, 3, stxData.length);
                int stxcount = (stx.length / 20) + 1;
                //发送文件名和文件大小， 分包发送，每个包20个字节
                sendDatePackage(stx, true, "文件数据" + sendCount + ",");
            }
        }
    }

    //主体部分最后一个数据包
    private void firmwareUpdateDataEnd() {
        Log.i("TAG", "-----最后一个包数据");
        if (null != stxEnd) {
            int total = (stxEnd.length / 128) + 1;
            if (sendDataEndCount <= total) {
                if (sendDataEndCount == total) {
                    Log.i("TAG", "-----最后一个包数据的最后一个包");
                    firmwareSendType = 3;
                    int stxEndCount = stxEnd.length - (sendDataEndCount - 1) * 128;
                    byte[] data = new byte[133];//整包数据133个字节，
                    data[0] = 0x01;
                    data[1] = (byte) Integer.parseInt(Integer.toHexString(sendCount + sendDataEndCount - 1), 16);
                    data[2] = (byte) Integer.parseInt(Integer.toHexString(255 - (sendCount + sendDataEndCount - 1)), 16);
                    byte[] dataend = new byte[128];//整包数据，
                    System.arraycopy(stxEnd, (sendDataEndCount - 1) * 128, dataend, 0, stxEndCount);
                    for (int i = stxEndCount; i < 128; i++) {
                        dataend[i] = 0x1A;
                    }
                    int c = CRC16Util.CRC_XModem(dataend);//CRC校验
                    data[131] = (byte) (c >> 8);
                    data[132] = (byte) c;

                    System.arraycopy(dataend, 0, data, 3, dataend.length);
                    int stxcount = data.length / 20 + 1;
                    //发送文件名和文件大小， 分包发送，每个包20个字节
                    sendDatePackage(data, true, "最后一个包数据" + (sendCount + sendDataEndCount - 1) + ",");
                } else {
                    Log.i("TAG", "-----最后一个包数据sendDataEndCount," + sendDataEndCount);
                    byte[] data = new byte[133];//整包数据133个字节，
                    data[0] = 0x01;
                    data[1] = (byte) Integer.parseInt(Integer.toHexString(sendCount + sendDataEndCount - 1), 16);
                    data[2] = (byte) Integer.parseInt(Integer.toHexString(255 - (sendCount + sendDataEndCount - 1)), 16);
                    byte[] dataend = new byte[128];//整包数据128个字节，
                    System.arraycopy(stxEnd, (sendDataEndCount - 1) * 128, dataend, 0, dataend.length);
                    int c = CRC16Util.CRC_XModem(dataend);//CRC校验
                    data[131] = (byte) (c >> 8);
                    data[132] = (byte) c;
                    System.arraycopy(dataend, 0, data, 3, dataend.length);

                    int stxcount = data.length / 20 + 1;
                    //发送文件名和文件大小， 分包发送，每个包20个字节
                    sendDatePackage(data, true, "最后一个包数据" + (sendCount + sendDataEndCount - 1) + ",");
                }
            }
        }
    }

    private void firmwareUpdateEnd() {
        //结束帧数据,所有数据全部用0x00填充
        byte[] soh2 = new byte[133];//SOH开头3个字节，加上128字节的数据，总共131字节
        soh2[0] = 0x01;
        soh2[1] = 0x00;
        soh2[2] = (byte) 0xFF;
        byte[] soh2data = new byte[128];
        for (int i = 0; i < soh2data.length; i++) {//SOH为128个字节，所有数据全部用0x00填充
            soh2data[i] = 0x00;
        }
        int c = CRC16Util.CRC_XModem(soh2data);//CRC校验
        soh2[131] = (byte) (c >> 8);
        soh2[132] = (byte) c;
        System.arraycopy(soh2data, 0, soh2, 3, soh2data.length);
        int count2 = soh2.length / 20 + 1;
        //发送文件名和文件大小， 分包发送，每个包20个字节
        sendDatePackage(soh2, true, "结束包数据" + ",");
    }

    //发送数据
    private void sendBleDate(byte[] date) {
//        SystemClock.sleep(30);
        BleKitUtils.writeP(mDeviceAddress, date, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
            }
        }, 0);
    }

    private int flag;

    /**
     *
     * @param data 发送数据
     * @param isReset 是否重置（是否发送的是新的数据包）
     * @param ceshi Log文字
     */
    private void sendDatePackage(final byte[] data, boolean isReset, final String ceshi) {
        if (isReset) {
            flag = 0;
        }

        int count = data.length / 20 + 1;
        byte[] b = new byte[20];
        if (flag == count - 1) {
            b = new byte[data.length - (count - 1) * 20];
        }
        System.arraycopy(data, 20 * flag, b, 0, b.length);
        String info = ConversionUtil.byte2HexStr(b);
        Log.i("TAG", "-----" + ceshi + info);
        if (!isRESend) {
            if (null != progressBar) {
                progress += b.length;
                progressBar.setProgress(progress);
                tvProgress.setText((progressBar.getProgress() * 100) / progressBar.getMax() + "%");
            }
        }
        Log.i("TAG", "------firmware,progress:" + progress);
        BleKitUtils.writeNoRspP(mDeviceAddress, b, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
//                if (code == REQUEST_SUCCESS) {
////                            Log.i("TAG", "------flag," + flag);
//                    if (20 * (flag + 1) >= data.length) {
//                        return;
//                    }
//                    flag++;
//                    sendDatePackage(data, false, ceshi);
//                } else if (code == REQUEST_FAILED) {
//                    if (20 * (flag + 1) >= data.length) {
//                        return;
//                    }
//                    if (BleKitUtils.getBluetoothClient().getConnectStatus(mDeviceAddress) == STATUS_DEVICE_CONNECTED) {
//                        sendDatePackage(data, false, ceshi);
//                    }
//                } else if (code == REQUEST_CANCELED) {
//
//                }
            }
        });

        if (20 * (flag + 1) >= data.length) {
                    return;
        }
        flag++;
//        SystemClock.sleep(10);
        sendDatePackage(data, false, ceshi);
    }
}
