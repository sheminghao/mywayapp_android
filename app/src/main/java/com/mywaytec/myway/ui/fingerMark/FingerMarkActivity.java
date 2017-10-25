package com.mywaytec.myway.ui.fingerMark;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.base.BluetoothLeService;
import com.mywaytec.myway.base.Constant;
import com.mywaytec.myway.view.SlideUp;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 指纹验证界面
 * 连接车辆，录制指纹等操作
 */
public class FingerMarkActivity extends BaseActivity<FingerMarkPresenter> implements FingerMarkView{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.slideview)
    LinearLayout slideview;
    @BindView(R.id.tv_enter_count)
    TextView tvEnterCount;
    @BindView(R.id.tv_hint_text)
    TextView tvHintText;
    @BindView(R.id.img_finger_wark)
    ImageView imgFingerWark;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;

    SlideUp slideUp;

    private BluetoothLeService mBluetoothLeService;
    private boolean mConnected = false;
    private String mDeviceAddress;
    String uuid;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_finger_mark;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.fingerprint_identification);
        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        slideUp = new SlideUp.Builder(slideview)
                .withStartGravity(Gravity.BOTTOM)
                .withLoggingEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .build();

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void updateViews() {

    }

    @OnClick({R.id.tv_add_finger, R.id.tv_delete_all_finger, R.id.tv_cancel})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_add_finger://添加指纹
                mPresenter.hint1();
                break;
            case R.id.tv_delete_all_finger://删除所有指纹
                if (null != mBluetoothLeService) {
                    mBluetoothLeService.writeCharacteristic(Constant.BLE.WRITE_SERVICE_UUID,
                            Constant.BLE.WRITE_CHARACTERISTIC_UUID, Constant.BLE.DETELE_ALL_FINGER_WARK);
                    mBluetoothLeService.setCharacteristicNotification(null, true);
                }
                break;
            case R.id.tv_cancel://取消
                slideUp.hide();
                break;
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public BluetoothLeService getBluetoothLeService() {
        return mBluetoothLeService;
    }

    @Override
    public SlideUp getSlideUp() {
        return slideUp;
    }

    @Override
    public TextView getEnterCountTV() {
        return tvEnterCount;
    }

    @Override
    public TextView getHintTextTV() {
        return tvHintText;
    }

    @Override
    public ImageView getFingerWarkImg() {
        return imgFingerWark;
    }

    @Override
    public TextView getCancelTV() {
        return tvCancel;
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
                mPresenter.displayData(data);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        Log.i("TAG", "-------MoreCarInfo OnResume");
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d("TAG", "Connect request result=" + result);
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

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}
