package com.mywaytec.myway.view;

import android.app.Dialog;
import android.content.Context;
import android.icu.text.IDNA;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.mywaytec.myway.R;

/**
 * Created by shemh on 2017/9/21.
 */

public class OneButtonDialog extends Dialog {

    private TextView tvText;
    private TextView tvConfirm;
    private OnConfirmClickListener onConfirmClickListener;

    public OneButtonDialog(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setBackgroundDrawableResource(R.color.transparency);
        setCanceledOnTouchOutside(false);

        View view = View.inflate(context, R.layout.dialog_one_button, null);
        tvText = (TextView) view.findViewById(R.id.tv_text);
        tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onConfirmClickListener){
                    onConfirmClickListener.confirmClickListener();
                }
            }
        });
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        view.setBackgroundResource(R.drawable.shape_corner_white);
        addContentView(view, params);
    }

    public void setText(String text){
        tvText.setText(text);
    }

    public void setText(int text){
        tvText.setText(text);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public interface OnConfirmClickListener{
        void confirmClickListener();
    }

    public void setOnConfirmClickListener(OnConfirmClickListener onConfirmClickListener){
        this.onConfirmClickListener = onConfirmClickListener;
    }
}
