package com.mywaytec.myway.ui.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.ybq.android.spinkit.SpinKitView;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.mywaytec.myway.APP;
import com.mywaytec.myway.ConnectedBleInfoDao;
import com.mywaytec.myway.DaoSession;
import com.mywaytec.myway.R;
import com.mywaytec.myway.adapter.BluetoothListAdapter;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.model.db.ConnectedBleInfo;
import com.mywaytec.myway.ui.lookoverAllCar.LookoverAllCarActivity;
import com.mywaytec.myway.utils.BleKitUtils;
import com.mywaytec.myway.utils.BleUtil;
import com.mywaytec.myway.utils.TimeUtil;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class BluetoothActivity extends BaseActivity<BluetoothPresenter> implements OnItemClickListener, BluetoothView {

    private static final long SCAN_PERIOD = 20000;
    private BluetoothListAdapter bluetoothListAdapter;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private BluetoothAdapter bluetoothAdapter;
    private ArrayList<SearchResult> bluetoothDevices = new ArrayList<>();
    private ArrayList<SearchResult> bluetoothInfoModels = new ArrayList<>();

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

        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(1000000, 1)   // 先扫BLE设备3次，每次3s
                .searchBluetoothLeDevice(2000)      // 再扫BLE设备2s
                .build();
        BleKitUtils.getBluetoothClient().search(request, searchResponse);
    }

    @Override
    protected void updateViews() {
    }

    @OnClick({R.id.img_search, R.id.layout_more_car_info})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_search:
                break;
            case R.id.layout_more_car_info:
                startActivity(new Intent(BluetoothActivity.this, LookoverAllCarActivity.class));
                finish();
                break;
        }
    }

    SearchResponse searchResponse = new SearchResponse() {
        @Override
        public void onSearchStarted() {

        }

        @Override
        public void onDeviceFounded(SearchResult device) {
            if (null != device && !bluetoothDevices.contains(device)) {
                //不重复添加
                bluetoothDevices.add(device);
                if(null != device.getName() && device.getName().contains("SC-") || device.getName().contains("RA-")) {
                    bluetoothInfoModels.add(device);
                    bluetoothListAdapter.setDataList(bluetoothInfoModels);
                }
            }
        }

        @Override
        public void onSearchStopped() {

        }

        @Override
        public void onSearchCanceled() {

        }
    };

    @Override
    public void onItemClick(View view, int position) {
        SearchResult bluetoothDevice = bluetoothInfoModels.get(position);
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
    @Override
    protected void onPause() {
        super.onPause();
        BleKitUtils.getBluetoothClient().stopSearch();
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
