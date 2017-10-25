package com.mywaytec.myway.adapter;

import android.content.Context;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.ListBaseAdapter;
import com.mywaytec.myway.base.SuperViewHolder;
import com.mywaytec.myway.model.bean.RouteListBean;
import com.mywaytec.myway.utils.TimeUtil;
import com.mywaytec.myway.view.CircleImageView;
import com.mywaytec.myway.view.RoundImageView;

import java.text.DecimalFormat;

/**
 * Created by shemh on 2017/2/23.
 */

public class WayAdapter extends ListBaseAdapter<RouteListBean.ObjBean>{

    private Context context;
    private DecimalFormat df = new DecimalFormat("######0.00");

    public WayAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_way;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        RoundImageView img = holder.getView(R.id.img_way);
        Glide.with(context).load(mDataList.get(position).getImage())
                .error(R.mipmap.icon_default)
                .placeholder(R.mipmap.icon_default)
                .into(img);
        TextView tvName = holder.getView(R.id.tv_name);
        tvName.setText(mDataList.get(position).getName());
        TextView tvPublishTime = holder.getView(R.id.tv_publish_time);
        tvPublishTime.setText(TimeUtil.YMDHMStoYMDTime(mDataList.get(position).getCreateTime())+"");
        TextView tvSceneryStar = holder.getView(R.id.tv_scenery_star);
        tvSceneryStar.setText(mDataList.get(position).getSceneryStar()+"");
        RatingBar ratingBar = holder.getView(R.id.ratingBar);
//        ratingBar.setProgress(mDataList.get(position).getSceneryStar());
        ratingBar.setRating(mDataList.get(position).getSceneryStar());
//        TextView tvLocation = holder.getView(R.id.tv_location);
//        tvLocation.setText(mDataList.get(position).getOrigin());
        TextView tvLikeNum = holder.getView(R.id.tv_like_num);
        tvLikeNum.setText(mDataList.get(position).getLikeNum()+"");
        TextView tvZonglicheng = holder.getView(R.id.tv_zonglicheng);
        if (mDataList.get(position).getLegend() > 0) {
            tvZonglicheng.setText(df.format(mDataList.get(position).getLegend() / 1000.0));
        }else {
            tvZonglicheng.setText(0+"");
        }
        CircleImageView imgHeadPortrait = holder.getView(R.id.img_head_portrait);
        if (mDataList.get(position).getUser().isGender()) {
            Glide.with(context).load(mDataList.get(position).getUser().getImgeUrl())
                    .error(R.mipmap.touxiang_boy_nor)
                    .into(imgHeadPortrait);
        }else {
            Glide.with(context).load(mDataList.get(position).getUser().getImgeUrl())
                    .error(R.mipmap.touxiang_girl_nor)
                    .into(imgHeadPortrait);
        }
        TextView tvUserName = holder.getView(R.id.tv_user_name);
        tvUserName.setText(mDataList.get(position).getUser().getNickname());

    }
}
