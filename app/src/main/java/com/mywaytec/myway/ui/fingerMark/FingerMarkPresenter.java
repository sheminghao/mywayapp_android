package com.mywaytec.myway.ui.fingerMark;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.mywaytec.myway.APP;
import com.mywaytec.myway.DaoSession;
import com.mywaytec.myway.FingerWarkInfoDao;
import com.mywaytec.myway.R;
import com.mywaytec.myway.adapter.FingerMarkAdapter;
import com.mywaytec.myway.base.Constant;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.db.FingerWarkInfo;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.BleKitUtils;
import com.mywaytec.myway.utils.BleUtil;
import com.mywaytec.myway.utils.ConversionUtil;
import com.mywaytec.myway.utils.ToastUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/8/15.
 */

public class FingerMarkPresenter extends RxPresenter<FingerMarkView> {

    RetrofitHelper retrofitHelper;
    Context mContext;
    FingerMarkAdapter fingerMarkAdapter;
    DaoSession daoSession;
    FingerWarkInfoDao fingerWarkInfoDao;
    List<FingerWarkInfo> fingerList;
    String bleAddress;

    @Inject
    public FingerMarkPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(FingerMarkView view) {
        super.attachView(view);
        mContext = mView.getContext();
        daoSession = APP.getInstance().getDaoSession();
        fingerWarkInfoDao = daoSession.getFingerWarkInfoDao();
        bleAddress = BleUtil.getBleAddress();
        initRecyclerView();
    }

    /**
     * 删除项
     */
    private int position;
    public void initRecyclerView(){
        try {
            fingerList = fingerWarkInfoDao.queryBuilder().where(FingerWarkInfoDao.Properties.Uid.eq(bleAddress)).list();
        }catch (Exception e){

        }
        fingerMarkAdapter = new FingerMarkAdapter(mContext);
        mView.getRecyclerView().setLayoutManager(new LinearLayoutManager(mContext));
        mView.getRecyclerView().setAdapter(fingerMarkAdapter);
        if (null != fingerList) {
            fingerMarkAdapter.setDataList(fingerList);
        }
        fingerMarkAdapter.setOnDelListener(new FingerMarkAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                position = pos;
                byte fingerWarkId = fingerMarkAdapter.getDataList().get(pos).getFingerWarkId();
                BleKitUtils.writeP(mView.getDeviceAddress(), BleUtil.deleteFingerWark(fingerWarkId), new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {

                    }
                });
            }

