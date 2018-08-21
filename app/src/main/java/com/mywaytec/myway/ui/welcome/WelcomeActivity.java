package com.mywaytec.myway.ui.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.adapter.WelcomePagerAdapter;
import com.mywaytec.myway.ui.login.LoginActivity;
import com.mywaytec.myway.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.vp_guide)
    ViewPager vp;
    @BindView(R.id.btn_enter)
    Button startBtn;

    private WelcomePagerAdapter adapter;
    private List<ImageView> views;

    // 引导页中文图片资源
    private int[] pics_ch = {R.mipmap.yindaoye01_ch,
            R.mipmap.yindaoye02_ch, R.mipmap.yindaoye03_ch};

    // 引导页中文图片资源
    private int[] pics_en = {R.mipmap.yindaoye01_en,
            R.mipmap.yindaoye02_en, R.mipmap.yindaoye03_en};

    // 引导页图片资源
    private int[] pics = {R.mipmap.yindaoye01_en,
            R.mipmap.yindaoye02_en, R.mipmap.yindaoye03_en};

    // 底部小点图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        startBtn.setOnClickListener(this);

        init();
    }

    public void init() {
        views = new ArrayList<ImageView>();

        Locale locale = getResources().getConfiguration().locale;
        if("zh".equals(locale.getLanguage())) {
            pics = pics_ch;
        }else {
            pics = pics_en;
        }

        // 初始化引导页视图列表
        for (int i = 0; i < pics.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(pics[i]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            views.add(imageView);

        }

        adapter = new WelcomePagerAdapter(views);
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(new PageChangeListener());

//        initDots();
    }

    private void initDots() {
        dots = new ImageView[pics.length];

        // 循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            // 得到一个LinearLayout下面的每一个子元素
//            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(false);// 都设为灰色
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(true); // 设置为白色，即选中状态

    }

    /**
     * 设置当前指示点
     *
     * @param position
     */
    private void setCurDot(int position) {
        if (position < 0 || position > pics.length || currentIndex == position) {
            return;
        }
        dots[position].setEnabled(true);
        dots[currentIndex].setEnabled(false);
        currentIndex = position;
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int position) {

        }

        @Override
        public void onPageScrolled(int position, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            // 设置底部小点选中状态
//            setCurDot(position);
            if (position == pics.length-1){
                startBtn.setVisibility(View.VISIBLE);
            }else{
                startBtn.setVisibility(View.GONE);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 如果切换到后台，就设置下次不进入功能引导页
        PreferencesUtils.putBoolean(WelcomeActivity.this, StartActivity.FIRST_OPEN, false);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        enterMainActivity();
    }

    private void enterMainActivity() {
        Intent intent = new Intent(WelcomeActivity.this,
                LoginActivity.class);
        startActivity(intent);
        PreferencesUtils.putBoolean(WelcomeActivity.this, StartActivity.FIRST_OPEN, false);
        finish();
    }
}
