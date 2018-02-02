package com.mywaytec.myway.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.Constant;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 下载文件
 */

public class DownloadFileUtil {

    private Callback.Cancelable cancelable;
    private static Context mContext;
    private static DownloadFileUtil downloadFileUtil;
    private boolean isProgressBar;
    private boolean isInstall;

    public static DownloadFileUtil getInstance(){
        if (null == downloadFileUtil){
            downloadFileUtil = new DownloadFileUtil();
        }
        return downloadFileUtil;
    }

    public static DownloadFileUtil init(Context context){
        mContext = context;
        return getInstance();
    }

    public void isProgressBar(boolean isProgressBar){
        this.isProgressBar = isProgressBar;
    }

    public DownloadFileUtil isInstall(boolean isInstall){
        this.isInstall = isInstall;
        return getInstance();
    }

    /**
     * 下载文件
     * @return
     */
    public void downLoadFile(String url, final String path) {

        RequestParams params = new RequestParams(url);
        //设置断点续传
        params.setAutoResume(true);
        params.setSaveFilePath(path);
        cancelable = x.http().get(params, new Callback.ProgressCallback<File>() {

            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
//                Toast.makeText(x.app(), "开始下载", Toast.LENGTH_LONG).show();
                if (isProgressBar){
                    downloadDialog(mContext);
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                BigDecimal b = new BigDecimal((float) current / (float) total);
                float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                Log.e("tag",f1+"===================");
                int progress = (int)(current * 100 / total);
                if (isProgressBar) {
                    progressBar.setProgress(progress);
                    tvProgress.setText(progress + "%");
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    double t = total / 1024.0 / 1024.0;
                    String ts = decimalFormat.format(t);
                    double c = current / 1024.0 / 1024.0;
                    String cs = decimalFormat.format(c);
                    tvFilesize.setText(cs + "/" + ts + "MB");
                }
            }

            @Override
            public void onSuccess(File result) {
//                Toast.makeText(x.app(), "下载成功", Toast.LENGTH_LONG).show();
                if (path.contains(".apk")) {
                    installApp(path);
                }
                if (null != onDownloadComplete) {
                    onDownloadComplete.downloadComplete(result.getPath(),result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "----下载失败"+ex.getMessage(), ex);
                if (!SystemUtil.isNetworkConnected()) {
                    ToastUtils.showToast(R.string.请检查您的网络是否连接);
                }else {
                    Toast.makeText(x.app(), "下载失败，请检查您的网络", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                if (null != downloadDialog) {
                    downloadDialog.dismiss();
                }
            }
        });
    }

    /**
     * 安装apk
     * @param filePath
     */
    public void installApp(String filePath) {
        File _file = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(_file),
                "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

    public DownloadFileUtil start(String url, String filename) {
        String filePath = Constant.DEFAULT_PATH+"/download/";
        File file = new File(filePath);
        if (!file.exists()){
            file.mkdir();
        }
        if (null != url) {
            downLoadFile(url, filePath+filename);
        }
        return getInstance();
    }

    public void stop() {
        if (null != cancelable)
        cancelable.cancel();
    }

    private AlertDialog downloadDialog;
    private ProgressBar progressBar;
    private TextView tvProgress;
    private TextView tvFilesize;
    /** 下载dialog */
    public void downloadDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_download, null);
        tvProgress = (TextView) view.findViewById(R.id.tv_progress);
        tvFilesize = (TextView) view.findViewById(R.id.tv_file_size);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        progressBar.setMax(100);
        builder.setView(view);
        downloadDialog = builder.show();
        downloadDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        downloadDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                stop();
            }
        });
        downloadDialog.show();
    }

    public OnDownloadComplete onDownloadComplete;
    public void setOnDownloadComplete(OnDownloadComplete onDownloadComplete){
        this.onDownloadComplete = onDownloadComplete;
    }

    public interface OnDownloadComplete{
        void downloadComplete(String path, File result);
    }

}
