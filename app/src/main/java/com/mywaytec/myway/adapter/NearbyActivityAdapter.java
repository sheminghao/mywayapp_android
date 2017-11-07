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
import com.mywaytec.myway.model.bean.NearbyActivityBean;
import com.mywaytec.myway.ui.huodongXiangqing.HuodongXiangqingActivity;
import com.mywaytec.myway.ui.huodongyueban.HuodongYuebanActivity;

import static com.mywaytec.myway.ui.huodongXiangqing.HuodongXiangqingActivity.ACTIVITY_INFO;

/**
 * Created by shemh on 2017/8/10.
 */

public class NearbyActivityAdapter extends ListBaseAdapter<NearbyActivityBean.ObjBean> {

    public NearbyActivityAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_nearby_activity;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        LinearLayout layoutNearby = holder.getView(R.id.layout_nearby);
        TextView tvTitle = holder.getView(R.id.tv_title);
        ImageView imgActivity = holder.getView(R.id.img_activity);
        TextView tvLocation = holder.getView(R.id.tv_location);
        TextView tvTime = holder.getView(R.id.tv_time);
        TextView tvPeopleNum = holder.getView(R.id.tv_people_num);
        TextView tvJifen = holder.getView(R.id.tv_jifen);

        tvTitle.setText(mDataList.get(position).getTitle());
        NearbyActivityBean.ObjBean.LocationBean locationBean = mDataList.get(position).getLocation();
        if (null != locationBean)
        tvLocation.setText(locationBean.getProvince()+locationBean.getCity()+locationBean.getStreet());
        tvTime.setText(mDataList.get(position).getCreateTime());
        tvPeopleNum.setText(mDataList.get(position).getCurrentNum()+"/"+mDataList.get(position).getNum());
        if (null != mDataList.get(position).getPhotos() &&
                mDataList.get(position).getPhotos().size() > 0){
            Glide.with(mContext).load(mDataList.get(position).getPhotos().get(0))
                    .error(R.mipmap.icon_default)
                    .centerCrop()
                    .into(imgActivity);
        }

        layoutNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HuodongXiangqingActivity.class);
                intent.putExtra(ACTIVITY_INFO, mDataList.get(position));
                intent.putExtra("position", position);
                ((HuodongYuebanActivity)mContext).startActivityForResult(intent, 0x141);
            }
        });
    }
}
