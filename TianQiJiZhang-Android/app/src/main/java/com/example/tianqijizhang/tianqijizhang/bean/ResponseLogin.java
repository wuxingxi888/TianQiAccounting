package com.example.tianqijizhang.tianqijizhang.bean;

import java.io.Serializable;

/**
* Created by Administrator on 2016/11/11.
        */
public class ResponseLogin implements Serializable {
    private String status;
    private String message;
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public void setMsg(String msg) {
        this.message = msg;
    }
    public String getMsg() {
        return message;
    }
}


