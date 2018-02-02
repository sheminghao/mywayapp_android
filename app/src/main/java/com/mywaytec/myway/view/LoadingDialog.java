package com.mywaytec.myway.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.mywaytec.myway.R;

/**
 * Created by shemh on 2017/3/29.
 */

public class LoadingDialog extends Dialog {

    private LoadingView loadingView;

    public LoadingDialog(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(R.color.transparency);
        setCanceledOnTouchOutside(false);

        View view = View.inflate(context, R.layout.dialog_loading, null);
        loadingView = (LoadingView) view.findViewById(R.id.loading_view);
        loadingView.start();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        addContentView(view, params);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (null != loadingView) {
            loadingView.stop();
        }
    }
}
