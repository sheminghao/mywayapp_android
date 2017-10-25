package com.mywaytec.myway.adapter;

import android.content.Context;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.ListBaseAdapter;
import com.mywaytec.myway.base.SuperViewHolder;

/**
 * Created by shemh on 2017/3/23.
 */

public class LocationAdapter extends ListBaseAdapter<PoiInfo> {

    public LocationAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_location;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        TextView tvName = holder.getView(R.id.tv_name);
        TextView tvAddress = holder.getView(R.id.tv_address);
        PoiInfo poi = mDataList.get(position);
        tvName.setText(poi.name);
        tvAddress.setText(poi.address);
    }
}
