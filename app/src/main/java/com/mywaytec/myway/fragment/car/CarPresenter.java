package com.mywaytec.myway.fragment.car;

import android.content.Context;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BluetoothLeService;
import com.mywaytec.myway.base.Constant;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.BleUtil;
import com.mywaytec.myway.utils.ConversionUtil;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CommonSubscriber;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/3/9.
 */

public class CarPresenter extends RxPresenter<CarView> {

    RetrofitHelper mRetrofitHelper;
    Context mContext;

    @Inject
    public CarPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper =  mRetrofitHelper;
        registerEvent();
    }

    void registerEvent() {
        getData();
    }

    @Override
    public void attachView(CarView view) {
        mView = view;
        mContext = mView.getContext();
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
    public void verificationVehiclePassword(final BluetoothLeService mBluetoothLeService){
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
                    if (mBluetoothLeService != null){
                        Log.i("TAG", "------verificationVehiclePassword确认发送");
                        mView.setOther(true);
                        SystemClock.sleep(30);
                        if (layoutVerifyPassword.getVisibility() == View.VISIBLE) {
                            if (TextUtils.isEmpty(password)){
                                ToastUtils.showToast("请输入密码");
                                return;
                            }
                            if (password.length() != 6){
                                ToastUtils.showToast("密码长度不正确");
                                return;
                            }
                            mBluetoothLeService.writeCharacteristic(Constant.BLE.WRITE_SERVICE_UUID,
                                    Constant.BLE.WRITE_CHARACTERISTIC_UUID,
                                    BleUtil.verificationVehiclePassword(password));
                        }else {
                            mBluetoothLeService.writeCharacteristic(Constant.BLE.WRITE_SERVICE_UUID,
                                    Constant.BLE.WRITE_CHARACTERISTIC_UUID,
                                    Constant.BLE.VEHICLE_PASSWORD_RESET);
                        }

                        SystemClock.sleep(30);
                        mView.setOther(false);
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
        Log.i("TAG", "-----MoreCarInfo_info" + info);
        String[] infos = info.split(" ");
        Log.i("TAG", "-----MoreCarInfo_infos" + infos.length);
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
                    ToastUtils.showToast("密码验证失败");
                }
            }
        }
    }

}
