package com.mywaytec.myway.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mywaytec.myway.APP;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.ListBaseAdapter;
import com.mywaytec.myway.base.SuperViewHolder;
import com.mywaytec.myway.model.bean.DayRankingBean;

/**
 * 评论列表适配器
*/
public class RankingListAdapter extends ListBaseAdapter<DayRankingBean.ObjBean.RankingListBean> {

    public RankingListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_ranking_list;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        ImageView imgRanking = holder.getView(R.id.img_ranking);
        TextView tvRanking = holder.getView(R.id.tv_ranking);
        if (position == 0){
            imgRanking.setVisibility(View.VISIBLE);
            tvRanking.setVisibility(View.GONE);
            imgRanking.setImageResource(R.mipmap.cebianlan_paihangbang_jinpai);
        }else if (position == 1){
            imgRanking.setVisibility(View.VISIBLE);
            tvRanking.setVisibility(View.GONE);
            imgRanking.setImageResource(R.mipmap.cebianlan_paihangbang_yinpai);
        }else if (position == 2){
            imgRanking.setVisibility(View.VISIBLE);
            tvRanking.setVisibility(View.GONE);
            imgRanking.setImageResource(R.mipmap.cebianlan_paihangbang_tongpai);
        }else {
            imgRanking.setVisibility(View.GONE);
            tvRanking.setVisibility(View.VISIBLE);
            tvRanking.setText(position+1+"");
        }

        ImageView imgHeadPortrait = holder.getView(R.id.img_head_portrait);
        TextView tvNickname = holder.getView(R.id.tv_nickname);
        TextView tvcarType = holder.getView(R.id.tv_car_type);
        TextView tvSumMileage = holder.getView(R.id.tv_sum_mileage);
        Glide.with(mContext).load(mDataList.get(position).getImgSrc())
                .error(R.mipmap.icon_default)
                .centerCrop()
                .into(imgHeadPortrait);
        tvNickname.setText(mDataList.get(position).getUname()+"");
        tvcarType.setText(mDataList.get(position).getUType()+"");
        tvSumMileage.setText(mDataList.get(position).getSumMileage()+"");

    }
}
