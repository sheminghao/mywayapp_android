package com.mywaytec.myway.ui.welcome;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.ui.login.LoginActivity;
import com.mywaytec.myway.ui.main.MainActivity;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxCountDown;
import com.mywaytec.myway.utils.data.IsLogin;

import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

public class StartActivity extends AppCompatActivity {

    @BindView(R.id.iv_entry)
    ImageView mIVEntry;
    @BindView(R.id.tv_skip)
    TextView tvSkip;

    private static final int ANIM_TIME = 500;

    public static final String FIRST_OPEN = "FIRST_OPEN";

    private static final int[] Imgs={
            R.mipmap.sc01_guanggaotu};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //      如果不是第一次启动app，则正常显示启动屏
//      透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        Locale locale = getResources().getConfiguration().locale;
        //初始化语言
        String language = PreferencesUtils.getString(StartActivity.this, "language", "aotu");
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if("zh".equals(language)){
            conf.setLocale(Locale.CHINESE);
        }else if("en".equals(language)){
            conf.setLocale(Locale.ENGLISH);
        }else {
            if("zh".equals(locale.getLanguage())) {
                conf.setLocale(Locale.CHINESE);
            }else {
                conf.setLocale(Locale.ENGLISH);
            }
        }
        res.updateConfiguration(conf, dm);

        ButterKnife.bind(this);
        tvSkip.setVisibility(View.GONE);

        // 判断是否是第一次开启应用
        boolean isFirstOpen = PreferencesUtils.getBoolean(this, FIRST_OPEN, true);
        // 如果是第一次启动，则先进入功能引导页
        if (isFirstOpen) {
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            finish();
//            return;
        }else {
            tvSkip.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    tvSkip.setVisibility(View.VISIBLE);
//                    startMainActivity();
                    startAnim();
                }
            }, 2000);
        }
    }

    private boolean isStart;
    private void startMainActivity(){
        Random random = new Random(SystemClock.elapsedRealtime());//SystemClock.elapsedRealtime() 从开机到现在的毫秒数（手机睡眠(sleep)的时间也包括在内）
        mIVEntry.setImageResource(Imgs[random.nextInt(Imgs.length)]);
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStart = true;
                startAnim();
            }
        });

        RxCountDown.countdown(3).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                if (!isStart) {
                    startAnim();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                tvSkip.setText(integer.toString()+" "+getResources().getString(R.string.skip));
            }
        });
    }

    private void startAnim() {
        if (!IsLogin.isLogin()) {
            startActivity(new Intent(StartActivity.this, LoginActivity.class));
        }else {
            startActivity(new Intent(StartActivity.this, MainActivity.class));
        }
         StartActivity.this.finish();
//         overridePendingTransition(R.anim.hold, R.anim.zoom_in_exit);
    }

    /**
     * 屏蔽物理返回按钮
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
