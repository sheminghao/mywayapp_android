package com.mywaytec.myway.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.ListBaseAdapter;
import com.mywaytec.myway.base.SuperViewHolder;
import com.mywaytec.myway.model.bean.ClubDetailBean;
import com.mywaytec.myway.ui.im.conversation.ConversationActivity;
import com.mywaytec.myway.ui.userDynamic.UserDynamicActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shemh on 2017/12/13.
 */

public class ClubMemberAdapter extends ListBaseAdapter<ClubDetailBean.ObjBean.UsersBean> {

    /**
     * 是否是编辑状态
     */
    private boolean isEdit;

    List<String> deleteUidsList;

    public ClubMemberAdapter(Context context) {
        super(context);
    }

    public String[] getDeleteUserUid() {
        deleteUidsList = new ArrayList<>();
        for (int i = 0; i < mDataList.size(); i++) {
            if (mDataList.get(i).isDeleteSelect()) {
                deleteUidsList.add(mDataList.get(i).getUid());
            }
        }
        
        String[] deleteUids = new String[deleteUidsList.size()];
        for (int i = 0; i < deleteUids.length; i++) {
            deleteUids[i] = deleteUidsList.get(i);
        }
        return deleteUids;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_club_member;
    }

    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
        if (!isEdit) {
            for (int i = 0; i < mDataList.size(); i++) {
                mDataList.get(i).setDeleteSelect(false);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        ImageView imgHeadPortrait = holder.getView(R.id.img_head_portrait);
        ImageView imgGender = holder.getView(R.id.img_gender);
        final TextView tvNickName = holder.getView(R.id.tv_nickname);

        if (mDataList.get(position).isGender()) {
            Glide.with(mContext).load(mDataList.get(position).getImgeUrl())
                    .error(R.mipmap.touxiang_boy_nor)
                    .centerCrop().into(imgHeadPortrait);
            imgGender.setImageResource(R.mipmap.cebianlan_fujinren_nan_icon);
        } else {
            Glide.with(mContext).load(mDataList.get(position).getImgeUrl())
                    .error(R.mipmap.touxiang_girl_nor)
                    .centerCrop().into(imgHeadPortrait);
            imgGender.setImageResource(R.mipmap.cebianlan_fujinren_nv_icon);
        }

        tvNickName.setText(mDataList.get(position).getNickname());

        CheckBox checkboxClubMember = holder.getView(R.id.checkbox_club_member);
        checkboxClubMember.setChecked(mDataList.get(position).isDeleteSelect());

        LinearLayout layoutClubMember = holder.getView(R.id.layout_club_member);
        layoutClubMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {//可编辑状态
                    if (mDataList.get(position).getGlobalState() != 2) {//当该用户不是群主时，可选择
                        if (mDataList.get(position).isDeleteSelect()) {
                            mDataList.get(position).setDeleteSelect(false);
                        } else {
                            mDataList.get(position).setDeleteSelect(true);
                        }
                        notifyItemChanged(position);
                        if (null != onSelectCountListener) {
                            int num = 0;
                            for (int i = 0; i < mDataList.size(); i++) {
                                if (mDataList.get(i).isDeleteSelect()) {
                                    num++;
                                }
                            }
                            onSelectCountListener.selectCount(num);
                        }
                    }
                } else {
                    Intent intent = new Intent(mContext, UserDynamicActivity.class);
                    intent.putExtra("uid", mDataList.get(position).getUid());
                    intent.putExtra("nickName", mDataList.get(position).getNickname());
                    mContext.startActivity(intent);
                }
            }
        });

        TextView tvGroupOwner = holder.getView(R.id.tv_group_owner);
        if (mDataList.get(position).getGlobalState() == 2) {//群主
            tvGroupOwner.setVisibility(View.VISIBLE);
            tvGroupOwner.setText(R.string.team_leader);
            checkboxClubMember.setVisibility(View.GONE);
        } else if (mDataList.get(position).getGlobalState() == 1) {//管理员
            if (isEdit) {
                tvGroupOwner.setVisibility(View.GONE);
                checkboxClubMember.setVisibility(View.VISIBLE);
            } else {
                tvGroupOwner.setVisibility(View.VISIBLE);
                tvGroupOwner.setText(R.string.administrator);
                checkboxClubMember.setVisibility(View.GONE);
            }
        } else if (mDataList.get(position).getGlobalState() == 0) {//成员
            if (isEdit) {
                checkboxClubMember.setVisibility(View.VISIBLE);
            } else {
                checkboxClubMember.setVisibility(View.GONE);
            }
        }
    }


    private OnSelectCountListener onSelectCountListener;

    public void setOnSelectCountListener(OnSelectCountListener onSelectCountListener) {
        this.onSelectCountListener = onSelectCountListener;
    }

    public interface OnSelectCountListener {
        void selectCount(int num);
    }
}
