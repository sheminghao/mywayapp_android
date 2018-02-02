package com.mywaytec.myway.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.mywaytec.myway.APP;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.IBaseView;
import com.mywaytec.myway.model.http.exception.ApiException;
import com.mywaytec.myway.utils.SystemUtil;
import com.mywaytec.myway.utils.ToastUtils;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by codeest on 2017/2/23.
 */

public abstract class CommonSubscriber<T> extends Subscriber<T> {
    private IBaseView mView;
    private String mErrorMsg;
    private LoadingDialog loadingDialog;
    private boolean isLoading;
    private Context context;

    protected CommonSubscriber( ){
    }

    protected CommonSubscriber(IBaseView view){
        this.mView = view;
    }

    protected CommonSubscriber(IBaseView view, Context context, boolean isLoading){
        this.mView = view;
        this.isLoading = isLoading;
        this.context = context;
    }

    protected CommonSubscriber(IBaseView view, String errorMsg){
        this.mView = view;
        this.mErrorMsg = errorMsg;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isLoading) {
            loadingDialog = new LoadingDialog(context);
            loadingDialog.show();
        }
    }

    @Override
    public void onCompleted() {
        if (null != loadingDialog){
            loadingDialog.dismiss();
        }
    }

    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    @Override
    public void onError(Throwable e) {
        Log.i("TAG", "------"+e.getMessage());
        if (null != loadingDialog){
            loadingDialog.dismiss();
        }
        //TODO 中英文
        if (!SystemUtil.isNetworkConnected()){
            ToastUtils.showToast("请检查网络是否连接");
        }
        if (mView == null)
            return;
        if (mErrorMsg != null && !TextUtils.isEmpty(mErrorMsg)) {
//            mView.showError(mErrorMsg);
        } else if (e instanceof ApiException) {
//            mView.showError(e.toString());
        }else if (e instanceof HttpException) {
//            mView.showError("数据加载失败ヽ(≧Д≦)ノ");
            ToastUtils.showToast(R.string.network_connection_is_failed);
            HttpException httpException = (HttpException) e;
            switch(httpException.code()){
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    break;
            }
        } else {
//            mView.showError("未知错误ヽ(≧Д≦)ノ");
        }
        try {
            if (null != e &&null != e.getMessage()) {
                String[] values = e.getMessage().split(":");
                if (null != values && values.length>1) {
                    ToastUtils.showToast(values[1]);
                }else {
                    ToastUtils.showToast(e.getMessage());
                }
            }
        }catch (Exception exception){
        }
    }
}
