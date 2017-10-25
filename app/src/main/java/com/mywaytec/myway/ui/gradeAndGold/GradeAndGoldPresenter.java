package com.mywaytec.myway.ui.gradeAndGold;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.WindowManager;

import com.mywaytec.myway.R;
import com.mywaytec.myway.adapter.AccessAdapter;
import com.mywaytec.myway.adapter.LevelAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.bean.GoldInfoBean;
import com.mywaytec.myway.model.bean.VersionBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.ui.about.AboutActivity;
import com.mywaytec.myway.ui.about.AboutView;
import com.mywaytec.myway.utils.DialogUtils;
import com.mywaytec.myway.utils.DownloadFileUtil;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CommonSubscriber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/3/8.
 */

public class GradeAndGoldPresenter extends RxPresenter<GradeAndGoldView> {

    RetrofitHelper retrofitHelper;
    Context mContext;
    AccessAdapter accessAdapter;
    LevelAdapter levelAdapter;

    @Inject
    public GradeAndGoldPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(GradeAndGoldView view) {
        super.attachView(view);
        mContext = mView.getContext();
    }

    public void initRecyclerView(){
        accessAdapter = new AccessAdapter(mContext);
        mView.getAccessRecyclerView().setLayoutManager(new LinearLayoutManager(mContext));
        mView.getAccessRecyclerView().setAdapter(accessAdapter);
        //获取增值攻略数据
        List<String> accessList = Arrays.asList(mContext.getResources().getStringArray(R.array.access_data));
        accessAdapter.setDataList(accessList);

        levelAdapter = new LevelAdapter(mContext);
        mView.getLevelRecyclerView().setLayoutManager(new LinearLayoutManager(mContext));
        mView.getLevelRecyclerView().setAdapter(levelAdapter);
        //获取增值攻略数据
        List<String> levelList = Arrays.asList(mContext.getResources().getStringArray(R.array.level_data));
        levelAdapter.setDataList(levelList);
    }

    //查询积分金币
    public void queryIntegralGold(){
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        retrofitHelper.queryIntegralGold(uid)
                .compose(RxUtil.<GoldInfoBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<GoldInfoBean>() {
                    @Override
                    public void onNext(GoldInfoBean goldInfoBean) {
                        if (goldInfoBean.getCode() == 1){
                            mView.getGoldTV().setText(goldInfoBean.getObj().getGold()+"");
                            loadLevel(goldInfoBean.getObj().getIntegral());
                        }
                    }
                });
    }

    public void loadLevel(int jifen){
        int levelStr = R.string.初级车手;
        int shengjijifen = 0;
        if(jifen >= 0 && jifen < 80){
            levelStr = R.string.初级车手;
            shengjijifen = 80;
        }else if(jifen >= 80 && jifen < 240){
            levelStr = R.string.普通车手;
            shengjijifen = 240;
        }else if(jifen >= 240 && jifen < 480){
            levelStr = R.string.资深车手;
            shengjijifen = 480;
        }else if(jifen >= 480 && jifen < 800){
            levelStr = R.string.赛车手E级;
            shengjijifen = 800;
        }else if(jifen >= 800 && jifen < 1200){
            levelStr = R.string.赛车手D级;
            shengjijifen = 1200;
        }else if(jifen >= 1200 && jifen < 1680){
            levelStr = R.string.赛车手C级;
            shengjijifen = 1680;
        }else if(jifen >= 1680 && jifen < 2240){
            levelStr = R.string.赛车手B级;
            shengjijifen = 2240;
        }else if(jifen >= 2240 && jifen < 2880){
            levelStr = R.string.赛车手A级;
            shengjijifen = 2880;
        }else if(jifen >= 2880 && jifen < 3600){
            levelStr = R.string.F1赛车手;
            shengjijifen = 3600;
        }else if(jifen >= 3600 && jifen < 4400){
            levelStr = R.string.赛车之王;
            shengjijifen = 4400;
        }else if(jifen >= 4400 && jifen < 5280){
            levelStr = R.string.飞车之王;
            shengjijifen = 5280;
        }else if(jifen >= 5280 && jifen < 6240){
            levelStr = R.string.超速之王;
            shengjijifen = 6240;
        }else if(jifen >= 6240 && jifen < 7280){
            levelStr = R.string.极速之王;
            shengjijifen = 7280;
        }else if(jifen >= 7280 && jifen < 8400){
            levelStr = R.string.光速之王;
            shengjijifen = 8400;
        }else if(jifen >= 8400 && jifen < 9600){
            levelStr = R.string.车王之王;
            shengjijifen = 9600;
        }else if(jifen >= 9600 && jifen < 10880){
            levelStr = R.string.新晋车神;
            shengjijifen = 10880;
        }else if(jifen >= 10880 && jifen < 12240){
            levelStr = R.string.风速车神;
            shengjijifen = 12240;
        }else if(jifen >= 12240 && jifen < 13680){
            levelStr = R.string.火速车神;
            shengjijifen = 13680;
        }else if(jifen >= 13680 && jifen < 15200){
            levelStr = R.string.闪电车神;
            shengjijifen = 15200;
        }else if(jifen >= 15200 && jifen < 18000){
            levelStr = R.string.雷霆车神;
            shengjijifen = 18000;
        }else if(jifen >= 18000){
            levelStr = R.string.曼威车神;
            shengjijifen = jifen;
        }
        mView.getDriverLevelTV().setText(levelStr);
        mView.getJinfenTV().setText(mContext.getResources().getString(R.string.scores)+"（"+ jifen + "/" + shengjijifen +"）");
        mView.getProgressBar().setMax(shengjijifen);
        mView.getProgressBar().setProgress(jifen);
    }
}
