package com.ruofan.demo.entity;

import java.io.Serializable;

/**
 * @author ruofan
 * @date 2019/11/13 12:08
 */
public class WebResponse<T> implements Serializable {
    //错误码
    //0代表成功，1代表失败
    private Integer code;
    //提示信息
    private String msg;
    //具体内容
    private T data;
    //数据数量
    private Integer count;

    public WebResponse() {
    }

    public WebResponse(Integer code, T data, Integer count) {
        this.code = code;
        this.data = data;
        this.count = count;
    }

    public WebResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public WebResponse(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public WebResponse(T data, Integer count) {
        this.data = data;
        this.count = count;
    }

    public WebResponse(Integer code, String msg, T data, Integer count) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.count = count;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "WebResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", count=" + count +
                '}';
    }
}
