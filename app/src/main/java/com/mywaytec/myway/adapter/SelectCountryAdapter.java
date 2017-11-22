package com.mywaytec.myway.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.ListBaseAdapter;
import com.mywaytec.myway.base.SuperViewHolder;
import com.mywaytec.myway.model.bean.CountryBean;
import com.mywaytec.myway.ui.selectCountry.SelectCountryActivity;

/**
 */

public class SelectCountryAdapter extends ListBaseAdapter<CountryBean> {

    protected Context mContext;

    public SelectCountryAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_select_country;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        TextView tvCountry = holder.getView(R.id.tv_country);
        tvCountry.setText(mDataList.get(position).getCountry());

        tvCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("code", mDataList.get(position).getCode());
                ((SelectCountryActivity)mContext).setResult(Activity.RESULT_OK, intent);
                ((SelectCountryActivity)mContext).finish();
            }
        });
    }
}
