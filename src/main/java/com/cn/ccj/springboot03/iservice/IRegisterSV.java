package com.cn.ccj.springboot03.iservice;

import com.cn.ccj.springboot03.generalException.GeneralException;

import java.util.Map;

/**
 * @author jiangcongcong
 * @date 2021/3/25 20:00
 */
public interface IRegisterSV {

    public String insertUserIdentity(Map map) throws GeneralException;

}
