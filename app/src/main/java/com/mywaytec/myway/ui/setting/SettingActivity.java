package com.mywaytec.myway.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.mywaytec.myway.AppManager;
import com.mywaytec.myway.R;
import com.mywaytec.myway.activity.MqttTestActivity;
import com.mywaytec.myway.activity.StoreActivity;
import com.mywaytec.myway.activity.ZhuyiShixiangActivity;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.model.bean.TestServiceBean;
import com.mywaytec.myway.ui.about.AboutActivity;
import com.mywaytec.myway.ui.feedback.FeedbackActivity;
import com.mywaytec.myway.ui.gprs.GPRSActivity;
import com.mywaytec.myway.ui.login.LoginActivity;
import com.mywaytec.myway.ui.switchLanguage.SwitchLanguageActivity;
import com.mywaytec.myway.ui.switchUnit.SwitchUnitActivity;
import com.mywaytec.myway.utils.CleanMessageUtil;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.data.BleInfo;
import com.mywaytec.myway.utils.data.IsLogin;
import com.mywaytec.myway.utils.data.TestServiceData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;

public class SettingActivity extends BaseActivity<SettingPresenter> implements SettingView{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_language)
    TextView tvLanguage;
    @BindView(R.id.tv_clear_cache)
    TextView tvClearCache;
    @BindView(R.id.spinner_host)
    Spinner spinner;
    @BindView(R.id.et_host)
    EditText etHost;

    TestServiceBean testServiceBean;
    ArrayAdapter<String> adapter;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.my_setting);
        String language = PreferencesUtils.getString(this, "language", "aotu");
        if("zh".equals(language)){
            tvLanguage.setText("简体中文");
        }else if("en".equals(language)){
            tvLanguage.setText("English");
        }else {
            tvLanguage.setText(R.string.aotu);
        }

        tvClearCache.setText(CleanMessageUtil.getTotalCacheSize(this)+"");


        //初始化测试服务器地址
        testServiceBean = TestServiceData.getTestServiceData();
        if (null == testServiceBean || null == testServiceBean.getHost()){
            testServiceBean = new TestServiceBean();
            List<String> hosts = new ArrayList<>();
            hosts.add("http://120.77.249.52:8090");
            hosts.add("http://192.168.1.162:8080");
            hosts.add("http://120.78.182.49:8071");
            testServiceBean.setHost(hosts);
            TestServiceData.saveTestServiceData(testServiceBean);
        }

        // 建立Adapter并且绑定数据源
        adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, testServiceBean.getHost());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spinner .setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String host = testServiceBean.getHost().get(pos);
                testServiceBean.setCurrentHost(host);
                TestServiceData.saveTestServiceData(testServiceBean);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    @Override
    protected void updateViews() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @OnClick({R.id.layout_about, R.id.layout_switch_language, R.id.layout_feedback,
            R.id.layout_unit_size, R.id.tv_login_out, R.id.layout_clear_cache, R.id.layout_zhuyishixiang,
            R.id.tv_mqtt, R.id.tv_store, R.id.tv_gps, R.id.btn_add_host})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.layout_about://关于
                startActivity(new Intent(SettingActivity.this, AboutActivity.class));
                break;
            case R.id.layout_switch_language://切换语言
                startActivity(new Intent(SettingActivity.this, SwitchLanguageActivity.class));
                break;
            case R.id.layout_unit_size://尺寸单位
                startActivity(new Intent(SettingActivity.this, SwitchUnitActivity.class));
                break;
            case R.id.layout_feedback://意见反馈
                startActivity(new Intent(SettingActivity.this, FeedbackActivity.class));
                break;
            case R.id.layout_zhuyishixiang://注意事项
                startActivity(new Intent(SettingActivity.this, ZhuyiShixiangActivity.class));
                break;
            case R.id.layout_clear_cache://清除缓存
                CleanMessageUtil.clearAllCache(this);
                tvClearCache.setText(CleanMessageUtil.getTotalCacheSize(this)+"");
                break;
            case R.id.tv_login_out://退出登录
                RongIM.getInstance().logout();
                BleInfo.clearBleInfo();//清除蓝牙缓存信息
                CleanMessageUtil.clearAllCache(this);//清除缓存
                startActivity(new Intent(this, LoginActivity.class));
                IsLogin.saveDynamicData(false);
                AppManager.getAppManager().finishAllActivity();
                break;
            case R.id.tv_store://商城
                startActivity(new Intent(SettingActivity.this, StoreActivity.class));
                break;
            case R.id.tv_gps:
                startActivity(new Intent(SettingActivity.this, GPRSActivity.class));
                break;
            case R.id.tv_mqtt://mqtt测试
                startActivity(new Intent(SettingActivity.this, MqttTestActivity.class));
                break;
            case R.id.btn_add_host://添加
                addHost();
                break;
        }
    }

    private void addHost(){
        testServiceBean = TestServiceData.getTestServiceData();
        if (null != testServiceBean){
            for (int i = 0; i < testServiceBean.getHost().size(); i++) {
                if (etHost.getText().toString().trim().equals(testServiceBean.getHost().get(i))){
                    return;
                }
            }
            testServiceBean.getHost().add(etHost.getText().toString().trim());
            TestServiceData.saveTestServiceData(testServiceBean);
            adapter.clear();
            adapter.addAll(testServiceBean.getHost());
            adapter.notifyDataSetChanged();
        }
    }
}
