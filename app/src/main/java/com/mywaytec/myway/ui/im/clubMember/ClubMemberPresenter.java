package com.mywaytec.myway.ui.im.clubMember;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.adapter.ClubMemberAdapter;
import com.mywaytec.myway.base.RxPresenter;
import com.mywaytec.myway.model.bean.ClubDetailBean;
import com.mywaytec.myway.model.bean.DeleteClubUsersBean;
import com.mywaytec.myway.model.http.RetrofitHelper;
import com.mywaytec.myway.utils.PreferencesUtils;
import com.mywaytec.myway.utils.RxUtil;
import com.mywaytec.myway.view.CommonSubscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by shemh on 2017/12/7.
 */

public class ClubMemberPresenter extends RxPresenter<ClubMemberView> {

    private Context mContext;
    private RetrofitHelper retrofitHelper;
    private ClubMemberAdapter clubMemberAdapter;

    @Inject
    public ClubMemberPresenter(RetrofitHelper retrofitHelper) {
        this.retrofitHelper = retrofitHelper;
    }

    @Override
    public void attachView(ClubMemberView view) {
        super.attachView(view);
        mContext = mView.getContext();
    }

    /**
     * 设置列表是否可编辑
     * @param isEdit
     */
    public void setEdit(boolean isEdit){
        if (null != clubMemberAdapter){
            clubMemberAdapter.setEdit(isEdit);
        }
    }

    public void initData(int gid){
        clubMemberAdapter = new ClubMemberAdapter(mContext);
        mView.getMemberRecyclerView().setLayoutManager(new LinearLayoutManager(mContext));
        mView.getMemberRecyclerView().setAdapter(clubMemberAdapter);
        clubMemberAdapter.setOnSelectCountListener(new ClubMemberAdapter.OnSelectCountListener() {
            @Override
            public void selectCount(int num) {
                mView.getDeleteTV().setText(mContext.getResources().getString(R.string.delete) + "(" + num + ")");
            }
        });

        final String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.rongClubDetail(uid, token, gid)
                .compose(RxUtil.<ClubDetailBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ClubDetailBean>(mView, mContext, true) {
                    @Override
                    public void onNext(ClubDetailBean clubDetailBean) {
                        if (clubDetailBean.getCode() == 1) {
                            if (null != clubDetailBean.getObj().getUsers()){
                                List<ClubDetailBean.ObjBean.UsersBean> usersList = new ArrayList<>();
                                for (int i = 0; i < clubDetailBean.getObj().getUsers().size(); i++) {
                                    if (clubDetailBean.getObj().getUsers().get(i).getGlobalState() == 2) {//创建者/群主
                                        usersList.add(0, clubDetailBean.getObj().getUsers().get(i));
                                        if (uid.equals(clubDetailBean.getObj().getUsers().get(i).getUid())){
                                            mView.getDeleteTV().setVisibility(View.VISIBLE);
                                        }
                                    }else if (clubDetailBean.getObj().getUsers().get(i).getGlobalState() == 1){//管理者
                                        usersList.add(1, clubDetailBean.getObj().getUsers().get(i));
                                    }else if (clubDetailBean.getObj().getUsers().get(i).getGlobalState() == 0){//成员
                                        usersList.add(clubDetailBean.getObj().getUsers().get(i));
                                    }
                                }

                                clubMemberAdapter.setDataList(usersList);
                            }
                        }
                    }
                });
    }


    public void deleteClubUsers(final int gid, final ImageView imgBack, final TextView tvCancel, final TextView tvDelete){
        String[] deleteUids = clubMemberAdapter.getDeleteUserUid();
        StringBuilder deleteUidStr = new StringBuilder();
        deleteUidStr.append("[");
//        ["cfb3c2e4a01c11e78dd300163e064d3e","e8fbd8fd0f6b11e78cff3497f69570f3"]
        for (int i = 0; i < deleteUids.length; i++) {
            deleteUidStr.append("\"" + deleteUids[i] + "\"");
            if (deleteUids.length-1 != i){
                deleteUidStr.append(",");
            }
        }
        deleteUidStr.append("]");
        Log.i("TAG", "------deleteUidStr, " + deleteUidStr.toString());

        String uid = PreferencesUtils.getLoginInfo().getObj().getUid();
        String token = PreferencesUtils.getLoginInfo().getObj().getToken();
        retrofitHelper.deleteClubUsers(uid, token, gid, deleteUidStr.toString())
                .compose(RxUtil.<DeleteClubUsersBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DeleteClubUsersBean>(mView, mContext, true) {
                    @Override
                    public void onNext(DeleteClubUsersBean deleteClubUsersBean) {
                        Log.i("TAG", "------deleteClubUsersBean, " + deleteClubUsersBean.getCode() + ",, "+ deleteClubUsersBean.getMsg()
                        + " === " + deleteClubUsersBean.getObj().toString());
                        if (deleteClubUsersBean.getCode() == 1){
                            imgBack.setVisibility(View.VISIBLE);
                            tvCancel.setVisibility(View.GONE);
                            tvDelete.setText(R.string.delete);
                            setEdit(false);
                            initData(gid);
                        }
                    }
                });
    }
}
