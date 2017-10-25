package com.mywaytec.myway.ui.messageDetail;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.mywaytec.myway.base.IBaseView;
import com.mywaytec.myway.fragment.comment.CommentFragment;
import com.mywaytec.myway.model.bean.DynamicListBean;
import com.mywaytec.myway.view.FamilyBookView;

/**
 * Created by shemh on 2017/3/8.
 */

public interface MessageDetailView extends IBaseView {

    CommentFragment getCommentFragment();

    TextView getCommentTV();

    ImageView getLikeImg();

    TextView getLikeTV();

    Context getContext();

    ImageView getHeadPortraitImg();

    TextView getNicknameTV();

    TextView getContentTV();

    TextView getPublishTimeTV();

    TextView getPraiseTV();

    FamilyBookView getFamilybookview();
}
