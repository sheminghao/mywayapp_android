package com.mywaytec.myway.ui.feedback;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.view.MaxByteLengthEditText;

import butterknife.BindView;
import butterknife.OnClick;

public class FeedbackActivity extends BaseActivity<FeedbackPresenter> implements FeedbackView{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.et_feedback)
    MaxByteLengthEditText etFeedback;
    @BindView(R.id.tv_text_num)
    TextView tvTextNum;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.feedback);
        tvRight.setText(R.string.submit);
        etFeedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                int current = etFeedback.getText().toString().length();
                tvTextNum.setText(current+"/255");
                if (current >255 ){
                    String content = etFeedback.getText().toString();
                    content = content.substring(0, 255);
                    etFeedback.setText(content);
//                    ToastUtils.showToast("字数已达到上限");
                }
            }
        });
    }

    @Override
    protected void updateViews() {

    }

    @OnClick({R.id.tv_right})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_right://提交
                mPresenter.feedback(this, etFeedback.getText().toString().trim());
                break;
        }
    }

    @Override
    public Context getContext() {
        return this;
    }
}
