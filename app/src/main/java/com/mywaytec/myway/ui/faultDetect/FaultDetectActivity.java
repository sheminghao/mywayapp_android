package com.mywaytec.myway.ui.faultDetect;

import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.base.Constant;
import com.mywaytec.myway.utils.BleKitUtils;
import com.mywaytec.myway.utils.BleUtil;
import com.mywaytec.myway.utils.ConversionUtil;
import com.mywaytec.myway.utils.PreferencesUtils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DISCONNECTED;

/**
 * 故障检测
 */
public class FaultDetectActivity extends BaseActivity<FaultDetectPresenter> implements FaultDetectView {

    @BindView(R.id.layout_fault_detect)
    CoordinatorLayout layoutFaultDetect;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_jiance)
    TextView tvJiance;
    @BindView(R.id.layout_detection)
    LinearLayout layoutDetection;
    @BindView(R.id.img_right_1)
    ImageView imgRight1;
    @BindView(R.id.empty_loading_1)
    SpinKitView emptyLoading1;
    @BindView(R.id.img_right_2)
    ImageView imgRight2;
    @BindView(R.id.empty_loading_2)
    SpinKitView emptyLoading2;
    @BindView(R.id.img_right_3)
    ImageView imgRight3;
    @BindView(R.id.empty_loading_3)
    SpinKitView emptyLoading3;
    @BindView(R.id.img_right_4)
    ImageView imgRight4;
    @BindView(R.id.empty_loading_4)
    SpinKitView emptyLoading4;
    @BindView(R.id.img_right_5)
    ImageView imgRight5;
    @BindView(R.id.empty_loading_5)
    SpinKitView emptyLoading5;
    @BindView(R.id.img_right_6)
    ImageView imgRight6;
    @BindView(R.id.empty_loading_6)
    SpinKitView emptyLoading6;
    @BindView(R.id.img_right_7)
    ImageView imgRight7;
    @BindView(R.id.empty_loading_7)
    SpinKitView emptyLoading7;
    @BindView(R.id.img_right_8)
    ImageView imgRight8;
    @BindView(R.id.empty_loading_8)
    SpinKitView emptyLoading8;
    @BindView(R.id.img_right_9)
    ImageView imgRight9;
    @BindView(R.id.empty_loading_9)
    SpinKitView emptyLoading9;
    @BindView(R.id.img_right_10)
    ImageView imgRight10;
    @BindView(R.id.empty_loading_10)
    SpinKitView emptyLoading10;
    @BindView(R.id.img_right_11)
    ImageView imgRight11;
    @BindView(R.id.empty_loading_11)
    SpinKitView emptyLoading11;
    @BindView(R.id.img_right_12)
    ImageView imgRight12;
    @BindView(R.id.empty_loading_12)
    SpinKitView emptyLoading12;
    @BindView(R.id.layout_finger_mark)
    LinearLayout layoutFingerWark;
    @BindView(R.id.layout_gps)
    LinearLayout layoutGps;
    @BindView(R.id.layout_ra)
    LinearLayout layoutRa;
    @BindView(R.id.layout_sc)
    LinearLayout layoutSc;


    private boolean mConnected = false;
    private String mDeviceAddress;
    private String firmwareCode;//软件版本号
    String uuid;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_fault_detect;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        // 透明状态栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        layoutFaultDetect.setPadding(0, AppUtils.getStatusBar(), 0, 0);
        tvTitle.setText(R.string.fault_diagnose);
        mDeviceAddress = getIntent().getStringExtra("mDeviceAddress");

        uuid = PreferencesUtils.getString(FaultDetectActivity.this, "uuid");
        if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
            layoutSc.setVisibility(View.VISIBLE);
            layoutRa.setVisibility(View.GONE);
        } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
            layoutSc.setVisibility(View.GONE);
            layoutRa.setVisibility(View.VISIBLE);
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
    }

    @Override
    protected void updateViews() {

    }

    private int huoer;
    private int aXiang;
    private int bXiang;
    private int cXiang;
    private int zongxian;
    private int youmen;
    private int tongxin;
    private int zhiwen;
    private int gps;
    private int gprs;
    private int mosfet;
    private int mpu6050;

    //蓝牙设备返回信息
    private void displayData(byte[] data) {
        if (data != null) {
            if (!BleUtil.receiveDataCRCCheck(data)) {
                return;
            }
            String info = ConversionUtil.byte2HexStr(data);
            Log.i("TAG", "-----FaultDetect_info" + info);
            String[] infos = info.split(" ");
            if (infos.length > 5) {
                if ("04".equals(infos[2]) && "01".equals(infos[3]) && "02".equals(infos[4])) {//前灯关闭应答
                    huoer = (data[8] >> 3) & 0x1;
                    aXiang = (data[8] >> 4) & 0x1;
                    bXiang = (data[8] >> 5) & 0x1;
                    cXiang = (data[8] >> 6) & 0x1;
                    zongxian = (data[8] >> 7) & 0x1;
                    youmen = (data[7] >> 0) & 0x1;
                    tongxin = ((data[7] >> 1) & 0x1);
                    zhiwen = ((data[7] >> 2) & 0x1);
                    gps = ((data[7] >> 3) & 0x1);
                    gprs = ((data[7] >> 4) & 0x1);
                    mpu6050 = (data[7] >> 0) & 0x1;
                    mosfet = (data[7] >> 1) & 0x1;

                    Log.i("TAG", "------" + huoer + aXiang + bXiang + cXiang + zongxian + youmen
                            + mosfet + tongxin + zhiwen + gps);

                    if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                        percentage(10);
                    } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                        percentage(7);
                    }
                    load1();
                }
            }
        }
    }

    @OnClick({R.id.layout_detection})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_detection://检测
                if (getResources().getString(R.string.diagnose).equals(tvJiance.getText().toString())) {
                    imgRight1.setVisibility(View.GONE);
                    imgRight2.setVisibility(View.GONE);
                    imgRight3.setVisibility(View.GONE);
                    imgRight4.setVisibility(View.GONE);
                    imgRight5.setVisibility(View.GONE);
                    imgRight6.setVisibility(View.GONE);
                    imgRight7.setVisibility(View.GONE);
                    imgRight8.setVisibility(View.GONE);
                    imgRight9.setVisibility(View.GONE);
                    imgRight10.setVisibility(View.GONE);
                    imgRight11.setVisibility(View.GONE);
                    imgRight12.setVisibility(View.GONE);
                    uuid = PreferencesUtils.getString(FaultDetectActivity.this, "uuid");
                    if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                        BleKitUtils.writeP(mDeviceAddress, Constant.BLE.CAR_STATE, new BleWriteResponse() {
                            @Override
                            public void onResponse(int code) {

                            }
                        });
                    } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                        BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.CAR_STATE, new BleWriteResponse() {
                            @Override
                            public void onResponse(int code) {

                            }
                        });
                    }
                }
                break;
        }
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
                finish();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void load1() {
        emptyLoading1.setVisibility(View.VISIBLE);
        imgRight1.postDelayed(new Runnable() {
            @Override
            public void run() {
                emptyLoading1.setVisibility(View.GONE);
                imgRight1.setVisibility(View.VISIBLE);
                if (huoer == 0) {
                    imgRight1.setImageResource(R.mipmap.guzhangjiance_right);
                } else {
                    imgRight1.setImageResource(R.mipmap.guzhangjiance_wrong);
                }
                load2();
            }
        }, 1000);
    }

    private void load2() {
        emptyLoading2.setVisibility(View.VISIBLE);
        imgRight2.postDelayed(new Runnable() {
            @Override
            public void run() {
                emptyLoading2.setVisibility(View.GONE);
                imgRight2.setVisibility(View.VISIBLE);
                if (aXiang == 0) {
                    imgRight2.setImageResource(R.mipmap.guzhangjiance_right);
                } else {
                    imgRight2.setImageResource(R.mipmap.guzhangjiance_wrong);
                }
                load3();
            }
        }, 1000);
    }

    private void load3() {
        emptyLoading3.setVisibility(View.VISIBLE);
        imgRight3.postDelayed(new Runnable() {
            @Override
            public void run() {
                emptyLoading3.setVisibility(View.GONE);
                imgRight3.setVisibility(View.VISIBLE);
                if (bXiang == 0) {
                    imgRight3.setImageResource(R.mipmap.guzhangjiance_right);
                } else {
                    imgRight3.setImageResource(R.mipmap.guzhangjiance_wrong);
                }
                load4();
            }
        }, 1000);
    }

    private void load4() {
        emptyLoading4.setVisibility(View.VISIBLE);
        imgRight4.postDelayed(new Runnable() {
            @Override
            public void run() {
                emptyLoading4.setVisibility(View.GONE);
                imgRight4.setVisibility(View.VISIBLE);
                if (cXiang == 0) {
                    imgRight4.setImageResource(R.mipmap.guzhangjiance_right);
                } else {
                    imgRight4.setImageResource(R.mipmap.guzhangjiance_wrong);
                }
                load5();
            }
        }, 1000);
    }

    private void load5() {
        emptyLoading5.setVisibility(View.VISIBLE);
        imgRight5.postDelayed(new Runnable() {
            @Override
            public void run() {
                emptyLoading5.setVisibility(View.GONE);
                imgRight5.setVisibility(View.VISIBLE);
                if (zongxian == 0) {
                    imgRight5.setImageResource(R.mipmap.guzhangjiance_right);
                } else {
                    imgRight5.setImageResource(R.mipmap.guzhangjiance_wrong);
                }
                if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                    load6();
                } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                    load11();
                }
            }
        }, 1000);
    }

    private void load6() {
        emptyLoading6.setVisibility(View.VISIBLE);
        imgRight6.postDelayed(new Runnable() {
            @Override
            public void run() {
                emptyLoading6.setVisibility(View.GONE);
                imgRight6.setVisibility(View.VISIBLE);
                if (youmen == 0) {
                    imgRight6.setImageResource(R.mipmap.guzhangjiance_right);
                } else {
                    imgRight6.setImageResource(R.mipmap.guzhangjiance_wrong);
                }
                load7();
            }
        }, 1000);
    }

    private void load7() {
        emptyLoading7.setVisibility(View.VISIBLE);
        imgRight7.postDelayed(new Runnable() {
            @Override
            public void run() {
                emptyLoading7.setVisibility(View.GONE);
                imgRight7.setVisibility(View.VISIBLE);
                if (tongxin == 0) {
                    imgRight7.setImageResource(R.mipmap.guzhangjiance_right);
                } else {
                    imgRight7.setImageResource(R.mipmap.guzhangjiance_wrong);
                }
                load8();
            }
        }, 1000);
    }

    private void load8() {
        emptyLoading8.setVisibility(View.VISIBLE);
        imgRight8.postDelayed(new Runnable() {
            @Override
            public void run() {
                emptyLoading8.setVisibility(View.GONE);
                imgRight8.setVisibility(View.VISIBLE);
                if (zhiwen == 0) {
                    imgRight8.setImageResource(R.mipmap.guzhangjiance_right);
                } else {
                    imgRight8.setImageResource(R.mipmap.guzhangjiance_wrong);
                }
                load9();
            }
        }, 1000);
    }

    private void load9() {
        emptyLoading9.setVisibility(View.VISIBLE);
        imgRight9.postDelayed(new Runnable() {
            @Override
            public void run() {
                emptyLoading9.setVisibility(View.GONE);
                imgRight9.setVisibility(View.VISIBLE);
                if (gps == 0) {
                    imgRight9.setImageResource(R.mipmap.guzhangjiance_right);
                } else {
                    imgRight9.setImageResource(R.mipmap.guzhangjiance_wrong);
                }
                load10();
            }
        }, 1000);
    }

    private void load10() {
        emptyLoading10.setVisibility(View.VISIBLE);
        imgRight10.postDelayed(new Runnable() {
            @Override
            public void run() {
                emptyLoading10.setVisibility(View.GONE);
                imgRight10.setVisibility(View.VISIBLE);
                if (gprs == 0) {
                    imgRight10.setImageResource(R.mipmap.guzhangjiance_right);
                } else {
                    imgRight10.setImageResource(R.mipmap.guzhangjiance_wrong);
                }
            }
        }, 1000);
    }

    private void load11() {
        emptyLoading11.setVisibility(View.VISIBLE);
        imgRight11.postDelayed(new Runnable() {
            @Override
            public void run() {
                emptyLoading11.setVisibility(View.GONE);
                imgRight11.setVisibility(View.VISIBLE);
                if (mpu6050 == 0) {
                    imgRight11.setImageResource(R.mipmap.guzhangjiance_right);
                } else {
                    imgRight11.setImageResource(R.mipmap.guzhangjiance_wrong);
                }
                load12();
            }
        }, 1000);
    }

    private void load12() {
        emptyLoading12.setVisibility(View.VISIBLE);
        imgRight12.postDelayed(new Runnable() {
            @Override
            public void run() {
                emptyLoading12.setVisibility(View.GONE);
                imgRight12.setVisibility(View.VISIBLE);
                if (mosfet == 0) {
                    imgRight12.setImageResource(R.mipmap.guzhangjiance_right);
                } else {
                    imgRight12.setImageResource(R.mipmap.guzhangjiance_wrong);
                }
            }
        }, 1000);
    }

    int count;
    Timer timer;
    TimerTask timerTask;

    //进度
    private void percentage(int jianceNum) {
        count = 0;
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                count++;
                tvJiance.post(new Runnable() {
                    @Override
                    public void run() {
                        tvJiance.setText(count + "%");
                    }
                });
                if (count == 100) {
                    tvJiance.post(new Runnable() {
                        @Override
                        public void run() {
                            tvJiance.setText(R.string.finish);
                        }
                    });
                    timerTask.cancel();
                    timer.cancel();
                    timerTask = null;
                    timer = null;
                }
            }
        };
        timer.schedule(timerTask, 0, (1000 * jianceNum) / 100);
    }
}
