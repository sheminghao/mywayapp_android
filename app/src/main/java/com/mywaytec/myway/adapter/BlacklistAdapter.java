package com.mywaytec.myway.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.ListBaseAdapter;
import com.mywaytec.myway.base.SuperViewHolder;
import com.mywaytec.myway.model.bean.BlacklistBean;
import com.mywaytec.myway.view.SwipeMenuView;

/**
 */

public class BlacklistAdapter extends ListBaseAdapter<BlacklistBean.ObjBean> {

    protected Context mContext;

    public BlacklistAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_blacklist;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        TextView tvName = holder.getView(R.id.tv_name);
        TextView tvTime = holder.getView(R.id.tv_time);
        tvName.setText(mDataList.get(position).getUser().getNickname());
        tvTime.setText(mDataList.get(position).getCreateTime());

        ImageView imgHeadPortrait = holder.getView(R.id.img_head_portrait);
        if (mDataList.get(position).getUser().isGender()) {
            Glide.with(mContext).load(mDataList.get(position).getUser().getImgeUrl())
                    .error(R.mipmap.touxiang_boy_nor)
                    .into(imgHeadPortrait);
        }else {
            Glide.with(mContext).load(mDataList.get(position).getUser().getImgeUrl())
                    .error(R.mipmap.touxiang_girl_nor)
                    .into(imgHeadPortrait);
        }

        View contentView = holder.getView(R.id.swipe_content);
        Button btnDelete = holder.getView(R.id.btnDelete);
        //这句话关掉IOS阻塞式交互效果 并依次打开左滑右滑
        ((SwipeMenuView)holder.itemView).setIos(false);

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
                if (null != onItemClickListener) {
                    onItemClickListener.onItemClick(position);
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

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private MycarInfoAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(MycarInfoAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
