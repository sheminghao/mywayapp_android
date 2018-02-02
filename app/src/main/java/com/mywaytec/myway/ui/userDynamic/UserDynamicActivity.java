package com.mywaytec.myway.ui.userDynamic;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.model.bean.DynamicListBean;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;

public class UserDynamicActivity extends BaseActivity<UserDynamicPresenter> implements UserDynamicView{

    @BindView(R.id.recyclerView)
    LRecyclerView recyclerView;
    @BindView(R.id.tv_guanzhu)
    TextView tvGuanzhu;
    @BindView(R.id.img_guanzhu)
    ImageView imgGuanzhu;

    String otherUid;
    String nickName;

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

        otherUid = getIntent().getStringExtra("uid");
        nickName = getIntent().getStringExtra("nickName");
        String headPortrait = getIntent().getStringExtra("headPortrait");
        mPresenter.initRecyclerView(otherUid);
    }

    @Override
    protected void updateViews() {

    }

    @OnClick({R.id.layout_guanzhu, R.id.layout_chat})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.layout_chat://聊天
                RongIM.getInstance().startPrivateChat(this, otherUid,
                        nickName);
                break;
            case R.id.layout_guanzhu:
                if (getResources().getString(R.string.follow).equals(tvGuanzhu.getText().toString())){//关注
                    mPresenter.attention(otherUid);
                }else if(getResources().getString(R.string.unfollow).equals(tvGuanzhu.getText().toString())) {//取消关注
                    mPresenter.cancelAttention(otherUid);
                }
                break;
        }
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
    public TextView getGuanzhuTV() {
        return tvGuanzhu;
    }

    @Override
    public ImageView getGuanzhuImg() {
        return imgGuanzhu;
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
