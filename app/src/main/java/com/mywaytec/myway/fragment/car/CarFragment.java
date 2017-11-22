package com.mywaytec.myway.fragment.car;


import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseFragment;
import com.mywaytec.myway.base.Constant;
import com.mywaytec.myway.base.MqttService;
import com.mywaytec.myway.model.bean.BleInfoBean;
import com.mywaytec.myway.model.bean.ObjStringBean;
import com.mywaytec.myway.ui.bluetooth.BluetoothActivity;
import com.mywaytec.myway.ui.firmwareUpdate.FirmwareUpdateActivity;
import com.mywaytec.myway.ui.moreCarInfo.MoreCarInfoActivity;
import com.mywaytec.myway.ui.scFirmwareUpdate.ScFirmwareUpdateActivity;
import com.mywaytec.myway.utils.BleKitUtils;
import com.mywaytec.myway.utils.BleUtil;
import com.mywaytec.myway.utils.ConversionUtil;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.SystemUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.utils.data.BleInfo;
import com.mywaytec.myway.view.CommonSubscriber;
import com.mywaytec.myway.view.EmptyLayout;

import java.util.List;
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
 *
 * 首先在该页面获取车辆所有状态，如果由于蓝牙通信质量未获取到，跳转到更多设置界面再次获取；
 * 获取车辆SN码后，判断是否为车主，若不是车主，不能进行一下操作：重置蓝牙密码、指纹录制、更改蓝牙密码、油门校准或水平校准、固件升级；
 *
 *
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

        mPresenter.sendZhiling();
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

    int zongLicheng;
    StringBuilder snCode1;
    StringBuilder snCode2;
    //处理蓝牙设备反馈指令
    private void displayData(byte[] data) {
        if (data != null) {
//            if (!BleUtil.receiveDataCRCCheck(data)) {
//                return;
//            }
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
//                    Log.i("TAG", "-----info" + speed + "," + power + "," + licheng + "," + zongLicheng);
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

                    if (!TextUtils.isEmpty(snCode1) && !TextUtils.isEmpty(snCode2)) {
                        String snCodeStr = snCode1.toString() + snCode2.toString();
                        if (snCodeStr.length() == 24) {
                            BleInfoBean bleInfoBean = BleInfo.getBleInfo();
                            bleInfoBean.setSnCode(snCodeStr);
                            BleInfo.saveBleInfo(bleInfoBean);
                            mPresenter.useCar(snCodeStr, zongLicheng * 100);
                            if (SystemUtil.isNetworkConnected()) {
                                Log.i("TAG", "------判断车主，" + snCodeStr);
                                String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
                                mPresenter.getRetrofitHelper().vehicleUserOwner(uid, snCodeStr)
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
                //查询车辆是否在引导程序中
                else if ("04".equals(infos[2]) && "01".equals(infos[3]) && "07".equals(infos[4])) {
                    Log.i("TAG", "------是否在引导程序," + infos[6]);
                    BleInfoBean bleInfoBean = BleInfo.getBleInfo();
                    if ("01".equals(infos[6])) {//引导程序中
                        bleInfoBean.setProgramLocation("01");
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
                        bleInfoBean.setProgramLocation("00");
                    }
                    BleInfo.saveBleInfo(bleInfoBean);
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
            //如果设备不为null，显示设备名称，若为null，显示MYWAY
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
            mPresenter.sendSpeedRequest();
        } else {
            tvConnection.setText(R.string.connected);
//            tvSpeed.setText("0.0");
//            tvLicheng.setText("0.0");
//            tvDianliang.setText("0");
//            tvZonglicheng.setText("0.0");
            mPresenter.stopSendSpeedRequest();
            BleInfoBean bleInfoBean = BleInfo.getBleInfo();
            bleInfoBean.setProgramLocation("");
            BleInfo.saveBleInfo(bleInfoBean);
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
    public void onPause() {
        super.onPause();
        mPresenter.stopSendSpeedRequest();
        getActivity().unregisterReceiver(mMqttUpdateReceiver);
        BleKitUtils.getBluetoothClient().unregisterConnectStatusListener(mDeviceAddress, mBleConnectStatusListener);
        if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
            BleKitUtils.unnotifyP(mDeviceAddress, new BleUnnotifyResponse() {
                @Override
                public void onResponse(int code) {
                }
            });
        } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
            BleKitUtils.unnotifyTaiTou(mDeviceAddress, new BleUnnotifyResponse() {
                @Override
                public void onResponse(int code) {
                }
            });
        }
    }

    private final BleConnectStatusListener mBleConnectStatusListener = new BleConnectStatusListener() {

        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if (status == STATUS_CONNECTED) {
                tvConnection.setText(R.string.disconnect);
            } else if (status == STATUS_DISCONNECTED) {
                tvConnection.setText(R.string.connected);
//                tvSpeed.setText("0.0");
//                tvLicheng.setText("0.0");
//                tvDianliang.setText("0");
//                tvZonglicheng.setText("0.0");
                mPresenter.stopSendSpeedRequest();
//                BleKitUtils.getBluetoothClient().
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
                                    //如果更新连接车辆，将车辆缓存信息清空
                                    if (!mDeviceAddress.equals(BleInfo.getBleInfo().getMac())){
                                        BleInfo.clearBleInfo();
                                        BleInfoBean bleInfoBean = BleInfo.getBleInfo();
                                        bleInfoBean.setMac(mDeviceAddress);
                                        bleInfoBean.setIsChezhu("");
                                        BleInfo.saveBleInfo(bleInfoBean);
                                    }
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
                mPresenter.stopSendSpeedRequest();
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

    @Override
    public String getDeviceAddress() {
        return mDeviceAddress;
    }
}
