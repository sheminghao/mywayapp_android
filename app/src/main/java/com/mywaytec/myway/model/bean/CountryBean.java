package com.mywaytec.myway.model.bean;

import com.mywaytec.myway.utils.indexlib.IndexBar.bean.BaseIndexPinyinBean;

/**
 * Created by shemh on 2017/11/15.
 */

public class CountryBean extends BaseIndexPinyinBean {

    private String code;

    private String rule;

    private String country;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    @Override
    public String getTarget() {
        return country;
    }
}
