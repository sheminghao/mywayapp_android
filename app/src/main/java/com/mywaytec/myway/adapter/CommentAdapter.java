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
import com.mywaytec.myway.model.bean.CommentListBean;
import com.mywaytec.myway.utils.Base64_2;
import com.mywaytec.myway.view.CircleImageView;

/**
 * 评论列表适配器
*/
public class CommentAdapter extends ListBaseAdapter<CommentListBean.ObjBean> {

    public CommentAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_comment;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        CircleImageView imgHeadPortrait = holder.getView(R.id.img_head_portrait);
        TextView tvNickname = holder.getView(R.id.tv_nickname);
        TextView tvPublishTime = holder.getView(R.id.tv_publish_time);
        TextView tvContent = holder.getView(R.id.tv_content);

        if (mDataList.get(position).getFromUser().getGender()){
            Glide.with(mContext).load(mDataList.get(position).getFromUser().getImgeUrl())
                    .error(R.mipmap.touxiang_boy_nor)
                    .into(imgHeadPortrait);
        }else {
            Glide.with(mContext).load(mDataList.get(position).getFromUser().getImgeUrl())
                    .error(R.mipmap.touxiang_girl_nor)
                    .into(imgHeadPortrait);
        }
        tvNickname.setText(mDataList.get(position).getFromUser().getNickname());
        tvPublishTime.setText(mDataList.get(position).getComCreateTime());
        try {
            tvContent.setText(new String(Base64_2.decode(mDataList.get(position).getComContent())));
        }catch (Exception e){
            tvContent.setText(mDataList.get(position).getComContent());
        }
    }
}
