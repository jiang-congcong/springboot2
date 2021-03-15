package com.cn.ccj.springboot03.generalException;

/**
 * @author jiangcongcong
 * @date 2021/3/15 16:01
 * 异常类
 */
public class GeneralException extends Exception {
    String errorCode;

    public GeneralException(String errorCode,String errorMessage){
        super(errorMessage);
        this.errorCode = errorCode;
    }

}
