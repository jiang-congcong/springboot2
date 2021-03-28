package com.cn.ccj.springboot03.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author jiangcongcong
 * @date 2021/3/25 20:01
 */
@Mapper
public interface UserDao {

    public void insertUserIdentity(Map map);

    public List<Map<String,Object>> selectUserByUserName(String userName);

}
