package com.mywaytec.myway.fragment.chat;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.adapter.ConversationListAdapterEx;
import com.mywaytec.myway.base.BaseFragment;
import com.mywaytec.myway.fragment.club.ClubFragment;
import com.mywaytec.myway.ui.im.clubVerify.ClubVerifyActivity;
import com.mywaytec.myway.ui.im.createClub.CreateClubActivity;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by shemh on 2017/11/27.
 */

public class ChatFragment extends BaseFragment<ChatPresenter> implements ChatView {

    @BindView(R.id.layout_conversationlist)
    FrameLayout layoutConversationlist;
    @BindView(R.id.tv_xiaoxi)
    TextView tvXiaoxi;
    @BindView(R.id.tv_julebu)
    TextView tvJulebu;
    @BindView(R.id.layout_club_message)
    LinearLayout layoutClubMessage;
    @BindView(R.id.tv_club_name)
    TextView tvClubName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_content)
    TextView tvContent;

    FragmentTransaction transaction;

    /**
     * 显示的是否是消息页面
     */
    private boolean isXiaoxi = true;

    private static ChatFragment chatFragment;

    public static ChatFragment getInstance() {
        if (chatFragment == null) {
            return new ChatFragment();
        }
        return chatFragment;
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void initInjector() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected void initViews() {
        mConversationListFragment = (ConversationListFragment) initConversationList();
        /* 加载 ConversationFragment */
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_conversationlist, mConversationListFragment);
        transaction.commit();

        mPresenter.setRongUserInfo();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isXiaoxi){
            mPresenter.isClubMessage();
        }
    }

    @Override
    protected void updateViews() {

    }

    public void refreshClubList(){
        tvJulebu.performClick();
    }

    @OnClick({R.id.tv_xiaoxi, R.id.tv_julebu, R.id.img_right, R.id.layout_club_message})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_xiaoxi://消息
                isXiaoxi = true;
                mPresenter.isClubMessage();
                tvXiaoxi.setTextColor(Color.parseColor("#ffffff"));
                tvJulebu.setTextColor(Color.parseColor("#999999"));
                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.layout_conversationlist, mConversationListFragment);
                transaction.commit();
                break;
            case R.id.tv_julebu://俱乐部
                isXiaoxi = false;
                layoutClubMessage.setVisibility(View.GONE);
                tvXiaoxi.setTextColor(Color.parseColor("#999999"));
                tvJulebu.setTextColor(Color.parseColor("#ffffff"));
                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.layout_conversationlist, new ClubFragment());
                transaction.commit();
                break;
            case R.id.img_right://创建俱乐部
                Intent intent = new Intent(getActivity(), CreateClubActivity.class);
                getActivity().startActivityForResult(intent, CreateClubActivity.CREATE_CLUB);
                break;
            case R.id.layout_club_message://俱乐部消息
                Intent intent2 = new Intent(getActivity(), ClubVerifyActivity.class);
                startActivity(intent2);
                break;
        }
    }

    /**
     * 会话列表的fragment
     */
    private ConversationListFragment mConversationListFragment = null;

    private Fragment initConversationList() {
        if (mConversationListFragment == null) {
            ConversationListFragment listFragment = new ConversationListFragment();
            listFragment.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
            Uri uri;
            uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//系统
                    .build();
            listFragment.setUri(uri);
            return listFragment;
        } else {
            return mConversationListFragment;
        }
    }

    @Override
    public LinearLayout getClubMessageLayout() {
        return layoutClubMessage;
    }

    @Override
    public TextView getClubNameTV() {
        return tvClubName;
    }

    @Override
    public TextView getTimeTV() {
        return tvTime;
    }

    @Override
    public TextView getContentTV() {
        return tvContent;
    }
}
