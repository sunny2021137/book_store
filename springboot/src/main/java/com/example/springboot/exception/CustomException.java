package com.example.springboot.exception;

// 只是定義exception的格式
public class CustomException extends RuntimeException{
    private String code;
    private String msg;

    public CustomException(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
