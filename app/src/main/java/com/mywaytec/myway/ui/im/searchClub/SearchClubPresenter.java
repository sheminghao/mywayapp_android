package com.mywaytec.myway.ui.im.searchClub;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.mywaytec.myway.adapter.SearchClubAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.bean.SearchClubBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.DialogUtils;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.view.CommonSubscriber;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/11/30.
 */

public class SearchClubPresenter extends RxPresenter<SearchClubView>{

    private RetrofitHelper retrofitHelper;
    private SearchClubAdapter searchClubAdapter;

    private Context mContext;

    @Inject
    public SearchClubPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
    }

    @Override
    public void attachView(SearchClubView view) {
        super.attachView(view);
        mContext = mView.getContext();
    }

    public void initRecyclerView(){
        searchClubAdapter = new SearchClubAdapter(mContext);
        mView.getRecyclerView().setLayoutManager(new LinearLayoutManager(mContext));
        mView.getRecyclerView().setAdapter(searchClubAdapter);
        searchClub("");
    }

    //搜索俱乐部
    public void searchClub(String name){
        if (TextUtils.isEmpty(name)){
            mView.getTuijianTV().setVisibility(View.VISIBLE);
        }else {
            mView.getTuijianTV().setVisibility(View.GONE);
        }
        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.searchClubList(uid, token, name)
                .compose(RxUtil.<SearchClubBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<SearchClubBean>() {
                    @Override
                    public void onNext(SearchClubBean searchClubBean) {
                        if (searchClubBean.getCode() == 1){
                            searchClubAdapter.setDataList(searchClubBean.getObj());
                        }else if(searchClubBean.getCode() == 19){
                            DialogUtils.reLoginDialog(mContext);
                        }
                    }
                });
    }


}
