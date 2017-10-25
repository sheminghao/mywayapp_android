package com.mywaytec.myway.fragment.way;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.mywaytec.myway.adapter.WayAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.bean.RouteListBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.ui.wayDetail.WayDetailActivity;
import com.mywaytec.myway.utils.DialogUtils;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.data.RouteData;
import com.mywaytec.myway.view.CommonSubscriber;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/3/9.
 */

public class WayPresenter extends RxPresenter<WayView> {

    private WayAdapter wayAdapter;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private RetrofitHelper retrofitHelper;

    @Inject
    public WayPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    public void init(final Context context){
        wayAdapter = new WayAdapter(context);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(wayAdapter);
        mView.getRecyclerView().setLayoutManager(new LinearLayoutManager(context));
        mView.getRecyclerView().setAdapter(lRecyclerViewAdapter);
        mView.getRecyclerView().setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                getData();
            }
        });
        mView.getRecyclerView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                currentPage++;
                getData();
            }
        });
        getData();
        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context, WayDetailActivity.class);
                intent.putExtra(WayDetailActivity.ROUTE_DATA, wayAdapter.getDataList().get(position));
                context.startActivity(intent);
            }
        });
    }

    int currentPage = 1;

    public void getData(){
        //获取缓存数据，先显示缓存数据
        RouteListBean routeListBean = RouteData.getRouteData();
        if (null != routeListBean && null!= routeListBean.getObj())
            wayAdapter.setDataList(routeListBean.getObj());
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.getRouteList(uid, token, currentPage)
                .compose(RxUtil.<RouteListBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<RouteListBean>(mView) {
                    @Override
                    public void onNext(RouteListBean routeListBean) {
                        mView.getRecyclerView().refreshComplete(0);
                        if (routeListBean.getCode() == 1) {
                            Log.i("TAG", "--------查询成功");
                            Log.i("TAG", "--------页数"+currentPage);
                            if (currentPage == 1) {
                                wayAdapter.setDataList(routeListBean.getObj());
                            }else {
                                wayAdapter.addAll(routeListBean.getObj());
                                if (routeListBean.getObj() == null|| routeListBean.getObj().size() < 10){
                                    mView.getRecyclerView().setNoMore(true);
                                }else {
                                    mView.getRecyclerView().setNoMore(false);
                                }
                            }
                            //保存缓存数据
                            RouteData.saveRouteData(wayAdapter.getDataList());
                        } else if (routeListBean.getCode() == 19){
                            DialogUtils.reLoginDialog(mView.getContext());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        //加赞失败，点击重新加载
                        mView.getRecyclerView().setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                getData();
                            }
                        });
                    }
                });
    }
}
