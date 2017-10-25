package com.mywaytec.myway.utils;


import com.mywaytec.myway.model.http.exception.ApiException;
import com.mywaytec.myway.model.http.response.MywayResponse;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by codeest on 2016/8/3.
 */
public class RxUtil {

    /**
     * 统一线程处理
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> rxSchedulerHelper() {    //compose简化线程
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 统一返回结果处理
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<MywayResponse<T>, T> handleMywayResult() {   //compose判断结果
        return new Observable.Transformer<MywayResponse<T>, T>() {
            @Override
            public Observable<T> call(Observable<MywayResponse<T>> mywayResponseObservable) {
                return mywayResponseObservable.flatMap(new Func1<MywayResponse<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(MywayResponse<T> tMywayHttpResponse) {
                        if(tMywayHttpResponse.getObj() != null) {
                            return createData(tMywayHttpResponse.getObj());
                        } else {
                            return Observable.error(new ApiException("服务器返回error"));
                        }
                    }
                });
            }
        };
    }

    /**
     * 生成Observable
     * @param <T>
     * @return
     */
    public static <T> Observable<T> createData(final T t) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(t);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}
