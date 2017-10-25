package com.mywaytec.myway.ui.gprs.editFence;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.model.bean.DianziweilanBean;
import com.mywaytec.myway.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class EditFenceActivity extends BaseActivity<EditFencePresenter> implements EditFenceView{

    public static final String DIANZIWEILAN = "DIANZIWEILAN";

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.et_name)
    EditText etName;

    DianziweilanBean.ObjBean dianziweilan;
    int position;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_edit_fence;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.electro_fence);
        tvRight.setText(R.string.complete);

        position = getIntent().getIntExtra("position", -1);
        dianziweilan = (DianziweilanBean.ObjBean) getIntent().getSerializableExtra(DIANZIWEILAN);
        if (null != dianziweilan) {
            etName.setText(dianziweilan.getName());
        }
    }

    @Override
    protected void updateViews() {

    }

    @OnClick({R.id.tv_right, R.id.tv_detele})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_right://完成
                if (null != dianziweilan){
                    String newName = etName.getText().toString().trim();
                    if (TextUtils.isEmpty(newName)){
                        ToastUtils.showToast(R.string.please_enter_name);
                        return;
                    }
                    mPresenter.changeName(dianziweilan.getId(), newName, position);
                }
                break;
            case R.id.tv_detele://删除电子围栏
                mPresenter.deleteFence(dianziweilan.getId(), position);
                break;
        }
    }

    @Override
    public Context getContext() {
        return this;
    }
}
