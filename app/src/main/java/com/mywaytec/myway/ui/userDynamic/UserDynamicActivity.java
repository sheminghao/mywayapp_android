package com.mywaytec.myway.ui.userDynamic;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.model.bean.DynamicListBean;

import butterknife.BindView;
import butterknife.OnClick;

public class UserDynamicActivity extends BaseActivity<UserDynamicPresenter> implements UserDynamicView{

    @BindView(R.id.recyclerView)
    LRecyclerView recyclerView;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_user_dynamic;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        String otherUid = getIntent().getStringExtra("otherUid");
        mPresenter.initRecyclerView(otherUid);
    }

    @Override
    protected void updateViews() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public LRecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x132 && resultCode == RESULT_OK){
            DynamicListBean.ObjBean dynamic = (DynamicListBean.ObjBean) data.getSerializableExtra("dynamic");
            int position = data.getIntExtra("position", -1);
            if (null != dynamic) {
                mPresenter.refreshItem(dynamic, position);
            }
        }
    }
}
