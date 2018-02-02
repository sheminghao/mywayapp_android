package com.mywaytec.myway.model.bean;

import java.util.List;

/**
 * Created by shemh on 2017/12/15.
 */

public class TestServiceBean {

    private List<String> host;
    private String currentHost;

    public List<String> getHost() {
        return host;
    }

    public void setHost(List<String> host) {
        this.host = host;
    }

    public String getCurrentHost() {
        return currentHost;
    }

    public void setCurrentHost(String currentHost) {
        this.currentHost = currentHost;
    }
}
