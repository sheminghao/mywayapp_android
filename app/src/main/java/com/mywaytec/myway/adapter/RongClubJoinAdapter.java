package com.mywaytec.myway.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.ListBaseAdapter;
import com.mywaytec.myway.base.SuperViewHolder;
import com.mywaytec.myway.model.bean.RongClubListBean;
import com.mywaytec.myway.view.CircleImageView;

import io.rong.imkit.RongIM;

/**
 * Created by shemh on 2017/11/29.
 */

public class RongClubJoinAdapter extends ListBaseAdapter<RongClubListBean.ObjBean.JoinBean> {

    private Context mContext;

    public RongClubJoinAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_club;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        CircleImageView imgClubHead = holder.getView(R.id.img_club_head);
        TextView tvClubName = holder.getView(R.id.tv_club_name);
        TextView tvClubAddress = holder.getView(R.id.tv_club_address);
        TextView tvClubNum = holder.getView(R.id.tv_club_num);
        TextView tvClubOfficial = holder.getView(R.id.tv_club_official);
        if (mDataList.get(position).isIsOfficial()){
            tvClubOfficial.setVisibility(View.VISIBLE);
        }else {
            tvClubOfficial.setVisibility(View.GONE);
        }

        Glide.with(mContext).load(mDataList.get(position).getImgUrl())
                .centerCrop()
                .into(imgClubHead);
        tvClubName.setText(mDataList.get(position).getGroupname());
        tvClubAddress.setText(mDataList.get(position).getCountry() + mDataList.get(position).getProvince()
                                + mDataList.get(position).getCity());
        tvClubNum.setText(mDataList.get(position).getDescription());

        LinearLayout layoutClub = holder.getView(R.id.layout_club);
        layoutClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RongIM.getInstance().startGroupChat(mContext, mDataList.get(position).getRong_gid(),
                        mDataList.get(position).getGroupname());
            }
        });

    }
}
