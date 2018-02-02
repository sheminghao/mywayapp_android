package com.mywaytec.myway.ui.im.conversation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.ui.im.exitClub.ExitClubActivity;
import com.mywaytec.myway.ui.personalCenter.PersonalCenterActivity;
import com.mywaytec.myway.ui.userDynamic.UserDynamicActivity;
import com.mywaytec.myway.utils.AppUtils;
import com.mywaytec.myway.utils.PreferencesUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.TypingMessage.TypingStatus;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

//CallKit start 1
//CallKit end 1

/**
 * 会话页面
 * 1，设置 ActionBar title
 * 2，加载会话页面
 * 3，push 和 通知 判断
 */
public class ConversationActivity extends BaseActivity<ConversationPresenter> implements ConversationView {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_conversation)
    LinearLayout layoutConversation;
    @BindView(R.id.img_right)
    ImageView imgRight;

    /**
     * 对方id
     */
    private String mTargetId;
    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;
    /**
     * title
     */
    private String title;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_conversation;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        layoutConversation.setPadding(0, AppUtils.getStatusBar(), 0, 0);

        Intent intent = getIntent();

        mTargetId = intent.getData().getQueryParameter("targetId");
        title = intent.getData().getQueryParameter("title");
        mConversationType = Conversation.ConversationType.valueOf(intent.getData()
                .getLastPathSegment().toUpperCase(Locale.US));

        if (Conversation.ConversationType.GROUP.equals(mConversationType)){
            imgRight.setVisibility(View.VISIBLE);
        }else {
            imgRight.setVisibility(View.GONE);
        }

        enterFragment(mConversationType ,mTargetId);
        tvTitle.setText(title);

        RongIMClient.setTypingStatusListener(new RongIMClient.TypingStatusListener() {
            @Override
            public void onTypingStatusChanged(Conversation.ConversationType type, String targetId, Collection<TypingStatus> typingStatusSet) {
                //当输入状态的会话类型和targetID与当前会话一致时，才需要显示
                if (type.equals(mConversationType) && targetId.equals(mTargetId)) {
                    //count表示当前会话中正在输入的用户数量，目前只支持单聊，所以判断大于0就可以给予显示了
                    int count = typingStatusSet.size();
                    if (count > 0) {
                        Iterator iterator = typingStatusSet.iterator();
                        TypingStatus status = (TypingStatus) iterator.next();
                        String objectName = status.getTypingContentType();

                        MessageTag textTag = TextMessage.class.getAnnotation(MessageTag.class);
                        MessageTag voiceTag = VoiceMessage.class.getAnnotation(MessageTag.class);
                        //匹配对方正在输入的是文本消息还是语音消息
                        if (objectName.equals(textTag.value())) {
                            //显示“对方正在输入”
                            tvTitle.post(new Runnable() {
                                @Override
                                public void run() {
                                    tvTitle.setText(R.string.the_other_person_is_typing);
                                }
                            });
                        } else if (objectName.equals(voiceTag.value())) {
                            //显示"对方正在讲话"
                            tvTitle.post(new Runnable() {
                                @Override
                                public void run() {
                                    tvTitle.setText(R.string.the_other_party_is_talking);
                                }
                            });
                        }
                    } else {
                        //当前会话没有用户正在输入，标题栏仍显示原来标题
                        tvTitle.post(new Runnable() {
                            @Override
                            public void run() {
                                tvTitle.setText(title);
                            }
                        });
                    }
                }
            }
        });

        mPresenter.setRongUserInfo(mConversationType, mTargetId);

        /**
         * 设置会话界面操作的监听器。
         */
        RongIM.setConversationBehaviorListener(new MyConversationBehaviorListener());
    }

    @Override
    protected void updateViews() {

    }

    @OnClick({R.id.img_right})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.img_right://更多详情
                Intent intent = new Intent(this, ExitClubActivity.class);
                intent.putExtra("gid", mTargetId);
                startActivity(intent);
                break;
        }
    }

    private class MyConversationBehaviorListener implements RongIM.ConversationBehaviorListener{

        /**
         * 当点击用户头像后执行。
         *
         * @param context           上下文。
         * @param conversationType  会话类型。
         * @param userInfo          被点击的用户的信息。
         * @return 如果用户自己处理了点击后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
         */
        @Override
        public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
            String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
            if (uid.equals(userInfo.getUserId())){
                Intent intent = new Intent(ConversationActivity.this, PersonalCenterActivity.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(ConversationActivity.this, UserDynamicActivity.class);
                intent.putExtra("uid", userInfo.getUserId());
                intent.putExtra("nickName", userInfo.getName());
                startActivity(intent);
            }
            return true;
        }

        /**
         * 当长按用户头像后执行。
         *
         * @param context          上下文。
         * @param conversationType 会话类型。
         * @param userInfo         被点击的用户的信息。
         * @return 如果用户自己处理了点击后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
         */
        @Override
        public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
            return false;
        }

        /**
         * 当点击消息时执行。
         *
         * @param context 上下文。
         * @param view    触发点击的 View。
         * @param message 被点击的消息的实体信息。
         * @return 如果用户自己处理了点击后的逻辑，则返回 true， 否则返回 false, false 走融云默认处理方式。
         */
        @Override
        public boolean onMessageClick(Context context, View view, Message message) {
            return false;
        }

        /**
         * 当长按消息时执行。
         *
         * @param context 上下文。
         * @param view    触发点击的 View。
         * @param message 被长按的消息的实体信息。
         * @return 如果用户自己处理了长按后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
         */
        @Override
        public boolean onMessageLongClick(Context context, View view, Message message) {
            return false;
        }
        /**
         * 当点击链接消息时执行。
         *
         * @param context 上下文。
         * @param link    被点击的链接。
         * @return 如果用户自己处理了点击后的逻辑处理，则返回 true， 否则返回 false, false 走融云默认处理方式。
         */
        @Override
        public boolean onMessageLinkClick(Context context, String link) {
            return false;
        }
    }

    /**
     * 加载会话页面 ConversationFragmentEx 继承自 ConversationFragment
     *
     * @param mConversationType 会话类型
     * @param mTargetId         会话 Id
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {
        ConversationFragment fragment = new ConversationFragment();

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //xxx 为你要加载的 id
        transaction.add(R.id.rong_content, fragment);
        transaction.commitAllowingStateLoss();
    }
}
