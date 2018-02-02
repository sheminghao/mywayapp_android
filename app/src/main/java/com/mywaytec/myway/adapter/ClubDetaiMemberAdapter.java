package com.mywaytec.myway.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.ListBaseAdapter;
import com.mywaytec.myway.base.SuperViewHolder;
import com.mywaytec.myway.model.bean.ClubDetailBean;

/**
 * Created by shemh on 2017/12/2.
 */

public class ClubDetaiMemberAdapter extends ListBaseAdapter<ClubDetailBean.ObjBean.UsersBean>{

    public ClubDetaiMemberAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemCount() {
        if (super.getItemCount() > 5) {
            return 5;
        }else {
            return super.getItemCount();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_club_detail_member;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        ImageView imgHeadPortrait = holder.getView(R.id.img_head_portrait);
        TextView tvNickname = holder.getView(R.id.tv_nickname);
        if (mDataList.get(position).isGender()){
            Glide.with(mContext).load(mDataList.get(position).getImgeUrl())
                    .error(R.mipmap.touxiang_boy_nor)
                    .centerCrop().into(imgHeadPortrait);
        }else {
            Glide.with(mContext).load(mDataList.get(position).getImgeUrl())
                    .error(R.mipmap.touxiang_girl_nor)
                    .centerCrop().into(imgHeadPortrait);
        }
        tvNickname.setText(mDataList.get(position).getNickname());

    }
}
