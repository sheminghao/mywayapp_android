package com.mywaytec.myway.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.ListBaseAdapter;
import com.mywaytec.myway.base.SuperViewHolder;
import com.mywaytec.myway.model.bean.NearbyBean;
import com.mywaytec.myway.ui.userDynamic.UserDynamicActivity;
import com.mywaytec.myway.utils.TimeUtil;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by shemh on 2017/8/2.
 */

public class PeopleNearbyAdapter extends ListBaseAdapter<NearbyBean.ObjBean> {

    private Context mContext;
    private DecimalFormat df = new DecimalFormat("######0.00");

    public PeopleNearbyAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_people_nearby;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        TextView tvName = holder.getView(R.id.tv_name);
        TextView tvDistance = holder.getView(R.id.tv_distance);
        TextView tvTime = holder.getView(R.id.tv_time);
        TextView tvAge = holder.getView(R.id.tv_age);
        tvName.setText(mDataList.get(position).getUser().getNickname());
        tvDistance.setText(df.format(mDataList.get(position).getDistance()/1000.0)+" km");
        tvTime.setText(TimeUtil.getTimeDelay(mDataList.get(position).getCreatTime()));
        tvAge.setText(getAge(mDataList.get(position).getUser().getBirthday())+"");

        ImageView imgHeadPortrait = holder.getView(R.id.img_head_portrait);
        RelativeLayout layoutGender = holder.getView(R.id.layout_gender);
        ImageView imgGender = holder.getView(R.id.img_gender);
        if (mDataList.get(position).getUser().isGender()) {
            layoutGender.setBackgroundResource(R.mipmap.cebianlan_fujinren_nan_beijing_icon);
            imgGender.setImageResource(R.mipmap.cebianlan_fujinren_nan_icon);
            Glide.with(mContext).load(mDataList.get(position).getUser().getImgeUrl())
                    .error(R.mipmap.touxiang_boy_nor)
                    .centerCrop()
                    .into(imgHeadPortrait);
        }else {
            layoutGender.setBackgroundResource(R.mipmap.cebianlan_fujinren_nv_beijing_icon);
            imgGender.setImageResource(R.mipmap.cebianlan_fujinren_nv_icon);
            Glide.with(mContext).load(mDataList.get(position).getUser().getImgeUrl())
                    .error(R.mipmap.touxiang_girl_nor)
                    .centerCrop()
                    .into(imgHeadPortrait);
        }

        LinearLayout layoutPeopleNearby = holder.getView(R.id.layout_people_nearby);
        layoutPeopleNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserDynamicActivity.class);
                intent.putExtra("otherUid", mDataList.get(position).getUid());
                mContext.startActivity(intent);
            }
        });
    }

    private int getAge(String birth){
        if (null != birth){
            String[] births = birth.split("-");
            if (null != births && births.length > 0) {
                int birthYear = Integer.parseInt(births[0]);
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
//                Log.i("TAG", "------birthYear,"+birthYear+",,currentYear"+currentYear);
                return currentYear - birthYear;
            }
        }
        return 0;
    }
}
