package com.mywaytec.myway.ui.gprs.electronicFence;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.WrapSlidingDrawer;


import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;

import static com.mywaytec.myway.ui.gprs.GPRSActivity.SNCODE;

/**
 * 电子围栏
 * 通过地图设置电子围栏发送给设备端
 */
public class ElectronicFenceActivity extends BaseActivity<ElectronicFencePresenter> implements ElectronicFenceView {

    public static final int DIANZIWEILAN_REQUESTCODE = 0x160;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.mapview)
    MapView mMapView;
    @BindView(R.id.slidingDrawer)
    WrapSlidingDrawer slidingDrawer;
    @BindView(R.id.layout_more)
    LinearLayout layoutMore;
    @BindView(R.id.rb_yuan)
    RadioButton rbYuan;
    @BindView(R.id.rb_zidingyi)
    RadioButton rbZidingyi;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.tv_current_radius)
    TextView tvCurrentRadius;
    @BindView(R.id.tv_select_hint)
    TextView tvSelectHint;
    @BindView(R.id.layout_yuan)
    LinearLayout layoutYuan;
    @BindView(R.id.layout_zidingyi)
    LinearLayout layoutZidingyi;
    @BindView(R.id.layout_select_dianziweilan)
    LinearLayout layoutSelectDianziweilan;
    @BindView(R.id.img_show_weilan)
    ImageView imgShowWeilan;
    @BindView(R.id.dianziweilan_recyclerView)
    RecyclerView dianziweilanRecyclerView;
    @BindView(R.id.et_fence_name)
    EditText etFenceName;

    private DecimalFormat df = new DecimalFormat("######0.0");
    String snCode;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_electronic_fence;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.electro_fence);

        snCode = getIntent().getStringExtra(SNCODE);

        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            public void onDrawerOpened() {
                imgShowWeilan.setImageResource(R.mipmap.dianziweilan_xiala);
            }

        });

        slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            public void onDrawerClosed() {
                imgShowWeilan.setImageResource(R.mipmap.dianziweilan_shangla);
            }

        });

        rbYuan.setChecked(true);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvCurrentRadius.setText(df.format((progress+100)/1000.0) + "km");
                mPresenter.updateYuan(progress+100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        rbYuan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    layoutYuan.setVisibility(View.VISIBLE);
                    layoutZidingyi.setVisibility(View.GONE);
                    mPresenter.setWeilanType(0);
                }else {
                    layoutYuan.setVisibility(View.GONE);
                    layoutZidingyi.setVisibility(View.VISIBLE);
                    mPresenter.setWeilanType(1);
                    tvSelectHint.setText(R.string.select_location_by_clicking_in_the_map);
                }
            }
        });

        mPresenter.initRecyclerView(snCode);

        layoutSelectDianziweilan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    @Override
    protected void updateViews() {

    }

    @OnClick({R.id.tv_tianjia_weilan, R.id.tv_dianziwei_queding, R.id.tv_dianziwei_quxiao,
            R.id.tv_zidingyi_chongxuan, R.id.tv_zidingyi_cexiao})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_tianjia_weilan://添加围栏
                slidingDrawer.close();
                layoutSelectDianziweilan.setVisibility(View.VISIBLE);
                rbYuan.setChecked(true);
                mPresenter.setWeilanType(0);//添加围栏默认为圆形
                break;
            case R.id.tv_dianziwei_queding://发送电子围栏
                String fenceName = etFenceName.getText().toString().trim();
                if (TextUtils.isEmpty(fenceName)){
                    ToastUtils.showToast(R.string.please_enter_name);
                    return;
                }

                if (mPresenter.getWeilanType() == 0) {//添加圆形电子围栏
                    mPresenter.addYuanDianziweilan(snCode, fenceName);
                }else if (mPresenter.getWeilanType() == 1){//添加自定义电子围栏
                    mPresenter.addZidingyiDianziweilan(snCode, fenceName);
                }
                break;
            case R.id.tv_dianziwei_quxiao://取消
                layoutSelectDianziweilan.setVisibility(View.GONE);
                mPresenter.setWeilanType(-1);//取消后，设置不能选取围栏
                break;
            case R.id.tv_zidingyi_chongxuan://自定义电子围栏重选
                mPresenter.chongxuan();
                break;
            case R.id.tv_zidingyi_cexiao://自定义电子围栏撤销
                mPresenter.cexiao();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DIANZIWEILAN_REQUESTCODE && resultCode == RESULT_OK){
            String newName = data.getStringExtra("newName");
            int position = data.getIntExtra("position", -1);
            String type = data.getStringExtra("type");
            if (position != -1) {
                if ("change".equals(type)) {
                    mPresenter.updateName(newName, position);
                }else if ("delete".equals(type)){
                    mPresenter.updateList(position);
                }
            }
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public MapView getMapView() {
        return mMapView;
    }

    @Override
    public SeekBar getSeekBar() {
        return seekBar;
    }

    @Override
    public TextView getSelectHintTV() {
        return tvSelectHint;
    }

    @Override
    public RecyclerView getDianziweilanRecyclerView() {
        return dianziweilanRecyclerView;
    }

    @Override
    public LinearLayout getSelectDianziweilanLayout() {
        return layoutSelectDianziweilan;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mPresenter.destroy();
        mMapView.onDestroy();
        mMapView = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }
}
