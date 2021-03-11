package com.cn.ccj.springboot03.iservice;

import java.util.Map;

public interface IMainSV {

    public  Map getUserMsssage(String userId);

    public int insertUserMessage(Map reqMap) throws Exception;
}
