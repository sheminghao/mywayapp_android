package com.mywaytec.myway.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mywaytec.myway.AppManager;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.Constant;
import com.mywaytec.myway.ui.login.LoginActivity;
import com.mywaytec.myway.utils.data.IsLogin;
import com.mywaytec.myway.view.OneButtonDialog;

/**
 *
 */

public class DialogUtils {

    public static AlertDialog confirmDialog;

    /** 是否确认dialog */
    public static void confirmDialog(Context context, String hint, View.OnClickListener mOnClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_confirm, null);
        TextView tvHint = (TextView) view.findViewById(R.id.tv_hint_text);
        tvHint.setText(hint);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog.dismiss();
            }
        });
        btnConfirm.setOnClickListener(mOnClickListener);
        builder.setView(view);
        confirmDialog = builder.show();
    }


    private static AlertDialog hintDialog;
    /** 提示信息dialog */
    public static void hintDialog(Context context, int imgRes, String hint){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_hint, null);
        TextView tvHint = (TextView) view.findViewById(R.id.tv_hint);
        tvHint.setText(hint);
        ImageView imgCancel = (ImageView) view.findViewById(R.id.img_cancel);
        ImageView imgHint = (ImageView) view.findViewById(R.id.img_hint);
        if (imgRes > 0){
            imgHint.setImageResource(imgRes);
        }else if (imgRes == 0){
            imgHint.setVisibility(View.GONE);
        }
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hintDialog.dismiss();
            }
        });
        builder.setView(view);
        hintDialog = builder.show();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int systemWidth = wm.getDefaultDisplay().getWidth();//屏幕宽度
        int wight = systemWidth * 3 / 4;
        WindowManager.LayoutParams params1 =
                hintDialog.getWindow().getAttributes();
        params1.width = wight;
        params1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        hintDialog.getWindow().setAttributes(params1);
    }


    //重新登录dialog
    public static void reLoginDialog(final Context context){
        final OneButtonDialog oneButtonDialog = new OneButtonDialog(context);
        oneButtonDialog.setText(R.string.用户验证失败);
        oneButtonDialog.setOnConfirmClickListener(new OneButtonDialog.OnConfirmClickListener() {
            @Override
            public void confirmClickListener() {
                context.startActivity(new Intent(context, LoginActivity.class));
                IsLogin.saveDynamicData(false);
                AppManager.getAppManager().finishAllActivity();
                oneButtonDialog.dismiss();
            }
        });
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int systemWidth = wm.getDefaultDisplay().getWidth();//屏幕宽度
        int wight = systemWidth * 3 / 4;
        WindowManager.LayoutParams params1 =
                oneButtonDialog.getWindow().getAttributes();
        params1.width = wight;
        params1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        oneButtonDialog.getWindow().setAttributes(params1);
        oneButtonDialog.show();
    }
}
