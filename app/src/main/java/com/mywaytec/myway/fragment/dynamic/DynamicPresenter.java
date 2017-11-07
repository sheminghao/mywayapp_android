package com.mywaytec.myway.fragment.dynamic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.mywaytec.myway.R;
import com.mywaytec.myway.adapter.DynamicAdapter;
import com.mywaytec.myway.adapter.WayAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.bean.DynamicListBean;
import com.mywaytec.myway.model.bean.PraiseBean;
import com.mywaytec.myway.model.bean.RouteListBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.ui.wayDetail.WayDetailActivity;
import com.mywaytec.myway.utils.DialogUtils;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.SystemUtil;
import com.mywaytec.myway.utils.ToastUtils;
import com.mywaytec.myway.utils.data.DynamicData;
import com.mywaytec.myway.utils.data.RouteData;
import com.mywaytec.myway.view.CommonSubscriber;
import com.mywaytec.myway.view.goodview.GoodView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/3/9.
 */

public class DynamicPresenter extends RxPresenter<DynamicView> {

    private DynamicAdapter dynamicAdapter;
    private LRecyclerViewAdapter dynamicLRecyclerViewAdapter;
    private LRecyclerViewAdapter wayLRecyclerViewAdapter;
    private RetrofitHelper retrofitHelper;
    GoodView goodView;

    @Inject
    public DynamicPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    public void initDynamicList(final Context context){
        goodView = new GoodView(context);
        dynamicAdapter = new DynamicAdapter(context, retrofitHelper);
        dynamicLRecyclerViewAdapter = new LRecyclerViewAdapter(dynamicAdapter);
        mView.getDynamicList().setLayoutManager(new LinearLayoutManager(context));
        mView.getDynamicList().setAdapter(dynamicLRecyclerViewAdapter);
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
        dynamicAdapter.setOnClickLike(new DynamicAdapter.OnClickLike() {//点赞
            @Override
            public void clickLick(final TextView tvLike, final ImageView imgLike, final int position) {
                String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
                retrofitHelper.like(uid, dynamicAdapter.getDataList().get(position).getId()+"")
                        .compose(RxUtil.<PraiseBean>rxSchedulerHelper())
                        .subscribe(new CommonSubscriber<PraiseBean>() {
                            @Override
                            public void onNext(PraiseBean praiseBean) {
//                                Log.i("TAG", "------点赞, "+praiseBean.getCode());
                                if (praiseBean.getCode() == 308){
//                                    ToastUtils.showToast("点赞成功");
//                                    Log.i("TAG", "------点赞成功");
                                    int likeNum = Integer.parseInt(tvLike.getText().toString().trim());
                                    dynamicAdapter.getDataList().get(position).setLikeNum(likeNum+1);
                                    dynamicAdapter.getDataList().get(position).setIsLike(true);
//                                    refreshItem(dynamicAdapter.getDataList().get(position), position);
                                    DynamicData.saveDynamicData(dynamicAdapter.getDataList());
                                    goodView.setText("+1");
                                    goodView.setTextInfo("+1", Color.parseColor("#2984DF"), SystemUtil.dp2px(10));
                                    goodView.show(imgLike);
                                    imgLike.setImageResource(R.mipmap.dianzan_press);
                                    tvLike.setText(dynamicAdapter.getDataList().get(position).getLikeNum() + "");
                                    if (null != praiseBean.getObj()){
//                                        ToastUtils.showToast("获得1金币，1积分");
                                    }else {

                                    }
                                }else if (praiseBean.getCode() == 310){//取消点赞成功
//                                    Log.i("TAG", "------取消点赞成功");
                                    int likeNum = Integer.parseInt(tvLike.getText().toString().trim());
                                    dynamicAdapter.getDataList().get(position).setLikeNum(likeNum-1);
                                    dynamicAdapter.getDataList().get(position).setIsLike(false);
//                                    refreshItem(dynamicAdapter.getDataList().get(position), position);
                                    DynamicData.saveDynamicData(dynamicAdapter.getDataList());
                                    imgLike.setImageResource(R.mipmap.dianzan);
                                    tvLike.setText(dynamicAdapter.getDataList().get(position).getLikeNum()+"");
                                }else if (praiseBean.getCode() == 19){
                                    DialogUtils.reLoginDialog(context);
                                }
                            }
                        });
            }
        });
    }

