package com.mywaytec.myway.di.module;

import android.text.TextUtils;

import com.mywaytec.myway.APP;
import com.mywaytec.myway.BuildConfig;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.Constant;
import com.mywaytec.myway.di.qualifier.LongchuangUrl;
import com.mywaytec.myway.di.qualifier.MywayUrl;
import com.mywaytec.myway.model.bean.TestServiceBean;
import com.mywaytec.myway.model.http.api.LongchuangApis;
import com.mywaytec.myway.model.http.api.MywayApis;
import com.mywaytec.myway.utils.SystemUtil;
import com.mywaytec.myway.utils.data.TestServiceData;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by codeest on 2017/2/26.
 */

@Module
public class HttpModule {

    TestServiceBean testServiceBean;

    @Singleton
    @Provides
    Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }


    @Singleton
    @Provides
    OkHttpClient.Builder provideOkHttpBuilder() {
        return new OkHttpClient.Builder();
    }

    @Singleton
    @Provides
    @MywayUrl
    Retrofit provideMywayRetrofit(Retrofit.Builder builder, OkHttpClient client) {
        String host = MywayApis.HOST;
        testServiceBean = TestServiceData.getTestServiceData();
        if (null != testServiceBean && !TextUtils.isEmpty(testServiceBean.getCurrentHost())){
            host = testServiceBean.getCurrentHost();
        }
        return createRetrofit(builder, client, host);
    }

    @Singleton
    @Provides
    @LongchuangUrl
    Retrofit provideLongchuangRetrofit(Retrofit.Builder builder, OkHttpClient client) {
        return createRetrofit(builder, client, LongchuangApis.HOST);
    }

    @Singleton
    @Provides
    OkHttpClient provideClient(OkHttpClient.Builder builder) {
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(loggingInterceptor);
        }
        File cacheFile = new File(Constant.PATH_CACHE);
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!SystemUtil.isNetworkConnected()) {
                    request = request.newBuilder()
                            .addHeader("deviceType", "android")
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }else {
                    request = request.newBuilder()
                            .addHeader("deviceType", "android")
                            .build();
                }
                Response response = chain.proceed(request);
                if (SystemUtil.isNetworkConnected()) {
                    int maxAge = 0;
                    // 有网络时, 不缓存, 最大保存时长为0
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Connected", APP.getInstance().getString(R.string.请检查您的网络是否连接))
//                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                            .message("请检查您的网络是否连接")
                            .removeHeader("Pragma")
                            .build();
                }
                return response;
            }
        };
//        Interceptor apikey = new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//                request = request.newBuilder()
//                        .addHeader("apikey",Constants.KEY_API)
//                        .build();
//                return chain.proceed(request);
//            }
//        }
//        设置统一的请求头部参数
//        builder.addInterceptor(apikey);
        //设置缓存
        builder.addNetworkInterceptor(cacheInterceptor);
        builder.addInterceptor(cacheInterceptor);
        builder.cache(cache);
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }

    @Singleton
    @Provides
    MywayApis provideMywayService(@MywayUrl Retrofit retrofit) {
        return retrofit.create(MywayApis.class);
    }

    @Singleton
    @Provides
    LongchuangApis provideLongchuangService(@LongchuangUrl Retrofit retrofit) {
        return retrofit.create(LongchuangApis.class);
    }

    private Retrofit createRetrofit(Retrofit.Builder builder, OkHttpClient client, String url) {
        return builder
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
