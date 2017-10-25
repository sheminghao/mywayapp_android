package com.mywaytec.myway.ui.switchLanguage;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.ui.main.MainActivity;
import com.mywaytec.myway.ui.setting.SettingActivity;
import com.mywaytec.myway.utils.PreferencesUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class SwitchLanguageActivity extends BaseActivity<SwitchLanguagePresenter> {

    public static final int SWITCH_LANGUAGE = 0x131;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_chinese)
    TextView tvChinese;
    @BindView(R.id.tv_english)
    TextView tvEnglish;
    @BindView(R.id.tv_aotu)
    TextView tvAotu;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_switch_language;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.switch_language);
    }

    @Override
    protected void updateViews() {
    }

    @OnClick({R.id.tv_chinese, R.id.tv_english, R.id.tv_aotu})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_chinese://选择中文
                // 本地语言设置
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = Locale.CHINESE;
                res.updateConfiguration(conf, dm);
                Intent intent = new Intent(SwitchLanguageActivity.this, MainActivity.class);
                intent.putExtra("language", "简体中文");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                setResult(RESULT_OK, intent);
                startActivity(intent);
                PreferencesUtils.putString(SwitchLanguageActivity.this, "language", "zh");
                finish();
                break;
            case R.id.tv_english://选择英文
                // 本地语言设置
                Resources res2 = getResources();
                DisplayMetrics dm2 = res2.getDisplayMetrics();
                Configuration conf2 = res2.getConfiguration();
                conf2.locale = Locale.ENGLISH;
                res2.updateConfiguration(conf2, dm2);
                Intent intent2 = new Intent(SwitchLanguageActivity.this, MainActivity.class);
                intent2.putExtra("language", "english");
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                setResult(RESULT_OK, intent2);
                startActivity(intent2);
                PreferencesUtils.putString(SwitchLanguageActivity.this, "language", "en");
                finish();
                break;
            case R.id.tv_aotu://跟随系统
                // 本地语言设置
                Resources res3 = getResources();
                DisplayMetrics dm3 = res3.getDisplayMetrics();
                Configuration conf3 = res3.getConfiguration();
                conf3.locale = Locale.getDefault();
                res3.updateConfiguration(conf3, dm3);
                Intent intent3 = new Intent(SwitchLanguageActivity.this, MainActivity.class);
                intent3.putExtra("language", "aotu");
                intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                setResult(RESULT_OK, intent2);
                startActivity(intent3);
                PreferencesUtils.putString(SwitchLanguageActivity.this, "language", "aotu");
                finish();
                break;
        }
    }
}
