package com.mywaytec.myway.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mywaytec.myway.AppManager;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.base.Constant;
import com.mywaytec.myway.fragment.car.CarFragment;
import com.mywaytec.myway.fragment.dynamic.DynamicFragment;
import com.mywaytec.myway.fragment.way.WayFragment;
import com.mywaytec.myway.model.bean.DynamicListBean;
import com.mywaytec.myway.ui.bindingCar.BindingCarActivity;
import com.mywaytec.myway.ui.gradeAndGold.GradeAndGoldActivity;
import com.mywaytec.myway.ui.huodongyueban.HuodongYuebanActivity;
import com.mywaytec.myway.ui.message.MessageActivity;
import com.mywaytec.myway.ui.mydynamic.MyDynamicActivity;
import com.mywaytec.myway.ui.myprofile.MyProfileActivity;
import com.mywaytec.myway.ui.mytrack.MyTrackActivity;
import com.mywaytec.myway.ui.peopleNearby.PeopleNearbyActivity;
import com.mywaytec.myway.ui.rankingList.RankingListActivity;
import com.mywaytec.myway.ui.setting.SettingActivity;
import com.mywaytec.myway.ui.track.TrackRecordActivity;
import com.mywaytec.myway.utils.AppUtils;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CircleImageView;
import com.mywaytec.myway.view.DragLayout;
import com.mywaytec.myway.view.MyRelativeLayout;
import com.tencent.tinker.lib.tinker.TinkerInstaller;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayerStandard;

import static com.mywaytec.myway.fragment.dynamic.DynamicFragment.PUBLISH;
import static com.mywaytec.myway.ui.myprofile.MyProfileActivity.MYPROFILE_CODE;

/**
 * 首页
 */
public class MainActivity extends BaseActivity<MainPresenter> implements MainView, View.OnClickListener{

