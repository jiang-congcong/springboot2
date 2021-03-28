package com.cn.ccj.springboot03.iservice;

import java.util.List;
import java.util.Map;

/**
 * @author jiangcongcong
 * @date 2021/3/28 16:19
 */
public interface IUserSV {

    public List<Map<String,Object>> selectUserByUserName(String userName) throws Exception;
}
