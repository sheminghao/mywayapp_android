package com.mywaytec.myway.model.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by shemh on 2017/3/10.
 */


@Entity
public class ConnectedBleInfo {

    @Id(autoincrement = true)
    private Long id;

    private String bleName;

    private String address;

    private String time;

    @Generated(hash = 547182234)
    public ConnectedBleInfo(Long id, String bleName, String address, String time) {
        this.id = id;
        this.bleName = bleName;
        this.address = address;
        this.time = time;
    }

    @Generated(hash = 1018368218)
    public ConnectedBleInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBleName() {
        return this.bleName;
    }

    public void setBleName(String bleName) {
        this.bleName = bleName;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
