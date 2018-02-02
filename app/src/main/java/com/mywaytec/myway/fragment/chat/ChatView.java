package com.mywaytec.myway.fragment.chat;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/11/27.
 */

public interface ChatView extends IBaseView {

    Context getContext();

    LinearLayout getClubMessageLayout();

    TextView getClubNameTV();

    TextView getTimeTV();

    TextView getContentTV();
}
