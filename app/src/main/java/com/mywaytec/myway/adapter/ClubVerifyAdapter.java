package com.mywaytec.myway.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.ListBaseAdapter;
import com.mywaytec.myway.base.SuperViewHolder;
import com.mywaytec.myway.model.bean.ClubJoinListBean;

/**
 * Created by shemh on 2017/12/5.
 */

public class ClubVerifyAdapter extends ListBaseAdapter<ClubJoinListBean.ObjBean>{

    public ClubVerifyAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_club_verify;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        ImageView imgHeadPortrait = holder.getView(R.id.img_head_portrait);
        TextView tvNickname = holder.getView(R.id.tv_nickname);
        TextView tvMessage = holder.getView(R.id.tv_message);
        TextView tvAgreement = holder.getView(R.id.tv_agreement);
        TextView tvJujue = holder.getView(R.id.tv_jujue);
        TextView tvApplyState = holder.getView(R.id.tv_apply_state);
        LinearLayout layoutApplyState = holder.getView(R.id.layout_apply_state);

        if (mDataList.get(position).getUser().isGender()){
            Glide.with(mContext).load(mDataList.get(position).getUser().getImgeUrl())
                    .error(R.mipmap.touxiang_boy_nor)
                    .centerCrop().into(imgHeadPortrait);
        }else {
            Glide.with(mContext).load(mDataList.get(position).getUser().getImgeUrl())
                    .error(R.mipmap.touxiang_girl_nor)
                    .centerCrop().into(imgHeadPortrait);
        }
        tvNickname.setText(mDataList.get(position).getUser().getNickname());
        tvMessage.setText(mContext.getResources().getString(R.string.apply_for_participation)
                + mDataList.get(position).getClub().getGroupname());
        if (mDataList.get(position).getStatus() == 0){//申请中
            tvApplyState.setVisibility(View.GONE);
            layoutApplyState.setVisibility(View.VISIBLE);
        }else if (mDataList.get(position).getStatus() == 1){//已同意
            tvApplyState.setVisibility(View.VISIBLE);
            layoutApplyState.setVisibility(View.GONE);
            tvApplyState.setText(R.string.agreed);
        }else if (mDataList.get(position).getStatus() == 2){//已拒绝
            tvApplyState.setVisibility(View.VISIBLE);
            layoutApplyState.setVisibility(View.GONE);
            tvApplyState.setText(R.string.rejected);
        }

        tvAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onApplyListener) {
                    onApplyListener.agreement(position);
                }
            }
        });

        tvJujue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onApplyListener){
                    onApplyListener.jujue(position);
                }
            }
        });
    }

    private OnApplyListener onApplyListener;

    public void setOnApplyListener(OnApplyListener onApplyListener){
        this.onApplyListener = onApplyListener;
    }

    public interface OnApplyListener{
        void agreement(int pos);
        void jujue(int pos);
    }
}
