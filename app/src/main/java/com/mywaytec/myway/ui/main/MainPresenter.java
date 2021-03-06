package com.mywaytec.myway.ui.main;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.bean.GoldInfoBean;
import com.mywaytec.myway.model.bean.ObjIntBean;
import com.mywaytec.myway.model.bean.ObjStringBean;
import com.mywaytec.myway.model.bean.OtherMsgBean;
import com.mywaytec.myway.model.bean.RongTokenBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.DialogUtils;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CommonSubscriber;

import java.text.MessageFormat;

import javax.inject.Inject;

import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by shemh on 2017/3/8.
 */

public class MainPresenter extends RxPresenter<MainView> implements IUnReadMessageObserver {

    RetrofitHelper retrofitHelper;

    @Inject
    public MainPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    public void initRongIM() {
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        retrofitHelper.clubRongToken(uid)
                .compose(RxUtil.<RongTokenBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<RongTokenBean>() {
                    @Override
                    public void onNext(RongTokenBean rongTokenBean) {
                        Log.i("TAG", "------ObjStringBean" + rongTokenBean.getCode() + "," + rongTokenBean.getMsg());
                        if (rongTokenBean.getCode() == 1){
                            connect(rongTokenBean.getObj().getRongToken());
                        }
                    }
                });
        initData();
    }

