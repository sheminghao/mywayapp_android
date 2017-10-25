package com.mywaytec.myway.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.ListBaseAdapter;
import com.mywaytec.myway.base.SuperViewHolder;
import com.mywaytec.myway.model.bean.FenceWarningBean;
import com.mywaytec.myway.utils.TimeUtil;
import com.mywaytec.myway.view.SwipeMenuView;

/**
 * Created by shemh on 2017/8/9.
 */

public class TongzhiAdapter extends ListBaseAdapter<FenceWarningBean.ObjBean.FenceWarningsBean> {

    String vehicleName;

    public TongzhiAdapter(Context context, String vehicleName) {
        super(context);
        this.vehicleName = vehicleName;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_tongzhi;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        TextView tvCheming = holder.getView(R.id.tv_cheming);
        TextView tvWeilan = holder.getView(R.id.tv_weilan);
        TextView tvShijian = holder.getView(R.id.tv_shijian);
        tvCheming.setText(vehicleName);
        tvWeilan.setText(mDataList.get(position).getFence().getName());
        tvShijian.setText(TimeUtil.toYMDHMSTime(mDataList.get(position).getCreateTime()));

        //这句话关掉IOS阻塞式交互效果 并依次打开左滑右滑
        ((SwipeMenuView)holder.itemView).setIos(false);

        Button btnDelete = holder.getView(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    mOnSwipeListener.onDel(position);
                }
            }
        });
    }

    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int pos);
    }

    private onSwipeListener mOnSwipeListener;

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }
}
