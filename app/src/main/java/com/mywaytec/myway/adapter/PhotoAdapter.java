package com.mywaytec.myway.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.mywaytec.myway.APP;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.ListBaseAdapter;
import com.mywaytec.myway.base.SuperViewHolder;

/**
 * Created by shemh on 2017/5/5.
 */

public class PhotoAdapter extends ListBaseAdapter<String> {
    public PhotoAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_photo;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        ImageView img = holder.getView(R.id.img_photo);
        APP.loadImg(mDataList.get(position), img);
    }
}
