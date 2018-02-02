package com.mywaytec.myway.activity;

import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.utils.AppUtils;

import butterknife.BindView;

public class AboutMywayActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_about)
    TextView tvAbout;
    @BindView(R.id.activity_about_myway)
    LinearLayout activityAboutMyway;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_about_myway;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initViews() {
        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        activityAboutMyway.setPadding(0, AppUtils.getStatusBar(), 0, 0);
        tvTitle.setText(R.string.about_mw);
        tvAbout.setText("深圳曼威科技有限公司成立于2015年10月，为上海龙创汽车设计股份有限公司下属全资子公司。\n" +
                "\n" +
                "上海龙创汽车设计股份有限公司龙创成立于2000年，中国最大的独立汽车设计公司之一，为国内大多数知名汽车公司提供设计开发服务，现为新三版上市公司，被认定为国家级工业设计中心、高新技术企业。\n" +
                "\n" +
                "曼威科技是一家专注于智能产品的研发与设计、品控、销售的科技型公司。公司产品主要涉及智能交通产品、智能穿戴产品、多用途无人机产品等。团队拥有最前沿的设计理念及丰富的产品研发设计经验，曼威的产品将以更卓越的品质、更雅致的设计及更愉悦的用户体验为全球用户带来更优质的使用感受。\n");
    }

    @Override
    protected void updateViews() {

    }
}
