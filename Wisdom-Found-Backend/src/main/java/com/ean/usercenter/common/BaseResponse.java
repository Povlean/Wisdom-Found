package com.ean.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:通用返回类
 * @author:Povlean
 *
 */
@Data
public class BaseResponse<T> implements Serializable {
    private int code;
    private T data;
    private String message;
    private String description;

    public BaseResponse(int code, T data, String message,String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data){
        this(code,data,"","");
    }

    public BaseResponse(int code,T data,String message){
        this(code,data,message,"");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(),null,errorCode.getMessage(),errorCode.getDescription());
    }

}
