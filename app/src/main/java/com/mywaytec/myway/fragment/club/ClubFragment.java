package com.mywaytec.myway.fragment.club;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseFragment;
import com.mywaytec.myway.ui.im.searchClub.SearchClubActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by shemh on 2017/11/28.
 */

public class ClubFragment extends BaseFragment<ClubPresenter> implements ClubView{

    @BindView(R.id.recyclerView_create)
    RecyclerView recyclerViewCreate;
    @BindView(R.id.recyclerView_join)
    RecyclerView recyclerViewJoin;
    @BindView(R.id.layout_noclub_hint)
    LinearLayout layoutNoclubHint;
    @BindView(R.id.tv_create)
    TextView tvCreate;
    @BindView(R.id.tv_join)
    TextView tvJoin;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_club;
    }

    @Override
    protected void initInjector() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected void initViews() {
        mPresenter.initData();
    }

    @Override
    protected void updateViews() {

    }

    @OnClick({R.id.layout_search_club})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.layout_search_club://搜索俱乐部
                Intent intent= new Intent(getActivity(), SearchClubActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public RecyclerView getCreateRecyclerView() {
        return recyclerViewCreate;
    }

    @Override
    public RecyclerView getJoinRecyclerView() {
        return recyclerViewJoin;
    }

    @Override
    public LinearLayout getNoclubHintLayout() {
        return layoutNoclubHint;
    }

    @Override
    public TextView getCreateTV() {
        return tvCreate;
    }

    @Override
    public TextView getJoinTV() {
        return tvJoin;
    }
}
