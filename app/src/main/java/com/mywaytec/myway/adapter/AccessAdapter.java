package com.mywaytec.myway.adapter;

import android.content.Context;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.ListBaseAdapter;
import com.mywaytec.myway.base.SuperViewHolder;

/**
 * Created by shemh on 2017/8/9.
 */

public class AccessAdapter extends ListBaseAdapter<String> {

    public AccessAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_access;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        TextView tvAccess = holder.getView(R.id.tv_access);
        TextView tvJifen = holder.getView(R.id.tv_jifen);
        TextView tvJinbi = holder.getView(R.id.tv_jinbi);

        if (null != mDataList.get(position)){
            String[] access = mDataList.get(position).split("--");
            if (access.length >= 3){
                tvAccess.setText(access[0]);
                tvJifen.setText("+"+access[1]);
                tvJinbi.setText("+"+access[2]);
            }
        }
    }
}
