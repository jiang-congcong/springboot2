package com.cn.ccj.springboot03.generalException;

import com.sun.jdi.event.ExceptionEvent;

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

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage(){
        String message = super.getMessage();
        if(null==message||"".equals(message)){
            message = "未定义异常！";
        }
        return message;
    }
}
