package com.mywaytec.myway.ui.switchUnit;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 尺寸单位
 */
public class SwitchUnitActivity extends BaseActivity<SwitchUnitPresenter> {

    public static final int SWITCH_UNIT = 0x132;

    @BindView(R.id.tv_gongzhi)
    TextView tvGongzhi;
    @BindView(R.id.tv_yingzhi)
    TextView tvYingzhi;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_switch_unit;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.unit_size);
    }

    @Override
    protected void updateViews() {

    }

    @OnClick({R.id.tv_gongzhi, R.id.tv_yingzhi})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_gongzhi://选择公制
                Intent intent = new Intent();
                intent.putExtra("unit", tvGongzhi.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.tv_yingzhi://选择英制
                Intent intent2 = new Intent();
                intent2.putExtra("unit", tvYingzhi.getText().toString());
                setResult(RESULT_OK, intent2);
                finish();
                break;
        }
    }
}
