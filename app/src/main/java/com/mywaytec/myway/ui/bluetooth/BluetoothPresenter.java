package com.mywaytec.myway.ui.bluetooth;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.mywaytec.myway.APP;
import com.mywaytec.myway.ConnectedBleInfoDao;
import com.mywaytec.myway.DaoSession;
import com.mywaytec.myway.adapter.MycarInfoAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.db.ConnectedBleInfo;
import com.mywaytec.myway.utils.BleUtil;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/4/11.
 */

public class BluetoothPresenter extends RxPresenter<BluetoothView> {

    MycarInfoAdapter mycarInfoAdapter;
    LRecyclerViewAdapter mLRecyclerViewAdapter;

    @Inject
    public BluetoothPresenter() {
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(BluetoothView view) {
        super.attachView(view);
        initMycarRecyclerView();
    }

    DaoSession daoSession;
    List<ConnectedBleInfo> list;
    ConnectedBleInfoDao connectedBleInfoDao;
    public void initMycarRecyclerView() {
        daoSession = APP.getInstance().getDaoSession();
        connectedBleInfoDao = daoSession.getConnectedBleInfoDao();
        list = connectedBleInfoDao.queryBuilder().orderDesc(ConnectedBleInfoDao.Properties.Time).list();
        if (list.size() > 3){
            mView.getLayoutMoreCarInfo().setVisibility(View.VISIBLE);
        }else {
            mView.getLayoutMoreCarInfo().setVisibility(View.GONE);
        }
        if (list.size() > 0){
            mView.getLayoutMycarHave().setVisibility(View.VISIBLE);
            mView.getLayoutMycarNo().setVisibility(View.GONE);
        }else {
            mView.getLayoutMycarHave().setVisibility(View.GONE);
            mView.getLayoutMycarNo().setVisibility(View.VISIBLE);
        }
        mycarInfoAdapter = new MycarInfoAdapter(mView.getContext(), 3);
//        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mycarInfoAdapter);
        mView.getMycarRecyclerView().setLayoutManager(new LinearLayoutManager(mView.getContext()));
//        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mycarInfoAdapter);
        mView.getMycarRecyclerView().setAdapter(mycarInfoAdapter);
        mycarInfoAdapter.setDataList(list);
        mycarInfoAdapter.setOnItemClickListener(new MycarInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                BleUtil.setBleAddress(mycarInfoAdapter.getDataList().get(position).getAddress());
                BleUtil.setBleName(mycarInfoAdapter.getDataList().get(position).getBleName());
                ((Activity)mView.getContext()).finish();
            }
        });
        mycarInfoAdapter.setOnDelListener(new MycarInfoAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                if (mycarInfoAdapter.getDataList().size() > 0) {
                    connectedBleInfoDao.deleteByKey(list.get(pos).getId());
                    mycarInfoAdapter.getDataList().remove(pos);
                    mycarInfoAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTop(int pos) {

            }
        });
    }
}
