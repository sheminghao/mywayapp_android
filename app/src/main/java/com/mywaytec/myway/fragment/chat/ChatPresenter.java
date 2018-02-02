package com.mywaytec.myway.fragment.chat;

import android.content.Context;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.bean.ClubMessageBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.TimeUtil;
import com.mywaytec.myway.view.CommonSubscriber;

import javax.inject.Inject;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * Created by shemh on 2017/11/27.
 */

public class ChatPresenter extends RxPresenter<ChatView> {

    private RetrofitHelper retrofitHelper;
    private Context mContext;

    @Inject
    public ChatPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
    }

    @Override
    public void attachView(ChatView view) {
        super.attachView(view);
        mContext = mView.getContext();
    }

    UserInfo rongUserInfo;
    boolean isUserInfo;
    int count;
    public void setRongUserInfo(){
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

            @Override
            public UserInfo getUserInfo(final String userId) {
                //根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
                Log.i("TAG", "------userId,"+userId);
                retrofitHelper.getUserInfo(userId)
                        .compose(RxUtil.<com.mywaytec.myway.model.bean.UserInfo>rxSchedulerHelper())
                        .subscribe(new CommonSubscriber<com.mywaytec.myway.model.bean.UserInfo>() {
                            @Override
                            public void onNext(com.mywaytec.myway.model.bean.UserInfo userInfo) {
                                if (null != userInfo.getObj()) {
                                    String name = userInfo.getObj().getNickname();
                                    String img = userInfo.getObj().getImgeUrl();
                                    Log.i("TAG", "------name,"+name + ",img," + img);
                                    if (name == null){
                                        name = "";
                                    }
                                    if (img == null){
                                        img = "";
                                    }
                                    rongUserInfo = new UserInfo(userId, name, Uri.parse(img));
                                }
                            }
                        });
                while (!isUserInfo){
                    if (null != rongUserInfo){
                        isUserInfo = true;
                    }
                    if (count > 100){
                        return rongUserInfo;
                    }
                    count++;
                    SystemClock.sleep(100);
                }
                return rongUserInfo;
            }

        }, true);
    }

    public void isClubMessage(){
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.clubJoinVerify(uid, token)
                .compose(RxUtil.<ClubMessageBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ClubMessageBean>() {
                    @Override
                    public void onNext(ClubMessageBean clubMessageBean) {
                        if (clubMessageBean.getCode() == 1){
                            mView.getClubMessageLayout().setVisibility(View.VISIBLE);
                            mView.getTimeTV().setText(TimeUtil.YMDHMStoMDHMTime(clubMessageBean.getObj().getCreateTime()));
                            mView.getContentTV().setText(clubMessageBean.getObj().getUser().getNickname()+
                            mContext.getResources().getString(R.string.apply_for_participation)
                            +clubMessageBean.getObj().getClub().getGroupname());
                        }else {
                            mView.getClubMessageLayout().setVisibility(View.GONE);
                        }
                    }
                });
    }
}
