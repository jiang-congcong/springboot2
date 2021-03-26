package com.cn.ccj.springboot03.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
public interface MainDao {

    public Map getUserName(String id);

    public int insertUserMessage(Map reqMap);

}
