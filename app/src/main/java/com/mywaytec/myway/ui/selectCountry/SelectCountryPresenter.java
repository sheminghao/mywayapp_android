package com.mywaytec.myway.ui.selectCountry;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;

import com.mywaytec.myway.adapter.SelectCountryAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.bean.CountryBean;
import com.mywaytec.myway.utils.indexlib.DividerItemDecoration;
import com.mywaytec.myway.utils.indexlib.suspension.SuspensionDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import cn.smssdk.SMSSDK;

/**
 * Created by shemh on 2017/7/8.
 */

public class SelectCountryPresenter extends RxPresenter<SelectCountryView> {

    private Context mContext;
    SelectCountryAdapter selectCountryAdapter;
    LinearLayoutManager mManager;
    private SuspensionDecoration mDecoration;
    // 国家号码规则
    private List<CountryBean> countryRules = new ArrayList<>();

    @Inject
    public SelectCountryPresenter() {
        registerEvent();
    }

    void registerEvent() {
    }

    @Override
    public void attachView(SelectCountryView view) {
        super.attachView(view);
        mContext = mView.getContext();
    }

    public void init(){
        HashMap<Character, ArrayList<String[]>> rawData = SMSSDK.getGroupedCountryList();
        Log.i("TAG", "-------selectCountry,"+rawData.size());
        for (Map.Entry<Character, ArrayList<String[]>> ent : rawData.entrySet()) {
            ArrayList<String[]> cl = ent.getValue();
            Log.i("TAG", "-------cl,"+cl.size());
            for (String[] paire : cl) {
                if (paire.length > 1){
                    CountryBean countryBean = new CountryBean();
                    countryBean.setCountry(paire[0]);
                    countryBean.setCode(paire[1]);
                    countryRules.add(countryBean);
                }
            }
        }
        selectCountryAdapter = new SelectCountryAdapter(mContext);
        mManager = new LinearLayoutManager(mContext);
        mView.getRecyclerView().setLayoutManager(mManager);
        mView.getRecyclerView().setAdapter(selectCountryAdapter);

        mView.getRecyclerView().addItemDecoration(mDecoration = new SuspensionDecoration(mContext, countryRules));
        //如果add两个，那么按照先后顺序，依次渲染。
        mView.getRecyclerView().addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));

        //indexbar初始化
        mView.getIndexBar().setmPressedShowTextView(mView.getSideBarHintTV())//设置HintTextView
                .setNeedRealIndex(false)//设置需要真实的索引
                .setmLayoutManager(mManager);//设置RecyclerView的LayoutManager
        mView.getIndexBar().getDataHelper().sortSourceDatas(countryRules);
        selectCountryAdapter.setDataList(countryRules);
        mView.getIndexBar().setmSourceDatas(countryRules)//设置数据
                .invalidate();
        mDecoration.setmDatas(countryRules);
    }

}