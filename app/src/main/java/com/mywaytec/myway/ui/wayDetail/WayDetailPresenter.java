package com.mywaytec.myway.ui.wayDetail;

import android.util.Log;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.view.CommonSubscriber;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/3/8.
 */

public class WayDetailPresenter extends RxPresenter<WayDetailView> {

    RetrofitHelper retrofitHelper;

    @Inject
    public WayDetailPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    public RetrofitHelper getRetrofitHelper(){
        return retrofitHelper;
    }

    public void like(String routeId){
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        retrofitHelper.routeLike(uid, routeId)
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseInfo>() {
                    @Override
                    public void onNext(BaseInfo baseInfo) {
                        Log.i("TAG", "------路线点赞:"+baseInfo.getMsg()+baseInfo.getCode());
                        if (baseInfo.getCode() == 405){//取消点赞成功
                            mView.getLikeImg().setImageResource(R.mipmap.luxianxiangqing_dianzang);
                        }else if(baseInfo.getCode() == 402){//点赞成功
                            mView.getLikeImg().setImageResource(R.mipmap.luxianxiangqing_dianzan_select);
                        }
                    }
                });
    }
}
