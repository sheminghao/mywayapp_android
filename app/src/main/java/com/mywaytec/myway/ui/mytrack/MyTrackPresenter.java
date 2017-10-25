package com.mywaytec.myway.ui.mytrack;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.mywaytec.myway.APP;
import com.mywaytec.myway.DaoSession;
import com.mywaytec.myway.MyTrackDao;
import com.mywaytec.myway.adapter.MyTrackAdapter;
import com.mywaytec.myway.adapter.MycarInfoAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.bean.RoutePathsListBean;
import com.mywaytec.myway.model.db.MyTrack;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.ui.trackResult.TrackResultActivity;
import com.mywaytec.myway.utils.DialogUtils;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.utils.SystemUtil;
import com.mywaytec.myway.view.CommonSubscriber;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/3/8.
 */

public class MyTrackPresenter extends RxPresenter<MytrackView> {

    RetrofitHelper retrofitHelper;
    MyTrackAdapter myTrackAdapter;
    LRecyclerViewAdapter lRecyclerViewAdapter;
    LRecyclerView lRecyclerView;
    MyTrackDao myTrackDao;
    List<MyTrack> list;
    DaoSession daoSession;

    @Inject
    public MyTrackPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(MytrackView view) {
        super.attachView(view);
        lRecyclerView = mView.getRecyclerView();
    }

    public void init(Context context){
        myTrackAdapter = new MyTrackAdapter(context);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(myTrackAdapter);
        lRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        lRecyclerView.setAdapter(lRecyclerViewAdapter);
        lRecyclerView.setPullRefreshEnabled(false);
        lRecyclerView.setLoadMoreEnabled(false);
        myTrackAdapter.setOnItemClickListener(new MycarInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                Long trackkey = myTrackAdapter.getDataList().get(position).getId();
                Intent intent = new Intent(mView.getContext(), TrackResultActivity.class);
                intent.putExtra("save_route_id", myTrackAdapter.getDataList().get(position).getId());
                intent.putExtra("save_route_name", myTrackAdapter.getDataList().get(position).getName());
                float total = myTrackAdapter.getDataList().get(position).getLegend();
                intent.putExtra("total", total);
                mView.getContext().startActivity(intent);
            }
        });

//        daoSession = APP.getInstance().getDaoSession();
//        myTrackDao = daoSession.getMyTrackDao();
//        list = myTrackDao.queryBuilder()
//                .orderDesc(MyTrackDao.Properties.Time).list();
//
//        myTrackAdapter.setDataList(list);

        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.routePathsList(uid, token)
                .compose(RxUtil.<RoutePathsListBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<RoutePathsListBean>() {
                    @Override
                    public void onNext(RoutePathsListBean routePathsListBean) {
                        if (routePathsListBean.getCode() == 1){
                            Log.i("TAG", "------routePathsListBean,"+routePathsListBean.getObj().toString());
                            myTrackAdapter.setDataList(routePathsListBean.getObj());
                        }else if (routePathsListBean.getCode() == 19){
                            DialogUtils.reLoginDialog(mView.getContext());
                        }
                    }
                });

        myTrackAdapter.setOnDelListener(new MycarInfoAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
//                myTrackDao.deleteByKey(list.get(pos).getId());
//                myTrackDao = daoSession.getMyTrackDao();
//                list = myTrackDao.queryBuilder().list();
//                myTrackAdapter.setDataList(list);
//                myTrackAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTop(int pos) {

            }
        });
    }
}