            @Override
            public void onChange(int pos) {
                amendFingerName(pos, fingerMarkAdapter.getDataList().get(pos).getFingerName());
            }
        });
    }

    //蓝牙设备返回信息
    public void displayData(byte[] data) {
        if (data != null) {
            String info = ConversionUtil.byte2HexStr(data);
            Log.i("TAG", "-----info" + info);
            String[] infos = info.split(" ");
//            Log.i("TAG", "-----infos"+infos.length);
            if (data.length > 6 && "04".equals(infos[2]) && "03".equals(infos[3]) && "0A".equals(infos[4])) {//删除所有指纹
                if ("01".equals(infos[6])){//操作成功
                    Log.i("TAG", "--------删除所有指纹成功");
                    ToastUtils.showToast(R.string.successfully);
                    fingerWarkInfoDao.deleteAll();
                    fingerMarkAdapter.clear();
                }else {
                    Log.i("TAG", "--------删除所有指纹失败");
                    ToastUtils.showToast(R.string.failed);
                }
            }else if (data.length > 6 && "04".equals(infos[2]) && "03".equals(infos[3]) && "0B".equals(infos[4])){//录入成功，返回ID
                byte fingerId = data[6];
                Log.i("TAG", "--------录入成功，返回ID,"+fingerId);
                fingerWarkInfoDao.insert(new FingerWarkInfo(null, fingerId, bleAddress, ""));
                fingerList = fingerWarkInfoDao.queryBuilder().where(FingerWarkInfoDao.Properties.Uid.eq(bleAddress)).list();
                fingerMarkAdapter.setDataList(fingerList);
            }else if (data.length > 6 && "04".equals(infos[2]) && "03".equals(infos[3]) && "09".equals(infos[4])){//录入指纹
                if ("00".equals(infos[6])){//录入失败。请先退出指纹录入界面然后重新进行录入。
                    Log.i("TAG", "--------录入失败。请先退出指纹录入界面然后重新进行录入");
                    hint2(mContext.getResources().getString(R.string.register_unsuccessfully)+
                            mContext.getResources().getString(R.string.please_exit_first));
                }else if ("01".equals(infos[6])){//车辆非静止
                    Log.i("TAG", "--------车辆非静止");
                    hint2(mContext.getResources().getString(R.string.fingerprint_cannot_be_registered));
                }else if ("02".equals(infos[6])){//指纹库满
                    Log.i("TAG", "--------指纹库满");
                    hint2(mContext.getResources().getString(R.string.the_fingerprint_database_is_full));
                }else if ("10".equals(infos[6])){//第一次录入：请将手指放在指纹识别器上轻按，再移开手指
                    Log.i("TAG", "--------第一次录入：请将手指放在指纹识别器上轻按，再移开手指");
                    mView.getSlideUp().show();
                    mView.getEnterCountTV().setText(R.string.first_finger_identify);
                    mView.getHintTextTV().setText(R.string.please_use_your_finger_to_press_on_the_fingerprint_recognition_sensor);
                }else if ("11".equals(infos[6])){//第一次录入失败：指纹图像质量不佳，请重新第一次录入
                    Log.i("TAG", "--------第一次录入失败：指纹图像质量不佳，请重新第一次录入");
                    mView.getEnterCountTV().setText(R.string.first_finger_identify_unsuccessfully);
                    mView.getHintTextTV().setText(R.string.fingerprint_identify_unsuccessfully);
                }else if ("12".equals(infos[6])){//第一次录入失败：该指纹已注册，请换另一根手指进行录入
                    Log.i("TAG", "--------第一次录入失败：该指纹已注册，请换另一根手指进行录入");
                    mView.getEnterCountTV().setText(R.string.first_finger_identify_unsuccessfully);
                    mView.getHintTextTV().setText(R.string.the_fingerprint_has_been_registered);
                }else if ("13".equals(infos[6])){//第一次录入成功：请将手指从指纹传感器上移开，准备第二次录入
                    Log.i("TAG", "--------第一次录入成功：请将手指从指纹传感器上移开，准备第二次录入");
                    mView.getFingerWarkImg().setImageResource(R.mipmap.icon_finger_wark_2);
                    mView.getEnterCountTV().setText(R.string.first_finger_identify_successfully);
                    mView.getHintTextTV().setText(R.string.please_remove_your_finger_from_fingerprint_recognition_sensor);
                }else if ("20".equals(infos[6])){//第二次录入：请将同一手指放在指纹传感器上轻按，再移开手指
                    Log.i("TAG", "--------第二次录入：请将同一手指放在指纹传感器上轻按，再移开手指");
                    mView.getFingerWarkImg().setImageResource(R.mipmap.icon_finger_wark_2);
                    mView.getEnterCountTV().setText(R.string.second_finger_identify);
                    mView.getHintTextTV().setText(R.string.please_use_the_same_finger_to_press_fingerprint_recognition_sensor);
                }else if ("21".equals(infos[6])){//第二次录入失败：指纹图像质量不佳，请重新第二次录入
                    Log.i("TAG", "--------第二次录入失败：指纹图像质量不佳，请重新第二次录入");
                    mView.getEnterCountTV().setText(R.string.second_finger_identify_unsuccessfully);
                    mView.getHintTextTV().setText(R.string.please_reregister_again);
                }else if ("22".equals(infos[6])){//第二次录入失败：该指纹与第一次输入的指纹不匹配，请换回第一次使用的手指进行录入
                    Log.i("TAG", "--------第二次录入失败：该指纹与第一次输入的指纹不匹配，请换回第一次使用的手指进行录入");
                    mView.getEnterCountTV().setText(R.string.second_finger_identify_unsuccessfully);
                    mView.getHintTextTV().setText(R.string.the_fingerprint_does_not_match_for_the_two_times);
                }else if ("FF".equals(infos[6])){//指纹录入完成，您可以使用指纹解锁车辆
                    Log.i("TAG", "--------指纹录入完成，您可以使用指纹解锁车辆");
                    mView.getFingerWarkImg().setImageResource(R.mipmap.icon_finger_wark_3);
                    mView.getEnterCountTV().setText(R.string.register_successfully);
                    mView.getHintTextTV().setText(R.string.you_can_use_your_fingerprint_to_lockunlock_vehicle_now);
                    mView.getCancelTV().setText(R.string.finish);
                }
            }else if (data.length > 6 && "04".equals(infos[2]) && "02".equals(infos[3]) && "10".equals(infos[4])) {//删除成功，返回ID
                byte fingerId = data[6];
                Log.i("TAG", "--------删除成功，返回ID,"+fingerId);
                QueryBuilder qb = fingerWarkInfoDao.queryBuilder();
                qb.where(FingerWarkInfoDao.Properties.FingerWarkId.eq(fingerId),
                        FingerWarkInfoDao.Properties.Uid.eq(bleAddress));
                List<FingerWarkInfo> list = qb.list();
                Log.i("TAG", "--------删除成功，qb.list(),"+list.size());
                if (null != list && list.size() > 0){
                    fingerWarkInfoDao.delete(list.get(0));
                }
                fingerMarkAdapter.remove(position);
            }
        }
    }

    public AlertDialog amendFingerNameDialog;
    /** 修改指纹名称dialog */
    public void amendFingerName(final int position, String name){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext, R.layout.dialog_amend_finger_name, null);
        final EditText etName = (EditText) view.findViewById(R.id.et_name);
        etName.setText(name);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amendFingerNameDialog.dismiss();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fingerName = etName.getText().toString().trim();
                if (!TextUtils.isEmpty(fingerName)) {
                    long id = fingerMarkAdapter.getDataList().get(position).getId();
                    byte fingerId = fingerMarkAdapter.getDataList().get(position).getFingerWarkId();
                    fingerWarkInfoDao.update(new FingerWarkInfo(id, fingerId, bleAddress, fingerName));
                    fingerMarkAdapter.getDataList().get(position).setFingerName(fingerName);
                    fingerMarkAdapter.notifyDataSetChanged();
                    amendFingerNameDialog.dismiss();
                }else {
                    ToastUtils.showToast(R.string.please_enter_name);
                }
            }
        });
        builder.setView(view);
        amendFingerNameDialog = builder.show();
    }

    public AlertDialog hint1Dialog;
    /** 添加指纹dialog */
    public void hint1(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext, R.layout.dialog_add_finger_hint, null);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tvContinue = (TextView) view.findViewById(R.id.tv_continue);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hint1Dialog.dismiss();
            }
        });
        tvContinue.setOnClickListener(new View.OnClickListener() {//添加指纹
            @Override
            public void onClick(View v) {
                BleKitUtils.writeP(mView.getDeviceAddress(), Constant.BLE.ENTER_FINGER_WARK, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        if (code == Constants.REQUEST_SUCCESS) {
                            hint1Dialog.dismiss();
                        }
                    }
                });
            }
        });
        builder.setView(view);
        hint1Dialog = builder.show();
    }

    AlertDialog hint2Dialog;
    TextView tvHint;
    /** 指纹录制提示dialog */
    public void hint2(String hint){
        if (null != hint2Dialog && hint2Dialog.isShowing()) {
            if (null != tvHint){
                tvHint.setText(hint);
            }
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            View view = View.inflate(mContext, R.layout.dialog_finger_wark_hint, null);
            tvHint = (TextView) view.findViewById(R.id.tv_hint);
            tvHint.setText(hint);
            builder.setView(view);
            hint2Dialog = builder.show();
        }
    }
}
