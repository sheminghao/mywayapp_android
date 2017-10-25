package com.mywaytec.myway.adapter;

import android.content.Context;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.ListBaseAdapter;
import com.mywaytec.myway.base.SuperViewHolder;
import com.mywaytec.myway.model.bean.LikeListBean;
import com.mywaytec.myway.view.CircleImageView;

/**
*/
public class PraiseAdapter extends ListBaseAdapter<LikeListBean.ObjBean> {

    public PraiseAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_praise;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        CircleImageView imgHeadPortrait = holder.getView(R.id.img_head_portrait);
        if (mDataList.get(position).getUser().isGender()){
            Glide.with(mContext).load(mDataList.get(position).getUser().getImgeUrl())
                    .error(R.mipmap.touxiang_boy_nor)
                    .into(imgHeadPortrait);
        }else {
            Glide.with(mContext).load(mDataList.get(position).getUser().getImgeUrl())
                    .error(R.mipmap.touxiang_girl_nor)
                    .into(imgHeadPortrait);
        }
        TextView tvNickname = holder.getView(R.id.tv_nickname);
        tvNickname.setText(mDataList.get(position).getUser().getNickname());
        TextView tvPublishTime = holder.getView(R.id.tv_publish_time);
        tvPublishTime.setText(mDataList.get(position).getUser().getLastlogindate());
    }
}
