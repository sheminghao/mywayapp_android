package com.mywaytec.myway.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.ListBaseAdapter;
import com.mywaytec.myway.base.SuperViewHolder;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.bean.DianziweilanBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.ui.gprs.editFence.EditFenceActivity;
import com.mywaytec.myway.ui.gprs.electronicFence.ElectronicFenceActivity;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CommonSubscriber;

import static com.mywaytec.myway.ui.gprs.editFence.EditFenceActivity.DIANZIWEILAN;
import static com.mywaytec.myway.ui.gprs.electronicFence.ElectronicFenceActivity.DIANZIWEILAN_REQUESTCODE;

/**
 * Created by shemh on 2017/8/9.
 */

public class DianziweilanAdapter extends ListBaseAdapter<DianziweilanBean.ObjBean> {

    private int mSelectedPos;
    RetrofitHelper retrofitHelper;

    public DianziweilanAdapter(Context context, RetrofitHelper retrofitHelper) {
        super(context);
        this.retrofitHelper = retrofitHelper;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_dianziweilan;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        final CheckBox checkBox = holder.getView(R.id.checkbox);
        checkBox.setText(mDataList.get(position).getName());

        checkBox.setChecked(mDataList.get(position).isStatus());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (mSelectedPos!=position){
                        //先取消上个item的勾选状态
                        mDataList.get(mSelectedPos).setStatus(false);
                        notifyItemChanged(mSelectedPos);
                        //设置新Item的勾选状态
                        mSelectedPos = position;
                        mDataList.get(mSelectedPos).setStatus(true);
                        notifyItemChanged(mSelectedPos);
                    }
                }
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    retrofitHelper.openVehicleFence(mDataList.get(position).getId())
                            .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                            .subscribe(new CommonSubscriber<BaseInfo>() {
                                @Override
                                public void onNext(BaseInfo baseInfo) {
                                    if (baseInfo.getCode() == 1){
//                                        ToastUtils.showToast(baseInfo.getMsg());
                                    }else {
                                        ToastUtils.showToast(baseInfo.getMsg());
                                    }
                                }
                            });
                }else {
                    retrofitHelper.closeVehicleFence(mDataList.get(position).getId())
                            .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                            .subscribe(new CommonSubscriber<BaseInfo>() {
                                @Override
                                public void onNext(BaseInfo baseInfo) {
                                    if (baseInfo.getCode() == 1){
//                                        ToastUtils.showToast(baseInfo.getMsg());
                                    }else {
                                        ToastUtils.showToast(baseInfo.getMsg());
                                    }
                                }
                            });
                }
            }
        });

        LinearLayout layoutMore = holder.getView(R.id.layout_more);
        layoutMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditFenceActivity.class);
                intent.putExtra(DIANZIWEILAN, mDataList.get(position));
                intent.putExtra("position", position);
                ((ElectronicFenceActivity) mContext).startActivityForResult(intent, DIANZIWEILAN_REQUESTCODE);
            }
        });
    }
}
