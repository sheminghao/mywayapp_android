package com.mywaytec.myway.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.ListBaseAdapter;
import com.mywaytec.myway.base.SuperViewHolder;
import com.mywaytec.myway.model.db.FingerWarkInfo;
import com.mywaytec.myway.view.SwipeMenuView;

/**
 * Created by shemh on 2017/8/16.
 */

public class FingerMarkAdapter extends ListBaseAdapter<FingerWarkInfo> {

    public FingerMarkAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_finger_mark;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        TextView tvName = holder.getView(R.id.tv_name);
        String fingerName = mDataList.get(position).getFingerName();
        if (TextUtils.isEmpty(fingerName)){
            tvName.setText(mContext.getResources().getString(R.string.指纹)+mDataList.get(position).getFingerWarkId());
        }else {
            tvName.setText(fingerName);
        }

        //这句话关掉IOS阻塞式交互效果 并依次打开左滑右滑
        ((SwipeMenuView)holder.itemView).setIos(false);

        Button btnDelete = holder.getView(R.id.btn_delete);
        Button btnChange = holder.getView(R.id.btn_change);
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
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    mOnSwipeListener.onChange(position);
                }
            }
        });
    }

    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int pos);

        void onChange(int pos);
    }

    private onSwipeListener mOnSwipeListener;

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }
}
