package com.mywaytec.myway.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.ListBaseAdapter;
import com.mywaytec.myway.base.SuperViewHolder;
import com.mywaytec.myway.utils.AppUtils;
import com.mywaytec.myway.utils.ImageUtils;

/**
 * Created by shemh on 2017/7/19.
 */

public class WayDetailImgAdapter extends ListBaseAdapter<String> {

    Context context;

    public WayDetailImgAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_way_detail_img;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        Log.i("TAG", "------WayDetailImgAdapter,"+mDataList.get(position));
        LinearLayout layoutWayDetail = holder.getView(R.id.layout_way_detail);
        ImageView img = holder.getView(R.id.img_way_detail);
//        Glide.with(context).load(mDataList.get(position))
//                .placeholder(R.mipmap.icon_default)
//                .error(R.mipmap.icon_default)
//                .into(img);
        ImageUtils.getPicByGlideAndScale(context, mDataList.get(position), img, AppUtils.getScreenWidth(context),
                    20, layoutWayDetail, 0);
    }
}
