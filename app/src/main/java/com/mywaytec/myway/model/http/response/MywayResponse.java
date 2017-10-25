package com.mywaytec.myway.model.http.response;

/**
 * Created by codeest on 16/8/28.
 */

public class MywayResponse<T> {

    private int code;
    private String msg;
    private T obj;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }
}
