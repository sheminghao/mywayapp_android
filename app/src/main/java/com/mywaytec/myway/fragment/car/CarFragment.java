package com.mywaytec.myway.fragment.car;


import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseFragment;
import com.mywaytec.myway.base.Constant;
import com.mywaytec.myway.base.MqttService;
import com.mywaytec.myway.model.bean.BleInfoBean;
import com.mywaytec.myway.ui.bluetooth.BluetoothActivity;
import com.mywaytec.myway.ui.firmwareUpdate.FirmwareUpdateActivity;
import com.mywaytec.myway.ui.moreCarInfo.MoreCarInfoActivity;
import com.mywaytec.myway.ui.scFirmwareUpdate.ScFirmwareUpdateActivity;
import com.mywaytec.myway.utils.BleKitUtils;
import com.mywaytec.myway.utils.BleUtil;
import com.mywaytec.myway.utils.ConversionUtil;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.utils.data.BleInfo;
import com.mywaytec.myway.view.EmptyLayout;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

import static com.inuker.bluetooth.library.Constants.REQUEST_CANCELED;
import static com.inuker.bluetooth.library.Constants.REQUEST_FAILED;
import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DEVICE_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DISCONNECTED;

/**
 * 车辆模块
 * Created by shemh on 2017/2/20.
 */

