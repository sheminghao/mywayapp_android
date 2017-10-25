package com.mywaytec.myway.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.ListBaseAdapter;
import com.mywaytec.myway.base.SuperViewHolder;

/**
 * Created by shemh on 2017/8/9.
 */

public class LevelAdapter extends ListBaseAdapter<String> {

    public LevelAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_level;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        ImageView imgLevel = holder.getView(R.id.img_level);
        TextView tvLevel = holder.getView(R.id.tv_level);
        TextView tvJifen = holder.getView(R.id.tv_jifen);
        TextView tvTotal = holder.getView(R.id.tv_total);

        if (null != mDataList.get(position)){
            String[] access = mDataList.get(position).split("--");
            if (access.length >= 4){
                loadLevelImg(imgLevel, Integer.parseInt(access[0]));
                tvLevel.setText(access[1]);
                tvJifen.setText(access[2]);
                tvTotal.setText(access[3]);
            }
        }
    }

    private void loadLevelImg(ImageView imgLevel, int level){
        int imgId = 0;
        switch (level){
            case 0:
                imgId = R.mipmap.chujicheshou_00;
                break;
            case 1:
                imgId = R.mipmap.putongcheshou_01;
                break;
            case 2:
                imgId = R.mipmap.zishencheshou_02;
                break;
            case 3:
                imgId = R.mipmap.saicheshou_e_03;
                break;
            case 4:
                imgId = R.mipmap.saicheshou_d_04;
                break;
            case 5:
                imgId = R.mipmap.saicheshou_c_05;
                break;
            case 6:
                imgId = R.mipmap.saicheshou_b_06;
                break;
            case 7:
                imgId = R.mipmap.saicheshou_a_07;
                break;
            case 8:
                imgId = R.mipmap.saicheshou_f1_08;
                break;
            case 9:
                imgId = R.mipmap.saichezhiwang_09;
                break;
            case 10:
                imgId = R.mipmap.feichezhiwang_10;
                break;
            case 11:
                imgId = R.mipmap.chaosuzhiwang_11;
                break;
            case 12:
                imgId = R.mipmap.jisuzhiwang_12;
                break;
            case 13:
                imgId = R.mipmap.guangsuzhiwang_13;
                break;
            case 14:
                imgId = R.mipmap.chewangzhiwang_14;
                break;
            case 15:
                imgId = R.mipmap.xinjincheshen_15;
                break;
            case 16:
                imgId = R.mipmap.xinjincheshen_16;
                break;
            case 17:
                imgId = R.mipmap.fengsucheshen_17;
                break;
            case 18:
                imgId = R.mipmap.shandiancheshen_18;
                break;
            case 19:
                imgId = R.mipmap.leitingcheshen_19;
                break;
            case 20:
                imgId = R.mipmap.manweicheshen_20;
                break;
        }
        Glide.with(mContext).load(imgId)
                .into(imgLevel);
    }
}
