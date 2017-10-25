package com.mywaytec.myway.ui.bindingCar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.activity.ScanQRCodeActivity;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.utils.ToastUtils;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class BindingCarActivity extends BaseActivity<BindingCarPresenter> implements BindingCarView{

    private static final int REQUEST_CODE = 0x153;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.et_sn_code)
    EditText etSnCode;
    @BindView(R.id.tv_binding_car)
    TextView tvBindingCar;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_binding_car;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.vehicle_binding);
        mPresenter.initRecycler();
    }

    @Override
    protected void updateViews() {

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
    public TextView getBindingCarTV() {
        return tvBindingCar;
    }

    @OnClick({R.id.tv_binding_car, R.id.img_cheliang_saomiao})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_binding_car://绑定车辆
                String snCode = etSnCode.getText().toString().trim();
                mPresenter.bindingCar(snCode);
                break;
            case R.id.img_cheliang_saomiao://扫描二维码
                Intent intent = new Intent(BindingCarActivity.this, ScanQRCodeActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
//                    ToastUtils.showToast("解析结果:" + result);
                    etSnCode.setText(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    ToastUtils.showToast(R.string.解析二维码失败);
                }
            }
        }
    }
}
