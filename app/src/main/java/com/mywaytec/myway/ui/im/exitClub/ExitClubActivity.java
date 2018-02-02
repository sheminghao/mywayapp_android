package com.mywaytec.myway.ui.im.exitClub;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.ui.im.clubMember.ClubMemberActivity;
import com.mywaytec.myway.ui.im.modifyClub.ModifyClubActivity;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 俱乐部详情，退出俱乐部
 */
public class ExitClubActivity extends BaseActivity<ExitClubPresenter> implements ExitClubView {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_exit_club)
    TextView tvExitJoin;
    @BindView(R.id.img_club_head)
    ImageView imgClubHead;
    @BindView(R.id.tv_club_name)
    TextView tvClubName;
    @BindView(R.id.tv_club_official)
    TextView tvClubOfficial;
    @BindView(R.id.tv_member)
    TextView tvMember;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_jianjie)
    TextView tvJianjie;
    @BindView(R.id.tv_people_num)
    TextView tvPeopleNum;
    @BindView(R.id.img_zhiding_liantiao)
    ImageView imgZhidingliaotian;
    @BindView(R.id.img_miandarao)
    ImageView imgMiandarao;

    private String gid;

    /**
     * 是否免打扰
     */
    private boolean isMiandarao;
    /**
     * 消息是否置顶
     */
    private boolean isTop;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_exit_club;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        gid = getIntent().getStringExtra("gid");

        //消息免打扰状态
        RongIM.getInstance().getConversationNotificationStatus(Conversation.ConversationType.GROUP,
                gid, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                    @Override
                    public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                        if (Conversation.ConversationNotificationStatus.DO_NOT_DISTURB.getValue() == conversationNotificationStatus.getValue()){
                            isMiandarao = true;
                            imgMiandarao.setImageResource(R.mipmap.lijituichu_dakai);
                        }else if(Conversation.ConversationNotificationStatus.NOTIFY.getValue() == conversationNotificationStatus.getValue()){
                            isMiandarao = false;
                            imgMiandarao.setImageResource(R.mipmap.lijituichu_guanbi);
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });

        RongIM.getInstance().getConversation(Conversation.ConversationType.GROUP,
                gid, new RongIMClient.ResultCallback<Conversation>() {
                    @Override
                    public void onSuccess(Conversation conversation) {
                        if (null != conversation) {
                            if (conversation.isTop()) {
                                isTop = true;
                                imgMiandarao.setImageResource(R.mipmap.lijituichu_dakai);
                            } else {
                                isTop = false;
                                imgMiandarao.setImageResource(R.mipmap.lijituichu_guanbi);
                            }
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
    }

    @Override
    protected void updateViews() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadData(Integer.parseInt(gid.substring(3)));
    }

    @OnClick({R.id.tv_exit_club, R.id.img_zhiding_liantiao, R.id.img_miandarao, R.id.img_right,
                R.id.layout_club_member})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_exit_club://立即退出
                mPresenter.exitClub(Integer.parseInt(gid.substring(3)));
                break;
            case R.id.img_zhiding_liantiao://置顶聊天
                if (isTop){//取消置顶
                    RongIM.getInstance().setConversationToTop(Conversation.ConversationType.GROUP,
                            gid, false, new RongIMClient.ResultCallback<Boolean>() {
                                @Override
                                public void onSuccess(Boolean aBoolean) {
                                    isTop = false;
                                    imgZhidingliaotian.setImageResource(R.mipmap.lijituichu_guanbi);
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {

                                }
                            });
                }else {//置顶
                    RongIM.getInstance().setConversationToTop(Conversation.ConversationType.GROUP,
                            gid, true, new RongIMClient.ResultCallback<Boolean>() {
                                @Override
                                public void onSuccess(Boolean aBoolean) {
                                    isTop = true;
                                    imgZhidingliaotian.setImageResource(R.mipmap.lijituichu_dakai);
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {

                                }
                            });
                }
                break;
            case R.id.img_miandarao://消息免打扰
                if (isMiandarao){//关闭免打扰
                    RongIM.getInstance().setConversationNotificationStatus(Conversation.ConversationType.GROUP,
                            gid, Conversation.ConversationNotificationStatus.NOTIFY,
                            new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                                @Override
                                public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                                    isMiandarao = false;
                                    imgMiandarao.setImageResource(R.mipmap.lijituichu_guanbi);
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {

                                }
                            });
                }else {//打开免打扰
                    RongIM.getInstance().setConversationNotificationStatus(Conversation.ConversationType.GROUP,
                            gid, Conversation.ConversationNotificationStatus.DO_NOT_DISTURB,
                            new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                                @Override
                                public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                                    isMiandarao = true;
                                    imgMiandarao.setImageResource(R.mipmap.lijituichu_dakai);
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {

                                }
                            });
                }
                break;
            case R.id.img_right://编辑
                Intent intent = new Intent(this, ModifyClubActivity.class);
                intent.putExtra("gid", gid);
                intent.putExtra("clubHead", mPresenter.getClubHead());
                intent.putExtra("clubName", tvClubName.getText().toString());
                intent.putExtra("clubArea", tvArea.getText().toString());
                intent.putExtra("clubJianjie", tvJianjie.getText().toString());
                startActivityForResult(intent, ModifyClubActivity.MODIFY_CLUB);
                break;
            case R.id.layout_club_member://俱乐部成员
                Intent intent2 = new Intent(this, ClubMemberActivity.class);
                intent2.putExtra("gid", gid);
                startActivity(intent2);
                break;
        }
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public TextView getTitleTV() {
        return tvTitle;
    }

    @Override
    public ImageView getClubHeadImg() {
        return imgClubHead;
    }

    @Override
    public TextView getClubNameTV() {
        return tvClubName;
    }

    @Override
    public TextView getClubOfficialTV() {
        return tvClubOfficial;
    }

    @Override
    public TextView getMemberTV() {
        return tvMember;
    }

    @Override
    public TextView getAreaTV() {
        return tvArea;
    }

    @Override
    public TextView getJianjieTV() {
        return tvJianjie;
    }

    @Override
    public TextView getPeopleNumTV() {
        return tvPeopleNum;
    }
}
