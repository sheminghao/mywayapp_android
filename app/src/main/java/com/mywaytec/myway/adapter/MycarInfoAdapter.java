package com.mywaytec.myway.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mywaytec.myway.APP;
import com.mywaytec.myway.ConnectedBleInfoDao;
import com.mywaytec.myway.DaoSession;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.ListBaseAdapter;
import com.mywaytec.myway.base.SuperViewHolder;
import com.mywaytec.myway.model.db.ConnectedBleInfo;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.SwipeMenuView;

/**
 * Created by shemh on 2017/4/11.
 */

public class MycarInfoAdapter extends ListBaseAdapter<ConnectedBleInfo> {

    private Context context;
    private int type;

    public MycarInfoAdapter(Context context, int type) {
        super(context);
        this.context = context;
        this.type = type;
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_item_swipe;
    }

    @Override
    public int getItemCount() {
        if (type == 3) {
            if (mDataList.size() > 3) {
                return 3;
            } else {
                return mDataList.size();
            }
        }else {
            return mDataList.size();
        }
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
//        TextView tvCarType = holder.getView(R.id.tv_car_type);
        final TextView tvName = holder.getView(R.id.tv_name);
        tvName.setText(mDataList.get(position).getBleName());
        ImageView imgChangeName = holder.getView(R.id.img_change_name);
        imgChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNameDialog(tvName, position);
            }
        });

        View viewLine = holder.getView(R.id.view_line);
        if (position == mDataList.size()-1){
            viewLine.setVisibility(View.INVISIBLE);
        }else {
            viewLine.setVisibility(View.VISIBLE);
        }

        View contentView = holder.getView(R.id.swipe_content);
        Button btnDelete = holder.getView(R.id.btnDelete);
        //这句话关掉IOS阻塞式交互效果 并依次打开左滑右滑
        ((SwipeMenuView)holder.itemView).setIos(false);
//       .setLeftSwipe(position % 2 == 0 ? true : false);
//        title.setText(getDataList().get(position).title + (position % 2 == 0 ? "我只能右滑动" : "我只能左滑动"));

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

    public void changeNameDialog(final TextView tvName, final int position){
         android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        // 创建对话框
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View view = View.inflate(context, R.layout.dialog_change_car_name, null);
        TextView tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        final EditText etName = (EditText) view.findViewById(R.id.et_name);
        etName.setText(tvName.getText().toString());
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                if (TextUtils.isEmpty(name)){
                    ToastUtils.showToast(R.string.please_enter_name);
                }else{
                    DaoSession daoSession = APP.getInstance().getDaoSession();
                    ConnectedBleInfoDao connectedBleInfoDao = daoSession.getConnectedBleInfoDao();
                    ConnectedBleInfo connectedBleInfo = new ConnectedBleInfo();
                    connectedBleInfo.setAddress(mDataList.get(position).getAddress());
                    connectedBleInfo.setBleName(name);
                    connectedBleInfo.setTime(mDataList.get(position).getTime());
                    connectedBleInfo.setId(mDataList.get(position).getId());
                    connectedBleInfoDao.update(connectedBleInfo);
                    mDataList.get(position).setBleName(name);
                    tvName.setText(name);
                    alertDialog.dismiss();
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(view);
        alertDialog.show();
    }

    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int pos);

        void onTop(int pos);
    }

    private onSwipeListener mOnSwipeListener;

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
