package com.mywaytec.myway.ui.trackResult;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.mywaytec.myway.base.IBaseView;

/**
 * Created by shemh on 2017/3/8.
 */

public interface TrackResultView extends IBaseView {

    Context getContext();

    ImageView getCoverImg();

    RecyclerView getPhotoRecycler();

}
