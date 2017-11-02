package com.mywaytec.myway.fragment.dynamic;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseFragment;
import com.mywaytec.myway.model.bean.DynamicListBean;
import com.mywaytec.myway.ui.main.MainActivity;
import com.mywaytec.myway.ui.publish.PublishActivity;
import com.mywaytec.myway.ui.track.TrackRecordActivity;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.view.CircleImageView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * 动态模块
 * Created by shemh on 2017/2/17.
 */

public class DynamicFragment extends BaseFragment<DynamicPresenter> implements DynamicView, View.OnClickListener {

    @BindView(R.id.tv_dongtai)
    TextView tvDongtai;
    @BindView(R.id.tv_luxian)
    TextView tvLuxian;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.lrecyclerview_dynamic)
    LRecyclerView dynamicLRecyclerView;
    @BindView(R.id.lrecyclerview_way)
    LRecyclerView wayLRecyclerView;
    @BindView(R.id.img_user)
    CircleImageView circleImageView;

    private static DynamicFragment dynamicFragment;

    public static DynamicFragment getInstance(){
        if (dynamicFragment == null){
            return new DynamicFragment();
        }
        return dynamicFragment;
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_dynamic;
    }

    @Override
    protected void initInjector() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected void initViews() {
        if (PreferencesUtils.getLoginInfo()!=null){
            if (PreferencesUtils.getLoginInfo().getObj().getGender()) {
                Glide.with(this).load(PreferencesUtils.getLoginInfo().getObj().getImgeUrl())
                        .error(R.mipmap.touxiang_boy_nor)
                        .into(circleImageView);
            }else {
                Glide.with(this).load(PreferencesUtils.getLoginInfo().getObj().getImgeUrl())
                        .error(R.mipmap.touxiang_girl_nor)
                        .into(circleImageView);
            }
        }
        mPresenter.initDynamicList(mContext);
        mPresenter.initWayList(mContext);
    }

    @Override
    protected void updateViews() {

    }

    public void refresh(){
        if (mPresenter!=null)
        mPresenter.getDynamicData();
    }

    //刷新用户头像
    public void refreshHeadPortrait(){
        Log.i("TAG", "-------DynamicFragment刷新用户头像");
        if (circleImageView == null){
            circleImageView = (CircleImageView) getActivity().findViewById(R.id.img_user);
        }
        if (circleImageView!=null){
            if (PreferencesUtils.getLoginInfo()!=null){
                if (PreferencesUtils.getLoginInfo().getObj().getGender()) {
                    Glide.with(getActivity()).load(PreferencesUtils.getLoginInfo().getObj().getImgeUrl())
                            .error(R.mipmap.touxiang_boy_nor)
                            .into(circleImageView);
                }else {
                    Glide.with(getActivity()).load(PreferencesUtils.getLoginInfo().getObj().getImgeUrl())
                            .error(R.mipmap.touxiang_girl_nor)
                            .into(circleImageView);
                }
            }
        }
    }

    //刷新单个item
    public void refreshItem(DynamicListBean.ObjBean dynamic, int position){
        mPresenter.refreshItem(dynamic, position);
        Log.i("TAG", "-------MainActivity刷新"+position);
    }

    public static final int PUBLISH = 0x112;

    @OnClick({R.id.img_right, R.id.img_user, R.id.tv_dongtai, R.id.tv_luxian, R.id.layout_dynamic_top})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_right:
                if (dynamicLRecyclerView.getVisibility() == View.VISIBLE){//发布动态
                    getActivity().startActivityForResult(new Intent(mContext, PublishActivity.class), PUBLISH);
                }else {//录制轨迹
                    initGPS();
                }
                break;
            case R.id.img_user:
                ((MainActivity)getActivity()).getDragLayout().open();
                break;
            case R.id.tv_dongtai://动态
                dynamicLRecyclerView.setVisibility(View.VISIBLE);
                wayLRecyclerView.setVisibility(View.GONE);
                tvDongtai.setTextColor(Color.parseColor("#ffffff"));
                tvLuxian.setTextColor(Color.parseColor("#B7B7B7"));
                imgRight.setImageResource(R.mipmap.fabiao);
                break;
            case R.id.tv_luxian://路线
                dynamicLRecyclerView.setVisibility(View.GONE);
                wayLRecyclerView.setVisibility(View.VISIBLE);
                tvDongtai.setTextColor(Color.parseColor("#B7B7B7"));
                tvLuxian.setTextColor(Color.parseColor("#ffffff"));
                imgRight.setImageResource(R.mipmap.gengduoshezhi_icon_jiluguiji);
                break;
            case R.id.layout_dynamic_top://状态栏
                dynamicLRecyclerView.smoothScrollToPosition(0);
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        JZVideoPlayerStandard.releaseAllVideos();
    }

    @Override
    public LRecyclerView getDynamicList() {
        return dynamicLRecyclerView;
    }

    @Override
    public LRecyclerView getWayList() {
        return wayLRecyclerView;
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
                    startActivityForResult(intent, 0x129); //设置完成后返回到原来的界面
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

}
