package com.mywaytec.myway.ui.personalCenter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mywaytec.myway.R;
import com.mywaytec.myway.adapter.AttentionAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.bean.MyAttentionBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.DialogUtils;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.view.CommonSubscriber;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/12/14.
 */

public class PersonalCenterPresenter extends RxPresenter<PersonalCenterView> {

    RetrofitHelper retrofitHelper;
    private Context mContext;
    private RecyclerView guanzhuRecyclerView;
    private AttentionAdapter attentionAdapter;

    @Inject
    public PersonalCenterPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
    }

    @Override
    public void attachView(PersonalCenterView view) {
        super.attachView(view);
        mContext = mView.getContext();
        guanzhuRecyclerView = mView.getGuanzhuRecyclerView();
        guanzhuRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        attentionList();
    }

    //关注列表
    public void attentionList(){
        attentionAdapter = new AttentionAdapter(mContext, 1);
        guanzhuRecyclerView.setAdapter(attentionAdapter);
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.myAttention(uid, token)
                .compose(RxUtil.<MyAttentionBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<MyAttentionBean>() {
                    @Override
                    public void onNext(MyAttentionBean myAttentionBean) {
                        if (myAttentionBean.getCode() == 1){
                            attentionAdapter.setDataList(myAttentionBean.getObj());
                        }else if (myAttentionBean.getCode() == 19){
                            DialogUtils.reLoginDialog(mContext);
                        }
                    }
                });

    }

    //粉丝列表
    public void fansList(){
        attentionAdapter = new AttentionAdapter(mContext, 2);
        guanzhuRecyclerView.setAdapter(attentionAdapter);
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.myFans(uid, token)
                .compose(RxUtil.<MyAttentionBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<MyAttentionBean>() {
                    @Override
                    public void onNext(MyAttentionBean myAttentionBean) {
                        if (myAttentionBean.getCode() == 1){
                            attentionAdapter.setDataList(myAttentionBean.getObj());
                        }else if (myAttentionBean.getCode() == 19){
                            DialogUtils.reLoginDialog(mContext);
                        }
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
