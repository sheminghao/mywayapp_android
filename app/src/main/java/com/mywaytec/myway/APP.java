package com.mywaytec.myway;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.widget.ImageView;

import com.antfortune.freeline.FreelineCore;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.bumptech.glide.Glide;
import com.github.moduth.blockcanary.BlockCanary;
import com.mywaytec.myway.base.AppBlockCanaryContext;
import com.mywaytec.myway.di.component.AppComponent;
import com.mywaytec.myway.di.component.DaggerAppComponent;
import com.mywaytec.myway.di.module.AppModule;
import com.mywaytec.myway.di.module.HttpModule;
import com.mywaytec.myway.di.module.PageModule;
import com.squareup.leakcanary.LeakCanary;
import com.zhy.autolayout.config.AutoLayoutConifg;

import org.xutils.x;

import cn.smssdk.SMSSDK;
import io.rong.imkit.RongIM;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by shemh on 2017/2/17.
 */

public class APP extends MultiDexApplication {

    public static APP instance;
    public static AppComponent appComponent;

    public static final boolean ENCRYPTED = true;
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        x.Ext.init(this);//Xutils初始化

        FreelineCore.init(this);//Freeline初始化

        //检测内存泄漏
//        LeakCanary.install(this);
//
        // 在主进程初始化调用哈, ANR检测工具
//        BlockCanary.install(this, new AppBlockCanaryContext()).start();

        //初始化百度地图
        SDKInitializer.initialize(this);
        //设置百度地图的坐标类型
        SDKInitializer.setCoordType(CoordType.GCJ02);//默认为BD09LL坐标

        //初始化短信验证码SDK
        SMSSDK.initSDK(this, "1c63698646b00", "dec81c14fd6a15ab206527ac60c4b6f7");

        //初始化GreenDao
        initGreenDao();

        //融云初始化
        RongIM.init(this);

        //设置字体
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/hwxh.ttf")
//                .setFontAttrId(R.attr.fontPath).build());

        AutoLayoutConifg.getInstance().useDeviceSize();
    }

    private void initGreenDao() {
        MySQLiteOpenHelper devOpenHelper = new MySQLiteOpenHelper(this, "notes-db", null);
        DaoMaster mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        daoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public static synchronized APP getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public static AppComponent getAppComponent(){
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(instance))
                    .httpModule(new HttpModule())
                    .pageModule(new PageModule())
                    .build();
        }
        return appComponent;
    }

    //加载图片
    public static void loadImg(String url, ImageView iv){
        Glide.with(instance).load(url).error(R.mipmap.icon_default)
                .placeholder(R.mipmap.icon_default)
                .centerCrop()
                .into(iv);
    }

    //加载图片(淡入加载的效果)
    public static void loadImgCrossFade(String url, ImageView iv){
        Glide.with(instance).load(url).error(R.mipmap.icon_default)
                .placeholder(R.mipmap.icon_default)
                .centerCrop()
                .crossFade(300) // 可设置时长，默认“300ms”
                .into(iv);
    }
}
