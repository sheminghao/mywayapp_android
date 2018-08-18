package com.mywaytec.myway.ui.moreCarInfo;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luck.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.luck.bluetooth.library.connect.response.BleNotifyResponse;
import com.luck.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.luck.bluetooth.library.connect.response.BleWriteResponse;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.base.Constant;
import com.mywaytec.myway.model.bean.BleInfoBean;
import com.mywaytec.myway.model.bean.FirmwareInfo;
import com.mywaytec.myway.ui.faultDetect.FaultDetectActivity;
import com.mywaytec.myway.ui.fingerMark.FingerMarkActivity;
import com.mywaytec.myway.ui.firmwareUpdate.raFirmwareUpdate.RAFirmwareUpdateActivity;
import com.mywaytec.myway.ui.firmwareUpdate.sc03FirmwareUpdate.Sc03FirmwareUpdateActivity;
import com.mywaytec.myway.ui.firmwareUpdate.scFirmwareUpdate.ScFirmwareUpdateActivity;
import com.mywaytec.myway.utils.BleKitUtils;
import com.mywaytec.myway.utils.BleUtil;
import com.mywaytec.myway.utils.CleanMessageUtil;
import com.mywaytec.myway.utils.ConversionUtil;
import com.mywaytec.myway.utils.DownloadFileUtil;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxCountDown;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.utils.data.BleInfo;
import com.mywaytec.myway.view.CommonSubscriber;
import com.mywaytec.myway.view.SpeedSeekBar;

import java.io.File;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

import static com.luck.bluetooth.library.Constants.REQUEST_FAILED;
import static com.luck.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.luck.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.luck.bluetooth.library.Constants.STATUS_DEVICE_CONNECTED;
import static com.luck.bluetooth.library.Constants.STATUS_DEVICE_DISCONNECTED;
import static com.luck.bluetooth.library.Constants.STATUS_DISCONNECTED;

/**
 * 蓝牙更多设置界面
 */
public class MoreCarInfoActivity extends BaseActivity<MoreCarInfoPresenter> implements MoreCarInfoView {

    @BindView(R.id.tv_title)
    TextView tvTitle;
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
    @BindView(R.id.img_qiandeng)
    ImageView imgQiandeng;
    @BindView(R.id.img_weideng)
    ImageView imgWeideng;
    @BindView(R.id.img_huaxing)
    ImageView imgHuaxing;
    @BindView(R.id.img_dingsuxunhang)
    ImageView imgDingsuxunhang;
    @BindView(R.id.tv_dengdai)
    TextView tvDengdai;

    //前灯是否打开
    private boolean isQiandeng;
    //后灯是否打开
    private boolean isHoudeng;
    //滑行启动是否打开
    private boolean isHuaxing;
    //定速巡航时候打开
    private boolean isDingsuxunhang;

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
        uuid = PreferencesUtils.getString(MoreCarInfoActivity.this, "uuid");
        if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
            layoutYoumenjiaozhun.setVisibility(View.VISIBLE);
            layoutCheshenjiaozhun.setVisibility(View.GONE);
            layoutScSet.setVisibility(View.VISIBLE);
            layoutRaSet.setVisibility(View.GONE);
            layoutFingerMark.setVisibility(View.VISIBLE);
            layoutChangeBlePassword.setVisibility(View.VISIBLE);
            layoutDengdaimoshi.setVisibility(View.VISIBLE);
        } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
            layoutYoumenjiaozhun.setVisibility(View.GONE);
            layoutCheshenjiaozhun.setVisibility(View.VISIBLE);
            layoutScSet.setVisibility(View.GONE);
            layoutRaSet.setVisibility(View.VISIBLE);
            layoutFingerMark.setVisibility(View.GONE);
            layoutChangeBlePassword.setVisibility(View.GONE);
            layoutDengdaimoshi.setVisibility(View.GONE);
        }

        //蓝牙notify
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

        //设置车辆限速值
        speedSeekBar.setOnProgressChange(new SpeedSeekBar.OnProgressChange() {
            @Override
            public void progressChange(int progress) {
                SystemClock.sleep(80);
                if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                    BleKitUtils.writeP(mDeviceAddress, BleUtil.getSpeedLimit(progress), new BleWriteResponse() {
                        @Override
                        public void onResponse(int code) {

                        }
                    });
                } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                    BleKitUtils.writeTaiTou(mDeviceAddress, BleUtil.getSpeedLimit(progress), new BleWriteResponse() {
                        @Override
                        public void onResponse(int code) {

                        }
                    });
                }
            }
        });

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (BleUtil.isChezhu(mPresenter.getRetrofitHelper())) {
            layoutChangeBlePassword.setVisibility(View.VISIBLE);
        }else {
            layoutChangeBlePassword.setVisibility(View.GONE);
        }

        mPresenter.initSetting();

        //初始化车辆状态
        if (null != BleInfo.getBleInfo().getCarState() && BleInfo.getBleInfo().getCarState().length >= 9){
            byte[] data = BleInfo.getBleInfo().getCarState();
            if (((data[9] >> 0) & 0x1) == 1) {//前灯状态
                isQiandeng = true;
                imgQiandeng.setImageResource(R.mipmap.gengduo_btn_queding);
            } else {
                isQiandeng = false;
                imgQiandeng.setImageResource(R.mipmap.gengduo_btn_qixuao);
            }
            if (((data[9] >> 6) & 0x1) == 0) {//尾灯状态
                isHoudeng = true;
                imgWeideng.setImageResource(R.mipmap.gengduo_btn_queding);
            } else {
                isHoudeng = false;
                imgWeideng.setImageResource(R.mipmap.gengduo_btn_qixuao);
            }
            if (((data[9] >> 4) & 0x1) == 0) {//车辆模式
                tvMode.setText(R.string.econ);
            } else {
                tvMode.setText(R.string.sport);
            }
            if (((data[9] >> 3) & 0x1) == 1){//滑行启动状态
                isHuaxing = true;
                imgHuaxing.setImageResource(R.mipmap.gengduo_btn_queding);
            }else {
                isHuaxing = false;
                imgHuaxing.setImageResource(R.mipmap.gengduo_btn_qixuao);
            }
            if (((data[9] >> 5) & 0x1) == 1){//定速巡航状态
                isDingsuxunhang = true;
                imgDingsuxunhang.setImageResource(R.mipmap.gengduo_btn_queding);
            }else {
                isDingsuxunhang = false;
                imgDingsuxunhang.setImageResource(R.mipmap.gengduo_btn_qixuao);
            }

        }

        if (BleInfo.getBleInfo().getSuduxianzhi() > 0) {
            //如果有缓存数据，初始化数据
            speedSeekBar.setPracticalSpeed(BleInfo.getBleInfo().getSuduxianzhi());
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                fasongZhiling();
            }
        }).start();
    }

    @Override
    protected void updateViews() {

    }

