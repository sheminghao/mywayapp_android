package com.mywaytec.myway.ui.userDynamic;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.mywaytec.myway.R;
import com.mywaytec.myway.adapter.OtherDynamicAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.bean.DynamicListBean;
import com.mywaytec.myway.model.bean.PraiseBean;
import com.mywaytec.myway.model.bean.UserInfo;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.utils.data.OtherUserInfo;
import com.mywaytec.myway.view.CommonSubscriber;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/8/7.
 */

public class UserDynamicPresenter extends RxPresenter<UserDynamicView> {

    RetrofitHelper retrofitHelper;
    Context mContext;
    OtherDynamicAdapter dynamicAdapter;
    LRecyclerViewAdapter lRecyclerViewAdapter;
    ImageView imgHeadPortrait;
    TextView tvNickname;
    ImageView imgLevel;
    TextView tvLevel;
    ImageView imgBack;

    private int currentPage = 1;

    @Inject
    public UserDynamicPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(UserDynamicView view) {
        super.attachView(view);
        mContext = mView.getContext();
    }

    public void initRecyclerView(final String otherUid){
        dynamicAdapter = new OtherDynamicAdapter(mContext, retrofitHelper);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(dynamicAdapter);
        View view = View.inflate(mContext, R.layout.view_other_dynamic_header, null);
        imgHeadPortrait = (ImageView) view.findViewById(R.id.img_head_portrait);
        tvNickname = (TextView) view.findViewById(R.id.tv_nickname);
        imgLevel = (ImageView) view.findViewById(R.id.img_level);
        tvLevel = (TextView) view.findViewById(R.id.tv_level);
        imgBack = (ImageView) view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)mContext).finish();
            }
        });
        lRecyclerViewAdapter.addHeaderView(view);
        mView.getRecyclerView().setLayoutManager(new LinearLayoutManager(mContext));
        mView.getRecyclerView().setAdapter(lRecyclerViewAdapter);
        mView.getRecyclerView().setPullRefreshEnabled(false);
        mView.getRecyclerView().setLoadMoreEnabled(true);
        mView.getRecyclerView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                currentPage++;
                getDynamicList(otherUid);
            }
        });

        dynamicAdapter.setOnClickLike(new OtherDynamicAdapter.OnClickLike() {//点赞
            @Override
            public void clickLick(final TextView tvLike, final ImageView imgLike, final int position) {
                String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
                retrofitHelper.like(uid, dynamicAdapter.getDataList().get(position).getId()+"")
                        .compose(RxUtil.<PraiseBean>rxSchedulerHelper())
                        .subscribe(new CommonSubscriber<PraiseBean>() {
                            @Override
                            public void onNext(PraiseBean praiseBean) {
                                if (praiseBean.getCode() == 308){
//                                    ToastUtils.showToast("点赞成功");
                                    int likeNum = Integer.parseInt(tvLike.getText().toString().trim());
                                    dynamicAdapter.getDataList().get(position).setLikeNum(likeNum+1);
                                    dynamicAdapter.getDataList().get(position).setIsLike(true);
                                    imgLike.setImageResource(R.mipmap.dianzan_press);
                                    tvLike.setText(likeNum+1+"");
                                    if (null != praiseBean.getObj()){
                                        ToastUtils.showToast(R.string.sign_up_successfully);
                                    }else {

                                    }
                                }else if (praiseBean.getCode() == 310){
                                    int likeNum = Integer.parseInt(tvLike.getText().toString().trim());
                                    dynamicAdapter.getDataList().get(position).setLikeNum(likeNum-1);
                                    dynamicAdapter.getDataList().get(position).setIsLike(false);
                                    imgLike.setImageResource(R.mipmap.dianzan);
                                    tvLike.setText(likeNum-1+"");
                                }
                            }
                        });
            }
        });

        retrofitHelper.getUserInfo(otherUid)
                .compose(RxUtil.<UserInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<UserInfo>() {
                    @Override
                    public void onNext(UserInfo userInfo) {
                        if (userInfo.getCode() == 240) {
                            OtherUserInfo.saveOtherUserInfo(userInfo);
                            getDynamicList(otherUid);
                            if (userInfo.getObj().isGender()){
                                Glide.with(mContext).load(userInfo.getObj().getImgeUrl())
                                        .error(R.mipmap.touxiang_boy_nor)
                                        .centerCrop()
                                        .into(imgHeadPortrait);
                            }else {
                                Glide.with(mContext).load(userInfo.getObj().getImgeUrl())
                                        .error(R.mipmap.touxiang_girl_nor)
                                        .centerCrop()
                                        .into(imgHeadPortrait);
                            }
                            tvNickname.setText(userInfo.getObj().getNickname());
                            loadLevel(imgLevel, tvLevel, userInfo.getObj().getIntegral());
                        }
                    }
                });
    }

    private void getDynamicList(final String otherUid){
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        Log.i("TAG", "------otherUid,"+otherUid+";uid,"+uid+";token,"+token);
        retrofitHelper.getOtherDynamicList(uid, token, otherUid, currentPage)
                .compose(RxUtil.<DynamicListBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DynamicListBean>() {
                    @Override
                    public void onNext(DynamicListBean dynamicListBean) {
                        if (dynamicListBean.getCode() == 322){
                            if (currentPage == 1) {
                                dynamicAdapter.setDataList(dynamicListBean.getObj());
                            }else {
                                dynamicAdapter.addAll(dynamicListBean.getObj());
                                if (dynamicListBean.getObj() == null || dynamicListBean.getObj().size() < 10){
                                    mView.getRecyclerView().setNoMore(true);
                                }else {
                                    mView.getRecyclerView().setNoMore(false);
                                }
                            }
                        }else {
                            ToastUtils.showToast(dynamicListBean.getMsg());
                            //加赞失败，点击重新加载
                            mView.getRecyclerView().setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                                @Override
                                public void reload() {
                                    getDynamicList(otherUid);
                                }
                            });
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        //加赞失败，点击重新加载
                        mView.getRecyclerView().setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                getDynamicList(otherUid);
                            }
                        });
                    }
                });
    }

    //显示用户等级
    public void loadLevel(ImageView imglevel, TextView tvLevel, int jifen){
        int levelStr = R.string.初级车手;
        int dengjiImg = 0;
        if(jifen >= 0 && jifen < 80){
            levelStr = R.string.初级车手;
            dengjiImg = R.mipmap.chujicheshou_00;
        }else if(jifen >= 80 && jifen < 240){
            levelStr = R.string.普通车手;
            dengjiImg = R.mipmap.putongcheshou_01;
        }else if(jifen >= 240 && jifen < 480){
            levelStr = R.string.资深车手;
            dengjiImg = R.mipmap.zishencheshou_02;
        }else if(jifen >= 480 && jifen < 800){
            levelStr = R.string.赛车手E级;
            dengjiImg = R.mipmap.saicheshou_e_03;
        }else if(jifen >= 800 && jifen < 1200){
            levelStr = R.string.赛车手D级;
            dengjiImg = R.mipmap.saicheshou_d_04;
        }else if(jifen >= 1200 && jifen < 1680){
            levelStr = R.string.赛车手C级;
            dengjiImg = R.mipmap.saicheshou_c_05;
        }else if(jifen >= 1680 && jifen < 2240){
            levelStr = R.string.赛车手B级;
            dengjiImg = R.mipmap.saicheshou_b_06;
        }else if(jifen >= 2240 && jifen < 2880){
            levelStr = R.string.赛车手A级;
            dengjiImg = R.mipmap.saicheshou_a_07;
        }else if(jifen >= 2880 && jifen < 3600){
            levelStr = R.string.F1赛车手;
            dengjiImg = R.mipmap.saicheshou_f1_08;
        }else if(jifen >= 3600 && jifen < 4400){
            levelStr = R.string.赛车之王;
            dengjiImg = R.mipmap.saichezhiwang_09;
        }else if(jifen >= 4400 && jifen < 5280){
            levelStr = R.string.飞车之王;
            dengjiImg = R.mipmap.feichezhiwang_10;
        }else if(jifen >= 5280 && jifen < 6240){
            levelStr = R.string.超速之王;
            dengjiImg = R.mipmap.chaosuzhiwang_11;
        }else if(jifen >= 6240 && jifen < 7280){
            levelStr = R.string.极速之王;
            dengjiImg = R.mipmap.jisuzhiwang_12;
        }else if(jifen >= 7280 && jifen < 8400){
            levelStr = R.string.光速之王;
            dengjiImg = R.mipmap.guangsuzhiwang_13;
        }else if(jifen >= 8400 && jifen < 9600){
            levelStr = R.string.车王之王;
            dengjiImg = R.mipmap.chewangzhiwang_14;
        }else if(jifen >= 9600 && jifen < 10880){
            levelStr = R.string.新晋车神;
            dengjiImg = R.mipmap.xinjincheshen_15;
        }else if(jifen >= 10880 && jifen < 12240){
            levelStr = R.string.风速车神;
            dengjiImg = R.mipmap.xinjincheshen_16;
        }else if(jifen >= 12240 && jifen < 13680){
            levelStr = R.string.火速车神;
            dengjiImg = R.mipmap.fengsucheshen_17;
        }else if(jifen >= 13680 && jifen < 15200){
            levelStr = R.string.闪电车神;
            dengjiImg = R.mipmap.shandiancheshen_18;
        }else if(jifen >= 15200 && jifen < 18000){
            levelStr = R.string.雷霆车神;
            dengjiImg = R.mipmap.leitingcheshen_19;
        }else if(jifen >= 18000){
            levelStr = R.string.曼威车神;
            dengjiImg = R.mipmap.manweicheshen_20;
        }
        Glide.with(mView.getContext()).load(dengjiImg).into(imglevel);
        tvLevel.setText(levelStr);
    }
}
