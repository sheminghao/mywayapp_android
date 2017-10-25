package com.mywaytec.myway.di.component;

import com.mywaytec.myway.APP;
import com.mywaytec.myway.di.module.AppModule;
import com.mywaytec.myway.di.module.HttpModule;
import com.mywaytec.myway.di.module.PageModule;
import com.mywaytec.myway.model.http.RetrofitHelper;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by codeest on 16/8/7.
 */

@Singleton
@Component(modules = {AppModule.class, HttpModule.class, PageModule.class})
public interface AppComponent {

    APP getContext();  // 提供App的Context

    RetrofitHelper retrofitHelper();  //提供http的帮助类

//    RealmHelper realmHelper();    //提供数据库帮助类

//    ZhihuMainFragment zhihuMainFragment();

//    GankMainFragment gankMainFragment();

//    WechatMainFragment wechatMainFragment();

//    GoldMainFragment goldMainFragment();

//    VtexMainFragment vtexMainFragment();

//    LikeFragment likeFragment();

//    SettingFragment settingFragment();

//    AboutFragment aboutFragment();
}
