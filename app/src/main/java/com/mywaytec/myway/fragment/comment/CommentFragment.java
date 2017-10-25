package com.mywaytec.myway.fragment.comment;

import android.os.Bundle;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseFragment;

import butterknife.BindView;

/**
 */
public class CommentFragment extends BaseFragment<CommentPresenter> implements CommentView {

    private static final String ARG_COLUMN_COUNT = "column-count";

    @BindView(R.id.lRecyclerView)
    LRecyclerView lRecyclerView;

    /**
     */
    public CommentFragment() {
    }

    public static CommentFragment newInstance(int columnCount) {
        CommentFragment fragment = new CommentFragment();
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
    public void initViews() {
        int id = getArguments().getInt(ARG_COLUMN_COUNT);
        mPresenter.init(id);
    }

    @Override
    protected void updateViews() {

    }

    @Override
    public LRecyclerView getRecyclerView() {
        return lRecyclerView;
    }
}
