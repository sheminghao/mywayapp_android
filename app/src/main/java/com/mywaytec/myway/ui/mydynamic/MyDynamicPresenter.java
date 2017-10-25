package com.mywaytec.myway.ui.mydynamic;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.mywaytec.myway.R;
import com.mywaytec.myway.adapter.MyDynamicAdapter;
import com.mywaytec.myway.adapter.WayAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.bean.DynamicListBean;
import com.mywaytec.myway.model.bean.RouteListBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.view.CommonSubscriber;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/3/9.
 */

public class MyDynamicPresenter extends RxPresenter<MyDynamicView> {

    private MyDynamicAdapter myDynamicAdapter;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private RetrofitHelper retrofitHelper;
    private WayAdapter wayAdapter;

    @Inject
    public MyDynamicPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    public void initDynamicList(Context context){
        myDynamicAdapter = new MyDynamicAdapter(context, retrofitHelper);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(myDynamicAdapter);
        mView.getDynamicList().setLayoutManager(new LinearLayoutManager(context));
        mView.getDynamicList().setAdapter(lRecyclerViewAdapter);
        mView.getDynamicList().setPullRefreshEnabled(true);
        mView.getDynamicList().setLoadMoreEnabled(true);
        mView.getDynamicList().setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                dynamicCurrentPage = 1;
                getDynamicData();
            }
        });
        mView.getDynamicList().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                dynamicCurrentPage++;
                getDynamicData();
            }
        });
        getDynamicData();
        myDynamicAdapter.setOnClickLike(new MyDynamicAdapter.OnClickLike() {//点赞
            @Override
            public void clickLick(final TextView tvLike, final ImageView imgLike, final int position) {
                String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
                retrofitHelper.like(uid, myDynamicAdapter.getDataList().get(position).getId()+"")
                        .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                        .subscribe(new CommonSubscriber<BaseInfo>() {
                            @Override
                            public void onNext(BaseInfo baseInfo) {
                                if (baseInfo.getCode() == 308){
                                    int likeNum = Integer.parseInt(tvLike.getText().toString().trim());
                                    myDynamicAdapter.getDataList().get(position).setLikeNum(likeNum+1);
                                    myDynamicAdapter.getDataList().get(position).setIsLike(true);
                                    tvLike.setText(likeNum+1+"");
                                    imgLike.setImageResource(R.mipmap.dianzan_press);
                                }else if (baseInfo.getCode() == 310){
                                    int likeNum = Integer.parseInt(tvLike.getText().toString().trim());
                                    myDynamicAdapter.getDataList().get(position).setLikeNum(likeNum-1);
                                    myDynamicAdapter.getDataList().get(position).setIsLike(false);
                                    tvLike.setText(likeNum-1+"");
                                    imgLike.setImageResource(R.mipmap.dianzan);
                                }
                            }
                        });
            }
        });
    }

    int dynamicCurrentPage = 1;

    public void getDynamicData(){
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.getMyDynamicList(uid, token, dynamicCurrentPage)
                .compose(RxUtil.<DynamicListBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DynamicListBean>(mView) {
                    @Override
                    public void onNext(DynamicListBean dynamicListBean) {
                        if (dynamicListBean.getCode() == 322) {
                            Log.i("TAG", "查询成功");
                            if (dynamicCurrentPage == 1) {
                                myDynamicAdapter.setDataList(dynamicListBean.getObj());
                                mView.getDynamicList().refreshComplete(0);
                            }else {
                                myDynamicAdapter.addAll(dynamicListBean.getObj());
                                if (dynamicListBean.getObj().size() < 10 || dynamicListBean.getObj() == null){
                                    mView.getDynamicList().setNoMore(true);
                                }else {
                                    mView.getDynamicList().setNoMore(false);
                                }
                            }
//                            DynamicData.saveDynamicData(dynamicAdapter.getDataList());
                        } else if (dynamicListBean.getCode() == 233) {
                            ToastUtils.showToast(R.string.用户验证失败);
                        } else {
                            ToastUtils.showToast(dynamicListBean.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.getDynamicList().refreshComplete(0);
                        //加赞失败，点击重新加载
                        mView.getDynamicList().setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                getDynamicData();
                            }
                        });
                    }
                });
    }

    public void initWayList(Context context){
        wayAdapter = new WayAdapter(context);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(wayAdapter);
        mView.getWayList().setLayoutManager(new LinearLayoutManager(context));
        mView.getWayList().setAdapter(lRecyclerViewAdapter);
        mView.getWayList().setPullRefreshEnabled(true);
        mView.getWayList().setLoadMoreEnabled(true);
        mView.getWayList().setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                wayCurrentPage = 1;
                getWayData();
            }
        });
        mView.getWayList().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                wayCurrentPage++;
                getWayData();
            }
        });
        getWayData();
    }

    int wayCurrentPage = 1;

    public void getWayData(){
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.myRoute(uid, token, wayCurrentPage)
                .compose(RxUtil.<RouteListBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<RouteListBean>(mView) {
                    @Override
                    public void onNext(RouteListBean routeListBean) {
                        if (routeListBean.getCode() == 1) {
                            Log.i("TAG", "查询成功");
                            if (wayCurrentPage == 1) {
                                wayAdapter.setDataList(routeListBean.getObj());
                                mView.getWayList().refreshComplete(0);
                            }else {
                                wayAdapter.addAll(routeListBean.getObj());
                                if (routeListBean.getObj().size() < 10 || routeListBean.getObj() == null){
                                    mView.getDynamicList().setNoMore(true);
                                }else {
                                    mView.getDynamicList().setNoMore(false);
                                }
                            }
                        } else {
                            ToastUtils.showToast(routeListBean.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.getWayList().refreshComplete(0);
                        //加赞失败，点击重新加载
                        mView.getWayList().setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                getWayData();
                            }
                        });
                    }
                });
    }
}
