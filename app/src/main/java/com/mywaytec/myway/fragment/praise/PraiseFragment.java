package com.mywaytec.myway.fragment.praise;

import android.os.Bundle;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseFragment;

import butterknife.BindView;

/**
 */
public class PraiseFragment extends BaseFragment<PraisePresenter> implements PraiseView {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";

    @BindView(R.id.lRecyclerView)
    LRecyclerView lRecyclerView;

    /**
     */
    public PraiseFragment() {
    }

    public static PraiseFragment newInstance(int columnCount) {
        PraiseFragment fragment = new PraiseFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_comment;
    }

    @Override
    protected void initInjector() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected void initViews() {
        int id = getArguments().getInt(ARG_COLUMN_COUNT);
        mPresenter.getData(id);
    }

    @Override
    protected void updateViews() {

    }

    @Override
    public LRecyclerView getRecyclerView() {
        return lRecyclerView;
    }
}
