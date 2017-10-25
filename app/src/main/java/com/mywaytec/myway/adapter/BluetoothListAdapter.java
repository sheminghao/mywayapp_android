package com.mywaytec.myway.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.ListBaseAdapter;
import com.mywaytec.myway.base.SuperViewHolder;
import com.mywaytec.myway.model.BluetoothInfoModel;
import com.mywaytec.myway.utils.BleUtil;

/**
 * Created by shemh on 2017/2/16.
 */

public class BluetoothListAdapter extends ListBaseAdapter<BluetoothInfoModel> {

    public BluetoothListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_bluetooth;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        TextView tvName = holder.getView(R.id.tv_name);
        TextView tvAddress = holder.getView(R.id.tv_address);
        ImageView imgRssi = holder.getView(R.id.img_rssi);
        String deviceName = mDataList.get(position).getDevice().getName();
        tvName.setText(deviceName);
        if (!TextUtils.isEmpty(mDataList.get(position).getDevice().getAddress())) {
            tvAddress.setText(mDataList.get(position).getDevice().getAddress());
        }
        if (null!=mDataList.get(position)) {
            int rssi = mDataList.get(position).getRssi();
            if (rssi >= -100 || rssi < -80){
                imgRssi.setImageResource(R.mipmap.icon_xinghao5);
            }else if(rssi >= -80 || rssi < -60){
                imgRssi.setImageResource(R.mipmap.icon_xinghao4);
            }else if(rssi >= -60 || rssi < -40){
                imgRssi.setImageResource(R.mipmap.icon_xinghao3);
            }else if(rssi >= -40 || rssi < -20){
                imgRssi.setImageResource(R.mipmap.icon_xinghao2);
            }else if(rssi >= -20 || rssi < 0){
                imgRssi.setImageResource(R.mipmap.icon_xinghao1);
            }
        }
    }
}
