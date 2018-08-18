package com.mywaytec.myway.ui.firmwareUpdate.raFirmwareUpdate;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.SystemClock;
import android.support.design.widget.CoordinatorLayout;
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
import com.mywaytec.myway.ui.main.MainActivity;
import com.mywaytec.myway.utils.AppUtils;
import com.mywaytec.myway.utils.BleKitUtils;
import com.mywaytec.myway.utils.CRC16Util;
import com.mywaytec.myway.utils.ConversionUtil;
import com.mywaytec.myway.utils.PreferencesUtils;

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

import static com.luck.bluetooth.library.Constants.REQUEST_CANCELED;
import static com.luck.bluetooth.library.Constants.REQUEST_FAILED;
import static com.luck.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.luck.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.luck.bluetooth.library.Constants.STATUS_DISCONNECTED;

/**
 * 固件升级
 */
public class RAFirmwareUpdateActivity extends BaseActivity<RAFirmwareUpdatePresenter> implements RAFirmwareUpdateView {

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
    String uuid;

    private String mDeviceAddress;

    //
    private String firmwareCode;
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

        mDeviceAddress = getIntent().getStringExtra("mDeviceAddress");
        uuid = PreferencesUtils.getString(RAFirmwareUpdateActivity.this, "uuid");

        firmwareCode = getIntent().getStringExtra("firmwareCode");
        String firmwareUpdataInfo = getIntent().getStringExtra("firmwareUpdataInfo");
        tvDescription.setText(firmwareUpdataInfo);
        if (null != firmwareCode)
            tvFirmwareCode.setText(getResources().getString(R.string.current_firmware_version)+"：" + firmwareCode);

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


