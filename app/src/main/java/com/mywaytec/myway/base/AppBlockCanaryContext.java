package com.mywaytec.myway.base;

import com.github.moduth.blockcanary.BlockCanaryContext;
import com.mywaytec.myway.BuildConfig;

/**
 * Created by shemh on 2017/3/8.
 */

public class AppBlockCanaryContext extends BlockCanaryContext {
    @Override
    public int getConfigBlockThreshold() {
        return 5000;
    }

    // if set true, notification will be shown, else only write log file
    @Override
    public boolean isNeedDisplay() {
        return BuildConfig.DEBUG;
    }

    // path to save log file
    @Override
    public String getLogPath() {
        return "/mnt/sdcard/";
    }
}
