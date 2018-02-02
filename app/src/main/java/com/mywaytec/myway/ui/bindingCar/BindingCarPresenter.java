package com.mywaytec.myway.ui.bindingCar;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.mywaytec.myway.R;
import com.mywaytec.myway.adapter.BindingCarAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.bean.AllBindingCarBean;
import com.mywaytec.myway.model.bean.BleInfoBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.DialogUtils;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.utils.data.BleInfo;
import com.mywaytec.myway.view.CommonSubscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/3/8.
 */

public class BindingCarPresenter extends RxPresenter<BindingCarView> {

    RetrofitHelper retrofitHelper;
    Context mContext;
    BindingCarAdapter bindingCarAdapter;

    @Inject
    public BindingCarPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(BindingCarView view) {
        super.attachView(view);
        mContext = mView.getContext();
    }

    //
    public void initRecycler(){
        bindingCarAdapter = new BindingCarAdapter(mContext);
        mView.getRecyclerView().setLayoutManager(new LinearLayoutManager(mContext));
        mView.getRecyclerView().setAdapter(bindingCarAdapter);

        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.allBindingCar(uid, token)
                .compose(RxUtil.<AllBindingCarBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<AllBindingCarBean>() {
                    @Override
                    public void onNext(AllBindingCarBean allBindingCarBean) {
                        if (allBindingCarBean.getCode() == 1){
                            bindingCarAdapter.setDataList(allBindingCarBean.getObj());
                            List<AllBindingCarBean.ObjBean> list = new ArrayList<>();
//                            AllBindingCarBean.ObjBean objBean = new AllBindingCarBean.ObjBean();
//                            objBean.setName("测试");
//                            objBean.setSnCode("3F70343313059F464D559647");
//                            list.add(objBean);
                            bindingCarAdapter.addAll(list);
                        }else if (allBindingCarBean.getCode() == -2){
                            DialogUtils.reLoginDialog(mView.getContext());
                        }
                    }
                });
    }

    //绑定车辆
    public void bindingCar(String snCode){
        if (TextUtils.isEmpty(snCode)){
            ToastUtils.showToast(R.string.please_enter_the_sn_code);
            return;
        }
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.bindingCar(uid, token, snCode)
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<BaseInfo>() {
                @Override
                public void onNext(BaseInfo baseInfo) {
                    //TODO 中英文
                    if (baseInfo.getCode() == 1){
                        ToastUtils.showToast(R.string.binding_success);
                        //绑定新的车辆后，清空车辆绑定信息
                        BleInfoBean bleInfoBean = BleInfo.getBleInfo();
                        bleInfoBean.setIsChezhu("");
                        BleInfo.saveBleInfo(bleInfoBean);
                        initRecycler();
                    }else if (baseInfo.getCode() == -1){
                        ToastUtils.showToast(R.string.binding_failure);
                    }else if (baseInfo.getCode() == -3){
                        ToastUtils.showToast(R.string.not_repeat_the_binding);
                    }else if (baseInfo.getCode() == -4){
                        ToastUtils.showToast(R.string.the_vehicle_has_been_bound);
                    }else if (baseInfo.getCode() == -2){//用户校验失败
                        DialogUtils.reLoginDialog(mContext);
                    }
                }
        });
    }
}
