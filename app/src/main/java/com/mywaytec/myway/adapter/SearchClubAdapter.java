package com.mywaytec.myway.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.ListBaseAdapter;
import com.mywaytec.myway.base.SuperViewHolder;
import com.mywaytec.myway.model.bean.SearchClubBean;
import com.mywaytec.myway.ui.im.JoinClub.JoinClubActivity;
import com.mywaytec.myway.view.CircleImageView;

/**
 * Created by shemh on 2017/12/4.
 */

public class SearchClubAdapter extends ListBaseAdapter<SearchClubBean.ObjBean>{

    public SearchClubAdapter(Context context) {
        super(context);
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
        if (mDataList.get(position).isOfficial()){
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
                Intent intent = new Intent(mContext, JoinClubActivity.class);
                intent.putExtra("clubDetail", mDataList.get(position));
                mContext.startActivity(intent);
            }
        });
    }
}
