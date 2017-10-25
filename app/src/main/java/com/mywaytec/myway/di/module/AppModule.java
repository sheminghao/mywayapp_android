package com.mywaytec.myway.di.module;

import com.mywaytec.myway.APP;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.model.http.api.LongchuangApis;
import com.mywaytec.myway.model.http.api.MywayApis;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by codeest on 16/8/7.
 */

@Module
public class AppModule {
    private final APP application;

    public AppModule(APP application) {
        this.application = application;
    }

    @Provides
    @Singleton
    APP provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    RetrofitHelper provideRetrofitHelper(MywayApis mywayService, LongchuangApis longchuangApis) {
        return new RetrofitHelper(mywayService, longchuangApis);
    }

//    @Provides
//    @Singleton
//    RealmHelper provideRealmHelper() {
//        return new RealmHelper(application);
//    }
}
