package com.mywaytec.myway.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.ListBaseAdapter;
import com.mywaytec.myway.base.SuperViewHolder;
import com.mywaytec.myway.model.bean.RoutePathsListBean;
import com.mywaytec.myway.utils.TimeUtil;
import com.mywaytec.myway.view.SwipeMenuView;

import java.text.DecimalFormat;

/**
 * 我的轨迹适配器
*/
public class MyTrackAdapter extends ListBaseAdapter<RoutePathsListBean.ObjBean> {

    private DecimalFormat df = new DecimalFormat("######0.00");

    public MyTrackAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_mytrack;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        //这句话关掉IOS阻塞式交互效果 并依次打开左滑右滑
        ((SwipeMenuView)holder.itemView).setIos(false);
        TextView tvTrackname = holder.getView(R.id.tv_trackname);
        TextView tvPublishTime = holder.getView(R.id.tv_publish_time);
        TextView tvLicheng = holder.getView(R.id.tv_licheng);
        TextView tvTime = holder.getView(R.id.tv_time);
        tvTrackname.setText(mDataList.get(position).getName() + "");
        tvPublishTime.setText(TimeUtil.YMDHMStoMDHMTime(mDataList.get(position).getCreateTime()));
        tvLicheng.setText(df.format(mDataList.get(position).getLegend()/1000.0) + "km");
        tvTime.setText(mDataList.get(position).getDuration()+"");

        View contentView = holder.getView(R.id.swipe_content);
        Button btnDelete = holder.getView(R.id.btnDelete);
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
        //注意事项，设置item点击，不能对整个holder.itemView设置咯，只能对第一个子View，即原来的content设置，这算是局限性吧。
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick() called with: v = [" + v + "]");
                onItemClickListener.onItemClick(position);
            }
        });
    }

    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int pos);

        void onTop(int pos);
    }

    private MycarInfoAdapter.onSwipeListener mOnSwipeListener;

    public void setOnDelListener(MycarInfoAdapter.onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private MycarInfoAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(MycarInfoAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
