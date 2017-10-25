package com.mywaytec.myway.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.ListBaseAdapter;
import com.mywaytec.myway.base.SuperViewHolder;
import com.mywaytec.myway.model.bean.DynamicDetailBean;
import com.mywaytec.myway.model.bean.MessageBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.view.CircleImageView;
import com.mywaytec.myway.view.CommonSubscriber;

/**
 * Created by shemh on 2017/5/3.
 */

public class MessageAdapter extends ListBaseAdapter<MessageBean.ObjBean> {

    RetrofitHelper retrofitHelper;
    String nickname;

    public MessageAdapter(Context context, RetrofitHelper retrofitHelper) {
        super(context);
        this.retrofitHelper = retrofitHelper;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_message;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        CircleImageView imgHeadPortrait = holder.getView(R.id.img_head_portrait);
        if (mDataList.get(position).getFromUser().isGender()){
            Glide.with(mContext).load(mDataList.get(position).getFromUser().getImgeUrl())
                    .error(R.mipmap.touxiang_boy_nor)
                    .into(imgHeadPortrait);
        }else {
            Glide.with(mContext).load(mDataList.get(position).getFromUser().getImgeUrl())
                    .error(R.mipmap.touxiang_girl_nor)
                    .into(imgHeadPortrait);
        }
        final TextView tvMessage = holder.getView(R.id.tv_message);
        tvMessage.setTag(position);
        TextView tvPublishTime = holder.getView(R.id.tv_publish_time);
        tvPublishTime.setText(mDataList.get(position).getCreateTime());

        nickname = mDataList.get(position).getFromUser().getNickname();

        retrofitHelper.getDynamicDetail(mDataList.get(position).getShId()+"")
                .compose(RxUtil.<DynamicDetailBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DynamicDetailBean>() {
                    @Override
                    public void onNext(DynamicDetailBean dynamicDetailBean) {
                        if (dynamicDetailBean.getCode() == 323){
                            String messgae = "";
                            //type: 1.系统消息 2.评论消息 3.点赞消息 4.回复消息 5.广告消息
                            int type = mDataList.get(position).getType();
                            nickname = mDataList.get(position).getFromUser().getNickname();
                            Log.i("TAG", "------dynamicDetailBean" + nickname);
                            switch (type){
                                case 1:
                                    messgae= mDataList.get(position).getText();
                                    break;
                                case 2:
                                    messgae = "<font color='#f78a09'>"+nickname+"</font>"
                                            +mContext.getResources().getString(R.string.comments_on_your_sharing)+"：“"
                                            +dynamicDetailBean.getObj().getContent()+"”";
                                    break;
                                case 3:
                                    messgae = "<font color='#f78a09'>"+nickname+"</font>"
                                            +mContext.getResources().getString(R.string.praise_your_sharing)+"：“"
                                            +dynamicDetailBean.getObj().getContent()+"”";
                                    break;
                                case 4:
                                    break;
                                case 5:
                                    break;
                            }
                            if (position == (int)tvMessage.getTag())
                            tvMessage.setText(Html.fromHtml(messgae));
                        }
                    }
                });
    }
}
