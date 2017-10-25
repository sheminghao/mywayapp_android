package com.mywaytec.myway.ui.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.ybq.android.spinkit.SpinKitView;
import com.mywaytec.myway.APP;
import com.mywaytec.myway.ConnectedBleInfoDao;
import com.mywaytec.myway.DaoSession;
import com.mywaytec.myway.R;
import com.mywaytec.myway.adapter.BluetoothListAdapter;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.model.BluetoothInfoModel;
import com.mywaytec.myway.model.db.ConnectedBleInfo;
import com.mywaytec.myway.ui.lookoverAllCar.LookoverAllCarActivity;
import com.mywaytec.myway.utils.BleUtil;
import com.mywaytec.myway.utils.LogUtil;
import com.mywaytec.myway.utils.TimeUtil;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import butterknife.BindView;
import butterknife.OnClick;

public class BluetoothActivity extends BaseActivity<BluetoothPresenter> implements OnItemClickListener, BluetoothView {

    private static final long SCAN_PERIOD = 20000;
    private BluetoothListAdapter bluetoothListAdapter;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private BluetoothAdapter bluetoothAdapter;
    private ArrayList<BluetoothDevice> bluetoothDevices = new ArrayList<>();
    private ArrayList<BluetoothInfoModel> bluetoothInfoModels = new ArrayList<>();

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.img_search)
    ImageView imgSearch;
    @BindView(R.id.empty_loading)
    SpinKitView spinKitView;
    @BindView(R.id.lrecyclerview)
    LRecyclerView lRecyclerView;
    @BindView(R.id.mycar_recyclerview)
    RecyclerView mycarRecyclerView;
    @BindView(R.id.layout_bluetooth)
    CoordinatorLayout layoutBluetooth;
    @BindView(R.id.layout_mycar_have)
    AutoLinearLayout layoutMycarHave;
    @BindView(R.id.layout_mycar_no)
    AutoLinearLayout layoutMycarNo;
    @BindView(R.id.layout_more_car_info)
    LinearLayout layoutMoreCarInfo;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_bluetooth;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        // 透明状态栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        layoutBluetooth.setPadding(0, AppUtils.getStatusBar(), 0, 0);
        tvTitle.setText(R.string.vehicle_management);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothListAdapter = new BluetoothListAdapter(this);
        bluetoothListAdapter.setDataList(bluetoothInfoModels);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(bluetoothListAdapter);
        lRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        lRecyclerView.setAdapter(lRecyclerViewAdapter);
        lRecyclerView.setPullRefreshEnabled(false);
        lRecyclerView.setLoadMoreEnabled(false);
        lRecyclerViewAdapter.setOnItemClickListener(this);

        scanLeDevice(true);
    }

    @Override
    protected void updateViews() {
    }

    @OnClick({R.id.img_search, R.id.layout_more_car_info})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_search:
                scanLeDevice(true);
                break;
            case R.id.layout_more_car_info:
                startActivity(new Intent(BluetoothActivity.this, LookoverAllCarActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        stopScan();
        BluetoothDevice bluetoothDevice = bluetoothInfoModels.get(position).getDevice();
        BleUtil.setBleAddress(bluetoothDevice.getAddress());
        BleUtil.setBleName(bluetoothDevice.getName());
        DaoSession daoSession = APP.getInstance().getDaoSession();
        ConnectedBleInfoDao connectedBleInfoDao = daoSession.getConnectedBleInfoDao();
        List<ConnectedBleInfo> list = connectedBleInfoDao.queryBuilder().list();
        if (list.size() > 0){
            for (int i = 0; i < list.size(); i++) {
                if (bluetoothDevice.getAddress().equals(list.get(i).getAddress())){
                    connectedBleInfoDao.deleteByKey(list.get(i).getId());
                    break;
                }
            }
        }
        connectedBleInfoDao.insert(new ConnectedBleInfo(null, bluetoothDevice.getName(),
                    bluetoothDevice.getAddress(), TimeUtil.getYMDHMSTime()));
        finish();
    }

    BluetoothAdapter.LeScanCallback lescancallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            LogUtil.v("TAG","-----device"+device.getName(), true);
            LogUtil.v("TAG","-----scanRecord"+scanRecord.toString(), true);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (null != device && !bluetoothDevices.contains(device)) {
                        //不重复添加
                        bluetoothDevices.add(device);
                        if(null != device.getName() && !device.getName().contains("iCre")) {
                            BluetoothInfoModel bleInfoModel = new BluetoothInfoModel();
                            bleInfoModel.setDevice(device);
                            bleInfoModel.setRssi(rssi);
                            bluetoothInfoModels.add(bleInfoModel);
                            bluetoothListAdapter.setDataList(bluetoothInfoModels);
                        }
                    }
                }
            });
//            bluetoothListAdapter.notifyDataSetChanged();
        }
    };

    Timer timer;
    //扫描设备
    public void scanLeDevice(final boolean enable) {
        if (enable) {
//            timer = new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    stopScan();
//                    Log.e("TAG", "run: stop");
//                }
//            }, SCAN_PERIOD);
            startScan();
            Log.e("TAG", "start");
        } else {
            stopScan();
            Log.e("TAG", "stop");
        }
    }

    //开始扫描BLE设备
    private void startScan() {
        imgSearch.setVisibility(View.GONE);
        spinKitView.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                bluetoothAdapter.startLeScan(lescancallback);

                // 如果正在搜索，就先取消搜索
                if (bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                }
                bluetoothAdapter.startDiscovery();
            }
        }).start();
    }

    //停止扫描BLE设备
    private void stopScan() {
        imgSearch.post(new Runnable() {
            @Override
            public void run() {
                imgSearch.setVisibility(View.VISIBLE);
                spinKitView.setVisibility(View.GONE);
            }
        });
        if (null != timer){
            timer.cancel();
            timer = null;
        }
        bluetoothAdapter.stopLeScan(lescancallback);
        bluetoothAdapter.cancelDiscovery();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            // 获得已经搜索到的蓝牙设备
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                LogUtil.v("TAG","-----device"+device.getName(), true);
                // 搜索到的不是已经绑定的蓝牙设备
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    // 显示在TextView上
//                    mTextView.append(device.getName() + ":"
//                            + device.getAddress()+"\n");
                    if (!bluetoothDevices.contains(device)) {
                        //不重复添加
                        bluetoothDevices.add(device);
                        if(null != device && null != device.getName() && !device.getName().contains("iCre")) {
                            BluetoothInfoModel bleInfoModel = new BluetoothInfoModel();
                            bleInfoModel.setDevice(device);
                            bleInfoModel.setRssi(intent.getIntExtra(BluetoothDevice.EXTRA_RSSI, 0));
                            bluetoothInfoModels.add(bleInfoModel);
                        }
                    }
//                    bluetoothListAdapter.setDataList(bluetoothInfoModels);
                    lRecyclerViewAdapter.notifyDataSetChanged();
                }
                // 搜索完成
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                setProgressBarIndeterminateVisibility(false);
//                setTitle("搜索蓝牙设备");
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public RecyclerView getMycarRecyclerView() {
        return mycarRecyclerView;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public AutoLinearLayout getLayoutMycarHave() {
        return layoutMycarHave;
    }

    @Override
    public AutoLinearLayout getLayoutMycarNo() {
        return layoutMycarNo;
    }

    @Override
    public LinearLayout getLayoutMoreCarInfo() {
        return layoutMoreCarInfo;
    }


}
