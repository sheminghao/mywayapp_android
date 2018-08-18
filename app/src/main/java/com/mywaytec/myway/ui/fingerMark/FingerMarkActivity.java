package com.mywaytec.myway.ui.fingerMark;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luck.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.luck.bluetooth.library.connect.response.BleNotifyResponse;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.base.Constant;
import com.mywaytec.myway.utils.BleKitUtils;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.view.SlideUp;

import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

import static com.luck.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.luck.bluetooth.library.Constants.STATUS_DISCONNECTED;

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
        mDeviceAddress = getIntent().getStringExtra("mDeviceAddress");
        uuid = PreferencesUtils.getString(this, "uuid");

        slideUp = new SlideUp.Builder(slideview)
                .withStartGravity(Gravity.BOTTOM)
                .withLoggingEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .build();

        if (Constant.BLE.WRITE_SERVICE_UUID.equals(uuid)) {
            BleKitUtils.notifyP(mDeviceAddress, new BleNotifyResponse() {
                @Override
                public void onNotify(UUID service, UUID character, byte[] value) {
                    mPresenter.displayData(value);
                }
                @Override
                public void onResponse(int code) {
                }
            });
        } else if (Constant.BLE.TAIDOU_WRITE_SERVICE_UUID.equals(uuid)) {
            BleKitUtils.notifyTaiTou(mDeviceAddress, new BleNotifyResponse() {
                @Override
                public void onNotify(UUID service, UUID character, byte[] value) {
                    mPresenter.displayData(value);
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

    @OnClick({R.id.tv_add_finger, R.id.tv_delete_all_finger, R.id.tv_cancel})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_add_finger://添加指纹
                mPresenter.hint1();
                break;
            case R.id.tv_delete_all_finger://删除所有指纹
                mPresenter.deteleAllFinger();
                break;
            case R.id.tv_cancel://完成
//                slideUp.hide();
                finish();
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

    @Override
    public String getDeviceAddress() {
        return mDeviceAddress;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("TAG", "-------FingerMarkActivity OnResume");
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
                if (tvCancel.getVisibility() == View.VISIBLE) {
                    tvCancel.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 3000);
                }else {
                    finish();
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
