package com.mywaytec.myway.ui.huodongXiangqing;

import android.content.Context;
import android.widget.TextView;

import com.mywaytec.myway.base.IBaseView;
import com.mywaytec.myway.model.bean.NearbyActivityBean;

/**
 * Created by shemh on 2017/3/8.
 */

public interface HuodongXiangqingView extends IBaseView {

    Context getContext();

    NearbyActivityBean.ObjBean getActivityInfo();

    TextView getCanyuTV();

    TextView getSigninTV();

    TextView getCurrentNumTV();

}
