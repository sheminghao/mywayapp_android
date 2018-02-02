package com.mywaytec.myway.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.ListBaseAdapter;
import com.mywaytec.myway.base.SuperViewHolder;
import com.mywaytec.myway.model.bean.AllBindingCarBean;
import com.mywaytec.myway.ui.gprs.GPRSActivity;

import static com.mywaytec.myway.ui.gprs.GPRSActivity.SNCODE;
import static com.mywaytec.myway.ui.gprs.GPRSActivity.VEHICLE_NAME;

/**
 * Created by shemh on 2017/7/20.
 */

public class BindingCarAdapter extends ListBaseAdapter<AllBindingCarBean.ObjBean>{

    private Context mContext;

    public BindingCarAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_binding_car;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        Log.i("TAG", "------sncode, " + mDataList.get(position).getSnCode());
        TextView tvCarName = holder.getView(R.id.tv_car_name);
        tvCarName.setText(mDataList.get(position).getName());

        TextView tvYuancheng = holder.getView(R.id.tv_yuancheng);
        tvYuancheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mDataList.get(position).getSnCode())) {
                    Intent intent = new Intent(mContext, GPRSActivity.class);
                    intent.putExtra(SNCODE, mDataList.get(position).getSnCode());
                    intent.putExtra(VEHICLE_NAME, mDataList.get(position).getName());
                    mContext.startActivity(intent);
                }
            }
        });
    }
}