    @BindView(R.id.layout_dynamic)
    LinearLayout layoutDynamic;
    @BindView(R.id.layout_car)
    LinearLayout layoutCar;
    @BindView(R.id.layout_way)
    LinearLayout layoutWay;
    @BindView(R.id.layout_menu)
    RelativeLayout layoutMenu;
    @BindView(R.id.myrelativelayout)
    MyRelativeLayout myrelativelayout;
    @BindView(R.id.dragLayout)
    DragLayout dragLayout;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_signature)
    TextView tvSignature;
    @BindView(R.id.tv_unread_count)
    TextView tvUnreadCount;
    @BindView(R.id.tv_paiming)
    TextView tvPaiming;
    @BindView(R.id.tv_gold)
    TextView tvGold;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.img_level)
    ImageView imgLevel;
    @BindView(R.id.img_auth)
    ImageView imgAuth;
    @BindView(R.id.img_sign_in)
    ImageView imgSignin;
    @BindView(R.id.tv_sign_in)
    TextView tvSignin;
    @BindView(R.id.img_dynamic)
    ImageView imgDynamic;
    @BindView(R.id.img_car)
    ImageView imgCar;
    @BindView(R.id.img_way)
    ImageView imgWay;
    @BindView(R.id.tv_dynamic)
    TextView tvDynamic;
    @BindView(R.id.tv_way)
    TextView tvWay;


    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        initView();
        initDragLayout();

    }

    CircleImageView imgMyProfile;
    private void initDragLayout() {
        tvNickname.setText(PreferencesUtils.getLoginInfo().getObj().getNickname());
        tvSignature.setText(PreferencesUtils.getLoginInfo().getObj().getSignature());
        if(PreferencesUtils.getLoginInfo().getObj().getAuthType() == 1){
            imgAuth.setVisibility(View.VISIBLE);
        }else {
            imgAuth.setVisibility(View.GONE);
        }
        tvGold.setText(PreferencesUtils.getLoginInfo().getObj().getGlod()+"");
        mPresenter.loadLevel(imgLevel, tvLevel, PreferencesUtils.getLoginInfo().getObj().getIntegral());
        imgMyProfile = (CircleImageView) findViewById(R.id.img_my_profile);
        imgMyProfile.setOnClickListener(this);
        if (PreferencesUtils.getLoginInfo().getObj().getGender()) {
            Glide.with(this).load(PreferencesUtils.getLoginInfo().getObj().getImgeUrl())
                    .error(R.mipmap.touxiang_boy_nor)
                    .into(imgMyProfile);
        }else {
            Glide.with(this).load(PreferencesUtils.getLoginInfo().getObj().getImgeUrl())
                    .error(R.mipmap.touxiang_girl_nor)
                    .into(imgMyProfile);
        }
        dragLayout.setDragListener(new DragLayout.DragListener() {
            @Override
            public void onOpen() {
//                lv.smoothScrollToPosition(0);
            }
            @Override
            public void onClose() {
//                shake();
            }
            @Override
            public void onDrag(float percent) {
//                ViewHelper.setAlpha(iv_icon, 1 - percent);
            }
        });
    }

    @Override
    protected void updateViews() {
    }

    private FragmentManager fragmentManager;
    private void initView(){
        Log.i("TAG", "=====onCreate()");
        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        layoutMenu.setPadding(0, AppUtils.getStatusBar(), 210, 30);
        myrelativelayout.setPadding(0, AppUtils.getStatusBar(), 0, 0);
        fragmentManager = getSupportFragmentManager();
        layoutCar.performClick();

        //热修复
        TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(),
                Constant.DEFAULT_PATH + "/patch_signed_7zip.apk");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TAG", "=====onResume()");
        mPresenter.getOtherMsg(tvUnreadCount, tvPaiming);
        mPresenter.queryIntegralGold(imgLevel, tvLevel);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("TAG", "=====onRestart()");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.i("TAG", "=====onSaveInstanceState()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("TAG", "=====onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("TAG", "=====onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("TAG", "=====onDestroy()");
    }

    DynamicFragment dynamicFragment;
    CarFragment carFragment;
    WayFragment wayFragment;
    @OnClick({R.id.layout_dynamic, R.id.layout_car, R.id.layout_way, R.id.layout_message,
              R.id.layout_binding_car, R.id.layout_paiming, R.id.layout_wodefabu, R.id.layout_people_nearby,
              R.id.layout_sign_in, R.id.layout_setting, R.id.layout_level, R.id.layout_huodongyueban,
              R.id.layout_my_track})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_dynamic://动态
                imgDynamic.setImageResource(R.mipmap.tongdai_bai);
                imgCar.setImageResource(R.mipmap.tab_car_press);
                imgWay.setImageResource(R.mipmap.luxian);
                tvDynamic.setTextColor(Color.parseColor("#ffffff"));
                tvWay.setTextColor(Color.parseColor("#898989"));
                dynamicFragment = (DynamicFragment) fragmentManager.findFragmentByTag("dynamicFragment");
                if (dynamicFragment == null){
//                    Log.i("TAG", "------首页创建dynamicFragment");
                    dynamicFragment = DynamicFragment.getInstance();
                    fragmentManager.beginTransaction().add(R.id.layout_fragment, dynamicFragment, "dynamicFragment").commit();
                    carFragment = (CarFragment) fragmentManager.findFragmentByTag("carFragment");
                    if (carFragment != null){
                        fragmentManager.beginTransaction().hide(carFragment).commit();
                    }
                    wayFragment = (WayFragment) fragmentManager.findFragmentByTag("wayFragment");
                    if (wayFragment != null){
                        fragmentManager.beginTransaction().hide(wayFragment).commit();
                    }
                }else{
//                    Log.i("TAG", "------首页dynamicFragment已创建");
                    if (carFragment == null){
                        carFragment = CarFragment.getInstance();
                    }
                    if (wayFragment == null){
                        wayFragment = WayFragment.getInstance();
                    }
                    fragmentManager.beginTransaction().show(dynamicFragment).hide(carFragment).hide(wayFragment).commit();
                }
                break;
            case R.id.layout_car://车辆
                imgDynamic.setImageResource(R.mipmap.tongdai);
                imgCar.setImageResource(R.mipmap.tab_car_nor);
                imgWay.setImageResource(R.mipmap.luxian);
                tvDynamic.setTextColor(Color.parseColor("#898989"));
                tvWay.setTextColor(Color.parseColor("#898989"));
                carFragment = (CarFragment) fragmentManager.findFragmentByTag("carFragment");
                if (carFragment == null){
//                    Log.i("TAG", "------首页创建carFragment");
                    carFragment = CarFragment.getInstance();
                    fragmentManager.beginTransaction().add(R.id.layout_fragment, carFragment, "carFragment").commit();
                    dynamicFragment = (DynamicFragment) fragmentManager.findFragmentByTag("dynamicFragment");
                    if (dynamicFragment != null){
                        fragmentManager.beginTransaction().hide(dynamicFragment).commit();
                    }
                    wayFragment = (WayFragment) fragmentManager.findFragmentByTag("wayFragment");
                    if (wayFragment != null){
                        fragmentManager.beginTransaction().hide(wayFragment).commit();
                    }
                }else{
//                    Log.i("TAG", "------首页carFragment已创建");
                    if (dynamicFragment == null){
                        dynamicFragment = DynamicFragment.getInstance();
                    }
                    if (wayFragment == null){
                        wayFragment = WayFragment.getInstance();
                    }
                    fragmentManager.beginTransaction().show(carFragment).hide(dynamicFragment).hide(wayFragment).commit();
                }
                break;
            case R.id.layout_way://路线
                imgDynamic.setImageResource(R.mipmap.tongdai);
                imgCar.setImageResource(R.mipmap.tab_car_press);
                imgWay.setImageResource(R.mipmap.luxian_bai);
                tvDynamic.setTextColor(Color.parseColor("#898989"));
                tvWay.setTextColor(Color.parseColor("#ffffff"));
                wayFragment = (WayFragment) fragmentManager.findFragmentByTag("wayFragment");
                if (wayFragment == null){
//                    Log.i("TAG", "------首页创建wayFragment");
                    wayFragment = WayFragment.getInstance();
                    fragmentManager.beginTransaction().add(R.id.layout_fragment, wayFragment, "wayFragment").commit();
                    dynamicFragment = (DynamicFragment) fragmentManager.findFragmentByTag("dynamicFragment");
                    if (dynamicFragment != null){
                        fragmentManager.beginTransaction().hide(dynamicFragment).commit();
                    }
                    carFragment = (CarFragment) fragmentManager.findFragmentByTag("carFragment");
                    if (carFragment != null){
                        fragmentManager.beginTransaction().hide(carFragment).commit();
                    }
                }else{
//                    Log.i("TAG", "------首页wayFragment已创建");
                    if (dynamicFragment == null){
                        dynamicFragment = DynamicFragment.getInstance();
                    }
                    if (carFragment == null){
                        carFragment = CarFragment.getInstance();
                    }
                    fragmentManager.beginTransaction().show(wayFragment).hide(dynamicFragment).hide(carFragment).commit();
                }
                break;
            case R.id.layout_sign_in://签到
                if (getResources().getString(R.string.sign_in).equals(tvSignin.getText().toString())) {
                    mPresenter.userSign();
                }
                break;
            case R.id.img_my_profile://我的资料
                startActivityForResult(new Intent(MainActivity.this, MyProfileActivity.class), MYPROFILE_CODE);
                break;
            case R.id.layout_wodefabu://我的发布
                startActivity(new Intent(MainActivity.this, MyDynamicActivity.class));
                break;
            case R.id.layout_message://消息中心
                startActivity(new Intent(MainActivity.this, MessageActivity.class));
                break;
            case R.id.layout_paiming://车辆排名
                startActivity(new Intent(MainActivity.this, RankingListActivity.class));
                break;
            case R.id.layout_binding_car://绑定车辆
                startActivity(new Intent(MainActivity.this, BindingCarActivity.class));
                break;
            case R.id.layout_people_nearby://附近的人
                startActivity(new Intent(MainActivity.this, PeopleNearbyActivity.class));
                break;
            case R.id.layout_setting://设置
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
            case R.id.layout_level://等级与金币
                startActivity(new Intent(MainActivity.this, GradeAndGoldActivity.class));
                break;
            case R.id.layout_huodongyueban://活动约伴
                startActivity(new Intent(MainActivity.this, HuodongYuebanActivity.class));
                break;
            case R.id.layout_my_track://我的轨迹
                startActivity(new Intent(MainActivity.this, MyTrackActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PUBLISH && resultCode == RESULT_OK){
            String s = data.getStringExtra("publish");
            if ("publish_ok".equals(s)){
                if (dynamicFragment !=null) {
                    dynamicFragment.refresh();
                }else{
                    layoutCar.performClick();
                    dynamicFragment.refresh();
                }
            }
        } else if (requestCode == MYPROFILE_CODE && resultCode == RESULT_OK) {
            Log.i("TAG", "-------MainActivity修改用户资料返回");
            tvNickname.setText(PreferencesUtils.getLoginInfo().getObj().getNickname());
            tvSignature.setText(PreferencesUtils.getLoginInfo().getObj().getSignature());
            if (null != imgMyProfile) {
                if (PreferencesUtils.getLoginInfo().getObj().getGender()) {
                    Glide.with(this).load(PreferencesUtils.getLoginInfo().getObj().getImgeUrl())
                            .error(R.mipmap.touxiang_boy_nor)
                            .into(imgMyProfile);
                }else {
                    Glide.with(this).load(PreferencesUtils.getLoginInfo().getObj().getImgeUrl())
                            .error(R.mipmap.touxiang_girl_nor)
                            .into(imgMyProfile);
                }
            }

            if (dynamicFragment !=null){
                dynamicFragment.refreshHeadPortrait();
            }
        }else if(requestCode == 0x129){
            LocationManager locationManager = (LocationManager) this
                    .getSystemService(Context.LOCATION_SERVICE);
            // 判断GPS模块是否开启，如果没有则开启
            if (locationManager
                    .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
                startActivity(new Intent(this, TrackRecordActivity.class));
            }
        }else if (requestCode == 0x130 && resultCode == RESULT_OK){
            DynamicListBean.ObjBean dynamic = (DynamicListBean.ObjBean) data.getSerializableExtra("dynamic");
            int position = data.getIntExtra("position", -1);
            if (null != dynamic) {
                dynamicFragment.refreshItem(dynamic, position);
            }
//            ToastUtils.showToast("刷新"+position);
            Log.i("TAG", "-------MainActivity刷新"+position);
        }
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayerStandard.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                ToastUtils.showToast(R.string.press_again_to_quit_app);
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                AppManager.getAppManager().AppExit(MainActivity.this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public DragLayout getDragLayout(){
        return dragLayout;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public TextView getGoldTV() {
        return tvGold;
    }

    @Override
    public ImageView getSigninImg() {
        return imgSignin;
    }

    @Override
    public TextView getSigninTV() {
        return tvSignin;
    }
}