    int dynamicCurrentPage = 1;

    public void getDynamicData(){
        DynamicListBean dynamicList = DynamicData.getDynamicData();
        if (null != dynamicList && null!= dynamicList.getObj())
        dynamicAdapter.setDataList(dynamicList.getObj());
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.getDynamicList(uid, token, dynamicCurrentPage)
                .compose(RxUtil.<DynamicListBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DynamicListBean>(mView) {
                    @Override
                    public void onNext(DynamicListBean dynamicListBean) {
                        mView.getDynamicList().refreshComplete(0);
                        if (dynamicListBean.getCode() == 322) {
                            Log.i("TAG", "查询成功");
                            if (dynamicCurrentPage == 1) {
                                dynamicAdapter.setDataList(dynamicListBean.getObj());
                            }else {
                                dynamicAdapter.addAll(dynamicListBean.getObj());
                                if (dynamicListBean.getObj() == null || dynamicListBean.getObj().size() < 10){
                                    mView.getDynamicList().setNoMore(true);
                                }else {
                                    mView.getDynamicList().setNoMore(false);
                                }
                            }
                            DynamicData.saveDynamicData(dynamicAdapter.getDataList());
                        } else if (dynamicListBean.getCode() == 233) {
                            DialogUtils.reLoginDialog(mView.getContext());
                        } else {
//                            ToastUtils.showToast(dynamicListBean.getMsg());
                            //加赞失败，点击重新加载
                            mView.getDynamicList().setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                                @Override
                                public void reload() {
                                    getDynamicData();
                                }
                            });
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

    //刷新单个item
    public void refreshItem(DynamicListBean.ObjBean dynamic, int position){
        Log.i("TAG", "-------DynamicPresenter刷新"+position + dynamic.getLikeNum());
        List<DynamicListBean.ObjBean> objBeanList = dynamicAdapter.getDataList();
        objBeanList.set(position, dynamic);
        dynamicLRecyclerViewAdapter.notifyDataSetChanged();
    }

    private WayAdapter wayAdapter;
    public void initWayList(final Context context){
        wayAdapter = new WayAdapter(context);
        wayLRecyclerViewAdapter = new LRecyclerViewAdapter(wayAdapter);
        mView.getWayList().setLayoutManager(new LinearLayoutManager(context));
        mView.getWayList().setAdapter(wayLRecyclerViewAdapter);
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
        wayLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context, WayDetailActivity.class);
                intent.putExtra(WayDetailActivity.ROUTE_DATA, wayAdapter.getDataList().get(position));
                context.startActivity(intent);
            }
        });
    }

    int wayCurrentPage = 1;

    public void getWayData(){
        //获取缓存数据，先显示缓存数据
        RouteListBean routeListBean = RouteData.getRouteData();
        if (null != routeListBean && null!= routeListBean.getObj())
            wayAdapter.setDataList(routeListBean.getObj());
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.getRouteList(uid, token, wayCurrentPage)
                .compose(RxUtil.<RouteListBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<RouteListBean>(mView) {
                    @Override
                    public void onNext(RouteListBean routeListBean) {
                        mView.getWayList().refreshComplete(0);
                        if (routeListBean.getCode() == 1) {
                            if (wayCurrentPage == 1) {
                                wayAdapter.setDataList(routeListBean.getObj());
                            }else {
                                wayAdapter.addAll(routeListBean.getObj());
                                if (routeListBean.getObj() == null|| routeListBean.getObj().size() < 10){
                                    mView.getWayList().setNoMore(true);
                                }else {
                                    mView.getWayList().setNoMore(false);
                                }
                            }
                            //保存缓存数据
                            RouteData.saveRouteData(wayAdapter.getDataList());
                        } else {
//                            ToastUtils.showToast(routeListBean.getMsg());
                            //加赞失败，点击重新加载
                            mView.getWayList().setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                                @Override
                                public void reload() {
                                    getWayData();
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.getDynamicList().refreshComplete(0);
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
