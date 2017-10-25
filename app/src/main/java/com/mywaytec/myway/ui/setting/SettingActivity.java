package com.mywaytec.myway.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.mywaytec.myway.AppManager;
import com.mywaytec.myway.R;
import com.mywaytec.myway.activity.MqttTestActivity;
import com.mywaytec.myway.activity.StoreActivity;
import com.mywaytec.myway.activity.ZhuyiShixiangActivity;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.ui.about.AboutActivity;
import com.mywaytec.myway.ui.feedback.FeedbackActivity;
import com.mywaytec.myway.ui.gprs.GPRSActivity;
import com.mywaytec.myway.ui.login.LoginActivity;
import com.mywaytec.myway.ui.switchLanguage.SwitchLanguageActivity;
import com.mywaytec.myway.ui.switchUnit.SwitchUnitActivity;
import com.mywaytec.myway.utils.CleanMessageUtil;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.data.IsLogin;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity<SettingPresenter> implements SettingView{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_language)
    TextView tvLanguage;
    @BindView(R.id.tv_clear_cache)
    TextView tvClearCache;

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
            R.id.tv_mqtt, R.id.tv_store, R.id.tv_gps})
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
        }
    }
}