public class CarFragment extends BaseFragment<CarPresenter> implements CarView, View.OnClickListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_speed)
    TextView tvSpeed;
    @BindView(R.id.tv_licheng)
    TextView tvLicheng;
    @BindView(R.id.tv_zonglicheng)
    TextView tvZonglicheng;
    @BindView(R.id.tv_dianliang)
    TextView tvDianliang;
    @BindView(R.id.connection)
    TextView tvConnection;
    @BindView(R.id.btn_lock_car)
    Button btnLockCar;

    /**
     * 是否锁车
     */
    private boolean isLock;
    /**
     * 是否是第一次连接
     */
    private boolean isFirstConn = true;
    /**
     * 设备名称
     */
    private String mDeviceName;
    private String mDeviceAddress;
    //是否在发速度请求以外的指令
    private boolean isOther;

    private BluetoothAdapter bluetoothAdapter;

    private static CarFragment carFragment;

    public static CarFragment getInstance() {
        if (carFragment == null) {
            return new CarFragment();
        }
        return carFragment;
    }

    String uuid = null;

    // 演示如何遍历支持GATT Services/Characteristics
    // 这个例子中，我们填充绑定到UI的ExpandableListView上的数据结构
    private void displayGattServices(List<BleGattService> gattServices) {
        if (gattServices == null) {
            Log.i("TAG", "-------gattServices为空");
            return;
        }
        Log.i("TAG", "-------进入gattServices" + gattServices.size());
        // 循环可用的GATT Services.
        for (BleGattService gattService : gattServices) {
            String suuid = gattService.getUUID().toString();
            if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(suuid)) {
                Log.i("TAG", "-------泰斗：" + suuid);
                uuid = suuid;
                break;
            } else if (Constant.BLE.WRITE_SERVICE_UUID.equals(suuid)) {
                Log.i("TAG", "-------滑板车：" + suuid);
                uuid = suuid;
                break;
            }
        }
        PreferencesUtils.putString(getActivity(), "uuid", uuid);

        SystemClock.sleep(100);
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

        SystemClock.sleep(100);
        //查询车辆sn码
        String uuid = PreferencesUtils.getString(getActivity(), "uuid");
        if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
            BleKitUtils.writeP(mDeviceAddress, Constant.BLE.SN_CODE, new BleWriteResponse() {
                @Override
                public void onResponse(int code) {

                }
            });
        } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
            BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.SN_CODE, new BleWriteResponse() {
                @Override
                public void onResponse(int code) {

                }
            });
        }

        SystemClock.sleep(100);
        //车辆状态
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

        SystemClock.sleep(100);

        if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
            BleKitUtils.writeP(mDeviceAddress, Constant.BLE.PROGRAM_LOCATION, new BleWriteResponse() {
                @Override
                public void onResponse(int code) {

                }
            });
        } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
            BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.PROGRAM_LOCATION, new BleWriteResponse() {
                @Override
                public void onResponse(int code) {

                }
            });
        }

        //发送速度请求指令
        sendSpeedRequest();
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_car;
    }

    @Override
    protected void initInjector() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText("MYWAY");
        setFouts();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (null != BleUtil.getBleName()) {
            //初始化，优先显示上次连接过的车辆，如果没有数据，显示MYWAY
            if (BleUtil.getBleName().length() > 3 && "RA".equals(BleUtil.getBleName().substring(0, 2))
                    || "SC".equals(BleUtil.getBleName().substring(0, 2))) {
                mDeviceName = BleUtil.getBleName().substring(3);
            } else {
                mDeviceName = BleUtil.getBleName();
            }
        } else {
            mDeviceName = "MYWAY";
        }
        mDeviceAddress = BleUtil.getBleAddress();

        tvTitle.setText(mDeviceName);
    }

    //设置字体
    private void setFouts() {
        AssetManager mgr = getActivity().getAssets();//得到AssetManager
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/Swis721_Cn_BT.ttf");//根据路径得到Typeface
        tvSpeed.setTypeface(tf);//设置字体
        tvDianliang.setTypeface(tf);
        tvZonglicheng.setTypeface(tf);
        tvLicheng.setTypeface(tf);
    }

    @Override
    protected void updateViews() {
    }


    private Timer timer;
    private TimerTask timerTask;

    //发送速度请求指令，1秒发送8次
    private void sendSpeedRequest() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (!isOther) {
                    if (null != BleUtil.getSpeedAndPower() || BleUtil.getSpeedAndPower().length > 0) {
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
        };
        timer.schedule(timerTask, 0, 125);
    }

    //停止发送速度请求指令
    private void stopSendSpeedRequest() {
        if (null != timerTask) {
            timerTask.cancel();
            timerTask = null;
        }
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
    }

    int zongLicheng;

    //处理蓝牙设备反馈指令
    private void displayData(byte[] data) {
        if (data != null) {
            if (!BleUtil.receiveDataCRCCheck(data)) {
                return;
            }
            mPresenter.displayData(data);
            String info = ConversionUtil.byte2HexStr(data);
//            Log.i("TAG", "-----info" + info);
            String[] infos = info.split(" ");
//            Log.i("TAG", "-----infos"+infos.length);
            if (infos.length > 5) {
                if (data.length > 12 && "04".equals(infos[2]) && "01".equals(infos[3]) && "01".equals(infos[4])) {
                    String name = new String(infos[0] + infos[1]);
                    int speed = Integer.parseInt(infos[6] + infos[7], 16);
                    int power = Integer.parseInt(infos[8], 16);
                    int licheng = Integer.parseInt(infos[9] + infos[10], 16);
                    zongLicheng = Integer.parseInt(infos[11] + infos[12] + infos[13] + infos[14], 16);
                    tvSpeed.setText(speed / 10.0 + "");
                    tvDianliang.setText(power + "");
                    tvLicheng.setText(licheng / 10.0 + "");
                    tvZonglicheng.setText(zongLicheng / 10.0 + "");
                    Log.i("TAG", "-----info" + speed + "," + power + "," + licheng + "," + zongLicheng);
                } else if ("04".equals(infos[2]) && "01".equals(infos[3]) && "02".equals(infos[4])) {
                    String info1 = ConversionUtil.byte2HexStr(data);
                    Log.i("TAG", "-----车辆状态," + info1);
                    //锁车状态位
                    if (((data[9] >> 1) & 0x1) == 1) {//锁车
                        Log.i("TAG", "-------锁车状态");
                        isLock = true;
                        btnLockCar.setBackgroundResource(R.drawable.btn_open_car_press);
                    } else {//解锁
                        Log.i("TAG", "-------解锁状态");
                        isLock = false;
                        btnLockCar.setBackgroundResource(R.drawable.btn_lock_car_press);
                    }

                    if (((data[8] >> 1) & 0x1) == 0) {//无指纹可用
                        Log.i("TAG", "-------无指纹可用");
                    }
                } else if ("04".equals(infos[2]) && "02".equals(infos[3]) && "04".equals(infos[4])) {//锁车
                    if ("01".equals(infos[6])) {
                        isLock = true;
                        btnLockCar.setBackgroundResource(R.drawable.btn_open_car_press);
                    } else if ("00".equals(infos[6])) {
                    }
                } else if ("04".equals(infos[2]) && "02".equals(infos[3]) && "03".equals(infos[4])) {//解锁
                    if ("01".equals(infos[6])) {
                        isLock = false;
                        btnLockCar.setBackgroundResource(R.drawable.btn_lock_car_press);
                    } else if ("00".equals(infos[6])) {
                    }
                }
                //车辆SN码
                else if ("04".equals(infos[2]) && "01".equals(infos[3]) && "03".equals(infos[4])) {
                    Log.i("TAG", "-----车辆SN码" + info);
                    StringBuilder snCode = new StringBuilder();
                    if (info.length() > 17) {
                        char code1 = (char) Integer.parseInt(infos[17], 16);
                        snCode.append(code1);
                        char code2 = (char) Integer.parseInt(infos[16], 16);
                        snCode.append(code2);
                        char code3 = (char) Integer.parseInt(infos[15], 16);
                        snCode.append(code3);
                        char code4 = (char) Integer.parseInt(infos[14], 16);
                        snCode.append(code4);
                        char code5 = (char) Integer.parseInt(infos[13], 16);
                        snCode.append(code5);
                        char code6 = (char) Integer.parseInt(infos[12], 16);
                        snCode.append(code6);
                        char code7 = (char) Integer.parseInt(infos[11], 16);
                        snCode.append(code7);
                        char code8 = (char) Integer.parseInt(infos[10], 16);
                        snCode.append(code8);
                        char code9 = (char) Integer.parseInt(infos[9], 16);
                        snCode.append(code9);
                        char code10 = (char) Integer.parseInt(infos[8], 16);
                        snCode.append(code10);
                        char code11 = (char) Integer.parseInt(infos[7], 16);
                        snCode.append(code11);
                        char code12 = (char) Integer.parseInt(infos[6], 16);
                        snCode.append(code12);
                    }
                    String snCodeStr = snCode.toString();
                    PreferencesUtils.putString(getActivity(), "snCode", snCodeStr);
                    mPresenter.useCar(snCodeStr, zongLicheng * 100);
                }
                //查询车辆是否在引导程序中
                else if ("04".equals(infos[2]) && "01".equals(infos[3]) && "07".equals(infos[4])) {
                    if ("01".equals(infos[6])) {//引导程序中
                        PreferencesUtils.putString(getActivity(), "programLocation", "01");
                        if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                            Intent intent = new Intent(getActivity(), ScFirmwareUpdateActivity.class);
//                            intent.putExtra("firmwareCode", fi);
//                            intent.putExtra("firmwareUpdataInfo", firmwareUpdataInfo);
                            intent.putExtra("mDeviceAddress", mDeviceAddress);
                            startActivity(intent);
                        } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                            Intent intent = new Intent(getActivity(), FirmwareUpdateActivity.class);
//                            intent.putExtra("firmwareCode", firmwareCode);
//                            intent.putExtra("firmwareUpdataInfo", firmwareUpdataInfo);
                            intent.putExtra("mDeviceAddress", mDeviceAddress);
                            startActivity(intent);
                        }
                    } else if ("00".equals(infos[6])) {//应用程序中
                        PreferencesUtils.putString(getActivity(), "programLocation", "00");
                    }
                }
                //车辆密码验证
                else if ("04".equals(infos[2]) && "02".equals(infos[3]) && "12".equals(infos[4])) {
                    if ("01".equals(infos[6])) {//验证成功
                        Log.i("TAG", "------密码验证成功");
                        isLock = false;
                        btnLockCar.setBackgroundResource(R.drawable.btn_lock_car_press);
                    } else {//验证失败
                        Log.i("TAG", "------密码验证失败");
                    }
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x00) {//打开蓝牙返回跳转到搜索页面
            if (bluetoothAdapter.enable()) {
                Intent intent1 = new Intent(getActivity(), BluetoothActivity.class);
                startActivityForResult(intent1, 0x01);
            }
        } else if (requestCode == 0x02) {//打开蓝牙返回连接设备
            tvConnection.setText(R.string.connecting);
            BleKitUtils.getBluetoothClient().connect(mDeviceAddress, new BleConnectResponse() {
                @Override
                public void onResponse(int code, BleGattProfile data) {
                    if (code == REQUEST_SUCCESS) {
                        displayGattServices(data.getServices());
                    } else if (code == REQUEST_FAILED) {
                        tvConnection.setText(R.string.connected);
                    } else if (code == REQUEST_CANCELED) {
                        tvConnection.setText(R.string.connected);
                    }
                }
            });
        } else if (requestCode == 0x01) {//选择设备返回
            mDeviceAddress = BleUtil.getBleAddress();
            if (null != BleUtil.getBleName()) {
                if (BleUtil.getBleName().length() > 3 && "RA".equals(BleUtil.getBleName().substring(0, 2))
                        || "SC".equals(BleUtil.getBleName().substring(0, 2))) {
                    mDeviceName = BleUtil.getBleName().substring(3);
                } else {
                    mDeviceName = BleUtil.getBleName();
                }
            } else {
                mDeviceName = "MYWAY";
            }
            tvTitle.setText(mDeviceName);
            Log.i("TAG", "----address:" + mDeviceAddress + "\n"
                    + "name:" + mDeviceName);
            tvConnection.setText(R.string.connecting);
            BleKitUtils.getBluetoothClient().connect(mDeviceAddress, new BleConnectResponse() {
                @Override
                public void onResponse(int code, BleGattProfile data) {
                    if (code == REQUEST_SUCCESS) {
                        displayGattServices(data.getServices());
                    } else if (code == REQUEST_FAILED) {
                        tvConnection.setText(R.string.connected);
                    } else if (code == REQUEST_CANCELED) {
                        tvConnection.setText(R.string.connected);
                    }
                }
            });
        }
    }

    private BroadcastReceiver mMqttUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (MqttService.ACTION_MQTT.equals(action)) {//获取手机解锁状态
                byte[] data = intent.getByteArrayExtra(MqttService.EXTRA_DATA);
                if (data != null && data.length > 0) {
                    if (data[0] == 1) {
                        Log.i("TAG", "------车辆为锁车状态");
                    } else if (data[0] == 2) {
                        Log.i("TAG", "------车辆为解锁状态");
                    }
                }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        Log.i("TAG", "-------CarFragment,onResume()");
        getActivity().registerReceiver(mMqttUpdateReceiver, makeMqttUpdateIntentFilter());
        BleKitUtils.getBluetoothClient().registerConnectStatusListener(mDeviceAddress, mBleConnectStatusListener);

        if (BleKitUtils.getBluetoothClient().getConnectStatus(mDeviceAddress) == STATUS_DEVICE_CONNECTED) {
            tvConnection.setText(R.string.disconnect);
            sendSpeedRequest();
        } else {
            tvConnection.setText(R.string.connected);
            tvSpeed.setText("0.0");
            tvLicheng.setText("0.0");
            tvDianliang.setText("0");
            tvZonglicheng.setText("0.0");
            stopSendSpeedRequest();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopSendSpeedRequest();
        getActivity().unregisterReceiver(mMqttUpdateReceiver);
        BleKitUtils.getBluetoothClient().unregisterConnectStatusListener(mDeviceAddress, mBleConnectStatusListener);
    }

    private final BleConnectStatusListener mBleConnectStatusListener = new BleConnectStatusListener() {

        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if (status == STATUS_CONNECTED) {
                //如果更新连接车辆，将车辆缓存信息清空
                if (TextUtils.isEmpty(BleInfo.getBleInfo().getMac()) && !mac.equals(BleInfo.getBleInfo().getMac())){
                    BleInfo.clearBleInfo();
                    BleInfoBean bleInfoBean = BleInfo.getBleInfo();
                    bleInfoBean.setMac(mac);
                    BleInfo.saveBleInfo(bleInfoBean);
                }
                tvConnection.setText(R.string.disconnect);
            } else if (status == STATUS_DISCONNECTED) {
                tvConnection.setText(R.string.connected);
                tvSpeed.setText("0.0");
                tvLicheng.setText("0.0");
                tvDianliang.setText("0");
                tvZonglicheng.setText("0.0");
                stopSendSpeedRequest();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private static IntentFilter makeMqttUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MqttService.ACTION_MQTT);
        return intentFilter;
    }

    @OnClick({R.id.connection, R.id.img_search, R.id.tv_right, R.id.btn_lock_car, R.id.btn_close_car})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connection:// 连接|断开设备
                if (getResources().getString(R.string.connected).equals(tvConnection.getText().toString())) {
                    if (BleKitUtils.getBluetoothClient().isBluetoothOpened()) {
                        tvConnection.setText(R.string.connecting);
                        BleKitUtils.getBluetoothClient().connect(mDeviceAddress, new BleConnectResponse() {
                            @Override
                            public void onResponse(int code, BleGattProfile data) {
                                if (code == REQUEST_SUCCESS) {
                                    displayGattServices(data.getServices());
                                } else if (code == REQUEST_FAILED) {
                                    tvConnection.setText(R.string.connected);
                                } else if (code == REQUEST_CANCELED) {
                                    tvConnection.setText(R.string.connected);
                                }
                            }
                        });
                    } else {
                        Intent intent2 = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(intent2, 0x02);
                    }
                } else if (getResources().getString(R.string.disconnect).equals(tvConnection.getText().toString())) {
                    BleKitUtils.getBluetoothClient().disconnect(mDeviceAddress);
                }
                break;
            case R.id.img_search://搜索
                if (null != bluetoothAdapter) {
                    if (bluetoothAdapter.isEnabled()) {
                        Intent intent1 = new Intent(getActivity(), BluetoothActivity.class);
                        startActivityForResult(intent1, 0x01);
                    } else {
                        Intent intent2 = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(intent2, 0x00);
                    }
                } else {
                    ToastUtils.showToast(R.string.该设备不支持蓝牙功能);
                }
                break;
            case R.id.tv_right://更多设置
                stopSendSpeedRequest();
                if (BleKitUtils.getBluetoothClient().getConnectStatus(mDeviceAddress) == STATUS_DEVICE_CONNECTED) {
                    Intent intent = new Intent(getActivity(), MoreCarInfoActivity.class);
                    intent.putExtra("mDeviceAddress", mDeviceAddress);
                    startActivity(intent);
                } else {
                    ToastUtils.showToast(R.string.please_firstly_connect_your_vehicle);
                }
                break;
            case R.id.btn_lock_car:
                if (BleKitUtils.getBluetoothClient().getConnectStatus(mDeviceAddress) == STATUS_DEVICE_CONNECTED) {
                    isOther = true;
                    SystemClock.sleep(30);
                    if (!isLock) {//锁车
                        if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                            BleKitUtils.writeP(mDeviceAddress, Constant.BLE.LOCK_CAR, new BleWriteResponse() {
                                @Override
                                public void onResponse(int code) {

                                }
                            });
                        } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                            BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.LOCK_CAR, new BleWriteResponse() {
                                @Override
                                public void onResponse(int code) {

                                }
                            });
                        }
                    } else {//解锁
                        if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                            Log.i("TAG", "------verificationVehiclePassword");
                            mPresenter.verificationVehiclePassword(mDeviceAddress);
                        } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                            BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.DEBLOCKING, new BleWriteResponse() {
                                @Override
                                public void onResponse(int code) {

                                }
                            });
                        }
                    }
                    SystemClock.sleep(30);
                    isOther = false;
                } else {
                    ToastUtils.showToast(R.string.please_firstly_connect_your_vehicle);
                }
                break;
            case R.id.btn_close_car://关机
                if (BleKitUtils.getBluetoothClient().getConnectStatus(mDeviceAddress) == STATUS_DEVICE_CONNECTED) {
                    isOther = true;
                    SystemClock.sleep(30);
                    if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                        BleKitUtils.writeP(mDeviceAddress, Constant.BLE.CLOSE_CAR, new BleWriteResponse() {
                            @Override
                            public void onResponse(int code) {

                            }
                        });
                    } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                        BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.CLOSE_CAR, new BleWriteResponse() {
                            @Override
                            public void onResponse(int code) {

                            }
                        });
                    }
                    SystemClock.sleep(30);
                    isOther = false;
                } else {
                    ToastUtils.showToast(R.string.please_firstly_connect_your_vehicle);
                }
                break;
        }
    }

    @Override
    public void showNetError(EmptyLayout.OnRetryListener onRetryListener) {

    }

    @Override
    public TextView getTextView() {
        return tvLicheng;
    }

    @Override
    public boolean isOther() {
        return isOther;
    }

    @Override
    public void setOther(boolean isOther) {
        this.isOther = isOther;
    }

    public AlertDialog confirmDialog;

    /**
     * 是否确认dialog
     */
    public void confirmDialog(Context context, String hint, View.OnClickListener mOnClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_confirm, null);
        TextView tvHint = (TextView) view.findViewById(R.id.tv_hint_text);
        tvHint.setText(hint);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog.dismiss();
            }
        });
        btnConfirm.setOnClickListener(mOnClickListener);
        builder.setView(view);
        confirmDialog = builder.show();
    }

}
