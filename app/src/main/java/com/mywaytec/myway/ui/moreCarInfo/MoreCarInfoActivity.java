package com.mywaytec.myway.ui.moreCarInfo;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.base.BluetoothLeService;
import com.mywaytec.myway.base.Constant;
import com.mywaytec.myway.model.bean.FirmwareInfo;
import com.mywaytec.myway.ui.faultDetect.FaultDetectActivity;
import com.mywaytec.myway.ui.fingerMark.FingerMarkActivity;
import com.mywaytec.myway.ui.firmwareUpdate.FirmwareUpdateActivity;
import com.mywaytec.myway.ui.scFirmwareUpdate.ScFirmwareUpdateActivity;
import com.mywaytec.myway.utils.BleUtil;
import com.mywaytec.myway.utils.ConversionUtil;
import com.mywaytec.myway.utils.DownloadFileUtil;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxCountDown;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CommonSubscriber;
import com.mywaytec.myway.view.SpeedSeekBar;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * 蓝牙更多设置界面
 */
public class MoreCarInfoActivity extends BaseActivity<MoreCarInfoPresenter> implements MoreCarInfoView,
        CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.cb_qianteng)
    CheckBox cbQiandeng;
    @BindView(R.id.cb_houdeng)
    CheckBox cbHoudeng;
    @BindView(R.id.cb_huaxing)
    CheckBox cbHuaxing;
    @BindView(R.id.cb_dingsuxunhang)
    CheckBox cbDingsuxunhang;
    @BindView(R.id.speedSeekBar)
    SpeedSeekBar speedSeekBar;
    @BindView(R.id.tv_firmware_code)
    TextView tvFirmwareCode;
    @BindView(R.id.tv_mode)
    TextView tvMode;
    @BindView(R.id.layout_finger_mark)
    LinearLayout layoutFingerMark;
    @BindView(R.id.layout_cheshenjiaozhun)
    LinearLayout layoutCheshenjiaozhun;
    @BindView(R.id.layout_youmenjiaozhun)
    LinearLayout layoutYoumenjiaozhun;
    @BindView(R.id.layout_change_ble_password)
    LinearLayout layoutChangeBlePassword;
    @BindView(R.id.layout_ra_set)
    LinearLayout layoutRaSet;
    @BindView(R.id.layout_sc_set)
    LinearLayout layoutScSet;
    @BindView(R.id.layout_dengdaimoshi)
    LinearLayout layoutDengdaimoshi;

    private BluetoothLeService mBluetoothLeService;
    private boolean mConnected = false;
    private String mDeviceAddress;
    private String firmwareCode;//软件版本号
    String uuid;

    protected int attachLayoutRes() {
        return R.layout.activity_more_car_info;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        // 透明状态栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        tvTitle.setText(R.string.more_setting);
        mDeviceAddress = getIntent().getStringExtra("mDeviceAddress");
        cbQiandeng.setOnCheckedChangeListener(this);
        cbHoudeng.setOnCheckedChangeListener(this);
        cbHuaxing.setOnCheckedChangeListener(this);
        cbDingsuxunhang.setOnCheckedChangeListener(this);
        uuid = PreferencesUtils.getString(MoreCarInfoActivity.this, "uuid");
        if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {//滑板车
            layoutYoumenjiaozhun.setVisibility(View.VISIBLE);
            layoutCheshenjiaozhun.setVisibility(View.GONE);
            layoutScSet.setVisibility(View.VISIBLE);
            layoutRaSet.setVisibility(View.GONE);
            layoutFingerMark.setVisibility(View.VISIBLE);
            layoutChangeBlePassword.setVisibility(View.VISIBLE);
            layoutDengdaimoshi.setVisibility(View.VISIBLE);
        } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {//平衡车
            layoutYoumenjiaozhun.setVisibility(View.GONE);
            layoutCheshenjiaozhun.setVisibility(View.VISIBLE);
            layoutScSet.setVisibility(View.GONE);
            layoutRaSet.setVisibility(View.VISIBLE);
            layoutFingerMark.setVisibility(View.GONE);
            layoutChangeBlePassword.setVisibility(View.GONE);
            layoutDengdaimoshi.setVisibility(View.GONE);
        }

        //设置车辆限速值
        speedSeekBar.setOnProgressChange(new SpeedSeekBar.OnProgressChange() {
            @Override
            public void progressChange(int progress) {
                if (mBluetoothLeService!= null) {
                    if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                        mBluetoothLeService.writeCharacteristic(Constant.BLE.WRITE_SERVICE_UUID,
                                Constant.BLE.WRITE_CHARACTERISTIC_UUID, BleUtil.getSpeedLimit(progress));
                    } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                        mBluetoothLeService.writeCharacteristic(Constant.BLE.TAIDOU_WRITE_SERVICE_UUID,
                                Constant.BLE.TAIDOU_WRITE_CHARACTERISTIC_UUID,
                                Constant.BLE.TAIDOU_NOTIFY_CHARACTERISTIC_UUID,
                                BleUtil.getSpeedLimit(progress));
                    }
                }
            }
        });

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //绑定蓝牙服务
        if (bluetoothAdapter.isEnabled()) {
            Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
            bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }

    }

    @Override
    protected void updateViews() {

    }

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {//手机不支持ble
                Log.e("TAG", "Unable to initialize Bluetooth");
                //  finish();
            }

            //查询软件版本号
            if (null != mBluetoothLeService) {
                String uuid = PreferencesUtils.getString(MoreCarInfoActivity.this, "uuid");
                if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                    mBluetoothLeService.writeCharacteristic(Constant.BLE.WRITE_SERVICE_UUID,
                            Constant.BLE.WRITE_CHARACTERISTIC_UUID, Constant.BLE.SOFTWARE_VERSION_CODE);
                    mBluetoothLeService.setCharacteristicNotification(null, true);
                } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                    mBluetoothLeService.writeCharacteristic(Constant.BLE.TAIDOU_WRITE_SERVICE_UUID,
                            Constant.BLE.TAIDOU_WRITE_CHARACTERISTIC_UUID,
                            Constant.BLE.TAIDOU_NOTIFY_CHARACTERISTIC_UUID,
                            Constant.BLE.SOFTWARE_VERSION_CODE);
                }
            }

            SystemClock.sleep(120);
            //车辆最高/低限速值
            if (null != mBluetoothLeService) {
                if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                    Log.i("TAG", "------发送车辆最高/低限速值指令");
                    mBluetoothLeService.writeCharacteristic(Constant.BLE.WRITE_SERVICE_UUID,
                            Constant.BLE.WRITE_CHARACTERISTIC_UUID,
                            Constant.BLE.SPEED_LIMIT_VALUES);
                    mBluetoothLeService.setCharacteristicNotification(null, true);
                } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                    mBluetoothLeService.writeCharacteristic(Constant.BLE.TAIDOU_WRITE_SERVICE_UUID,
                            Constant.BLE.TAIDOU_WRITE_CHARACTERISTIC_UUID,
                            Constant.BLE.TAIDOU_NOTIFY_CHARACTERISTIC_UUID,
                            Constant.BLE.SPEED_LIMIT_VALUES);
                }
            }

            SystemClock.sleep(120);
            //查询车辆状态
            if (null != mBluetoothLeService) {
                if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                    mBluetoothLeService.writeCharacteristic(Constant.BLE.WRITE_SERVICE_UUID,
                            Constant.BLE.WRITE_CHARACTERISTIC_UUID, Constant.BLE.CAR_STATE);
                    mBluetoothLeService.setCharacteristicNotification(null, true);
                } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                    mBluetoothLeService.writeCharacteristic(Constant.BLE.TAIDOU_WRITE_SERVICE_UUID,
                            Constant.BLE.TAIDOU_WRITE_CHARACTERISTIC_UUID,
                            Constant.BLE.TAIDOU_NOTIFY_CHARACTERISTIC_UUID,
                            Constant.BLE.CAR_STATE);
                }
            }

            SystemClock.sleep(120);
            //车辆实际限速值
            if (null != mBluetoothLeService) {
                if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                    mBluetoothLeService.writeCharacteristic(Constant.BLE.WRITE_SERVICE_UUID,
                            Constant.BLE.WRITE_CHARACTERISTIC_UUID, Constant.BLE.PRACTICAL_LIMIT_VALUES);
                    mBluetoothLeService.setCharacteristicNotification(null, true);
                } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                    mBluetoothLeService.writeCharacteristic(Constant.BLE.TAIDOU_WRITE_SERVICE_UUID,
                            Constant.BLE.TAIDOU_WRITE_CHARACTERISTIC_UUID,
                            Constant.BLE.TAIDOU_NOTIFY_CHARACTERISTIC_UUID,
                            Constant.BLE.PRACTICAL_LIMIT_VALUES);
                }
            }

            SystemClock.sleep(120);
            //查询车辆硬件版本号
            if (null != mBluetoothLeService) {
                if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                    mBluetoothLeService.writeCharacteristic(Constant.BLE.WRITE_SERVICE_UUID,
                            Constant.BLE.WRITE_CHARACTERISTIC_UUID, Constant.BLE.FIRMWARE_VERSION_CODE);
                    mBluetoothLeService.setCharacteristicNotification(null, true);
                } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                    mBluetoothLeService.writeCharacteristic(Constant.BLE.TAIDOU_WRITE_SERVICE_UUID,
                            Constant.BLE.TAIDOU_WRITE_CHARACTERISTIC_UUID,
                            Constant.BLE.TAIDOU_NOTIFY_CHARACTERISTIC_UUID,
                            Constant.BLE.FIRMWARE_VERSION_CODE);
                }
            }

            SystemClock.sleep(120);
            //程序运行位置
            if (null != mBluetoothLeService) {
                if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                    mBluetoothLeService.writeCharacteristic(Constant.BLE.WRITE_SERVICE_UUID,
                            Constant.BLE.WRITE_CHARACTERISTIC_UUID, Constant.BLE.PROGRAM_LOCATION);
                    mBluetoothLeService.setCharacteristicNotification(null, true);
                } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                    mBluetoothLeService.writeCharacteristic(Constant.BLE.TAIDOU_WRITE_SERVICE_UUID,
                            Constant.BLE.TAIDOU_WRITE_CHARACTERISTIC_UUID,
                            Constant.BLE.TAIDOU_NOTIFY_CHARACTERISTIC_UUID,
                            Constant.BLE.PROGRAM_LOCATION);
                }
            }

            SystemClock.sleep(120);
            //氛围灯模式
            if (null != mBluetoothLeService) {
                if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                    mBluetoothLeService.writeCharacteristic(Constant.BLE.WRITE_SERVICE_UUID,
                            Constant.BLE.WRITE_CHARACTERISTIC_UUID,
                            Constant.BLE.CURRENT_DENGDAI_MODE);
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.i("TAG", "----蓝牙连接成功");
                mConnected = true;
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.i("TAG", "----蓝牙断开连接成功");
                mConnected = false;
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                Log.i("TAG", "----蓝牙DISCOVERED");
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                byte[] data = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                displayData(data);
            }
        }
    };

    //蓝牙设备返回信息
    private void displayData(byte[] data) {
        if (data != null) {
            if (!BleUtil.receiveDataCRCCheck(data)) {
                return;
            }
            mPresenter.displayData(data);
            String info = ConversionUtil.byte2HexStr(data);
            Log.i("TAG", "-----MoreCarInfo_info" + info);
            String[] infos = info.split(" ");
            Log.i("TAG", "-----MoreCarInfo_infos" + infos.length);
            if (infos.length > 5) {
                //TODO 发送指令成功后改变状态
                //前灯关闭应答
                if ("04".equals(infos[2]) && "02".equals(infos[3]) && "01".equals(infos[4])) {
                    if ("01".equals(infos[6])) {
//                        ToastUtils.showToast("前灯关闭");
                    } else if ("00".equals(infos[6])) {
//                        ToastUtils.showToast("设置失败");
                    }
                }
                //前灯打开应答
                else if ("04".equals(infos[2]) && "02".equals(infos[3]) && "02".equals(infos[4])) {
                    if ("01".equals(infos[6])) {
//                        ToastUtils.showToast("前灯打开");
                    } else if ("00".equals(infos[6])) {
//                        ToastUtils.showToast("设置失败");
                    }
                }
                //后灯关闭应答
                else if ("04".equals(infos[2]) && "02".equals(infos[3]) && "0E".equals(infos[4])) {
                    if ("01".equals(infos[6])) {
//                        ToastUtils.showToast("后灯关闭");
                    } else if ("00".equals(infos[6])) {
//                        ToastUtils.showToast("设置失败");
                    }
                }
                //后灯打开应答
                else if ("04".equals(infos[2]) && "02".equals(infos[3]) && "0F".equals(infos[4])) {
                    if ("01".equals(infos[6])) {
//                        ToastUtils.showToast("后灯打开");
                    } else if ("00".equals(infos[6])) {
//                        ToastUtils.showToast("设置失败");
                    }
                }
                //打开定速巡航应答
                else if ("04".equals(infos[2]) && "02".equals(infos[3]) && "0B".equals(infos[4])) {
                    if ("01".equals(infos[6])) {//成功
                    } else if ("00".equals(infos[6])) {//失败
                    }
                }
                //关闭定速巡航应答
                else if ("04".equals(infos[2]) && "02".equals(infos[3]) && "0A".equals(infos[4])) {
                    if ("01".equals(infos[6])) {//成功
                    } else if ("00".equals(infos[6])) {//失败
                    }
                }
                //车辆限速应答
                else if ("04".equals(infos[2]) && "02".equals(infos[3]) && "05".equals(infos[4])) {
                    Log.i("TAG", "------车速限制反馈," + infos[6]);
                }
                //软件版本号
                else if ("04".equals(infos[2]) && "01".equals(infos[3]) && "05".equals(infos[4])) {
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
                    firmwareCode = code.toString();
                    tvFirmwareCode.setText(getResources().getString(R.string.current_firmware_version) + "：" + firmwareCode);
                    Log.i("TAG", "------软件版本号," + firmwareCode);
                } else if ("04".equals(infos[2]) && "03".equals(infos[3]) && "01".equals(infos[4])) {//水平校准
                    Log.i("TAG", "------水平校准反馈," + infos[6]);
                    if (null != layoutIsContinue && null != layoutStartAdjust)
                        if ("01".equals(infos[6])) {//操作成功
                            layoutIsContinue.setVisibility(View.GONE);
                            layoutStartAdjust.setVisibility(View.VISIBLE);
                        } else if ("00".equals(infos[6])) {//操作失败
                            layoutIsContinue.setVisibility(View.GONE);
                            layoutTishi.setVisibility(View.VISIBLE);
                        }
                } else if ("04".equals(infos[2]) && "02".equals(infos[3]) && "08".equals(infos[4])) {//标准模式切换
                    Log.i("TAG", "------节能模式切换反馈," + infos[6]);
                    if ("01".equals(infos[6])) {//设置成功
                        tvMode.setText(R.string.econ);
                    } else if ("00".equals(infos[6])) {//设置失败
//                        ToastUtils.showToast("切换失败");
                    }
                } else if ("04".equals(infos[2]) && "02".equals(infos[3]) && "09".equals(infos[4])) {//运动模式切换
                    Log.i("TAG", "------运动模式切换反馈," + infos[6]);
                    if ("01".equals(infos[6])) {//设置成功
                        tvMode.setText(R.string.sport);
                    } else if ("00".equals(infos[6])) {//设置失败
//                        ToastUtils.showToast("切换失败");
                    }
                } else if ("04".equals(infos[2]) && "01".equals(infos[3]) && "08".equals(infos[4])) {//最高限速、最低限速
                    Log.i("TAG", "------最高限速、最低限速反馈,");
                    int highSpeed = Integer.parseInt(infos[6], 16);
                    int lowSpeed = Integer.parseInt(infos[7], 16);
                    speedSeekBar.setHighSpeed(highSpeed, lowSpeed);
                    speedSeekBar.setLowSpeed(highSpeed, lowSpeed);
                } else if ("04".equals(infos[2]) && "01".equals(infos[3]) && "04".equals(infos[4])) {//车辆实际限速值
                    Log.i("TAG", "------车辆实际限速值,");
                    int practicalSpeed = Integer.parseInt(infos[6], 16);
                    speedSeekBar.setPracticalSpeed(practicalSpeed);
                } else if ("04".equals(infos[2]) && "01".equals(infos[3]) && "02".equals(infos[4])) {
                    String info1 = ConversionUtil.byte2HexStr(data);
                    Log.i("TAG", "-----车辆状态," + info1);
                    String b6 = Integer.toBinaryString(Integer.parseInt(infos[9], 16));
                    int a = (data[9] >> 4) & 0x1;
                    Log.i("TAG", "-----车辆状态,b6" + b6 + "," + a);
                    if (((data[9] >> 0) & 0x1) == 1) {
                        cbQiandeng.setChecked(true);
                    } else {
                        cbQiandeng.setChecked(false);
                    }
                    if (((data[9] >> 6) & 0x1) == 0) {
                        cbHoudeng.setChecked(true);
                    } else {
                        cbHoudeng.setChecked(false);
                    }
                    if (((data[9] >> 3) & 0x1) == 0) {
                        tvMode.setText(R.string.econ);
                    } else {
                        tvMode.setText(R.string.sport);
                    }
                }
                //固件更新
                else if ("04".equals(infos[2]) && "03".equals(infos[3]) && "03".equals(infos[4])) {
                    Log.i("TAG", "------固件更新反馈," + infos[6]);
                    if ("01".equals(infos[6])) {//操作成功
//                        layoutIsContinue.setVisibility(View.GONE);
//                        layoutStartAdjust.setVisibility(View.VISIBLE);
                        if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                            Intent intent = new Intent(MoreCarInfoActivity.this, ScFirmwareUpdateActivity.class);
                            intent.putExtra("firmwareCode", firmwareCode);
                            intent.putExtra("firmwareUpdataInfo", firmwareUpdataInfo);
                            startActivity(intent);
                        } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                            Intent intent = new Intent(MoreCarInfoActivity.this, FirmwareUpdateActivity.class);
                            intent.putExtra("firmwareCode", firmwareCode);
                            intent.putExtra("firmwareUpdataInfo", firmwareUpdataInfo);
                            startActivity(intent);
                        }
                        if (firmwareUpdatePopupWindow != null && firmwareUpdatePopupWindow.isShowing()) {
                            firmwareUpdatePopupWindow.dismiss();
                        }
                    } else if ("00".equals(infos[6])) {//操作失败
//                        layoutIsContinue.setVisibility(View.GONE);
//                        layoutTishi.setVisibility(View.VISIBLE);
                    }
                } else if ("04".equals(infos[2]) && "01".equals(infos[3]) && "07".equals(infos[4])) {
                    if ("01".equals(infos[6])) {//引导程序中
                        PreferencesUtils.putString(MoreCarInfoActivity.this, "programLocation", "01");
                        if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                            startActivity(new Intent(this, ScFirmwareUpdateActivity.class));
                        } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                            startActivity(new Intent(this, FirmwareUpdateActivity.class));
                        }
                    } else if ("00".equals(infos[6])) {//应用程序中
                        PreferencesUtils.putString(MoreCarInfoActivity.this, "programLocation", "00");
                    }
                }
                //车辆密码设置
                else if ("04".equals(infos[2]) && "02".equals(infos[3]) && "13".equals(infos[4])) {
                    if ("01".equals(infos[6])) {//设置成功
                        Log.i("TAG", "------密码设置成功");
                    } else {//设置失败
                        Log.i("TAG", "------密码设置失败");
                    }
                }
                //车辆密码复位
                else if ("04".equals(infos[2]) && "03".equals(infos[3]) && "0C".equals(infos[4])) {
                    if ("01".equals(infos[6])) {//设置成功
                        Log.i("TAG", "------车辆密码复位成功");
                    } else {//设置失败
                        Log.i("TAG", "------车辆密码复位失败");
                    }
                }
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_qianteng://前灯开关
                if (isChecked) {
                    if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                        mBluetoothLeService.writeCharacteristic(Constant.BLE.WRITE_SERVICE_UUID,
                                Constant.BLE.WRITE_CHARACTERISTIC_UUID, Constant.BLE.OPEN_LIGHT_FRONT);
                    } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                        mBluetoothLeService.writeCharacteristic(Constant.BLE.TAIDOU_WRITE_SERVICE_UUID,
                                Constant.BLE.TAIDOU_WRITE_CHARACTERISTIC_UUID,
                                Constant.BLE.TAIDOU_NOTIFY_CHARACTERISTIC_UUID,
                                Constant.BLE.OPEN_LIGHT_FRONT);
                    }
                } else {
                    if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                        mBluetoothLeService.writeCharacteristic(Constant.BLE.WRITE_SERVICE_UUID,
                                Constant.BLE.WRITE_CHARACTERISTIC_UUID, Constant.BLE.CLOSE_LIGHT_FRONT);
                    } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                        mBluetoothLeService.writeCharacteristic(Constant.BLE.TAIDOU_WRITE_SERVICE_UUID,
                                Constant.BLE.TAIDOU_WRITE_CHARACTERISTIC_UUID,
                                Constant.BLE.TAIDOU_NOTIFY_CHARACTERISTIC_UUID,
                                Constant.BLE.CLOSE_LIGHT_FRONT);
                    }
                }
                break;
            case R.id.cb_houdeng://后灯开关
                if (isChecked) {
                    if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                        mBluetoothLeService.writeCharacteristic(Constant.BLE.WRITE_SERVICE_UUID,
                                Constant.BLE.WRITE_CHARACTERISTIC_UUID, Constant.BLE.OPEN_LIGHT_BACK);
                    } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                        mBluetoothLeService.writeCharacteristic(Constant.BLE.TAIDOU_WRITE_SERVICE_UUID,
                                Constant.BLE.TAIDOU_WRITE_CHARACTERISTIC_UUID,
                                Constant.BLE.TAIDOU_NOTIFY_CHARACTERISTIC_UUID,
                                Constant.BLE.OPEN_LIGHT_BACK);
                    }
                } else {
                    if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                        mBluetoothLeService.writeCharacteristic(Constant.BLE.WRITE_SERVICE_UUID,
                                Constant.BLE.WRITE_CHARACTERISTIC_UUID, Constant.BLE.CLOSE_LIGHT_BACK);
                    } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                        mBluetoothLeService.writeCharacteristic(Constant.BLE.TAIDOU_WRITE_SERVICE_UUID,
                                Constant.BLE.TAIDOU_WRITE_CHARACTERISTIC_UUID,
                                Constant.BLE.TAIDOU_NOTIFY_CHARACTERISTIC_UUID,
                                Constant.BLE.CLOSE_LIGHT_BACK);
                    }
                }
                break;
            case R.id.cb_huaxing://滑行模式开关
                if (isChecked) {//开启滑行模式
                    if (null != mBluetoothLeService) {
                        if (null != Constant.BLE.OPEN_TAXI_START || Constant.BLE.OPEN_TAXI_START.length > 0) {
                            if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                                mBluetoothLeService.writeCharacteristic(Constant.BLE.WRITE_SERVICE_UUID,
                                        Constant.BLE.WRITE_CHARACTERISTIC_UUID, Constant.BLE.OPEN_TAXI_START);
                            } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                                mBluetoothLeService.writeCharacteristic(Constant.BLE.TAIDOU_WRITE_SERVICE_UUID,
                                        Constant.BLE.TAIDOU_WRITE_CHARACTERISTIC_UUID,
                                        Constant.BLE.TAIDOU_NOTIFY_CHARACTERISTIC_UUID,
                                        Constant.BLE.OPEN_TAXI_START);
                            }
                        }
                    }
                } else {//关闭滑行模式
                    if (null != mBluetoothLeService) {
                        if (null != Constant.BLE.CLOSE_TAXI_START || Constant.BLE.CLOSE_TAXI_START.length > 0) {
                            if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                                mBluetoothLeService.writeCharacteristic(Constant.BLE.WRITE_SERVICE_UUID,
                                        Constant.BLE.WRITE_CHARACTERISTIC_UUID, Constant.BLE.CLOSE_TAXI_START);
                            } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                                mBluetoothLeService.writeCharacteristic(Constant.BLE.TAIDOU_WRITE_SERVICE_UUID,
                                        Constant.BLE.TAIDOU_WRITE_CHARACTERISTIC_UUID,
                                        Constant.BLE.TAIDOU_NOTIFY_CHARACTERISTIC_UUID,
                                        Constant.BLE.CLOSE_TAXI_START);
                            }
                        }
                    }
                }
                break;
            case R.id.cb_dingsuxunhang://定速巡航
                if (isChecked) {//开启定速巡航
                    if (null != mBluetoothLeService) {
                        mBluetoothLeService.writeCharacteristic(Constant.BLE.WRITE_SERVICE_UUID,
                                Constant.BLE.WRITE_CHARACTERISTIC_UUID, Constant.BLE.OPEN_CONSTANT_SPEED);
                    }
                } else {//关闭定速巡航
                    if (null != mBluetoothLeService) {
                        mBluetoothLeService.writeCharacteristic(Constant.BLE.WRITE_SERVICE_UUID,
                                Constant.BLE.WRITE_CHARACTERISTIC_UUID, Constant.BLE.CLOSE_CONSTANT_SPEED);
                    }
                }
                break;
        }
    }

    //
    private void sendBleDate(final byte[] date) {
        SystemClock.sleep(25);
        String uuid = PreferencesUtils.getString(MoreCarInfoActivity.this, "uuid");
        if (null != mBluetoothLeService) {
            if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                mBluetoothLeService.writeCharacteristic(Constant.BLE.WRITE_SERVICE_UUID,
                        Constant.BLE.WRITE_CHARACTERISTIC_UUID,
                        date);

            } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                mBluetoothLeService.writeCharacteristic(Constant.BLE.TAIDOU_WRITE_SERVICE_UUID,
                        Constant.BLE.TAIDOU_WRITE_CHARACTERISTIC_UUID,
                        Constant.BLE.TAIDOU_NOTIFY_CHARACTERISTIC_UUID,
                        date);
            }
        }

    }

    @OnClick({R.id.layout_fault_detect, R.id.layout_firmware_update, R.id.layout_change_ble_password,
            R.id.layout_finger_mark, R.id.layout_cheshenjiaozhun, R.id.layout_moshiqiehuan,
            R.id.layout_youmenjiaozhun, R.id.layout_dengdaimoshi})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_finger_mark://指纹录制
                startActivity(new Intent(MoreCarInfoActivity.this, FingerMarkActivity.class));
                break;
            case R.id.layout_fault_detect://故障检测
                startActivity(new Intent(MoreCarInfoActivity.this, FaultDetectActivity.class));
                break;
            case R.id.layout_firmware_update://固件升级
                if (null != mBluetoothLeService) {
                    if (mBluetoothLeService.getConnectionState() == 2) {
                        checkVersion();
                    } else {
                        ToastUtils.showToast(R.string.please_firstly_connect_your_vehicle);
                    }
                }else {
                    ToastUtils.showToast(R.string.please_firstly_connect_your_vehicle);
                }
                break;
            case R.id.layout_change_ble_password://修改密码
                mPresenter.changeBlePasswordDialog(this, mBluetoothLeService);
                break;
            case R.id.layout_cheshenjiaozhun://车身校准
                showHorizontalAlignmentPop(findViewById(R.id.layout_cheshenjiaozhun));
                break;
            case R.id.layout_moshiqiehuan://模式切换
                String uuid3 = PreferencesUtils.getString(MoreCarInfoActivity.this, "uuid");
                if (null != mBluetoothLeService) {
                    if (getResources().getString(R.string.econ).equals(tvMode.getText().toString())) {//标准模式切换到运动模式
                        if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid3)) {
                            mBluetoothLeService.writeCharacteristic(Constant.BLE.WRITE_SERVICE_UUID,
                                    Constant.BLE.WRITE_CHARACTERISTIC_UUID,
                                    Constant.BLE.MODE_SWITCH_SPORTS);
                        } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid3)) {
                            mBluetoothLeService.writeCharacteristic(Constant.BLE.TAIDOU_WRITE_SERVICE_UUID,
                                    Constant.BLE.TAIDOU_WRITE_CHARACTERISTIC_UUID,
                                    Constant.BLE.TAIDOU_NOTIFY_CHARACTERISTIC_UUID,
                                    Constant.BLE.MODE_SWITCH_SPORTS);
                        }
                    } else if (getResources().getString(R.string.sport).equals(tvMode.getText().toString())) {//运动模式切换到标准模式
                        if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid3)) {
                            mBluetoothLeService.writeCharacteristic(Constant.BLE.WRITE_SERVICE_UUID,
                                    Constant.BLE.WRITE_CHARACTERISTIC_UUID,
                                    Constant.BLE.MODE_SWITCH_ENERGY_SAVING);
                        } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid3)) {
                            mBluetoothLeService.writeCharacteristic(Constant.BLE.TAIDOU_WRITE_SERVICE_UUID,
                                    Constant.BLE.TAIDOU_WRITE_CHARACTERISTIC_UUID,
                                    Constant.BLE.TAIDOU_NOTIFY_CHARACTERISTIC_UUID,
                                    Constant.BLE.MODE_SWITCH_ENERGY_SAVING);
                        }
                    }
                }
                break;
            case R.id.layout_dengdaimoshi://灯带模式
                mPresenter.openDengdaiPopupWindow(findViewById(R.id.layout_dengdaimoshi), mBluetoothLeService);
                break;
            case R.id.layout_youmenjiaozhun://油门校准
                mPresenter.showYoumenAlignmentPop(layoutYoumenjiaozhun, mBluetoothLeService);
                break;
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("TAG", "-------MoreCarInfo OnResume");
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

        if (null != mBluetoothLeService && mBluetoothLeService.getConnectionState() == BluetoothLeService.STATE_DISCONNECTED) {
            Log.i("TAG", "-------MoreCarInfoActivity,onResume(),1");
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.i("TAG", "-------Connect request result=" + result);
        }

        if (null != mBluetoothLeService && mBluetoothLeService.getConnectionState() == BluetoothLeService.STATE_CONNECTED) {
            Log.d("TAG", "-------MoreCarInfoActivity,onResume(),2");
        }

        if (null == mBluetoothLeService){
            Log.i("TAG", "-------MoreCarInfoActivity,onResume(),3");
            Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
            bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mBluetoothLeService) {
            unbindService(mServiceConnection);
            mBluetoothLeService = null;
        }
    }

    PopupWindow popupWindow;
    LinearLayout layoutIsContinue;
    LinearLayout layoutTishi;
    LinearLayout layoutStartAdjust;
    LinearLayout layoutProgress;

    /**
     * 车身水平校准弹框
     */
    public void showHorizontalAlignmentPop(View v) {
        //防止重复按按钮
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        //设置PopupWindow的View
        final View view = LayoutInflater.from(MoreCarInfoActivity.this).inflate(R.layout.popup_horizontal_alignment, null);
        layoutIsContinue = (LinearLayout) view.findViewById(R.id.layout_is_continue);
        layoutTishi = (LinearLayout) view.findViewById(R.id.layout_tishi);
        layoutStartAdjust = (LinearLayout) view.findViewById(R.id.layout_start_adjust);
        layoutProgress = (LinearLayout) view.findViewById(R.id.layout_progress);
        final LinearLayout layoutAdjustConfirm = (LinearLayout) view.findViewById(R.id.layout_adjust_confirm);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        TextView tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tvStartAdjust = (TextView) view.findViewById(R.id.tv_start_adjust);
        final TextView tvProgress = (TextView) view.findViewById(R.id.tv_progress);
        tvProgress.setVisibility(View.VISIBLE);

        //继续
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uuid = PreferencesUtils.getString(MoreCarInfoActivity.this, "uuid");
                if (null != mBluetoothLeService) {
                    if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {

                    } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                        mBluetoothLeService.writeCharacteristic(Constant.BLE.TAIDOU_WRITE_SERVICE_UUID,
                                Constant.BLE.TAIDOU_WRITE_CHARACTERISTIC_UUID,
                                Constant.BLE.TAIDOU_NOTIFY_CHARACTERISTIC_UUID,
                                Constant.BLE.HORIZONTAL_ALIGNMENT);
                    }
                }
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
        //开始校准
        tvStartAdjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutStartAdjust.setVisibility(View.GONE);
                layoutProgress.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                String uuid = PreferencesUtils.getString(MoreCarInfoActivity.this, "uuid");
                if (null != mBluetoothLeService) {
                    if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {

                    } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                        mBluetoothLeService.writeCharacteristic(Constant.BLE.TAIDOU_WRITE_SERVICE_UUID,
                                Constant.BLE.TAIDOU_WRITE_CHARACTERISTIC_UUID,
                                Constant.BLE.TAIDOU_NOTIFY_CHARACTERISTIC_UUID,
                                Constant.BLE.HORIZONTAL_ALIGNMENT_CONFIRM);
                    }
                }
                RxCountDown.countdown(5).subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        layoutProgress.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        layoutAdjustConfirm.setVisibility(View.VISIBLE);
                        progressBar.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (null != popupWindow){
                                    popupWindow.dismiss();
                                }
                            }
                        }, 2000);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        progressBar.setProgress(100 - 20 * integer);
                        tvProgress.setText((100 - 20 * integer)+"%");
                    }
                });
            }
        });
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置背景,这个没什么效果，不添加会报错
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击弹窗外隐藏自身
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置动画
        popupWindow.setAnimationStyle(R.style.PopupWindow);
        //设置位置
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }


    private PopupWindow firmwareUpdatePopupWindow;
    /**
     * 固件更新弹框
     */
    public void showFirmwareUpdatePop(View v) {
        //防止重复按按钮
        if (firmwareUpdatePopupWindow != null && firmwareUpdatePopupWindow.isShowing()) {
            return;
        }
        //设置PopupWindow的View
        final View view = LayoutInflater.from(MoreCarInfoActivity.this).inflate(R.layout.popup_horizontal_alignment, null);
        LinearLayout layoutIsContinue = (LinearLayout) view.findViewById(R.id.layout_is_continue);
        LinearLayout layoutTishi = (LinearLayout) view.findViewById(R.id.layout_tishi);
        LinearLayout layoutStartAdjust = (LinearLayout) view.findViewById(R.id.layout_start_adjust);
        LinearLayout layoutAdjustConfirm = (LinearLayout) view.findViewById(R.id.layout_adjust_confirm);
        LinearLayout layoutProgress = (LinearLayout) view.findViewById(R.id.layout_progress);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        TextView tvProgress = (TextView) view.findViewById(R.id.tv_progress);
        tvProgress.setVisibility(View.VISIBLE);

        TextView tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tvStartAdjust = (TextView) view.findViewById(R.id.tv_start_adjust);
        tvStartAdjust.setText(R.string.start_update);
        TextView tvTishi = (TextView) view.findViewById(R.id.tv_tishi);
        tvTishi.setText(R.string.firmware_cant_update_because_the_vehicle_is_not_still);
        TextView tvWancheng = (TextView) view.findViewById(R.id.tv_wancheng);
        tvWancheng.setText(R.string.update_successfully_please_restart_vehicle);
        //继续
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uuid = PreferencesUtils.getString(MoreCarInfoActivity.this, "uuid");
                if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                    mBluetoothLeService.writeCharacteristic(Constant.BLE.WRITE_SERVICE_UUID,
                            Constant.BLE.WRITE_CHARACTERISTIC_UUID,
                            Constant.BLE.FIRMWARE_UPDATE);
                } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                    mBluetoothLeService.writeCharacteristic(Constant.BLE.TAIDOU_WRITE_SERVICE_UUID,
                            Constant.BLE.TAIDOU_WRITE_CHARACTERISTIC_UUID,
                            Constant.BLE.TAIDOU_NOTIFY_CHARACTERISTIC_UUID,
                            Constant.BLE.FIRMWARE_UPDATE);
                }
            }
        });

        //取消
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firmwareUpdatePopupWindow != null && firmwareUpdatePopupWindow.isShowing()) {
                    firmwareUpdatePopupWindow.dismiss();
                }
            }
        });
        firmwareUpdatePopupWindow = new
                PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置背景,这个没什么效果，不添加会报错
        firmwareUpdatePopupWindow.setBackgroundDrawable(new
                BitmapDrawable()
        );
        //设置点击弹窗外隐藏自身
        firmwareUpdatePopupWindow.setFocusable(true);
        firmwareUpdatePopupWindow.setOutsideTouchable(true);
        //设置动画
        firmwareUpdatePopupWindow.setAnimationStyle(R.style.PopupWindow);
        //设置位置
        firmwareUpdatePopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }

    private String firmwareUpdataInfo;
    private void checkVersion() {
        //        if (null != firmwareCode && firmwareCode.length() > 4) {
        String carType = "";
        if (null != firmwareCode && firmwareCode.length() > 4) {
            carType = firmwareCode.substring(0, 4);
            mPresenter.getRetrofitHelper().getFirmware(carType)
                    .compose(RxUtil.<FirmwareInfo>rxSchedulerHelper())
                    .subscribe(new CommonSubscriber<FirmwareInfo>() {
                        @Override
                        public void onNext(FirmwareInfo firmwareInfo) {
                            if (firmwareInfo.getCode() == 1) {
                                Log.i("TAG", "------固件信息，" + firmwareInfo.getObj().getFiles());
//                                PreferencesUtils.putString(MoreCarInfoActivity.this, "downloadUrl", firmwareInfo.getObj().getUrl());
                                char[] c = firmwareCode.toCharArray();
                                int carCode = Integer.parseInt(c[5] + "" + 0 + "" + c[7]);
                                Log.i("TAG", "------固件信息，设备版本：" + carCode + ", 后台版本：" + firmwareInfo.getObj().getFirmwareVersion());
                                if (carCode < firmwareInfo.getObj().getFirmwareVersion()||
                                         "01".equals(PreferencesUtils.getString(
                                        MoreCarInfoActivity.this, "programLocation"))) {//不是最新版本，提示升级或是在引导程序中
                                    Log.i("TAG", "------固件信息，不是最新版本，提示升级");
                                    if ("RA".equals(firmwareCode.substring(0, 2)) && firmwareInfo.getObj().getFiles()!=null
                                            && firmwareInfo.getObj().getFiles().size()>0) {
                                        firmwareUpdataInfo = firmwareInfo.getObj().getDescription();
                                        String downloadUrl = firmwareInfo.getObj().getFiles().get(0).getUrl();
                                        downloadRANew(downloadUrl);
                                    }else if("SC".equals(firmwareCode.substring(0, 2)) && firmwareInfo.getObj().getFiles()!=null
                                            && firmwareInfo.getObj().getFiles().size()>1){
                                        firmwareUpdataInfo = firmwareInfo.getObj().getDescription();
                                        String masterUrl = "";
                                        String slave01Url = "";
                                        for (int i = 0; i < firmwareInfo.getObj().getFiles().size(); i++) {
                                            if ("master".equals(firmwareInfo.getObj().getFiles().get(i).getFVersion())){
                                                masterUrl = firmwareInfo.getObj().getFiles().get(i).getUrl();
                                            }else if("slave01".equals(firmwareInfo.getObj().getFiles().get(i).getFVersion())){
                                                slave01Url = firmwareInfo.getObj().getFiles().get(i).getUrl();
                                            }
                                        }
                                        downloadSCNew(masterUrl, slave01Url);
                                    }
                                } else {
                                    Log.i("TAG", "------固件信息，是最新版本，不升级");
                                    ToastUtils.showToast(getResources().getString(R.string.the_current_firmware_version_is_already_latest));
                                }
                            }else {
                                ToastUtils.showToast(firmwareInfo.getMsg());
                            }
                        }
                    });
        }
    }

    //下载RA固件
    private void downloadRANew(String downloadUrl) {
        Log.i("TAG", "------下载固件，" + downloadUrl);
        DownloadFileUtil.init(this).start(downloadUrl, "firmware.bin").isInstall(false)
                .setOnDownloadComplete(new DownloadFileUtil.OnDownloadComplete() {
                    @Override
                    public void downloadComplete(String path, File result) {
                        Log.i("TAG", "------下载固件完成，" + path);
                        showFirmwareUpdatePop(tvFirmwareCode);
                    }
                });
    }

    //下载SC固件
    private void downloadSCNew(final String masterUrl, final String slave01Url) {
        Log.i("TAG", "------下载固件，" + masterUrl+","+masterUrl);
        if (TextUtils.isEmpty(masterUrl) || TextUtils.isEmpty(slave01Url)){
            return;
        }
        DownloadFileUtil.init(this).start(masterUrl, "master.bin").isInstall(false)
                .setOnDownloadComplete(new DownloadFileUtil.OnDownloadComplete() {
                    @Override
                    public void downloadComplete(String path, File result) {
                        Log.i("TAG", "------下载固件完成，" + path);
                        DownloadFileUtil.init(MoreCarInfoActivity.this).start(slave01Url, "slave01.bin").isInstall(false)
                                .setOnDownloadComplete(new DownloadFileUtil.OnDownloadComplete() {
                                    @Override
                                    public void downloadComplete(String path, File result) {
                                        Log.i("TAG", "------下载固件完成，" + path);
                                        showFirmwareUpdatePop(tvFirmwareCode);
                                    }
                                });
                    }
                });
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public BluetoothLeService getBluetoothLeService() {
        return mBluetoothLeService;
    }
}
