package com.mywaytec.myway.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.ListBaseAdapter;
import com.mywaytec.myway.base.SuperViewHolder;
import com.mywaytec.myway.model.bean.AllPeopleBean;
import com.mywaytec.myway.ui.userDynamic.UserDynamicActivity;

/**
 * Created by shemh on 2017/8/15.
 */

public class HuodongChengyuanAdapter extends ListBaseAdapter<AllPeopleBean.ObjBean.ParticipantBean> {

    private Context mContext;

    public HuodongChengyuanAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_huodongchengyuan;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        TextView tvName = holder.getView(R.id.tv_name);
        tvName.setText(mDataList.get(position).getNickname());

        ImageView imgHeadPortrait = holder.getView(R.id.img_head_portrait);
        ImageView imgGender = holder.getView(R.id.img_gender);
        if (mDataList.get(position).isGender()) {
            Glide.with(mContext).load(R.mipmap.cebianlan_fujinren_nan_icon)
                    .into(imgGender);
            Glide.with(mContext).load(mDataList.get(position).getImgeUrl())
                    .error(R.mipmap.touxiang_boy_nor)
                    .into(imgHeadPortrait);
        }else {
            Glide.with(mContext).load(R.mipmap.cebianlan_fujinren_nv_icon)
                    .into(imgGender);
            Glide.with(mContext).load(mDataList.get(position).getImgeUrl())
                    .error(R.mipmap.touxiang_girl_nor)
                    .into(imgHeadPortrait);
        }

        LinearLayout layoutChengyuan = holder.getView(R.id.layout_chengyuan);
        layoutChengyuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserDynamicActivity.class);
                intent.putExtra("otherUid", mDataList.get(position).getUid());
                mContext.startActivity(intent);
            }
        });
    }
}
