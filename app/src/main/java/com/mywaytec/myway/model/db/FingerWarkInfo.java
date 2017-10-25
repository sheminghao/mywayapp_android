package com.mywaytec.myway.model.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by shemh on 2017/8/16.
 */

@Entity
public class FingerWarkInfo {

    @Id(autoincrement = true)
    private Long id;

    private byte fingerWarkId;

    private String uid;

    private String fingerName;

    @Generated(hash = 1801056365)
    public FingerWarkInfo(Long id, byte fingerWarkId, String uid,
            String fingerName) {
        this.id = id;
        this.fingerWarkId = fingerWarkId;
        this.uid = uid;
        this.fingerName = fingerName;
    }

    @Generated(hash = 198254093)
    public FingerWarkInfo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte getFingerWarkId() {
        return fingerWarkId;
    }

    public void setFingerWarkId(byte fingerWarkId) {
        this.fingerWarkId = fingerWarkId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFingerName() {
        return fingerName;
    }

    public void setFingerName(String fingerName) {
        this.fingerName = fingerName;
    }
}
