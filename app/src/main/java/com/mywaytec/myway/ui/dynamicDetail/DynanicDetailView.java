package com.mywaytec.myway.ui.dynamicDetail;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mywaytec.myway.base.IBaseView;
import com.mywaytec.myway.fragment.comment.CommentFragment;

/**
 * Created by shemh on 2017/3/8.
 */

public interface DynanicDetailView extends IBaseView {

    CommentFragment getCommentFragment();

    TextView getCommentTV();

    ImageView getLikeImg();

    TextView getLikeTV();

    LinearLayout getPraiseLayout();

    Context getContext();

}
