package com.mywaytec.myway.fragment.way;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseFragment;
import com.mywaytec.myway.ui.mytrack.MyTrackActivity;
import com.mywaytec.myway.ui.track.TrackRecordActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 路线模块
 * Created by shemh on 2017/2/20.
 */

public class WayFragment extends BaseFragment<WayPresenter> implements WayView, View.OnClickListener {

    @BindView(R.id.lrecyclerview_way)
    LRecyclerView lRecyclerView;

    private static WayFragment wayFragment;

    public static WayFragment getInstance(){
        if (wayFragment == null){
            return new WayFragment();
        }
        return wayFragment;
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_way;
    }

    @Override
    protected void initInjector() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected void initViews() {
        init();
        mPresenter.init(getActivity());
    }

    @Override
    protected void updateViews() {

    }

    private void init(){
    }

    @OnClick({R.id.layout_way_top})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_track_record://记录轨迹
                if (null != mPopupWindow && mPopupWindow.isShowing()){
                    mPopupWindow.dismiss();
                }
                initGPS();
                break;
            case R.id.layout_mine_record://我的轨迹
                if (null != mPopupWindow && mPopupWindow.isShowing()){
                    mPopupWindow.dismiss();
                }
                startActivity(new Intent(getActivity(), MyTrackActivity.class));
                break;
            case R.id.layout_way_top:
                lRecyclerView.smoothScrollToPosition(0);
                break;
        }
    }

    private PopupWindow mPopupWindow;

    /**
     * 右上角下拉框
     */
    private void rightTopPop(TextView v){
        View popupView = getActivity().getLayoutInflater().inflate(R.layout.layout_popupwindow, null);
        LinearLayout layoutTrackRecord = (LinearLayout) popupView.findViewById(R.id.layout_track_record);
        LinearLayout layoutMineRecord = (LinearLayout) popupView.findViewById(R.id.layout_mine_record);
        layoutTrackRecord.setOnClickListener(this);
        layoutMineRecord.setOnClickListener(this);
        mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));

        mPopupWindow.getContentView().setFocusableInTouchMode(true);
        mPopupWindow.getContentView().setFocusable(true);
        mPopupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                    return true;
                }
                return false;
            }
        });
        mPopupWindow.showAsDropDown(v);
    }

    /*
     * 监听GPS
     */
    private void initGPS() {
        LocationManager locationManager = (LocationManager) getActivity()
                .getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则开启
        if (!locationManager
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            final AlertDialog.Builder alterDialog = new AlertDialog.Builder(getActivity());
            alterDialog.setMessage(getResources().getString(R.string.please_open_gps));
            alterDialog.setCancelable(true);
            alterDialog.setPositiveButton(getResources().getString(R.string.go_to_setting), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 转到手机设置界面，用户设置GPS
                    Intent intent = new Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    getActivity().startActivityForResult(intent, 0x129); // 设置完成后返回到原来的界面
                }
            });
            alterDialog.setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alterDialog.show();
        } else {
            // 弹出Toast
            startActivity(new Intent(getActivity(), TrackRecordActivity.class));
        }
    }

    @Override
    public LRecyclerView getRecyclerView() {
        return lRecyclerView;
    }
}
