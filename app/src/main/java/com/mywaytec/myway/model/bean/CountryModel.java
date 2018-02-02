package com.mywaytec.myway.model.bean;

import java.util.List;

/**
 * Created by shemh on 2017/11/30.
 */

public class CountryModel {

    private String name;
    private String code;
    private List<ProvinceModel> provinceList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<ProvinceModel> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<ProvinceModel> provinceList) {
        this.provinceList = provinceList;
    }
}