//    LoadingDialog loadingDialog;
    //发送指令超时时间
    private int sendTime;
    private void fasongZhiling() {
            Log.i("TAG", "------查询软件版本号");
            //查询软件版本号
            if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                BleKitUtils.writeP(mDeviceAddress, Constant.BLE.SOFTWARE_VERSION_CODE, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        if (code == REQUEST_SUCCESS) {
                            sendTime = 0;
                            allCarData();
                        } else if (code == REQUEST_FAILED) {
                            if (sendTime > 10) {
                                return;
                            }
                            sendTime++;
                            fasongZhiling();
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
                                return;
                            }
                            sendTime++;
                            fasongZhiling();
                        }
                    }
                });
            }
    }

    private void allCarData() {

        SystemClock.sleep(200);
        Log.i("TAG", "------查询整车数据");
        if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
            BleKitUtils.writeP(mDeviceAddress, Constant.BLE.ALL_CAR_DATA, new BleWriteResponse() {
                @Override
                public void onResponse(int code) {
                    if (code == REQUEST_SUCCESS) {
                        sendTime = 0;
                        snCode();
                    } else if (code == REQUEST_FAILED) {
                        if (sendTime > 10) {
                            return;
                        }
                        sendTime++;
                        allCarData();
                    }
                }
            });
        }
    }

    //车辆最高/低限速值
    private void speedLimitValues(){
        if (BleInfo.getBleInfo().getHighSpeed() == 0) {
            SystemClock.sleep(80);
            //车辆最高/低限速值
            if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                Log.i("TAG", "------发送车辆最高/低限速值指令");
                BleKitUtils.writeP(mDeviceAddress, Constant.BLE.SPEED_LIMIT_VALUES, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        if (code == REQUEST_SUCCESS) {
                            sendTime = 0;
                            carState();
                        } else if (code == REQUEST_FAILED) {
                            if (sendTime > 10) {
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
                                return;
                            }
                            sendTime++;
                            speedLimitValues();
                        }
                    }
                });
            }
        }else {
            speedSeekBar.setHighSpeed(BleInfo.getBleInfo().getHighSpeed(), BleInfo.getBleInfo().getLowSpeed());
            speedSeekBar.setLowSpeed(BleInfo.getBleInfo().getHighSpeed(), BleInfo.getBleInfo().getLowSpeed());
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
        if (false) {
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
                            programLocation();
                        } else if (code == REQUEST_FAILED) {
                            if (sendTime > 10) {
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
                            programLocation();
                        } else if (code == REQUEST_FAILED) {
                            if (sendTime > 10) {
                                return;
                            }
                            sendTime++;
                            practicalLimitValues();
                        }
                    }
                });
            }
        }else {
            //如果有缓存数据，初始化数据
            speedSeekBar.setPracticalSpeed(BleInfo.getBleInfo().getSuduxianzhi());
            programLocation();
        }
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
                            currentDengdaiMode();
                        } else if (code == REQUEST_FAILED) {
                            if (sendTime > 10) {
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
                        } else if (code == REQUEST_FAILED) {
                            if (sendTime > 10) {
                                return;
                            }
                            sendTime++;
                            snCode();
                        }
                    }
                });
            }
        }else {
            currentDengdaiMode();
        }
    }

    private void currentDengdaiMode(){
        if(BleInfo.getBleInfo().getDengdaimoshi() == 0) {
            SystemClock.sleep(80);
            //氛围灯模式
            if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                BleKitUtils.writeP(mDeviceAddress, Constant.BLE.CURRENT_DENGDAI_MODE, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        if (code == REQUEST_SUCCESS) {
                            sendTime = 0;
                        } else if (code == REQUEST_FAILED) {
                            if (sendTime > 10) {
                              return;
                            }
                            sendTime++;
                            currentDengdaiMode();
                        }
                    }
                });
            }
        }else {
        }
    }


    //蓝牙设备返回信息
    private void displayData(byte[] data) {
        if (data != null) {
//            if (!BleUtil.receiveDataCRCCheck(data)) {
//                return;
//            }
            mPresenter.displayData(data);
            String info = ConversionUtil.byte2HexStr(data);
            Log.i("TAG", "-----MoreCarInfo_info" + info);
            String[] infos = info.split(" ");
//            Log.i("TAG", "-----MoreCarInfo_infos" + infos.length);
            if (infos.length > 5) {
                //前灯关闭应答
                if ("04".equals(infos[2]) && "02".equals(infos[3]) && "01".equals(infos[4])) {
                    if ("01".equals(infos[6])) {
                        isQiandeng = false;
                        imgQiandeng.setImageResource(R.mipmap.gengduo_btn_qixuao);
                    } else if ("00".equals(infos[6])) {
//                        ToastUtils.showToast("设置失败");
                    }
                }
                //前灯打开应答
                else if ("04".equals(infos[2]) && "02".equals(infos[3]) && "02".equals(infos[4])) {
                    if ("01".equals(infos[6])) {
                        isQiandeng = true;
                        imgQiandeng.setImageResource(R.mipmap.gengduo_btn_queding);
                    } else if ("00".equals(infos[6])) {
                    }
                }
                //后灯关闭应答
                else if ("04".equals(infos[2]) && "02".equals(infos[3]) && "0E".equals(infos[4])) {
                    if ("01".equals(infos[6])) {
                        isHoudeng = false;
                        imgWeideng.setImageResource(R.mipmap.gengduo_btn_qixuao);
                    } else if ("00".equals(infos[6])) {
                    }
                }
                //后灯打开应答
                else if ("04".equals(infos[2]) && "02".equals(infos[3]) && "0F".equals(infos[4])) {
                    if ("01".equals(infos[6])) {
                        isHoudeng = true;
                        imgWeideng.setImageResource(R.mipmap.gengduo_btn_queding);
                    } else if ("00".equals(infos[6])) {
                    }
                }
                //打开定速巡航应答
                else if ("04".equals(infos[2]) && "02".equals(infos[3]) && "0B".equals(infos[4])) {
                    if ("01".equals(infos[6])) {//成功
                        isDingsuxunhang = true;
                        imgDingsuxunhang.setImageResource(R.mipmap.gengduo_btn_queding);
                    } else if ("00".equals(infos[6])) {//失败
                    }
                }
                //关闭定速巡航应答
                else if ("04".equals(infos[2]) && "02".equals(infos[3]) && "0A".equals(infos[4])) {
                    if ("01".equals(infos[6])) {//成功
                        isDingsuxunhang = false;
                        imgDingsuxunhang.setImageResource(R.mipmap.gengduo_btn_qixuao);
                    } else if ("00".equals(infos[6])) {//失败
                    }
                }
                //打开滑行启动
                else if ("04".equals(infos[2]) && "02".equals(infos[3]) && "07".equals(infos[4])) {
                    if ("01".equals(infos[6])) {//成功
                        isHuaxing = true;
                        imgHuaxing.setImageResource(R.mipmap.gengduo_btn_queding);
                    } else if ("00".equals(infos[6])) {//失败
                    }
                }
                //关闭滑行启动
                else if ("04".equals(infos[2]) && "02".equals(infos[3]) && "06".equals(infos[4])) {
                    if ("01".equals(infos[6])) {//成功
                        isHuaxing = false;
                        imgHuaxing.setImageResource(R.mipmap.gengduo_btn_qixuao);
                    } else if ("00".equals(infos[6])) {//失败
                    }
                }
                //车辆限速应答
                else if ("04".equals(infos[2]) && "02".equals(infos[3]) && "05".equals(infos[4])) {
                    Log.i("TAG", "------车速限制反馈," + infos[6]);
                    int practicalSpeed = Integer.parseInt(infos[6], 16);
                    BleInfoBean bleInfoBean = BleInfo.getBleInfo();
                    bleInfoBean.setSuduxianzhi(practicalSpeed);
                    BleInfo.saveBleInfo(bleInfoBean);
                }
                //软件版本号
                else if ("04".equals(infos[2]) && "01".equals(infos[3]) && "05".equals(infos[4])) {
                    Log.i("TAG", "------软件版本号info," + info);
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
                    BleInfoBean bleInfoBean = BleInfo.getBleInfo();
                    bleInfoBean.setRuanjianCode(firmwareCode);
                    BleInfo.saveBleInfo(bleInfoBean);
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
                    }
                } else if ("04".equals(infos[2]) && "02".equals(infos[3]) && "09".equals(infos[4])) {//运动模式切换
                    Log.i("TAG", "------运动模式切换反馈," + infos[6]);
                    if ("01".equals(infos[6])) {//设置成功
                        tvMode.setText(R.string.sport);
                    } else if ("00".equals(infos[6])) {//设置失败
                    }
                } else if ("04".equals(infos[2]) && "01".equals(infos[3]) && "08".equals(infos[4])) {//最高限速、最低限速
                    int highSpeed = Integer.parseInt(infos[6], 16);
                    int lowSpeed = Integer.parseInt(infos[7], 16);
                    Log.i("TAG", "------最高限速、最低限速反馈,"+highSpeed+"--"+lowSpeed);
                    speedSeekBar.setHighSpeed(highSpeed, lowSpeed);
                    speedSeekBar.setLowSpeed(highSpeed, lowSpeed);
                    BleInfoBean bleInfoBean = BleInfo.getBleInfo();
                    bleInfoBean.setHighSpeed(highSpeed);
                    bleInfoBean.setLowSpeed(lowSpeed);
                    BleInfo.saveBleInfo(bleInfoBean);
                } else if ("04".equals(infos[2]) && "01".equals(infos[3]) && "04".equals(infos[4])) {//车辆实际限速值
                    int practicalSpeed = Integer.parseInt(infos[6], 16);
                    Log.i("TAG", "------车辆实际限速值,"+practicalSpeed);
                    speedSeekBar.setPracticalSpeed(practicalSpeed);
                    BleInfoBean bleInfoBean = BleInfo.getBleInfo();
                    bleInfoBean.setSuduxianzhi(practicalSpeed);
                    BleInfo.saveBleInfo(bleInfoBean);
                } else if ("04".equals(infos[2]) && "01".equals(infos[3]) && "02".equals(infos[4])) {
                    String info1 = ConversionUtil.byte2HexStr(data);
                    Log.i("TAG", "-----车辆状态," + info1);
                    String b6 = Integer.toBinaryString(Integer.parseInt(infos[9], 16));
                    int a = (data[9] >> 4) & 0x1;
                    Log.i("TAG", "-----车辆状态,b6" + b6 + "," + a);
                    Log.i("TAG", "-----车辆状态,前灯"+((data[9] >> 0) & 0x1)+",尾灯,"+((data[9] >> 6) & 0x1)
                            +",滑行启动,"+((data[9] >> 3) & 0x1) +",定速巡航,"+((data[9] >> 5) & 0x1));
                    if (((data[9] >> 0) & 0x1) == 1) {//前灯状态
                        isQiandeng = true;
                        imgQiandeng.setImageResource(R.mipmap.gengduo_btn_queding);
                    } else {
                        isQiandeng = false;
                        imgQiandeng.setImageResource(R.mipmap.gengduo_btn_qixuao);
                    }
                    if (((data[9] >> 6) & 0x1) == 0) {//尾灯状态
                        isHoudeng = true;
                        imgWeideng.setImageResource(R.mipmap.gengduo_btn_queding);
                    } else {
                        isHoudeng = false;
                        imgWeideng.setImageResource(R.mipmap.gengduo_btn_qixuao);
                    }
                    if (((data[9] >> 4) & 0x1) == 0) {//车辆模式
                        tvMode.setText(R.string.econ);
                    } else {
                        tvMode.setText(R.string.sport);
                    }
                    if (((data[9] >> 3) & 0x1) == 1){//滑行启动状态
                        isHuaxing = true;
                        imgHuaxing.setImageResource(R.mipmap.gengduo_btn_queding);
                    }else {
                        isHuaxing = false;
                        imgHuaxing.setImageResource(R.mipmap.gengduo_btn_qixuao);
                    }
                    if (((data[9] >> 5) & 0x1) == 1){//定速巡航状态
                        isDingsuxunhang = true;
                        imgDingsuxunhang.setImageResource(R.mipmap.gengduo_btn_queding);
                    }else {
                        isDingsuxunhang = false;
                        imgDingsuxunhang.setImageResource(R.mipmap.gengduo_btn_qixuao);
                    }
                    BleInfoBean bleInfoBean = BleInfo.getBleInfo();
                    bleInfoBean.setCarState(data);
                    BleInfo.saveBleInfo(bleInfoBean);
                }
                //固件更新
                else if ("04".equals(infos[2]) && "03".equals(infos[3]) && "03".equals(infos[4])) {
                    Log.i("TAG", "------固件更新反馈," + infos[6]);
                    if ("01".equals(infos[6])) {//操作成功
                        if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                            Intent intent = new Intent();
                            String bleName = BleUtil.getBleName();
                            if (!TextUtils.isEmpty(bleName)) {
                                Log.i("TAG", "------bleName.substring(0, 3)," + bleName.substring(0, 3));
                                if ("SC3".equals(bleName.substring(0, 3))) {
                                    Log.i("TAG", "------SC03跳转");
                                    intent.setClass(MoreCarInfoActivity.this, Sc03FirmwareUpdateActivity.class);
                                }else {
                                    intent.setClass(MoreCarInfoActivity.this, ScFirmwareUpdateActivity.class);
                                }
                            }
                            intent.putExtra("firmwareCode", firmwareCode);
                            intent.putExtra("firmwareUpdataInfo", firmwareUpdataInfo);
                            intent.putExtra("mDeviceAddress", mDeviceAddress);
                            startActivity(intent);
                        } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                            Intent intent = new Intent(MoreCarInfoActivity.this, RAFirmwareUpdateActivity.class);
                            intent.putExtra("firmwareCode", firmwareCode);
                            intent.putExtra("firmwareUpdataInfo", firmwareUpdataInfo);
                            intent.putExtra("mDeviceAddress", mDeviceAddress);
                            startActivity(intent);
                        }
                        if (firmwareUpdatePopupWindow != null && firmwareUpdatePopupWindow.isShowing()) {
                            firmwareUpdatePopupWindow.dismiss();
                        }
                    } else if ("00".equals(infos[6])){//车辆非静止，禁止操作
                        //TODO 中英文
                        ToastUtils.showToast("车辆非静止，禁止操作");
                    } else if ("02".equals(infos[6])){//正在充电，禁止操作
                        ToastUtils.showToast("正在充电，禁止操作");
                    }
                } else if ("04".equals(infos[2]) && "01".equals(infos[3]) && "07".equals(infos[4])) {
                    BleInfoBean bleInfoBean = BleInfo.getBleInfo();
                    if ("01".equals(infos[6])) {//引导程序中
                        bleInfoBean.setProgramLocation("01");
                        if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                            Intent intent = new Intent();
                            String bleName = BleUtil.getBleName();
                            if (!TextUtils.isEmpty(bleName)) {
                                if ("SC3".equals(bleName.substring(0, 3))) {
                                    intent.setClass(MoreCarInfoActivity.this, Sc03FirmwareUpdateActivity.class);

                                }else {
                                    intent.setClass(MoreCarInfoActivity.this, ScFirmwareUpdateActivity.class);
                                }
                            }
                            intent.putExtra("firmwareCode", firmwareCode);
                            intent.putExtra("firmwareUpdataInfo", firmwareUpdataInfo);
                            intent.putExtra("mDeviceAddress", mDeviceAddress);
                            startActivity(intent);
                        } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                            Intent intent = new Intent(MoreCarInfoActivity.this, RAFirmwareUpdateActivity.class);
                            intent.putExtra("firmwareCode", firmwareCode);
                            intent.putExtra("firmwareUpdataInfo", firmwareUpdataInfo);
                            intent.putExtra("mDeviceAddress", mDeviceAddress);
                            startActivity(intent);
                        }
                    } else if ("00".equals(infos[6])) {//应用程序中
                        bleInfoBean.setProgramLocation("00");
                    }
                    BleInfo.saveBleInfo(bleInfoBean);
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
                //整车数据
                else if ("04".equals(infos[2]) && "01".equals(infos[3]) && "0A".equals(infos[4])
                        || !("4D".equals(infos[0]) && "57".equals(infos[1]))){
                    if (infos.length == 20) {
                        allCar = new StringBuffer();
                        allCar.append(info);
                        Log.i("TAG", "-----整车数据1, " + allCar.toString());

                        Log.i("TAG", "-----车辆状态,前灯"+((data[9] >> 0) & 0x1)+",尾灯,"+((data[9] >> 6) & 0x1)
                                +",滑行启动,"+((data[9] >> 3) & 0x1) +",定速巡航,"+((data[9] >> 5) & 0x1));
                        if (((data[9] >> 0) & 0x1) == 1) {//前灯状态
                            isQiandeng = true;
                            imgQiandeng.setImageResource(R.mipmap.gengduo_btn_queding);
                        } else {
                            isQiandeng = false;
                            imgQiandeng.setImageResource(R.mipmap.gengduo_btn_qixuao);
                        }
                        if (((data[9] >> 6) & 0x1) == 0) {//尾灯状态
                            isHoudeng = true;
                            imgWeideng.setImageResource(R.mipmap.gengduo_btn_queding);
                        } else {
                            isHoudeng = false;
                            imgWeideng.setImageResource(R.mipmap.gengduo_btn_qixuao);
                        }
                        if (((data[9] >> 4) & 0x1) == 0) {//车辆模式
                            tvMode.setText(R.string.econ);
                        } else {
                            tvMode.setText(R.string.sport);
                        }
                        if (((data[9] >> 3) & 0x1) == 1){//滑行启动状态
                            isHuaxing = true;
                            imgHuaxing.setImageResource(R.mipmap.gengduo_btn_queding);
                        }else {
                            isHuaxing = false;
                            imgHuaxing.setImageResource(R.mipmap.gengduo_btn_qixuao);
                        }
                        if (((data[9] >> 5) & 0x1) == 1){//定速巡航状态
                            isDingsuxunhang = true;
                            imgDingsuxunhang.setImageResource(R.mipmap.gengduo_btn_queding);
                        }else {
                            isDingsuxunhang = false;
                            imgDingsuxunhang.setImageResource(R.mipmap.gengduo_btn_qixuao);
                        }
                        BleInfoBean bleInfoBean = BleInfo.getBleInfo();
                        bleInfoBean.setCarState(data);
                        BleInfo.saveBleInfo(bleInfoBean);
                    }else if (infos.length == 5){
                        allCar.append(" "+info);
                    }
                }
            }else {
                if (!("4D".equals(infos[0]) && "57".equals(infos[1]))){
                    if (infos.length == 5) {
                        allCar.append(" " + info);
                        String[] allCarData = allCar.toString().split(" ");
                        BleInfoBean bleInfoBean = BleInfo.getBleInfo();
                        //车辆实际限速值
                        int practicalSpeed = Integer.parseInt(allCarData[10], 16);
                        Log.i("TAG", "------车辆实际限速值,"+practicalSpeed);
                        speedSeekBar.setPracticalSpeed(practicalSpeed);
                        bleInfoBean.setSuduxianzhi(practicalSpeed);

                        StringBuilder code = new StringBuilder();
                        char code1 = (char) Integer.parseInt(allCarData[18], 16);
                        code.append(code1);
                        char code2 = (char) Integer.parseInt(allCarData[17], 16);
                        code.append(code2);
                        char code3 = (char) Integer.parseInt(allCarData[16], 16);
                        code.append(code3);
                        char code4 = (char) Integer.parseInt(allCarData[15], 16);
                        code.append(code4);
                        char code5 = (char) Integer.parseInt(allCarData[14], 16);
                        code.append(code5);
                        char code6 = (char) Integer.parseInt(allCarData[13], 16);
                        code.append(code6);
                        char code7 = (char) Integer.parseInt(allCarData[12], 16);
                        code.append(code7);
                        char code8 = (char) Integer.parseInt(allCarData[11], 16);
                        code.append(code8);
                        String yingJianCode = code.toString();
                        Log.i("TAG", "------硬件版本号， " + yingJianCode);
                        tvFirmwareCode.setText(yingJianCode);


                        if ("01".equals(allCarData[19])) {//引导程序中
                            bleInfoBean.setProgramLocation("01");
                            if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                                Intent intent = new Intent();
                                String bleName = BleUtil.getBleName();
                                if (!TextUtils.isEmpty(bleName)) {
                                    if ("SC3".equals(bleName.substring(0, 3))) {
                                        intent.setClass(MoreCarInfoActivity.this, Sc03FirmwareUpdateActivity.class);

                                    }else {
                                        intent.setClass(MoreCarInfoActivity.this, ScFirmwareUpdateActivity.class);
                                    }
                                }
                                intent.putExtra("firmwareCode", firmwareCode);
                                intent.putExtra("firmwareUpdataInfo", firmwareUpdataInfo);
                                intent.putExtra("mDeviceAddress", mDeviceAddress);
                                startActivity(intent);
                            } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                                Intent intent = new Intent(MoreCarInfoActivity.this, RAFirmwareUpdateActivity.class);
                                intent.putExtra("firmwareCode", firmwareCode);
                                intent.putExtra("firmwareUpdataInfo", firmwareUpdataInfo);
                                intent.putExtra("mDeviceAddress", mDeviceAddress);
                                startActivity(intent);
                            }
                        } else if ("00".equals(allCarData[19])) {//应用程序中
                            bleInfoBean.setProgramLocation("00");
                        }

                        //最高限速、最低限速
                        int highSpeed = Integer.parseInt(allCarData[20], 16);
                        int lowSpeed = Integer.parseInt(allCarData[21], 16);
                        speedSeekBar.setHighSpeed(highSpeed, lowSpeed);
                        speedSeekBar.setLowSpeed(highSpeed, lowSpeed);
                        bleInfoBean.setHighSpeed(highSpeed);
                        bleInfoBean.setLowSpeed(lowSpeed);
                        BleInfo.saveBleInfo(bleInfoBean);
                    }
                }
            }
        }
    }

    StringBuffer allCar = new StringBuffer();

    @OnClick({R.id.layout_fault_detect, R.id.layout_firmware_update, R.id.layout_change_ble_password,
            R.id.layout_finger_mark, R.id.layout_cheshenjiaozhun, R.id.layout_moshiqiehuan,
            R.id.layout_youmenjiaozhun, R.id.layout_dengdaimoshi, R.id.img_qiandeng, R.id.img_weideng,
            R.id.img_huaxing, R.id.img_dingsuxunhang})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_finger_mark://指纹管理
                if (null != BleUtil.getBleName() && BleUtil.getBleName().length()>3) {
                    if (!"SC1".equals(BleUtil.getBleName().substring(0, 3))) {
                        if (BleUtil.isChezhu(mPresenter.getRetrofitHelper())) {
                            Intent intent = new Intent(MoreCarInfoActivity.this, FingerMarkActivity.class);
                            intent.putExtra("mDeviceAddress", mDeviceAddress);
                            startActivity(intent);
                        } else {
                            //TODO 非车主操作提示（中英文）
                            ToastUtils.showToast("非车主不可操作");
                        }
                    }else {
                        //TODO 经济版无指纹提示（中英文）
                        ToastUtils.showToast("经济版无此功能");
                    }
                }
                break;
            case R.id.layout_fault_detect://故障检测
                Intent intent2 = new Intent(MoreCarInfoActivity.this, FaultDetectActivity.class);
                intent2.putExtra("mDeviceAddress", mDeviceAddress);
                startActivity(intent2);
                break;
            case R.id.layout_firmware_update://固件升级
                if (BleKitUtils.getBluetoothClient().getConnectStatus(mDeviceAddress) == STATUS_DEVICE_CONNECTED) {
                    //TODO 固件升级测试
                    if (BleUtil.isChezhu(mPresenter.getRetrofitHelper())) {
                        checkVersion();
                    }else {
                        //TODO 固件升级非车主操作提示（中英文）
                        ToastUtils.showToast("非车主不可操作");
                    }
                } else {
                    ToastUtils.showToast(R.string.please_firstly_connect_your_vehicle);
                }
                break;
            case R.id.layout_change_ble_password://修改密码
                if (BleUtil.isChezhu(mPresenter.getRetrofitHelper())) {
                    mPresenter.changeBlePasswordDialog(this, mDeviceAddress);
                }else {
                    //TODO 非车主操作提示（中英文）
                    ToastUtils.showToast("非车主不可操作");
                }
                break;
            case R.id.layout_cheshenjiaozhun://车身校准
                if (BleUtil.isChezhu(mPresenter.getRetrofitHelper())) {
                    showHorizontalAlignmentPop(findViewById(R.id.layout_cheshenjiaozhun));
                }else {
                    //TODO 非车主操作提示（中英文）
                    ToastUtils.showToast("非车主不可操作");
                }
                break;
            case R.id.layout_moshiqiehuan://模式切换
                String uuid3 = PreferencesUtils.getString(MoreCarInfoActivity.this, "uuid");
                if (getResources().getString(R.string.econ).equals(tvMode.getText().toString())) {//标准模式切换到运动模式
                    if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid3)) {
                        BleKitUtils.writeP(mDeviceAddress, Constant.BLE.MODE_SWITCH_SPORTS, new BleWriteResponse() {
                            @Override
                            public void onResponse(int code) {
                            }
                        });
                    } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid3)) {
                        BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.MODE_SWITCH_SPORTS, new BleWriteResponse() {
                            @Override
                            public void onResponse(int code) {
                            }
                        });
                    }
                } else if (getResources().getString(R.string.sport).equals(tvMode.getText().toString())) {//运动模式切换到标准模式
                    if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid3)) {
                        BleKitUtils.writeP(mDeviceAddress, Constant.BLE.MODE_SWITCH_ENERGY_SAVING, new BleWriteResponse() {
                            @Override
                            public void onResponse(int code) {
                            }
                        });
                    } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid3)) {
                        BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.MODE_SWITCH_ENERGY_SAVING, new BleWriteResponse() {
                            @Override
                            public void onResponse(int code) {
                            }
                        });
                    }
                }
                break;
            case R.id.img_qiandeng://前灯
                if (!isQiandeng) {
                    if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                        BleKitUtils.writeP(mDeviceAddress, Constant.BLE.OPEN_LIGHT_FRONT, new BleWriteResponse() {
                            @Override
                            public void onResponse(int code) {
                            }
                        });
                    } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                        BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.OPEN_LIGHT_FRONT, new BleWriteResponse() {
                            @Override
                            public void onResponse(int code) {
                            }
                        });
                    }
                } else {
                    if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                        BleKitUtils.writeP(mDeviceAddress, Constant.BLE.CLOSE_LIGHT_FRONT, new BleWriteResponse() {
                            @Override
                            public void onResponse(int code) {
                            }
                        });
                    } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                        BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.CLOSE_LIGHT_FRONT, new BleWriteResponse() {
                            @Override
                            public void onResponse(int code) {
                            }
                        });
                    }
                }
                break;
            case R.id.img_weideng://尾灯
                if (!isHoudeng) {
                    if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                        BleKitUtils.writeP(mDeviceAddress, Constant.BLE.OPEN_LIGHT_BACK, new BleWriteResponse() {
                            @Override
                            public void onResponse(int code) {
                            }
                        });
                    } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                        BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.OPEN_LIGHT_BACK, new BleWriteResponse() {
                            @Override
                            public void onResponse(int code) {
                            }
                        });
                    }
                } else {
                    if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                        BleKitUtils.writeP(mDeviceAddress, Constant.BLE.CLOSE_LIGHT_BACK, new BleWriteResponse() {
                            @Override
                            public void onResponse(int code) {
                            }
                        });
                    } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                        BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.CLOSE_LIGHT_BACK, new BleWriteResponse() {
                            @Override
                            public void onResponse(int code) {
                            }
                        });

                    }
                }
                break;
            case R.id.img_huaxing://滑行启动
                if (!isHuaxing) {//开启滑行模式
                    if (null != Constant.BLE.OPEN_TAXI_START || Constant.BLE.OPEN_TAXI_START.length > 0) {
                        if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                            BleKitUtils.writeP(mDeviceAddress, Constant.BLE.OPEN_TAXI_START, new BleWriteResponse() {
                                @Override
                                public void onResponse(int code) {
                                }
                            });
                        } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                            BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.OPEN_TAXI_START, new BleWriteResponse() {
                                @Override
                                public void onResponse(int code) {
                                }
                            });
                        }
                    }
                } else {//关闭滑行模式
                    if (null != Constant.BLE.CLOSE_TAXI_START || Constant.BLE.CLOSE_TAXI_START.length > 0) {
                        if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
                            BleKitUtils.writeP(mDeviceAddress, Constant.BLE.CLOSE_TAXI_START, new BleWriteResponse() {
                                @Override
                                public void onResponse(int code) {
                                }
                            });
                        } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                            BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.CLOSE_TAXI_START, new BleWriteResponse() {
                                @Override
                                public void onResponse(int code) {
                                }
                            });
                        }
                    }
                }
                break;
            case R.id.img_dingsuxunhang://定速巡航
                if (!isDingsuxunhang) {//开启定速巡航
                    BleKitUtils.writeP(mDeviceAddress, Constant.BLE.OPEN_CONSTANT_SPEED, new BleWriteResponse() {
                        @Override
                        public void onResponse(int code) {
                        }
                    });
                } else {//关闭定速巡航
                    BleKitUtils.writeP(mDeviceAddress, Constant.BLE.CLOSE_CONSTANT_SPEED, new BleWriteResponse() {
                        @Override
                        public void onResponse(int code) {
                        }
                    });
                }
                break;
            case R.id.layout_dengdaimoshi://灯带模式
                if (null != BleUtil.getBleName() && BleUtil.getBleName().length()>3) {
                    if (!"SC1".equals(BleUtil.getBleName().substring(0, 3))) {
                        mPresenter.openDengdaiPopupWindow(findViewById(R.id.layout_dengdaimoshi), mDeviceAddress);
                    }else {
                        //TODO 经济版无指纹提示（中英文）
                        ToastUtils.showToast("经济版无此功能");
                    }
                }
                break;
            case R.id.layout_youmenjiaozhun://油门校准
                if (BleUtil.isChezhu(mPresenter.getRetrofitHelper())) {
                    mPresenter.showYoumenAlignmentPop(layoutYoumenjiaozhun, mDeviceAddress);
                }else {
                    //TODO 非车主操作提示（中英文）
                    ToastUtils.showToast("非车主不可操作");
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        BleKitUtils.getBluetoothClient().registerConnectStatusListener(mDeviceAddress, mBleConnectStatusListener);
        if (BleKitUtils.getBluetoothClient().getConnectStatus(mDeviceAddress) == STATUS_DEVICE_DISCONNECTED){
            finish();
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
            } else if (status == STATUS_DISCONNECTED) {
                finish();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
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
                if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {

                } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                    BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.HORIZONTAL_ALIGNMENT, new BleWriteResponse() {
                        @Override
                        public void onResponse(int code) {
                        }
                    });
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
                if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {

                } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                    BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.HORIZONTAL_ALIGNMENT_CONFIRM, new BleWriteResponse() {
                        @Override
                        public void onResponse(int code) {

                        }
                    });
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
                                if (null != popupWindow) {
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
                        tvProgress.setText((100 - 20 * integer) + "%");
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
                    BleKitUtils.writeP(mDeviceAddress, Constant.BLE.FIRMWARE_UPDATE, new BleWriteResponse() {
                        @Override
                        public void onResponse(int code) {
                        }
                    });
                } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
                    BleKitUtils.writeTaiTou(mDeviceAddress, Constant.BLE.FIRMWARE_UPDATE, new BleWriteResponse() {
                        @Override
                        public void onResponse(int code) {
                        }
                    });
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
                                if (carCode < firmwareInfo.getObj().getFirmwareVersion() ||
                                        "01".equals(PreferencesUtils.getString(
                                                MoreCarInfoActivity.this, "programLocation"))) {//不是最新版本，提示升级或是在引导程序中
                                    Log.i("TAG", "------固件信息，不是最新版本，提示升级");
                                    if ("RA".equals(firmwareCode.substring(0, 2)) && firmwareInfo.getObj().getFiles() != null
                                            && firmwareInfo.getObj().getFiles().size() > 0) {
                                        firmwareUpdataInfo = firmwareInfo.getObj().getDescription();
                                        String downloadUrl = firmwareInfo.getObj().getFiles().get(0).getUrl();
                                        downloadRANew(downloadUrl);
                                    } else if ("SC".equals(firmwareCode.substring(0, 2)) && firmwareInfo.getObj().getFiles() != null
                                            && firmwareInfo.getObj().getFiles().size() > 1) {
                                        firmwareUpdataInfo = firmwareInfo.getObj().getDescription();
                                        Log.i("TAG", "------固件更新信息firmwareUpdataInfo, " + firmwareUpdataInfo);
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
                                } else {
                                    Log.i("TAG", "------固件信息，是最新版本，不升级");
                                    ToastUtils.showToast(getResources().getString(R.string.the_current_firmware_version_is_already_latest));
                                }
                            } else {
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
    private void downloadSCNew(final String masterUrl, final String slave01Url, final String slave02Url) {
        Log.i("TAG", "------下载固件，" + masterUrl + "," + masterUrl);
        if (TextUtils.isEmpty(masterUrl) || TextUtils.isEmpty(slave01Url)) {
            return;
        }
        //清除残留固件，避免固件混淆
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
                        DownloadFileUtil.init(MoreCarInfoActivity.this).start(slave01Url, "slave01.bin").isInstall(false)
                                .setOnDownloadComplete(new DownloadFileUtil.OnDownloadComplete() {
                                    @Override
                                    public void downloadComplete(String path, File result) {
                                        Log.i("TAG", "------下载固件完成，" + path);
                                        if (TextUtils.isEmpty(slave02Url)){
                                            showFirmwareUpdatePop(tvFirmwareCode);
                                        }else {
                                            DownloadFileUtil.init(MoreCarInfoActivity.this).start(slave02Url, "slave02.bin").isInstall(false)
                                                    .setOnDownloadComplete(new DownloadFileUtil.OnDownloadComplete() {
                                                        @Override
                                                        public void downloadComplete(String path, File result) {
                                                            Log.i("TAG", "------下载固件SC03完成，" + path);
                                                            showFirmwareUpdatePop(tvFirmwareCode);
                                                        }
                                                    });
                                        }
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
    public TextView getDengdaiTV() {
        return tvDengdai;
    }

    @Override
    public SpeedSeekBar getSpeedSeekBar() {
        return speedSeekBar;
    }

}