    /**
     * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在 {@link # init(Context)} 之后调用。</p>
     * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
     *
     * @param token 从服务端获取的用户身份令牌（Token）。
     * @return RongIM  客户端核心类的实例。
     */
    private void connect(String token) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {

            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
            }

            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                Log.i("TAG", "------onSuccess" + userid);
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.i("TAG", "------onError" + errorCode.getMessage() + "," + errorCode.getValue());
            }
        });
    }

    public void initData() {

        final Conversation.ConversationType[] conversationTypes = {
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE
        };

        RongIM.getInstance().addUnReadMessageCountChangedObserver(this, conversationTypes);
    }

    @Override
    public void onCountChanged(int i) {//消息数量改变
        if (i > 0) {
            mView.getXiaoxiHintImg().setVisibility(View.VISIBLE);
        }else {
            mView.getXiaoxiHintImg().setVisibility(View.GONE);
        }
    }

    //获取侧边栏信息
    public void getOtherMsg(final TextView tvUnreadCount, final TextView tvPaiming) {
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.getOtherMsg(uid, token)
                .compose(RxUtil.<OtherMsgBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<OtherMsgBean>() {
                    @Override
                    public void onNext(OtherMsgBean otherMsgBean) {
                        if (otherMsgBean.getCode() == 1 || otherMsgBean.getCode() == 11) {
                            tvUnreadCount.setText(otherMsgBean.getObj().getMsgUnread() + "");
                            String raking = otherMsgBean.getObj().getRanking();
                            if ("0".equals(raking) || "-1".equals(raking)) {
                                tvPaiming.setText(R.string.none);
                            } else {
                                tvPaiming.setText(MessageFormat.format(mView.getContext()
                                        .getResources().getString(R.string.no), raking));
                            }
                            if ("true".equals(otherMsgBean.getObj().getIsSign())) {
                                mView.getSigninImg().setImageResource(R.mipmap.cebianlan_qiandao_done);
                                mView.getSigninTV().setText(R.string.signed_up_already_activity);
                                mView.getSigninTV().setTextColor(Color.parseColor("#e0e0e0"));
                            } else {
                                mView.getSigninImg().setImageResource(R.mipmap.cebianlan_qiandao);
                                mView.getSigninTV().setText(R.string.sign_in);
                                mView.getSigninTV().setTextColor(Color.parseColor("#ffffff"));
                            }
                        } else if (otherMsgBean.getCode() == 19) {
                            DialogUtils.reLoginDialog(mView.getContext());
                        } else {
                            tvPaiming.setText(R.string.none);
                        }
                    }
                });


        //获取未读消息
        retrofitHelper.getUnreadCount(uid)
                .compose(RxUtil.<ObjIntBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ObjIntBean>() {
                    @Override
                    public void onNext(ObjIntBean objStringBean) {
                        if (objStringBean.getCode() == 353) {
                            if (objStringBean.getObj() == 0) {
                                tvUnreadCount.setVisibility(View.GONE);
                            } else {
                                tvUnreadCount.setVisibility(View.VISIBLE);
                                tvUnreadCount.setText(objStringBean.getObj() + "");
                            }
                        }
                    }
                });
    }

    //签到
    public void userSign() {
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.userSign(uid, token)
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseInfo>() {
                    @Override
                    public void onNext(BaseInfo baseInfo) {
                        if (baseInfo.getCode() == 1) {
                            ToastUtils.showToast(R.string.sign_up_successfully);
                            mView.getSigninImg().setImageResource(R.mipmap.cebianlan_qiandao_done);
                            mView.getSigninTV().setText(R.string.signed_up_already_activity);
                            mView.getSigninTV().setTextColor(Color.parseColor("#e0e0e0"));
                        } else if (baseInfo.getCode() == -2) {
                            ToastUtils.showToast(R.string.signed_up_already);
                            mView.getSigninImg().setImageResource(R.mipmap.cebianlan_qiandao_done);
                            mView.getSigninTV().setText(R.string.signed_up_already_activity);
                            mView.getSigninTV().setTextColor(Color.parseColor("#e0e0e0"));
                        }else {
                            ToastUtils.showToast(baseInfo.getMsg());
                        }
                    }
                });
    }

    //查询积分金币
    public void queryIntegralGold(final ImageView imglevel, final TextView tvLevel) {
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        retrofitHelper.queryIntegralGold(uid)
                .compose(RxUtil.<GoldInfoBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<GoldInfoBean>() {
                    @Override
                    public void onNext(GoldInfoBean goldInfoBean) {
                        if (goldInfoBean.getCode() == 1) {
                            mView.getGoldTV().setText(goldInfoBean.getObj().getGold() + "");
                            loadLevel(imglevel, tvLevel, goldInfoBean.getObj().getIntegral());
                        }
                    }
                });
    }

    //显示用户等级
    public void loadLevel(ImageView imglevel, TextView tvLevel, int jifen) {
        int levelStr = R.string.初级车手;
        int dengjiImg = 0;
        if (jifen >= 0 && jifen < 80) {
            levelStr = R.string.初级车手;
            dengjiImg = R.mipmap.chujicheshou_00;
        } else if (jifen >= 80 && jifen < 240) {
            levelStr = R.string.普通车手;
            dengjiImg = R.mipmap.putongcheshou_01;
        } else if (jifen >= 240 && jifen < 480) {
            levelStr = R.string.资深车手;
            dengjiImg = R.mipmap.zishencheshou_02;
        } else if (jifen >= 480 && jifen < 800) {
            levelStr = R.string.赛车手E级;
            dengjiImg = R.mipmap.saicheshou_e_03;
        } else if (jifen >= 800 && jifen < 1200) {
            levelStr = R.string.赛车手D级;
            dengjiImg = R.mipmap.saicheshou_d_04;
        } else if (jifen >= 1200 && jifen < 1680) {
            levelStr = R.string.赛车手C级;
            dengjiImg = R.mipmap.saicheshou_c_05;
        } else if (jifen >= 1680 && jifen < 2240) {
            levelStr = R.string.赛车手B级;
            dengjiImg = R.mipmap.saicheshou_b_06;
        } else if (jifen >= 2240 && jifen < 2880) {
            levelStr = R.string.赛车手A级;
            dengjiImg = R.mipmap.saicheshou_a_07;
        } else if (jifen >= 2880 && jifen < 3600) {
            levelStr = R.string.F1赛车手;
            dengjiImg = R.mipmap.saicheshou_f1_08;
        } else if (jifen >= 3600 && jifen < 4400) {
            levelStr = R.string.赛车之王;
            dengjiImg = R.mipmap.saichezhiwang_09;
        } else if (jifen >= 4400 && jifen < 5280) {
            levelStr = R.string.飞车之王;
            dengjiImg = R.mipmap.feichezhiwang_10;
        } else if (jifen >= 5280 && jifen < 6240) {
            levelStr = R.string.超速之王;
            dengjiImg = R.mipmap.chaosuzhiwang_11;
        } else if (jifen >= 6240 && jifen < 7280) {
            levelStr = R.string.极速之王;
            dengjiImg = R.mipmap.jisuzhiwang_12;
        } else if (jifen >= 7280 && jifen < 8400) {
            levelStr = R.string.光速之王;
            dengjiImg = R.mipmap.guangsuzhiwang_13;
        } else if (jifen >= 8400 && jifen < 9600) {
            levelStr = R.string.车王之王;
            dengjiImg = R.mipmap.chewangzhiwang_14;
        } else if (jifen >= 9600 && jifen < 10880) {
            levelStr = R.string.新晋车神;
            dengjiImg = R.mipmap.xinjincheshen_15;
        } else if (jifen >= 10880 && jifen < 12240) {
            levelStr = R.string.风速车神;
            dengjiImg = R.mipmap.xinjincheshen_16;
        } else if (jifen >= 12240 && jifen < 13680) {
            levelStr = R.string.火速车神;
            dengjiImg = R.mipmap.fengsucheshen_17;
        } else if (jifen >= 13680 && jifen < 15200) {
            levelStr = R.string.闪电车神;
            dengjiImg = R.mipmap.shandiancheshen_18;
        } else if (jifen >= 15200 && jifen < 18000) {
            levelStr = R.string.雷霆车神;
            dengjiImg = R.mipmap.leitingcheshen_19;
        } else if (jifen >= 18000) {
            levelStr = R.string.曼威车神;
            dengjiImg = R.mipmap.manweicheshen_20;
        }
        Glide.with(mView.getContext()).load(dengjiImg).into(imglevel);
        tvLevel.setText(levelStr);
    }
}