        String uuid = PreferencesUtils.getString(RAFirmwareUpdateActivity.this, "uuid");
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
                if (getResources().getString(R.string.update_finish).equals(tvUpload.getText().toString())){
                    PreferencesUtils.putBoolean(RAFirmwareUpdateActivity.this, "isFirmwareUpdate", false);
                    startActivity(new Intent(RAFirmwareUpdateActivity.this, MainActivity.class));
                    finish();
                }else if (getResources().getString(R.string.update).equals(tvUpload.getText().toString())){
                    PreferencesUtils.putBoolean(RAFirmwareUpdateActivity.this, "isFirmwareUpdate", true);
                    Log.i("TAG", "------firmwareUpdate, 更新");

                    showFirmwareUpdatePop(tvUpload);

                    //发送固件更新确认指令
                    firmwareSendType = -2;
                    timer = new Timer();
                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                                Log.i("TAG", "------发送固件确认指令");
                                if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                                    BleKitUtils.writeP(mDeviceAddress, Constant.BLE.FIRMWARE_UPDATE_CONFIRM, new BleWriteResponse() {
                                        @Override
                                        public void onResponse(int code) {

                                        }
                                    });
                                } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                                    BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.FIRMWARE_UPDATE_CONFIRM, new BleWriteResponse() {
                                        @Override
                                        public void onResponse(int code) {

                                        }
                                    });
                                }
                        }
                    };
                    timer.schedule(timerTask, 0, 1000);
                }
        }
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
            Log.i("TAG", "-----firmware" + infos.length);
            if (infos.length > 5) {
                if ("04".equals(infos[2]) && "03".equals(infos[3]) && "03".equals(infos[4])) {//固件更新
                    Log.i("TAG", "------固件更新反馈," + infos[6]);
                    if ("01".equals(infos[6])) {//操作成功
                        layoutIsContinue.setVisibility(View.GONE);
                        layoutStartAdjust.setVisibility(View.VISIBLE);
                    } else if ("00".equals(infos[6])) {//操作失败
                        layoutIsContinue.setVisibility(View.GONE);
                        layoutTishi.setVisibility(View.VISIBLE);
                    }
                } else if ("04".equals(infos[2]) && "03".equals(infos[3]) && "07".equals(infos[4])) {//固件更新
                    Log.i("TAG", "------固件更新确认反馈," + infos[6]);
                    if ("01".equals(infos[6])) {//操作成功
                        firmwareSendType = -1;
                        tvUpload.setText(R.string.updating);
                    } else if ("00".equals(infos[6])) {//操作失败
                        layoutIsContinue.setVisibility(View.GONE);
                        layoutTishi.setVisibility(View.VISIBLE);
                    }
                } else if ("04".equals(infos[2]) && "01".equals(infos[3]) && "06".equals(infos[4])) {//固件版本号
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
                    Log.i("TAG", "-----硬件版本号," + hardwareCode);
                }
            }
            if ("06".equals(infos[0])) {
                isRESend = false;
                resend = 0;
                if (firmwareSendType == 0) {//发送起始包
                    firmwareSendType = 1;
                    firmwareUpdatedata();
                } else if (firmwareSendType == 1) {//发送1024的数据包
                    sendCount++;
                    firmwareUpdatedata();
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
                } else if (firmwareSendType == 5) {//数据发送完毕，更新完成,从级包发送完毕再发送主包
                    layoutProgress.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    layoutAdjustConfirm.setVisibility(View.VISIBLE);
                    PreferencesUtils.putBoolean(RAFirmwareUpdateActivity.this, "isFirmwareUpdate", false);
                    tvUpload.setText(R.string.update_finish);
                    tvUpload.setEnabled(true);
                    tvUpload.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (null != popupWindow){
                                popupWindow.dismiss();
                            }
                        }
                    }, 2000);
                }
            }

            if (firmwareSendType == -2){//进入引导程序
                boolean c = false;
                boolean d = false;
                for (int i = 0; i < infos.length; i++) {
                    if ("43".equals(infos[i])){
                        c = true;
                    }
                    if ("44".equals(infos[i])){
                        d = true;
                    }
                    if (c&&d){
                        firmwareSendType = -1;
                        return;
                    }
                }
            }

            if (!"06".equals(infos[0])) {
//                for (int i = 0; i < infos.length; i++) {
                    if ("43".equals(infos[0])) {
                        if (resend >= 20) {
                            layoutProgress.setVisibility(View.GONE);
                            tvTishi.setText(R.string.update_unsuccessfully_please_restart_vehicle_to_update_firmware_again);
                            layoutTishi.setVisibility(View.VISIBLE);
                            return;
                        }
                        if (firmwareSendType == -1) {//开始发送数据
                            Log.i("TAG", "------开始固件更新");
                            isRESend = false;
                            if (null != timerTask) {
                                timerTask.cancel();
                                timerTask = null;
                            }
                            if (null != timer) {
                                timer.cancel();
                                timer = null;
                            }
                            firmwareUpdateStart();
                        } else if (firmwareSendType == 0) {//发送起始包，128个字节
                            isRESend = true;
                            resend++;
                            firmwareUpdateStart();
                        } else if (firmwareSendType == 1) {//发送数据包，每个为1024个字节
                            isRESend = true;
                            resend++;
                            firmwareUpdatedata();
                        } else if (firmwareSendType == 2) {//发送数据包的最后一个包，具体大小由计算得出
                            isRESend = true;
                            resend++;
                            firmwareUpdateDataEnd();
                        } else if (firmwareSendType == 3) {//发送数据包最后一个包的最后一个小包，具体大小由计算得出
                            isRESend = true;
                            resend++;
                            firmwareUpdateDataEnd();
                        } else if (firmwareSendType == 4) {//发送EOT
                            isRESend = true;
                            resend++;
                            byte[] b = new byte[]{0x04};
                            sendBleDate(b);
                        } else if (firmwareSendType == 5) {//发送结束包
                            isRESend = true;
                            resend++;
                            firmwareUpdateEnd();
                        }
//                        break;
                    }
                }
            }
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        BleKitUtils.getBluetoothClient().registerConnectStatusListener(mDeviceAddress, mBleConnectStatusListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        BleKitUtils.getBluetoothClient().unregisterConnectStatusListener(mDeviceAddress, mBleConnectStatusListener);
    }

    private final BleConnectStatusListener mBleConnectStatusListener = new BleConnectStatusListener() {

        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if (status == STATUS_CONNECTED) {
            } else if (status == STATUS_DISCONNECTED) {
                if (popupWindow.isShowing()){
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                finish();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferencesUtils.putBoolean(RAFirmwareUpdateActivity.this, "isFirmwareUpdate", false);
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
        final View view = LayoutInflater.from(RAFirmwareUpdateActivity.this).inflate(R.layout.popup_horizontal_alignment, null);
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
                BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.FIRMWARE_UPDATE, new BleWriteResponse() {
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
        tvStartAdjust.setOnClickListener(new View.OnClickListener() {
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
                                                             String uuid = PreferencesUtils.getString(RAFirmwareUpdateActivity.this, "uuid");
                                                         BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.FIRMWARE_UPDATE_CONFIRM, new BleWriteResponse() {
                                                             @Override
                                                             public void onResponse(int code) {

                                                             }
                                                         });
                                                     }
                                                 };
                                                 timer.schedule(timerTask, 0, 125);
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
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置动画
        popupWindow.setAnimationStyle(R.style.PopupWindow);
        //设置位置
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }

    int totalProgress;
    //发送固件升级包起始包
    public void firmwareUpdateStart() {
        firmwareSendType = 0;
        File file = new File(Constant.DEFAULT_PATH + "/download/firmware.bin");
        //起始帧的数据
        byte[] firmwareByte = getBytes(Constant.DEFAULT_PATH + "/download/firmware.bin");
        totalProgress = 133 * 2 + firmwareByte.length + (firmwareByte.length / 1024 + 1) * 5;
        if (null != progressBar) {
            progressBar.setMax(totalProgress);
        }

        byte[] soh = new byte[133];//SOH开头3个字节，加上128字节的数据，总共131字节
        soh[0] = 0x01;
        soh[1] = 0x00;
        soh[2] = (byte) 0xFF;
        byte[] s1 = new byte[]{0x66, 0x69, 0x72, 0x6d, 0x77, 0x61, 0x72,
                0x65, 0x2e, 0x62, 0x69, 0x6e, 0x00};//SOH开头加上文件名
        String len = firmwareByte.length + "";
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
    private void firmwareUpdatedata() {
        //
        byte[] firmwareByte = getBytes(Constant.DEFAULT_PATH + "/download/firmware.bin");
        Log.i("TAG", "-----firmwareByte的大小，" + firmwareByte.length);
        int total = firmwareByte.length / 1024 + 1;
        if (sendCount <= total) {
            if (sendCount == total) {//数据包的最后一个包
                Log.i("TAG", "-----文件数据最后一个包入口" + sendCount);
                firmwareSendType = 2;
                int stxEndCount = firmwareByte.length - (sendCount - 1) * 1024;
                stxEnd = new byte[stxEndCount];//最后一个包
                System.arraycopy(firmwareByte, (sendCount - 1) * 1024, stxEnd, 0, stxEnd.length);
                firmwareUpdateDataEnd();
            } else {
                byte[] stx = new byte[1029];//整包数据1029个字节，
                stx[0] = 0x02;
                stx[1] = (byte) Integer.parseInt(Integer.toHexString(sendCount), 16);
                stx[2] = (byte) Integer.parseInt(Integer.toHexString(255 - sendCount), 16);
                byte[] stxData = new byte[1024];//每包数据1024个字节，
                System.arraycopy(firmwareByte, (sendCount - 1) * 1024, stxData, 0, stxData.length);
                int c = CRC16Util.CRC_XModem(stxData);//CRC校验
                stx[1027] = (byte) (c >> 8);
                stx[1028] = (byte) c;

                System.arraycopy(stxData, 0, stx, 3, stxData.length);
                int stxcount = stx.length / 20 + 1;
                //发送文件名和文件大小， 分包发送，每个包20个字节
                sendDatePackage(stx, true, "文件数据"+sendCount + ",");
            }
        }
    }

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
                    sendDatePackage(data, true, "最后一个包数据"+(sendCount + sendDataEndCount - 1) + ",");
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
                    sendDatePackage(data, true, "最后一个包数据"+(sendCount + sendDataEndCount - 1) + ",");
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
        SystemClock.sleep(80);
        String uuid = PreferencesUtils.getString(RAFirmwareUpdateActivity.this, "uuid");
            if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                BleKitUtils.writeP(mDeviceAddress, date, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {

                    }
                });
            } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                BleKitUtils.writeTaiTou(mDeviceAddress, date, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {

                    }
                });
            }
    }

    private int flag;
    private void sendDatePackage(final byte[] data, boolean isReset, final String ceshi){
        if (isReset){
            flag = 0;
        }
//        SystemClock.sleep(30);

        int count = data.length / 20 + 1;
        byte[] b = new byte[20];
        if (flag == count - 1) {
            b = new byte[data.length - (count - 1) * 20];
        }
        System.arraycopy(data, 20 * flag, b, 0, b.length);
        String info = ConversionUtil.byte2HexStr(b);
        Log.i("TAG", "-----"+ceshi + info);
        if (!isRESend) {
            if (null != progressBar) {
                progress += b.length;
                progressBar.setProgress(progress);
                tvProgress.setText((progressBar.getProgress() * 100) / progressBar.getMax() + "%");
            }
        }
        Log.i("TAG", "------firmware,progress:" + progress);
        BleKitUtils.writeNoRspTaiTou(mDeviceAddress, b, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        if (code == REQUEST_SUCCESS){
//                            Log.i("TAG", "------flag," + flag);
                            if (20 * (flag+1) >= data.length){
                                return;
                            }
                            flag++;
                            sendDatePackage(data, false, ceshi);
                        }else if(code == REQUEST_FAILED){
                            if (20 * (flag+1) >= data.length){
                                return;
                            }
                            sendDatePackage(data, false, ceshi);
                        }else if(code == REQUEST_CANCELED){

                        }
                    }
                });
    }
}
