package com.mywaytec.myway.ui.lookoverAllCar;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.mywaytec.myway.APP;
import com.mywaytec.myway.ConnectedBleInfoDao;
import com.mywaytec.myway.DaoSession;
import com.mywaytec.myway.R;
import com.mywaytec.myway.adapter.MycarInfoAdapter;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.model.db.ConnectedBleInfo;
import com.mywaytec.myway.utils.BleUtil;
import com.mywaytec.myway.utils.TimeUtil;

import java.util.List;

import butterknife.BindView;

public class LookoverAllCarActivity extends BaseActivity<LookoverAllCarPresenter> implements LookoverAllCarView{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    MycarInfoAdapter mycarInfoAdapter;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_lookover_all_car;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.all_vehicle);
        initRecyclerView();
    }

    @Override
    protected void updateViews() {

    }

    ConnectedBleInfoDao connectedBleInfoDao;
    List<ConnectedBleInfo> list;
    private void initRecyclerView(){
        DaoSession daoSession = APP.getInstance().getDaoSession();
        connectedBleInfoDao = daoSession.getConnectedBleInfoDao();
        list = connectedBleInfoDao.queryBuilder().
                orderDesc(ConnectedBleInfoDao.Properties.Time).list();
        mycarInfoAdapter = new MycarInfoAdapter(this, -1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mycarInfoAdapter);
        mycarInfoAdapter.setDataList(list);
        mycarInfoAdapter.setOnItemClickListener(new MycarInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                list.get(position).setTime(TimeUtil.getYMDHMSTime());
                connectedBleInfoDao.update(list.get(position));
                BleUtil.setBleAddress(mycarInfoAdapter.getDataList().get(position).getAddress());
                BleUtil.setBleName(mycarInfoAdapter.getDataList().get(position).getBleName());
                finish();
            }
        });
        mycarInfoAdapter.setOnDelListener(new MycarInfoAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                connectedBleInfoDao.deleteByKey(list.get(pos).getId());
                mycarInfoAdapter.getDataList().remove(pos);
                mycarInfoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTop(int pos) {

            }
        });
    }

    @Override
    public Context getContext() {
        return this;
    }
}
