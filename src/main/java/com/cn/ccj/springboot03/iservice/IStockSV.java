package com.cn.ccj.springboot03.iservice;

import com.cn.ccj.springboot03.generalException.GeneralException;

import java.util.*;

/**
 * @author jiangcongcong
 * @date 2021/3/31 14:22
 */
public interface IStockSV {

    public int insertStock(Map map) throws GeneralException;

    public int insertStockRelation(Map map) throws GeneralException;

    public List<Map<String,Object>> selectStock(String goodsId) throws GeneralException;

}
