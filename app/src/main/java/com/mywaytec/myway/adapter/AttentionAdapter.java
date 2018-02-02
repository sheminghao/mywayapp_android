package com.mywaytec.myway.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.ListBaseAdapter;
import com.mywaytec.myway.base.SuperViewHolder;
import com.mywaytec.myway.model.bean.MyAttentionBean;

/**
 * Created by shemh on 2017/12/20.
 */

public class AttentionAdapter extends ListBaseAdapter<MyAttentionBean.ObjBean> {

    /**
     * 1,关注； 2，粉丝
     */
    private int type;

    public AttentionAdapter(Context context, int type) {
        super(context);
        this.type = type;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_attention;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        ImageView imgHeadPortrait = holder.getView(R.id.img_head_portrait);
        TextView tvNickname = holder.getView(R.id.tv_nickname);
        ImageView imgGender = holder.getView(R.id.img_gender);
        TextView tvAttention = holder.getView(R.id.tv_attention);

        tvNickname.setText(mDataList.get(position).getUser().getNickname());
        if (mDataList.get(position).getUser().isGender()) {
            imgGender.setImageResource(R.mipmap.cebianlan_fujinren_nan_icon);
            Glide.with(mContext).load(mDataList.get(position).getUser().getImgeUrl())
                    .error(R.mipmap.touxiang_boy_nor)
                    .centerCrop()
                    .into(imgHeadPortrait);
        }else {
            imgGender.setImageResource(R.mipmap.cebianlan_fujinren_nv_icon);
            Glide.with(mContext).load(mDataList.get(position).getUser().getImgeUrl())
                    .error(R.mipmap.touxiang_girl_nor)
                    .centerCrop()
                    .into(imgHeadPortrait);
        }

        if (mDataList.get(position).isIs_mutual()){//是否互相关注
            if (type == 2) {//粉丝列表
                tvAttention.setText(R.string.follow);
                tvAttention.setTextColor(Color.parseColor("#0774DD"));
                tvAttention.setBackgroundResource(R.drawable.kuang_follow);
            }else if (type == 1){//关注列表
                tvAttention.setText(R.string.followed);
                tvAttention.setTextColor(Color.parseColor("#999999"));
                tvAttention.setBackgroundResource(R.drawable.kuang_follow_each_other);
            }
        }else {
            tvAttention.setText(R.string.follow_each_other);
            tvAttention.setTextColor(Color.parseColor("#999999"));
            tvAttention.setBackgroundResource(R.drawable.kuang_follow_each_other);
        }
    }
}
