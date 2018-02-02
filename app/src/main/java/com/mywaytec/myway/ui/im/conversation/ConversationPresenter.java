package com.mywaytec.myway.ui.im.conversation;

import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.bean.ClubDetailBean;
import com.mywaytec.myway.model.bean.GoldInfoBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.view.CommonSubscriber;

import javax.inject.Inject;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

/**
 * Created by shemh on 2017/8/10.
 */

public class ConversationPresenter extends RxPresenter<ConversationView> {

    private RetrofitHelper retrofitHelper;

    @Inject
    public ConversationPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(ConversationView view) {
        super.attachView(view);
    }

    public RetrofitHelper getRetrofitHelper(){
        return retrofitHelper;
    }

    UserInfo rongUserInfo;
    boolean isUserInfo;
    int count;
    public void setRongUserInfo(Conversation.ConversationType mConversationType, final String userId){
//        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
//
//            @Override
//            public UserInfo getUserInfo(final String userId) {
//                //根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
//                Log.i("TAG", "------userId,"+userId);
//                retrofitHelper.getUserInfo(userId)
//                        .compose(RxUtil.<com.mywaytec.myway.model.bean.UserInfo>rxSchedulerHelper())
//                        .subscribe(new CommonSubscriber<com.mywaytec.myway.model.bean.UserInfo>() {
//                            @Override
//                            public void onNext(com.mywaytec.myway.model.bean.UserInfo userInfo) {
//                                if (null != userInfo.getObj()) {
//                                    String name = userInfo.getObj().getNickname();
//                                    String img = userInfo.getObj().getImgeUrl();
//                                    if (name == null){
//                                        name = "";
//                                    }
//                                    if (img == null){
//                                        img = "";
//                                    }
//                                    rongUserInfo = new UserInfo(userId, name, Uri.parse(img));
//                                }
//                            }
//                        });
//                while (!isUserInfo){//数据更新完成后再return
//                    if (null != rongUserInfo){
//                        isUserInfo = true;
//                    }
//                    if (count > 100){
//                        return rongUserInfo;
//                    }
//                    count++;
//                    SystemClock.sleep(100);
//                }
//                return rongUserInfo;
//            }
//        }, true);

        if (Conversation.ConversationType.GROUP.equals(mConversationType)){//群聊
            String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
            String token = PreferencesUtils.getLoginInfo().getObj().getToken();
            retrofitHelper.rongClubDetail(uid, token, Integer.parseInt(userId.substring(3)))
                    .compose(RxUtil.<ClubDetailBean>rxSchedulerHelper())
                    .subscribe(new CommonSubscriber<ClubDetailBean>() {
                        @Override
                        public void onNext(ClubDetailBean clubDetailBean) {
                            if (clubDetailBean.getCode() == 1){
                                Group group = new Group(userId, clubDetailBean.getObj().getGroupname(),
                                        Uri.parse(clubDetailBean.getObj().getImgUrl()));
                                RongIM.getInstance().refreshGroupInfoCache(group);
                            }
                        }
                    });
        }else {
            retrofitHelper.getUserInfo(userId)
                    .compose(RxUtil.<com.mywaytec.myway.model.bean.UserInfo>rxSchedulerHelper())
                    .subscribe(new CommonSubscriber<com.mywaytec.myway.model.bean.UserInfo>() {
                        @Override
                        public void onNext(com.mywaytec.myway.model.bean.UserInfo userInfo) {
                            if (null != userInfo.getObj()) {
                                String name = userInfo.getObj().getNickname();
                                String img = userInfo.getObj().getImgeUrl();
                                if (name == null){
                                    name = "";
                                }
                                if (img == null){
                                    img = "";
                                }
                                rongUserInfo = new UserInfo(userId, name, Uri.parse(img));
                                RongIM.getInstance().refreshUserInfoCache(rongUserInfo);
                            }
                        }
                    });
        }



    }
}
